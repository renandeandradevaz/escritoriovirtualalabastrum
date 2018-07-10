package br.com.alabastrum.escritoriovirtual.controller;

import java.util.Calendar;
import java.util.TreeMap;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.ExtratoService;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.service.PosicoesService;
import br.com.alabastrum.escritoriovirtual.service.QualificacaoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Util;
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

		Usuario usuario = this.sessaoUsuario.getUsuario();

		TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquica = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(usuario.getId_Codigo());
		result.include("quantidadeAfiliados", arvoreHierarquica.size());
		result.include("ultimosQualificados", new QualificacaoService(hibernateUtil).obterUltimosQualificados(arvoreHierarquica));
		result.include("ultimosCadastros", new QualificacaoService(hibernateUtil).obterUltimosCadastros(arvoreHierarquica));
		result.include("posicaoAtual", usuario.getPosAtual().replaceAll(" ", "").toLowerCase());
		result.include("proximaPosicao", new PosicoesService(hibernateUtil).obterNomeProximaPosicao(usuario.getPosAtual()).replaceAll(" ", "").toLowerCase());
		result.include("saldoAtual", new ExtratoService(hibernateUtil).gerarSaldoEExtrato(usuario.getId_Codigo(), Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR)).getSaldoAtual());
	}
}
