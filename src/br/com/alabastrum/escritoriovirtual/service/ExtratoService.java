package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.dto.SaldoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Transferencia;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class ExtratoService {

	private HibernateUtil hibernateUtil;

	public ExtratoService(HibernateUtil hibernateUtil) {
		this.hibernateUtil = hibernateUtil;
	}

	public SaldoDTO gerarSaldoEExtrato(Integer idCodigo, Integer mes, Integer ano) {

		return gerarSaldoEExtrato(idCodigo, mes, ano, false);
	}

	public SaldoDTO gerarSaldoEExtrato(Integer idCodigo, Integer mes, Integer ano, boolean compressaoDeBonus) {

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
		extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasPorCompressaoDeBonus(idCodigo));
		extratoCompleto = ordenarExtratoPorDataCrescente(extratoCompleto);

		GregorianCalendar tempoCorrente = Util.getTempoCorrenteAMeiaNoite();

		if (compressaoDeBonus) {
			tempoCorrente.add(Calendar.MONTH, -1);
		}

		int mesAtual = tempoCorrente.get(Calendar.MONTH);
		int anoAtual = tempoCorrente.get(Calendar.YEAR);

		BigDecimal saldoPrevistoNoMes = BigDecimal.ZERO;
		BigDecimal saldoLiberado = BigDecimal.ZERO;
		BigDecimal ganhosAteHoje = BigDecimal.ZERO;

		List<ExtratoDTO> extratoDoMes = new ArrayList<ExtratoDTO>();
		for (ExtratoDTO extratoDTO : extratoCompleto) {

			if (extratoDTO.getValor().compareTo(BigDecimal.ZERO) != 0) {

				if (extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_PARA_OUTRO_DISTRIBUIDOR) //
						|| extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_PARA_ALABASTRUM_CARD) //
						|| extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_PARA_PAGAMENTO_DE_PEDIDO) //
						|| extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_POR_COMPRESSAO_DE_BONUS)) {

					saldoLiberado = saldoLiberado.add(extratoDTO.getValor());
					adicionarNoExtratoDoMes(mes, ano, saldoLiberado, extratoDoMes, extratoDTO);

				} else {

					if (new AtividadeService(hibernateUtil).isAtivo(idCodigo, extratoDTO.getData())) {

						saldoLiberado = saldoLiberado.add(extratoDTO.getValor());
						adicionarNoExtratoDoMes(mes, ano, saldoLiberado, extratoDoMes, extratoDTO);
						ganhosAteHoje = ganhosAteHoje.add(extratoDTO.getValor());
					} else {

						if (extratoDTO.getData().get(Calendar.MONTH) == mesAtual && extratoDTO.getData().get(Calendar.YEAR) == anoAtual) {

							saldoPrevistoNoMes = saldoPrevistoNoMes.add(extratoDTO.getValor());
							adicionarNoExtratoDoMes(mes, ano, saldoPrevistoNoMes, extratoDoMes, extratoDTO);
						}
					}
				}
			}
		}

		BigDecimal saldoPrevistoTotal = saldoPrevistoNoMes.add(saldoLiberado);
		return new SaldoDTO(saldoPrevistoNoMes, saldoPrevistoTotal, saldoLiberado, ganhosAteHoje, extratoDoMes);
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
