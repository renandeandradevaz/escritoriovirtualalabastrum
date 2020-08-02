package br.com.alabastrum.escritoriovirtual.service;

import java.io.PrintWriter;

import org.joda.time.DateTime;

public class ArquivoService {

    public static final String PASTA_RAIZ = "/root/Dropbox/";
    public static final String PASTA_ATUALIZACAO_CSV_PERIODO_LONGO = PASTA_RAIZ + "do-desktop-para-o-escritorio/csvs-periodo-longo";
    public static final String PASTA_ATUALIZACAO_CSV_PERIODO_CURTO = PASTA_RAIZ + "do-desktop-para-o-escritorio/csvs-periodo-curto";
    public static final String PASTA_BACKUP_CSV_PERIODO_LONGO = PASTA_RAIZ + "backup-csvs-periodo-longo/";
    public static final String PASTA_BACKUP_CSV_PERIODO_CURTO = PASTA_RAIZ + "backup-csvs-periodo-curto/";
    public static final String PASTA_IMAGEM_PRODUTOS = PASTA_RAIZ + "do-desktop-para-o-escritorio/imagens-produtos/";
    public static final String PASTA_ARQUIVOS = PASTA_RAIZ + "do-desktop-para-o-escritorio/arquivos/";
    public static final String PASTA_PRE_CADASTRO = PASTA_RAIZ + "do-escritorio-para-o-desktop/pre-cadastro-de-distribuidor-pelo-site/";
    public static final String PASTA_ATUALIZACAO_DADOS = PASTA_RAIZ + "do-escritorio-para-o-desktop/atualizacao-dados/";
    public static final String PASTA_CONFIGURACOES = PASTA_RAIZ + "do-escritorio-para-o-desktop/configuracoes/";
    public static final String PASTA_PEDIDOS = PASTA_RAIZ + "do-escritorio-para-o-desktop/pedidos/";

    public static void criarArquivoNoDisco(String texto, String caminhoDaPastaNoDisco) throws Exception {

	PrintWriter writer = new PrintWriter(caminhoDaPastaNoDisco + new DateTime().toString("dd-MM-YYYY_HH-mm-ss") + ".txt", "UTF-8");
	writer.println(texto);
	writer.close();
    }
}
