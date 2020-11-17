package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.PesquisaRelatorioResultadoOperacionalDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.SolicitacaoSaque;
import br.com.alabastrum.escritoriovirtual.service.PedidoService;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RelatorioResultadoOperacionalController {

    private Result result;
    private HibernateUtil hibernateUtil;

    public RelatorioResultadoOperacionalController(Result result, HibernateUtil hibernateUtil) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
    }

    @Funcionalidade(administrativa = "true")
    public void acessarRelatorioResultadoOperacional() {
	result.include("franquias", hibernateUtil.buscar(new Franquia()));
    }

    @Funcionalidade(administrativa = "true")
    public void pesquisarResultadoOperacional(PesquisaRelatorioResultadoOperacionalDTO pesquisaRelatorioResultadoOperacionalDTO) throws Exception {

	BigDecimal faturamentoTotal = calcularFaturamentoTotal(pesquisaRelatorioResultadoOperacionalDTO, false);
	BigDecimal comprasRealizadasComSaldo = calcularFaturamentoTotal(pesquisaRelatorioResultadoOperacionalDTO, true);
	Map<String, BigDecimal> saquesRealizados = calcularSaquesRealizados(pesquisaRelatorioResultadoOperacionalDTO);

	result.include("faturamentoTotal", faturamentoTotal);
	result.include("comprasRealizadasComSaldo", comprasRealizadasComSaldo);
	result.include("saquesRealizadosValorBruto", saquesRealizados.get("saquesRealizadosValorBruto"));
	result.include("saquesRealizadosValorComDesconto", saquesRealizados.get("saquesRealizadosValorComDesconto"));
	result.include("pesquisaRelatorioResultadoOperacionalDTO", pesquisaRelatorioResultadoOperacionalDTO);
	result.include("pesquisa", true);

	result.forwardTo(this).acessarRelatorioResultadoOperacional();
    }

    private Map<String, BigDecimal> calcularSaquesRealizados(PesquisaRelatorioResultadoOperacionalDTO pesquisaRelatorioResultadoOperacionalDTO) throws Exception {

	BigDecimal saquesRealizadosValorBruto = BigDecimal.ZERO;
	BigDecimal saquesRealizadosValorComDesconto = BigDecimal.ZERO;

	SolicitacaoSaque solicitacaoSaqueFiltro = new SolicitacaoSaque();
	solicitacaoSaqueFiltro.setStatus("FINALIZADO");

	List<Criterion> restricoes = new ArrayList<Criterion>();
	if (Util.preenchido(pesquisaRelatorioResultadoOperacionalDTO.getDataInicial()) && Util.preenchido(pesquisaRelatorioResultadoOperacionalDTO.getDataFinal())) {
	    Calendar dataInicialGregorianCalendar = Util.getDateByString(pesquisaRelatorioResultadoOperacionalDTO.getDataInicial());
	    Calendar dataFinalGregorianCalendar = Util.getDateByString(pesquisaRelatorioResultadoOperacionalDTO.getDataFinal());
	    dataFinalGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

	    restricoes.add(Restrictions.between("data", dataInicialGregorianCalendar, dataFinalGregorianCalendar));
	}

	List<SolicitacaoSaque> solicitacoes = this.hibernateUtil.buscar(solicitacaoSaqueFiltro, restricoes);

	for (SolicitacaoSaque solicitacaoSaque : solicitacoes) {
	    saquesRealizadosValorBruto = saquesRealizadosValorBruto.add(solicitacaoSaque.getValorBrutoSolicitado());
	    saquesRealizadosValorComDesconto = saquesRealizadosValorComDesconto.add(solicitacaoSaque.getValorFinalComDescontos());
	}

	Map<String, BigDecimal> saquesRealizados = new HashMap<String, BigDecimal>();
	saquesRealizados.put("saquesRealizadosValorBruto", saquesRealizadosValorBruto);
	saquesRealizados.put("saquesRealizadosValorComDesconto", saquesRealizadosValorComDesconto);

	return saquesRealizados;
    }

    private BigDecimal calcularFaturamentoTotal(PesquisaRelatorioResultadoOperacionalDTO pesquisaRelatorioResultadoOperacionalDTO, boolean comprasRealizadasComSaldo) throws Exception {

	BigDecimal faturalmento = BigDecimal.ZERO;

	Pedido pedidoFiltro = new Pedido();
	pedidoFiltro.setCompleted(true);
	pedidoFiltro.setStatus("FINALIZADO");

	if (comprasRealizadasComSaldo) {
	    pedidoFiltro.setFormaDePagamento("pagarComSaldo");
	}

	List<Criterion> restricoes = new ArrayList<Criterion>();

	if (Util.preenchido(pesquisaRelatorioResultadoOperacionalDTO.getOrigem())) {

	    if (pesquisaRelatorioResultadoOperacionalDTO.getOrigem().equals("internet")) {
		pedidoFiltro.setFormaDeEntrega("receberEmCasa");
	    } else {
		List<Integer> idFranquias = new ArrayList<Integer>();
		idFranquias.add(Integer.valueOf(pesquisaRelatorioResultadoOperacionalDTO.getOrigem()));
		restricoes.add(Restrictions.in("idFranquia", idFranquias));
	    }
	}

	if (Util.preenchido(pesquisaRelatorioResultadoOperacionalDTO.getDataInicial()) && Util.preenchido(pesquisaRelatorioResultadoOperacionalDTO.getDataFinal())) {
	    Calendar dataInicialGregorianCalendar = Util.getDateByString(pesquisaRelatorioResultadoOperacionalDTO.getDataInicial());
	    Calendar dataFinalGregorianCalendar = Util.getDateByString(pesquisaRelatorioResultadoOperacionalDTO.getDataFinal());
	    dataFinalGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

	    restricoes.add(Restrictions.between("data", dataInicialGregorianCalendar, dataFinalGregorianCalendar));
	}

	List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro, restricoes);

	for (Pedido pedido : pedidos) {
	    faturalmento = faturalmento.add(new PedidoService(hibernateUtil).calcularTotais(pedido).getValorTotal());
	}

	return faturalmento;
    }
}
