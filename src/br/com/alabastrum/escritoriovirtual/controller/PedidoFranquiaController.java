package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ItemPedidoDTO;
import br.com.alabastrum.escritoriovirtual.dto.PedidoFranquiaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.ItemPedidoFranquia;
import br.com.alabastrum.escritoriovirtual.modelo.PedidoFranquia;
import br.com.alabastrum.escritoriovirtual.modelo.Produto;
import br.com.alabastrum.escritoriovirtual.service.EstoqueService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoGeral;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class PedidoFranquiaController {

	private Result result;
	private HibernateUtil hibernateUtil;
	private SessaoUsuario sessaoUsuario;
	private SessaoGeral sessaoGeral;

	public PedidoFranquiaController(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario, SessaoGeral sessaoGeral) {
		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.sessaoUsuario = sessaoUsuario;
		this.sessaoGeral = sessaoGeral;
	}

	@Funcionalidade
	public void pedidosFranquia(String status) {

		if (this.sessaoUsuario.getUsuario().getDonoDeFranquia()) {

			if (Util.vazio(status)) {
				status = "PENDENTE";
			}

			PedidoFranquia filtro = new PedidoFranquia();
			filtro.setStatus(status);

			List<Criterion> restricoes = new ArrayList<Criterion>();

			Franquia franquiaFiltro = new Franquia();
			franquiaFiltro.setId_Codigo(this.sessaoUsuario.getUsuario().getId_Codigo());
			List<Franquia> franquias = hibernateUtil.buscar(franquiaFiltro);

			List<Integer> idFranquias = new ArrayList<Integer>();

			for (Franquia franquia : franquias) {
				idFranquias.add(franquia.getId_Estoque());
			}

			restricoes.add(Restrictions.in("idFranquia", idFranquias));

			List<PedidoFranquia> pedidos = hibernateUtil.buscar(filtro, restricoes);

			List<PedidoFranquiaDTO> pedidosDTO = new ArrayList<PedidoFranquiaDTO>();
			for (PedidoFranquia pedidoFranquia : pedidos) {

				Franquia franquia = hibernateUtil.selecionar(new Franquia(pedidoFranquia.getIdFranquia()));

				pedidosDTO.add(new PedidoFranquiaDTO(pedidoFranquia, franquia, pedidoFranquia.obterValorTotal()));
			}

			result.include("pedidos", pedidosDTO);
			result.include("status", status);
		}
	}

	@Funcionalidade
	public void novoPedido() {

		if (this.sessaoUsuario.getUsuario().getDonoDeFranquia()) {

			Franquia franquiaFiltro = new Franquia();
			franquiaFiltro.setId_Codigo(this.sessaoUsuario.getUsuario().getId_Codigo());
			result.include("franquias", hibernateUtil.buscar(franquiaFiltro));
		}
	}

	@Funcionalidade
	public void listarProdutos(Integer idFranquia) {

		if (this.sessaoUsuario.getUsuario().getDonoDeFranquia()) {

			this.sessaoGeral.adicionar("idFranquia", idFranquia);

			List<Produto> produtos = hibernateUtil.buscar(new Produto(), Order.asc("id_Categoria"));

			List<ItemPedidoDTO> itens = new ArrayList<ItemPedidoDTO>();

			for (Produto produto : produtos) {

				itens.add(new ItemPedidoDTO(produto, 0, calcularPrecoUnitario(produto), new EstoqueService(hibernateUtil).getQuantidadeEmEstoque(produto.getId_Produtos(), idFranquia), produto.obterCategoria()));
			}

			result.include("itens", itens);
		}
	}

	private BigDecimal calcularPrecoUnitario(Produto produto) {

		BigDecimal precoUnitario = produto.getPrdPreco_Unit();

		if (produto.getPrdMatApoio().equals("0")) {
			precoUnitario = precoUnitario.subtract(precoUnitario.multiply(new BigDecimal("0.1")));
		}
		return precoUnitario;
	}

	@Funcionalidade
	public void concluirPedido(HashMap<String, String> quantidades) {

		Integer idFranquia = (Integer) this.sessaoGeral.getValor("idFranquia");

		PedidoFranquia pedidoFranquia = new PedidoFranquia();
		pedidoFranquia.setData(new GregorianCalendar());
		pedidoFranquia.setIdFranquia(idFranquia);
		pedidoFranquia.setStatus("PENDENTE");
		hibernateUtil.salvarOuAtualizar(pedidoFranquia);

		for (Entry<String, String> quantidadeEntry : quantidades.entrySet()) {

			Integer quantidade = Integer.valueOf(quantidadeEntry.getValue());

			if (quantidade > 0) {

				Produto produto = hibernateUtil.selecionar(new Produto(String.valueOf(quantidadeEntry.getKey())));

				ItemPedidoFranquia itemPedidoFranquia = new ItemPedidoFranquia();
				itemPedidoFranquia.setPedidoFranquia(pedidoFranquia);
				itemPedidoFranquia.setIdProduto(produto.getId_Produtos());
				itemPedidoFranquia.setQuantidade(quantidade);
				itemPedidoFranquia.setPrecoUnitario(calcularPrecoUnitario(produto));
				hibernateUtil.salvarOuAtualizar(itemPedidoFranquia);
			}
		}

		result.forwardTo(this).pedidosFranquia(null);
	}
}