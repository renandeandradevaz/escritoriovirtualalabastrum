package br.com.alabastrum.escritoriovirtual.controller;

import static br.com.caelum.vraptor.view.Results.json;

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
	public void teste(Integer codigo) {

	}

	@Public
	public void obterNomeDistribuidor(Integer codigo) {

		Usuario usuario = hibernateUtil.selecionar(new Usuario(codigo));

		if (usuario == null) {
			result.use(json()).from("Código inexistente").serialize();

		} else {
			result.use(json()).from(usuario.getvNome()).serialize();
		}
	}
}