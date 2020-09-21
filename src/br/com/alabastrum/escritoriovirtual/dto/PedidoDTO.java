package br.com.alabastrum.escritoriovirtual.dto;

import java.math.BigDecimal;

import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class PedidoDTO {

    private Pedido pedido;
    private Franquia franquia;
    private BigDecimal valorTotal;
    private Integer totalItens;
    private BigDecimal totalPontosPagaveis;
    private BigDecimal totalPontosQualificacao;

    private Usuario distribuidor;

    public PedidoDTO(Pedido pedido, Franquia franquia, BigDecimal valorTotal, Integer totalItens, BigDecimal totalPontosPagaveis, BigDecimal totalPontosQualificacao, Usuario distribuidor) {

	this.pedido = pedido;
	this.franquia = franquia;
	this.valorTotal = valorTotal;
	this.totalItens = totalItens;
	this.totalPontosPagaveis = totalPontosPagaveis;
	this.totalPontosQualificacao = totalPontosQualificacao;
	this.setDistribuidor(distribuidor);
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

    public Usuario getDistribuidor() {
	return distribuidor;
    }

    public void setDistribuidor(Usuario distribuidor) {
	this.distribuidor = distribuidor;
    }

    public BigDecimal getTotalPontosPagaveis() {
	return totalPontosPagaveis;
    }

    public void setTotalPontosPagaveis(BigDecimal totalPontosPagaveis) {
	this.totalPontosPagaveis = totalPontosPagaveis;
    }

    public BigDecimal getTotalPontosQualificacao() {
	return totalPontosQualificacao;
    }

    public void setTotalPontosQualificacao(BigDecimal totalPontosQualificacao) {
	this.totalPontosQualificacao = totalPontosQualificacao;
    }
}