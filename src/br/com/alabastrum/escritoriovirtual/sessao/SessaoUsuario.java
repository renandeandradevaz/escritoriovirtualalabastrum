package br.com.alabastrum.escritoriovirtual.sessao;

import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class SessaoUsuario {

    private Usuario usuario;
    private boolean ativo;

    public Usuario getUsuario() {
	return usuario;
    }

    public void login(Usuario usuario, boolean ativo) {
	this.usuario = usuario;
	this.ativo = ativo;
    }

    public void logout() {
	this.usuario = null;
    }

    public boolean isAtivo() {
	return ativo;
    }

    public void setAtivo(boolean ativo) {
	this.ativo = ativo;
    }

}
