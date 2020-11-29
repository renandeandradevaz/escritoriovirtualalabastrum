package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.SolicitacaoSaqueDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.SolicitacaoSaque;
import br.com.alabastrum.escritoriovirtual.modelo.Transferencia;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.ExtratoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoGeral;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class SolicitacaoSaqueController {

    private Result result;
    private HibernateUtil hibernateUtil;
    private SessaoUsuario sessaoUsuario;
    private SessaoGeral sessaoGeral;
    private Validator validator;

    public SolicitacaoSaqueController(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario, Validator validator, SessaoGeral sessaoGeral) {
	this.result = result;
	this.hibernateUtil = hibernateUtil;
	this.sessaoUsuario = sessaoUsuario;
	this.sessaoGeral = sessaoGeral;
	this.validator = validator;
    }

    @Funcionalidade
    public void acessarTelaSolicitacaoSaque() throws Exception {

	if (this.sessaoUsuario.getUsuario().getDocumentacaoEnviada() == null || this.sessaoUsuario.getUsuario().getDocumentacaoEnviada().equals(0)) {
	    validator.add(new ValidationMessage("Você ainda não enviou sua documentação ou ela ainda está em análise.", "Erro"));
	    validator.onErrorRedirectTo(HomeController.class).home();
	    return;
	}

	if (Util.vazio(this.sessaoUsuario.getUsuario().getCadBanco()) || Util.vazio(this.sessaoUsuario.getUsuario().getCadTipoConta()) || Util.vazio(this.sessaoUsuario.getUsuario().getCadAgencia()) || Util.vazio(this.sessaoUsuario.getUsuario().getCadCCorrente())) {

	    validator.add(new ValidationMessage("Seus dados cadastrais estão incompletos. Para realizar esta ação, é necessário que todos seu cadastro esteja completo. Você deve atualizar os seus dados através do Menu: Dados Cadastrais", "Erro"));
	    validator.onErrorRedirectTo(HomeController.class).home();
	    return;
	}

	Integer saldoLiberado = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(this.sessaoUsuario.getUsuario().getId_Codigo(), Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR)).getSaldoLiberado().intValue();
	result.include("saldo", saldoLiberado);
    }

    @Funcionalidade(administrativa = "true")
    public void solicitacoesSaqueAdministrativa(String status) throws Exception {

	if (Util.vazio(status)) {
	    status = "PENDENTE";
	}

	SolicitacaoSaque solicitacaoSaqueFiltro = new SolicitacaoSaque();
	solicitacaoSaqueFiltro.setStatus(status);
	List<SolicitacaoSaque> solicitacoesBanco = this.hibernateUtil.buscar(solicitacaoSaqueFiltro);

	List<SolicitacaoSaqueDTO> solicitacoes = new ArrayList<SolicitacaoSaqueDTO>();
	for (SolicitacaoSaque solicitacaoSaque : solicitacoesBanco) {
	    SolicitacaoSaqueDTO solicitacaoSaqueDTO = new SolicitacaoSaqueDTO();
	    solicitacaoSaqueDTO.setId(solicitacaoSaque.getId());
	    solicitacaoSaqueDTO.setUsuario((Usuario) this.hibernateUtil.selecionar(new Usuario(solicitacaoSaque.getIdCodigo())));

	    if (Util.preenchido(solicitacaoSaque.getIdCodigoAdmAprovou())) {
		solicitacaoSaqueDTO.setUsuarioAdm((Usuario) this.hibernateUtil.selecionar(new Usuario(solicitacaoSaque.getIdCodigoAdmAprovou())));
	    }

	    solicitacaoSaqueDTO.setStatus(solicitacaoSaque.getStatus());
	    solicitacaoSaqueDTO.setData(solicitacaoSaque.getData());
	    solicitacaoSaqueDTO.setValorBrutoSolicitado(solicitacaoSaque.getValorBrutoSolicitado());
	    solicitacaoSaqueDTO.setValorFinalComDescontos(solicitacaoSaque.getValorFinalComDescontos());
	    solicitacoes.add(solicitacaoSaqueDTO);
	}

	result.include("solicitacoes", solicitacoes);
	result.include("status", status);
	result.include("pesquisa", true);
    }

    @Funcionalidade(administrativa = "true")
    @Get("/solicitacaoSaque/cancelarSolicitacao/{idSolicitacao}")
    public void cancelarSolicitacao(Integer idSolicitacao) throws Exception {

	SolicitacaoSaque solicitacaoSaque = new SolicitacaoSaque();
	solicitacaoSaque.setId(idSolicitacao);
	solicitacaoSaque = this.hibernateUtil.selecionar(solicitacaoSaque);
	this.hibernateUtil.deletar(solicitacaoSaque);
	result.forwardTo(this).solicitacoesSaqueAdministrativa("PENDENTE");
    }

    @Funcionalidade(administrativa = "true")
    @Get("/solicitacaoSaque/confirmarSolicitacao/{idSolicitacao}")
    public void confirmarSolicitacao(Integer idSolicitacao) throws Exception {

	SolicitacaoSaque solicitacaoSaque = new SolicitacaoSaque();
	solicitacaoSaque.setId(idSolicitacao);
	solicitacaoSaque = this.hibernateUtil.selecionar(solicitacaoSaque);

	Integer saldoLiberado = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(solicitacaoSaque.getIdCodigo(), Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR)).getSaldoLiberado().intValue();

	if (saldoLiberado.intValue() < solicitacaoSaque.getValorBrutoSolicitado().intValue()) {
	    validator.add(new ValidationMessage("O saldo da pessoa é menor do que o valor solicitado para saque", "Erro"));
	    validator.onErrorRedirectTo(this).solicitacoesSaqueAdministrativa("PENDENTE");
	    return;
	}

	Transferencia transferencia = new Transferencia();
	transferencia.setData(new GregorianCalendar());
	transferencia.setDe(solicitacaoSaque.getIdCodigo());
	transferencia.setValor(solicitacaoSaque.getValorBrutoSolicitado());
	transferencia.setTipo(Transferencia.TRANSFERENCIA_PARA_SAQUE);
	hibernateUtil.salvarOuAtualizar(transferencia);

	solicitacaoSaque.setStatus("FINALIZADO");
	solicitacaoSaque.setIdCodigoAdmAprovou(this.sessaoUsuario.getUsuario().getId_Codigo());
	this.hibernateUtil.salvarOuAtualizar(solicitacaoSaque);
	result.forwardTo(this).solicitacoesSaqueAdministrativa("PENDENTE");
    }

    @Funcionalidade
    public void solicitarSaque(String valorString) throws Exception {

	if (Util.vazio(valorString)) {
	    valorString = "0";
	}

	valorString = valorString.replaceAll("\\.", "").replaceAll(",", "");

	Integer valor = Integer.valueOf(valorString);

	if (valor < 100) {
	    validator.add(new ValidationMessage("O valor mínimo é 100 reais", "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaSolicitacaoSaque();
	    return;
	}

	Integer saldoLiberado = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(this.sessaoUsuario.getUsuario().getId_Codigo(), Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR)).getSaldoLiberado().intValue();

	if (valor > saldoLiberado) {
	    validator.add(new ValidationMessage("Você está tentando sacar um valor maior do que tem disponível para saque", "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaSolicitacaoSaque();
	    return;
	}

	BigDecimal valorFinalComDescontos = calcularValorFinalComDescontos(valor);
	result.include("valorFinalComDescontos", valorFinalComDescontos);

	this.sessaoGeral.adicionar("valorSolicitacaoSaque", valor);
    }

    private BigDecimal calcularValorFinalComDescontos(Integer valor) {

	BigDecimal valorBigDecimal = new BigDecimal(valor);
	BigDecimal descontoTarifaBancaria = new BigDecimal("10");
	// BigDecimal descontoINSS = valorBigDecimal.multiply(Constants.TARIFA_INSS);
	BigDecimal descontoINSS = valorBigDecimal.multiply(BigDecimal.ZERO);
	// BigDecimal descontoImpostoDeRenda = valorBigDecimal.multiply(new
	// BigDecimal("0.10"));
	BigDecimal descontoImpostoDeRenda = BigDecimal.ZERO;

	return valorBigDecimal.subtract(descontoTarifaBancaria).subtract(descontoINSS).subtract(descontoImpostoDeRenda);
    }

    @Funcionalidade
    public void confirmar() throws Exception {

	Integer valorBrutoSolicitado = (Integer) this.sessaoGeral.getValor("valorSolicitacaoSaque");
	BigDecimal valorFinalComDescontos = calcularValorFinalComDescontos(valorBrutoSolicitado);

	SolicitacaoSaque solicitacaoSaque = new SolicitacaoSaque();
	solicitacaoSaque.setIdCodigo(this.sessaoUsuario.getUsuario().getId_Codigo());
	solicitacaoSaque.setData(new GregorianCalendar());
	solicitacaoSaque.setStatus("PENDENTE");
	solicitacaoSaque.setValorBrutoSolicitado(BigDecimal.valueOf(valorBrutoSolicitado));
	solicitacaoSaque.setValorFinalComDescontos(valorFinalComDescontos);
	this.hibernateUtil.salvarOuAtualizar(solicitacaoSaque);

	Mail.enviarEmail("Nova solicitação de saque", "O distribuidor " + this.sessaoUsuario.getUsuario().getApelido() + " fez uma solicitação de saque. Acesse o EV para ver todos os detalhes", "financeiro@dunastes.com.br");

	result.include("sucesso", "Solicitação de saque feita com sucesso. Aguarde nossa análise e transferência");

	result.redirectTo(HomeController.class).home();
    }
}
