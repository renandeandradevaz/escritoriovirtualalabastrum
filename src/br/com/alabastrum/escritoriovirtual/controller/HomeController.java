package br.com.alabastrum.escritoriovirtual.controller;

import java.util.List;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.PontuacaoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class HomeController {

    private Result result;
    private HibernateUtil hibernateUtil;
    private SessaoUsuario sessaoUsuario;

    public HomeController(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
	this.sessaoUsuario = sessaoUsuario;
    }

    @Path("/home")
    @Funcionalidade
    public void home() throws Exception {

	Usuario usuarioLogado = this.sessaoUsuario.getUsuario();

	Integer totalAbaixoFilaUnica = 0;

	if (usuarioLogado.getFila_unica() != null && usuarioLogado.getFila_unica() > 0) {

	    List<Usuario> usuarios = this.hibernateUtil.buscar(new Usuario());
	    for (Usuario usuario : usuarios) {
		if (usuario.getFila_unica() != null && usuario.getFila_unica() > usuarioLogado.getFila_unica()) {
		    totalAbaixoFilaUnica++;
		}
	    }
	}

	result.include("totalAbaixoFilaUnica", totalAbaixoFilaUnica);
	result.include("graduacaoMensal", new PontuacaoService(this.hibernateUtil).calcularGraduacaoMensalPorPontuacaoDeProduto(usuarioLogado.getId_Codigo()));
    }
}
