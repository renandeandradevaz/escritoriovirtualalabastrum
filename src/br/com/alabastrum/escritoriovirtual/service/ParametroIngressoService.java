package br.com.alabastrum.escritoriovirtual.service;

import java.util.GregorianCalendar;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroIngresso;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class ParametroIngressoService {

	private HibernateUtil hibernateUtil;

	public ParametroIngressoService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public ParametroIngresso buscarParametroIngresso(GregorianCalendar data, Integer nivel) {

		GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(data);

		ParametroIngresso parametroIngressoFiltro = new ParametroIngresso();
		parametroIngressoFiltro.setData(primeiroDiaDoMes);
		parametroIngressoFiltro.setNivel(nivel);
		return hibernateUtil.selecionar(parametroIngressoFiltro);
	}
}