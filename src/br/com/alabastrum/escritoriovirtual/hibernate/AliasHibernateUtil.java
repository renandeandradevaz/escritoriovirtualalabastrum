package br.com.alabastrum.escritoriovirtual.hibernate;

public class AliasHibernateUtil {

	private String alias1;
	private String alias2;

	public AliasHibernateUtil(String alias1, String alias2) {

		this.alias1 = alias1;
		this.alias2 = alias2;
	}

	public String getAlias1() {
		return alias1;
	}

	public void setAlias1(String alias1) {
		this.alias1 = alias1;
	}

	public String getAlias2() {
		return alias2;
	}

	public void setAlias2(String alias2) {
		this.alias2 = alias2;
	}

}
