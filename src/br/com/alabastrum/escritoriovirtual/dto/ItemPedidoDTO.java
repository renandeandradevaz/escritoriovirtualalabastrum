package br.com.alabastrum.escritoriovirtual.dto;

import java.math.BigDecimal;

import br.com.alabastrum.escritoriovirtual.modelo.Produto;

public class ItemPedidoDTO {

	private Produto produto;
	private Integer quantidade;
	private BigDecimal precoUnitario;
	private Integer quantidadeEmEstoque;

	public ItemPedidoDTO(Produto produto, Integer quantidade, BigDecimal precoUnitario, Integer quantidadeEmEstoque) {

		this.produto = produto;
		this.quantidade = quantidade;
		this.precoUnitario = precoUnitario;
		this.quantidadeEmEstoque = quantidadeEmEstoque;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
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

	public Integer getQuantidadeEmEstoque() {
		return quantidadeEmEstoque;
	}

	public void setQuantidadeEmEstoque(Integer quantidadeEmEstoque) {
		this.quantidadeEmEstoque = quantidadeEmEstoque;
	}
}