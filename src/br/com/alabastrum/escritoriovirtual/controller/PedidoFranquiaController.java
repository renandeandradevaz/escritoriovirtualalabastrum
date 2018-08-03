package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
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
import br.com.caelum.vraptor.Get;
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

			List<PedidoFranquia> pedidos = hibernateUtil.buscar(filtro, restricoes, Order.desc("id"));

			List<PedidoFranquiaDTO> pedidosDTO = new ArrayList<PedidoFranquiaDTO>();
			for (PedidoFranquia pedidoFranquia : pedidos) {

				Franquia franquia = hibernateUtil.selecionar(new Franquia(pedidoFranquia.getIdFranquia()));

				pedidosDTO.add(new PedidoFranquiaDTO(pedidoFranquia, franquia, pedidoFranquia.obterValorTotal()));
			}

			result.include("searchEndpoint", "pedidosFranquia");
			result.include("pedidos", pedidosDTO);
			result.include("status", status);
		}
	}

	@Funcionalidade(administrativa = "true")
	public void todosPedidosFranquia(String status) {

		if (Util.vazio(status)) {
			status = "PENDENTE";
		}

		PedidoFranquia filtro = new PedidoFranquia();
		filtro.setStatus(status);

		List<PedidoFranquia> pedidos = hibernateUtil.buscar(filtro, Order.desc("id"));

		List<PedidoFranquiaDTO> pedidosDTO = new ArrayList<PedidoFranquiaDTO>();
		for (PedidoFranquia pedidoFranquia : pedidos) {

			Franquia franquia = hibernateUtil.selecionar(new Franquia(pedidoFranquia.getIdFranquia()));

			pedidosDTO.add(new PedidoFranquiaDTO(pedidoFranquia, franquia, pedidoFranquia.obterValorTotal()));
		}

		result.include("searchEndpoint", "todosPedidosFranquia");
		result.include("pedidos", pedidosDTO);
		result.include("status", status);

		result.forwardTo("/WEB-INF/jsp//pedidoFranquia/pedidosFranquia.jsp");
	}

	@Funcionalidade
	public void novoPedido() {

		if (this.sessaoUsuario.getUsuario().getDonoDeFranquia()) {

			this.sessaoGeral.adicionar("idPedido", null);

			Franquia franquiaFiltro = new Franquia();
			franquiaFiltro.setId_Codigo(this.sessaoUsuario.getUsuario().getId_Codigo());
			result.include("franquias", hibernateUtil.buscar(franquiaFiltro));
		}
	}

	@Funcionalidade
	public void listarProdutos(Integer idFranquia) {

		

			this.sessaoGeral.adicionar("idFranquia", idFranquia);

			List<Produto> produtos = hibernateUtil.buscar(new Produto(), Order.asc("id_Categoria"));

			List<ItemPedidoDTO> itens = new ArrayList<ItemPedidoDTO>();

			for (Produto produto : produtos) {

				itens.add(new ItemPedidoDTO(produto, 0, calcularPrecoUnitario(produto), new EstoqueService(hibernateUtil).getQuantidadeEmEstoque(produto.getId_Produtos(), idFranquia), produto.obterCategoria()));
			}

			result.include("permitirAlterar", true);
			result.include("itens", itens);
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

		if (this.sessaoUsuario.getUsuario().getDonoDeFranquia()) {

			if (this.sessaoGeral.getValor("idPedido") == null) {

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

			} else {

				Integer idPedido = (Integer) this.sessaoGeral.getValor("idPedido");

				ItemPedidoFranquia filtro = new ItemPedidoFranquia();
				filtro.setPedidoFranquia(new PedidoFranquia(idPedido));
				List<ItemPedidoFranquia> itens = hibernateUtil.buscar(filtro);

				for (ItemPedidoFranquia item : itens) {

					Integer quantidade = Integer.valueOf(quantidades.get(item.getIdProduto()));

					if (quantidade < 0) {
						quantidade = 0;
					}

					if (quantidade == 0) {
						hibernateUtil.deletar(item);
					}

					if (quantidade > 0) {
						item.setQuantidade(quantidade);
						hibernateUtil.salvarOuAtualizar(item);
					}
				}
			}
		}

		result.forwardTo(this).pedidosFranquia(null);
	}

	@Funcionalidade
	@Get("/pedidoFranquia/verItens/{idPedido}")
	public void verItens(Integer idPedido) {

		

			this.sessaoGeral.adicionar("idPedido", idPedido);

			List<ItemPedidoDTO> itensPedidoDTO = new ArrayList<ItemPedidoDTO>();

			PedidoFranquia pedidoFranquia = hibernateUtil.selecionar(new PedidoFranquia(idPedido));

			ItemPedidoFranquia filtro = new ItemPedidoFranquia();
			filtro.setPedidoFranquia(pedidoFranquia);
			List<ItemPedidoFranquia> itens = hibernateUtil.buscar(filtro);

			for (ItemPedidoFranquia item : itens) {

				Produto produto = hibernateUtil.selecionar(new Produto(item.getIdProduto()), MatchMode.EXACT);
				itensPedidoDTO.add(new ItemPedidoDTO(produto, item.getQuantidade(), item.getPrecoUnitario(), new EstoqueService(hibernateUtil).getQuantidadeEmEstoque(produto.getId_Produtos(), pedidoFranquia.getIdFranquia()), produto.obterCategoria()));
			}

			result.include("itens", itensPedidoDTO);
			result.include("permitirAlterar", pedidoFranquia.getStatus().equals("PENDENTE"));
			result.forwardTo("/WEB-INF/jsp//pedidoFranquia/listarProdutos.jsp");
		
	}

	@Funcionalidade(administrativa = "true")
	@Get("/pedidoFranquia/marcarComoEntregue/{idPedido}")
	public void marcarComoEntregue(Integer idPedido) {

		PedidoFranquia pedidoFranquia = hibernateUtil.selecionar(new PedidoFranquia(idPedido));
		ItemPedidoFranquia filtro = new ItemPedidoFranquia();
		filtro.setPedidoFranquia(pedidoFranquia);
		List<ItemPedidoFranquia> itens = hibernateUtil.buscar(filtro);

		for (ItemPedidoFranquia item : itens) {
			new EstoqueService(hibernateUtil).adicionarAoEstoque(item.getIdProduto(), pedidoFranquia.getIdFranquia(), item.getQuantidade());
		}

		pedidoFranquia.setStatus("ENTREGUE");
		hibernateUtil.salvarOuAtualizar(pedidoFranquia);

		result.forwardTo(this).pedidosFranquia(null);
	}
}
