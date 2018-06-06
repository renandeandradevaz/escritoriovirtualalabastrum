package br.com.alabastrum.escritoriovirtual.dto;

import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class QualificacaoDTO {

	private Usuario usuario;
	private Qualificacao qualificacao;

	public QualificacaoDTO(Usuario usuario, Qualificacao qualificacao) {
		this.usuario = usuario;
		this.qualificacao = qualificacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Qualificacao getQualificacao() {
		return qualificacao;
	}

	public void setQualificacao(Qualificacao qualificacao) {
		this.qualificacao = qualificacao;
	}
}