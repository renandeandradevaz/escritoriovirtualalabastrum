package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroUnilevel;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class BonusUnilevelService {

	private HibernateUtil hibernateUtil;

	public BonusUnilevelService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public List<ExtratoDTO> obterBonificacoesUnilevel(Integer idCodigo) {

		List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

		for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaDTOEntry : new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(idCodigo).entrySet()) {

			Pedido pedidoFiltro = new Pedido();
			pedidoFiltro.setIdCodigo(arvoreHierarquicaDTOEntry.getKey());
			pedidoFiltro.setStatus("FINALIZADO");
			List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro);

			for (Pedido pedido : pedidos) {

				List<Integer> arvoreHierarquicaAscendente = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAscendente(arvoreHierarquicaDTOEntry.getKey(), pedido.getData());

				if (arvoreHierarquicaAscendente.contains(idCodigo)) {

					String posicao = new QualificacaoService(hibernateUtil).obterPosicaoNaData(idCodigo, pedido.getData());

					ParametroUnilevel parametroUnilevel = new ParametroUnilevelService(hibernateUtil).buscarParametroUnilevel(pedido.getData(), posicao, arvoreHierarquicaAscendente.indexOf(idCodigo));

					if (parametroUnilevel == null) {
						continue;
					}

					BigDecimal totalPedido = new PedidoService(hibernateUtil).calcularTotalPedidoParaBonificacao(pedido);

					BigDecimal bonus = totalPedido.multiply(parametroUnilevel.getValor()).divide(new BigDecimal(100));
					extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo())), pedido.getData(), bonus, "Unilevel"));
				}
			}
		}

		return extratos;
	}
}