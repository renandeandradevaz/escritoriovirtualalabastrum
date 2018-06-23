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
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class PontuacaoService {

	private HibernateUtil hibernateUtil;

	public PontuacaoService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public PontuacaoDTO calcularPontuacoes(Integer idCodigo) {

		Usuario usuario = hibernateUtil.selecionar(new Usuario(idCodigo));

		Integer quantidadeDeMesesSomatorioPontuacao = Integer.valueOf(new Configuracao().retornarConfiguracao("quantidadeDeMesesSomatorioPontuacao"));
		Integer valorMinimoPontuacaoPessoalParaQualificacao = Integer.valueOf(new Configuracao().retornarConfiguracao("valorMinimoPontuacaoPessoalParaQualificacao"));
		Integer pontuacaoMaximaPorLinhaEmPorcentagem = Integer.valueOf(new Configuracao().retornarConfiguracao("pontuacaoMaximaPorLinhaEmPorcentagem"));
		Integer pontuacaoMinimaPorLinhaEmPorcentagem = Integer.valueOf(new Configuracao().retornarConfiguracao("pontuacaoMinimaPorLinhaEmPorcentagem"));
		TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquica = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(idCodigo);

		Qualificacao ultimaQualificao = new QualificacaoService(hibernateUtil).obterUltimaQualificacao(idCodigo);
		GregorianCalendar dataUltimaQualificacao = ultimaQualificao.getData();
		Posicao proximaPosicao = new PosicoesService(hibernateUtil).obterProximaPosicao(usuario.getPosAtual());
		Integer pontuacaoProximaPosicao = proximaPosicao.getPontuacao();
		Integer pontuacaoMaximaPorLinha = pontuacaoProximaPosicao * pontuacaoMaximaPorLinhaEmPorcentagem / 100;
		Integer pontuacaoMinimaPorLinha = pontuacaoProximaPosicao * pontuacaoMinimaPorLinhaEmPorcentagem / 100;

		PontuacaoDTO pontuacaoDTO = new PontuacaoDTO();

		List<String> meses = new ArrayList<String>();
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
			Integer codigoLiderDireita = distribuidoresPrimeiroNivel.get(2);

			TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEsquerda = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(codigoLiderEsquerda);
			TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMeio = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(codigoLiderMeio);
			TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaDireita = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(codigoLiderDireita);

			Integer pontuacaoRedeEsquerda = calcularPontuacaoRede(codigoLiderEsquerda, primeiroDiaDoMes, ultimoDiaDoMes, arvoreHierarquicaEsquerda);
			Integer pontuacaoRedeMeio = calcularPontuacaoRede(codigoLiderMeio, primeiroDiaDoMes, ultimoDiaDoMes, arvoreHierarquicaMeio);
			Integer pontuacaoRedeDireita = calcularPontuacaoRede(codigoLiderDireita, primeiroDiaDoMes, ultimoDiaDoMes, arvoreHierarquicaDireita);
			pontuacoesLinhaDaEsquerda.add(pontuacaoRedeEsquerda);
			pontuacoesLinhaDoMeio.add(pontuacaoRedeMeio);
			pontuacoesLinhaDaDireita.add(pontuacaoRedeDireita);

			Integer pontuacaoLinhaDaEsquerdaParaQualificacao = pontuacaoRedeEsquerda;
			if (pontuacaoRedeEsquerda > pontuacaoMaximaPorLinha) {
				pontuacaoLinhaDaEsquerdaParaQualificacao = pontuacaoMaximaPorLinha;
			}

			Integer pontuacaoLinhaDoMeioParaQualificacao = pontuacaoRedeMeio;
			if (pontuacaoRedeMeio > pontuacaoMaximaPorLinha) {
				pontuacaoLinhaDoMeioParaQualificacao = pontuacaoMaximaPorLinha;
			}

			Integer pontuacaoLinhaDaDireitaParaQualificacao = pontuacaoRedeDireita;
			if (pontuacaoRedeDireita > pontuacaoMaximaPorLinha) {
				pontuacaoLinhaDaDireitaParaQualificacao = pontuacaoMaximaPorLinha;
			}

			pontuacoesLinhaDaEsquerdaParaQualificacao.add(pontuacaoLinhaDaEsquerdaParaQualificacao);
			pontuacoesLinhaDoMeioParaQualificacao.add(pontuacaoLinhaDoMeioParaQualificacao);
			pontuacoesLinhaDaDireitaParaQualificacao.add(pontuacaoLinhaDaDireitaParaQualificacao);
		}

		pontuacaoDTO.setMeses(meses);
		pontuacaoDTO.setPontuacaoMaximaPorLinha(pontuacaoMaximaPorLinha);
		pontuacaoDTO.setPontuacaoMinimaPorLinha(pontuacaoMinimaPorLinha);
		pontuacaoDTO.setPontuacaoParaOProximoNivel(pontuacaoProximaPosicao);
		pontuacaoDTO.setPontuacoesLinhaDaEsquerda(pontuacoesLinhaDaEsquerda);
		pontuacaoDTO.setPontuacoesLinhaDoMeio(pontuacoesLinhaDoMeio);
		pontuacaoDTO.setPontuacoesLinhaDaDireita(pontuacoesLinhaDaDireita);
		pontuacaoDTO.setPontuacoesLinhaDaEsquerdaParaQualificacao(pontuacoesLinhaDaEsquerdaParaQualificacao);
		pontuacaoDTO.setPontuacoesLinhaDoMeioParaQualificacao(pontuacoesLinhaDoMeioParaQualificacao);
		pontuacaoDTO.setPontuacoesLinhaDaDireitaParaQualificacao(pontuacoesLinhaDaDireitaParaQualificacao);
		pontuacaoDTO.setPontuacoesPessoais(pontuacoesPessoais);
		pontuacaoDTO.setPontuacoesPessoaisParaQualificacao(pontuacoesPessoaisParaQualificacao);

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