package br.com.alabastrum.escritoriovirtual.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class MatrizController {

	private static final Integer QUANTIDADE_DE_NIVEIS = 3;

	private Result result;
	private HibernateUtil hibernateUtil;
	private SessaoUsuario sessaoUsuario;

	public MatrizController(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario) {

		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.sessaoUsuario = sessaoUsuario;
	}

	@Funcionalidade
	public void acessarTelaMatriz(Integer codigo) throws Exception {

		Usuario usuario = this.sessaoUsuario.getUsuario();

		Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaCompleta = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(usuario.getId_Codigo());

		if (arvoreHierarquicaCompleta.containsKey(codigo)) {
			arvoreHierarquicaCompleta = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(codigo);
			usuario = hibernateUtil.selecionar(new Usuario(codigo));
		}

		List<ArvoreHierarquicaDTO> arvoreHierarquicaFiltrada = new ArrayList<ArvoreHierarquicaDTO>();

		for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquicaCompleta.values()) {

			if (arvoreHierarquicaDTO.getNivel() <= QUANTIDADE_DE_NIVEIS) {
				arvoreHierarquicaDTO.getUsuario().setvNome(arvoreHierarquicaDTO.getUsuario().getvNome().split(" ")[0]);
				arvoreHierarquicaFiltrada.add(arvoreHierarquicaDTO);
			}
		}

		usuario.setvNome(usuario.getvNome().split(" ")[0]);
		result.include("lider", usuario);
		result.include("arvoreHierarquica", arvoreHierarquicaFiltrada);
	}
}
