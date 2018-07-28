package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class ItemPedidoFranquia implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	@ManyToOne
	private PedidoFranquia pedidoFranquia;

	private String idProduto;
	private Integer quantidade;
	private BigDecimal precoUnitario;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PedidoFranquia getPedidoFranquia() {
		return pedidoFranquia;
	}

	public void setPedidoFranquia(PedidoFranquia pedidoFranquia) {
		this.pedidoFranquia = pedidoFranquia;
	}

	public String getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(String idProduto) {
		this.idProduto = idProduto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}
}