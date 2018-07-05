package br.com.alabastrum.escritoriovirtual.service;

import java.util.GregorianCalendar;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroDivisaoLucro;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class ParametroDivisaoLucroService {

	private HibernateUtil hibernateUtil;

	public ParametroDivisaoLucroService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public ParametroDivisaoLucro buscarParametroDivisaoLucro(GregorianCalendar data, String posicao) {

		GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(data);

		ParametroDivisaoLucro parametroDivisaoLucroFiltro = new ParametroDivisaoLucro();
		parametroDivisaoLucroFiltro.setData(primeiroDiaDoMes);
		parametroDivisaoLucroFiltro.setPosicao(posicao);
		return hibernateUtil.selecionar(parametroDivisaoLucroFiltro);
	}
}