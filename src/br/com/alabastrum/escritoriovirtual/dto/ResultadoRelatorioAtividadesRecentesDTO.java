package br.com.alabastrum.escritoriovirtual.dto;

import java.util.GregorianCalendar;

public class ResultadoRelatorioAtividadesRecentesDTO {

    private String tipoDeAtividade;
    private GregorianCalendar data;
    private String identificador;

    public String getTipoDeAtividade() {
	return tipoDeAtividade;
    }

    public void setTipoDeAtividade(String tipoDeAtividade) {
	this.tipoDeAtividade = tipoDeAtividade;
    }

    public GregorianCalendar getData() {
	return data;
    }

    public void setData(GregorianCalendar data) {
	this.data = data;
    }

    public String getIdentificador() {
	return identificador;
    }

    public void setIdentificador(String identificador) {
	this.identificador = identificador;
    }
}