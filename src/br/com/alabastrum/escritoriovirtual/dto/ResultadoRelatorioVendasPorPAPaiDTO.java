package br.com.alabastrum.escritoriovirtual.dto;

import java.util.List;

public class ResultadoRelatorioVendasPorPAPaiDTO {

    private String franquia;
    private List<ResultadoRelatorioVendasPorPAFilhoDTO> filhos;

    public String getFranquia() {

	return franquia;
    }

    public void setFranquia(String franquia) {
	this.franquia = franquia;
    }

    public List<ResultadoRelatorioVendasPorPAFilhoDTO> getFilhos() {
	return filhos;
    }

    public void setFilhos(List<ResultadoRelatorioVendasPorPAFilhoDTO> filhos) {
	this.filhos = filhos;
    }
}