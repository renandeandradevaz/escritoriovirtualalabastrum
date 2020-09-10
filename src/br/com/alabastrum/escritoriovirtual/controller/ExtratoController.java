package br.com.alabastrum.escritoriovirtual.controller;

import java.util.Calendar;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.service.ExtratoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ExtratoController {

    private Result result;
    private HibernateUtil hibernateUtil;
    private SessaoUsuario sessaoUsuario;

    public ExtratoController(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
	this.sessaoUsuario = sessaoUsuario;
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
    }
}
