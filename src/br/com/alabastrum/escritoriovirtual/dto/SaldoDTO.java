package br.com.alabastrum.escritoriovirtual.dto;

import java.math.BigDecimal;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class SaldoDTO {

	private Usuario usuario;
	private BigDecimal saldoPrevistoNoMes;
	private BigDecimal saldoPrevistoTotal;
	private BigDecimal saldoLiberado;
	private BigDecimal ganhosAteHoje;
	private List<ExtratoDTO> extratoDoMes;

	public SaldoDTO(BigDecimal saldoPrevistoNoMes, BigDecimal saldoPrevistoTotal, BigDecimal saldoLiberado, BigDecimal ganhosAteHoje, List<ExtratoDTO> extratoDoMes) {
		this.saldoPrevistoNoMes = saldoPrevistoNoMes;
		this.saldoPrevistoTotal = saldoPrevistoTotal;
		this.saldoLiberado = saldoLiberado;
		this.ganhosAteHoje = ganhosAteHoje;
		this.extratoDoMes = extratoDoMes;
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
}