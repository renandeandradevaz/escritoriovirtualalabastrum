package br.com.alabastrum.escritoriovirtual.service;

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

		GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(data);

		ParametroUnilevel parametroUnilevelFiltro = new ParametroUnilevel();
		parametroUnilevelFiltro.setData(primeiroDiaDoMes);
		parametroUnilevelFiltro.setPosicao(posicao);
		parametroUnilevelFiltro.setNivel(nivel);
		return hibernateUtil.selecionar(parametroUnilevelFiltro);
	}
}