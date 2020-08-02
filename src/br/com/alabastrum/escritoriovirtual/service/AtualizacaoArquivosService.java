package br.com.alabastrum.escritoriovirtual.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import au.com.bytecode.opencsv.CSVReader;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Adesao;
import br.com.alabastrum.escritoriovirtual.modelo.Categoria;
import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroAtividade;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroDivisaoLucro;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroIngresso;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroUnilevel;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroVip;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.modelo.Produto;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.modelo.ReceitaDivisaoLucro;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class AtualizacaoArquivosService {

    private HibernateUtil hibernateUtil;

    public AtualizacaoArquivosService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public void processarArquivos() throws Exception {

	processarCSVRelacionamentos();
	processarCSVPosicoes();
	processarCSVQualificacao();
	processarCSVPontuacao();
	processarCSVParametroIngresso();
	processarCSVParametroAtividade();
	processarCSVParametroUnilevel();
	processarCSVParametroDivisaoLucro();
	processarCSVReceitaDivisaoLucro();
	processarCSVParametroVip();
	processarCSVFranquia();
	processarCSVCategoria();
	processarCSVProduto();
	processarCSVAdesao();
    }

    private void processarCSVRelacionamentos() throws Exception {

	CSVReader reader = lerArquivo("tblRelacionamentos.csv");
	List<Usuario> usuarios = new ArrayList<Usuario>();
	int quantidadeLinhas = preencherObjeto(reader, usuarios, "Usuario");
	this.hibernateUtil.executarSQL("delete from usuario");
	System.out.println("Quantidade de usuários no banco após delete: " + this.hibernateUtil.contar(new Usuario()));
	Map<Integer, Usuario> usuariosMap = new HashMap<Integer, Usuario>();
	for (Usuario usuario : usuarios) {
	    usuariosMap.put(usuario.getId_Codigo(), usuario);
	}
	Collection<Usuario> usuariosValues = usuariosMap.values();
	this.hibernateUtil.salvarOuAtualizar(usuariosValues);
	if (quantidadeLinhas != usuariosValues.size()) {
	    Mail.enviarEmail("Quantidade de linhas do csv não bate com quantidade de registros salvos", String.format("csv: tblRelacionamentos, quantidadeLinhas: %s, registros salvos: %s", quantidadeLinhas, usuariosValues.size()));
	}
    }

    private void processarCSVPosicoes() throws Exception {

	CSVReader reader = lerArquivo("tblPosicoes.csv");
	List<Posicao> posicoes = new ArrayList<Posicao>();
	int quantidadeLinhas = preencherObjeto(reader, posicoes, "Posicao");
	this.hibernateUtil.executarSQL("delete from posicao");
	System.out.println("Quantidade de posicoes no banco após delete: " + this.hibernateUtil.contar(new Posicao()));
	Map<Integer, Posicao> posicoesMap = new HashMap<Integer, Posicao>();
	for (Posicao posicao : posicoes) {
	    posicoesMap.put(posicao.getPosicao(), posicao);
	}
	Collection<Posicao> posicoesValues = posicoesMap.values();
	this.hibernateUtil.salvarOuAtualizar(posicoesValues);
	if (quantidadeLinhas != posicoesValues.size()) {
	    Mail.enviarEmail("Quantidade de linhas do csv não bate com quantidade de registros salvos", String.format("csv: tblPosicoes, quantidadeLinhas: %s, registros salvos: %s", quantidadeLinhas, posicoesValues.size()));
	}
    }

    private void processarCSVQualificacao() throws Exception {

	CSVReader reader = lerArquivo("tblQualificacoes.csv");
	List<Qualificacao> qualificacoes = new ArrayList<Qualificacao>();
	int quantidadeLinhas = preencherObjeto(reader, qualificacoes, "Qualificacao");
	this.hibernateUtil.executarSQL("delete from qualificacao");
	System.out.println("Quantidade de qualificacoes no banco após delete: " + this.hibernateUtil.contar(new Qualificacao()));
	Map<String, Qualificacao> qualificacoesMap = new HashMap<String, Qualificacao>();
	for (Qualificacao qualificacao : qualificacoes) {
	    qualificacoesMap.put(qualificacao.getId_Codigo() + "-" + String.valueOf(qualificacao.getData().get(Calendar.YEAR)) + "-" + String.valueOf(qualificacao.getData().get(Calendar.MONTH)) + "-" + String.valueOf(qualificacao.getData().get(Calendar.DAY_OF_MONTH)), qualificacao);
	}
	Collection<Qualificacao> qualificacoesValues = qualificacoesMap.values();
	this.hibernateUtil.salvarOuAtualizar(qualificacoesValues);
	if (quantidadeLinhas != qualificacoesValues.size()) {
	    Mail.enviarEmail("Quantidade de linhas do csv não bate com quantidade de registros salvos", String.format("csv: tblQualificacoes, quantidadeLinhas: %s, registros salvos: %s", quantidadeLinhas, qualificacoesValues.size()));
	}
    }

    private void processarCSVPontuacao() throws Exception {

	CSVReader reader = lerArquivo("tblPontuacao.csv");
	List<Pontuacao> pontuacoes = new ArrayList<Pontuacao>();
	int quantidadeLinhas = preencherObjeto(reader, pontuacoes, "Pontuacao");
	this.hibernateUtil.executarSQL("delete from pontuacao");
	System.out.println("Quantidade de pontuacoes no banco após delete: " + this.hibernateUtil.contar(new Pontuacao()));
	Map<String, Pontuacao> pontuacoesMap = new HashMap<String, Pontuacao>();
	for (Pontuacao pontuacao : pontuacoes) {
	    pontuacoesMap.put(pontuacao.getId_Codigo() + "-" + String.valueOf(pontuacao.getDt_Pontos().get(Calendar.YEAR)) + "-" + String.valueOf(pontuacao.getDt_Pontos().get(Calendar.MONTH)) + "-" + String.valueOf(pontuacao.getDt_Pontos().get(Calendar.DAY_OF_MONTH)), pontuacao);
	}
	Collection<Pontuacao> pontuacoesValues = pontuacoesMap.values();
	this.hibernateUtil.salvarOuAtualizar(pontuacoesValues);
	if (quantidadeLinhas != pontuacoesValues.size()) {
	    Mail.enviarEmail("Quantidade de linhas do csv não bate com quantidade de registros salvos", String.format("csv: tblPontuacao, quantidadeLinhas: %s, registros salvos: %s", quantidadeLinhas, pontuacoesValues.size()));
	}
    }

    private void processarCSVParametroIngresso() throws Exception {

	CSVReader reader = lerArquivo("tblParametrosIngresso.csv");
	List<ParametroIngresso> parametrosIngresso = new ArrayList<ParametroIngresso>();
	int quantidadeLinhas = preencherObjeto(reader, parametrosIngresso, "ParametroIngresso");
	this.hibernateUtil.executarSQL("delete from parametroingresso");

	System.out.println("Quantidade de parametrosIngresso no banco após delete: " + this.hibernateUtil.contar(new ParametroIngresso()));
	Map<String, ParametroIngresso> parametrosIngressoMap = new HashMap<String, ParametroIngresso>();
	for (ParametroIngresso parametroIngresso : parametrosIngresso) {
	    parametrosIngressoMap.put(parametroIngresso.getNivel() + "-" + String.valueOf(parametroIngresso.getData().get(Calendar.YEAR)) + "-" + String.valueOf(parametroIngresso.getData().get(Calendar.MONTH)) + "-" + String.valueOf(parametroIngresso.getData().get(Calendar.DAY_OF_MONTH)), parametroIngresso);
	}
	Collection<ParametroIngresso> parametrosIngressoValues = parametrosIngressoMap.values();
	this.hibernateUtil.salvarOuAtualizar(parametrosIngressoValues);
	if (quantidadeLinhas != parametrosIngressoValues.size()) {
	    Mail.enviarEmail("Quantidade de linhas do csv não bate com quantidade de registros salvos", String.format("csv: tblParametrosIngresso, quantidadeLinhas: %s, registros salvos: %s", quantidadeLinhas, parametrosIngressoValues.size()));
	}
    }

    private void processarCSVParametroAtividade() throws Exception {

	CSVReader reader = lerArquivo("tblAtividade.csv");
	List<ParametroAtividade> parametrosAtividades = new ArrayList<ParametroAtividade>();
	preencherObjeto(reader, parametrosAtividades, "ParametroAtividade");
	this.hibernateUtil.executarSQL("delete from parametroatividade");
	this.hibernateUtil.salvarOuAtualizar(parametrosAtividades);
    }

    private void processarCSVParametroUnilevel() throws Exception {

	CSVReader reader = lerArquivo("tblUnilevel.csv");
	List<ParametroUnilevel> parametrosUnilevel = new ArrayList<ParametroUnilevel>();
	preencherObjeto(reader, parametrosUnilevel, "ParametroUnilevel");
	this.hibernateUtil.executarSQL("delete from parametrounilevel");
	this.hibernateUtil.salvarOuAtualizar(parametrosUnilevel);
    }

    private void processarCSVParametroDivisaoLucro() throws Exception {

	CSVReader reader = lerArquivo("tblDivisaoLucro.csv");
	List<ParametroDivisaoLucro> parametrosDivisaoLucro = new ArrayList<ParametroDivisaoLucro>();
	preencherObjeto(reader, parametrosDivisaoLucro, "ParametroDivisaoLucro");
	this.hibernateUtil.executarSQL("delete from parametrodivisaolucro");
	this.hibernateUtil.salvarOuAtualizar(parametrosDivisaoLucro);
    }

    private void processarCSVReceitaDivisaoLucro() throws Exception {

	CSVReader reader = lerArquivo("tblReceitaDivisaoLucro.csv");
	List<ReceitaDivisaoLucro> receitasDivisaoLucro = new ArrayList<ReceitaDivisaoLucro>();
	preencherObjeto(reader, receitasDivisaoLucro, "ReceitaDivisaoLucro");
	this.hibernateUtil.executarSQL("delete from receitadivisaolucro");
	this.hibernateUtil.salvarOuAtualizar(receitasDivisaoLucro);
    }

    private void processarCSVParametroVip() throws Exception {

	CSVReader reader = lerArquivo("tblVIP.csv");
	List<ParametroVip> parametrosVip = new ArrayList<ParametroVip>();
	preencherObjeto(reader, parametrosVip, "ParametroVip");
	this.hibernateUtil.executarSQL("delete from parametrovip");
	this.hibernateUtil.salvarOuAtualizar(parametrosVip);
    }

    private void processarCSVFranquia() throws Exception {

	CSVReader reader = lerArquivo("tblCDA.csv");
	List<Franquia> franquias = new ArrayList<Franquia>();
	preencherObjeto(reader, franquias, "Franquia");
	this.hibernateUtil.executarSQL("delete from franquia");
	this.hibernateUtil.salvarOuAtualizar(franquias);
    }

    private void processarCSVCategoria() throws Exception {

	CSVReader reader = lerArquivo("tblCategorias.csv");
	List<Categoria> categorias = new ArrayList<Categoria>();
	preencherObjeto(reader, categorias, "Categoria");
	this.hibernateUtil.executarSQL("delete from categoria");
	this.hibernateUtil.salvarOuAtualizar(categorias);
    }

    private void processarCSVProduto() throws Exception {

	CSVReader reader = lerArquivo("tblProdutos.csv");
	List<Produto> produtos = new ArrayList<Produto>();
	preencherObjeto(reader, produtos, "Produto");
	this.hibernateUtil.executarSQL("delete from produto");
	this.hibernateUtil.salvarOuAtualizar(produtos);
    }

    private void processarCSVAdesao() throws Exception {

	CSVReader reader = lerArquivo("tblAdesao.csv");
	List<Adesao> adesoes = new ArrayList<Adesao>();
	preencherObjeto(reader, adesoes, "Adesao");
	this.hibernateUtil.executarSQL("delete from adesao");
	this.hibernateUtil.salvarOuAtualizar(adesoes);
    }

    private CSVReader lerArquivo(String nomeCsv) throws Exception {

	String caminho = ArquivoService.PASTA_ATUALIZACAO_CSV;
	String caminhoCompletoArquivo = caminho + File.separator + nomeCsv;
	File arquivoNoDisco = new File(caminhoCompletoArquivo);
	String content = FileUtils.readFileToString(arquivoNoDisco, "ISO8859_1");
	FileUtils.write(arquivoNoDisco, content, "UTF-8");
	return new CSVReader(new FileReader(new File(caminhoCompletoArquivo)), '\t');
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private int preencherObjeto(CSVReader reader, List listaDeEntidades, String nomeDaClasse) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {

	HashMap<Integer, String> hashColunas = new HashMap<Integer, String>();

	int line = 0;

	String[] nextLine;
	while ((nextLine = reader.readNext()) != null) {

	    line++;

	    String[] colunas = nextLine[0].split(";");

	    for (int i = 0; i < colunas.length; i++) {

		colunas[i] = colunas[i].replaceAll("\"", "");
	    }

	    if (line > 1) {

		Object entidade = Class.forName("br.com.alabastrum.escritoriovirtual.modelo." + nomeDaClasse).newInstance();

		for (int i = 0; i < colunas.length; i++) {

		    Field field = null;

		    try {

			field = entidade.getClass().getDeclaredField(hashColunas.get(i));

			field.setAccessible(true);

			try {

			    BigDecimal numero = Util.converterStringParaBigDecimal(colunas[i]);
			    field.set(entidade, numero);
			}

			catch (Exception e1) {

			    try {

				DecimalFormatSymbols dsf = new DecimalFormatSymbols();
				field.set(entidade, (int) Double.parseDouble(colunas[i].replace(dsf.getDecimalSeparator(), '.')));
			    }

			    catch (Exception e2) {

				try {

				    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yy");
				    DateTime data = formatter.parseDateTime("01/" + colunas[i]);

				    field.set(entidade, data.toGregorianCalendar());
				}

				catch (Exception e3) {

				    try {

					DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yy");
					GregorianCalendar gregorianCalendar = new GregorianCalendar();
					gregorianCalendar.setTime(formatter.parseLocalDateTime(colunas[i]).toDate());
					field.set(entidade, gregorianCalendar);

				    } catch (Exception e4) {

					field.set(entidade, colunas[i]);
				    }
				}
			    }
			}

		    } catch (Exception e) {
		    }
		}

		listaDeEntidades.add(entidade);
	    }

	    else {

		for (int i = 0; i < colunas.length; i++) {

		    hashColunas.put(i, colunas[i].replaceAll(" ", ""));
		}
	    }
	}

	return line;
    }
}
