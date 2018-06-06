package br.com.alabastrum.escritoriovirtual.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.caelum.vraptor.Resource;

@Resource
@Entity
public class Configuracao implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private String chave;
	private String valor;

	public String retornarConfiguracao(String chave) {

		HibernateUtil hibernateUtil = new HibernateUtil();

		Configuracao configuracao = new Configuracao();
		configuracao.setChave(chave);

		configuracao = hibernateUtil.selecionar(configuracao);

		hibernateUtil.fecharSessao();

		return configuracao.getValor();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}
