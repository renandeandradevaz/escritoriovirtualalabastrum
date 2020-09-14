package br.com.alabastrum.escritoriovirtual.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.service.ExtratoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoGeral;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ExtratoController {

    private Result result;
    private HibernateUtil hibernateUtil;
    private SessaoUsuario sessaoUsuario;
    private SessaoGeral sessaoGeral;

    public ExtratoController(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario, SessaoGeral sessaoGeral) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
	this.sessaoUsuario = sessaoUsuario;
	this.sessaoGeral = sessaoGeral;
    }

    @Funcionalidade
    public void acessarTelaExtrato(Integer mes, Integer ano) {

	if (mes == null || ano == null) {
	    mes = Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH);
	    ano = Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR);
	}

	result.include("mes", mes);
	result.include("ano", ano);
    }

    @Funcionalidade
    public void gerarExtrato(Integer mes, Integer ano) throws Exception {
	result.include("extrato", new ExtratoService(hibernateUtil).gerarSaldoEExtrato(this.sessaoUsuario.getUsuario().getId_Codigo(), mes, ano));
	result.forwardTo(this).acessarTelaExtrato(mes, ano);

	this.sessaoGeral.adicionar("mesExtrato", mes);
	this.sessaoGeral.adicionar("anoExtrato", ano);
    }

    @Funcionalidade
    public void detalharBonificacao(String bonificacao) throws Exception {

	List<ExtratoDTO> extrato = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(this.sessaoUsuario.getUsuario().getId_Codigo(), (Integer) this.sessaoGeral.getValor("mesExtrato"), (Integer) this.sessaoGeral.getValor("anoExtrato")).getExtratoDoMes();

	List<ExtratoDTO> extratoDetalhado = new ArrayList<ExtratoDTO>();

	for (ExtratoDTO extratoDTO : extrato) {
	    if (extratoDTO.getDiscriminador().equals(bonificacao)) {
		extratoDetalhado.add(extratoDTO);
	    }
	}

	result.include("extratoDetalhado", extratoDetalhado);
    }
}
