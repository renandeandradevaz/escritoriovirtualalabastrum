package br.com.alabastrum.escritoriovirtual.dto;

import java.util.HashMap;

public class PesquisaRelatorioVendasPorPADTO {

    private HashMap<String, String> idsFranquias;
    private String dataInicial;
    private String dataFinal;

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

    public HashMap<String, String> getIdsFranquias() {
	return idsFranquias;
    }

    public void setIdsFranquias(HashMap<String, String> idsFranquias) {
	this.idsFranquias = idsFranquias;
    }
}