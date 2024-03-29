package br.com.alabastrum.escritoriovirtual.controller;

import static br.com.caelum.vraptor.view.Results.json;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.anotacoes.Public;
import br.com.alabastrum.escritoriovirtual.dto.FreteResponseDTO;
import br.com.alabastrum.escritoriovirtual.dto.ItemPedidoDTO;
import br.com.alabastrum.escritoriovirtual.dto.PedidoDTO;
import br.com.alabastrum.escritoriovirtual.dto.SaldoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Categoria;
import br.com.alabastrum.escritoriovirtual.modelo.Comprador;
import br.com.alabastrum.escritoriovirtual.modelo.Configuracao;
import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.ItemPedido;
import br.com.alabastrum.escritoriovirtual.modelo.KitAdesao;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Produto;
import br.com.alabastrum.escritoriovirtual.modelo.Transferencia;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.alabastrum.escritoriovirtual.service.EstoqueService;
import br.com.alabastrum.escritoriovirtual.service.ExtratoService;
import br.com.alabastrum.escritoriovirtual.service.FreteService;
import br.com.alabastrum.escritoriovirtual.service.KitAdesaoService;
import br.com.alabastrum.escritoriovirtual.service.PagSeguroService;
import br.com.alabastrum.escritoriovirtual.service.PedidoService;
import br.com.alabastrum.escritoriovirtual.service.PosicoesService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoGeral;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class PedidoController {

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

	if (this.sessaoUsuario.getUsuario().getId() == null) {

	    Object idUsuarioLojaPessoal = this.sessaoGeral.getValor(PedidoService.ID_USUARIO_PEDIDO);

	    if (Util.preenchido(idUsuarioLojaPessoal)) {
		Usuario usuario = this.hibernateUtil.selecionar(new Usuario((Integer) idUsuarioLojaPessoal));
		lojaPessoal(usuario.getApelido());
	    }

	    return;
	}

	Pedido pedido = selecionarPedidoAberto();
	if (pedido != null && pedido.getTipo().equals(PedidoService.LOJA_PESSOAL)) {
	    pedido.setCompleted(true);
	    pedido.setData(new GregorianCalendar());
	    pedido.setStatus(PedidoService.CANCELADO);
	    hibernateUtil.salvarOuAtualizar(pedido);
	}

	buscarFranquias();
	result.include("kitsAdesao", new KitAdesaoService(hibernateUtil).buscarKits(new GregorianCalendar()));

	Usuario usuario = this.hibernateUtil.selecionar(new Usuario(this.sessaoUsuario.getUsuario().getId_Codigo()));
	if (!"revendedor".equalsIgnoreCase(usuario.getNome_kit())) {

	    if (isPrimeiroPedido(this.sessaoUsuario.getUsuario().getId_Codigo())) {
		this.sessaoGeral.adicionar("isPrimeiroPedido", true);
		result.include("alerta", new ValidationMessage("Você ainda não fez um pedido de adesão. Você precisa realizar este pedido para ingressar, de fato, na empresa, e poder ficar ativo este mês", "Erro"));
		return;
	    }

	    if (isInativo(this.sessaoUsuario.getUsuario().getId_Codigo())) {
		this.sessaoGeral.adicionar("isInativo", true);
		result.include("alerta", new ValidationMessage("Você ainda não está ativo este mês. Você precisa fazer um pedido para ficar ativo", "Erro"));
		return;
	    }
	}
    }

    private boolean isPrimeiroPedido(Integer idCodigo) {

	Pedido pedidoFiltro = new Pedido();
	pedidoFiltro.setIdCodigo(idCodigo);
	pedidoFiltro.setStatus(PedidoService.FINALIZADO);
	return hibernateUtil.contar(pedidoFiltro) == 0;
    }

    private boolean isInativo(Integer idCodigo) {

	return !new AtividadeService(hibernateUtil).isAtivo(idCodigo);
    }

    private void buscarFranquias() {

	Franquia franquiaFiltro = new Franquia();
	if (this.sessaoUsuario.getUsuario().getDonoDeFranquia()) {
	    franquiaFiltro.setId_Codigo(this.sessaoUsuario.getUsuario().getId_Codigo());
	}
	result.include("franquias", hibernateUtil.buscar(franquiaFiltro));
    }

    @Funcionalidade
    public void escolherProdutos(Integer idFranquia, String nickname, String formaDeEntrega, Boolean adesaoPontoDeApoio, Boolean escolherCategoriaPadrao, Integer idKit) {

	if (this.sessaoUsuario.getUsuario().getDonoDeFranquia()) {
	    formaDeEntrega = PedidoService.RECEBER_NO_PA;

	    Franquia franquia = new Franquia();
	    franquia.setId_Codigo(this.sessaoUsuario.getUsuario().getId_Codigo());
	    franquia = (Franquia) hibernateUtil.buscar(franquia).get(0);
	    idFranquia = franquia.getId_Estoque();
	}

	if (Util.vazio(formaDeEntrega)) {

	    validator.add(new ValidationMessage("Selecione uma forma de entrega", "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaNovoPedido();
	    return;
	}

	if (Util.vazio(nickname)) {
	    nickname = this.sessaoUsuario.getUsuario().getApelido();
	}

	Usuario usuario = new Usuario();
	usuario.setApelido(nickname);
	usuario = this.hibernateUtil.selecionar(usuario);

	if (Util.vazio(usuario)) {

	    validator.add(new ValidationMessage("Nao existe ninguem cadastrado com este nickname: " + nickname, "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaNovoPedido();
	    return;
	}

	Integer idCodigo = usuario.getId_Codigo();

	if (this.sessaoUsuario.getUsuario().getId() == null) {
	    idCodigo = (Integer) this.sessaoGeral.getValor(PedidoService.ID_USUARIO_PEDIDO);

	    Franquia franquia = new Franquia();
	    franquia.setId_Estoque(1);
	    franquia = this.hibernateUtil.selecionar(franquia);
	    idFranquia = franquia.getId_Estoque();
	}

	if (idCodigo == null || idCodigo == 0) {
	    idCodigo = this.sessaoUsuario.getUsuario().getId_Codigo();
	}
	this.sessaoGeral.adicionar(PedidoService.ID_USUARIO_PEDIDO, idCodigo);
	boolean primeiroPedido = isPrimeiroPedido(idCodigo);
	this.sessaoGeral.adicionar("isPrimeiroPedido", primeiroPedido);
	this.sessaoGeral.adicionar("isInativo", isInativo(idCodigo));
	this.sessaoGeral.adicionar("adesaoPontoDeApoio", adesaoPontoDeApoio);

	if (idKit != null) {
	    KitAdesao kitAdesao = new KitAdesao();
	    kitAdesao.setId(idKit);
	    kitAdesao = this.hibernateUtil.selecionar(kitAdesao);
	    this.sessaoGeral.adicionar("kitAdesao", kitAdesao.getKit());
	} else {
	    Object kitAdesao = this.sessaoGeral.getValor("kitAdesao");
	    if (kitAdesao == null) {
		this.sessaoGeral.adicionar("kitAdesao", usuario.getNome_kit());
	    }
	}

	Object kitAdesao = this.sessaoGeral.getValor("kitAdesao");
	if (primeiroPedido && (Util.vazio(kitAdesao) || "precadastro".equalsIgnoreCase((String) kitAdesao))) {
	    validator.add(new ValidationMessage("É necessário escolher um kit de adesão para fazer o seu primeiro pedido", "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaNovoPedido();
	    return;
	}

	if (formaDeEntrega.equals(PedidoService.RECEBER_EM_CASA)) {
	    idFranquia = 1;
	}

	if (idFranquia == null) {

	    validator.add(new ValidationMessage("Selecione uma franquia", "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaNovoPedido();
	    return;
	}

	Pedido pedido = selecionarPedidoAberto();

	if (Util.vazio(pedido)) {

	    pedido = new Pedido();
	    pedido.setIdCodigo(idCodigo);
	    pedido.setCompleted(false);
	    pedido.setStatus(PedidoService.PENDENTE);
	    pedido.setTipo(definirTipoDoPedido(pedido.getComprador()));
	}

	pedido.setFormaDeEntrega(formaDeEntrega);
	pedido.setIdFranquia(idFranquia);
	hibernateUtil.salvarOuAtualizar(pedido);

	result.include("franquia", hibernateUtil.selecionar(new Franquia(pedido.getIdFranquia())));
	result.include("mostrarPontuacao", !Boolean.TRUE.equals(this.sessaoGeral.getValor("isPrimeiroPedido")));
	List<Categoria> todasCategorias = hibernateUtil.buscar(new Categoria(), Order.asc("catNome"));
	List<Categoria> categoriasSemTaxas = new ArrayList<Categoria>();
	for (Categoria categoria : todasCategorias) {
	    if (!categoria.getCatNome().equalsIgnoreCase(PedidoService.TAXAS)) {
		categoriasSemTaxas.add(categoria);
	    }
	}
	result.include("categorias", categoriasSemTaxas);

	if (escolherCategoriaPadrao != null && escolherCategoriaPadrao) {
	    Integer categoriaPadrao = null;
	    for (Categoria categoria : todasCategorias) {
		if (!categoria.getCatNome().equalsIgnoreCase(PedidoService.TAXAS)) {
		    categoriasSemTaxas.add(categoria);
		}
		if (Integer.valueOf(1).equals(categoria.getPadrao())) {
		    categoriaPadrao = categoria.getId_Categoria();
		}
	    }

	    result.redirectTo(this).selecionarCategoria(categoriaPadrao);
	}
    }

    @Funcionalidade
    @Get("/pedido/imprimirPedido/{idPedido}")
    public void imprimirPedido(Integer idPedido) {

	Pedido pedido = hibernateUtil.selecionar(new Pedido(idPedido));
	Usuario usuario = this.hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));
	BigDecimal valorTotal = new PedidoService(hibernateUtil).calcularTotais(pedido).getValorTotal();

	List<ItemPedidoDTO> itensPedidoDTO = new ArrayList<ItemPedidoDTO>();
	for (ItemPedido itemPedido : new PedidoService(hibernateUtil).listarItensPedido(pedido)) {
	    Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);
	    Integer quantidade = itemPedido.getQuantidade();
	    itensPedidoDTO.add(new ItemPedidoDTO(produto, quantidade, itemPedido.getPrecoUnitario(), 0, null));
	}

	result.include("pedido", pedido);
	result.include("valorTotal", valorTotal);
	result.include("usuarioPedido", usuario);
	result.include("itensPedidoDTO", itensPedidoDTO);
    }

    private String definirTipoDoPedido(Comprador comprador) {

	if (comprador != null || this.sessaoUsuario.getUsuario().getId_Codigo() == null) {
	    return PedidoService.LOJA_PESSOAL;
	}

	Object adesaoPontoDeApoio = this.sessaoGeral.getValor("adesaoPontoDeApoio");
	Object isPrimeiroPedido = this.sessaoGeral.getValor("isPrimeiroPedido");
	Object isInativo = this.sessaoGeral.getValor("isInativo");

	if (adesaoPontoDeApoio != null && (Boolean) adesaoPontoDeApoio)
	    return PedidoService.ADESAO_PA;
	else if (isPrimeiroPedido != null && (Boolean) isPrimeiroPedido)
	    return PedidoService.ADESAO;
	else if (isInativo != null && (Boolean) isInativo)
	    return PedidoService.ATIVIDADE;

	return PedidoService.RECOMPRA;
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

		String periodoParaCompraDeInativos = produto.getPeriodoParaCompraDeInativos();
		if (Util.vazio(periodoParaCompraDeInativos)) {
		    adicionarItemPedido(itensPedidoDTO, produto, quantidadeEmEstoque, quantidade);
		} else {
		    Integer diaInicialParaCompraDeInativos = Integer.valueOf(periodoParaCompraDeInativos.split("-")[0]);
		    Integer diaFinalParaCompraDeInativos = Integer.valueOf(periodoParaCompraDeInativos.split("-")[1]);
		    Integer diaDoMes = Util.getTempoCorrenteAMeiaNoite().get(Calendar.DAY_OF_MONTH);

		    if (diaDoMes >= diaInicialParaCompraDeInativos && diaDoMes <= diaFinalParaCompraDeInativos && !new AtividadeService(hibernateUtil).isAtivo(pedido.getIdCodigo())) {
			adicionarItemPedido(itensPedidoDTO, produto, quantidadeEmEstoque, quantidade);
		    }
		}
	    }
	}

	result.include("itensPedidoDTO", itensPedidoDTO);
	result.include("totais", new PedidoService(hibernateUtil).calcularTotais(pedido));
	Usuario usuario = this.hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));
	result.forwardTo(this).escolherProdutos(pedido.getIdFranquia(), usuario.getApelido(), pedido.getFormaDeEntrega(), (Boolean) this.sessaoGeral.getValor("adesaoPontoDeApoio"), null, null);
    }

    private void adicionarItemPedido(List<ItemPedidoDTO> itensPedidoDTO, Produto produto, int quantidadeEmEstoque, Integer quantidade) {
	itensPedidoDTO.add(new ItemPedidoDTO(produto, quantidade, calcularPrecoUnitarioProduto(produto.getPrdPreco_Unit()), quantidadeEmEstoque, null));
    }

    private BigDecimal calcularPrecoUnitarioProduto(BigDecimal precoUnitario) {

	Object adesaoPontoDeApoio = this.sessaoGeral.getValor("adesaoPontoDeApoio");
	Object isPrimeiroPedido = this.sessaoGeral.getValor("isPrimeiroPedido");
	Object isInativo = this.sessaoGeral.getValor("isInativo");

	if (this.sessaoUsuario.getUsuario().getId() == null || (isPrimeiroPedido != null && (Boolean) isPrimeiroPedido) || (isInativo != null && (Boolean) isInativo) || (adesaoPontoDeApoio != null && (Boolean) adesaoPontoDeApoio)) {
	    // return precoUnitario.multiply(new BigDecimal("2"));
	}
	return precoUnitario;
    }

    @Funcionalidade
    @Post("/pedido/adicionarProduto/{idProduto}")
    public void adicionarProduto(String idProduto, Integer quantidade) {

	Pedido pedido = selecionarPedidoAberto();

	adicionarItemPedidoTaxaAdesao(idProduto, pedido);

	ItemPedido itemPedido = selecionarItemPedido(idProduto, pedido);
	Produto produto = hibernateUtil.selecionar(new Produto(idProduto), MatchMode.EXACT);

	if (Util.vazio(itemPedido)) {
	    itemPedido = new ItemPedido();
	    itemPedido.setPedido(pedido);
	    itemPedido.setIdProduto(idProduto);
	    itemPedido.setPrecoUnitario(calcularPrecoUnitarioProduto(produto.getPrdPreco_Unit()));
	}

	if (quantidade == null || quantidade == 0) {
	    quantidade = 1;
	}

	itemPedido.setQuantidade(quantidade);
	hibernateUtil.salvarOuAtualizar(itemPedido);

	if (quantidade <= 0) {
	    hibernateUtil.deletar(itemPedido);
	}

	result.forwardTo(this).selecionarCategoria(produto.getId_Categoria());
    }

    @Funcionalidade(administrativa = "true")
    @Post("/pedido/removerDoEstoque/{idProduto}")
    public void removerDoEstoque(String idProduto, Integer quantidade) {

	Pedido pedido = selecionarPedidoAberto();
	new EstoqueService(hibernateUtil).retirarDoEstoque(idProduto, pedido.getIdFranquia(), quantidade);
	Produto produto = hibernateUtil.selecionar(new Produto(idProduto), MatchMode.EXACT);
	result.forwardTo(this).selecionarCategoria(produto.getId_Categoria());
    }

    private void adicionarItemPedidoTaxaAdesao(String idProduto, Pedido pedido) {

	if (this.sessaoUsuario.getUsuario().getId() != null) {

	    Object isPrimeiroPedido = this.sessaoGeral.getValor("isPrimeiroPedido");
	    if (isPrimeiroPedido != null && (Boolean) isPrimeiroPedido) {
		if (new PedidoService(hibernateUtil).listarItensPedido(pedido).size() == 0) {

		    Produto produto = hibernateUtil.selecionar(new Produto(PedidoService.PRODUTO_TAXA_ADESAO_ID));
		    ItemPedido itemPedidoTaxaAdesao = new ItemPedido();
		    itemPedidoTaxaAdesao.setPedido(pedido);
		    itemPedidoTaxaAdesao.setIdProduto(produto.getId_Produtos());
		    itemPedidoTaxaAdesao.setQuantidade(1);

		    Object adesaoPontoDeApoio = this.sessaoGeral.getValor("adesaoPontoDeApoio");
		    if (adesaoPontoDeApoio != null && (Boolean) adesaoPontoDeApoio) {
			itemPedidoTaxaAdesao.setPrecoUnitario(new BigDecimal(300));
		    } else {
			itemPedidoTaxaAdesao.setPrecoUnitario(calcularPrecoUnitarioProduto(produto.getPrdPreco_Unit()));
		    }

		    hibernateUtil.salvarOuAtualizar(itemPedidoTaxaAdesao);
		}
	    }
	}
    }

    @Funcionalidade
    public void acessarCarrinho() throws Exception {

	if (this.sessaoUsuario.getUsuario().getId() != null) {
	    Pedido pedido = selecionarPedidoAberto();
	    if (pedido != null && pedido.getTipo().equals(PedidoService.LOJA_PESSOAL)) {
		pedido.setCompleted(true);
		pedido.setData(new GregorianCalendar());
		pedido.setStatus(PedidoService.CANCELADO);
		hibernateUtil.salvarOuAtualizar(pedido);
	    }
	}

	List<ItemPedidoDTO> itensPedidoDTO = new ArrayList<ItemPedidoDTO>();

	Pedido pedido = selecionarPedidoAberto();

	if (pedido != null) {

	    Integer idCodigo = pedido.getIdCodigo();

	    result.include(PedidoService.FORMA_DE_ENTREGA, pedido.getFormaDeEntrega());
	    this.sessaoGeral.adicionar(PedidoService.ID_USUARIO_PEDIDO, idCodigo);
	    this.sessaoGeral.adicionar("isPrimeiroPedido", isPrimeiroPedido(idCodigo));
	    this.sessaoGeral.adicionar("isInativo", isInativo(idCodigo));

	    pedido.setTipo(definirTipoDoPedido(pedido.getComprador()));
	    hibernateUtil.salvarOuAtualizar(pedido);

	    for (ItemPedido itemPedido : new PedidoService(hibernateUtil).listarItensPedido(pedido)) {
		Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);
		Integer quantidade = itemPedido.getQuantidade();
		itensPedidoDTO.add(new ItemPedidoDTO(produto, quantidade, itemPedido.getPrecoUnitario(), 0, null));
	    }

	    // Object isPrimeiroPedido = this.sessaoGeral.getValor("isPrimeiroPedido");
	    // if (isPrimeiroPedido != null && (Boolean) isPrimeiroPedido) {
	    // Integer valorTotal = new
	    // PedidoService(hibernateUtil).calcularTotalSemFrete(pedido).intValue();
	    // result.include("kitAdesao", new
	    // KitAdesaoService(hibernateUtil).encontrarKitPeloValor(new
	    // GregorianCalendar(), valorTotal));
	    // }
	}

	result.include("itensPedidoDTO", itensPedidoDTO);
	result.include("totais", new PedidoService(hibernateUtil).calcularTotais(pedido));
    }

    @Funcionalidade
    public void escolherFormaDeEnvio() throws Exception {

	Pedido pedido = selecionarPedidoAberto();
	List<ItemPedido> itensPedido = new PedidoService(hibernateUtil).listarItensPedido(pedido);
	Franquia franquia = hibernateUtil.selecionar(new Franquia(pedido.getIdFranquia()));
	Usuario distribuidorPedido = hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));
	Comprador comprador = pedido.getComprador();

	String cep = "";

	if (comprador != null)
	    cep = comprador.getCep();
	else
	    cep = distribuidorPedido.getCadCEP();

	List<FreteResponseDTO> opcoesDeFrete = new FreteService(hibernateUtil).buscarOpcoesDeFrete(franquia.getEstqCEP(), cep, itensPedido);
	result.include(PedidoService.OPCOES_DE_FRETE, opcoesDeFrete);
	this.sessaoGeral.adicionar(PedidoService.OPCOES_DE_FRETE, opcoesDeFrete);
    }

    @SuppressWarnings("unchecked")
    @Funcionalidade
    @Get("/pedido/escolherFormaDeEnvioPorId/{formaDeEnvioId}")
    public void escolherFormaDeEnvioPorId(Integer formaDeEnvioId) throws Exception {

	List<FreteResponseDTO> opcoesDeFrete = (List<FreteResponseDTO>) this.sessaoGeral.getValor(PedidoService.OPCOES_DE_FRETE);
	for (FreteResponseDTO opcaoDeFrete : opcoesDeFrete) {
	    if (opcaoDeFrete.getId().equals(formaDeEnvioId)) {

		ItemPedido itemPedidoFrete = null;
		Pedido pedido = selecionarPedidoAberto();
		List<ItemPedido> itensPedido = new PedidoService(hibernateUtil).listarItensPedido(pedido);
		for (ItemPedido itemPedido : itensPedido) {
		    Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);
		    if (produto.getId_Produtos().equals(PedidoService.PRODUTO_FRETE_ID)) {
			itemPedidoFrete = itemPedido;
			break;
		    }
		}

		if (itemPedidoFrete == null) {
		    itemPedidoFrete = new ItemPedido();
		    itemPedidoFrete.setIdProduto(PedidoService.PRODUTO_FRETE_ID);
		    itemPedidoFrete.setPedido(pedido);
		    itemPedidoFrete.setQuantidade(1);
		}

		itemPedidoFrete.setPrecoUnitario(new BigDecimal(opcaoDeFrete.getPrice()));
		hibernateUtil.salvarOuAtualizar(itemPedidoFrete);

		pedido.setEmpresaParaEntrega(opcaoDeFrete.getCompany().getName() + " - " + opcaoDeFrete.getName());
		hibernateUtil.salvarOuAtualizar(pedido);

		break;
	    }
	}

	result.forwardTo(this).escolherFormaDePagamento();
    }

    @Funcionalidade
    @Get("/pedido/removerProduto/{idProduto}")
    public void removerProduto(String idProduto) throws Exception {

	Pedido pedido = selecionarPedidoAberto();
	ItemPedido itemPedido = selecionarItemPedido(idProduto, pedido);
	Produto produto = this.hibernateUtil.selecionar(new Produto(idProduto));
	Categoria categoria = new Categoria();
	categoria.setId_Categoria(produto.getId_Categoria());
	categoria = this.hibernateUtil.selecionar(categoria);

	if (!categoria.getCatNome().equalsIgnoreCase(PedidoService.TAXAS)) {
	    hibernateUtil.deletar(itemPedido);
	}

	result.forwardTo(this).acessarCarrinho();
    }

    @Funcionalidade
    public void escolherFormaDePagamento() throws Exception {

	Pedido pedido = selecionarPedidoAberto();
	Integer valorTotal = new PedidoService(hibernateUtil).calcularTotalSemFrete(pedido).intValue();

	if (pedido.getComprador() == null) {

	    boolean mostrarDialogoDescontos = true;

	    Object adesaoPontoDeApoio = this.sessaoGeral.getValor("adesaoPontoDeApoio");
	    Object isPrimeiroPedido = this.sessaoGeral.getValor("isPrimeiroPedido");
	    Object isInativo = this.sessaoGeral.getValor("isInativo");

	    String kit = (String) this.sessaoGeral.getValor("kitAdesao");
	    KitAdesao kitAdesao = new KitAdesaoService(hibernateUtil).encontrarKitPeloNome(new GregorianCalendar(), kit);
	    Integer valorMinimoPedidoAdesao = kitAdesao.getValor().intValue();
	    Integer valorMinimoPedidoAtividade = kitAdesao.getValor_atividade().intValue();

	    if (adesaoPontoDeApoio != null && (Boolean) adesaoPontoDeApoio) {
		mostrarDialogoDescontos = false;

		if (valorTotal < 2500) {
		    validator.add(new ValidationMessage("O valor mínimo para pedido de adesão de Ponto de apoio é de R$2500", "Erro"));
		    validator.onErrorRedirectTo(this).acessarCarrinho();
		    return;
		}
	    } else if (isPrimeiroPedido != null && (Boolean) isPrimeiroPedido) {
		mostrarDialogoDescontos = false;

		if (valorTotal < valorMinimoPedidoAdesao) {
		    validator.add(new ValidationMessage(String.format("O valor mínimo para pedido de adesão é de R$%s", valorMinimoPedidoAdesao), "Erro"));
		    validator.onErrorRedirectTo(this).acessarCarrinho();
		    return;
		}
	    } else if (isInativo != null && (Boolean) isInativo) {

		Usuario usuario = this.hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));

		if (KitAdesao.DISTRIBUIDOR.equalsIgnoreCase(usuario.getNome_kit())) {

		    mostrarDialogoDescontos = false;

		    if (valorTotal < valorMinimoPedidoAtividade) {
			validator.add(new ValidationMessage(String.format("O valor mínimo para pedido de atividade é de R$%s", valorMinimoPedidoAtividade), "Erro"));
			validator.onErrorRedirectTo(this).acessarCarrinho();
			return;
		    }
		}
	    }
	    result.include("mostrarDialogoDescontos", mostrarDialogoDescontos);
	}

	result.include(PedidoService.FORMA_DE_ENTREGA, pedido.getFormaDeEntrega());

	verificarPagamentoComSaldoHabilitado(pedido);
    }

    @Funcionalidade
    public void informarDadosComprador() {
    }

    @Funcionalidade
    public void salvarDadosComprador(String nome, String cpf, String email, String telefone, String cep, String bairro, String cidade, String uf, String endereco, String numeroEndereco, String complementoEndereco) throws Exception {

	cpf = cpf.replaceAll(" ", "").replaceAll("\\.", "").replaceAll("-", "");
	telefone = telefone.replaceAll(" ", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "");

	Comprador comprador = new Comprador();
	comprador.setCpf(cpf);
	comprador = hibernateUtil.selecionar(comprador);

	if (comprador == null) {
	    comprador = new Comprador();
	}

	comprador.setCpf(cpf);
	comprador.setNome(nome);
	comprador.setEmail(email);
	comprador.setTelefone(telefone);
	comprador.setCep(cep);
	comprador.setBairro(bairro);
	comprador.setCidade(cidade);
	comprador.setUf(uf);
	comprador.setEndereco(endereco);
	comprador.setNumeroEndereco(numeroEndereco);
	comprador.setComplementoEndereco(complementoEndereco);

	hibernateUtil.salvarOuAtualizar(comprador);

	Pedido pedido = selecionarPedidoAberto();
	pedido.setComprador(comprador);
	hibernateUtil.salvarOuAtualizar(pedido);

	this.sessaoGeral.adicionar("formaDePagamento", "pagarComBoleto");

	result.forwardTo(this).escolherFormaDeEnvio();
    }

    private void verificarPagamentoComSaldoHabilitado(Pedido pedido) {

	boolean pagamentoComSaldoHabilitado = false;

	if (this.sessaoUsuario.getUsuario().getId() != null && (pedido.getIdCodigo().equals(this.sessaoUsuario.getUsuario().getId_Codigo()) || this.sessaoUsuario.getUsuario().obterInformacoesFixasUsuario().getAdministrador())) {
	    pagamentoComSaldoHabilitado = true;
	}

	result.include("pagamentoComSaldoHabilitado", pagamentoComSaldoHabilitado);
    }

    @Funcionalidade
    public void mostrarValorFinalPedido(String formaDePagamento) throws Exception {

	if (formaDePagamento == null || formaDePagamento.equals("")) {
	    validator.add(new ValidationMessage("Selecione a forma de pagamento", "Erro"));
	    validator.onErrorRedirectTo(this).escolherFormaDePagamento();
	    return;
	}

	this.sessaoGeral.adicionar("formaDePagamento", formaDePagamento);

	Pedido pedido = selecionarPedidoAberto();
	alterarValorItensPedidoDeAcordoComFormaDePagamento(pedido, formaDePagamento);
	BigDecimal precoFinal = new PedidoService(hibernateUtil).calcularTotais(pedido).getValorTotal();
	result.include("precoFinal", precoFinal);

	if (formaDePagamento.equals("pagarComSaldo")) {
	    result.include("tarifas", precoFinal.multiply(BigDecimal.ZERO));
	}
    }

    private void alterarValorItensPedidoDeAcordoComFormaDePagamento(Pedido pedido, String formaDePagamento) throws Exception {

	// String tipoDoPedido = definirTipoDoPedido(pedido.getComprador());
	// Usuario usuario = this.hibernateUtil.selecionar(new
	// Usuario(this.sessaoUsuario.getUsuario().getId_Codigo()));

//	if (tipoDoPedido.equals(PedidoService.RECOMPRA) || "revendedor".equalsIgnoreCase(usuario.getNome_kit())) {

	for (ItemPedido itemPedido : new PedidoService(hibernateUtil).listarItensPedido(pedido)) {
	    Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);

	    if (!produto.getId_Produtos().equals(PedidoService.PRODUTO_FRETE_ID)) {

		BigDecimal precoUnitarioProduto = produto.getPrdPreco_Unit();
		BigDecimal precoUnitarioItemPedido = precoUnitarioProduto.multiply(new BigDecimal("2"));

		if (formaDePagamento.equalsIgnoreCase("pagarComSaldo")) {
		    precoUnitarioItemPedido = precoUnitarioItemPedido.subtract(precoUnitarioItemPedido.multiply(new BigDecimal("0.50")));

		} else if (formaDePagamento.equalsIgnoreCase("pagarComDinheiro")) {
		    precoUnitarioItemPedido = precoUnitarioItemPedido.subtract(precoUnitarioItemPedido.multiply(new BigDecimal("0.50")));

		} else if (formaDePagamento.equalsIgnoreCase("pagarComBoleto")) {
		    precoUnitarioItemPedido = precoUnitarioItemPedido.subtract(precoUnitarioItemPedido.multiply(new BigDecimal("0.47")));

		} else if (formaDePagamento.equalsIgnoreCase("pagarComCartaoDeDebitoNoPA")) {
		    precoUnitarioItemPedido = precoUnitarioItemPedido.subtract(precoUnitarioItemPedido.multiply(new BigDecimal("0.472")));

		} else if (formaDePagamento.equalsIgnoreCase("pagarComCartaoDeDebitoOnline")) {
		    precoUnitarioItemPedido = precoUnitarioItemPedido.subtract(precoUnitarioItemPedido.multiply(new BigDecimal("0.472")));

		} else if (formaDePagamento.equalsIgnoreCase("pagarComCartaoDeCreditoNoPA")) {
		    precoUnitarioItemPedido = precoUnitarioItemPedido.subtract(precoUnitarioItemPedido.multiply(new BigDecimal("0.467")));

		} else if (formaDePagamento.equalsIgnoreCase("pagarComCartaoDeCreditoOnline")) {
		    precoUnitarioItemPedido = precoUnitarioItemPedido.subtract(precoUnitarioItemPedido.multiply(new BigDecimal("0.467")));

		} else {
		    throw new Exception("Forma de pagamento desconhecida: " + formaDePagamento);
		}

		itemPedido.setPrecoUnitario(precoUnitarioItemPedido);
		this.hibernateUtil.salvarOuAtualizar(itemPedido);
	    }
	}
    }
    // }

    @Funcionalidade
    public void concluirPedido() throws Exception {

	String formaDePagamento = (String) this.sessaoGeral.getValor("formaDePagamento");

	if (formaDePagamento.equals("pagarComCartaoDeCreditoOnline")) {

	    result.include("pagseguroSessionId", new PagSeguroService(hibernateUtil).gerarSessionId());
	    result.forwardTo("/WEB-INF/jsp/pedido/pagarComCartaoDeCredito.jsp");
	    return;
	}

	Pedido pedido = selecionarPedidoAberto();

	if (pedido == null) {
	    validator.add(new ValidationMessage("Você não tem nenhum pedido aberto neste momento", "Erro"));
	    validator.onErrorRedirectTo(this).acessarCarrinho();
	    return;
	}

	BigDecimal totalPedido = new PedidoService(hibernateUtil).calcularTotais(pedido).getValorTotal();

	for (ItemPedido itemPedido : new PedidoService(hibernateUtil).listarItensPedido(pedido)) {

	    Integer quantidadeEmEstoque = new EstoqueService(hibernateUtil).getQuantidadeEmEstoque(itemPedido.getIdProduto(), pedido.getIdFranquia());

	    if (itemPedido.getQuantidade() > quantidadeEmEstoque) {

		Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()));
		Categoria categoria = new Categoria();
		categoria.setId_Categoria(produto.getId_Categoria());
		categoria = this.hibernateUtil.selecionar(categoria);

		if (!categoria.getCatNome().equalsIgnoreCase(PedidoService.TAXAS)) {
		    validator.add(new ValidationMessage("O produto " + produto.getPrdNome() + " possui " + quantidadeEmEstoque + " unidades no estoque", "Erro"));
		    validator.onErrorRedirectTo(this).acessarCarrinho();
		    return;
		}
	    }
	}

	Usuario comprador = hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));

	if (pedido.getComprador() == null) {

	    String valorMinimoPedidosPrimeiraPosicao = new Configuracao().retornarConfiguracao("valorMinimoPedidosPrimeiraPosicao");
	    if (comprador.getPosAtual().equalsIgnoreCase(new PosicoesService(hibernateUtil).obterNomeDaPosicao(1, new GregorianCalendar())) && totalPedido.compareTo(new BigDecimal(valorMinimoPedidosPrimeiraPosicao)) < 0) {

		validator.add(new ValidationMessage("O valor mínimo para pedidos é de R$" + valorMinimoPedidosPrimeiraPosicao, "Erro"));
		validator.onErrorRedirectTo(this).acessarCarrinho();
		return;
	    }
	}

	if (formaDePagamento.equals("pagarComSaldo")) {

	    BigDecimal saldoLiberado = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(pedido.getIdCodigo(), Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR)).getSaldoLiberado();
	    BigDecimal valorASerDescontadoDoSaldo = totalPedido.add(totalPedido.multiply(BigDecimal.ZERO));

	    if (valorASerDescontadoDoSaldo.compareTo(saldoLiberado) > 0) {
		validator.add(new ValidationMessage("Você não possui saldo suficiente para pagar este pedido. Saldo atual: R$" + String.format("%.2f", saldoLiberado) + ". Valor do pedido(com tarifas): R$" + String.format("%.2f", valorASerDescontadoDoSaldo), "Erro"));
		validator.onErrorRedirectTo(this).escolherFormaDePagamento();
		return;
	    }

	    if (KitAdesao.DISTRIBUIDOR.equalsIgnoreCase(comprador.getNome_kit())) {
		if (Util.vazio(comprador.getCadBanco()) || Util.vazio(comprador.getCadTipoConta()) || Util.vazio(comprador.getCadAgencia()) || Util.vazio(comprador.getCadCCorrente())) {

		    validator.add(new ValidationMessage("Seus dados cadastrais estão incompletos. Para realizar esta ação, é necessário que todos seu cadastro esteja completo. Você deve atualizar os seus dados através do Menu: Dados Cadastrais", "Erro"));
		    validator.onErrorRedirectTo(this).escolherFormaDePagamento();
		    return;
		}
	    } else {
		if (Util.vazio(comprador.getCadEndereco()) || Util.vazio(comprador.getNumeroEndereco()) || Util.vazio(comprador.getCadBairro()) || Util.vazio(comprador.getCadCidade()) || Util.vazio(comprador.getCadUF())) {
		    validator.add(new ValidationMessage("Seu endereço está incompleto.Para realizar esta ação, é necessário que seu endereço e telefone estejam completos no cadastro. O telefone celular/whatsapp precisa estar preenchido com DDD + NUMERO. Você deve atualizar os seus dados através do Menu: Dados Cadastrais", "Erro"));
		    validator.onErrorRedirectTo(this).escolherFormaDePagamento();
		    return;
		}
	    }
	}

	pedido.setCompleted(true);
	pedido.setData(new GregorianCalendar());
	pedido.setFormaDePagamento(formaDePagamento);

	if (pedido.getTipo().equals(PedidoService.ADESAO_PA)) {
	    if (totalPedido.compareTo(new BigDecimal(2500)) < 0) {
		pedido.setTipo(PedidoService.ADESAO);
	    }
	}

	Object kitAdesao = this.sessaoGeral.getValor("kitAdesao");
	if (kitAdesao != null && kitAdesao != "") {
	    pedido.setKitIngresso((String) kitAdesao);
	}

	hibernateUtil.salvarOuAtualizar(pedido);

	for (ItemPedido itemPedido : new PedidoService(hibernateUtil).listarItensPedido(pedido)) {
	    new EstoqueService(hibernateUtil).retirarDoEstoque(itemPedido.getIdProduto(), pedido.getIdFranquia(), itemPedido.getQuantidade());
	}

	if (this.sessaoUsuario.getUsuario().getDonoDeFranquia()) {

	    result.forwardTo(this).pesquisarPedidosDosDistribuidores(null, null);

	} else {

	    if (!formaDePagamento.equalsIgnoreCase("pagamentoFinalizadoComCartaoDeCredito")) {
		result.include("sucesso", "Pedido realizado com sucesso.");
	    }

	    if (formaDePagamento.equalsIgnoreCase("pagarComBoleto")) {

		String urlPagamento = new PagSeguroService(hibernateUtil).gerarBoleto(pedido.getId(), new DecimalFormat("0.00").format(totalPedido), (Usuario) hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo())), pedido.getComprador());
		result.include("urlPagamento", urlPagamento);
		result.forwardTo("/WEB-INF/jsp/pedido/informacoesBoleto.jsp");

	    } else if (this.sessaoUsuario.getUsuario().getId() == null) {
		result.forwardTo(this).finalizarCompraLojaPessoal();
	    } else {
		result.forwardTo(this).meusPedidos();
	    }
	}
    }

    @Funcionalidade
    public void cancelarPedidoAtual() {

	Pedido pedido = selecionarPedidoAberto();

	if (pedido != null) {
	    pedido.setCompleted(true);
	    pedido.setData(new GregorianCalendar());
	    pedido.setStatus(PedidoService.CANCELADO);
	    hibernateUtil.salvarOuAtualizar(pedido);
	}

	result.forwardTo(this).meusPedidos();
    }

    @Funcionalidade
    public void pagarComCartaoDeCredito(String senderHash, String creditCardToken, String nomeCartao, String parcelas) throws Exception {

	Pedido pedido = selecionarPedidoAberto();

	new PagSeguroService(hibernateUtil).executarTransacao(senderHash, creditCardToken, String.valueOf(pedido.getId()), new DecimalFormat("0.00").format(new BigDecimal(new PedidoService(hibernateUtil).calcularTotais(pedido).getValorTotal().toString())).replaceAll(",", "."), nomeCartao, parcelas, (Usuario) hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo())));

	result.include("sucesso", "Seu cartão de crédito está passando por avaliação junto com sua operadora. Assim que o pagamento for confirmado, você receberá um e-mail de confirmação");

	this.sessaoGeral.adicionar("formaDePagamento", "pagamentoFinalizadoComCartaoDeCredito");

	concluirPedido();
    }

    @Public
    @Funcionalidade
    public void pagseguroNotificacao(String notificationCode, String tokenEV) throws Exception {

	if (tokenEV.equals(new Configuracao().retornarConfiguracao("tokenEV"))) {

	    String xml = new PagSeguroService(hibernateUtil).consultarTransacao(notificationCode);

	    try {

		String status = xml.split("<status>")[1].split("</status>")[0];

		if (status.equals("1")) {
		    result.use(json()).from("Boleto recem criado com status 1").serialize();
		    return;
		}

		if (status.equals("3") || status.equals("4")) {

		    Integer idPedido;

		    if (xml.contains("<reference>")) {
			idPedido = Integer.valueOf(xml.split("<reference>")[1].split("</reference>")[0]);
		    } else {
			idPedido = Integer.valueOf(xml.split("<items><item><id>")[1].split("</id>")[0]);
		    }

		    Pedido pedido = hibernateUtil.selecionar(new Pedido(idPedido));

		    if (pedido != null) {

			pedido.setStatus(PedidoService.PAGO);
			hibernateUtil.salvarOuAtualizar(pedido);

			Usuario usuario = hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));

			Mail.enviarEmail("Pagamento confirmado", "Seu pagamento foi confirmado. Seu pedido de código " + idPedido + " está pronto para entrega.", usuario.geteMail());
		    }

		    Mail.enviarEmail("Pagamento confirmado para o pedido: " + idPedido, "Status: " + status);

		    result.use(json()).from("Pagamento realizado com sucesso. Status pagseguro = " + status).serialize();

		} else {

		    String pagamentoNaoRealizadoMessage = "Pagamento não realizado. Status diferente de 3 ou 4. Status = " + status;

		    Mail.enviarEmail(pagamentoNaoRealizadoMessage, xml);

		    result.use(json()).from(pagamentoNaoRealizadoMessage);
		}

	    } catch (Exception e) {

		String exceptionMessage = Util.getExceptionMessage(e);

		Mail.enviarEmail("Exception no pagseguroNotificacao", "Exception: " + exceptionMessage + "<br><br><br> XML: " + xml);

		result.use(json()).from("Ocorreu um erro. Exception: " + exceptionMessage).serialize();
	    }
	} else {
	    Mail.enviarEmail("tokenEV incorreto no momento da confirmação do pedido vinda do pagSeguro", "Esperado: " + new Configuracao().retornarConfiguracao("tokenEV") + " <br> Atual: " + tokenEV);
	    result.use(json()).from("tokenEV incorreto").serialize();
	}
    }

    @Public
    @Funcionalidade
    public void pagarMeNotificacao(String tokenEV, String pedidoId, String current_status) throws Exception {

	String status = current_status;

	if (tokenEV.equals(new Configuracao().retornarConfiguracao("tokenEV"))) {

	    try {
		if (status.equalsIgnoreCase("paid")) {

		    Pedido pedido = hibernateUtil.selecionar(new Pedido(Integer.valueOf(pedidoId)));

		    if (pedido != null) {

			pedido.setStatus(PedidoService.PAGO);
			hibernateUtil.salvarOuAtualizar(pedido);
			Usuario usuario = hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));
			Mail.enviarEmail("Pagamento confirmado", "Seu pagamento foi confirmado. Seu pedido de código " + pedidoId + " está pronto para entrega.", usuario.geteMail());
		    }

		    Mail.enviarEmail("Pagamento confirmado para o pedido: " + pedidoId, "Status: " + status);

		    result.use(json()).from("Pagamento confirmado com sucesso").serialize();

		} else {

		    String pagamentoNaoRealizadoMessage = "Pagamento não realizado. Status não aceito. Status = " + status;

		    Mail.enviarEmail(pagamentoNaoRealizadoMessage, pagamentoNaoRealizadoMessage);

		    result.use(json()).from(pagamentoNaoRealizadoMessage);
		}

	    } catch (Exception e) {

		String exceptionMessage = Util.getExceptionMessage(e);

		Mail.enviarEmail("Exception no pagarMeNotificacao", "Exception: " + exceptionMessage);

		throw new Exception(exceptionMessage);
	    }
	}
    }

    @Funcionalidade
    public void meusPedidos() {

	Integer idCodigo = (Integer) this.sessaoGeral.getValor(PedidoService.ID_USUARIO_PEDIDO);

	if (idCodigo == null || idCodigo == 0) {
	    idCodigo = this.sessaoUsuario.getUsuario().getId_Codigo();
	}

	montarPedidosDTO(null, idCodigo);
    }

    @Funcionalidade
    public void finalizarCompraLojaPessoal() {
    }

    @Funcionalidade
    public void pesquisarPedidosDosDistribuidores(String status, Integer idCodigo) {

	if (verificarPermissaoPedidosAdministrativos()) {

	    if (Util.vazio(status)) {
		status = PedidoService.PENDENTE;
	    }

	    montarPedidosDTO(status, idCodigo);

	    result.include("idCodigo", idCodigo);
	    result.include("status", status);
	}
    }

    private boolean verificarPermissaoPedidosAdministrativos() {

	return this.sessaoUsuario.getUsuario().getId() != null && (this.sessaoUsuario.getUsuario().getDonoDeFranquia() || this.sessaoUsuario.getUsuario().obterInformacoesFixasUsuario().getAdministrador());
    }

    @Funcionalidade
    @Get("/pedido/alterarStatus/{idPedido}/{status}")
    public void alterarStatus(String status, Integer idPedido) throws Exception {

	if (verificarPermissaoPedidosAdministrativos()) {

	    Pedido pedido = hibernateUtil.selecionar(new Pedido(idPedido));

	    if (status.equals(PedidoService.FINALIZADO) && !pedido.getStatus().equals(PedidoService.FINALIZADO)) {

		if (pedido.getFormaDePagamento().equals("pagarComSaldo")) {

		    BigDecimal totalPedido = new PedidoService(hibernateUtil).calcularTotais(pedido).getValorTotal();

		    BigDecimal saldoLiberado = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(pedido.getIdCodigo(), Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR)).getSaldoLiberado();
		    BigDecimal valorASerDescontadoDoSaldo = totalPedido.add(totalPedido.multiply(BigDecimal.ZERO));

		    if (valorASerDescontadoDoSaldo.compareTo(saldoLiberado) > 0) {
			validator.add(new ValidationMessage("O distribuidor não possui saldo suficiente para pagar este pedido. Saldo atual: R$" + String.format("%.2f", saldoLiberado) + ". Valor do pedido(com tarifas): R$" + String.format("%.2f", valorASerDescontadoDoSaldo), "Erro"));
			validator.onErrorRedirectTo(this).escolherFormaDePagamento();
			return;
		    }
		    salvarTransferencia(valorASerDescontadoDoSaldo, pedido.getIdCodigo());
		}

		if (pedido.getTipo().equals(PedidoService.LOJA_PESSOAL)) {

		    BigDecimal comissaoTotal = BigDecimal.ZERO;
		    List<ItemPedido> itens = new PedidoService(hibernateUtil).listarItensPedido(pedido);
		    for (ItemPedido itemPedido : itens) {
			Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);
			if (!produto.getId_Produtos().equals(PedidoService.PRODUTO_FRETE_ID)) {
			    Integer quantidade = itemPedido.getQuantidade();
			    comissaoTotal = comissaoTotal.add(itemPedido.getPrecoUnitario().multiply(BigDecimal.valueOf(quantidade)).multiply(produto.getPercentualLoja().divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP)));
			}
		    }

		    Bonificacao bonificacao = new Bonificacao();
		    bonificacao.setIdCodigo(pedido.getIdCodigo());
		    bonificacao.setData(new GregorianCalendar());
		    bonificacao.setTipo(Bonificacao.BÔNUS_LOJA_VIRTUAL);
		    bonificacao.setValor(comissaoTotal);
		    hibernateUtil.salvarOuAtualizar(bonificacao);
		}

		gerarArquivoCsv(pedido.getId());
	    }

	    if (status.equals(PedidoService.CANCELADO)) {
		new PedidoService(hibernateUtil).cancelarPedido(pedido);
	    }

	    pedido.setStatus(status);
	    hibernateUtil.salvarOuAtualizar(pedido);

	    result.forwardTo(this).pesquisarPedidosDosDistribuidores(null, null);
	}
    }

    private void gerarArquivoCsv(Integer idPedido) throws Exception {

	Pedido pedido = this.hibernateUtil.selecionar(new Pedido(idPedido));

	String textoArquivo = "id_Codigo=" + pedido.getIdCodigo() + "\r\n";
	textoArquivo += "id_CDA=" + pedido.getIdFranquia() + "\r\n";
	textoArquivo += "tipo_pedido=" + pedido.getTipo() + "\r\n";
	textoArquivo += "pedido_id=" + pedido.getId() + "\r\n";

	if (pedido.getKitIngresso() != null && pedido.getKitIngresso() != "") {
	    textoArquivo += "kit_adesao=" + pedido.getKitIngresso() + "\r\n";
	}

	List<ItemPedido> itensPedido = new PedidoService(hibernateUtil).listarItensPedido(pedido);

	if (itensPedido.size() <= 0) {
	    Mail.enviarEmail("Pedido " + pedido.getId() + " sem itens de pedido no momento de gerar o arquivo no disco.", "Quantidade de itens de pedido: " + itensPedido.size());
	}

	for (ItemPedido itemPedido : itensPedido) {
	    textoArquivo += itemPedido.getIdProduto() + "=" + itemPedido.getQuantidade() + "\r\n";
	}

	ArquivoService.criarArquivoNoDisco(textoArquivo, ArquivoService.PASTA_PEDIDOS);

    }

    @Funcionalidade
    @Get("/pedido/realizarPagamento/{idPedido}")
    public void realizarPagamento(Integer idPedido) throws Exception {

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
	    BigDecimal valorASerDescontadoDoSaldo = valor.add(valor.multiply(BigDecimal.ZERO));

	    if (valorASerDescontadoDoSaldo.compareTo(saldoLiberado) > 0) {
		validator.add(new ValidationMessage(String.format("O valor a ser debitado não pode ser maior do que o saldo atual. Valor(com tarifas): R$%s. Saldo atual: R$%s", String.format("%.2f", valorASerDescontadoDoSaldo), String.format("%.2f", saldoLiberado)), "Erro"));
		validator.onErrorRedirectTo(this).realizarPagamento(idPedido);
		return;
	    }

	    salvarTransferencia(valorASerDescontadoDoSaldo, pedido.getIdCodigo());

	    result.forwardTo(this).alterarStatus(PedidoService.FINALIZADO, idPedido);
	}
    }

    private void salvarTransferencia(BigDecimal valor, Integer idCodigo) {

	if (valor.compareTo(BigDecimal.ZERO) > 0) {

	    Transferencia transferencia = new Transferencia();
	    transferencia.setData(new GregorianCalendar());
	    transferencia.setDe(idCodigo);
	    transferencia.setValor(valor);
	    transferencia.setTipo(Transferencia.TRANSFERENCIA_PARA_PAGAMENTO_DE_PEDIDO);
	    hibernateUtil.salvarOuAtualizar(transferencia);
	}
    }

    @Funcionalidade
    @Get("/pedido/verItens/{idPedido}")
    public void verItens(Integer idPedido) {

	Pedido pedido = hibernateUtil.selecionar(new Pedido(idPedido));

	List<ItemPedidoDTO> itensPedidoDTO = new ArrayList<ItemPedidoDTO>();

	for (ItemPedido itemPedido : new PedidoService(hibernateUtil).listarItensPedido(pedido)) {
	    Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);
	    Integer quantidade = itemPedido.getQuantidade();
	    itensPedidoDTO.add(new ItemPedidoDTO(produto, quantidade, itemPedido.getPrecoUnitario(), 0, null));
	}

	result.include("itensPedidoDTO", itensPedidoDTO);
	result.include("comprador", pedido.getComprador());
    }

    @Public
    @Funcionalidade
    @Get("/lojaPessoal/{apelido}")
    public void lojaPessoal(String apelido) {

	Usuario usuario = new Usuario();
	usuario.setApelido(apelido);
	usuario = this.hibernateUtil.selecionar(usuario);
	this.sessaoGeral.adicionar(PedidoService.ID_USUARIO_PEDIDO, usuario.getId_Codigo());
	Usuario usuarioFakeLojaPessoal = new Usuario();
	usuarioFakeLojaPessoal.setApelido("Bem vindo à loja de " + usuario.getApelido());
	this.sessaoUsuario.login(usuarioFakeLojaPessoal, false);

	Franquia franquia = new Franquia();
	franquia.setId_Estoque(1);
	franquia = this.hibernateUtil.selecionar(franquia);

	Pedido pedido = selecionarPedidoAberto();
	if (pedido != null && !pedido.getTipo().equals(PedidoService.LOJA_PESSOAL)) {
	    pedido.setCompleted(true);
	    pedido.setData(new GregorianCalendar());
	    pedido.setStatus(PedidoService.CANCELADO);
	    hibernateUtil.salvarOuAtualizar(pedido);
	}

	this.escolherProdutos(franquia.getId_Estoque(), usuario.getApelido(), PedidoService.RECEBER_EM_CASA, null, true, null);
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

	List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro, restricoes, Order.desc("id"));
	List<PedidoDTO> pedidosDTO = new ArrayList<PedidoDTO>();

	for (Pedido pedido : pedidos) {
	    Usuario usuario = this.hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));
	    pedidosDTO.add(new PedidoDTO(pedido, (Franquia) hibernateUtil.selecionar(new Franquia(pedido.getIdFranquia())), new PedidoService(hibernateUtil).calcularTotais(pedido).getValorTotal(), null, null, null, usuario));
	}

	result.include("pedidosDTO", pedidosDTO);
    }

    private Pedido selecionarPedidoAberto() {

	Integer idCodigo = (Integer) this.sessaoGeral.getValor(PedidoService.ID_USUARIO_PEDIDO);

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
