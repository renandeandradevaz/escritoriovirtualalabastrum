package br.com.alabastrum.escritoriovirtual.controller;

import java.lang.reflect.Field;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ConfiguracaoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Configuracao;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ConfiguracaoController {

	private Result result;
	private SessaoUsuario sessaoUsuario;
	private HibernateUtil hibernateUtil;

	public ConfiguracaoController(Result result, SessaoUsuario sessaoUsuario, HibernateUtil hibernateUtil) {

		this.result = result;
		this.sessaoUsuario = sessaoUsuario;
		this.hibernateUtil = hibernateUtil;
	}

	@Funcionalidade()
	public void acessarTelaConfiguracao() throws Exception {

		Configuracao filtro = new Configuracao();
		filtro.setId_Codigo(sessaoUsuario.getUsuario().getId_Codigo());
		List<Configuracao> configuracoes = hibernateUtil.buscar(filtro);

		ConfiguracaoDTO configuracaoDTO = new ConfiguracaoDTO();

		for (Configuracao configuracao : configuracoes) {

			Field field = configuracaoDTO.getClass().getDeclaredField(configuracao.getChave());
			field.setAccessible(true);
			field.set(configuracaoDTO, configuracao.getValor());
		}

		result.include("configuracaoDTO", configuracaoDTO);
	}

	@Funcionalidade()
	public void salvarConfiguracoes(ConfiguracaoDTO configuracaoDTO) throws Exception {

		Integer id_Codigo = sessaoUsuario.getUsuario().getId_Codigo();

		for (Field field : configuracaoDTO.getClass().getDeclaredFields()) {
			salvarConfiguracao(configuracaoDTO, id_Codigo, field.getName());
		}

		ArquivoService.criarArquivoNoDisco(montarTextoArquivo(configuracaoDTO, id_Codigo), ArquivoService.CAMINHO_PASTA_CONFIGURACOES);

		result.include("sucesso", "Configurações salvas com sucesso");

		result.forwardTo(this).acessarTelaConfiguracao();
	}

	private void salvarConfiguracao(ConfiguracaoDTO configuracaoDTO, Integer id_Codigo, String chave) throws Exception {

		Configuracao filtro = new Configuracao();
		filtro.setId_Codigo(id_Codigo);
		filtro.setChave(chave);
		Configuracao configuracao = hibernateUtil.selecionar(filtro);

		if (configuracao == null) {
			configuracao = new Configuracao();
			configuracao.setId_Codigo(id_Codigo);
			configuracao.setChave(chave);
		}

		Field field = configuracaoDTO.getClass().getDeclaredField(chave);
		field.setAccessible(true);
		configuracao.setValor((String) field.get(configuracaoDTO));

		hibernateUtil.salvarOuAtualizar(configuracao);
	}

	private String montarTextoArquivo(ConfiguracaoDTO configuracaoDTO, Integer id_Codigo) throws Exception {

		String textoArquivo = "";

		textoArquivo += "id_Codigo" + "=" + id_Codigo + "\r\n";

		for (Field field : configuracaoDTO.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			textoArquivo += field.getName() + "=" + field.get(configuracaoDTO) + "\r\n";
		}

		return textoArquivo;
	}
}
