package br.com.alabastrum.escritoriovirtual.service;

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

		GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(data);

		ParametroAtividade parametroAtividadeFiltro = new ParametroAtividade();
		parametroAtividadeFiltro.setData_referencia(primeiroDiaDoMes);
		parametroAtividadeFiltro.setGeracao(nivel);
		return hibernateUtil.selecionar(parametroAtividadeFiltro);
	}
}