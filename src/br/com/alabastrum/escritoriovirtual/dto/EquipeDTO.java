package br.com.alabastrum.escritoriovirtual.dto;

import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class EquipeDTO {

	private Usuario usuario;
	private Integer nivel;
	private String preCadastro;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getNivel() {
		return nivel;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	public String getPreCadastro() {
		return preCadastro;
	}

	public void setPreCadastro(String preCadastro) {
		this.preCadastro = preCadastro;
	}
}