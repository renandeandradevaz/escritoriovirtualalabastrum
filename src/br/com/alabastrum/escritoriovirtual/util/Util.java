package br.com.alabastrum.escritoriovirtual.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Locale;
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

	public static GregorianCalendar copiaGregorianCalendar(GregorianCalendar calendario) {

		GregorianCalendar newCalendar = new GregorianCalendar();

		newCalendar.set(Calendar.HOUR_OF_DAY, calendario.get(Calendar.HOUR_OF_DAY));
		newCalendar.set(Calendar.YEAR, calendario.get(Calendar.YEAR));
		newCalendar.set(Calendar.MONTH, calendario.get(Calendar.MONTH));
		newCalendar.set(Calendar.DAY_OF_MONTH, calendario.get(Calendar.DAY_OF_MONTH));
		newCalendar.set(Calendar.MINUTE, calendario.get(Calendar.MINUTE));
		newCalendar.set(Calendar.SECOND, 0);
		newCalendar.set(Calendar.MILLISECOND, 0);

		return newCalendar;
	}

	public static String mesAbreviado(Integer mes) {

		if (mes == 1) {
			return "Jan";
		}
		if (mes == 2) {
			return "Fev";
		}
		if (mes == 3) {
			return "Mar";
		}
		if (mes == 4) {
			return "Abr";
		}
		if (mes == 5) {
			return "Mai";
		}
		if (mes == 6) {
			return "Jun";
		}
		if (mes == 7) {
			return "Jul";
		}
		if (mes == 8) {
			return "Ago";
		}
		if (mes == 9) {
			return "Set";
		}
		if (mes == 10) {
			return "Out";
		}
		if (mes == 11) {
			return "Nov";
		}
		if (mes == 12) {
			return "Dez";
		}

		return null;
	}

	public static BigDecimal converterStringParaBigDecimal(String numString) {

		numString = numString.replaceAll(",", ".");
		return new BigDecimal(numString);
	}

	public static String formatarBigDecimal(BigDecimal valor) {

		if (Util.vazio(valor)) {

			return "0";
		}

		else {

			Locale LOCAL = new Locale("pt", "BR");

			DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(LOCAL));

			return df.format(valor).replace(",00", "");
		}
	}
}
