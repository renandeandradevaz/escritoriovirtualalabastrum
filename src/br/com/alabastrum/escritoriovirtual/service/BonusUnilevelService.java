package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.criterion.MatchMode;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ItemPedido;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroUnilevel;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Produto;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class BonusUnilevelService {

	private HibernateUtil hibernateUtil;

	public BonusUnilevelService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public List<ExtratoDTO> obterBonificacoesUnilevel(Integer idCodigo) {

		Usuario usuario = hibernateUtil.selecionar(new Usuario(idCodigo));

		List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

		for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaDTOEntry : new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(idCodigo).entrySet()) {

			Pedido pedidoFiltro = new Pedido();
			pedidoFiltro.setIdCodigo(arvoreHierarquicaDTOEntry.getKey());
			pedidoFiltro.setStatus("PAGO");
			List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro);

			for (Pedido pedido : pedidos) {

				List<Integer> arvoreHierarquicaAtivaAscendente = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAtivaAscendente(arvoreHierarquicaDTOEntry.getKey(), pedido.getData());

				if (arvoreHierarquicaAtivaAscendente.contains(idCodigo)) {

					ParametroUnilevel parametroUnilevel = new ParametroUnilevelService(hibernateUtil).buscarParametroUnilevel(pedido.getData(), usuario.getPosAtual(), arvoreHierarquicaAtivaAscendente.indexOf(idCodigo));

					if (parametroUnilevel == null) {
						continue;
					}

					BigDecimal totalPedido = calcularTotalPedido(pedido);

					BigDecimal bonus = totalPedido.multiply(parametroUnilevel.getValor()).divide(new BigDecimal(100));
					extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo())), pedido.getData(), bonus, "Unilevel"));
				}
			}
		}

		return extratos;
	}

	private BigDecimal calcularTotalPedido(Pedido pedido) {

		BigDecimal totalPedido = BigDecimal.ZERO;

		ItemPedido itemPedidoFiltro = new ItemPedido();
		itemPedidoFiltro.setPedido(pedido);
		List<ItemPedido> itensPedido = hibernateUtil.buscar(itemPedidoFiltro);

		for (ItemPedido itemPedido : itensPedido) {

			Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);

			if (produto.getPrdComissionado().equals("1") || produto.getPrdPromocao().equals("1")) {
				totalPedido = totalPedido.add(itemPedido.getPrecoUnitario().multiply(BigDecimal.valueOf(itemPedido.getQuantidade())));
			}
		}

		return totalPedido;
	}
}