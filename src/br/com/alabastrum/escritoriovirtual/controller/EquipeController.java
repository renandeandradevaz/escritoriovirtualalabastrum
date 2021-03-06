package br.com.alabastrum.escritoriovirtual.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.EquipeDTO;
import br.com.alabastrum.escritoriovirtual.dto.PesquisaEquipeDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.service.PosicoesService;
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
	int totalCadastros = arvoreHierarquica.size();

	if (Util.vazio(pesquisaEquipeDTO)) {
	    pesquisaEquipeDTO = new PesquisaEquipeDTO();
	}

	arvoreHierarquica = filtrarPorNome(arvoreHierarquica, pesquisaEquipeDTO.getNome());
	arvoreHierarquica = filtrarPorEmail(arvoreHierarquica, pesquisaEquipeDTO.getEmail());
	arvoreHierarquica = filtrarPorCpf(arvoreHierarquica, pesquisaEquipeDTO.getCpf());
	arvoreHierarquica = filtrarPorApelido(arvoreHierarquica, pesquisaEquipeDTO.getApelido());
	arvoreHierarquica = filtrarPorNivel(arvoreHierarquica, pesquisaEquipeDTO.getNivel());
	arvoreHierarquica = filtrarPorPosicao(arvoreHierarquica, pesquisaEquipeDTO.getPosicao());
	arvoreHierarquica = filtrarPorKitIngresso(arvoreHierarquica, pesquisaEquipeDTO.getKitIngresso());
	arvoreHierarquica = filtrarPorApenasIndicados(arvoreHierarquica, pesquisaEquipeDTO.getApenasIndicados());
	arvoreHierarquica = filtrarPorAtividade(arvoreHierarquica, pesquisaEquipeDTO.getAtivos());
	arvoreHierarquica = filtrarPorMesAniversario(arvoreHierarquica, pesquisaEquipeDTO.getMesAniversario());
	arvoreHierarquica = filtrarPorPagosEPendentes(arvoreHierarquica, pesquisaEquipeDTO.getPagosEPendentes());
	List<EquipeDTO> equipe = gerarEquipe(arvoreHierarquica);

	result.include("totalCadastros", totalCadastros);
	result.include("pesquisa", pesquisa);
	result.include("posicoes", new PosicoesService(hibernateUtil).obterPosicesNoMes(new GregorianCalendar()));
	result.include("pesquisaEquipeDTO", pesquisaEquipeDTO);
	result.include("equipe", equipe);
    }

    private List<EquipeDTO> gerarEquipe(Collection<ArvoreHierarquicaDTO> arvoreHierarquica) {

	List<EquipeDTO> equipe = new ArrayList<EquipeDTO>();

	for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquica) {

	    EquipeDTO equipeDTO = new EquipeDTO();
	    equipeDTO.setUsuario(arvoreHierarquicaDTO.getUsuario());
	    equipeDTO.setNivel(arvoreHierarquicaDTO.getNivel());

	    equipe.add(equipeDTO);
	}

	Collections.sort(equipe, new Comparator<EquipeDTO>() {

	    public int compare(EquipeDTO item1, EquipeDTO item2) {
		return item1.getUsuario().getvNome().compareTo(item2.getUsuario().getvNome());
	    }
	});

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

    private Collection<ArvoreHierarquicaDTO> filtrarPorNome(Collection<ArvoreHierarquicaDTO> arvoreHierarquica, String nomeFiltro) {

	List<ArvoreHierarquicaDTO> arvoreHierarquicaFiltrada = new ArrayList<ArvoreHierarquicaDTO>();

	if (Util.preenchido(nomeFiltro)) {
	    for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquica) {
		if (arvoreHierarquicaDTO.getUsuario().getvNome().toLowerCase().contains(nomeFiltro.toLowerCase())) {
		    arvoreHierarquicaFiltrada.add(arvoreHierarquicaDTO);
		}
	    }
	    return arvoreHierarquicaFiltrada;
	}
	return arvoreHierarquica;
    }

    private Collection<ArvoreHierarquicaDTO> filtrarPorEmail(Collection<ArvoreHierarquicaDTO> arvoreHierarquica, String emailFiltro) {

	List<ArvoreHierarquicaDTO> arvoreHierarquicaFiltrada = new ArrayList<ArvoreHierarquicaDTO>();

	if (Util.preenchido(emailFiltro)) {
	    for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquica) {
		if (arvoreHierarquicaDTO.getUsuario().geteMail().toLowerCase().contains(emailFiltro.toLowerCase())) {
		    arvoreHierarquicaFiltrada.add(arvoreHierarquicaDTO);
		}
	    }
	    return arvoreHierarquicaFiltrada;
	}
	return arvoreHierarquica;
    }

    private Collection<ArvoreHierarquicaDTO> filtrarPorCpf(Collection<ArvoreHierarquicaDTO> arvoreHierarquica, String cpfFiltro) {

	List<ArvoreHierarquicaDTO> arvoreHierarquicaFiltrada = new ArrayList<ArvoreHierarquicaDTO>();

	if (Util.preenchido(cpfFiltro)) {

	    cpfFiltro = cpfFiltro.replaceAll(" ", "").replaceAll("\\.", "").replaceAll("-", "");

	    for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquica) {
		if (arvoreHierarquicaDTO.getUsuario().getCPF().toLowerCase().contains(cpfFiltro.toLowerCase())) {
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

    private Collection<ArvoreHierarquicaDTO> filtrarPorKitIngresso(Collection<ArvoreHierarquicaDTO> arvoreHierarquica, String kitIngressoFiltro) {

	if (Util.preenchido(kitIngressoFiltro)) {

	    List<ArvoreHierarquicaDTO> arvoreHierarquicaFiltrada = new ArrayList<ArvoreHierarquicaDTO>();

	    for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquica) {
		if (kitIngressoFiltro.equalsIgnoreCase(arvoreHierarquicaDTO.getUsuario().getNome_kit())) {
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

    private Collection<ArvoreHierarquicaDTO> filtrarPorPagosEPendentes(Collection<ArvoreHierarquicaDTO> arvoreHierarquica, String pagosEPendentesFiltro) {

	if (Util.preenchido(pagosEPendentesFiltro)) {

	    List<ArvoreHierarquicaDTO> arvoreHierarquicaFiltrada = new ArrayList<ArvoreHierarquicaDTO>();

	    for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquica) {

		Usuario usuario = this.hibernateUtil.selecionar(new Usuario(arvoreHierarquicaDTO.getUsuario().getId_Codigo()));

		if (pagosEPendentesFiltro.equals("pagos") && usuario.getCadPreCadastro().equals(0)) {
		    arvoreHierarquicaFiltrada.add(arvoreHierarquicaDTO);
		} else if (pagosEPendentesFiltro.equals("pendentes") && usuario.getCadPreCadastro().equals(1)) {
		    arvoreHierarquicaFiltrada.add(arvoreHierarquicaDTO);
		}
	    }
	    return arvoreHierarquicaFiltrada;
	}
	return arvoreHierarquica;
    }
}
