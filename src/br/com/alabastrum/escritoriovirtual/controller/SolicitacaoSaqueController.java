package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.Calendar;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.SolicitacaoSaque;
import br.com.alabastrum.escritoriovirtual.service.ExtratoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoGeral;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Util;
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

	Integer saldoLiberado = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(this.sessaoUsuario.getUsuario().getId_Codigo(), Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR)).getSaldoLiberado().intValue();
	result.include("saldo", saldoLiberado);
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
	BigDecimal descontoINSS = valorBigDecimal.multiply(new BigDecimal("0.11"));
	BigDecimal descontoImpostoDeRenda = valorBigDecimal.multiply(new BigDecimal("0.10"));

	return valorBigDecimal.subtract(descontoTarifaBancaria).subtract(descontoINSS).subtract(descontoImpostoDeRenda);
    }

    @Funcionalidade
    public void confirmar() throws Exception {

	Integer valorBrutoSolicitado = (Integer) this.sessaoGeral.getValor("valorSolicitacaoSaque");
	BigDecimal valorFinalComDescontos = calcularValorFinalComDescontos(valorBrutoSolicitado);

	SolicitacaoSaque solicitacaoSaque = new SolicitacaoSaque();
	solicitacaoSaque.setIdCodigo(this.sessaoUsuario.getUsuario().getId_Codigo());
	solicitacaoSaque.setValorBrutoSolicitado(BigDecimal.valueOf(valorBrutoSolicitado));
	solicitacaoSaque.setValorFinalComDescontos(valorFinalComDescontos);
	this.hibernateUtil.salvarOuAtualizar(solicitacaoSaque);

	result.include("sucesso", "Solicitação de saque feita com sucesso. Aguarde nossa análise e transferência");

	result.redirectTo(HomeController.class).home();
    }
}
