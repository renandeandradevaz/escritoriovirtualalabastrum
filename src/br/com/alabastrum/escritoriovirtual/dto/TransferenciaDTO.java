package br.com.alabastrum.escritoriovirtual.dto;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class TransferenciaDTO {

    private GregorianCalendar data;
    private BigDecimal valor;
    private String tipo;
    private String descricao;
    private Usuario distribuidor;
    private Usuario usuarioResponsavelPelaTransferencia;

    public GregorianCalendar getData() {
	return data;
    }

    public void setData(GregorianCalendar data) {
	this.data = data;
    }

    public BigDecimal getValor() {
	return valor;
    }

    public void setValor(BigDecimal valor) {
	this.valor = valor;
    }

    public String getTipo() {
	return tipo;
    }

    public void setTipo(String tipo) {
	this.tipo = tipo;
    }

    public String getDescricao() {
	return descricao;
    }

    public void setDescricao(String descricao) {
	this.descricao = descricao;
    }

    public Usuario getDistribuidor() {
	return distribuidor;
    }

    public void setDistribuidor(Usuario distribuidor) {
	this.distribuidor = distribuidor;
    }

    public Usuario getUsuarioResponsavelPelaTransferencia() {
	return usuarioResponsavelPelaTransferencia;
    }

    public void setUsuarioResponsavelPelaTransferencia(Usuario usuarioResponsavelPelaTransferencia) {
	this.usuarioResponsavelPelaTransferencia = usuarioResponsavelPelaTransferencia;
    }
}