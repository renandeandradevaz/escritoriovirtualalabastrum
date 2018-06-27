package br.com.alabastrum.escritoriovirtual.service;

import java.util.Calendar;
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

		GregorianCalendar primeiroDiaDoMes = Util.getTempoCorrenteAMeiaNoite();
		primeiroDiaDoMes.set(Calendar.YEAR, data.get(Calendar.YEAR));
		primeiroDiaDoMes.set(Calendar.MONTH, data.get(Calendar.MONTH));
		primeiroDiaDoMes.set(Calendar.DAY_OF_MONTH, 1);

		ParametroIngresso parametroIngressoFiltro = new ParametroIngresso();
		parametroIngressoFiltro.setData(primeiroDiaDoMes);
		parametroIngressoFiltro.setNivel(nivel);
		return hibernateUtil.selecionar(parametroIngressoFiltro);
	}
}