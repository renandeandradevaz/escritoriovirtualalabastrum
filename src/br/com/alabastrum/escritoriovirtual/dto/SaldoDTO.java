package br.com.alabastrum.escritoriovirtual.dto;

import java.math.BigDecimal;
import java.util.List;

public class SaldoDTO {

	private BigDecimal saldoAtual;
	private List<ExtratoDTO> extratoDoMes;

	public SaldoDTO(BigDecimal saldoAtual, List<ExtratoDTO> extratoDoMes) {
		this.saldoAtual = saldoAtual;
		this.extratoDoMes = extratoDoMes;
	}

	public BigDecimal getSaldoAtual() {
		return saldoAtual;
	}

	public void setSaldoAtual(BigDecimal saldoAtual) {
		this.saldoAtual = saldoAtual;
	}

	public List<ExtratoDTO> getExtratoDoMes() {
		return extratoDoMes;
	}

	public void setExtratoDoMes(List<ExtratoDTO> extratoDoMes) {
		this.extratoDoMes = extratoDoMes;
	}
}