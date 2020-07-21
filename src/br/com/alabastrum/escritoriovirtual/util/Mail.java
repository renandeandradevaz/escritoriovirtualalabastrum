package br.com.alabastrum.escritoriovirtual.util;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.alabastrum.escritoriovirtual.modelo.Configuracao;

public class Mail {

	public static void enviarEmail(String titulo, String mensagem) throws Exception {

		enviarEmail(titulo, mensagem, "");
	}

	public static void enviarEmail(String titulo, String mensagem, String remetente) throws Exception {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("dunastes.notificacoes@gmail.com", new Configuracao().retornarConfiguracao("senhaEmail"));
			}
		});

		session.setDebug(true);

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("dunastes.notificacoes@gmail.com"));
		message.setContent(mensagem.replaceAll("\\r\\n", "<br>"), "text/html; charset=utf-8");

		Address[] toUser = InternetAddress.parse("renanandrade_rj@hotmail.com,ev@alabastrum.com.br, " + remetente);
		message.setRecipients(Message.RecipientType.TO, toUser);

		message.setSubject(titulo);

		Transport.send(message);
	}
}
