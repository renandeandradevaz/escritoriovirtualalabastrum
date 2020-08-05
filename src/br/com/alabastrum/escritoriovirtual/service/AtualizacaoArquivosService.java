package br.com.alabastrum.escritoriovirtual.service;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import au.com.bytecode.opencsv.CSVReader;
import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Adesao;
import br.com.alabastrum.escritoriovirtual.modelo.Categoria;
import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroAtividade;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroDivisaoLucro;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroUnilevel;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroVip;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.modelo.Produto;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.modelo.ReceitaDivisaoLucro;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class AtualizacaoArquivosService {

    private HibernateUtil hibernateUtil;

    public AtualizacaoArquivosService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public void processarArquivosPeriodoLongo() throws Exception {

	System.out.println(new Date() + ". Executando metodo processarArquivosPeriodoLongo");

	processarCSVPosicoes();
	processarCSVPontuacao();
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

    public void processarArquivosPeriodoCurto() throws Exception {

	System.out.println(new Date() + ". Executando metodo processarArquivosPeriodoCurto");

	processarCSVRelacionamentos();
	processarCSVQualificacao();
    }

    public void processarArquivoAtualizacaoUsuario() throws Exception {

	System.out.println(new Date() + ". Executando metodo processarArquivoAtualizacaoUsuario");

	CSVReader reader = lerArquivo("tblRelacionamentos.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_DISTRIBUIDOR);
	List<Usuario> usuarios = new ArrayList<Usuario>();
	preencherObjeto(reader, usuarios, "Usuario");

	for (Usuario usuario : usuarios) {
	    Usuario usuarioBanco = this.hibernateUtil.selecionar(new Usuario(usuario.getId_Codigo()));
	    if (usuarioBanco != null) {
		this.hibernateUtil.deletar(usuarioBanco);
		this.hibernateUtil.salvarOuAtualizar(usuario);
	    }
	}

	System.out.println("Quantidade de usuários atualizados salvos: " + usuarios.size());

    }

    private void processarCSVRelacionamentos() throws Exception {

	CSVReader reader = lerArquivo("tblRelacionamentos.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_CURTO);
	List<Usuario> usuarios = new ArrayList<Usuario>();
	preencherObjeto(reader, usuarios, "Usuario");
	this.hibernateUtil.salvarOuAtualizar(usuarios);
	System.out.println("Quantidade de usuários novos salvos: " + usuarios.size());
    }

    private void processarCSVQualificacao() throws Exception {

	CSVReader reader = lerArquivo("tblQualificacoes.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_CURTO);
	List<Qualificacao> qualificacoes = new ArrayList<Qualificacao>();
	preencherObjeto(reader, qualificacoes, "Qualificacao");
	this.hibernateUtil.salvarOuAtualizar(qualificacoes);
	System.out.println("Quantidade de qualificacoes salvas: " + qualificacoes.size());
    }

    private void processarCSVPosicoes() throws Exception {

	CSVReader reader = lerArquivo("tblPosicoes.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<Posicao> posicoes = new ArrayList<Posicao>();
	preencherObjeto(reader, posicoes, "Posicao");
	this.hibernateUtil.executarSQL("delete from posicao");
	this.hibernateUtil.salvarOuAtualizar(posicoes);
    }

    private void processarCSVPontuacao() throws Exception {

	CSVReader reader = lerArquivo("tblPontuacao.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<Pontuacao> pontuacoes = new ArrayList<Pontuacao>();
	preencherObjeto(reader, pontuacoes, "Pontuacao");
	this.hibernateUtil.executarSQL("delete from pontuacao");
	this.hibernateUtil.salvarOuAtualizar(pontuacoes);
    }

    private void processarCSVParametroAtividade() throws Exception {

	CSVReader reader = lerArquivo("tblAtividade.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<ParametroAtividade> parametrosAtividades = new ArrayList<ParametroAtividade>();
	preencherObjeto(reader, parametrosAtividades, "ParametroAtividade");
	this.hibernateUtil.executarSQL("delete from parametroatividade");
	this.hibernateUtil.salvarOuAtualizar(parametrosAtividades);
    }

    private void processarCSVParametroUnilevel() throws Exception {

	CSVReader reader = lerArquivo("tblUnilevel.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<ParametroUnilevel> parametrosUnilevel = new ArrayList<ParametroUnilevel>();
	preencherObjeto(reader, parametrosUnilevel, "ParametroUnilevel");
	this.hibernateUtil.executarSQL("delete from parametrounilevel");
	this.hibernateUtil.salvarOuAtualizar(parametrosUnilevel);
    }

    private void processarCSVParametroDivisaoLucro() throws Exception {

	CSVReader reader = lerArquivo("tblDivisaoLucro.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<ParametroDivisaoLucro> parametrosDivisaoLucro = new ArrayList<ParametroDivisaoLucro>();
	preencherObjeto(reader, parametrosDivisaoLucro, "ParametroDivisaoLucro");
	this.hibernateUtil.executarSQL("delete from parametrodivisaolucro");
	this.hibernateUtil.salvarOuAtualizar(parametrosDivisaoLucro);
    }

    private void processarCSVReceitaDivisaoLucro() throws Exception {

	CSVReader reader = lerArquivo("tblReceitaDivisaoLucro.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<ReceitaDivisaoLucro> receitasDivisaoLucro = new ArrayList<ReceitaDivisaoLucro>();
	preencherObjeto(reader, receitasDivisaoLucro, "ReceitaDivisaoLucro");
	this.hibernateUtil.executarSQL("delete from receitadivisaolucro");
	this.hibernateUtil.salvarOuAtualizar(receitasDivisaoLucro);
    }

    private void processarCSVParametroVip() throws Exception {

	CSVReader reader = lerArquivo("tblVIP.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<ParametroVip> parametrosVip = new ArrayList<ParametroVip>();
	preencherObjeto(reader, parametrosVip, "ParametroVip");
	this.hibernateUtil.executarSQL("delete from parametrovip");
	this.hibernateUtil.salvarOuAtualizar(parametrosVip);
    }

    private void processarCSVFranquia() throws Exception {

	CSVReader reader = lerArquivo("tblCDA.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<Franquia> franquias = new ArrayList<Franquia>();
	preencherObjeto(reader, franquias, "Franquia");
	this.hibernateUtil.executarSQL("delete from franquia");
	this.hibernateUtil.salvarOuAtualizar(franquias);
    }

    private void processarCSVCategoria() throws Exception {

	CSVReader reader = lerArquivo("tblCategorias.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<Categoria> categorias = new ArrayList<Categoria>();
	preencherObjeto(reader, categorias, "Categoria");
	this.hibernateUtil.executarSQL("delete from categoria");
	this.hibernateUtil.salvarOuAtualizar(categorias);
    }

    private void processarCSVProduto() throws Exception {

	CSVReader reader = lerArquivo("tblProdutos.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<Produto> produtos = new ArrayList<Produto>();
	preencherObjeto(reader, produtos, "Produto");
	this.hibernateUtil.executarSQL("delete from produto");
	this.hibernateUtil.salvarOuAtualizar(produtos);
    }

    private void processarCSVAdesao() throws Exception {

	CSVReader reader = lerArquivo("tblAdesao.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<Adesao> adesoes = new ArrayList<Adesao>();
	preencherObjeto(reader, adesoes, "Adesao");
	this.hibernateUtil.executarSQL("delete from adesao");
	this.hibernateUtil.salvarOuAtualizar(adesoes);
    }

    private CSVReader lerArquivo(String nomeCsv, String caminho) throws Exception {

	String caminhoCompletoArquivo = caminho + File.separator + nomeCsv;
	File arquivoNoDisco = new File(caminhoCompletoArquivo);
	String content = FileUtils.readFileToString(arquivoNoDisco, "ISO8859_1");
	FileUtils.write(arquivoNoDisco, content, "UTF-8");
	return new CSVReader(new FileReader(new File(caminhoCompletoArquivo)), '\t');
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void preencherObjeto(CSVReader reader, List listaDeEntidades, String nomeDaClasse) throws Exception {

	Map<String, Entidade> entidadesMap = new HashMap<String, Entidade>();

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

			String coluna = colunas[i];
			try {

			    BigDecimal numero = Util.converterStringParaBigDecimal(coluna);
			    field.set(entidade, numero);
			}

			catch (Exception e1) {

			    try {

				DecimalFormatSymbols dsf = new DecimalFormatSymbols();
				field.set(entidade, (int) Double.parseDouble(coluna.replace(dsf.getDecimalSeparator(), '.')));
			    }

			    catch (Exception e2) {

				try {

				    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yy");
				    DateTime data = formatter.parseDateTime("01/" + coluna);

				    field.set(entidade, data.toGregorianCalendar());
				}

				catch (Exception e3) {

				    try {

					DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yy");
					GregorianCalendar gregorianCalendar = new GregorianCalendar();
					gregorianCalendar.setTime(formatter.parseLocalDateTime(coluna).toDate());
					field.set(entidade, gregorianCalendar);

				    } catch (Exception e4) {

					field.set(entidade, coluna);
				    }
				}
			    }
			}

		    } catch (Exception e) {
		    }
		}

		entidadesMap.put(colunas[0], (Entidade) entidade);
	    }

	    else {

		for (int i = 0; i < colunas.length; i++) {

		    if (!colunas[0].equals("linha_csv")) {
			throw new Exception("Primeira coluna do arquivo referente a " + nomeDaClasse + " nao é linha_csv");
		    }

		    hashColunas.put(i, colunas[i].replaceAll(" ", ""));
		}
	    }
	}

	listaDeEntidades.addAll(entidadesMap.values());
    }
}