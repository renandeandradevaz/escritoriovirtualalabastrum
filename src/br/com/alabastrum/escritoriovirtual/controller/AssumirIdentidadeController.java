package br.com.alabastrum.escritoriovirtual.controller;

import org.hibernate.criterion.MatchMode;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.InformacoesFixasUsuario;
import br.com.alabastrum.escritoriovirtual.modelo.KitAdesao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class AssumirIdentidadeController {

    private Result result;
    private SessaoUsuario sessaoUsuario;
    private HibernateUtil hibernateUtil;
    private Validator validator;

    public AssumirIdentidadeController(Result result, SessaoUsuario sessaoUsuario, HibernateUtil hibernateUtil, Validator validator) {

	this.result = result;
	this.sessaoUsuario = sessaoUsuario;
	this.hibernateUtil = hibernateUtil;
	this.validator = validator;
    }

    @Funcionalidade(administrativa = "true")
    public void acessarTelaAssumirIdentidade() {

    }

    @Funcionalidade(administrativa = "true")
    public void assumirIdentidade(String nickname) throws Exception {

	Usuario usuario = new Usuario();
	usuario.setApelido(nickname);
	usuario = this.hibernateUtil.selecionar(usuario, MatchMode.EXACT);

	if (usuario == null) {

	    validator.add(new ValidationMessage("O usuário com nickname " + nickname + " não existe no Escritório Virtual. Entre em contato com o suporte da empresa", "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaAssumirIdentidade();
	    return;

	}

	usuario.setInformacoesFixasUsuario(new InformacoesFixasUsuario());
	usuario.getInformacoesFixasUsuario().setAdministrador(true);
	usuario.setNome_kit(KitAdesao.DISTRIBUIDOR);
	this.sessaoUsuario.login(usuario, true);

	result.forwardTo(HomeController.class).home();
    }
}
