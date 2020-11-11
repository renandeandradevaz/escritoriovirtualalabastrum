package br.com.alabastrum.escritoriovirtual.dto;

public class PesquisaRelatorioPedidosDTO {

    private String apelido;
    private String numeroPedido;
    private String status;
    private String dataInicial;
    private String dataFinal;
    private String origem;
    private String ordenacao;

    public String getApelido() {
	return apelido;
    }

    public void setApelido(String apelido) {
	this.apelido = apelido;
    }

    public String getNumeroPedido() {
	return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
	this.numeroPedido = numeroPedido;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getDataInicial() {
	return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
	this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
	return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
	this.dataFinal = dataFinal;
    }

    public String getOrigem() {
	return origem;
    }

    public void setOrigem(String origem) {
	this.origem = origem;
    }

    public String getOrdenacao() {
	return ordenacao;
    }

    public void setOrdenacao(String ordenacao) {
	this.ordenacao = ordenacao;
    }
}