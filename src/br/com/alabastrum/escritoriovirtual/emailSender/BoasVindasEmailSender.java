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
			enviarEmail(usuario, hibernateUtil);
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

    private static void enviarEmail(Usuario usuario, HibernateUtil hibernateUtil) throws Exception {

	Usuario indicante = hibernateUtil.selecionar(new Usuario(usuario.getId_Indicante()));

	String textoEmail = "";
	textoEmail += "Olá " + usuario.getvNome();
	textoEmail += "<br><br> <b> SEJA MUITO BEM VINDO(A) À DUNASTES! </b>";
	textoEmail += "<br><br> Você nasceu para reinar!";
	textoEmail += "<br><br> O seu código de distribuidor na Dunastes é:  <b> " + usuario.getId_Codigo() + "</b>";

	textoEmail += "<br><br> O seu patrocinador é: " + indicante.getId_Codigo() + " - " + indicante.getvNome() + " - Email: " + indicante.geteMail() + " - Tel: " + indicante.getTel() + " " + indicante.getCadCelular();

	textoEmail += "<br> Primeiro Acesso ao Escritório Virtual da Dunastes: ";
	textoEmail += "<br> 01. Tenha em mãos o seu Id, e-mail e CPF (o mesmo utilizado no cadastro). ";
	textoEmail += "<br> 02. Acesse link https://ev.dunastes.com.br ";
	textoEmail += "<br> 03. Informe seu Id e a senha \"dunastes\" (sem as aspas). ";

	textoEmail += "<br> <br> Você será redirecionado(a) para uma nova tela onde fará a troca de senha por uma de sua preferência";
	textoEmail += "<br> <br> Qualquer dúvida, envie um e-mail para contato@dunastes.com.br";

	Mail.enviarEmail("Dunastes - Instruções para acesso ao Escritório Virtual", textoEmail, usuario.geteMail());
    }
}
