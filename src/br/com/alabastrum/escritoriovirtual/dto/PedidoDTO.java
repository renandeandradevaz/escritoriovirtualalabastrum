package br.com.alabastrum.escritoriovirtual.dto;

import java.math.BigDecimal;

public class PedidoDTO {

	private BigDecimal valorTotal;
	private Integer totalItens;
	private Integer totalPontos;

	public PedidoDTO(BigDecimal valorTotal, Integer totalItens, Integer totalPontos) {
		this.valorTotal = valorTotal;
		this.totalItens = totalItens;
		this.totalPontos = totalPontos;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Integer getTotalItens() {
		return totalItens;
	}

	public void setTotalItens(Integer totalItens) {
		this.totalItens = totalItens;
	}

	public Integer getTotalPontos() {
		return totalPontos;
	}

	public void setTotalPontos(Integer totalPontos) {
		this.totalPontos = totalPontos;
	}
}