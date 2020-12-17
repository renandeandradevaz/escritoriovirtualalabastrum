package br.com.alabastrum.escritoriovirtual.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.net.ssl.HttpsURLConnection;

import org.joda.time.DateTime;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Comprador;
import br.com.alabastrum.escritoriovirtual.modelo.Configuracao;
import br.com.alabastrum.escritoriovirtual.modelo.DadosFake;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class PagSeguroService {

    private static final String PAGSEGURO_URL = "https://ws.pagseguro.uol.com.br/v2";

    private HibernateUtil hibernateUtil;

    public PagSeguroService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

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

    public void executarTransacao(String senderHash, String creditCardToken, String pedidoId, String valorTotal, String nomeCartao, String parcelas, Usuario usuario) throws Exception {

	String valorDaParcela = new DecimalFormat("0.00").format(new BigDecimal(valorTotal).divide(new BigDecimal(parcelas))).toString().replaceAll(",", ".");

	String tokenEV = new Configuracao().retornarConfiguracao("tokenEV");
	String emailPagseguro = new Configuracao().retornarConfiguracao("emailPagseguro");
	String tokenPagseguro = new Configuracao().retornarConfiguracao("tokenPagseguro");
	String emailEToken = "email=" + emailPagseguro + "&token=" + tokenPagseguro;

	Pedido pedido = hibernateUtil.selecionar(new Pedido(Integer.valueOf(pedidoId)));

	String email = null;
	String cpf = null;
	String nome = null;

	DadosFake dadosFake = new DadosFake();

	Comprador comprador = pedido.getComprador();
	if (comprador != null) {

	    dadosFake.setId(comprador.getId());
	    email = comprador.getEmail();
	    cpf = comprador.getCpf();
	    nome = comprador.getNome();

	} else {
	    dadosFake.setId(usuario.getId_Codigo());
	    email = usuario.geteMail();
	    cpf = usuario.getCPF();
	    nome = usuario.getvNome();
	}

	dadosFake = hibernateUtil.selecionar(dadosFake);
	String telefone = dadosFake.getTelefone();
	String bairro = dadosFake.getBairro();
	String rua = dadosFake.getRua();
	String numero = dadosFake.getNumero();
	String cep = dadosFake.getCep();
	String dataNascimento = new SimpleDateFormat("dd/MM/yyyy").format(dadosFake.getDataNascimento());

	String urlParameters = "paymentMode=default&paymentMethod=creditCard&receiverEmail=" + emailPagseguro + "&currency=BRL&itemId1=" + pedidoId + "&itemDescription1=Pedido " + pedidoId + "&itemAmount1=" + valorTotal + "&itemQuantity1=1&notificationURL=https://ev.dunastes.com.br/pedido/pagseguroNotificacao?tokenEV=" + tokenEV + "&senderEmail=" + email + "&senderHash=" + senderHash + "&shippingAddressCountry=BRA&shippingType=1&creditCardToken=" + creditCardToken + "&installmentQuantity=" + parcelas + "&installmentValue=" + valorDaParcela + "&noInterestInstallmentQuantity=6&senderName=" + nome + "&senderCPF=" + cpf + "&senderAreaCode=11&senderPhone=" + telefone + "&&shippingAddressStreet=" + rua + "&shippingAddressNumber=" + numero + "&shippingAddressDistrict=" + bairro + "&shippingAddressPostalCode=" + cep + "&shippingAddressCity=Sao Paulo&shippingAddressState=SP&shippingAddressCountry=BRA&shippingType=1&creditCardHolderName=" + nomeCartao + "&creditCardHolderCPF=" + cpf
		+ "&creditCardHolderBirthDate=" + dataNascimento + "&creditCardHolderAreaCode=11&creditCardHolderPhone=" + telefone + "&billingAddressStreet=" + rua + "&billingAddressNumber=" + numero + "&billingAddressDistrict=" + bairro + "&billingAddressPostalCode=" + cep + "&billingAddressCity=Sao Paulo&billingAddressState=SP&billingAddressCountry=BRA";

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
	    throw new RuntimeException("Pagseguro retornou um código inesperado: " + con.getResponseCode() + ". NotificationCode recebido: " + notificationCode + ". Erro: " + response.toString());
	}

	return response.toString();
    }

    public String gerarBoleto(Integer pedidoId, String valorTotal, Usuario usuario, Comprador comprador) throws Exception {

	String cpf = "";
	String nome = "";

	if (comprador != null) {
	    cpf = comprador.getCpf();
	    nome = comprador.getNome();
	} else {
	    cpf = usuario.getCPF();
	    nome = usuario.getvNome();
	}

	String apiKeyPagarMe = new Configuracao().retornarConfiguracao("apiKeyPagarMe");
	String tokenEV = new Configuracao().retornarConfiguracao("tokenEV");

	URL url = new URL("https://api.pagar.me/1/transactions");
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setConnectTimeout(30000);
	conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	conn.setDoOutput(true);
	conn.setDoInput(true);
	conn.setRequestMethod("POST");

	String json = "{" //
		+ "\"amount\":" + valorTotal.replace(",", "") + "," //
		+ "\"api_key\":" + "\"" + apiKeyPagarMe + "\"" + "," //
		+ "\"postback_url\":" + "\"" + "https://ev.dunastes.com.br/pedido/pagarMeNotificacao?tokenEV=" + tokenEV + "&pedidoId=" + pedidoId + "\"" + "," //
		+ "\"payment_method\":" + "\"" + "boleto" + "\"" + "," //
		+ "\"customer\":{" + "\"type\":" + "\"" + "individual" + "\"" + "," //
		+ "\"country\":" + "\"" + "br" + "\"" + "," //
		+ "\"name\":" + "\"" + nome + "\"" + "," //
		+ "\"documents\": [{" //
		+ "\"type\": \"cpf\"," //
		+ "\"number\":" + "\"" + cpf + "\"" //
		+ " }]  }} ";

	System.out.println(json);

	OutputStream os = conn.getOutputStream();
	os.write(json.getBytes("UTF-8"));
	os.close();

	InputStream in = new BufferedInputStream(conn.getInputStream());
	String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

	in.close();

	if (!(conn.getResponseCode() == 200 || conn.getResponseCode() == 201)) {
	    throw new Exception("Pagar.me retornou um código inesperado: " + conn.getResponseCode());
	}

	conn.disconnect();

	System.out.println(result);

	return result.split("\"paymentLink\":\"")[1].split("\"")[0];
    }

    public String gerarBoletoModeloAntigo(Integer pedidoId, String valorTotal, Usuario usuario, Comprador comprador) throws Exception {

	String cpf = "";
	String nome = "";
	String email = "";

	if (comprador != null) {
	    cpf = comprador.getCpf();
	    nome = comprador.getNome();
	    email = comprador.getEmail();
	} else {
	    cpf = usuario.getCPF();
	    nome = usuario.getvNome();
	    email = usuario.geteMail();
	}

	DateTime validade = new DateTime();
	validade = validade.plusDays(7);
	String dataDeValidade = validade.toString("YYYY-MM-dd");

	String tokenEV = new Configuracao().retornarConfiguracao("tokenEV");
	String emailPagseguro = new Configuracao().retornarConfiguracao("emailPagseguro");
	String tokenPagseguro = new Configuracao().retornarConfiguracao("tokenPagseguro");
	String emailEToken = "email=" + emailPagseguro + "&token=" + tokenPagseguro;

	URL url = new URL("https://ws.pagseguro.uol.com.br/recurring-payment/boletos?" + emailEToken);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setConnectTimeout(30000);
	conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	conn.setDoOutput(true);
	conn.setDoInput(true);
	conn.setRequestMethod("POST");

	String json = "{  \n" + "	    \"notificationURL\": \"https://ev.dunastes.com.br/pedido/pagseguroNotificacao?tokenEV=" + tokenEV + "\",\n" + "	    \"reference\": \"" + pedidoId + "\",\n" + "        \"description\": \"Dunastes\",\n" + "	    \"periodicity\":\"monthly\",\n" + "	    \"firstDueDate\":\"" + dataDeValidade + "\",\n" + "	    \"numberOfPayments\":1,\n" + "	    \"amount\":\"" + valorTotal.replace(",", ".") + "\",\n" + "	    \"customer\":{  \n" + "	       \"document\":{  \n" + "	          \"type\":\"CPF\",\n" + "	          \"value\":\"" + cpf + "\"\n" + "	       },                          \n" + "	       \"name\":\"" + nome + "\",\n" + "	       \"email\":\"" + email + "\",\n" + "	       \"phone\":{                              \n" + "	          \"areaCode\":\"21\",\n" + "	          \"number\":\"900000000\"\n" + "	       }                 \n" + "	    }    \n" + "	 }";

	OutputStream os = conn.getOutputStream();
	os.write(json.getBytes("UTF-8"));
	os.close();

	InputStream in = new BufferedInputStream(conn.getInputStream());
	String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

	in.close();

	if (!(conn.getResponseCode() == 200 || conn.getResponseCode() == 201)) {
	    throw new Exception("Pagseguro retornou um código inesperado: " + conn.getResponseCode());
	}

	conn.disconnect();

	return result.split("\"paymentLink\":\"")[1].split("\"")[0];
    }
}