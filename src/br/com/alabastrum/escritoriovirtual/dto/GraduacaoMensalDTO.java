package br.com.alabastrum.escritoriovirtual.dto;

public class GraduacaoMensalDTO {

    private String posicaoAtual;
    private Integer pontuacaoDaPosicaoAtual;
    private String proximaPosicao;
    private Integer pontuacaoDaProximaPosicao;
    private Integer pontosAproveitados;
    private Integer pontosRestantesParaProximaPosicao;
    private Integer porcentagemConclusao;

    public String getPosicaoAtual() {
	return posicaoAtual;
    }

    public void setPosicaoAtual(String posicaoAtual) {
	this.posicaoAtual = posicaoAtual;
    }

    public Integer getPontuacaoDaPosicaoAtual() {
	return pontuacaoDaPosicaoAtual;
    }

    public void setPontuacaoDaPosicaoAtual(Integer pontuacaoDaPosicaoAtual) {
	this.pontuacaoDaPosicaoAtual = pontuacaoDaPosicaoAtual;
    }

    public String getProximaPosicao() {
	return proximaPosicao;
    }

    public void setProximaPosicao(String proximaPosicao) {
	this.proximaPosicao = proximaPosicao;
    }

    public Integer getPontuacaoDaProximaPosicao() {
	return pontuacaoDaProximaPosicao;
    }

    public void setPontuacaoDaProximaPosicao(Integer pontuacaoDaProximaPosicao) {
	this.pontuacaoDaProximaPosicao = pontuacaoDaProximaPosicao;
    }

    public Integer getPontosRestantesParaProximaPosicao() {
	return pontosRestantesParaProximaPosicao;
    }

    public void setPontosRestantesParaProximaPosicao(Integer pontosRestantesParaProximaPosicao) {
	this.pontosRestantesParaProximaPosicao = pontosRestantesParaProximaPosicao;
    }

    public Integer getPorcentagemConclusao() {
	return porcentagemConclusao;
    }

    public void setPorcentagemConclusao(Integer porcentagemConclusao) {
	this.porcentagemConclusao = porcentagemConclusao;
    }

    public Integer getPontosAproveitados() {
	return pontosAproveitados;
    }

    public void setPontosAproveitados(Integer pontosAproveitados) {
	this.pontosAproveitados = pontosAproveitados;
    }
}