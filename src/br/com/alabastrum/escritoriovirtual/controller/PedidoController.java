package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ItemPedidoDTO;
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
import br.com.caelum.vraptor.Post;
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

		Pedido pedido = selecionarPedidoAberto();

		if (Util.vazio(pedido)) {
			pedido = new Pedido();
			pedido.setIdCodigo(sessaoUsuario.getUsuario().getId_Codigo());
			pedido.setCompleted(false);
		}

		if (idFranquia != null && idFranquia != 0) {
			pedido.setIdFranquia(idFranquia);
			hibernateUtil.salvarOuAtualizar(pedido);
		}

		result.include("franquia", hibernateUtil.selecionar(new Franquia(pedido.getIdFranquia())));
		result.include("categorias", hibernateUtil.buscar(new Categoria(), Order.asc("catNome")));
	}

	@Funcionalidade
	@Get("/pedido/selecionarCategoria/{idCategoria}")
	public void selecionarCategoria(Integer idCategoria) {

		Produto filtro = new Produto();
		filtro.setId_Categoria(idCategoria);
		List<Produto> produtos = hibernateUtil.buscar(filtro);
		List<ItemPedidoDTO> itensPedidoDTO = new ArrayList<ItemPedidoDTO>();
		for (Produto produto : produtos) {
			ItemPedido itemPedido = selecionarItemPedido(produto.getId_Produtos(), selecionarPedidoAberto());
			Integer quantidade = 0;
			if (itemPedido != null) {
				quantidade = itemPedido.getQuantidade();
			}
			itensPedidoDTO.add(new ItemPedidoDTO(produto, quantidade));
		}
		result.include("itensPedidoDTO", itensPedidoDTO);

		Pedido pedido = selecionarPedidoAberto();
		result.include("totais", calcularTotais(pedido));
		result.forwardTo(this).escolherProdutos(pedido.getIdFranquia());
	}

	@Funcionalidade
	@Post("/pedido/adicionarProduto/{idProduto}")
	public void adicionarProduto(String idProduto, Integer quantidade) {

		Pedido pedido = selecionarPedidoAberto();
		ItemPedido itemPedido = selecionarItemPedido(idProduto, pedido);

		if (Util.vazio(itemPedido)) {
			itemPedido = new ItemPedido();
			itemPedido.setPedido(pedido);
			itemPedido.setIdProduto(idProduto);
		}

		itemPedido.setQuantidade(quantidade);
		hibernateUtil.salvarOuAtualizar(itemPedido);

		if (quantidade <= 0) {
			hibernateUtil.deletar(itemPedido);
		}

		Produto produto = hibernateUtil.selecionar(new Produto(idProduto));
		result.forwardTo(this).selecionarCategoria(produto.getId_Categoria());
	}

	@Funcionalidade
	public void acessarCarrinho() {

		List<ItemPedidoDTO> itensPedidoDTO = new ArrayList<ItemPedidoDTO>();

		Pedido pedido = selecionarPedidoAberto();

		if (pedido != null) {

			ItemPedido filtro = new ItemPedido();
			filtro.setPedido(pedido);
			List<ItemPedido> itensPedido = hibernateUtil.buscar(filtro);

			for (ItemPedido itemPedido : itensPedido) {
				Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()));
				Integer quantidade = itemPedido.getQuantidade();
				itensPedidoDTO.add(new ItemPedidoDTO(produto, quantidade));
			}
		}
		result.include("itensPedidoDTO", itensPedidoDTO);
		result.include("totais", calcularTotais(pedido));
	}

	@Funcionalidade
	@Get("/pedido/removerProduto/{idProduto}")
	public void removerProduto(String idProduto) {

		Pedido pedido = selecionarPedidoAberto();
		ItemPedido itemPedido = selecionarItemPedido(idProduto, pedido);
		hibernateUtil.deletar(itemPedido);
		result.forwardTo(this).acessarCarrinho();
	}

	private PedidoDTO calcularTotais(Pedido pedido) {

		BigDecimal valorTotal = BigDecimal.ZERO;
		Integer totalItens = 0;
		Integer totalPontos = 0;

		ItemPedido filtro = new ItemPedido();
		filtro.setPedido(pedido);
		List<ItemPedido> itens = hibernateUtil.buscar(filtro);

		for (ItemPedido itemPedido : itens) {

			Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()));
			Integer quantidade = itemPedido.getQuantidade();

			totalItens += quantidade;
			valorTotal = valorTotal.add(produto.getPrdPreco_Unit().multiply(BigDecimal.valueOf(quantidade)));
			totalPontos += produto.getPrdPontos() * quantidade;
		}

		return new PedidoDTO(valorTotal, totalItens, totalPontos);
	}

	private Pedido selecionarPedidoAberto() {

		Pedido pedido = new Pedido();
		pedido.setIdCodigo(sessaoUsuario.getUsuario().getId_Codigo());
		pedido.setCompleted(false);
		return hibernateUtil.selecionar(pedido);
	}

	private ItemPedido selecionarItemPedido(String idProduto, Pedido pedido) {

		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setPedido(pedido);
		itemPedido.setIdProduto(idProduto);
		return hibernateUtil.selecionar(itemPedido);
	}
}
