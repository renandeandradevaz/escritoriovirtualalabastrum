package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

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

			if (new AtividadeService(hibernateUtil).isAtivo(idCodigo, primeiroDiaDoMes, ultimoDiaDoMes)) {

				BigDecimal totalPedidosDistribuidor = calcularTotalPedidosDistribuidor(idCodigo, primeiroDiaDoMes, ultimoDiaDoMes);

				if (totalPedidosDistribuidor.compareTo(parametroVip.getValor()) >= 0) {

					String nomePosicao = new QualificacaoService(hibernateUtil).obterPosicaoNaData(idCodigo, parametroVip.getData());

					ParametroDivisaoLucro parametroDivisaoLucro = new ParametroDivisaoLucroService(hibernateUtil).buscarParametroDivisaoLucro(parametroVip.getData(), nomePosicao);
					if (parametroDivisaoLucro == null) {
						continue;
					}

					BigDecimal valorNaPosicao = getValorNaPosicao(parametroDivisaoLucro, primeiroDiaDoMes, ultimoDiaDoMes);

					int quantidadeCotas = calcularQuantidadeDeCotas(primeiroDiaDoMes, ultimoDiaDoMes, parametroVip, obterPosicoesAcima(nomePosicao));

				}
			}
		}

		// extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new
		// Usuario(pedido.getIdCodigo())), pedido.getData(), bonus,
		// "xxxxxxxxxxxxxxxxxxx"));
		return extratos;
	}

	private Integer calcularQuantidadeDeCotas(GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes, ParametroVip parametroVip, List<String> posicoesAcima) {

		int quantidadeCotas = 0;

		List<Usuario> todosDistribuidores = hibernateUtil.buscar(new Usuario());

		for (Usuario distribuidor : todosDistribuidores) {

			if (distribuidor.getId_Codigo() > 1) {

				if (new AtividadeService(hibernateUtil).isAtivo(distribuidor.getId_Codigo(), primeiroDiaDoMes, ultimoDiaDoMes)) {

					BigDecimal totalPedidosDistribuidor = calcularTotalPedidosDistribuidor(distribuidor.getId_Codigo(), primeiroDiaDoMes, ultimoDiaDoMes);

					if (totalPedidosDistribuidor.compareTo(parametroVip.getValor()) >= 0) {

						String nomePosicao = new QualificacaoService(hibernateUtil).obterPosicaoNaData(distribuidor.getId_Codigo(), parametroVip.getData());

						if (posicoesAcima.contains(nomePosicao)) {
							quantidadeCotas++;
						}
					}
				}
			}
		}

		return quantidadeCotas;
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

	private List<String> obterPosicoesAcima(String nomePosicao) {

		List<String> posicoesAcima = new ArrayList<String>();

		List<Posicao> todasPosicoes = hibernateUtil.buscar(new Posicao());

		Posicao posicao = new PosicoesService(hibernateUtil).obterPosicaoPorNome(nomePosicao);

		for (Posicao pos : todasPosicoes) {
			if (pos.getPosicao() >= posicao.getPosicao()) {
				posicoesAcima.add(pos.getNome());
			}
		}

		return posicoesAcima;
	}
}
