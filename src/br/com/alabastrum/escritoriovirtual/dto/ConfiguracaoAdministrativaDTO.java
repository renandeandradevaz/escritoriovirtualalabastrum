package br.com.alabastrum.escritoriovirtual.dto;

public class ConfiguracaoAdministrativaDTO {

	private String senhaEmail;
	private String valorMinimoPontuacaoPessoalParaQualificacao;
	private String pontuacaoMaximaPorLinhaEmPorcentagem;
	private String pontuacaoMinimaPorLinhaEmPorcentagem;
	private String quantidadeDeMesesSomatorioPontuacao;
	private String valorMinimoPedidosPrimeiraPosicao;
	private String emailPagseguro;
	private String tokenPagseguro;
	private String tokenEV;

	public String getSenhaEmail() {
		return senhaEmail;
	}

	public void setSenhaEmail(String senhaEmail) {
		this.senhaEmail = senhaEmail;
	}

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

	public String getQuantidadeDeMesesSomatorioPontuacao() {
		return quantidadeDeMesesSomatorioPontuacao;
	}

	public void setQuantidadeDeMesesSomatorioPontuacao(String quantidadeDeMesesSomatorioPontuacao) {
		this.quantidadeDeMesesSomatorioPontuacao = quantidadeDeMesesSomatorioPontuacao;
	}

	public String getValorMinimoPedidosPrimeiraPosicao() {
		return valorMinimoPedidosPrimeiraPosicao;
	}

	public void setValorMinimoPedidosPrimeiraPosicao(String valorMinimoPedidosPrimeiraPosicao) {
		this.valorMinimoPedidosPrimeiraPosicao = valorMinimoPedidosPrimeiraPosicao;
	}

	public String getEmailPagseguro() {
		return emailPagseguro;
	}

	public void setEmailPagseguro(String emailPagseguro) {
		this.emailPagseguro = emailPagseguro;
	}

	public String getTokenPagseguro() {
		return tokenPagseguro;
	}

	public void setTokenPagseguro(String tokenPagseguro) {
		this.tokenPagseguro = tokenPagseguro;
	}

	public String getTokenEV() {
		return tokenEV;
	}

	public void setTokenEV(String tokenEV) {
		this.tokenEV = tokenEV;
	}
}