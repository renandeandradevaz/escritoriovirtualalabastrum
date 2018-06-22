package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.PontuacaoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Configuracao;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class PontuacaoService {

	private HibernateUtil hibernateUtil;

	public PontuacaoService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public PontuacaoDTO calcularPontuacoes(Integer idCodigo) {

		Integer quantidadeDeMesesSomatorioPontuacao = Integer.valueOf(new Configuracao().retornarConfiguracao("quantidadeDeMesesSomatorioPontuacao"));
		Integer valorMinimoPontuacaoPessoalParaQualificacao = Integer.valueOf(new Configuracao().retornarConfiguracao("valorMinimoPontuacaoPessoalParaQualificacao"));
		TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquica = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(idCodigo);

		PontuacaoDTO pontuacaoDTO = new PontuacaoDTO();

		List<Integer> pontuacoesPessoais = new ArrayList<Integer>();
		List<Integer> pontuacoesPessoaisParaQualificacao = new ArrayList<Integer>();
		List<Integer> pontuacoesLinhaDaEsquerda = new ArrayList<Integer>();
		List<Integer> pontuacoesLinhaDaEsquerdaParaQualificacao = new ArrayList<Integer>();
		List<Integer> pontuacoesLinhaDoMeio = new ArrayList<Integer>();
		List<Integer> pontuacoesLinhaDoMeioParaQualificacao = new ArrayList<Integer>();
		List<Integer> pontuacoesLinhaDaDireita = new ArrayList<Integer>();
		List<Integer> pontuacoesLinhaDaDireitaParaQualificacao = new ArrayList<Integer>();

		for (int mesesAtras = quantidadeDeMesesSomatorioPontuacao - 1; mesesAtras >= 0; mesesAtras--) {

			GregorianCalendar primeiroDiaDoMes = Util.getTempoCorrenteAMeiaNoite();
			primeiroDiaDoMes.add(Calendar.MONTH, -mesesAtras);
			primeiroDiaDoMes.set(Calendar.DAY_OF_MONTH, 1);

			GregorianCalendar ultimoDiaDoMes = Util.getTempoCorrenteAMeiaNoite();
			primeiroDiaDoMes.add(Calendar.MONTH, -mesesAtras);
			ultimoDiaDoMes.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));

			Qualificacao qualificacaoFiltro = new Qualificacao();
			qualificacaoFiltro.setId_Codigo(idCodigo);
			List<Qualificacao> qualificacoesDoDistribuidor = hibernateUtil.buscar(qualificacaoFiltro);
			Qualificacao ultimaQualificao = qualificacoesDoDistribuidor.get(qualificacoesDoDistribuidor.size() - 1);
			GregorianCalendar dataUltimaQualificacao = ultimaQualificao.getData();

			if (primeiroDiaDoMes.before(dataUltimaQualificacao)) {
				primeiroDiaDoMes = dataUltimaQualificacao;
			}

			pontuacoesPessoais.add(calcularPontuacao(idCodigo, primeiroDiaDoMes, ultimoDiaDoMes));
			pontuacoesPessoais.add(calcularPontuacao(idCodigo, primeiroDiaDoMes, ultimoDiaDoMes) - valorMinimoPontuacaoPessoalParaQualificacao);

			pontuacaoDTO.setPontuacoesPessoais(pontuacoesPessoais);
			pontuacaoDTO.setPontuacoesPessoaisParaQualificacao(pontuacoesPessoaisParaQualificacao);

			List<Integer> distribuidoresPrimeiroNivel = new ArrayList<Integer>();
			for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEntry : arvoreHierarquica.entrySet()) {
				if (arvoreHierarquicaEntry.getValue().getNivel() == 1) {
					distribuidoresPrimeiroNivel.add(arvoreHierarquicaEntry.getValue().getUsuario().getId_Codigo());
				}
			}

			if (distribuidoresPrimeiroNivel.size() < 3) {
				return null;
			}

			Integer codigoLiderEsquerda = distribuidoresPrimeiroNivel.get(0);
			Integer codigoLiderMeio = distribuidoresPrimeiroNivel.get(1);
			Integer codigoLiderDireita = distribuidoresPrimeiroNivel.get(1);

			TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEsquerda = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(codigoLiderEsquerda);
			TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMeio = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(codigoLiderMeio);
			TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicadireita = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(codigoLiderDireita);

			pontuacoesLinhaDaEsquerda.add(calcularPontuacaoRede(codigoLiderEsquerda, primeiroDiaDoMes, ultimoDiaDoMes, arvoreHierarquicaEsquerda));
		}

		return pontuacaoDTO;
	}

	private Integer calcularPontuacaoRede(Integer idCodigo, GregorianCalendar inicio, GregorianCalendar fim, TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquica) {

		Integer pontuacaoRede = calcularPontuacao(idCodigo, inicio, fim);

		for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEntry : arvoreHierarquica.entrySet()) {
			pontuacaoRede += calcularPontuacao(arvoreHierarquicaEntry.getValue().getUsuario().getId_Codigo(), inicio, fim);
		}

		return pontuacaoRede;
	}

	private Integer calcularPontuacao(Integer idCodigo, GregorianCalendar inicio, GregorianCalendar fim) {

		List<Criterion> restricoes = new ArrayList<Criterion>();
		restricoes.add(Restrictions.between("Dt_Pontos", inicio, fim));
		Pontuacao pontuacaoFiltro = new Pontuacao();
		pontuacaoFiltro.setId_Codigo(idCodigo);
		List<Pontuacao> pontuacoes = hibernateUtil.buscar(pontuacaoFiltro, restricoes);
		BigDecimal somaPontuacao = BigDecimal.ZERO;

		for (Pontuacao pontuacao : pontuacoes) {
			somaPontuacao = somaPontuacao.add(pontuacao.getPntAtividade().add(pontuacao.getPntIngresso().add(pontuacao.getPntProduto())));
		}

		return somaPontuacao.intValue();
	}
}