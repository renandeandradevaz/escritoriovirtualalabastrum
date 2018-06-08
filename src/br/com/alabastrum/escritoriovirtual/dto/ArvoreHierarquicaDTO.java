package br.com.alabastrum.escritoriovirtual.dto;

import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class ArvoreHierarquicaDTO {

	private Usuario usuario;
	private Integer nivel;

	public ArvoreHierarquicaDTO(Usuario usuario, Integer nivel) {

		this.usuario = usuario;
		this.nivel = nivel;
	}

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
}