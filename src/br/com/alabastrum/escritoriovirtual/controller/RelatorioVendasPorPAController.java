package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.PesquisaRelatorioVendasPorPADTO;
import br.com.alabastrum.escritoriovirtual.dto.ResultadoRelatorioVendasPorPAFilhoDTO;
import br.com.alabastrum.escritoriovirtual.dto.ResultadoRelatorioVendasPorPAPaiDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.service.PedidoService;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RelatorioVendasPorPAController {

    private Result result;
    private HibernateUtil hibernateUtil;

    public RelatorioVendasPorPAController(Result result, HibernateUtil hibernateUtil) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
    }

    @Funcionalidade(administrativa = "true")
    public void acessarRelatorioVendasPorPA() {
	result.include("franquias", hibernateUtil.buscar(new Franquia()));
    }

    @Funcionalidade(administrativa = "true")
    public void pesquisarVendasPorPA(PesquisaRelatorioVendasPorPADTO pesquisaRelatorioVendasPorPADTO) throws Exception {

	if (Util.preenchido(pesquisaRelatorioVendasPorPADTO.getIdsFranquias()) && Util.preenchido(pesquisaRelatorioVendasPorPADTO.getDataInicial()) && Util.preenchido(pesquisaRelatorioVendasPorPADTO.getDataFinal())) {

	    Map<String, List<ResultadoRelatorioVendasPorPAFilhoDTO>> vendasPorPa = new HashMap<String, List<ResultadoRelatorioVendasPorPAFilhoDTO>>();

	    Calendar dataInicialGregorianCalendar = Util.getDateByString(pesquisaRelatorioVendasPorPADTO.getDataInicial());
	    Calendar dataFinalGregorianCalendar = Util.getDateByString(pesquisaRelatorioVendasPorPADTO.getDataFinal());
	    dataFinalGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

	    while (dataInicialGregorianCalendar.before(dataFinalGregorianCalendar)) {

		GregorianCalendar dataCorrente = (GregorianCalendar) dataInicialGregorianCalendar.clone();
		GregorianCalendar dataCorrenteMais1Dia = (GregorianCalendar) dataCorrente.clone();
		dataCorrenteMais1Dia.add(Calendar.DAY_OF_MONTH, 1);

		for (Entry<String, String> idFranquia : pesquisaRelatorioVendasPorPADTO.getIdsFranquias().entrySet()) {

		    Pedido pedidoFiltro = new Pedido();
		    pedidoFiltro.setCompleted(true);
		    pedidoFiltro.setStatus("FINALIZADO");

		    List<Criterion> restricoes = new ArrayList<Criterion>();

		    List<Integer> idFranquias = new ArrayList<Integer>();
		    idFranquias.add(Integer.valueOf(String.valueOf(idFranquia.getKey())));
		    restricoes.add(Restrictions.in("idFranquia", idFranquias));

		    restricoes.add(Restrictions.between("data", dataCorrente, dataCorrenteMais1Dia));

		    List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro, restricoes);

		    BigDecimal totalNoDiaENaFranquia = BigDecimal.ZERO;
		    for (Pedido pedido : pedidos) {
			totalNoDiaENaFranquia = totalNoDiaENaFranquia.add(new PedidoService(hibernateUtil).calcularTotais(pedido).getValorTotal());
		    }

		    Franquia franquia = this.hibernateUtil.selecionar(new Franquia(Integer.valueOf(String.valueOf(idFranquia.getKey()))));

		    if (vendasPorPa.get(franquia.getEstqNome()) == null) {
			vendasPorPa.put(franquia.getEstqNome(), new ArrayList<ResultadoRelatorioVendasPorPAFilhoDTO>());
		    }
		    ResultadoRelatorioVendasPorPAFilhoDTO resultadoRelatorioVendasPorPADTO = new ResultadoRelatorioVendasPorPAFilhoDTO();
		    resultadoRelatorioVendasPorPADTO.setAno(dataCorrente.get(Calendar.YEAR));
		    resultadoRelatorioVendasPorPADTO.setMes(dataCorrente.get(Calendar.MONTH));
		    resultadoRelatorioVendasPorPADTO.setDia(dataCorrente.get(Calendar.DAY_OF_MONTH));
		    resultadoRelatorioVendasPorPADTO.setValor(totalNoDiaENaFranquia.intValue());
		    vendasPorPa.get(franquia.getEstqNome()).add(resultadoRelatorioVendasPorPADTO);
		}

		dataInicialGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);
	    }

	    List<ResultadoRelatorioVendasPorPAPaiDTO> vendasPorPaAgrupadoPorFranquia = new ArrayList<ResultadoRelatorioVendasPorPAPaiDTO>();
	    for (Entry<String, List<ResultadoRelatorioVendasPorPAFilhoDTO>> vendasPorPaEntry : vendasPorPa.entrySet()) {
		ResultadoRelatorioVendasPorPAPaiDTO resultadoRelatorioVendasPorPAPaiDTO = new ResultadoRelatorioVendasPorPAPaiDTO();
		resultadoRelatorioVendasPorPAPaiDTO.setFranquia(vendasPorPaEntry.getKey());
		resultadoRelatorioVendasPorPAPaiDTO.setFilhos(vendasPorPaEntry.getValue());
		vendasPorPaAgrupadoPorFranquia.add(resultadoRelatorioVendasPorPAPaiDTO);
	    }

	    result.include("vendasPorPa", vendasPorPaAgrupadoPorFranquia);
	    result.include("pesquisa", true);
	    result.include("pesquisaRelatorioVendasPorPADTO", pesquisaRelatorioVendasPorPADTO);
	}
    }
}
