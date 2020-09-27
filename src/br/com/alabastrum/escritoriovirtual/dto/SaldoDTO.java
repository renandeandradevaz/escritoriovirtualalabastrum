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
    private BigDecimal bonusPrimeiraCompraNoMes;
    private BigDecimal bonusDeAdesaoDePontoDeApoioNoMes;
    private BigDecimal bonusLinearNoMes;
    private BigDecimal bonusTrinarioNoMes;
    private BigDecimal bonusFilaUnicaNoMes;
    private BigDecimal bonificacoesNoMes;
    private BigDecimal inssNoMes;
    private List<ExtratoDTO> extratoDoMes;

    public Usuario getUsuario() {
	return usuario;
    }

    public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
    }

    public BigDecimal getSaldoPrevistoNoMes() {
	return saldoPrevistoNoMes;
    }

    public void setSaldoPrevistoNoMes(BigDecimal saldoPrevistoNoMes) {
	this.saldoPrevistoNoMes = saldoPrevistoNoMes;
    }

    public BigDecimal getSaldoDoMesAtual() {
	return saldoDoMesAtual;
    }

    public void setSaldoDoMesAtual(BigDecimal saldoDoMesAtual) {
	this.saldoDoMesAtual = saldoDoMesAtual;
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

    public BigDecimal getSaldoComDescontos() {
	return saldoComDescontos;
    }

    public void setSaldoComDescontos(BigDecimal saldoComDescontos) {
	this.saldoComDescontos = saldoComDescontos;
    }

    public BigDecimal getGanhosAteHoje() {
	return ganhosAteHoje;
    }

    public void setGanhosAteHoje(BigDecimal ganhosAteHoje) {
	this.ganhosAteHoje = ganhosAteHoje;
    }

    public BigDecimal getInss() {
	return inss;
    }

    public void setInss(BigDecimal inss) {
	this.inss = inss;
    }

    public BigDecimal getBonusPrimeiraCompraNoMes() {
	return bonusPrimeiraCompraNoMes;
    }

    public void setBonusPrimeiraCompraNoMes(BigDecimal bonusPrimeiraCompraNoMes) {
	this.bonusPrimeiraCompraNoMes = bonusPrimeiraCompraNoMes;
    }

    public List<ExtratoDTO> getExtratoDoMes() {
	return extratoDoMes;
    }

    public void setExtratoDoMes(List<ExtratoDTO> extratoDoMes) {
	this.extratoDoMes = extratoDoMes;
    }

    public BigDecimal getBonusDeAdesaoDePontoDeApoioNoMes() {
	return bonusDeAdesaoDePontoDeApoioNoMes;
    }

    public void setBonusDeAdesaoDePontoDeApoioNoMes(BigDecimal bonusDeAdesaoDePontoDeApoioNoMes) {
	this.bonusDeAdesaoDePontoDeApoioNoMes = bonusDeAdesaoDePontoDeApoioNoMes;
    }

    public BigDecimal getBonusLinearNoMes() {
	return bonusLinearNoMes;
    }

    public void setBonusLinearNoMes(BigDecimal bonusLinearNoMes) {
	this.bonusLinearNoMes = bonusLinearNoMes;
    }

    public BigDecimal getBonusTrinarioNoMes() {
	return bonusTrinarioNoMes;
    }

    public void setBonusTrinarioNoMes(BigDecimal bonusTrinarioNoMes) {
	this.bonusTrinarioNoMes = bonusTrinarioNoMes;
    }

    public BigDecimal getBonificacoesNoMes() {
	return bonificacoesNoMes;
    }

    public void setBonificacoesNoMes(BigDecimal bonificacoesNoMes) {
	this.bonificacoesNoMes = bonificacoesNoMes;
    }

    public BigDecimal getInssNoMes() {
	return inssNoMes;
    }

    public void setInssNoMes(BigDecimal inssNoMes) {
	this.inssNoMes = inssNoMes;
    }

    public BigDecimal getBonusFilaUnicaNoMes() {
	return bonusFilaUnicaNoMes;
    }

    public void setBonusFilaUnicaNoMes(BigDecimal bonusFilaUnicaNoMes) {
	this.bonusFilaUnicaNoMes = bonusFilaUnicaNoMes;
    }
}