package br.com.alabastrum.escritoriovirtual.controller;

import br.com.alabastrum.escritoriovirtual.anotacoes.Public;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.CartaoVisita;
import br.com.alabastrum.escritoriovirtual.util.Util;
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

		CartaoVisita cartaoVisitaFiltro = new CartaoVisita();
		cartaoVisitaFiltro.setCodigo(cartaoVisita.getCodigo());
		if (this.hibernateUtil.contar(cartaoVisitaFiltro).equals(0)) {

			cartaoVisita.setSite(Util.removeHttp(cartaoVisita.getSite()));
			cartaoVisita.setCatalogo(Util.removeHttp(cartaoVisita.getCatalogo()));
			cartaoVisita.setFacebook(Util.removeHttp(cartaoVisita.getFacebook()));
			cartaoVisita.setInstagram(Util.removeHttp(cartaoVisita.getInstagram()));
			cartaoVisita.setTwitter(Util.removeHttp(cartaoVisita.getTwitter()));
			cartaoVisita.setYoutube(Util.removeHttp(cartaoVisita.getYoutube()));
			cartaoVisita.setTiktok(Util.removeHttp(cartaoVisita.getTiktok()));
			cartaoVisita.setLinkedin(Util.removeHttp(cartaoVisita.getLinkedin()));
			cartaoVisita.setLinkCadastro(Util.removeHttp(cartaoVisita.getLinkCadastro()));

			cartaoVisita.setWhatsapp(cartaoVisita.getWhatsapp().replaceAll(" ", "").replaceAll("-", "")
					.replaceAll("(", "").replaceAll(")", ""));
			cartaoVisita
					.setWhatsapp(cartaoVisita.getWhatsapp().startsWith("55") ? cartaoVisita.getWhatsapp().substring(2)
							: cartaoVisita.getWhatsapp());

			this.hibernateUtil.salvarOuAtualizar(cartaoVisita);
		}

		result.redirectTo(this).cartaoVisita(cartaoVisita.getCodigo());
	}
}
