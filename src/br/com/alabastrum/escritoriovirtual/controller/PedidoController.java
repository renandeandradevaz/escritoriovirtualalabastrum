package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.MatchMode;
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
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoGeral;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class PedidoController {

	private static final String ID_USUARIO_PEDIDO = "idUsuarioPedido";
	private static final String PENDENTE = "PENDENTE";
	private static final String PAGO = "PAGO";

	private Result result;
	private HibernateUtil hibernateUtil;
	private SessaoUsuario sessaoUsuario;
	private SessaoGeral sessaoGeral;
	private Validator validator;

	public PedidoController(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario, Validator validator, SessaoGeral sessaoGeral) {
		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.sessaoUsuario = sessaoUsuario;
		this.sessaoGeral = sessaoGeral;
		this.validator = validator;
	}

	@Funcionalidade
	public void acessarTelaNovoPedido() {
		result.include("franquias", hibernateUtil.buscar(new Franquia()));
	}

	@Funcionalidade
	public void escolherProdutos(Integer idFranquia, Integer idCodigo) {

		if (idCodigo == null || idCodigo == 0) {
			idCodigo = this.sessaoUsuario.getUsuario().getId_Codigo();
		}
		this.sessaoGeral.adicionar(ID_USUARIO_PEDIDO, idCodigo);

		if (idFranquia == null || idFranquia == 0) {
			validator.add(new ValidationMessage("Selecione uma franquia", "Erro"));
			validator.onErrorRedirectTo(this).acessarTelaNovoPedido();
			return;
		}

		Pedido pedido = selecionarPedidoAberto();

		if (Util.vazio(pedido)) {
			pedido = new Pedido();
			pedido.setIdCodigo(idCodigo);
			pedido.setCompleted(false);
			pedido.setStatus(PENDENTE);
		}

		pedido.setIdFranquia(idFranquia);
		hibernateUtil.salvarOuAtualizar(pedido);

		result.include("franquia", hibernateUtil.selecionar(new Franquia(pedido.getIdFranquia())));
		result.include("categorias", hibernateUtil.buscar(new Categoria(), Order.asc("catNome")));
	}

	@Funcionalidade
	@Get("/pedido/selecionarCategoria/{idCategoria}")
	public void selecionarCategoria(Integer idCategoria) {

		Produto filtro = new Produto();
		filtro.setId_Categoria(idCategoria);
		filtro.setDisponivel("1");
		List<Produto> produtos = hibernateUtil.buscar(filtro);
		List<ItemPedidoDTO> itensPedidoDTO = new ArrayList<ItemPedidoDTO>();
		for (Produto produto : produtos) {
			ItemPedido itemPedido = selecionarItemPedido(produto.getId_Produtos(), selecionarPedidoAberto());
			Integer quantidade = 0;
			if (itemPedido != null) {
				quantidade = itemPedido.getQuantidade();
			}
			itensPedidoDTO.add(new ItemPedidoDTO(produto, quantidade, produto.getPrdPreco_Unit()));
		}

		result.include("itensPedidoDTO", itensPedidoDTO);

		Pedido pedido = selecionarPedidoAberto();
		result.include("totais", calcularTotais(pedido));
		result.forwardTo(this).escolherProdutos(pedido.getIdFranquia(), pedido.getIdCodigo());
	}

	@Funcionalidade
	@Post("/pedido/adicionarProduto/{idProduto}")
	public void adicionarProduto(String idProduto, Integer quantidade) {

		Pedido pedido = selecionarPedidoAberto();
		ItemPedido itemPedido = selecionarItemPedido(idProduto, pedido);
		Produto produto = hibernateUtil.selecionar(new Produto(idProduto), MatchMode.EXACT);

		if (Util.vazio(itemPedido)) {
			itemPedido = new ItemPedido();
			itemPedido.setPedido(pedido);
			itemPedido.setIdProduto(idProduto);
			itemPedido.setPrecoUnitario(produto.getPrdPreco_Unit());
		}

		itemPedido.setQuantidade(quantidade);
		hibernateUtil.salvarOuAtualizar(itemPedido);

		if (quantidade <= 0) {
			hibernateUtil.deletar(itemPedido);
		}

		result.forwardTo(this).selecionarCategoria(produto.getId_Categoria());
	}

	@Funcionalidade
	public void acessarCarrinho() {

		List<ItemPedidoDTO> itensPedidoDTO = new ArrayList<ItemPedidoDTO>();

		Pedido pedido = selecionarPedidoAberto();

		if (pedido != null) {

			for (ItemPedido itemPedido : listarItensPedido(pedido)) {
				Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);
				Integer quantidade = itemPedido.getQuantidade();
				itensPedidoDTO.add(new ItemPedidoDTO(produto, quantidade, produto.getPrdPreco_Unit()));
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

	@Funcionalidade
	public void concluirPedido() throws Exception {

		Pedido pedido = selecionarPedidoAberto();
		pedido.setCompleted(true);
		pedido.setData(new GregorianCalendar());
		hibernateUtil.salvarOuAtualizar(pedido);

		result.include("sucesso", "Pedido feito com sucesso. Você pode buscar no endereço escolhido. De segunda a sexta-feira. De 9h as 17h.");
		result.forwardTo(this).meusPedidos();
	}

	@Funcionalidade
	public void meusPedidos() throws Exception {

		Integer idCodigo = (Integer) this.sessaoGeral.getValor(ID_USUARIO_PEDIDO);

		if (idCodigo == null || idCodigo == 0) {
			idCodigo = this.sessaoUsuario.getUsuario().getId_Codigo();
		}

		montarPedidosDTO(null, idCodigo);
	}

	@Funcionalidade(administrativa = "true")
	public void pesquisarPedidosDosDistribuidores(String status, Integer idCodigo) {

		if (Util.vazio(status)) {
			status = PENDENTE;
		}

		montarPedidosDTO(status, idCodigo);

		result.include("idCodigo", idCodigo);
		result.include("status", status);
	}

	@Funcionalidade(administrativa = "true")
	@Get("/pedido/alterarStatus/{idPedido}/{status}")
	public void alterarStatus(String status, Integer idPedido) throws Exception {

		Pedido pedido = hibernateUtil.selecionar(new Pedido(idPedido));

		if (pedido.getStatus().equals(PENDENTE) && status.equals(PAGO)) {

			String textoArquivo = "id_Codigo=" + pedido.getIdCodigo() + "\r\n";
			textoArquivo += "id_CDA=" + pedido.getIdFranquia() + "\r\n";
			for (ItemPedido itemPedido : listarItensPedido(pedido)) {
				textoArquivo += itemPedido.getIdProduto() + "=" + itemPedido.getQuantidade() + "\r\n";
			}
			ArquivoService.criarArquivoNoDisco(textoArquivo, ArquivoService.PASTA_PEDIDOS);
		}

		pedido.setStatus(status);
		hibernateUtil.salvarOuAtualizar(pedido);

		result.forwardTo(this).pesquisarPedidosDosDistribuidores(null, null);
	}

	@Funcionalidade
	@Get("/pedido/verItens/{idPedido}")
	public void verItens(Integer idPedido) {

		List<ItemPedidoDTO> itensPedidoDTO = new ArrayList<ItemPedidoDTO>();

		for (ItemPedido itemPedido : listarItensPedido((Pedido) hibernateUtil.selecionar(new Pedido(idPedido)))) {
			Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);
			Integer quantidade = itemPedido.getQuantidade();
			itensPedidoDTO.add(new ItemPedidoDTO(produto, quantidade, produto.getPrdPreco_Unit()));
		}

		result.include("itensPedidoDTO", itensPedidoDTO);
	}

	private void montarPedidosDTO(String status, Integer idCodigo) {

		Pedido pedidoFiltro = new Pedido();
		pedidoFiltro.setIdCodigo(idCodigo);
		pedidoFiltro.setStatus(status);
		pedidoFiltro.setCompleted(true);
		List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro);
		List<PedidoDTO> pedidosDTO = new ArrayList<PedidoDTO>();
		for (Pedido pedido : pedidos) {
			pedidosDTO.add(new PedidoDTO(pedido, (Franquia) hibernateUtil.selecionar(new Franquia(pedido.getIdFranquia())), calcularTotais(pedido).getValorTotal()));
		}
		result.include("pedidosDTO", pedidosDTO);
	}

	private PedidoDTO calcularTotais(Pedido pedido) {

		BigDecimal valorTotal = BigDecimal.ZERO;
		Integer totalItens = 0;
		Integer totalPontos = 0;

		List<ItemPedido> itens = listarItensPedido(pedido);

		for (ItemPedido itemPedido : itens) {

			Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);
			Integer quantidade = itemPedido.getQuantidade();

			totalItens += quantidade;
			valorTotal = valorTotal.add(itemPedido.getPrecoUnitario().multiply(BigDecimal.valueOf(quantidade)));
			totalPontos += produto.getPrdPontos() * quantidade;
		}

		return new PedidoDTO(valorTotal, totalItens, totalPontos);
	}

	private List<ItemPedido> listarItensPedido(Pedido pedido) {

		ItemPedido filtro = new ItemPedido();
		filtro.setPedido(pedido);
		return hibernateUtil.buscar(filtro);
	}

	private Pedido selecionarPedidoAberto() {

		Integer idCodigo = (Integer) this.sessaoGeral.getValor(ID_USUARIO_PEDIDO);

		if (idCodigo == null || idCodigo == 0) {
			idCodigo = this.sessaoUsuario.getUsuario().getId_Codigo();
		}

		Pedido pedido = new Pedido();
		pedido.setIdCodigo(idCodigo);
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
