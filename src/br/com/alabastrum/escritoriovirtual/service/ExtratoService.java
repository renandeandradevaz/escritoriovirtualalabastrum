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
		extratoCompleto = ordenarExtratoPorDataCrescente(extratoCompleto);

		BigDecimal saldoAtual = BigDecimal.ZERO;

		List<ExtratoDTO> extratoDoMes = new ArrayList<ExtratoDTO>();
		for (ExtratoDTO extratoDTO : extratoCompleto) {

			saldoAtual = saldoAtual.add(extratoDTO.getValor());

			if (extratoDTO.getValor().compareTo(BigDecimal.ZERO) != 0 && extratoDTO.getData().get(Calendar.MONTH) == mes && extratoDTO.getData().get(Calendar.YEAR) == ano) {
				extratoDTO.setSaldo(saldoAtual);
				extratoDoMes.add(extratoDTO);
			}
		}

		return new SaldoDTO(saldoAtual, extratoDoMes);
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
