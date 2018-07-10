package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.SaldoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Transferencia;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.ExtratoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class TransferenciaController {

	private static final Integer DIA_MAXIMO_TRANSFERENCIA_ALABASTRUM_CARD = 5;
	private static final BigDecimal VALOR_MINIMO_TRANSFERENCIA_ALABASTRUM_CARD = new BigDecimal(150);

	private Result result;
	private HibernateUtil hibernateUtil;
	private SessaoUsuario sessaoUsuario;
	private Validator validator;

	public TransferenciaController(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario, Validator validator) {
		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.sessaoUsuario = sessaoUsuario;
		this.validator = validator;
	}

	@Funcionalidade
	public void acessarTelaTransferencia() {
	}

	@Funcionalidade
	public void acessarTelaTransferenciaParaAlabastrumCard() {
		result.include("saldo", gerarSaldoAtual());
	}

	@Funcionalidade
	public void acessarTelaTransferenciaParaOutroDistribuidor() {
		result.include("saldo", gerarSaldoAtual());
	}

	@Funcionalidade
	public void transferirParaOutroDistribuidor(Integer codigoOutroDistribuidor, BigDecimal valor) {

		BigDecimal saldo = gerarSaldoAtual();

		if (valor.compareTo(saldo) > 0 || valor.compareTo(BigDecimal.ZERO) <= 0) {
			validator.add(new ValidationMessage("O valor a ser transferido não pode ser maior do que o seu saldo atual", "Erro"));
			validator.onErrorRedirectTo(this).acessarTelaTransferenciaParaOutroDistribuidor();
			return;
		}

		if (codigoOutroDistribuidor == 0 || hibernateUtil.selecionar(new Usuario(codigoOutroDistribuidor)) == null) {

			validator.add(new ValidationMessage("O distribuidor com código " + codigoOutroDistribuidor + " não foi encontrado", "Erro"));
			validator.onErrorRedirectTo(this).acessarTelaTransferenciaParaOutroDistribuidor();
			return;
		}

		if (codigoOutroDistribuidor == this.sessaoUsuario.getUsuario().getId_Codigo()) {

			validator.add(new ValidationMessage("Código inválido", "Erro"));
			validator.onErrorRedirectTo(this).acessarTelaTransferenciaParaOutroDistribuidor();
			return;
		}

		Transferencia transferencia = new Transferencia();
		transferencia.setData(new GregorianCalendar());
		transferencia.setDe(this.sessaoUsuario.getUsuario().getId_Codigo());
		transferencia.setPara(codigoOutroDistribuidor);
		transferencia.setValor(valor);
		transferencia.setTipo(Transferencia.TRANSFERENCIA_PARA_OUTRO_DISTRIBUIDOR);
		hibernateUtil.salvarOuAtualizar(transferencia);

		result.redirectTo(ExtratoController.class).gerarExtrato(Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR));
	}

	@Funcionalidade
	public void transferirParaAlabastrumCard(BigDecimal valor) {

		BigDecimal saldo = gerarSaldoAtual();

		if (valor.compareTo(saldo) > 0) {
			validator.add(new ValidationMessage("O valor a ser transferido não pode ser maior do que o seu saldo atual", "Erro"));
			validator.onErrorRedirectTo(this).acessarTelaTransferenciaParaAlabastrumCard();
			return;
		}

		if (valor.compareTo(VALOR_MINIMO_TRANSFERENCIA_ALABASTRUM_CARD) < 0) {
			validator.add(new ValidationMessage("O valor a ser transferido não pode ser menor que " + VALOR_MINIMO_TRANSFERENCIA_ALABASTRUM_CARD + " reais", "Erro"));
			validator.onErrorRedirectTo(this).acessarTelaTransferenciaParaAlabastrumCard();
			return;
		}

		if (Util.getTempoCorrenteAMeiaNoite().get(Calendar.DAY_OF_MONTH) > DIA_MAXIMO_TRANSFERENCIA_ALABASTRUM_CARD) {
			validator.add(new ValidationMessage("A transferência só pode ser feita até o dia " + DIA_MAXIMO_TRANSFERENCIA_ALABASTRUM_CARD, "Erro"));
			validator.onErrorRedirectTo(this).acessarTelaTransferenciaParaAlabastrumCard();
			return;
		}

		Transferencia transferencia = new Transferencia();
		transferencia.setData(new GregorianCalendar());
		transferencia.setDe(this.sessaoUsuario.getUsuario().getId_Codigo());
		transferencia.setValor(valor);
		transferencia.setTipo(Transferencia.TRANSFERENCIA_PARA_ALABASTRUM_CARD);
		hibernateUtil.salvarOuAtualizar(transferencia);

		result.redirectTo(ExtratoController.class).gerarExtrato(Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR));
	}

	@Funcionalidade(administrativa = "true")
	public void acessarTelaTransferenciasParaAlabastrumCardAdministrativa() {

		Transferencia transferenciaFiltro = new Transferencia();
		transferenciaFiltro.setTipo(Transferencia.TRANSFERENCIA_PARA_ALABASTRUM_CARD);
		result.include("transferencias", hibernateUtil.buscar(transferenciaFiltro));
	}

	private BigDecimal gerarSaldoAtual() {

		Integer idCodigo = this.sessaoUsuario.getUsuario().getId_Codigo();
		SaldoDTO saldoDTO = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(idCodigo, Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR));
		return saldoDTO.getSaldoAtual();
	}
}
