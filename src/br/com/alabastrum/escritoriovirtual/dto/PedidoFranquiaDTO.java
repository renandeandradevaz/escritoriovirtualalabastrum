package br.com.alabastrum.escritoriovirtual.dto;

import java.math.BigDecimal;

import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.PedidoFranquia;

public class PedidoFranquiaDTO {

	private PedidoFranquia pedidoFranquia;
	private Franquia franquia;
	private BigDecimal valorTotal;

	public PedidoFranquiaDTO(PedidoFranquia pedidoFranquia, Franquia franquia, BigDecimal valorTotal) {

		this.pedidoFranquia = pedidoFranquia;
		this.franquia = franquia;
		this.valorTotal = valorTotal;
	}

	public PedidoFranquia getPedidoFranquia() {
		return pedidoFranquia;
	}

	public void setPedidoFranquia(PedidoFranquia pedidoFranquia) {
		this.pedidoFranquia = pedidoFranquia;
	}

	public Franquia getFranquia() {
		return franquia;
	}

	public void setFranquia(Franquia franquia) {
		this.franquia = franquia;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
}