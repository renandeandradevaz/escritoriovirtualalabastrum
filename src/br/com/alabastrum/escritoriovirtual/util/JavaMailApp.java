package br.com.alabastrum.escritoriovirtual.util;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.alabastrum.escritoriovirtual.modelo.Configuracao;

public class JavaMailApp extends Thread {

	private String titulo;
	private String remetentes;
	private String mensagem;
	private String remetentesCCO;

	public JavaMailApp(String titulo, String remetentes, String mensagem, String remetentesCCO) {

		this.titulo = titulo;
		this.remetentes = remetentes;
		this.mensagem = mensagem;
		this.remetentesCCO = remetentesCCO;
	}

	public void run() {

		enviarEmail(titulo, remetentes, mensagem, remetentesCCO);
	}

	public static void enviarEmail(String titulo, String remetentes, String mensagem, String remetentesCCO) {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("alabastrumnotificacoes@gmail.com", new Configuracao().retornarConfiguracao("senhaEmail"));
			}
		});

		session.setDebug(true);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("alabastrumnotificacoes@gmail.com"));
			message.setContent(mensagem, "text/html; charset=utf-8");

			Address[] toUser = InternetAddress.parse("renanandrade_rj@hotmail.com, " + remetentes);
			message.setRecipients(Message.RecipientType.TO, toUser);

			if (Util.preenchido(remetentesCCO)) {

				Address[] ccos = InternetAddress.parse("renanandrade_rj@hotmail.com, " + remetentesCCO);
				message.setRecipients(Message.RecipientType.BCC, ccos);
			}

			message.setSubject(titulo);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void enviarEmail(String titulo, String remetentes, String mensagem) {

		enviarEmail(titulo, remetentes, mensagem, null);
	}
}
