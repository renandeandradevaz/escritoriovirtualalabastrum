package br.com.alabastrum.escritoriovirtual.dto;

public class PesquisaRelatorioAtividadesRecentesDTO {

    private String tipoDeAtividade;
    private String dataInicial;
    private String dataFinal;

    public String getTipoDeAtividade() {
	return tipoDeAtividade;
    }

    public void setTipoDeAtividade(String tipoDeAtividade) {
	this.tipoDeAtividade = tipoDeAtividade;
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
}