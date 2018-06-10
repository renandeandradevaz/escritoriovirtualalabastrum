package br.com.alabastrum.escritoriovirtual.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import au.com.bytecode.opencsv.CSVReader;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
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
	}

	private void processarCSVRelacionamentos() throws Exception {

		CSVReader reader = lerArquivo("tblRelacionamentos.csv");
		List<Usuario> usuarios = new ArrayList<Usuario>();
		String nomeDaPrimeiraColuna = "id_Codigo";
		preencherObjeto(reader, usuarios, nomeDaPrimeiraColuna, "Usuario");
		this.hibernateUtil.executarSQL("delete from usuario");
		this.hibernateUtil.salvarOuAtualizar(usuarios);
	}

	private void processarCSVPosicoes() throws Exception {

		CSVReader reader = lerArquivo("tblPosicoes.csv");
		List<Posicao> posicoes = new ArrayList<Posicao>();
		String nomeDaPrimeiraColuna = "posicao";
		preencherObjeto(reader, posicoes, nomeDaPrimeiraColuna, "Posicao");
		this.hibernateUtil.executarSQL("delete from posicao");
		this.hibernateUtil.salvarOuAtualizar(posicoes);
	}

	private void processarCSVQualificacao() throws Exception {

		CSVReader reader = lerArquivo("tblQualificacoes.csv");
		List<Qualificacao> qualificacoes = new ArrayList<Qualificacao>();
		String nomeDaPrimeiraColuna = "id_Codigo";
		preencherObjeto(reader, qualificacoes, nomeDaPrimeiraColuna, "Qualificacao");
		this.hibernateUtil.executarSQL("delete from qualificacao");
		this.hibernateUtil.salvarOuAtualizar(qualificacoes);
	}

	private void processarCSVPontuacao() throws Exception {

		CSVReader reader = lerArquivo("tblPontuacao.csv");
		List<Pontuacao> pontuacoes = new ArrayList<Pontuacao>();
		String nomeDaPrimeiraColuna = "id_Codigo";
		preencherObjeto(reader, pontuacoes, nomeDaPrimeiraColuna, "Pontuacao");
		this.hibernateUtil.executarSQL("delete from pontuacao");
		this.hibernateUtil.salvarOuAtualizar(pontuacoes);
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
	private void preencherObjeto(CSVReader reader, List listaDeEntidades, String nomeDaPrimeiraColuna, String nomeDaClasse) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		HashMap<Integer, String> hashColunas = new HashMap<Integer, String>();

		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {

			String[] colunas = nextLine[0].split(";");

			for (int i = 0; i < colunas.length; i++) {

				colunas[i] = colunas[i].replaceAll("\"", "");
			}

			if (!colunas[0].contains(nomeDaPrimeiraColuna)) {

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
										DateTime data = formatter.parseDateTime(colunas[i]);

										field.set(entidade, data.toGregorianCalendar());

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

					hashColunas.put(i, colunas[i]);
				}
			}
		}
	}
}
