package br.com.alabastrum.escritoriovirtual.dto;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class ExtratoDTO {

    private Usuario usuario;
    private GregorianCalendar data;
    private BigDecimal valor;
    private String discriminador;

    public ExtratoDTO(Usuario usuario, GregorianCalendar data, BigDecimal valor, String discriminador) {
	this.usuario = usuario;
	this.data = data;
	this.valor = valor;
	this.discriminador = discriminador;
    }

    public Usuario getUsuario() {
	return usuario;
    }

    public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
    }

    public GregorianCalendar getData() {
	return data;
    }

    public void setData(GregorianCalendar data) {
	this.data = data;
    }

    public String getDiscriminador() {
	return discriminador;
    }

    public void setDiscriminador(String discriminador) {
	this.discriminador = discriminador;
    }

    public BigDecimal getValor() {
	return valor;
    }

    public void setValor(BigDecimal valor) {
	this.valor = valor;
    }
}