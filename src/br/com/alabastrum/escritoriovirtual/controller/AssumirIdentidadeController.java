package br.com.alabastrum.escritoriovirtual.controller;

import org.hibernate.criterion.MatchMode;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class AssumirIdentidadeController {

	private Result result;
	private SessaoUsuario sessaoUsuario;
	private HibernateUtil hibernateUtil;

	public AssumirIdentidadeController(Result result, SessaoUsuario sessaoUsuario, HibernateUtil hibernateUtil) {

		this.result = result;
		this.sessaoUsuario = sessaoUsuario;
		this.hibernateUtil = hibernateUtil;
	}

	@Funcionalidade(administrativa = "true")
	public void acessarTelaAssumirIdentidade() {

	}

	@Funcionalidade(administrativa = "true")
	public void assumirIdentidade(Integer codigo) throws Exception {

		Usuario usuario = new Usuario();
		usuario.setId_Codigo(codigo);

		usuario = this.hibernateUtil.selecionar(usuario, MatchMode.EXACT);

		this.sessaoUsuario.login(usuario);

		result.forwardTo(HomeController.class).home();
	}
}
