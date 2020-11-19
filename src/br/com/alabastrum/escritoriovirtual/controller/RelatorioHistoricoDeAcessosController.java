package br.com.alabastrum.escritoriovirtual.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.HistoricoDeAcessoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.HistoricoAcesso;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RelatorioHistoricoDeAcessosController {

    private Result result;
    private HibernateUtil hibernateUtil;

    public RelatorioHistoricoDeAcessosController(Result result, HibernateUtil hibernateUtil) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
    }

    @Funcionalidade(administrativa = "true")
    public void acessarRelatorioHistoricoDeAcessos() {

	List<Criterion> restricoes = new ArrayList<Criterion>();

	GregorianCalendar dataInicial = new GregorianCalendar();
	dataInicial.add(Calendar.YEAR, -1);
	GregorianCalendar dataFinal = new GregorianCalendar();
	restricoes.add(Restrictions.between("dataHora", dataInicial, dataFinal));

	List<HistoricoAcesso> acessos = hibernateUtil.buscar(new HistoricoAcesso(), restricoes);

	Map<String, Integer> acessosMap = new TreeMap<String, Integer>();

	for (HistoricoAcesso acesso : acessos) {

	    Integer ano = acesso.getDataHora().get(Calendar.YEAR);
	    Integer mes = acesso.getDataHora().get(Calendar.MONTH);

	    String key = ano + "-" + mes;

	    if (acessosMap.get(key) == null)
		acessosMap.put(key, 1);
	    else
		acessosMap.put(key, acessosMap.get(key) + 1);
	}

	List<HistoricoDeAcessoDTO> datas = new ArrayList<HistoricoDeAcessoDTO>();

	for (Entry<String, Integer> acessoEntry : acessosMap.entrySet()) {

	    Integer ano = Integer.valueOf(acessoEntry.getKey().split("-")[0]);
	    Integer mes = Integer.valueOf(acessoEntry.getKey().split("-")[1]);

	    HistoricoDeAcessoDTO historicoDeAcessoDTO = new HistoricoDeAcessoDTO();
	    historicoDeAcessoDTO.setAno(ano);
	    historicoDeAcessoDTO.setMes(mes);
	    historicoDeAcessoDTO.setQuantidade(acessoEntry.getValue());

	    datas.add(historicoDeAcessoDTO);
	}

	Collections.sort(datas, new Comparator<HistoricoDeAcessoDTO>() {

	    public int compare(HistoricoDeAcessoDTO h1, HistoricoDeAcessoDTO h2) {

		GregorianCalendar data1 = new GregorianCalendar(h1.getAno(), h1.getMes(), 1);
		GregorianCalendar data2 = new GregorianCalendar(h2.getAno(), h2.getMes(), 1);

		if (data1.getTimeInMillis() < data2.getTimeInMillis())
		    return -1;

		if (data1.getTimeInMillis() > data2.getTimeInMillis())
		    return 1;

		return 0;
	    }
	});

	result.include("datas", datas);
    }
}
