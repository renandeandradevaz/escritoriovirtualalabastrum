package br.com.alabastrum.escritoriovirtual.dto;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class AnaliseDTO {

	private Integer codigoUsuario;
	private Long contagemAcessos;

	public Usuario getUsuario() {

		HibernateUtil hibernateUtil = new HibernateUtil();

		Usuario usuario = new Usuario();

		usuario = hibernateUtil.selecionar(usuario);

		hibernateUtil.fecharSessao();

		return usuario;
	}

	public Integer getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Integer codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public Long getContagemAcessos() {
		return contagemAcessos;
	}

	public void setContagemAcessos(Long contagemAcessos) {
		this.contagemAcessos = contagemAcessos;
	}

}