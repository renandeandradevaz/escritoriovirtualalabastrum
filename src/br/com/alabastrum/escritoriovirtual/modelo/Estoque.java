package br.com.alabastrum.escritoriovirtual.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class Estoque implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private String idProduto;
	private Integer idFranquia;
	private Integer quantidade;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(String idProduto) {
		this.idProduto = idProduto;
	}

	public Integer getIdFranquia() {
		return idFranquia;
	}

	public void setIdFranquia(Integer idFranquia) {
		this.idFranquia = idFranquia;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
}