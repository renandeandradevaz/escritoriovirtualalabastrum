package br.com.alabastrum.escritoriovirtual.service;

import java.util.GregorianCalendar;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ReceitaDivisaoLucro;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class ReceitaDivisaoLucroService {

	private HibernateUtil hibernateUtil;

	public ReceitaDivisaoLucroService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public ReceitaDivisaoLucro buscarReceitaDivisaoLucro(GregorianCalendar data) {

		GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(data);

		ReceitaDivisaoLucro receitaDivisaoLucroFiltro = new ReceitaDivisaoLucro();
		receitaDivisaoLucroFiltro.setData(primeiroDiaDoMes);
		return hibernateUtil.selecionar(receitaDivisaoLucroFiltro);
	}
}