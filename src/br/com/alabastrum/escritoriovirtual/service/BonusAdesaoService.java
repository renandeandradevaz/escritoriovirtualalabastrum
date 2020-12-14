package br.com.alabastrum.escritoriovirtual.service;

import java.util.GregorianCalendar;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.BonusAdesao;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class BonusAdesaoService {

    private HibernateUtil hibernateUtil;

    public BonusAdesaoService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public BonusAdesao buscarBonusAdesao(GregorianCalendar data, Integer geracao, String kit) {

	if (Util.preenchido(kit)) {

	    BonusAdesao bonusAdesaoFiltro = new BonusAdesao();
	    bonusAdesaoFiltro.setKit(kit);
	    bonusAdesaoFiltro.setData_referencia(Util.getPrimeiroDiaDoMes(data));
	    bonusAdesaoFiltro.setGeracao(geracao);
	    return hibernateUtil.selecionar(bonusAdesaoFiltro);
	}

	return null;
    }
}