package br.com.alabastrum.escritoriovirtual.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
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

    public static GregorianCalendar getPrimeiroDiaDoMes(GregorianCalendar data) {

	GregorianCalendar primeiroDiaDoMes = getTempoCorrenteAMeiaNoite();
	primeiroDiaDoMes.set(Calendar.YEAR, data.get(Calendar.YEAR));
	primeiroDiaDoMes.set(Calendar.MONTH, data.get(Calendar.MONTH));
	primeiroDiaDoMes.set(Calendar.DAY_OF_MONTH, 1);
	return primeiroDiaDoMes;
    }

    public static GregorianCalendar getUltimoDiaDoMes(GregorianCalendar data) {

	GregorianCalendar ultimoDiaDoMes = Util.getTempoCorrenteAMeiaNoite();
	ultimoDiaDoMes.set(Calendar.YEAR, data.get(Calendar.YEAR));
	ultimoDiaDoMes.set(Calendar.MONTH, data.get(Calendar.MONTH));
	ultimoDiaDoMes.set(Calendar.DAY_OF_MONTH, ultimoDiaDoMes.getActualMaximum(Calendar.DAY_OF_MONTH));
	return ultimoDiaDoMes;
    }

    public static String getMesString(Integer mes) {

	HashMap<Integer, String> meses = new HashMap<Integer, String>();

	meses.put(0, "Jan");
	meses.put(1, "Fev");
	meses.put(2, "Mar");
	meses.put(3, "Abr");
	meses.put(4, "Mai");
	meses.put(5, "Jun");
	meses.put(6, "Jul");
	meses.put(7, "Ago");
	meses.put(8, "Set");
	meses.put(9, "Out");
	meses.put(10, "Nov");
	meses.put(11, "Dez");

	return meses.get(mes);
    }

    public static String getExceptionMessage(Exception e) {

	StringWriter stringWriter = new StringWriter();
	e.printStackTrace(new PrintWriter(stringWriter));
	return stringWriter.toString();
    }

    public static boolean isValidCPF(String CPF) {

	CPF = CPF.replaceAll(" ", "").replaceAll("\\.", "").replaceAll("-", "");

	if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222") || CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555") || CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888") || CPF.equals("99999999999") || (CPF.length() != 11))
	    return (false);

	char dig10, dig11;
	int sm, i, r, num, peso;

	try {

	    sm = 0;
	    peso = 10;
	    for (i = 0; i < 9; i++) {

		num = (int) (CPF.charAt(i) - 48);
		sm = sm + (num * peso);
		peso = peso - 1;
	    }

	    r = 11 - (sm % 11);
	    if ((r == 10) || (r == 11))
		dig10 = '0';
	    else
		dig10 = (char) (r + 48);

	    sm = 0;
	    peso = 11;
	    for (i = 0; i < 10; i++) {
		num = (int) (CPF.charAt(i) - 48);
		sm = sm + (num * peso);
		peso = peso - 1;
	    }

	    r = 11 - (sm % 11);
	    if ((r == 10) || (r == 11))
		dig11 = '0';
	    else
		dig11 = (char) (r + 48);

	    if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
		return (true);
	    else
		return (false);
	} catch (Exception erro) {
	    return (false);
	}
    }
}
