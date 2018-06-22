package br.com.alabastrum.escritoriovirtual.dto;

import java.util.List;

public class PontuacaoDTO {

	private List<String> meses;
	private List<Integer> pontuacoesPessoais;
	private List<Integer> pontuacoesPessoaisParaQualificacao;
	private List<Integer> pontuacoesLinhaDaEsquerda;
	private List<Integer> pontuacoesLinhaDaEsquerdaParaQualificacao;
	private List<Integer> pontuacoesLinhaDoMeio;
	private List<Integer> pontuacoesLinhaDoMeioParaQualificacao;
	private List<Integer> pontuacoesLinhaDaDireita;
	private List<Integer> pontuacoesLinhaDaDireitaParaQualificacao;
	private List<Integer> pontuacoesTotais;
	private Integer total;
	private Integer pontuacaoParaOProximoNivel;
	private Integer pontuacaoMaximaPorLinha;
	private Integer pontuacaoMinimaPorLinha;
	private Integer valorMinimoPontuacaoPessoalParaQualificacao;

	public Integer getValorMinimoPontuacaoPessoalParaQualificacao() {
		return valorMinimoPontuacaoPessoalParaQualificacao;
	}

	public void setValorMinimoPontuacaoPessoalParaQualificacao(Integer valorMinimoPontuacaoPessoalParaQualificacao) {
		this.valorMinimoPontuacaoPessoalParaQualificacao = valorMinimoPontuacaoPessoalParaQualificacao;
	}

	public List<String> getMeses() {
		return meses;
	}

	public void setMeses(List<String> meses) {
		this.meses = meses;
	}

	public List<Integer> getPontuacoesPessoais() {
		return pontuacoesPessoais;
	}

	public void setPontuacoesPessoais(List<Integer> pontuacoesPessoais) {
		this.pontuacoesPessoais = pontuacoesPessoais;
	}

	public List<Integer> getPontuacoesPessoaisParaQualificacao() {
		return pontuacoesPessoaisParaQualificacao;
	}

	public void setPontuacoesPessoaisParaQualificacao(List<Integer> pontuacoesPessoaisParaQualificacao) {
		this.pontuacoesPessoaisParaQualificacao = pontuacoesPessoaisParaQualificacao;
	}

	public List<Integer> getPontuacoesLinhaDaEsquerda() {
		return pontuacoesLinhaDaEsquerda;
	}

	public void setPontuacoesLinhaDaEsquerda(List<Integer> pontuacoesLinhaDaEsquerda) {
		this.pontuacoesLinhaDaEsquerda = pontuacoesLinhaDaEsquerda;
	}

	public List<Integer> getPontuacoesLinhaDaEsquerdaParaQualificacao() {
		return pontuacoesLinhaDaEsquerdaParaQualificacao;
	}

	public void setPontuacoesLinhaDaEsquerdaParaQualificacao(List<Integer> pontuacoesLinhaDaEsquerdaParaQualificacao) {
		this.pontuacoesLinhaDaEsquerdaParaQualificacao = pontuacoesLinhaDaEsquerdaParaQualificacao;
	}

	public List<Integer> getPontuacoesLinhaDoMeio() {
		return pontuacoesLinhaDoMeio;
	}

	public void setPontuacoesLinhaDoMeio(List<Integer> pontuacoesLinhaDoMeio) {
		this.pontuacoesLinhaDoMeio = pontuacoesLinhaDoMeio;
	}

	public List<Integer> getPontuacoesLinhaDoMeioParaQualificacao() {
		return pontuacoesLinhaDoMeioParaQualificacao;
	}

	public void setPontuacoesLinhaDoMeioParaQualificacao(List<Integer> pontuacoesLinhaDoMeioParaQualificacao) {
		this.pontuacoesLinhaDoMeioParaQualificacao = pontuacoesLinhaDoMeioParaQualificacao;
	}

	public List<Integer> getPontuacoesLinhaDaDireita() {
		return pontuacoesLinhaDaDireita;
	}

	public void setPontuacoesLinhaDaDireita(List<Integer> pontuacoesLinhaDaDireita) {
		this.pontuacoesLinhaDaDireita = pontuacoesLinhaDaDireita;
	}

	public List<Integer> getPontuacoesLinhaDaDireitaParaQualificacao() {
		return pontuacoesLinhaDaDireitaParaQualificacao;
	}

	public void setPontuacoesLinhaDaDireitaParaQualificacao(List<Integer> pontuacoesLinhaDaDireitaParaQualificacao) {
		this.pontuacoesLinhaDaDireitaParaQualificacao = pontuacoesLinhaDaDireitaParaQualificacao;
	}

	public List<Integer> getPontuacoesTotais() {
		return pontuacoesTotais;
	}

	public void setPontuacoesTotais(List<Integer> pontuacoesTotais) {
		this.pontuacoesTotais = pontuacoesTotais;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getPontuacaoParaOProximoNivel() {
		return pontuacaoParaOProximoNivel;
	}

	public void setPontuacaoParaOProximoNivel(Integer pontuacaoParaOProximoNivel) {
		this.pontuacaoParaOProximoNivel = pontuacaoParaOProximoNivel;
	}

	public Integer getPontuacaoMaximaPorLinha() {
		return pontuacaoMaximaPorLinha;
	}

	public void setPontuacaoMaximaPorLinha(Integer pontuacaoMaximaPorLinha) {
		this.pontuacaoMaximaPorLinha = pontuacaoMaximaPorLinha;
	}

	public Integer getPontuacaoMinimaPorLinha() {
		return pontuacaoMinimaPorLinha;
	}

	public void setPontuacaoMinimaPorLinha(Integer pontuacaoMinimaPorLinha) {
		this.pontuacaoMinimaPorLinha = pontuacaoMinimaPorLinha;
	}
}