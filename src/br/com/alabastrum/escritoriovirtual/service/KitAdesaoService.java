package br.com.alabastrum.escritoriovirtual.service;

import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.Order;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.KitAdesao;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class KitAdesaoService {

    private HibernateUtil hibernateUtil;

    public KitAdesaoService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public List<KitAdesao> buscarKits(GregorianCalendar data) {

	KitAdesao kitAdesaoFiltro = new KitAdesao();
	kitAdesaoFiltro.setData_referencia(Util.getPrimeiroDiaDoMes(data));
	return hibernateUtil.buscar(kitAdesaoFiltro, Order.desc("valor"));
    }

    public KitAdesao encontrarKitPeloNome(GregorianCalendar data, String kit) {

	for (KitAdesao kitAdesao : buscarKits(data)) {
	    if (kit.equalsIgnoreCase(kitAdesao.getKit())) {
		return kitAdesao;
	    }
	}

	return null;
    }
}