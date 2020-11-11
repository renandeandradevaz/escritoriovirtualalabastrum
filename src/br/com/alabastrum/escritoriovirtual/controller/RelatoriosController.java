package br.com.alabastrum.escritoriovirtual.controller;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.caelum.vraptor.Resource;

@Resource
public class RelatoriosController {

    @Funcionalidade(administrativa = "true")
    public void acessarTelaRelatorios() {
    }
}
