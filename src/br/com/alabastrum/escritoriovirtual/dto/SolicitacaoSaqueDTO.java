package br.com.alabastrum.escritoriovirtual.dto;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class SolicitacaoSaqueDTO {

    private Integer id;
    private Usuario usuario;
    private Usuario usuarioAdm;
    private GregorianCalendar data;
    private String status;
    private BigDecimal valorBrutoSolicitado;
    private BigDecimal valorFinalComDescontos;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Usuario getUsuario() {
	return usuario;
    }

    public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
    }

    public BigDecimal getValorBrutoSolicitado() {
	return valorBrutoSolicitado;
    }

    public void setValorBrutoSolicitado(BigDecimal valorBrutoSolicitado) {
	this.valorBrutoSolicitado = valorBrutoSolicitado;
    }

    public BigDecimal getValorFinalComDescontos() {
	return valorFinalComDescontos;
    }

    public void setValorFinalComDescontos(BigDecimal valorFinalComDescontos) {
	this.valorFinalComDescontos = valorFinalComDescontos;
    }

    public Usuario getUsuarioAdm() {
	return usuarioAdm;
    }

    public void setUsuarioAdm(Usuario usuarioAdm) {
	this.usuarioAdm = usuarioAdm;
    }

    public GregorianCalendar getData() {
	return data;
    }

    public void setData(GregorianCalendar data) {
	this.data = data;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }
}