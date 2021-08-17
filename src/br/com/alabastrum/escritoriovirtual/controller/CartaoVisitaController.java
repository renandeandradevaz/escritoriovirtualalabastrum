package br.com.alabastrum.escritoriovirtual.controller;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import br.com.alabastrum.escritoriovirtual.anotacoes.Public;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.CartaoVisita;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

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
	public void salvarCartaoVisita(CartaoVisita cartaoVisita, UploadedFile foto) throws Exception {

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
					.replaceAll("\\(", "").replaceAll("\\)", ""));
			cartaoVisita
					.setWhatsapp(cartaoVisita.getWhatsapp().startsWith("55") ? cartaoVisita.getWhatsapp().substring(2)
							: cartaoVisita.getWhatsapp());

			cartaoVisita.setCelular(cartaoVisita.getCelular().replaceAll(" ", "").replaceAll("-", "")
					.replaceAll("\\(", "").replaceAll("\\)", ""));
			cartaoVisita.setCelular(cartaoVisita.getCelular().startsWith("55") ? cartaoVisita.getCelular().substring(2)
					: cartaoVisita.getCelular());

			if (foto != null) {
				File fotoSalva = new File("/dnt-connection-fotos", cartaoVisita.getCodigo());
				IOUtils.copyLarge(foto.getFile(), new FileOutputStream(fotoSalva));
			}

			this.hibernateUtil.salvarOuAtualizar(cartaoVisita);
		}

		result.redirectTo(this).cartaoVisita(cartaoVisita.getCodigo());
	}

	@Public
	@Get("/cartao-visita/downloadFoto/{codigo}")
	public File downloadFoto(String codigo) {

		File file = new File("/dnt-connection-fotos", codigo);

		if (file.exists()) {
			return file;
		}
		return new File(ArquivoService.PASTA_IMAGEM_PRODUTOS + "imagem-nao-disponivel.jpg");
	}

	@Public
	@Get("/cartao-visita/downloadVcard/{codigo}")
	public Download downloadVcard(String codigo) throws Exception {

		CartaoVisita cartaoVisita = new CartaoVisita();
		cartaoVisita.setCodigo(codigo);
		cartaoVisita = this.hibernateUtil.selecionar(cartaoVisita);

		String vcfContentFile = "BEGIN:VCARD\n" + "VERSION:3.0\n" + "N:" + cartaoVisita.getNome() + "\n" + "FN:"
				+ cartaoVisita.getNome() + "\n" + "TEL;TYPE=WORK,MSG:+55" + cartaoVisita.getCelular() + "\n" + "TITLE:"
				+ cartaoVisita.getCargo() + "\n" + "URL;TYPE=WHATSAPP:https://api.whatsapp.com/send?phone=+55"
				+ cartaoVisita.getWhatsapp() + "\n" + "URL;TYPE=TELEGRAM:https://telegram.me/@"
				+ cartaoVisita.getTelegram() + "\n" + "URL;TYPE=FACEBOOK:https://" + cartaoVisita.getFacebook() + "\n"
				+ "EMAIL:" + cartaoVisita.getEmail() + "\n" + "URL;TYPE=YOUTUBE:https://" + cartaoVisita.getYoutube()
				+ "\n" + "URL;TYPE=INSTAGRAM:https://" + cartaoVisita.getInstagram() + "\n"
				+ "URL;TYPE=LINKEDIN:https://" + cartaoVisita.getLinkedin() + "\n" + "URL;TYPE=WEBSITE:https://"
				+ cartaoVisita.getSite() + "\n" + "END:VCARD";

		FileUtils.writeStringToFile(new File("/tmp/" + codigo + ".vcf"), vcfContentFile);

		return new ByteArrayDownload(FileUtils.readFileToByteArray(new File("/tmp/" + codigo + ".vcf")), "text/vcard",
				codigo + ".vcf");
	}
}
