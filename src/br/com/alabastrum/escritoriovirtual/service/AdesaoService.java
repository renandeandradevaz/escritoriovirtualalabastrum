package br.com.alabastrum.escritoriovirtual.service;

import java.util.GregorianCalendar;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Adesao;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class AdesaoService {

    private HibernateUtil hibernateUtil;

    public AdesaoService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public Adesao buscarAdesao(GregorianCalendar data, Integer geracao) {

	Adesao adesaoFiltro = new Adesao();
	adesaoFiltro.setData_referencia(Util.getPrimeiroDiaDoMes(data));
	adesaoFiltro.setGeracao(geracao);
	return hibernateUtil.selecionar(adesaoFiltro);
    }
}