package br.com.alabastrum.escritoriovirtual.service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.google.gson.Gson;

import br.com.alabastrum.escritoriovirtual.dto.FreteResponseDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Caixa;
import br.com.alabastrum.escritoriovirtual.modelo.Configuracao;
import br.com.alabastrum.escritoriovirtual.modelo.ItemPedido;
import br.com.alabastrum.escritoriovirtual.modelo.Produto;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class FreteService {

    private HibernateUtil hibernateUtil;

    public FreteService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public List<FreteResponseDTO> buscarOpcoesDeFrete(String cepOrigem, String cepDestino, List<ItemPedido> itensPedido) throws Exception {

	BigDecimal tamanhoTotal = BigDecimal.ZERO;
	BigDecimal pesoProdutos = BigDecimal.ZERO;

	for (ItemPedido itemPedido : itensPedido) {
	    Produto produto = hibernateUtil.selecionar(new Produto(itemPedido.getIdProduto()), MatchMode.EXACT);
	    if (!produto.getId_Produtos().equals(PedidoService.PRODUTO_FRETE_ID) && !produto.getId_Produtos().equals(PedidoService.PRODUTO_TAXA_ADESAO_ID)) {
		tamanhoTotal = tamanhoTotal.add(produto.getPrdTamanho().multiply(new BigDecimal(itemPedido.getQuantidade())));
		pesoProdutos = pesoProdutos.add(produto.getPrdPeso().multiply(new BigDecimal(itemPedido.getQuantidade())));
	    }
	}

	Integer tamanhoTotalInteiro = tamanhoTotal.intValue();

	Integer quantidadeDeCaixas = 1;

	Caixa caixaEscolhida = null;
	Integer capacidadeEscolhida = Integer.MAX_VALUE;
	List<Caixa> caixas = this.hibernateUtil.buscar(new Caixa());
	for (Caixa caixa : caixas) {
	    if (caixa.getCapacidadeCaixa() >= tamanhoTotalInteiro && caixa.getCapacidadeCaixa() <= capacidadeEscolhida) {
		capacidadeEscolhida = caixa.getCapacidadeCaixa();
		caixaEscolhida = caixa;
	    }
	}

	if (caixaEscolhida == null) {
	    Integer tamanhoMaiorCaixa = Integer.MIN_VALUE;
	    for (Caixa caixa : caixas) {
		if (caixa.getCapacidadeCaixa() > tamanhoMaiorCaixa) {
		    tamanhoMaiorCaixa = caixa.getCapacidadeCaixa();
		    caixaEscolhida = caixa;
		}
	    }

	    quantidadeDeCaixas = (tamanhoTotalInteiro / tamanhoMaiorCaixa) + 1;
	    pesoProdutos = pesoProdutos.divide(new BigDecimal(quantidadeDeCaixas), 2, RoundingMode.HALF_UP);
	}

	FreteResponseDTO[] opcoesDeFrete = buscarOpcoesDeFreteAPIMelhorEnvio(cepOrigem, cepDestino, caixaEscolhida, pesoProdutos);

	List<FreteResponseDTO> opcoesValidasDeFrete = new ArrayList<FreteResponseDTO>();

	for (FreteResponseDTO freteResponseDTO : opcoesDeFrete) {

	    if (Util.vazio(freteResponseDTO.getError())) {
		freteResponseDTO.setPrice(new BigDecimal(freteResponseDTO.getPrice()).multiply(new BigDecimal(quantidadeDeCaixas)).toString());
		opcoesValidasDeFrete.add(freteResponseDTO);
	    }
	}

	return opcoesValidasDeFrete;
    }

    private FreteResponseDTO[] buscarOpcoesDeFreteAPIMelhorEnvio(String cepOrigem, String cepDestino, Caixa caixa, BigDecimal pesoProdutos) throws Exception {

	BigDecimal peso = pesoProdutos.add(BigDecimal.valueOf(caixa.getPesoCaixa())).divide(new BigDecimal(1000));
	Integer largura = caixa.getLarguraCaixa();
	Integer altura = caixa.getAlturaCaixa();
	Integer comprimento = caixa.getComprimentoCaixa();

	String tokenMelhorEnvio = new Configuracao().retornarConfiguracao("tokenMelhorEnvio");

	URL url = new URL("https://www.melhorenvio.com.br/api/v2/me/shipment/calculate");
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setConnectTimeout(30000);
	conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	conn.setRequestProperty("Authorization", "Bearer " + tokenMelhorEnvio);
	conn.setDoOutput(true);
	conn.setDoInput(true);
	conn.setRequestMethod("POST");

	String json = "{\n" + //
		"    \"from\": {\n" + //
		"      \"postal_code\": \"" + cepOrigem.replaceAll(" ", "").replace("-", "") + "\"\n" + //
		"    },\n" + //
		"    \"to\": {\n" + //
		"      \"postal_code\": \"" + cepDestino.replaceAll(" ", "").replace("-", "") + "\"\n" + //
		"    },\n" + //
		"    \"package\": {\n" + //
		"      \"weight\":" + peso + ",\n" + //
		"      \"width\": " + largura + ",\n" + //
		"      \"height\": " + altura + ",\n" + //
		"      \"length\": " + comprimento + "\n" + //
		"    }\n" + //
		"  }";

	OutputStream os = conn.getOutputStream();
	os.write(json.getBytes("UTF-8"));
	os.close();

	InputStream in = new BufferedInputStream(conn.getInputStream());
	String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

	in.close();

	if (!(conn.getResponseCode() == 200)) {
	    throw new Exception("Melhor envio API retornou um código inesperado: " + conn.getResponseCode());
	}

	conn.disconnect();

	return new Gson().fromJson(result, FreteResponseDTO[].class);
    }
}