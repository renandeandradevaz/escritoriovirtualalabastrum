package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ItemPedidoDTO;
import br.com.alabastrum.escritoriovirtual.dto.PedidoDTO;
import br.com.alabastrum.escritoriovirtual.dto.SaldoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Categoria;
import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.ItemPedido;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Produto;
import br.com.alabastrum.escritoriovirtual.modelo.Transferencia;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.alabastrum.escritoriovirtual.service.EstoqueService;
import br.com.alabastrum.escritoriovirtual.service.ExtratoService;
import br.com.alabastrum.escritoriovirtual.service.PedidoService;
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

		Franquia franquiaFiltro = new Franquia();

		if (this.sessaoUsuario.getUsuario().getDonoDeFranquia()) {
			franquiaFiltro.setId_Codigo(this.sessaoUsuario.getUsuario().getId_Codigo());
		}

		result.include("franquias", hibernateUtil.buscar(franquiaFiltro));
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
			pedido.setStatus("PENDENTE");
		}

		pedido.setIdFranquia(idFranquia);
		hibernateUtil.salvarOuAtualizar(pedido);

		result.include("franquia", hibernateUtil.selecionar(new Franquia(pedido.getIdFranquia())));
		result.include("categorias", hibernateUtil.buscar(new Categoria(), Order.asc("catNome")));
	}

	@Funcionalidade
	@Get("/pedido/selecionarCategoria/{idCategoria}")
	public void selecionarCategoria(Integer idCategoria) {

		Pedido pedido = selecionarPedidoAberto();

		Produto filtro = new Produto();
		filtro.setId_Categoria(idCategoria);

		List<Produto> produtos = hibernateUtil.buscar(filtro);
		List<ItemPedidoDTO> itensPedidoDTO = new ArrayList<ItemPedidoDTO>();
		for (Produto produto : produtos) {

			int quantidadeEmEstoque = new EstoqueService(hibernateUtil).getQuantidadeEmEstoque(produto.getId_Produtos(), pedido.getIdFranquia());

			if (quantidadeEmEstoque > 0) {

				ItemPedido itemPedido = selecionarItemPedido(produto.getId_Produtos(), selecionarPedidoAberto());
				Integer quantidade = 0;
				if (itemPedido != null) {
					quantidade = itemPedido.getQuantidade();
				}

				itensPedidoDTO.add(new ItemPedidoDTO(produto, quantidade, produto.getPrdPreco_Unit(), quantidadeEmEstoque));
			}
		}

		result.include("itensPedidoDTO", itensPedidoDTO);

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

			for (ItemPedido itemPedido : new PedidoService(hibernateUtil).listarItensPedido(pedido)) {
				Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);
				Integer quantidade = itemPedido.getQuantidade();
				itensPedidoDTO.add(new ItemPedidoDTO(produto, quantidade, produto.getPrdPreco_Unit(), 0));
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
	public void escolherFormaDePagamento() {

		Pedido pedido = selecionarPedidoAberto();
		verificarPagamentoComSaldoHabilitado(pedido);
	}

	private void verificarPagamentoComSaldoHabilitado(Pedido pedido) {

		boolean pagamentoComSaldoHabilitado = false;

		if (pedido.getIdCodigo().equals(this.sessaoUsuario.getUsuario().getId_Codigo()) || this.sessaoUsuario.getUsuario().obterInformacoesFixasUsuario().getAdministrador()) {
			pagamentoComSaldoHabilitado = true;
		}

		result.include("pagamentoComSaldoHabilitado", pagamentoComSaldoHabilitado);
	}

	@Funcionalidade
	public void concluirPedido(String formaDePagamento) {

		if (formaDePagamento == null || formaDePagamento.equals("")) {
			validator.add(new ValidationMessage("Selecione a forma de pagamento", "Erro"));
			validator.onErrorRedirectTo(this).escolherFormaDePagamento();
			return;
		}

		Pedido pedido = selecionarPedidoAberto();

		for (ItemPedido itemPedido : new PedidoService(hibernateUtil).listarItensPedido(pedido)) {

			Integer quantidadeEmEstoque = new EstoqueService(hibernateUtil).getQuantidadeEmEstoque(itemPedido.getIdProduto(), pedido.getIdFranquia());

			if (itemPedido.getQuantidade() > quantidadeEmEstoque) {

				Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()));

				validator.add(new ValidationMessage("O produto " + produto.getPrdNome() + " possui " + quantidadeEmEstoque + " unidades no estoque", "Erro"));
				validator.onErrorRedirectTo(this).acessarCarrinho();
				return;
			}
		}

		if (formaDePagamento.equals("pagarComSaldo")) {

			BigDecimal saldoLiberado = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(pedido.getIdCodigo(), Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR)).getSaldoLiberado();
			BigDecimal totalPedido = calcularTotais(pedido).getValorTotal();

			if (totalPedido.compareTo(saldoLiberado) > 0) {
				validator.add(new ValidationMessage("você não possui saldo suficiente. Saldo atual: R$" + String.format("%.2f", saldoLiberado), "Erro"));
				validator.onErrorRedirectTo(this).escolherFormaDePagamento();
				return;
			}

			salvarTransferencia(totalPedido, pedido);
			pedido.setStatus("PAGO");
		}

		pedido.setCompleted(true);
		pedido.setData(new GregorianCalendar());
		hibernateUtil.salvarOuAtualizar(pedido);

		for (ItemPedido itemPedido : new PedidoService(hibernateUtil).listarItensPedido(pedido)) {
			new EstoqueService(hibernateUtil).retirarDoEstoque(itemPedido.getIdProduto(), pedido.getIdFranquia(), itemPedido.getQuantidade());
		}

		if (this.sessaoUsuario.getUsuario().getDonoDeFranquia()) {

			result.forwardTo(this).pesquisarPedidosDosDistribuidores(null, null);

		} else {
			result.include("sucesso", "Pedido feito com sucesso. Você pode buscar no endereço escolhido. De segunda a sexta-feira. De 9h as 17h.");
			result.forwardTo(this).meusPedidos();
		}
	}

	@Funcionalidade
	public void meusPedidos() {

		Integer idCodigo = (Integer) this.sessaoGeral.getValor(ID_USUARIO_PEDIDO);

		if (idCodigo == null || idCodigo == 0) {
			idCodigo = this.sessaoUsuario.getUsuario().getId_Codigo();
		}

		montarPedidosDTO(null, idCodigo);
	}

	@Funcionalidade
	public void pesquisarPedidosDosDistribuidores(String status, Integer idCodigo) {

		if (verificarPermissaoPedidosAdministrativos()) {

			if (Util.vazio(status)) {
				status = "PENDENTE";
			}

			montarPedidosDTO(status, idCodigo);

			result.include("idCodigo", idCodigo);
			result.include("status", status);
		}
	}

	private boolean verificarPermissaoPedidosAdministrativos() {

		return this.sessaoUsuario.getUsuario().getDonoDeFranquia() || this.sessaoUsuario.getUsuario().obterInformacoesFixasUsuario().getAdministrador();
	}

	@Funcionalidade
	@Get("/pedido/alterarStatus/{idPedido}/{status}")
	public void alterarStatus(String status, Integer idPedido) throws Exception {

		if (verificarPermissaoPedidosAdministrativos()) {

			Pedido pedido = hibernateUtil.selecionar(new Pedido(idPedido));

			if (status.equals("FINALIZADO") && !pedido.getStatus().equals("FINALIZADO")) {

				String textoArquivo = "id_Codigo=" + pedido.getIdCodigo() + "\r\n";
				textoArquivo += "id_CDA=" + pedido.getIdFranquia() + "\r\n";
				for (ItemPedido itemPedido : new PedidoService(hibernateUtil).listarItensPedido(pedido)) {
					textoArquivo += itemPedido.getIdProduto() + "=" + itemPedido.getQuantidade() + "\r\n";
				}
				ArquivoService.criarArquivoNoDisco(textoArquivo, ArquivoService.PASTA_PEDIDOS);
			}

			if (status.equals("CANCELADO")) {

				new PedidoService(hibernateUtil).cancelarPedido(pedido);
			}

			pedido.setStatus(status);
			hibernateUtil.salvarOuAtualizar(pedido);

			result.forwardTo(this).pesquisarPedidosDosDistribuidores(null, null);
		}
	}

	@Funcionalidade
	@Get("/pedido/realizarPagamento/{idPedido}")
	public void realizarPagamento(Integer idPedido) {

		if (verificarPermissaoPedidosAdministrativos()) {

			Pedido pedido = hibernateUtil.selecionar(new Pedido(idPedido));
			SaldoDTO saldoDTO = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(pedido.getIdCodigo(), Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR));

			verificarPagamentoComSaldoHabilitado(pedido);
			result.include("idPedido", idPedido);
			result.include("saldoLiberado", saldoDTO.getSaldoLiberado());
		}
	}

	@Funcionalidade
	@Post("/pedido/pagarEFinalizar")
	public void pagarEFinalizar(Integer idPedido, BigDecimal valor) throws Exception {

		if (verificarPermissaoPedidosAdministrativos()) {

			if (valor == null) {
				valor = BigDecimal.ZERO;
			}

			Pedido pedido = hibernateUtil.selecionar(new Pedido(idPedido));
			SaldoDTO saldoDTO = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(pedido.getIdCodigo(), Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR));
			BigDecimal saldoLiberado = saldoDTO.getSaldoLiberado();

			if (valor.compareTo(saldoLiberado) > 0) {
				validator.add(new ValidationMessage("O valor a ser debitado não pode ser maior do que o saldo atual", "Erro"));
				validator.onErrorRedirectTo(this).realizarPagamento(idPedido);
				return;
			}

			salvarTransferencia(valor, pedido);

			result.forwardTo(this).alterarStatus("FINALIZADO", idPedido);
		}
	}

	private void salvarTransferencia(BigDecimal valor, Pedido pedido) {

		if (valor.compareTo(BigDecimal.ZERO) > 0) {

			Transferencia transferencia = new Transferencia();
			transferencia.setData(new GregorianCalendar());
			transferencia.setDe(pedido.getIdCodigo());
			transferencia.setValor(valor);
			transferencia.setTipo(Transferencia.TRANSFERENCIA_PARA_PAGAMENTO_DE_PEDIDO);
			hibernateUtil.salvarOuAtualizar(transferencia);
		}
	}

	@Funcionalidade
	@Get("/pedido/verItens/{idPedido}")
	public void verItens(Integer idPedido) {

		List<ItemPedidoDTO> itensPedidoDTO = new ArrayList<ItemPedidoDTO>();

		for (ItemPedido itemPedido : new PedidoService(hibernateUtil).listarItensPedido((Pedido) hibernateUtil.selecionar(new Pedido(idPedido)))) {
			Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);
			Integer quantidade = itemPedido.getQuantidade();
			itensPedidoDTO.add(new ItemPedidoDTO(produto, quantidade, itemPedido.getPrecoUnitario(), 0));
		}

		result.include("itensPedidoDTO", itensPedidoDTO);
	}

	private void montarPedidosDTO(String status, Integer idCodigo) {

		Pedido pedidoFiltro = new Pedido();
		pedidoFiltro.setIdCodigo(idCodigo);
		pedidoFiltro.setStatus(status);
		pedidoFiltro.setCompleted(true);

		List<Criterion> restricoes = new ArrayList<Criterion>();

		if (this.sessaoUsuario.getUsuario().getDonoDeFranquia() && !this.sessaoUsuario.getUsuario().obterInformacoesFixasUsuario().getAdministrador()) {

			Franquia franquiaFiltro = new Franquia();
			franquiaFiltro.setId_Codigo(this.sessaoUsuario.getUsuario().getId_Codigo());
			List<Franquia> franquias = hibernateUtil.buscar(franquiaFiltro);

			List<Integer> idFranquias = new ArrayList<Integer>();

			for (Franquia franquia : franquias) {
				idFranquias.add(franquia.getId_Estoque());
			}

			restricoes.add(Restrictions.in("idFranquia", idFranquias));
		}

		List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro, restricoes);
		List<PedidoDTO> pedidosDTO = new ArrayList<PedidoDTO>();

		for (Pedido pedido : pedidos) {
			pedidosDTO.add(new PedidoDTO(pedido, (Franquia) hibernateUtil.selecionar(new Franquia(pedido.getIdFranquia())), calcularTotais(pedido).getValorTotal(), null, null));
		}

		result.include("pedidosDTO", pedidosDTO);
	}

	private PedidoDTO calcularTotais(Pedido pedido) {

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
