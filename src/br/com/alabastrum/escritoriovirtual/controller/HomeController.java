package br.com.alabastrum.escritoriovirtual.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.service.MatrizService;
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

	Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaCompletaPorIdLider = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(usuarioLogado.getId_Codigo(), "id_lider");
	Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaCompletaPorIdIndicante = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(usuarioLogado.getId_Codigo());
	Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaPorIdIndicanteNivel1 = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(usuarioLogado.getId_Codigo(), 1);

	int ativos = 0;
	int inativos = 0;

	for (Entry<Integer, ArvoreHierarquicaDTO> distribuidorEntry : arvoreHierarquicaCompletaPorIdIndicante.entrySet()) {

	    if (new AtividadeService(hibernateUtil).isAtivo(distribuidorEntry.getKey()))
		ativos++;
	    else
		inativos++;

	}

	result.include("totalAbaixoFilaUnica", totalAbaixoFilaUnica);
	result.include("graduacaoMensal", new PontuacaoService(this.hibernateUtil).calcularGraduacaoMensal(usuarioLogado.getId_Codigo(), null));
	result.include("quantidadesExistentes", new MatrizService().calcularQuantidadesExistentes(arvoreHierarquicaCompletaPorIdLider));
	result.include("diretos", arvoreHierarquicaPorIdIndicanteNivel1.size());
	result.include("equipe", arvoreHierarquicaCompletaPorIdIndicante.size());
	result.include("ativos", ativos);
	result.include("inativos", inativos);

    }
}
