package br.com.alabastrum.escritoriovirtual.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.EquipeDTO;
import br.com.alabastrum.escritoriovirtual.dto.PesquisaEquipeDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.service.QualificacaoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class EquipeController {

    private Result result;
    private HibernateUtil hibernateUtil;
    private SessaoUsuario sessaoUsuario;

    public EquipeController(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
	this.sessaoUsuario = sessaoUsuario;
    }

    @Funcionalidade
    public void acessarTelaEquipe(PesquisaEquipeDTO pesquisaEquipeDTO, boolean pesquisa) throws Exception {

	Usuario usuario = this.sessaoUsuario.getUsuario();

	Collection<ArvoreHierarquicaDTO> arvoreHierarquica = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(usuario.getId_Codigo()).values();

	if (Util.vazio(pesquisaEquipeDTO)) {
	    pesquisaEquipeDTO = new PesquisaEquipeDTO();
	}

	arvoreHierarquica = filtrarPorApelido(arvoreHierarquica, pesquisaEquipeDTO.getApelido());
	arvoreHierarquica = filtrarPorNivel(arvoreHierarquica, pesquisaEquipeDTO.getNivel());
	arvoreHierarquica = filtrarPorPosicao(arvoreHierarquica, pesquisaEquipeDTO.getPosicao());
	arvoreHierarquica = filtrarPorApenasIndicados(arvoreHierarquica, pesquisaEquipeDTO.getApenasIndicados());
	arvoreHierarquica = filtrarPorAtividade(arvoreHierarquica, pesquisaEquipeDTO.getAtivos());
	arvoreHierarquica = filtrarPorMesAniversario(arvoreHierarquica, pesquisaEquipeDTO.getMesAniversario());
	List<EquipeDTO> equipe = gerarEquipe(arvoreHierarquica);

	result.include("pesquisa", pesquisa);
	result.include("posicoes", hibernateUtil.buscar(new Posicao()));
	result.include("pesquisaEquipeDTO", pesquisaEquipeDTO);
	result.include("equipe", equipe);
    }

    private List<EquipeDTO> gerarEquipe(Collection<ArvoreHierarquicaDTO> arvoreHierarquica) {

	List<EquipeDTO> equipe = new ArrayList<EquipeDTO>();

	for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquica) {

	    EquipeDTO equipeDTO = new EquipeDTO();
	    equipeDTO.setUsuario(arvoreHierarquicaDTO.getUsuario());
	    equipeDTO.setNivel(arvoreHierarquicaDTO.getNivel());

	    if (new QualificacaoService(hibernateUtil).obterQualificacoes(arvoreHierarquicaDTO.getUsuario().getId_Codigo()).size() == 1)
		equipeDTO.setPreCadastro("Sim");
	    else
		equipeDTO.setPreCadastro("Não");

	    equipe.add(equipeDTO);
	}
	return equipe;
    }

    @Funcionalidade
    public void pesquisar(PesquisaEquipeDTO pesquisaEquipeDTO) throws Exception {

	result.forwardTo(this).acessarTelaEquipe(pesquisaEquipeDTO, true);
    }

    private Collection<ArvoreHierarquicaDTO> filtrarPorApelido(Collection<ArvoreHierarquicaDTO> arvoreHierarquica, String apelidoFiltro) {

	List<ArvoreHierarquicaDTO> arvoreHierarquicaFiltrada = new ArrayList<ArvoreHierarquicaDTO>();

	if (Util.preenchido(apelidoFiltro)) {
	    for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquica) {
		if (arvoreHierarquicaDTO.getUsuario().getApelido().equalsIgnoreCase(apelidoFiltro.replaceAll(" ", ""))) {
		    arvoreHierarquicaFiltrada.add(arvoreHierarquicaDTO);
		}
	    }
	    return arvoreHierarquicaFiltrada;
	}
	return arvoreHierarquica;
    }

    private Collection<ArvoreHierarquicaDTO> filtrarPorNivel(Collection<ArvoreHierarquicaDTO> arvoreHierarquica, Integer nivelFiltro) {

	if (Util.preenchido(nivelFiltro)) {

	    List<ArvoreHierarquicaDTO> arvoreHierarquicaFiltrada = new ArrayList<ArvoreHierarquicaDTO>();

	    for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquica) {
		if (arvoreHierarquicaDTO.getNivel().equals(nivelFiltro)) {
		    arvoreHierarquicaFiltrada.add(arvoreHierarquicaDTO);
		}
	    }
	    return arvoreHierarquicaFiltrada;
	}
	return arvoreHierarquica;
    }

    private Collection<ArvoreHierarquicaDTO> filtrarPorPosicao(Collection<ArvoreHierarquicaDTO> arvoreHierarquica, String posicaoFiltro) {

	if (Util.preenchido(posicaoFiltro)) {

	    List<ArvoreHierarquicaDTO> arvoreHierarquicaFiltrada = new ArrayList<ArvoreHierarquicaDTO>();

	    for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquica) {
		if (arvoreHierarquicaDTO.getUsuario().getPosAtual().equals(posicaoFiltro)) {
		    arvoreHierarquicaFiltrada.add(arvoreHierarquicaDTO);
		}
	    }
	    return arvoreHierarquicaFiltrada;
	}
	return arvoreHierarquica;
    }

    private Collection<ArvoreHierarquicaDTO> filtrarPorApenasIndicados(Collection<ArvoreHierarquicaDTO> arvoreHierarquica, Boolean apenasIndicados) {

	if (Util.preenchido(apenasIndicados) && apenasIndicados) {

	    List<ArvoreHierarquicaDTO> arvoreHierarquicaFiltrada = new ArrayList<ArvoreHierarquicaDTO>();

	    for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquica) {
		if (arvoreHierarquicaDTO.getUsuario().getId_Indicante().equals(this.sessaoUsuario.getUsuario().getId_Codigo())) {
		    arvoreHierarquicaFiltrada.add(arvoreHierarquicaDTO);
		}
	    }
	    return arvoreHierarquicaFiltrada;
	}
	return arvoreHierarquica;
    }

    private Collection<ArvoreHierarquicaDTO> filtrarPorMesAniversario(Collection<ArvoreHierarquicaDTO> arvoreHierarquica, String mesAniversario) {

	if (Util.preenchido(mesAniversario)) {

	    List<ArvoreHierarquicaDTO> arvoreHierarquicaFiltrada = new ArrayList<ArvoreHierarquicaDTO>();

	    for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquica) {
		if (arvoreHierarquicaDTO.getUsuario().getDt_Nasc().contains("/" + mesAniversario + "/")) {
		    arvoreHierarquicaFiltrada.add(arvoreHierarquicaDTO);
		}
	    }
	    return arvoreHierarquicaFiltrada;
	}
	return arvoreHierarquica;
    }

    private Collection<ArvoreHierarquicaDTO> filtrarPorAtividade(Collection<ArvoreHierarquicaDTO> arvoreHierarquica, Boolean ativosFiltro) {

	if (Util.preenchido(ativosFiltro)) {

	    List<ArvoreHierarquicaDTO> arvoreHierarquicaFiltrada = new ArrayList<ArvoreHierarquicaDTO>();

	    for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquica) {

		boolean usuarioAtivo = new AtividadeService(hibernateUtil).isAtivo(arvoreHierarquicaDTO.getUsuario().getId_Codigo());

		if ((ativosFiltro && usuarioAtivo) || (!ativosFiltro && !usuarioAtivo)) {
		    arvoreHierarquicaFiltrada.add(arvoreHierarquicaDTO);
		}
	    }
	    return arvoreHierarquicaFiltrada;
	}
	return arvoreHierarquica;
    }
}
