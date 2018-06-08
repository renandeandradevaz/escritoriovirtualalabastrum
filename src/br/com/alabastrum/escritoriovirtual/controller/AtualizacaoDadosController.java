package br.com.alabastrum.escritoriovirtual.controller;

import java.lang.reflect.Field;

import org.hibernate.criterion.MatchMode;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.anotacoes.Public;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoAtualizacaoDados;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.JavaMailApp;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class AtualizacaoDadosController {

	private Result result;
	private SessaoAtualizacaoDados sessaoAtualizacaoDados;
	private SessaoUsuario sessaoUsuario;
	private HibernateUtil hibernateUtil;

	public AtualizacaoDadosController(Result result, SessaoAtualizacaoDados sessaoAtualizacaoDados, SessaoUsuario sessaoUsuario, HibernateUtil hibernateUtil) {

		this.result = result;
		this.sessaoAtualizacaoDados = sessaoAtualizacaoDados;
		this.sessaoUsuario = sessaoUsuario;
		this.hibernateUtil = hibernateUtil;
	}

	@Funcionalidade
	public void acessarTelaAtualizacaoDados() {

		Usuario usuario = this.hibernateUtil.selecionar(new Usuario(this.sessaoUsuario.getUsuario().getId_Codigo()), MatchMode.EXACT);

		Field[] fields = usuario.getClass().getDeclaredFields();

		for (Field field : fields) {

			try {

				field.setAccessible(true);

				Field fieldByName = this.sessaoAtualizacaoDados.getClass().getDeclaredField(field.getName());

				fieldByName.setAccessible(true);

				fieldByName.set(this.sessaoAtualizacaoDados, field.get(usuario));
			}

			catch (Exception e) {
			}
		}

		if (Util.preenchido(this.sessaoAtualizacaoDados.getDt_Nasc())) {

			this.sessaoAtualizacaoDados.setDt_Nasc(this.sessaoAtualizacaoDados.getDt_Nasc().substring(0, 10));
		}
	}

	@Public
	public void salvarPreCadastroDistribuidorPeloSite(SessaoAtualizacaoDados sessaoAtualizacaoDados) throws Exception {

		this.sessaoAtualizacaoDados = sessaoAtualizacaoDados;
		ArquivoService.criarArquivoNoDisco(montarTextoArquivo(), ArquivoService.CAMINHO_PASTA_PRE_CADASTRO);
		JavaMailApp.enviarEmail("Pré-cadastro de distribuidor pelo site", "atendimento@alabastrum.com.br", montarTextoEmail());
		result.redirectTo("https://alabastrum.com.br/sucesso");
	}

	private String montarTextoEmail() {

		String textoEmail = "";

		textoEmail += "<br> <b>Nome: </b> " + this.sessaoAtualizacaoDados.getvNome();
		textoEmail += "<br> <b>Data de nascimento: </b> " + this.sessaoAtualizacaoDados.getDt_Nasc();
		textoEmail += "<br> <b>CPF: </b> " + this.sessaoAtualizacaoDados.getCPF();
		textoEmail += "<br> <b>RG: </b> " + this.sessaoAtualizacaoDados.getCadRG();
		textoEmail += "<br> <b>Emissor: </b> " + this.sessaoAtualizacaoDados.getCadOrgaoExpedidor();
		textoEmail += "<br> <b> Sexo: </b> " + this.sessaoAtualizacaoDados.getCadSexo();
		textoEmail += "<br> <b> Estado civil: </b> " + this.sessaoAtualizacaoDados.getCadEstCivil();
		textoEmail += "<br> <b> CEP: </b> " + this.sessaoAtualizacaoDados.getCadCEP();
		textoEmail += "<br> <b> Endereço: </b> " + this.sessaoAtualizacaoDados.getCadEndereco();
		textoEmail += "<br> <b> Bairro: </b> " + this.sessaoAtualizacaoDados.getCadBairro();
		textoEmail += "<br> <b> Cidade: </b> " + this.sessaoAtualizacaoDados.getCadCidade();
		textoEmail += "<br> <b> Estado: </b> " + this.sessaoAtualizacaoDados.getCadUF();
		textoEmail += "<br> <b> Telefone residencial: </b> " + this.sessaoAtualizacaoDados.getTel();
		textoEmail += "<br> <b> Telefone celular: </b> " + this.sessaoAtualizacaoDados.getCadCelular();
		textoEmail += "<br> <b> Email: </b> " + this.sessaoAtualizacaoDados.geteMail();
		textoEmail += "<br><br><br> <b> Dados bancários: </b> <br>";
		textoEmail += "<br> <b> Banco: </b> " + this.sessaoAtualizacaoDados.getCadBanco();
		textoEmail += "<br> <b> Tipo da conta: </b> " + this.sessaoAtualizacaoDados.getCadTipoConta();
		textoEmail += "<br> <b> Número da agência: </b> " + this.sessaoAtualizacaoDados.getCadAgencia();
		textoEmail += "<br> <b> Número da conta: </b> " + this.sessaoAtualizacaoDados.getCadCCorrente();
		textoEmail += "<br><br><br> <b> Quem te indicou? </b> <br>";
		textoEmail += "<br> <b> Código: </b> " + this.sessaoAtualizacaoDados.getCodigoQuemIndicou();
		textoEmail += "<br> <b> Nome: </b> " + this.sessaoAtualizacaoDados.getNomeQuemIndicou();
		textoEmail += "<br><br><br> <b> Apelido: </b> " + this.sessaoAtualizacaoDados.getApelido();

		return textoEmail;
	}

	private String montarTextoArquivo() {

		String textoArquivo = "";

		textoArquivo += "Nome: \'" + this.sessaoAtualizacaoDados.getvNome() + "\'\r\n";
		textoArquivo += "Data_de_nascimento: \'" + this.sessaoAtualizacaoDados.getDt_Nasc() + "\'\r\n";
		textoArquivo += "CPF: \'" + this.sessaoAtualizacaoDados.getCPF() + "\'\r\n";
		textoArquivo += "RG: \'" + this.sessaoAtualizacaoDados.getCadRG() + "\'\r\n";
		textoArquivo += "Emissor: \'" + this.sessaoAtualizacaoDados.getCadOrgaoExpedidor() + "\'\r\n";
		textoArquivo += "Sexo: \'" + this.sessaoAtualizacaoDados.getCadSexo() + "\'\r\n";
		textoArquivo += "Estado_civil: \'" + this.sessaoAtualizacaoDados.getCadEstCivil() + "\'\r\n";
		textoArquivo += "CEP: \'" + this.sessaoAtualizacaoDados.getCadCEP() + "\'\r\n";
		textoArquivo += "Endereco: \'" + this.sessaoAtualizacaoDados.getCadEndereco() + "\'\r\n";
		textoArquivo += "Bairro: \'" + this.sessaoAtualizacaoDados.getCadBairro() + "\'\r\n";
		textoArquivo += "Cidade: \'" + this.sessaoAtualizacaoDados.getCadCidade() + "\'\r\n";
		textoArquivo += "Estado: \'" + this.sessaoAtualizacaoDados.getCadUF() + "\'\r\n";
		textoArquivo += "Telefone_residencial: \'" + this.sessaoAtualizacaoDados.getTel() + "\'\r\n";
		textoArquivo += "Telefone_celular: \'" + this.sessaoAtualizacaoDados.getCadCelular() + "\'\r\n";
		textoArquivo += "Email: \'" + this.sessaoAtualizacaoDados.geteMail() + "\'\r\n";
		textoArquivo += "Banco: \'" + this.sessaoAtualizacaoDados.getCadBanco() + "\'\r\n";
		textoArquivo += "Tipo_da_conta: \'" + this.sessaoAtualizacaoDados.getCadTipoConta() + "\'\r\n";
		textoArquivo += "Numero_da_agencia: \'" + this.sessaoAtualizacaoDados.getCadAgencia() + "\'\r\n";
		textoArquivo += "Numero_da_conta: \'" + this.sessaoAtualizacaoDados.getCadCCorrente() + "\'\r\n";
		textoArquivo += "Codigo: \'" + this.sessaoAtualizacaoDados.getCodigoQuemIndicou() + "\'\r\n";
		textoArquivo += "nomepatroc: \'" + this.sessaoAtualizacaoDados.getNomeQuemIndicou() + "\'\r\n";
		textoArquivo += "apelido: \'" + this.sessaoAtualizacaoDados.getApelido() + "\'\r\n";

		return textoArquivo;
	}

	@Funcionalidade
	public void salvarAtualizacaoDados(SessaoAtualizacaoDados sessaoAtualizacaoDados) {

		this.sessaoAtualizacaoDados = sessaoAtualizacaoDados;

		Integer codigoUsuario = this.sessaoUsuario.getUsuario().getId_Codigo();

		String textoEmail = "O usuário com código " + codigoUsuario + " atualizou seus dados no escritório virtual<br><br>";

		textoEmail += "Informações:<br>";

		textoEmail += "<br> <b>Nome: </b> " + this.sessaoAtualizacaoDados.getvNome();
		textoEmail += "<br> <b>Data de nascimento: </b> " + this.sessaoAtualizacaoDados.getDt_Nasc();
		textoEmail += "<br> <b>CPF: </b> " + this.sessaoUsuario.getUsuario().getCPF();
		textoEmail += "<br> <b>RG: </b> " + this.sessaoAtualizacaoDados.getCadRG();
		textoEmail += "<br> <b>Emissor: </b> " + this.sessaoAtualizacaoDados.getCadOrgaoExpedidor();
		textoEmail += "<br> <b> Sexo: </b> " + this.sessaoAtualizacaoDados.getCadSexo();
		textoEmail += "<br> <b> Estado civil: </b> " + this.sessaoAtualizacaoDados.getCadEstCivil();
		textoEmail += "<br> <b> CEP: </b> " + this.sessaoAtualizacaoDados.getCadCEP();
		textoEmail += "<br> <b> Endereço: </b> " + this.sessaoAtualizacaoDados.getCadEndereco();
		textoEmail += "<br> <b> Bairro: </b> " + this.sessaoAtualizacaoDados.getCadBairro();
		textoEmail += "<br> <b> Cidade: </b> " + this.sessaoAtualizacaoDados.getCadCidade();
		textoEmail += "<br> <b> Estado: </b> " + this.sessaoAtualizacaoDados.getCadUF();
		textoEmail += "<br> <b> Telefone residencial: </b> " + this.sessaoAtualizacaoDados.getTel();
		textoEmail += "<br> <b> Telefone celular: </b> " + this.sessaoAtualizacaoDados.getCadCelular();
		textoEmail += "<br> <b> Email: </b> " + this.sessaoAtualizacaoDados.geteMail();
		textoEmail += "<br><br><br> <b> Dados bancários: </b> <br>";
		textoEmail += "<br> <b> Banco: </b> " + this.sessaoAtualizacaoDados.getCadBanco();
		textoEmail += "<br> <b> Tipo da conta: </b> " + this.sessaoAtualizacaoDados.getCadTipoConta();
		textoEmail += "<br> <b> Número da agência: </b> " + this.sessaoAtualizacaoDados.getCadAgencia();
		textoEmail += "<br> <b> Número da conta: </b> " + this.sessaoAtualizacaoDados.getCadCCorrente();

		JavaMailApp.enviarEmail("Atualização de dados de usuário", "atualizacaocadastro@alabastrum.com.br", textoEmail);

		result.include("sucesso", "Foi enviado um email para a Alabastrum solicitando a atualização dos seus dados cadastrais. Você receberá um email assim que a atualização for concluída.");

		result.redirectTo(HomeController.class).home();
	}
}
