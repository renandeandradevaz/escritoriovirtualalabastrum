package br.com.alabastrum.escritoriovirtual.controller;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.anotacoes.Public;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.JavaMailApp;
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

		String textoEmail = "O usuário com código " + this.sessaoUsuario.getUsuario().getId_Codigo() + " atualizou seus dados no escritório virtual<br><br>";

		textoEmail += "Informações:<br>";

		textoEmail += "<br> <b>Nome: </b> " + usuario.getvNome();
		textoEmail += "<br> <b>Data de nascimento: </b> " + usuario.getDt_Nasc();
		textoEmail += "<br> <b>CPF: </b> " + this.sessaoUsuario.getUsuario().getCPF();
		textoEmail += "<br> <b>RG: </b> " + usuario.getCadRG();
		textoEmail += "<br> <b>Emissor: </b> " + usuario.getCadOrgaoExpedidor();
		textoEmail += "<br> <b> Sexo: </b> " + usuario.getCadSexo();
		textoEmail += "<br> <b> Estado civil: </b> " + usuario.getCadEstCivil();
		textoEmail += "<br> <b> CEP: </b> " + usuario.getCadCEP();
		textoEmail += "<br> <b> Endereço: </b> " + usuario.getCadEndereco();
		textoEmail += "<br> <b> Bairro: </b> " + usuario.getCadBairro();
		textoEmail += "<br> <b> Cidade: </b> " + usuario.getCadCidade();
		textoEmail += "<br> <b> Estado: </b> " + usuario.getCadUF();
		textoEmail += "<br> <b> Telefone residencial: </b> " + usuario.getTel();
		textoEmail += "<br> <b> Telefone celular: </b> " + usuario.getCadCelular();
		textoEmail += "<br> <b> Email: </b> " + usuario.geteMail();
		textoEmail += "<br><br><br> <b> Dados bancários: </b> <br>";
		textoEmail += "<br> <b> Banco: </b> " + usuario.getCadBanco();
		textoEmail += "<br> <b> Tipo da conta: </b> " + usuario.getCadTipoConta();
		textoEmail += "<br> <b> Número da agência: </b> " + usuario.getCadAgencia();
		textoEmail += "<br> <b> Número da conta: </b> " + usuario.getCadCCorrente();

		JavaMailApp.enviarEmail("Atualização de dados de usuário", "atualizacaocadastro@alabastrum.com.br", textoEmail);

		result.include("sucesso", "Foi enviado um email para a Alabastrum solicitando a atualização dos seus dados cadastrais. Você receberá um email assim que a atualização for concluída.");

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

		ArquivoService.criarArquivoNoDisco(textoArquivo, ArquivoService.CAMINHO_PASTA_PRE_CADASTRO);

		result.redirectTo("https://alabastrum.com.br/sucesso");
	}
}
