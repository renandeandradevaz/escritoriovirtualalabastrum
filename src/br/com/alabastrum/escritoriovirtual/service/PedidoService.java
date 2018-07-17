package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ItemPedido;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Produto;

public class PedidoService {

	private HibernateUtil hibernateUtil;

	public PedidoService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public List<Pedido> getPedidosDoDistribuidor(Integer idCodigo, GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes) {

		List<Criterion> restricoes = new ArrayList<Criterion>();
		restricoes.add(Restrictions.between("data", primeiroDiaDoMes, ultimoDiaDoMes));
		Pedido pedidoFiltro = new Pedido();
		pedidoFiltro.setIdCodigo(idCodigo);
		pedidoFiltro.setStatus("FINALIZADO");
		return hibernateUtil.buscar(pedidoFiltro, restricoes);
	}

	public BigDecimal calcularTotalPedidoParaBonificacao(Pedido pedido) {

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