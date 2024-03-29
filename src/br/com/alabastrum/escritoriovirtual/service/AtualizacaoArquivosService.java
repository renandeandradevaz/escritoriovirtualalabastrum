package br.com.alabastrum.escritoriovirtual.service;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.alabastrum.escritoriovirtual.modelo.*;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import au.com.bytecode.opencsv.CSVReader;
import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class AtualizacaoArquivosService {

    private HibernateUtil hibernateUtil;

    public AtualizacaoArquivosService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public void processarArquivosPeriodoLongo() throws Exception {

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
	processarCSVCaixa();
	processarCSVAdesao();
	processarCSVKitAdesao();
	processarCSVBonusAdesao();
	processarCSVFaixaAtividade();
	processarCSVFaixaBonusAtividade();
	processarCSVMovimentacaoDeRede();
    }

    public void processarArquivosPeriodoCurto() throws Exception {

	processarCSVRelacionamentos();
	processarCSVQualificacao(ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_CURTO);
    }

    public void processarArquivoAtualizacaoUsuario() throws Exception {

	String pasta = ArquivoService.PASTA_ATUALIZACAO_CSV_DISTRIBUIDOR;

	List<String> pastaAtualizacaoCSV = Arrays.asList(new File(pasta).list());

	if (pastaAtualizacaoCSV.contains("tblRelacionamentos.csv")) {
	    CSVReader reader = lerArquivo("tblRelacionamentos.csv", pasta);
	    List<Usuario> usuarios = new ArrayList<Usuario>();
	    preencherObjeto(reader, usuarios, "Usuario");

	    for (Usuario usuario : usuarios) {
		Usuario usuarioBanco = this.hibernateUtil.selecionar(new Usuario(usuario.getId_Codigo()));
		if (usuarioBanco != null) {
		    this.hibernateUtil.deletar(usuarioBanco);
		    this.hibernateUtil.salvarOuAtualizar(usuario);
		}
	    }

	}

	if (pastaAtualizacaoCSV.contains("tblQualificacoes.csv")) {
	    processarCSVQualificacao(pasta);
	}
    }

    private void processarCSVRelacionamentos() throws Exception {

	CSVReader reader = lerArquivo("tblRelacionamentos.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_CURTO);
	List<Usuario> usuarios = new ArrayList<Usuario>();
	preencherObjeto(reader, usuarios, "Usuario");

	for (Usuario usuario : usuarios) {
	    Usuario usuarioBanco = this.hibernateUtil.selecionar(new Usuario(usuario.getId_Codigo()));
	    if (usuarioBanco == null) {
		this.hibernateUtil.salvarOuAtualizar(usuario);
	    }
	}
    }

    private void processarCSVQualificacao(String pasta) throws Exception {

	CSVReader reader = lerArquivo("tblQualificacoes.csv", pasta);
	List<Qualificacao> qualificacoes = new ArrayList<Qualificacao>();
	preencherObjeto(reader, qualificacoes, "Qualificacao");

	for (Qualificacao qualificacao : qualificacoes) {

	    Qualificacao qualificacaoFiltro = new Qualificacao();
	    qualificacaoFiltro.setId_Codigo(qualificacao.getId_Codigo());
	    qualificacaoFiltro.setData(qualificacao.getData());

	    Qualificacao qualificacaoBanco = this.hibernateUtil.selecionar(qualificacaoFiltro);
	    if (qualificacaoBanco == null) {
		this.hibernateUtil.salvarOuAtualizar(qualificacao);
	    }
	}
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

    private void processarCSVCaixa() throws Exception {

	CSVReader reader = lerArquivo("tblCaixa.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<Caixa> caixas = new ArrayList<Caixa>();
	preencherObjeto(reader, caixas, "Caixa");
	this.hibernateUtil.executarSQL("delete from caixa");
	this.hibernateUtil.salvarOuAtualizar(caixas);
    }

    private void processarCSVAdesao() throws Exception {

	CSVReader reader = lerArquivo("tblAdesao.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<Adesao> adesoes = new ArrayList<Adesao>();
	preencherObjeto(reader, adesoes, "Adesao");
	this.hibernateUtil.executarSQL("delete from adesao");
	this.hibernateUtil.salvarOuAtualizar(adesoes);
    }

    private void processarCSVKitAdesao() throws Exception {

	CSVReader reader = lerArquivo("tblKitAdesao.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<KitAdesao> kitsAdesoes = new ArrayList<KitAdesao>();
	preencherObjeto(reader, kitsAdesoes, "KitAdesao");
	this.hibernateUtil.executarSQL("delete from kitadesao");
	this.hibernateUtil.salvarOuAtualizar(kitsAdesoes);
    }

    private void processarCSVBonusAdesao() throws Exception {

	CSVReader reader = lerArquivo("tblbonusAdesao.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<BonusAdesao> bonusAdesoes = new ArrayList<BonusAdesao>();
	preencherObjeto(reader, bonusAdesoes, "BonusAdesao");
	this.hibernateUtil.executarSQL("delete from bonusadesao");
	this.hibernateUtil.salvarOuAtualizar(bonusAdesoes);
    }

    private void processarCSVFaixaAtividade() throws Exception {

	CSVReader reader = lerArquivo("tblFaixaAtividade.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<FaixaAtividade> faixasAtividades = new ArrayList<FaixaAtividade>();
	preencherObjeto(reader, faixasAtividades, "FaixaAtividade");
	this.hibernateUtil.executarSQL("delete from faixaatividade");
	this.hibernateUtil.salvarOuAtualizar(faixasAtividades);
    }

    private void processarCSVFaixaBonusAtividade() throws Exception {

	CSVReader reader = lerArquivo("tblFaixaBonusAtividade.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
	List<FaixaBonusAtividade> faixasBonusAtividades = new ArrayList<FaixaBonusAtividade>();
	preencherObjeto(reader, faixasBonusAtividades, "FaixaBonusAtividade");
	this.hibernateUtil.executarSQL("delete from faixabonusatividade");
	this.hibernateUtil.salvarOuAtualizar(faixasBonusAtividades);
    }

	private void processarCSVMovimentacaoDeRede() throws Exception {

		CSVReader reader = lerArquivo("tblMovimentacaoRede.csv", ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);
		List<MovimentacaoRede> movimentacaoRedes = new ArrayList<MovimentacaoRede>();
		preencherObjeto(reader, movimentacaoRedes, "MovimentacaoRede");
		this.hibernateUtil.executarSQL("delete from movimentacaorede");
		this.hibernateUtil.salvarOuAtualizar(movimentacaoRedes);
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