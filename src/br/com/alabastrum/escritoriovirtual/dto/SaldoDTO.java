package br.com.alabastrum.escritoriovirtual.dto;

import java.math.BigDecimal;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class SaldoDTO {

    private Usuario usuario;
    private BigDecimal saldoPrevistoNoMes;
    private BigDecimal saldoDoMesAtual;
    private BigDecimal saldoPrevistoTotal;
    private BigDecimal saldoLiberado;
    private BigDecimal saldoComDescontos;
    private BigDecimal ganhosAteHoje;
    private BigDecimal inss;
    private List<ExtratoDTO> extratoDoMes;

    public SaldoDTO(BigDecimal saldoPrevistoNoMes, BigDecimal saldoPrevistoTotal, BigDecimal saldoLiberado, BigDecimal ganhosAteHoje, BigDecimal inss, BigDecimal saldoDoMesAtual, BigDecimal saldoComDescontos, List<ExtratoDTO> extratoDoMes) {
	this.saldoPrevistoNoMes = saldoPrevistoNoMes;
	this.saldoPrevistoTotal = saldoPrevistoTotal;
	this.saldoLiberado = saldoLiberado;
	this.ganhosAteHoje = ganhosAteHoje;
	this.extratoDoMes = extratoDoMes;
	this.inss = inss;
	this.saldoDoMesAtual = saldoDoMesAtual;
	this.saldoComDescontos = saldoComDescontos;
    }

    public BigDecimal getSaldoPrevistoNoMes() {
	return saldoPrevistoNoMes;
    }

    public void setSaldoPrevistoNoMes(BigDecimal saldoPrevistoNoMes) {
	this.saldoPrevistoNoMes = saldoPrevistoNoMes;
    }

    public BigDecimal getSaldoPrevistoTotal() {
	return saldoPrevistoTotal;
    }

    public void setSaldoPrevistoTotal(BigDecimal saldoPrevistoTotal) {
	this.saldoPrevistoTotal = saldoPrevistoTotal;
    }

    public BigDecimal getSaldoLiberado() {
	return saldoLiberado;
    }

    public void setSaldoLiberado(BigDecimal saldoLiberado) {
	this.saldoLiberado = saldoLiberado;
    }

    public BigDecimal getGanhosAteHoje() {
	return ganhosAteHoje;
    }

    public void setGanhosAteHoje(BigDecimal ganhosAteHoje) {
	this.ganhosAteHoje = ganhosAteHoje;
    }

    public List<ExtratoDTO> getExtratoDoMes() {
	return extratoDoMes;
    }

    public void setExtratoDoMes(List<ExtratoDTO> extratoDoMes) {
	this.extratoDoMes = extratoDoMes;
    }

    public Usuario getUsuario() {
	return usuario;
    }

    public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
    }

    public BigDecimal getInss() {
	return inss;
    }

    public void setInss(BigDecimal inss) {
	this.inss = inss;
    }

    public BigDecimal getSaldoDoMesAtual() {
	return saldoDoMesAtual;
    }

    public void setSaldoDoMesAtual(BigDecimal saldoDoMesAtual) {
	this.saldoDoMesAtual = saldoDoMesAtual;
    }

    public BigDecimal getSaldoComDescontos() {
	return saldoComDescontos;
    }

    public void setSaldoComDescontos(BigDecimal saldoComDescontos) {
	this.saldoComDescontos = saldoComDescontos;
    }
}