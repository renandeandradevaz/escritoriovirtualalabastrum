package br.com.alabastrum.escritoriovirtual.controller;

import br.com.alabastrum.escritoriovirtual.anotacoes.Public;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.CartaoVisita;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class CartaoVisitaController {

	private final Result result;
	private HibernateUtil hibernateUtil;

	public CartaoVisitaController(Result result, HibernateUtil hibernateUtil) {
		this.result = result;
		this.hibernateUtil = hibernateUtil;
	}

	@Public
	@Get("/cartao-visita/{codigo}")
	public void cartaoVisita(String codigo) {

		CartaoVisita cartaoVisita = new CartaoVisita();
		cartaoVisita.setCodigo(codigo);
		result.include("cartaoVisita", this.hibernateUtil.selecionar(cartaoVisita));
		result.include("codigo", codigo);
	}

	@Public
	@Post("/cartao-visita/salvarCartaoVisita")
	public void salvarCartaoVisita(CartaoVisita cartaoVisita) {
		this.hibernateUtil.salvarOuAtualizar(cartaoVisita);
		result.redirectTo(this).cartaoVisita(cartaoVisita.getCodigo());
	}
}
