package br.com.alabastrum.escritoriovirtual.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class Categoria implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private Integer id_Categoria;
	private String catNome;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_Categoria() {
		return id_Categoria;
	}

	public void setId_Categoria(Integer id_Categoria) {
		this.id_Categoria = id_Categoria;
	}

	public String getCatNome() {
		return catNome;
	}

	public void setCatNome(String catNome) {
		this.catNome = catNome;
	}
}
