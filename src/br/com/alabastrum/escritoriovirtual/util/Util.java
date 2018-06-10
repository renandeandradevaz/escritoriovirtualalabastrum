package br.com.alabastrum.escritoriovirtual.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Map;

public class Util {

	public static boolean vazio(Object objeto) {

		if (objeto == null) {
			return true;
		}

		if (objeto instanceof String) {
			if (((String) objeto).trim().equals("")) {
				return true;
			} else {
				return false;
			}
		}

		if (objeto instanceof Collection) {
			if (((Collection<?>) objeto).isEmpty()) {
				return true;
			} else {
				return false;
			}
		}

		if (objeto instanceof Map) {
			if (((Map<?, ?>) objeto).isEmpty()) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	public static boolean preenchido(Object objeto) {
		return !vazio(objeto);
	}

	public static BigDecimal converterStringParaBigDecimal(String numString) {

		numString = numString.replaceAll(",", ".");
		return new BigDecimal(numString);
	}

	public static GregorianCalendar getTempoCorrenteAMeiaNoite() {

		GregorianCalendar tempoCorrenteAMeiaNoite = new GregorianCalendar();
		tempoCorrenteAMeiaNoite.set(Calendar.HOUR_OF_DAY, 0);
		tempoCorrenteAMeiaNoite.set(Calendar.MINUTE, 0);
		tempoCorrenteAMeiaNoite.set(Calendar.SECOND, 0);
		tempoCorrenteAMeiaNoite.set(Calendar.MILLISECOND, 0);
		return tempoCorrenteAMeiaNoite;
	}
}
