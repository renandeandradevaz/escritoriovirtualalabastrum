package br.com.alabastrum.escritoriovirtual.hibernate;

import org.hibernate.cfg.DefaultNamingStrategy;

public class ImprovedNamingStrategy extends DefaultNamingStrategy {

	private static final long serialVersionUID = 8696343034947504368L;

	@Override
	public String classToTableName(String arg0) {

		return arg0.toLowerCase();
	}

	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {

		return propertyName + "_" + referencedColumnName;
	}
}