package br.com.alabastrum.escritoriovirtual.dto;

import java.math.BigDecimal;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class SaldoDTO {

    private Usuario usuario;
    private BigDecimal saldoLiberado;
    private List<ExtratoDTO> extratoDoMes;
    private List<ExtratoDTO> extratoCompleto;
    private BigDecimal saldoAnteriorAoMesPesquisado;
    private BigDecimal ganhosNoMesPesquisado;
    private BigDecimal inssNoMesPesquisado;
    private BigDecimal gastosNoMesPesquisado;
    private BigDecimal ganhosAteHoje;
    private BigDecimal bonusPrimeiraCompraNoMes;
    private BigDecimal bonusDeAdesaoDePontoDeApoioNoMes;
    private BigDecimal bonusLinearNoMes;
    private BigDecimal bonusTrinarioNoMes;
    private BigDecimal bonusFilaUnicaNoMes;
    private BigDecimal bonusGlobalNoMes;
    private BigDecimal bonusReconhecimentoNoMes;
    private BigDecimal bonusDesempenhoNoMes;
    private BigDecimal bonusLojaVirtualNoMes;

    public Usuario getUsuario() {
	return usuario;
    }

    public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
    }

    public BigDecimal getSaldoLiberado() {
	return saldoLiberado;
    }

    public void setSaldoLiberado(BigDecimal saldoLiberado) {
	this.saldoLiberado = saldoLiberado;
    }

    public List<ExtratoDTO> getExtratoDoMes() {
	return extratoDoMes;
    }

    public void setExtratoDoMes(List<ExtratoDTO> extratoDoMes) {
	this.extratoDoMes = extratoDoMes;
    }

    public BigDecimal getSaldoAnteriorAoMesPesquisado() {
	return saldoAnteriorAoMesPesquisado;
    }

    public void setSaldoAnteriorAoMesPesquisado(BigDecimal saldoAnteriorAoMesPesquisado) {
	this.saldoAnteriorAoMesPesquisado = saldoAnteriorAoMesPesquisado;
    }

    public BigDecimal getGanhosNoMesPesquisado() {
	return ganhosNoMesPesquisado;
    }

    public void setGanhosNoMesPesquisado(BigDecimal ganhosNoMesPesquisado) {
	this.ganhosNoMesPesquisado = ganhosNoMesPesquisado;
    }

    public BigDecimal getInssNoMesPesquisado() {
	return inssNoMesPesquisado;
    }

    public void setInssNoMesPesquisado(BigDecimal inssNoMesPesquisado) {
	this.inssNoMesPesquisado = inssNoMesPesquisado;
    }

    public BigDecimal getGastosNoMesPesquisado() {
	return gastosNoMesPesquisado;
    }

    public void setGastosNoMesPesquisado(BigDecimal gastosNoMesPesquisado) {
	this.gastosNoMesPesquisado = gastosNoMesPesquisado;
    }

    public BigDecimal getGanhosAteHoje() {
	return ganhosAteHoje;
    }

    public void setGanhosAteHoje(BigDecimal ganhosAteHoje) {
	this.ganhosAteHoje = ganhosAteHoje;
    }

    public BigDecimal getBonusPrimeiraCompraNoMes() {
	return bonusPrimeiraCompraNoMes;
    }

    public void setBonusPrimeiraCompraNoMes(BigDecimal bonusPrimeiraCompraNoMes) {
	this.bonusPrimeiraCompraNoMes = bonusPrimeiraCompraNoMes;
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

    public BigDecimal getBonusFilaUnicaNoMes() {
	return bonusFilaUnicaNoMes;
    }

    public void setBonusFilaUnicaNoMes(BigDecimal bonusFilaUnicaNoMes) {
	this.bonusFilaUnicaNoMes = bonusFilaUnicaNoMes;
    }

    public BigDecimal getBonusGlobalNoMes() {
	return bonusGlobalNoMes;
    }

    public void setBonusGlobalNoMes(BigDecimal bonusGlobalNoMes) {
	this.bonusGlobalNoMes = bonusGlobalNoMes;
    }

    public BigDecimal getBonusReconhecimentoNoMes() {
	return bonusReconhecimentoNoMes;
    }

    public void setBonusReconhecimentoNoMes(BigDecimal bonusReconhecimentoNoMes) {
	this.bonusReconhecimentoNoMes = bonusReconhecimentoNoMes;
    }

    public BigDecimal getBonusDesempenhoNoMes() {
	return bonusDesempenhoNoMes;
    }

    public void setBonusDesempenhoNoMes(BigDecimal bonusDesempenhoNoMes) {
	this.bonusDesempenhoNoMes = bonusDesempenhoNoMes;
    }

    public List<ExtratoDTO> getExtratoCompleto() {
	return extratoCompleto;
    }

    public void setExtratoCompleto(List<ExtratoDTO> extratoCompleto) {
	this.extratoCompleto = extratoCompleto;
    }

    public BigDecimal getBonusLojaVirtualNoMes() {
	return bonusLojaVirtualNoMes;
    }

    public void setBonusLojaVirtualNoMes(BigDecimal bonusLojaVirtualNoMes) {
	this.bonusLojaVirtualNoMes = bonusLojaVirtualNoMes;
    }
}