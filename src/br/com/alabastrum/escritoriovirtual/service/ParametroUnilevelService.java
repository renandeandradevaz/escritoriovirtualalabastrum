package br.com.alabastrum.escritoriovirtual.service;

import java.util.Calendar;
import java.util.GregorianCalendar;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroUnilevel;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class ParametroUnilevelService {

	private HibernateUtil hibernateUtil;

	public ParametroUnilevelService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public ParametroUnilevel buscarParametroUnilevel(GregorianCalendar data, String posicao, Integer nivel) {

		GregorianCalendar primeiroDiaDoMes = Util.getTempoCorrenteAMeiaNoite();
		primeiroDiaDoMes.set(Calendar.YEAR, data.get(Calendar.YEAR));
		primeiroDiaDoMes.set(Calendar.MONTH, data.get(Calendar.MONTH));
		primeiroDiaDoMes.set(Calendar.DAY_OF_MONTH, 1);

		ParametroUnilevel parametroUnilevelFiltro = new ParametroUnilevel();
		parametroUnilevelFiltro.setData(primeiroDiaDoMes);
		parametroUnilevelFiltro.setPosicao(posicao.toLowerCase());
		parametroUnilevelFiltro.setNivel(nivel);
		return hibernateUtil.selecionar(parametroUnilevelFiltro);
	}
}