package br.com.alabastrum.escritoriovirtual.dto;

import java.math.BigDecimal;

import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;

public class PedidoDTO {

	private Pedido pedido;
	private Franquia franquia;
	private BigDecimal valorTotal;
	private Integer totalItens;
	private Integer totalPontos;

	public PedidoDTO(Pedido pedido, Franquia franquia, BigDecimal valorTotal) {
		this.pedido = pedido;
		this.franquia = franquia;
		this.valorTotal = valorTotal;
	}

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

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Franquia getFranquia() {
		return franquia;
	}

	public void setFranquia(Franquia franquia) {
		this.franquia = franquia;
	}
}