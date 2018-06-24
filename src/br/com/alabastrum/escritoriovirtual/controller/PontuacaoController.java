package br.com.alabastrum.escritoriovirtual.controller;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.service.PontuacaoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class PontuacaoController {

	private Result result;
	private HibernateUtil hibernateUtil;
	private SessaoUsuario sessaoUsuario;

	public PontuacaoController(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario) {

		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.sessaoUsuario = sessaoUsuario;
	}

	@Funcionalidade
	public void acessarTelaPontuacao() {

		result.include("pontuacaoDTO", new PontuacaoService(hibernateUtil).calcularPontuacoes(this.sessaoUsuario.getUsuario().getId_Codigo()));
	}
}
