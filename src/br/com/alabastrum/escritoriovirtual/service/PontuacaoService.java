package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.GraduacaoMensalDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class PontuacaoService {

    private HibernateUtil hibernateUtil;

    public PontuacaoService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public GraduacaoMensalDTO calcularGraduacaoMensal(Integer idCodigo) {

	Usuario usuario = this.hibernateUtil.selecionar(new Usuario(idCodigo));

	GregorianCalendar hoje = Util.getTempoCorrenteAMeiaNoite();
	GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(hoje);
	GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(hoje);

	Integer somaPontuacaoAproveitadaTotal = calcularPontuacaoParaQualificacao(primeiroDiaDoMes, ultimoDiaDoMes, idCodigo);
	Integer somaPontuacaoTotal = new Integer(somaPontuacaoAproveitadaTotal);

	List<Integer> pontuacoesPorLinha = new ArrayList<Integer>();

	if (somaPontuacaoAproveitadaTotal > 0) {
	    pontuacoesPorLinha.add(somaPontuacaoAproveitadaTotal);
	}

	Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaNivel1 = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(usuario.getId_Codigo(), 1);
	for (ArvoreHierarquicaDTO distribuidorNivel1 : arvoreHierarquicaNivel1.values()) {

	    Integer somaPontuacaoPorLinha = calcularPontuacaoParaQualificacao(primeiroDiaDoMes, ultimoDiaDoMes, distribuidorNivel1.getUsuario().getId_Codigo());

	    Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaTodosOsNiveis = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(distribuidorNivel1.getUsuario().getId_Codigo());

	    for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquicaTodosOsNiveis.values()) {
		somaPontuacaoPorLinha += calcularPontuacaoParaQualificacao(primeiroDiaDoMes, ultimoDiaDoMes, arvoreHierarquicaDTO.getUsuario().getId_Codigo());
	    }

	    if (somaPontuacaoPorLinha > 0) {
		pontuacoesPorLinha.add(somaPontuacaoPorLinha);
	    }

	    somaPontuacaoTotal += somaPontuacaoPorLinha;
	}

	Posicao posicaoAtual = new PosicoesService(hibernateUtil).obterPosicaoPorOrdemNumerica(1, hoje);

	if (new AtividadeService(hibernateUtil).possuiIndicadosDiretosAtivos(idCodigo, new GregorianCalendar(), 3)) {

	    List<Posicao> posicoes = this.hibernateUtil.buscar(new Posicao(), Order.desc("posicao"));

	    for (Posicao posicao : posicoes) {

		Integer pontuacaoAproveitadaDaPosicao = posicao.getPontuacao() / 2;
		Integer pontuacaoTotalNestaPosicao = 0;
		for (Integer pontuacaoPorLinha : pontuacoesPorLinha) {
		    Integer pontuacaoAproveitadaPorLinha = pontuacaoPorLinha;
		    if (pontuacaoPorLinha > pontuacaoAproveitadaDaPosicao) {
			pontuacaoAproveitadaPorLinha = pontuacaoAproveitadaDaPosicao;
		    }
		    pontuacaoTotalNestaPosicao += pontuacaoAproveitadaPorLinha;
		}

		if (pontuacaoTotalNestaPosicao >= posicao.getPontuacao()) {
		    posicaoAtual = posicao;
		    break;
		}
		somaPontuacaoAproveitadaTotal = pontuacaoTotalNestaPosicao;
	    }
	}

	Posicao proximaPosicao = new PosicoesService(hibernateUtil).obterProximaPosicao(posicaoAtual.getNome(), hoje);

	GraduacaoMensalDTO graduacaoMensalDTO = new GraduacaoMensalDTO();
	graduacaoMensalDTO.setPosicaoAtual(posicaoAtual.getNome());
	graduacaoMensalDTO.setPontuacaoDaPosicaoAtual(posicaoAtual.getPontuacao());
	graduacaoMensalDTO.setProximaPosicao(proximaPosicao.getNome());
	graduacaoMensalDTO.setPontuacaoDaProximaPosicao(proximaPosicao.getPontuacao());

	if (graduacaoMensalDTO.getPontuacaoDaProximaPosicao() == 0)
	    graduacaoMensalDTO.setPontosAproveitados(0);
	else
	    graduacaoMensalDTO.setPontosAproveitados(somaPontuacaoAproveitadaTotal);

	graduacaoMensalDTO.setPontuacaoTotal(somaPontuacaoTotal);
	graduacaoMensalDTO.setPontosRestantesParaProximaPosicao(graduacaoMensalDTO.getPontuacaoDaProximaPosicao() - graduacaoMensalDTO.getPontosAproveitados());

	if (graduacaoMensalDTO.getPontosRestantesParaProximaPosicao() < 0) {
	    graduacaoMensalDTO.setPontosRestantesParaProximaPosicao(0);
	}

	if (graduacaoMensalDTO.getPontuacaoDaProximaPosicao().equals(0)) {
	    graduacaoMensalDTO.setPorcentagemConclusao(0);
	} else {
	    graduacaoMensalDTO.setPorcentagemConclusao(new BigDecimal(graduacaoMensalDTO.getPontosAproveitados()).divide(new BigDecimal(graduacaoMensalDTO.getPontuacaoDaProximaPosicao()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).intValue());
	}

	return graduacaoMensalDTO;
    }

    public int calcularPontuacaoParaQualificacao(GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes, Integer idCodigo) {

	BigDecimal somaPontuacao = BigDecimal.ZERO;

	List<Criterion> restricoes = new ArrayList<Criterion>();
	restricoes.add(Restrictions.between("Dt_Pontos", primeiroDiaDoMes, ultimoDiaDoMes));
	Pontuacao pontuacaoFiltro = new Pontuacao();
	pontuacaoFiltro.setId_Codigo(idCodigo);
	List<Pontuacao> pontuacoes = hibernateUtil.buscar(pontuacaoFiltro, restricoes);

	for (Pontuacao pontuacao : pontuacoes) {
	    somaPontuacao = somaPontuacao.add(pontuacao.getPntQualificacao());
	}

	return somaPontuacao.intValue();
    }

    public int calcularPontuacaoDeProduto(GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes, Integer idCodigo) {

	BigDecimal somaPontuacao = BigDecimal.ZERO;

	List<Criterion> restricoes = new ArrayList<Criterion>();
	restricoes.add(Restrictions.between("Dt_Pontos", primeiroDiaDoMes, ultimoDiaDoMes));
	Pontuacao pontuacaoFiltro = new Pontuacao();
	pontuacaoFiltro.setId_Codigo(idCodigo);
	List<Pontuacao> pontuacoes = hibernateUtil.buscar(pontuacaoFiltro, restricoes);

	for (Pontuacao pontuacao : pontuacoes) {
	    somaPontuacao = somaPontuacao.add(pontuacao.getPntProduto());
	}

	return somaPontuacao.intValue();
    }

    public BigDecimal getValorIngresso(Integer codigo, GregorianCalendar data) {

	GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(data);
	GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(data);

	List<Pontuacao> pontuacoes = buscarPontuacoes(codigo, primeiroDiaDoMes, ultimoDiaDoMes);

	BigDecimal valorIngresso = BigDecimal.ZERO;

	for (Pontuacao pontuacao : pontuacoes) {
	    valorIngresso = valorIngresso.add(pontuacao.getValorIngresso());
	}

	return valorIngresso;
    }

    public List<Pontuacao> buscarPontuacoes(Integer codigo, GregorianCalendar dataInicial, GregorianCalendar dataFinal) {

	List<Criterion> restricoes = new ArrayList<Criterion>();
	restricoes.add(Restrictions.between("Dt_Pontos", dataInicial, dataFinal));
	Pontuacao filtro = new Pontuacao();
	filtro.setId_Codigo(codigo);
	return hibernateUtil.buscar(filtro, restricoes);
    }

    public List<Pontuacao> buscarPontuacoes(Integer codigo) {
	Pontuacao filtro = new Pontuacao();
	filtro.setId_Codigo(codigo);
	return hibernateUtil.buscar(filtro);
    }
}
