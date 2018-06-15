package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;

import org.hibernate.criterion.Order;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.PedidoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Categoria;
import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.ItemPedido;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Produto;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class PedidoController {

	private Result result;
	private HibernateUtil hibernateUtil;
	private SessaoUsuario sessaoUsuario;

	public PedidoController(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario) {
		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.sessaoUsuario = sessaoUsuario;
	}

	@Funcionalidade
	public void acessarTelaNovoPedido() {
		result.include("franquias", hibernateUtil.buscar(new Franquia()));
	}

	@Funcionalidade
	public void escolherProdutos(Integer idFranquia) {

		Pedido pedido = selecionarPedido();

		if (Util.vazio(pedido)) {
			pedido = new Pedido();
			pedido.setIdCodigo(sessaoUsuario.getUsuario().getId_Codigo());
			pedido.setCompleted(false);
		}

		pedido.setIdFranquia(idFranquia);
		hibernateUtil.salvarOuAtualizar(pedido);

		result.include("franquia", hibernateUtil.selecionar(new Franquia(idFranquia)));
		result.include("categorias", hibernateUtil.buscar(new Categoria(), Order.asc("catNome")));
	}

	@Funcionalidade
	@Get("/pedido/selecionarCategoria/{idCategoria}")
	public void selecionarCategoria(Integer idCategoria) {

		Produto filtro = new Produto();
		filtro.setId_Categoria(idCategoria);
		result.include("produtos", hibernateUtil.buscar(filtro));

		Pedido pedido = selecionarPedido();
		result.include("totais", calcularTotais(pedido));
		result.forwardTo(this).escolherProdutos(pedido.getIdFranquia());
	}

	private PedidoDTO calcularTotais(Pedido pedido) {

		BigDecimal valorTotal = BigDecimal.ZERO;
		Integer totalItens = 0;
		Integer totalPontos = 0;

		for (ItemPedido itemPedido : pedido.getItens()) {

			Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()));
			Integer quantidade = itemPedido.getQuantidade();

			totalItens += quantidade;
			valorTotal = valorTotal.add(produto.getPrdPreco_Unit().multiply(BigDecimal.valueOf(quantidade)));
			totalPontos += produto.getPrdPontos() * quantidade;
		}

		return new PedidoDTO(valorTotal, totalItens, totalPontos);
	}

	private Pedido selecionarPedido() {

		Pedido pedido = new Pedido();
		pedido.setIdCodigo(sessaoUsuario.getUsuario().getId_Codigo());
		pedido.setCompleted(false);
		return hibernateUtil.selecionar(pedido);
	}
}
