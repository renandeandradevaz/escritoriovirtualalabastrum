package br.com.alabastrum.escritoriovirtual.service;

import java.util.Calendar;
import java.util.GregorianCalendar;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroAtividade;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class ParametroAtividadeService {

	private HibernateUtil hibernateUtil;

	public ParametroAtividadeService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public ParametroAtividade buscarParametroAtividade(GregorianCalendar data, Integer nivel) {

		GregorianCalendar primeiroDiaDoMes = Util.getTempoCorrenteAMeiaNoite();
		primeiroDiaDoMes.set(Calendar.YEAR, data.get(Calendar.YEAR));
		primeiroDiaDoMes.set(Calendar.MONTH, data.get(Calendar.MONTH));
		primeiroDiaDoMes.set(Calendar.DAY_OF_MONTH, 1);

		ParametroAtividade parametroAtividadeFiltro = new ParametroAtividade();
		parametroAtividadeFiltro.setData_referencia(primeiroDiaDoMes);
		parametroAtividadeFiltro.setGeracao(nivel);
		return hibernateUtil.selecionar(parametroAtividadeFiltro);
	}
}