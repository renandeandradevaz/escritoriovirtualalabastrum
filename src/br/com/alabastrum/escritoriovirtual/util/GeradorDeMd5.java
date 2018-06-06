package br.com.alabastrum.escritoriovirtual.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class GeradorDeMd5 {

	public static String converter(String string) {

		MessageDigest m;

		try {

			m = MessageDigest.getInstance("MD5");
			m.update(string.getBytes(), 0, string.length());
			return new BigInteger(1, m.digest()).toString(16);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
