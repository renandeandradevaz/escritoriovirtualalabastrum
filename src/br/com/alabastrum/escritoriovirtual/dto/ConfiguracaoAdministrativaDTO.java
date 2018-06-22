package br.com.alabastrum.escritoriovirtual.dto;

public class ConfiguracaoAdministrativaDTO {

	private String senhaEmail;
	private String valorMinimoPontuacaoPessoalParaQualificacao;
	private String pontuacaoMaximaPorLinhaEmPorcentagem;
	private String pontuacaoMinimaPorLinhaEmPorcentagem;
	private String quantidadeDeMesesSomatorioPontuacao;

	public String getValorMinimoPontuacaoPessoalParaQualificacao() {
		return valorMinimoPontuacaoPessoalParaQualificacao;
	}

	public void setValorMinimoPontuacaoPessoalParaQualificacao(String valorMinimoPontuacaoPessoalParaQualificacao) {
		this.valorMinimoPontuacaoPessoalParaQualificacao = valorMinimoPontuacaoPessoalParaQualificacao;
	}

	public String getPontuacaoMaximaPorLinhaEmPorcentagem() {
		return pontuacaoMaximaPorLinhaEmPorcentagem;
	}

	public void setPontuacaoMaximaPorLinhaEmPorcentagem(String pontuacaoMaximaPorLinhaEmPorcentagem) {
		this.pontuacaoMaximaPorLinhaEmPorcentagem = pontuacaoMaximaPorLinhaEmPorcentagem;
	}

	public String getPontuacaoMinimaPorLinhaEmPorcentagem() {
		return pontuacaoMinimaPorLinhaEmPorcentagem;
	}

	public void setPontuacaoMinimaPorLinhaEmPorcentagem(String pontuacaoMinimaPorLinhaEmPorcentagem) {
		this.pontuacaoMinimaPorLinhaEmPorcentagem = pontuacaoMinimaPorLinhaEmPorcentagem;
	}

	public String getSenhaEmail() {
		return senhaEmail;
	}

	public void setSenhaEmail(String senhaEmail) {
		this.senhaEmail = senhaEmail;
	}

	public String getQuantidadeDeMesesSomatorioPontuacao() {
		return quantidadeDeMesesSomatorioPontuacao;
	}

	public void setQuantidadeDeMesesSomatorioPontuacao(String quantidadeDeMesesSomatorioPontuacao) {
		this.quantidadeDeMesesSomatorioPontuacao = quantidadeDeMesesSomatorioPontuacao;
	}
}