package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroDivisaoLucro;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroVip;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class BonusDivisaoLucroService {

	private static final BigDecimal PORCENTAGEM_LUCRO_A_SER_DIVIDIDA = BigDecimal.valueOf(15);

	private HibernateUtil hibernateUtil;

	public BonusDivisaoLucroService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public List<ExtratoDTO> obterBonificacoesDivisaoLucro(Integer idCodigo) {

		List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

		List<ParametroVip> parametrosVip = hibernateUtil.buscar(new ParametroVip());
		for (ParametroVip parametroVip : parametrosVip) {

			GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(parametroVip.getData());
			GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(parametroVip.getData());

			if (isHabilitado(idCodigo, primeiroDiaDoMes, ultimoDiaDoMes, parametroVip)) {

				Posicao posicao = new PosicoesService(hibernateUtil).obterPosicaoPorNome(new QualificacaoService(hibernateUtil).obterPosicaoNaData(idCodigo, parametroVip.getData()));

				BigDecimal bonificacao = BigDecimal.ZERO;

				Map<Integer, BigDecimal> valoresPorPosicao = obterValoresPorPosicao(parametroVip, primeiroDiaDoMes, ultimoDiaDoMes);

				for (Entry<Integer, BigDecimal> valoresPorPosicaoEntry : valoresPorPosicao.entrySet()) {

					if (valoresPorPosicaoEntry.getKey() <= posicao.getPosicao()) {

						bonificacao = bonificacao.add(valoresPorPosicaoEntry.getValue());
					}
				}

				extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(idCodigo)), parametroVip.getData(), bonificacao, "Divisão de lucro"));
			}
		}

		return extratos;
	}

	private Map<Integer, BigDecimal> obterValoresPorPosicao(ParametroVip parametroVip, GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes) {

		List<Usuario> distribuidoresHabilitados = obterDistribuidoresHabilitados(parametroVip, primeiroDiaDoMes, ultimoDiaDoMes);

		Map<Integer, BigDecimal> valoresPorPosicao = new HashMap<Integer, BigDecimal>();

		List<Posicao> todasPosicoes = hibernateUtil.buscar(new Posicao());
		for (Posicao posicao : todasPosicoes) {

			ParametroDivisaoLucro parametroDivisaoLucro = new ParametroDivisaoLucroService(hibernateUtil).buscarParametroDivisaoLucro(parametroVip.getData(), posicao.getNome());
			if (parametroDivisaoLucro == null) {
				continue;
			}

			int quantidadeCotas = calcularQuantidadeDeCotas(parametroVip, distribuidoresHabilitados, todasPosicoes, posicao);

			if (quantidadeCotas == 0) {
				continue;
			}

			BigDecimal valorNaPosicao = getValorNaPosicao(parametroDivisaoLucro, primeiroDiaDoMes, ultimoDiaDoMes).divide(BigDecimal.valueOf(quantidadeCotas));

			valoresPorPosicao.put(posicao.getPosicao(), valorNaPosicao);
		}
		return valoresPorPosicao;
	}

	private int calcularQuantidadeDeCotas(ParametroVip parametroVip, List<Usuario> distribuidoresHabilitados, List<Posicao> todasPosicoes, Posicao posicao) {

		List<String> posicoesAcima = obterPosicoesAcima(todasPosicoes, posicao);

		int quantidadeCotas = 0;

		for (Usuario distribuidor : distribuidoresHabilitados) {

			String nomePosicao = new QualificacaoService(hibernateUtil).obterPosicaoNaData(distribuidor.getId_Codigo(), parametroVip.getData());

			if (posicoesAcima.contains(nomePosicao)) {
				quantidadeCotas++;
			}
		}
		return quantidadeCotas;
	}

	private List<String> obterPosicoesAcima(List<Posicao> todasPosicoes, Posicao posicao) {

		List<String> posicoesAcima = new ArrayList<String>();

		for (Posicao pos : todasPosicoes) {

			if (pos.getPosicao() >= posicao.getPosicao()) {
				posicoesAcima.add(pos.getNome());
			}
		}
		return posicoesAcima;
	}

	private List<Usuario> obterDistribuidoresHabilitados(ParametroVip parametroVip, GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes) {

		List<Usuario> distribuidoresHabilitados = new ArrayList<Usuario>();

		List<Usuario> todosDistribuidores = hibernateUtil.buscar(new Usuario());
		for (Usuario distribuidor : todosDistribuidores) {
			if (isHabilitado(distribuidor.getId_Codigo(), primeiroDiaDoMes, ultimoDiaDoMes, parametroVip)) {
				distribuidoresHabilitados.add(distribuidor);
			}
		}
		return distribuidoresHabilitados;
	}

	private boolean isHabilitado(Integer idCodigo, GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes, ParametroVip parametroVip) {

		if (new AtividadeService(hibernateUtil).isAtivo(idCodigo, primeiroDiaDoMes, ultimoDiaDoMes)) {

			BigDecimal totalPedidosDistribuidor = calcularTotalPedidosDistribuidor(idCodigo, primeiroDiaDoMes, ultimoDiaDoMes);

			if (totalPedidosDistribuidor.compareTo(parametroVip.getValor()) >= 0) {
				return true;
			}
		}

		return false;
	}

	private BigDecimal calcularTotalPedidosDistribuidor(Integer idCodigo, GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes) {

		BigDecimal totalPedidosDistribuidor = BigDecimal.ZERO;

		List<Pedido> pedidosDoDistribuidor = new PedidoService(hibernateUtil).getPedidosDoDistribuidor(idCodigo, primeiroDiaDoMes, ultimoDiaDoMes);

		for (Pedido pedido : pedidosDoDistribuidor) {
			totalPedidosDistribuidor = totalPedidosDistribuidor.add(new PedidoService(hibernateUtil).calcularTotalPedidoParaBonificacao(pedido));
		}

		return totalPedidosDistribuidor;
	}

	private BigDecimal getValorNaPosicao(ParametroDivisaoLucro parametroDivisaoLucro, GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes) {

		List<Pedido> pedidosTodaRede = new PedidoService(hibernateUtil).getPedidosTodaRede(primeiroDiaDoMes, ultimoDiaDoMes);

		BigDecimal totalPedidosTodaRede = BigDecimal.ZERO;

		for (Pedido pedido : pedidosTodaRede) {
			totalPedidosTodaRede = totalPedidosTodaRede.add(new PedidoService(hibernateUtil).calcularTotalPedidoIgnorandoMaterialDeApoio(pedido));
		}

		BigDecimal lucroASerDividido = totalPedidosTodaRede.multiply(PORCENTAGEM_LUCRO_A_SER_DIVIDIDA).divide(new BigDecimal(100));

		return lucroASerDividido.multiply(parametroDivisaoLucro.getValor()).divide(new BigDecimal(100));
	}
}
