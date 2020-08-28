package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.GraduacaoMensalDTO;
import br.com.alabastrum.escritoriovirtual.dto.PontuacaoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Configuracao;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class PontuacaoService {

    private static final Integer NIVEL_MAXIMO_PONTUACAO = 10;

    private HibernateUtil hibernateUtil;

    public PontuacaoService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public PontuacaoDTO calcularPontuacoesRelatorio(Integer idCodigo) {

	Usuario usuario = hibernateUtil.selecionar(new Usuario(idCodigo));

	Integer quantidadeDeMesesSomatorioPontuacao = Integer.valueOf(new Configuracao().retornarConfiguracao("quantidadeDeMesesSomatorioPontuacao"));
	Integer valorMinimoPontuacaoPessoalParaQualificacao = Integer.valueOf(new Configuracao().retornarConfiguracao("valorMinimoPontuacaoPessoalParaQualificacao"));
	Integer pontuacaoMaximaPorLinhaEmPorcentagem = Integer.valueOf(new Configuracao().retornarConfiguracao("pontuacaoMaximaPorLinhaEmPorcentagem"));
	Integer pontuacaoMinimaPorLinhaEmPorcentagem = Integer.valueOf(new Configuracao().retornarConfiguracao("pontuacaoMinimaPorLinhaEmPorcentagem"));
	Object[] distribuidoresPrimeiroNivel = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(idCodigo, 1).values().toArray();

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
	List<Integer> pontuacoesTotais = new ArrayList<Integer>();
	Integer total = 0;

	for (int mesesAtras = quantidadeDeMesesSomatorioPontuacao - 1; mesesAtras >= 0; mesesAtras--) {

	    GregorianCalendar primeiroDiaDoMes = Util.getTempoCorrenteAMeiaNoite();
	    primeiroDiaDoMes.add(Calendar.MONTH, -mesesAtras);
	    primeiroDiaDoMes.set(Calendar.DAY_OF_MONTH, 1);

	    GregorianCalendar ultimoDiaDoMes = Util.getTempoCorrenteAMeiaNoite();
	    ultimoDiaDoMes.add(Calendar.MONTH, -mesesAtras);
	    ultimoDiaDoMes.set(Calendar.DAY_OF_MONTH, ultimoDiaDoMes.getActualMaximum(Calendar.DAY_OF_MONTH));

	    if (primeiroDiaDoMes.before(dataUltimaQualificacao)) {
		primeiroDiaDoMes = dataUltimaQualificacao;
	    }

	    meses.add(Util.getMesString(ultimoDiaDoMes.get(Calendar.MONTH)));

	    pontuacoesPessoais.add(calcularPontuacao(idCodigo, primeiroDiaDoMes, ultimoDiaDoMes));
	    int pontuacaoPessoalParaQualificacao = calcularPontuacao(idCodigo, primeiroDiaDoMes, ultimoDiaDoMes) - valorMinimoPontuacaoPessoalParaQualificacao;
	    if (pontuacaoPessoalParaQualificacao < 0) {
		pontuacaoPessoalParaQualificacao = 0;
	    }
	    pontuacoesPessoaisParaQualificacao.add(pontuacaoPessoalParaQualificacao);

	    if (distribuidoresPrimeiroNivel.length < 3) {
		return null;
	    }

	    ArvoreHierarquicaDTO liderEsquerda = (ArvoreHierarquicaDTO) distribuidoresPrimeiroNivel[0];
	    ArvoreHierarquicaDTO liderMeio = (ArvoreHierarquicaDTO) distribuidoresPrimeiroNivel[1];
	    ArvoreHierarquicaDTO liderDireita = (ArvoreHierarquicaDTO) distribuidoresPrimeiroNivel[2];

	    TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEsquerda = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(liderEsquerda.getUsuario().getId_Codigo(), NIVEL_MAXIMO_PONTUACAO - 1);
	    TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMeio = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(liderMeio.getUsuario().getId_Codigo(), NIVEL_MAXIMO_PONTUACAO - 1);
	    TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaDireita = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(liderDireita.getUsuario().getId_Codigo(), NIVEL_MAXIMO_PONTUACAO - 1);

	    Integer pontuacaoRedeEsquerda = calcularPontuacaoRede(liderEsquerda.getUsuario().getId_Codigo(), primeiroDiaDoMes, ultimoDiaDoMes, arvoreHierarquicaEsquerda);
	    Integer pontuacaoRedeMeio = calcularPontuacaoRede(liderMeio.getUsuario().getId_Codigo(), primeiroDiaDoMes, ultimoDiaDoMes, arvoreHierarquicaMeio);
	    Integer pontuacaoRedeDireita = calcularPontuacaoRede(liderDireita.getUsuario().getId_Codigo(), primeiroDiaDoMes, ultimoDiaDoMes, arvoreHierarquicaDireita);
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

	    int pontuacaoTotal = pontuacaoPessoalParaQualificacao + pontuacaoLinhaDaEsquerdaParaQualificacao + pontuacaoLinhaDoMeioParaQualificacao + pontuacaoLinhaDaDireitaParaQualificacao;
	    pontuacoesTotais.add(pontuacaoTotal);
	    total += pontuacaoTotal;
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
	pontuacaoDTO.setPontuacoesTotais(pontuacoesTotais);
	pontuacaoDTO.setTotal(total);
	pontuacaoDTO.setValorMinimoPontuacaoPessoalParaQualificacao(valorMinimoPontuacaoPessoalParaQualificacao);

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

    public GraduacaoMensalDTO calcularGraduacaoMensalPorPontuacaoDeProduto(Integer idCodigo) {

	Usuario usuario = this.hibernateUtil.selecionar(new Usuario(idCodigo));

	GregorianCalendar hoje = Util.getTempoCorrenteAMeiaNoite();
	GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(hoje);
	GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(hoje);

	BigDecimal somaPontuacao = calcularPontuacaoDeProdutos(primeiroDiaDoMes, ultimoDiaDoMes, idCodigo);

	Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaCompleta = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(usuario.getId_Codigo(), null);

	for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquicaCompleta.values()) {
	    somaPontuacao = somaPontuacao.add(calcularPontuacaoDeProdutos(primeiroDiaDoMes, ultimoDiaDoMes, arvoreHierarquicaDTO.getUsuario().getId_Codigo()));
	}

	Posicao posicaoAtual = new PosicoesService(hibernateUtil).obterPosicaoPorNome(usuario.getPosAtual());
	Posicao proximaPosicao = new PosicoesService(hibernateUtil).obterProximaPosicao(usuario.getPosAtual());

	GraduacaoMensalDTO graduacaoMensalDTO = new GraduacaoMensalDTO();
	graduacaoMensalDTO.setPosicaoAtual(usuario.getPosAtual());
	graduacaoMensalDTO.setPontuacaoDaPosicaoAtual(posicaoAtual.getPontuacao());
	graduacaoMensalDTO.setProximaPosicao(proximaPosicao.getNome());
	graduacaoMensalDTO.setPontuacaoDaProximaPosicao(proximaPosicao.getPontuacao());
	graduacaoMensalDTO.setPontosFeitosAteOMomento(somaPontuacao.intValue());
	graduacaoMensalDTO.setPontosRestantesParaProximaPosicao(graduacaoMensalDTO.getPontuacaoDaProximaPosicao() - graduacaoMensalDTO.getPontosFeitosAteOMomento());

	if (graduacaoMensalDTO.getPontosFeitosAteOMomento().equals(0)) {
	    graduacaoMensalDTO.setPorcentagemConclusao(0);
	} else {
	    graduacaoMensalDTO.setPorcentagemConclusao(graduacaoMensalDTO.getPontuacaoDaProximaPosicao() / graduacaoMensalDTO.getPontosFeitosAteOMomento() * 100);
	}

	return graduacaoMensalDTO;
    }

    private BigDecimal calcularPontuacaoDeProdutos(GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes, Integer idCodigo) {

	BigDecimal somaPontuacao = BigDecimal.ZERO;

	List<Criterion> restricoes = new ArrayList<Criterion>();
	restricoes.add(Restrictions.between("Dt_Pontos", primeiroDiaDoMes, ultimoDiaDoMes));
	Pontuacao pontuacaoFiltro = new Pontuacao();
	pontuacaoFiltro.setId_Codigo(idCodigo);
	List<Pontuacao> pontuacoes = hibernateUtil.buscar(pontuacaoFiltro, restricoes);

	for (Pontuacao pontuacao : pontuacoes) {
	    somaPontuacao = somaPontuacao.add(pontuacao.getPntProduto());
	}

	return somaPontuacao;
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
}