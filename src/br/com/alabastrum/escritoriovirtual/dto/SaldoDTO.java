package br.com.alabastrum.escritoriovirtual.dto;

import java.math.BigDecimal;
import java.util.List;

public class SaldoDTO {

	private BigDecimal saldoPrevisto;
	private BigDecimal saldoLiberado;
	private BigDecimal ganhosAteHoje;
	private List<ExtratoDTO> extratoDoMes;

	public SaldoDTO(BigDecimal saldoPrevisto, BigDecimal saldoLiberado, BigDecimal ganhosAteHoje, List<ExtratoDTO> extratoDoMes) {
		this.saldoPrevisto = saldoPrevisto;
		this.saldoLiberado = saldoLiberado;
		this.ganhosAteHoje = ganhosAteHoje;
		this.extratoDoMes = extratoDoMes;
	}

	public BigDecimal getSaldoPrevisto() {
		return saldoPrevisto;
	}

	public void setSaldoPrevisto(BigDecimal saldoPrevisto) {
		this.saldoPrevisto = saldoPrevisto;
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

	public BigDecimal getGanhosAteHoje() {
		return ganhosAteHoje;
	}

	public void setGanhosAteHoje(BigDecimal ganhosAteHoje) {
		this.ganhosAteHoje = ganhosAteHoje;
	}
}