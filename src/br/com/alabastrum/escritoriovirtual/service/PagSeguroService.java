package br.com.alabastrum.escritoriovirtual.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

import javax.net.ssl.HttpsURLConnection;

import br.com.alabastrum.escritoriovirtual.modelo.Configuracao;

public class PagSeguroService {

	private static final String PAGSEGURO_URL = "https://ws.pagseguro.uol.com.br/v2";

	public String gerarSessionId() throws Exception {

		String emailPagseguro = new Configuracao().retornarConfiguracao("emailPagseguro");
		String tokenPagseguro = new Configuracao().retornarConfiguracao("tokenPagseguro");
		String urlParameters = "email=" + emailPagseguro + "&token=" + tokenPagseguro;

		URL obj = new URL(PAGSEGURO_URL + "/sessions");
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString().split("<session><id>")[1].split("</id></session>")[0];
	}

	public void executarTransacao(String senderHash, String creditCardToken, String pedidoId, String valorTotal, String nome, String parcelas) throws Exception {

		String valorDaParcela = new DecimalFormat("0.00").format(new BigDecimal(valorTotal).divide(new BigDecimal(parcelas))).toString().replaceAll(",", ".");

		String emailPagseguro = new Configuracao().retornarConfiguracao("emailPagseguro");
		String tokenPagseguro = new Configuracao().retornarConfiguracao("tokenPagseguro");
		String emailEToken = "email=" + emailPagseguro + "&token=" + tokenPagseguro;

		String senderEmail = new Configuracao().retornarConfiguracao("senderEmail");
		String tokenEV = new Configuracao().retornarConfiguracao("tokenEV");

		String urlParameters = "paymentMode=default&paymentMethod=creditCard&receiverEmail=" + emailPagseguro + "&currency=BRL&itemId1=" + pedidoId + "&itemDescription1=Pedido " + pedidoId + "&itemAmount1=" + valorTotal + "&itemQuantity1=1&notificationURL=https://escritoriovirtual.alabastrum.com.br/pedido/pagseguroNotificacao?tokenEV=" + tokenEV + "&senderEmail=" + senderEmail + "&senderHash=" + senderHash + "&shippingAddressCountry=BRA&shippingType=1&creditCardToken=" + creditCardToken + "&installmentQuantity=" + parcelas + "&installmentValue=" + valorDaParcela + "&noInterestInstallmentQuantity=6&senderName=Distribuidor%20Alabastrum&senderCPF=01234567890&senderAreaCode=21&senderPhone=56273440&shippingAddressStreet=Teste&shippingAddressNumber=1234&shippingAddressDistrict=Teste&shippingAddressPostalCode=01452002&shippingAddressCity=RioDeJaneiro&shippingAddressState=RJ&shippingAddressCountry=BRA&shippingType=1&creditCardHolderName=" + nome + "&creditCardHolderCPF=01234567890&creditCardHolderBirthDate=27%2F10%2F1987&creditCardHolderAreaCode=21&creditCardHolderPhone=56273440&billingAddressStreet=Teste&billingAddressNumber=1234&billingAddressComplement=teste&billingAddressDistrict=teste&billingAddressPostalCode=01452002&billingAddressCity=teste&billingAddressState=RJ&billingAddressCountry=BRA";

		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;
		String request = PAGSEGURO_URL + "/transactions?" + emailEToken;
		URL url = new URL(request);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("charset", "utf-8");
		conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		conn.setUseCaches(false);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.write(postData);
		wr.flush();
		wr.close();

		InputStream inputStream = null;

		if (conn.getResponseCode() == 200) {
			inputStream = conn.getInputStream();
		} else {
			inputStream = conn.getErrorStream();
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		if (conn.getResponseCode() != 200) {
			throw new Exception("Pagseguro retornou um código inesperado: " + conn.getResponseCode() + ". Erro: " + response.toString());
		}
	}

	public String consultarTransacao(String notificationCode) throws Exception {

		String emailPagseguro = new Configuracao().retornarConfiguracao("emailPagseguro");
		String tokenPagseguro = new Configuracao().retornarConfiguracao("tokenPagseguro");
		String emailEToken = "email=" + emailPagseguro + "&token=" + tokenPagseguro;

		String url = PAGSEGURO_URL + "/transactions/notifications/" + notificationCode + "?" + emailEToken;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();

		InputStream inputStream = null;

		if (responseCode == 200) {
			inputStream = con.getInputStream();
		} else {
			inputStream = con.getErrorStream();
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		if (con.getResponseCode() != 200) {
			throw new RuntimeException("Pagseguro retornou um código inesperado: " + con.getResponseCode() + ". Erro: " + response.toString());
		}

		return response.toString();
	}
}