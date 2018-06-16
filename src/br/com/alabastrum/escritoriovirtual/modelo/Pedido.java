package br.com.alabastrum.escritoriovirtual.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class Pedido implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private Boolean completed;
	private Integer idFranquia;

	@Index(name = "index_idCodigo_pedido")
	private Integer idCodigo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public Integer getIdFranquia() {
		return idFranquia;
	}

	public void setIdFranquia(Integer idFranquia) {
		this.idFranquia = idFranquia;
	}

	public Integer getIdCodigo() {
		return idCodigo;
	}

	public void setIdCodigo(Integer idCodigo) {
		this.idCodigo = idCodigo;
	}
}