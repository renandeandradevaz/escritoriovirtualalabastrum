package br.com.alabastrum.escritoriovirtual.dto;

public class ResultadoRelatorioVendasPorPAFilhoDTO {

    private Integer ano;
    private Integer mes;
    private Integer dia;
    private Integer valor;

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

    public Integer getDia() {
	return dia;
    }

    public void setDia(Integer dia) {
	this.dia = dia;
    }

    public Integer getValor() {
	return valor;
    }

    public void setValor(Integer valor) {
	this.valor = valor;
    }
}