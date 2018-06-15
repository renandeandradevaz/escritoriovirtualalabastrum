package br.com.alabastrum.escritoriovirtual.controller;

import java.io.File;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;

@Resource
public class DownloadController {

	@Funcionalidade
	@Get("/download/imagem/produto/{idProduto}")
	public File downloadImagemProduto(String idProduto) {
		return new File(ArquivoService.PASTA_IMAGEM_PRODUTOS + idProduto + ".jpg");
	}
}
