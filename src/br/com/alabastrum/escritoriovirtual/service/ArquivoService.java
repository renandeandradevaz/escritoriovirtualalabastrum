package br.com.alabastrum.escritoriovirtual.service;

import java.io.PrintWriter;

import org.joda.time.DateTime;

public class ArquivoService {

	public static final String PASTA_RAIZ = "/root/Dropbox/";
	public static final String PASTA_ATUALIZACAO_CSV = PASTA_RAIZ + "do-desktop-para-o-escritorio/csvs";
	public static final String CAMINHO_PASTA_PRE_CADASTRO = PASTA_RAIZ + "do-escritorio-para-o-desktop/pre-cadastro-de-distribuidor-pelo-site/";
	public static final String CAMINHO_PASTA_CONFIGURACOES = PASTA_RAIZ + "do-escritorio-para-o-desktop/configuracoes/";

	public static void criarArquivoNoDisco(String texto, String caminhoDaPastaNoDisco) throws Exception {

		PrintWriter writer = new PrintWriter(caminhoDaPastaNoDisco + new DateTime().toString("dd-MM-YYYY_HH-mm-ss") + ".txt", "UTF-8");
		writer.println(texto);
		writer.close();
	}
}
