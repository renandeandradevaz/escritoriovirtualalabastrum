package br.com.alabastrum.escritoriovirtual.dto;

public class PesquisaEquipeDTO {

	private Integer idCodigo;
	private Integer nivel;
	private String posicao;
	private Boolean apenasIndicados;
	private Boolean ativos;

	public Integer getIdCodigo() {
		return idCodigo;
	}

	public void setIdCodigo(Integer idCodigo) {
		this.idCodigo = idCodigo;
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
}