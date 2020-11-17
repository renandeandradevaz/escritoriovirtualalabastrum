package br.com.alabastrum.escritoriovirtual.dto;

public class PesquisaRelatorioResultadoOperacionalDTO {

    private String dataInicial;
    private String dataFinal;
    private String origem;

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
}