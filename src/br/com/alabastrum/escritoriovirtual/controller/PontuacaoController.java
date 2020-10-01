package br.com.alabastrum.escritoriovirtual.controller;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.caelum.vraptor.Resource;

@Resource
public class PontuacaoController {

    @Funcionalidade
    public void acessarTelaPontuacao() {

	// result.include("pontuacaoDTO", new
	// PontuacaoService(hibernateUtil).calcularPontuacoesRelatorio(this.sessaoUsuario.getUsuario().getId_Codigo()));
    }
}
