package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;

import br.com.alabastrum.escritoriovirtual.util.Constants;

public class TarifasService {

    public BigDecimal calcularInss(Integer descontaInss, BigDecimal valor) {

	if (descontaInss == null || descontaInss.equals(1)) {
	    return valor.multiply(Constants.TARIFA_INSS);
	}

	return BigDecimal.ZERO;
    }
}