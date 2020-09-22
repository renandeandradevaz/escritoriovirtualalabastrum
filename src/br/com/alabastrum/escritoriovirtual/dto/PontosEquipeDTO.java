package br.com.alabastrum.escritoriovirtual.dto;

import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class PontosEquipeDTO {

    private Usuario usuario;
    private Integer volumeIndividualPagavel;
    private Integer volumeIndividualQualificavel;
    private Integer volumePagavelTodosOsNiveis;
    private Integer volumeQualificavelTodosOsNiveis;

    public Usuario getUsuario() {
	return usuario;
    }

    public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
    }

    public Integer getVolumeIndividualPagavel() {
	return volumeIndividualPagavel;
    }

    public void setVolumeIndividualPagavel(Integer volumeIndividualPagavel) {
	this.volumeIndividualPagavel = volumeIndividualPagavel;
    }

    public Integer getVolumeIndividualQualificavel() {
	return volumeIndividualQualificavel;
    }

    public void setVolumeIndividualQualificavel(Integer volumeIndividualQualificavel) {
	this.volumeIndividualQualificavel = volumeIndividualQualificavel;
    }

    public Integer getVolumePagavelTodosOsNiveis() {
	return volumePagavelTodosOsNiveis;
    }

    public void setVolumePagavelTodosOsNiveis(Integer volumePagavelTodosOsNiveis) {
	this.volumePagavelTodosOsNiveis = volumePagavelTodosOsNiveis;
    }

    public Integer getVolumeQualificavelTodosOsNiveis() {
	return volumeQualificavelTodosOsNiveis;
    }

    public void setVolumeQualificavelTodosOsNiveis(Integer volumeQualificavelTodosOsNiveis) {
	this.volumeQualificavelTodosOsNiveis = volumeQualificavelTodosOsNiveis;
    }
}