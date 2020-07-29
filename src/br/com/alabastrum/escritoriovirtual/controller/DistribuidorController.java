package br.com.alabastrum.escritoriovirtual.controller;

import static br.com.caelum.vraptor.view.Results.json;

import java.text.Normalizer;

import br.com.alabastrum.escritoriovirtual.anotacoes.Public;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class DistribuidorController {

    private Result result;
    private HibernateUtil hibernateUtil;

    public DistribuidorController(Result result, HibernateUtil hibernateUtil) {
	this.result = result;
	this.hibernateUtil = hibernateUtil;
    }

    @Public
    public void obterNomeDistribuidor(String nickname) {

	Usuario usuarioFiltro = new Usuario();
	usuarioFiltro.setApelido(Normalizer.normalize(nickname.toLowerCase().replaceAll(" ", ""), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));

	if (hibernateUtil.contar(usuarioFiltro) != 1) {
	    result.use(json()).from("Código inexistente").serialize();
	    return;
	}

	Usuario usuario = hibernateUtil.selecionar(usuarioFiltro);
	result.use(json()).from(usuario.getvNome()).serialize();
    }
}