package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Constants;

public class TarifasService {

    private HibernateUtil hibernateUtil;

    public TarifasService(HibernateUtil hibernateUtil) {
	this.hibernateUtil = hibernateUtil;
    }

    public BigDecimal calcularInss(Integer idCodigo, BigDecimal valor) {

	Usuario usuario = this.hibernateUtil.selecionar(new Usuario(idCodigo));

	if (usuario.getDescontaInss() == null || usuario.getDescontaInss().equals(1)) {
	    return valor.multiply(Constants.TARIFA_INSS);
	}

	return BigDecimal.ZERO;
    }
}