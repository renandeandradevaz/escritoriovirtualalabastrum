package br.com.alabastrum.escritoriovirtual.dto;

import br.com.alabastrum.escritoriovirtual.modelo.Produto;

public class ItemPedidoDTO {

	private Produto produto;
	private Integer quantidade;

	public ItemPedidoDTO(Produto produto, Integer quantidade) {
		this.produto = produto;
		this.quantidade = quantidade;
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
}