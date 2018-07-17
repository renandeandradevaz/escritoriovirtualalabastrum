package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.dto.SaldoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class ExtratoService {

	private HibernateUtil hibernateUtil;

	public ExtratoService(HibernateUtil hibernateUtil) {
		this.hibernateUtil = hibernateUtil;
	}

	public SaldoDTO gerarSaldoEExtrato(Integer idCodigo, Integer mes, Integer ano) {

		List<ExtratoDTO> extratoCompleto = new ArrayList<ExtratoDTO>();
		extratoCompleto.addAll(new IndicacaoDiretaService(hibernateUtil).obterIndicacoesDiretas(idCodigo));
		extratoCompleto.addAll(new IndicacaoIndiretaService(hibernateUtil).obterIndicacoesIndiretas(idCodigo));
		extratoCompleto.addAll(new BonusAtivacaoService(hibernateUtil).obterBonificacoesPorAtivacao(idCodigo));
		extratoCompleto.addAll(new BonusUnilevelService(hibernateUtil).obterBonificacoesUnilevel(idCodigo));
		extratoCompleto.addAll(new BonusDivisaoLucroService(hibernateUtil).obterBonificacoesDivisaoLucro(idCodigo));
		extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasDeOutroDistribuidor(idCodigo));
		extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasParaOutroDistribuidor(idCodigo));
		extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasParaAlabastrumCard(idCodigo));
		extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasParaPagamentoDePedido(idCodigo));
		extratoCompleto = ordenarExtratoPorDataCrescente(extratoCompleto);

		int mesAtual = Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH);
		int anoAtual = Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR);

		BigDecimal saldoPrevisto = BigDecimal.ZERO;
		BigDecimal saldoLiberado = BigDecimal.ZERO;
		BigDecimal ganhosAteHoje = BigDecimal.ZERO;

		List<ExtratoDTO> extratoDoMes = new ArrayList<ExtratoDTO>();
		for (ExtratoDTO extratoDTO : extratoCompleto) {

			if (extratoDTO.getValor().compareTo(BigDecimal.ZERO) != 0) {

				if (new AtividadeService(hibernateUtil).isAtivo(idCodigo, extratoDTO.getData())) {
					saldoLiberado = saldoLiberado.add(extratoDTO.getValor());
					adicionarNoExtratoDoMes(mes, ano, saldoLiberado, extratoDoMes, extratoDTO);

					if (!extratoDTO.getDiscriminador().contains("Transferência")) {
						ganhosAteHoje = ganhosAteHoje.add(extratoDTO.getValor());
					}

				} else {
					if (extratoDTO.getData().get(Calendar.MONTH) == mesAtual && extratoDTO.getData().get(Calendar.YEAR) == anoAtual) {
						saldoPrevisto = saldoPrevisto.add(extratoDTO.getValor());
						adicionarNoExtratoDoMes(mes, ano, saldoPrevisto, extratoDoMes, extratoDTO);
					}
				}
			}
		}

		BigDecimal saldoPrevistoFinal = saldoPrevisto.add(saldoLiberado);

		return new SaldoDTO(saldoPrevistoFinal, saldoLiberado, ganhosAteHoje, extratoDoMes);
	}

	private void adicionarNoExtratoDoMes(Integer mes, Integer ano, BigDecimal saldo, List<ExtratoDTO> extratoDoMes, ExtratoDTO extratoDTO) {

		if (extratoDTO.getData().get(Calendar.MONTH) == mes && extratoDTO.getData().get(Calendar.YEAR) == ano) {
			extratoDTO.setSaldo(saldo);
			extratoDoMes.add(extratoDTO);
		}
	}

	private List<ExtratoDTO> ordenarExtratoPorDataCrescente(List<ExtratoDTO> extrato) {

		Collections.sort(extrato, new Comparator<ExtratoDTO>() {

			public int compare(ExtratoDTO e1, ExtratoDTO e2) {

				if (e1.getData().getTimeInMillis() < e2.getData().getTimeInMillis()) {
					return -1;
				}
				return 1;
			}
		});

		return extrato;
	}
}
