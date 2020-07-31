package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.dto.PedidoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ItemPedido;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Produto;

public class PedidoService {

    public static final String RECOMPRA = "recompra";
    public static final String ATIVIDADE = "atividade";
    public static final String ADESAO = "adesao";
    public static final String ID_USUARIO_PEDIDO = "idUsuarioPedido";
    public static final String ID_USUARIO_LOJA_PESSOAL = "idUsuarioLojaPessoal";
    public static final String FINALIZADO = "FINALIZADO";
    public static final String PENDENTE = "PENDENTE";
    public static final String CANCELADO = "CANCELADO";

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

    public PedidoDTO calcularTotais(Pedido pedido) {

	BigDecimal valorTotal = BigDecimal.ZERO;
	BigDecimal totalPontos = BigDecimal.ZERO;
	Integer totalItens = 0;

	List<ItemPedido> itens = new PedidoService(hibernateUtil).listarItensPedido(pedido);

	for (ItemPedido itemPedido : itens) {

	    Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);
	    Integer quantidade = itemPedido.getQuantidade();

	    totalItens += quantidade;
	    valorTotal = valorTotal.add(itemPedido.getPrecoUnitario().multiply(BigDecimal.valueOf(quantidade)));
	    totalPontos = totalPontos.add(produto.getPrdPontos().multiply(new BigDecimal(quantidade)));
	}

	return new PedidoDTO(pedido, null, valorTotal, totalItens, totalPontos);
    }

    public List<ItemPedido> listarItensPedido(Pedido pedido) {

	ItemPedido filtro = new ItemPedido();
	filtro.setPedido(pedido);
	return hibernateUtil.buscar(filtro);
    }

    public void cancelarPedido(Pedido pedido) {

	for (ItemPedido itemPedido : listarItensPedido(pedido)) {
	    new EstoqueService(hibernateUtil).adicionarAoEstoque(itemPedido.getIdProduto(), pedido.getIdFranquia(), itemPedido.getQuantidade());
	}
    }
}