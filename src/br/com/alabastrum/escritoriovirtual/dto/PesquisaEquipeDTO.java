package br.com.alabastrum.escritoriovirtual.dto;

public class PesquisaEquipeDTO {

    private String apelido;
    private Integer nivel;
    private String posicao;
    private Boolean apenasIndicados;
    private Boolean ativos;
    private String mesAniversario;
    private String pagosEPendentes;

    public String getApelido() {
	return apelido;
    }

    public void setApelido(String apelido) {
	this.apelido = apelido;
    }

    public Integer getNivel() {
	return nivel;
    }

    public void setNivel(Integer nivel) {
	this.nivel = nivel;
    }

    public String getPosicao() {
	return posicao;
    }

    public void setPosicao(String posicao) {
	this.posicao = posicao;
    }

    public Boolean getApenasIndicados() {
	return apenasIndicados;
    }

    public void setApenasIndicados(Boolean apenasIndicados) {
	this.apenasIndicados = apenasIndicados;
    }

    public Boolean getAtivos() {
	return ativos;
    }

    public void setAtivos(Boolean ativos) {
	this.ativos = ativos;
    }

    public String getMesAniversario() {
	return mesAniversario;
    }

    public void setMesAniversario(String mesAniversario) {
	this.mesAniversario = mesAniversario;
    }

    public String getPagosEPendentes() {
	return pagosEPendentes;
    }

    public void setPagosEPendentes(String pagosEPendentes) {
	this.pagosEPendentes = pagosEPendentes;
    }
}