package br.com.alabastrum.escritoriovirtual.emailSender;

import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.BoasVindas;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class BoasVindasEmailSender {

	public static void enviarEmail() throws Exception {

		HibernateUtil hibernateUtil = new HibernateUtil();

		GregorianCalendar hoje = Util.getTempoCorrenteAMeiaNoite();

		Qualificacao qualificacaoFiltro = new Qualificacao();
		qualificacaoFiltro.setData(hoje);
		List<Qualificacao> qualificacoes = hibernateUtil.buscar(qualificacaoFiltro);

		for (Qualificacao qualificacao : qualificacoes) {

			Integer id_Codigo = qualificacao.getId_Codigo();

			qualificacaoFiltro = new Qualificacao();
			qualificacaoFiltro.setId_Codigo(id_Codigo);
			List<Qualificacao> qualificacoesDoUsuario = hibernateUtil.buscar(qualificacaoFiltro);

			if (qualificacoesDoUsuario.size() == 1) {

				Usuario usuario = hibernateUtil.selecionar(new Usuario(id_Codigo));

				if (Util.preenchido(usuario.geteMail())) {

					BoasVindas boasVindas = new BoasVindas();
					boasVindas.setIdCodigo(usuario.getId_Codigo());
					boasVindas.setData(hoje);
					boasVindas = hibernateUtil.selecionar(boasVindas);

					if (boasVindas == null) {
						enviarEmail(usuario);
						salvarBoasVindas(hibernateUtil, hoje, usuario);
						apagarSenha(hibernateUtil, usuario);
					}
				}
			}
		}

		limparAntigas(hibernateUtil, hoje);

		hibernateUtil.fecharSessao();
	}

	private static void limparAntigas(HibernateUtil hibernateUtil, GregorianCalendar hoje) {

		List<BoasVindas> boasVindasList = hibernateUtil.buscar(new BoasVindas());

		for (BoasVindas boasVindas : boasVindasList) {
			if (boasVindas.getData().before(hoje)) {
				hibernateUtil.deletar(boasVindas);
			}
		}
	}

	private static void apagarSenha(HibernateUtil hibernateUtil, Usuario usuario) {
		hibernateUtil.executarSQL("delete from informacoesfixasusuario where codigoUsuario = " + usuario.getId_Codigo());
	}

	private static void salvarBoasVindas(HibernateUtil hibernateUtil, GregorianCalendar hoje, Usuario usuario) {

		BoasVindas boasVindas;
		boasVindas = new BoasVindas();
		boasVindas.setIdCodigo(usuario.getId_Codigo());
		boasVindas.setData(hoje);
		hibernateUtil.salvarOuAtualizar(boasVindas);
	}

	private static void enviarEmail(Usuario usuario) throws Exception {

		String textoEmail = "";
		textoEmail += "Olá " + usuario.getvNome();
		textoEmail += "<br> O seu código de distribuidor na Alabastrum é:  <b> " + usuario.getId_Codigo() + "</b>";
		textoEmail += "<br> Você já pode acessar o Escritório Virtual da Alabastrum ";
		textoEmail += "<br> Você só precisa acessar o seguinte link: <a href='http://escritoriovirtual.alabastrum.com.br' > http://escritoriovirtual.alabastrum.com.br </a> ";
		textoEmail += "<br> Deverá ter em mãos o seu código, email e cpf (o mesmo utilizado no cadastro).  ";
		textoEmail += "<br> A primeira vez que acessar o sistema. Deverá utilizar o seu código e a senha <b> alabastrum </b> ";
		textoEmail += "<br> Você será redirecionado para uma nova tela para trocar a sua senha. ";
		textoEmail += "<br> Qualquer dúvida, envie um e-mail para atendimento@alabastrum.com.br ";
		Mail.enviarEmail("Alabastrum - Instruções para acesso ao Escritório Virtual", textoEmail, usuario.geteMail());
	}
}
