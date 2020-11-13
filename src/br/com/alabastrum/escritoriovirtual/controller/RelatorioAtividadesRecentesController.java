package br.com.alabastrum.escritoriovirtual.controller;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.PesquisaRelatorioAtividadesRecentesDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RelatorioAtividadesRecentesController {

    private Result result;
    private HibernateUtil hibernateUtil;

    public RelatorioAtividadesRecentesController(Result result, HibernateUtil hibernateUtil) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
    }

    @Funcionalidade(administrativa = "true")
    public void acessarRelatorioAtividadesRecentes() {
    }

    @Funcionalidade(administrativa = "true")
    public void pesquisarAtividadesRecentes(PesquisaRelatorioAtividadesRecentesDTO pesquisaRelatorioAtividadesRecentesDTO) throws Exception {

	result.include("pesquisaRelatorioAtividadesRecentesDTO", pesquisaRelatorioAtividadesRecentesDTO);
	result.include("pesquisa", true);

	result.forwardTo(this).acessarRelatorioAtividadesRecentes();
    }
}
