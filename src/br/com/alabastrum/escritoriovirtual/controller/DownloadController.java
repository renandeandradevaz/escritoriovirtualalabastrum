package br.com.alabastrum.escritoriovirtual.controller;

import java.io.File;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;

@Resource
public class DownloadController {

    @Funcionalidade
    public void downloads() {
    }

    @Funcionalidade
    @Get("/downloadArquivo/{idArquivo}")
    public File downloadArquivo(String idArquivo) {
	return new File(ArquivoService.PASTA_ARQUIVOS + idArquivo);
    }

    @Funcionalidade
    @Get("/download/imagem/produto/{idProduto}")
    public File downloadImagemProduto(String idProduto) {

	File file = new File(ArquivoService.PASTA_IMAGEM_PRODUTOS + idProduto);

	if (file.exists()) {
	    return file;
	}
	return new File(ArquivoService.PASTA_IMAGEM_PRODUTOS + "imagem-nao-disponivel.jpg");
    }
}
