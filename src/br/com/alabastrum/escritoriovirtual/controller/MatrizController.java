package br.com.alabastrum.escritoriovirtual.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.service.MatrizService;
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
    public void acessarTelaMatrizMultilevel(Integer codigo) throws Exception {
	gerarMatriz(codigo, null);
	result.include("nomeMatriz", "Matriz Multilevel");
	result.include("tipoDeMatriz", "Multilevel");
	result.forwardTo("/WEB-INF/jsp//matriz/acessarTelaMatriz.jsp");
    }

    @Funcionalidade
    public void acessarTelaMatrizTrinaria(Integer codigo) throws Exception {

	Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaCompleta = gerarMatriz(codigo, "id_lider");

	if (codigo == null) {
	    result.include("quantidadesExistentes", new MatrizService().calcularQuantidadesExistentes(arvoreHierarquicaCompleta));
	}

	result.include("nomeMatriz", "Matriz Trinária");
	result.include("tipoDeMatriz", "Trinaria");
	result.forwardTo("/WEB-INF/jsp//matriz/acessarTelaMatriz.jsp");
    }

    private Map<Integer, ArvoreHierarquicaDTO> gerarMatriz(Integer codigo, String tipoDeFiltro) {

	Usuario usuario = this.sessaoUsuario.getUsuario();

	Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaCompleta = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(usuario.getId_Codigo(), tipoDeFiltro);

	if (codigo != null && arvoreHierarquicaCompleta.containsKey(codigo)) {
	    arvoreHierarquicaCompleta = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(codigo, tipoDeFiltro);
	    usuario = hibernateUtil.selecionar(new Usuario(codigo));
	}

	List<ArvoreHierarquicaDTO> arvoreHierarquicaFiltrada = new ArrayList<ArvoreHierarquicaDTO>();

	for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquicaCompleta.values()) {

	    if (arvoreHierarquicaDTO.getNivel() <= QUANTIDADE_DE_NIVEIS) {
		arvoreHierarquicaFiltrada.add(arvoreHierarquicaDTO);
	    }
	}

	result.include("lider", usuario);
	result.include("arvoreHierarquica", arvoreHierarquicaFiltrada);

	return arvoreHierarquicaCompleta;
    }
}
