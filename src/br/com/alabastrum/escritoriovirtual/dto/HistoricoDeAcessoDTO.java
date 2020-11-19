package br.com.alabastrum.escritoriovirtual.dto;

public class HistoricoDeAcessoDTO {

    private Integer ano;
    private Integer mes;
    private Integer quantidade;

    public Integer getAno() {
	return ano;
    }

    public void setAno(Integer ano) {
	this.ano = ano;
    }

    public Integer getMes() {
	return mes;
    }

    public void setMes(Integer mes) {
	this.mes = mes;
    }

    public Integer getQuantidade() {
	return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
	this.quantidade = quantidade;
    }
}