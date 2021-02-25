package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.FaixaAtividade;
import br.com.alabastrum.escritoriovirtual.modelo.KitAdesao;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class AtividadeService {

    private HibernateUtil hibernateUtil;

    public AtividadeService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public boolean isAtivo(Integer codigo) {

	GregorianCalendar primeiroDiaDoMes = Util.getTempoCorrenteAMeiaNoite();
	primeiroDiaDoMes.set(Calendar.DAY_OF_MONTH, 1);

	GregorianCalendar ultimoDiaDoMes = Util.getTempoCorrenteAMeiaNoite();
	ultimoDiaDoMes.set(Calendar.DAY_OF_MONTH, ultimoDiaDoMes.getActualMaximum(Calendar.DAY_OF_MONTH));

	return isAtivoV2(codigo, primeiroDiaDoMes, ultimoDiaDoMes);
    }

    public boolean isAtivo(Integer codigo, GregorianCalendar data) {

	GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(data);
	GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(data);

	GregorianCalendar primeiroFevereiro2021 = new GregorianCalendar(2021, Calendar.FEBRUARY, 1);
	if (data.before(primeiroFevereiro2021)) {
	    return isAtivoVersaoAntiga(codigo, primeiroDiaDoMes, ultimoDiaDoMes);
	}

	return isAtivoV2(codigo, primeiroDiaDoMes, ultimoDiaDoMes);
    }

    public boolean possuiIndicadosDiretosAtivos(Integer codigo, GregorianCalendar data, Integer quantidadeDeIndicadosDiretosMinimo) {

	return possuiIndicadosDiretosAtivos(codigo, data, quantidadeDeIndicadosDiretosMinimo, new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(codigo, 1));
    }

    public boolean possuiIndicadosDiretosAtivos(Integer codigo, GregorianCalendar data, Integer quantidadeDeIndicadosDiretosMinimo, TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMap) {

	int quantidadeIndicadosAtual = 0;

	for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEntry : arvoreHierarquicaMap.entrySet()) {

	    if (arvoreHierarquicaEntry.getValue().getNivel() <= 1) {

		if (isAtivo(arvoreHierarquicaEntry.getKey(), data)) {
		    quantidadeIndicadosAtual++;
		}

		if (quantidadeIndicadosAtual >= quantidadeDeIndicadosDiretosMinimo) {
		    return true;
		}
	    }
	}

	return false;
    }

    private boolean isAtivoVersaoAntiga(Integer codigo, GregorianCalendar dataInicial, GregorianCalendar dataFinal) {

	List<Pontuacao> pontuacoes = new PontuacaoService(hibernateUtil).buscarPontuacoes(codigo, dataInicial, dataFinal);

	if (pontuacoes.size() > 0) {

	    Integer parametroAtividade = pontuacoes.get(0).getParametroAtividade().intValue();
	    Integer somaPontosAtividade = 0;

	    for (Pontuacao pontuacao : pontuacoes) {
		somaPontosAtividade += pontuacao.getPntAtividade().intValue();
	    }

	    if (somaPontosAtividade >= parametroAtividade) {
		return true;
	    }
	}

	return false;
    }

    private boolean isAtivoV2(Integer codigo, GregorianCalendar dataInicial, GregorianCalendar dataFinal) {

	FaixaAtividade faixaAtividade = encontrarFaixaAtividade(codigo, dataInicial);

	if (faixaAtividade == null) {
	    return false;
	}

	BigDecimal totalPedidosPessoais = BigDecimal.ZERO;
	for (Pedido pedido : new PedidoService(hibernateUtil).getPedidosDoDistribuidor(codigo, dataInicial, dataFinal)) {
	    totalPedidosPessoais = totalPedidosPessoais.add(new PedidoService(hibernateUtil).calcularTotalSemFrete(pedido));
	}

	BigDecimal totalPedidosAfiliados = BigDecimal.ZERO;
	Map<Integer, ArvoreHierarquicaDTO> arvoreNivel1 = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(codigo, 1);

	for (Entry<Integer, ArvoreHierarquicaDTO> arvoreEntry : arvoreNivel1.entrySet()) {
	    if (KitAdesao.AFILIADO.equalsIgnoreCase(arvoreEntry.getValue().getUsuario().getNome_kit())) {
		for (Pedido pedido : new PedidoService(hibernateUtil).getPedidosDoDistribuidor(arvoreEntry.getValue().getUsuario().getId_Codigo(), dataInicial, dataFinal)) {
		    totalPedidosAfiliados = totalPedidosAfiliados.add(new PedidoService(hibernateUtil).calcularTotalSemFrete(pedido));
		}
	    }
	}

	if (totalPedidosPessoais.add(totalPedidosAfiliados).intValue() >= faixaAtividade.getValor().intValue()) {
	    return true;
	}
	return false;
    }

    private FaixaAtividade encontrarFaixaAtividade(Integer codigo, GregorianCalendar data) {

	Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaCompleta = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(codigo, "id_lider");

	int tamanhoDaArvore = arvoreHierarquicaCompleta.size();

	FaixaAtividade faixaAtividade = new FaixaAtividade();
	faixaAtividade.setData_referencia(data);
	List<FaixaAtividade> faixasAtividade = this.hibernateUtil.buscar(faixaAtividade);

	for (FaixaAtividade fa : faixasAtividade) {
	    if (fa.getMin_pessoas().intValue() <= tamanhoDaArvore && fa.getMax_pessoas().intValue() >= tamanhoDaArvore) {
		return fa;
	    }
	}
	return null;
    }
}
