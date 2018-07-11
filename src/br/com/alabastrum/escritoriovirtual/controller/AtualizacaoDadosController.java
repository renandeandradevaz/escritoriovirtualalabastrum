package br.com.alabastrum.escritoriovirtual.controller;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.anotacoes.Public;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class AtualizacaoDadosController {

	private Result result;
	private SessaoUsuario sessaoUsuario;
	private HibernateUtil hibernateUtil;

	public AtualizacaoDadosController(Result result, SessaoUsuario sessaoUsuario, HibernateUtil hibernateUtil) {

		this.result = result;
		this.sessaoUsuario = sessaoUsuario;
		this.hibernateUtil = hibernateUtil;
	}

	@Funcionalidade
	public void acessarTelaAtualizacaoDados() {

		Usuario usuario = this.hibernateUtil.selecionar(new Usuario(this.sessaoUsuario.getUsuario().getId_Codigo()));
		result.include("usuario", usuario);
	}

	@Funcionalidade
	public void salvarAtualizacaoDados(Usuario usuario) throws Exception {

		String textoArquivo = "id_codigo: \'" + this.sessaoUsuario.getUsuario().getId_Codigo() + "\'\r\n";
		textoArquivo += "CPF: \'" + this.sessaoUsuario.getUsuario().getCPF() + "\'\r\n";
		textoArquivo += "Nome: \'" + usuario.getvNome() + "\'\r\n";
		textoArquivo += "Data_de_nascimento: \'" + usuario.getDt_Nasc() + "\'\r\n";
		textoArquivo += "RG: \'" + usuario.getCadRG() + "\'\r\n";
		textoArquivo += "Emissor: \'" + usuario.getCadOrgaoExpedidor() + "\'\r\n";
		textoArquivo += "Sexo: \'" + usuario.getCadSexo() + "\'\r\n";
		textoArquivo += "Estado_civil: \'" + usuario.getCadEstCivil() + "\'\r\n";
		textoArquivo += "CEP: \'" + usuario.getCadCEP() + "\'\r\n";
		textoArquivo += "Endereco: \'" + usuario.getCadEndereco() + "\'\r\n";
		textoArquivo += "Bairro: \'" + usuario.getCadBairro() + "\'\r\n";
		textoArquivo += "Cidade: \'" + usuario.getCadCidade() + "\'\r\n";
		textoArquivo += "Estado: \'" + usuario.getCadUF() + "\'\r\n";
		textoArquivo += "Telefone_residencial: \'" + usuario.getTel() + "\'\r\n";
		textoArquivo += "Telefone_celular: \'" + usuario.getCadCelular() + "\'\r\n";
		textoArquivo += "Email: \'" + usuario.geteMail() + "\'\r\n";
		textoArquivo += "banco: \'" + usuario.getCadBanco() + "\'\r\n";
		textoArquivo += "tipo_conta: \'" + usuario.getCadTipoConta() + "\'\r\n";
		textoArquivo += "agencia: \'" + usuario.getCadAgencia() + "\'\r\n";
		textoArquivo += "conta: \'" + usuario.getCadCCorrente() + "\'\r\n";

		ArquivoService.criarArquivoNoDisco(textoArquivo, ArquivoService.PASTA_ATUALIZACAO_DADOS);
		Mail.enviarEmail("Atualização de dados de usuário", textoArquivo);

		result.include("sucesso", "Sua solicitação de alteração de dados cadastrais foi feita com sucesso. Estamos avaliando seus dados, e em breve faremos a atualização no sistema.");

		result.redirectTo(HomeController.class).home();
	}

	@Public
	public void salvarPreCadastroDistribuidorPeloSite(Usuario sessaoAtualizacaoDados) throws Exception {

		Usuario usuario = sessaoAtualizacaoDados;

		String textoArquivo = "Nome: \'" + usuario.getvNome() + "\'\r\n";
		textoArquivo += "Data_de_nascimento: \'" + usuario.getDt_Nasc() + "\'\r\n";
		textoArquivo += "CPF: \'" + usuario.getCPF() + "\'\r\n";
		textoArquivo += "RG: \'" + usuario.getCadRG() + "\'\r\n";
		textoArquivo += "Emissor: \'" + usuario.getCadOrgaoExpedidor() + "\'\r\n";
		textoArquivo += "Sexo: \'" + usuario.getCadSexo() + "\'\r\n";
		textoArquivo += "Estado_civil: \'" + usuario.getCadEstCivil() + "\'\r\n";
		textoArquivo += "CEP: \'" + usuario.getCadCEP() + "\'\r\n";
		textoArquivo += "Endereco: \'" + usuario.getCadEndereco() + "\'\r\n";
		textoArquivo += "Bairro: \'" + usuario.getCadBairro() + "\'\r\n";
		textoArquivo += "Cidade: \'" + usuario.getCadCidade() + "\'\r\n";
		textoArquivo += "Estado: \'" + usuario.getCadUF() + "\'\r\n";
		textoArquivo += "Telefone_residencial: \'" + usuario.getTel() + "\'\r\n";
		textoArquivo += "Telefone_celular: \'" + usuario.getCadCelular() + "\'\r\n";
		textoArquivo += "Email: \'" + usuario.geteMail() + "\'\r\n";
		textoArquivo += "Codigo: \'" + usuario.getCodigoQuemIndicou() + "\'\r\n";
		textoArquivo += "nomepatroc: \'" + usuario.getNomeQuemIndicou() + "\'\r\n";

		ArquivoService.criarArquivoNoDisco(textoArquivo, ArquivoService.PASTA_PRE_CADASTRO);
		Mail.enviarEmail("Pré cadastro pelo site", textoArquivo);

		result.redirectTo("https://alabastrum.com.br/sucesso");
	}
}
