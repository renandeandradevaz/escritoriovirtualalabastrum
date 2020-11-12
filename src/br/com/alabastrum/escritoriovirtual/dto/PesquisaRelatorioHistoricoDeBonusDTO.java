package br.com.alabastrum.escritoriovirtual.dto;

public class PesquisaRelatorioHistoricoDeBonusDTO {

    private String apelido;
    private String tipoDeBonus;
    private String dataInicial;
    private String dataFinal;

    public String getApelido() {
	return apelido;
    }

    public void setApelido(String apelido) {
	this.apelido = apelido;
    }

    public String getTipoDeBonus() {
	return tipoDeBonus;
    }

    public void setTipoDeBonus(String tipoDeBonus) {
	this.tipoDeBonus = tipoDeBonus;
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