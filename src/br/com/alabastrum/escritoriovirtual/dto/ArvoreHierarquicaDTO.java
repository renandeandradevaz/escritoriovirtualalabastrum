package br.com.alabastrum.escritoriovirtual.dto;

import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class ArvoreHierarquicaDTO {

	private Usuario usuario;
	private int nivel;

	public ArvoreHierarquicaDTO(Usuario usuario, int nivel) {

		this.usuario = usuario;
		this.nivel = nivel;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

}