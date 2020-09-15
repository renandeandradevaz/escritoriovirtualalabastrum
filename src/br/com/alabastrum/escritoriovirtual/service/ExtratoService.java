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
import br.com.alabastrum.escritoriovirtual.util.Constants;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class ExtratoService {

    private HibernateUtil hibernateUtil;

    public ExtratoService(HibernateUtil hibernateUtil) {
	this.hibernateUtil = hibernateUtil;
    }

    public SaldoDTO gerarSaldoEExtrato(Integer idCodigo, Integer mes, Integer ano) throws Exception {

	return gerarSaldoEExtrato(idCodigo, mes, ano, false);
    }

    public SaldoDTO gerarSaldoEExtrato(Integer idCodigo, Integer mes, Integer ano, boolean compressaoDeBonus) throws Exception {

	List<ExtratoDTO> extratoCompleto = new ArrayList<ExtratoDTO>();
	extratoCompleto.addAll(new BonusDePrimeiraCompraService(hibernateUtil).obterBonificacoesDePrimeiraCompra(idCodigo));
	extratoCompleto.addAll(new BonusLinearService(hibernateUtil).obterBonificacoesLineares(idCodigo));
	extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasDeOutroDistribuidor(idCodigo));
	extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasParaOutroDistribuidor(idCodigo));
	extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasParaAlabastrumCard(idCodigo));
	extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasParaPagamentoDePedido(idCodigo));
	extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasParaSaque(idCodigo));
	extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasPorCompressaoDeBonus(idCodigo));
	extratoCompleto = ordenarExtratoPorDataCrescente(extratoCompleto);

	GregorianCalendar tempoCorrente = Util.getTempoCorrenteAMeiaNoite();

	if (compressaoDeBonus) {
	    tempoCorrente.add(Calendar.MONTH, -1);
	}

	int mesAtual = tempoCorrente.get(Calendar.MONTH);
	int anoAtual = tempoCorrente.get(Calendar.YEAR);

	BigDecimal saldoPrevistoNoMes = BigDecimal.ZERO;
	BigDecimal saldoDoMesAtual = BigDecimal.ZERO;
	BigDecimal saldoLiberado = BigDecimal.ZERO;
	BigDecimal ganhosAteHoje = BigDecimal.ZERO;
	BigDecimal bonusPrimeiraCompraNoMes = BigDecimal.ZERO;
	BigDecimal bonusDeAdesaoDePontoDeApoioNoMes = BigDecimal.ZERO;
	BigDecimal bonusLinearNoMes = BigDecimal.ZERO;

	List<ExtratoDTO> extratoDoMes = new ArrayList<ExtratoDTO>();
	for (ExtratoDTO extratoDTO : extratoCompleto) {

	    if (extratoDTO.getValor().compareTo(BigDecimal.ZERO) != 0) {

		if (extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_PARA_OUTRO_DISTRIBUIDOR) //
			|| extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_PARA_ALABASTRUM_CARD) //
			|| extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_PARA_PAGAMENTO_DE_PEDIDO) //
			|| extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_PARA_SAQUE) //
			|| extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_POR_COMPRESSAO_DE_BONUS)) {

		    saldoLiberado = saldoLiberado.add(extratoDTO.getValor());
		    adicionarNoExtratoDoMes(mes, ano, saldoLiberado, extratoDoMes, extratoDTO);

		    if (extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_POR_COMPRESSAO_DE_BONUS)) {
			ganhosAteHoje = ganhosAteHoje.add(extratoDTO.getValor());
		    }
		} else {

		    if (new AtividadeService(hibernateUtil).isAtivo(idCodigo, extratoDTO.getData())) {

			saldoLiberado = saldoLiberado.add(extratoDTO.getValor());
			adicionarNoExtratoDoMes(mes, ano, saldoLiberado, extratoDoMes, extratoDTO);
			ganhosAteHoje = ganhosAteHoje.add(extratoDTO.getValor());

			if (extratoDTO.getData().get(Calendar.MONTH) == mesAtual && extratoDTO.getData().get(Calendar.YEAR) == anoAtual) {
			    saldoDoMesAtual = saldoDoMesAtual.add(extratoDTO.getValor());
			}
		    } else {

			if (extratoDTO.getData().get(Calendar.MONTH) == mesAtual && extratoDTO.getData().get(Calendar.YEAR) == anoAtual) {
			    saldoPrevistoNoMes = saldoPrevistoNoMes.add(extratoDTO.getValor());
			    adicionarNoExtratoDoMes(mes, ano, saldoPrevistoNoMes, extratoDoMes, extratoDTO);
			}
		    }

		    if (extratoDTO.getData().get(Calendar.MONTH) == mes && extratoDTO.getData().get(Calendar.YEAR) == ano) {

			if (extratoDTO.getDiscriminador().equals(BonusDePrimeiraCompraService.BÔNUS_DE_PRIMEIRA_COMPRA)) {
			    bonusPrimeiraCompraNoMes = bonusPrimeiraCompraNoMes.add(extratoDTO.getValor());
			}

			if (extratoDTO.getDiscriminador().equals(BonusDePrimeiraCompraService.BÔNUS_DE_ADESÃO_DE_PONTO_DE_APOIO)) {
			    bonusDeAdesaoDePontoDeApoioNoMes = bonusDeAdesaoDePontoDeApoioNoMes.add(extratoDTO.getValor());
			}

			if (extratoDTO.getDiscriminador().equals(BonusLinearService.BÔNUS_LINEAR)) {
			    bonusLinearNoMes = bonusLinearNoMes.add(extratoDTO.getValor());
			}
		    }
		}
	    }
	}

	BigDecimal saldoPrevistoTotal = saldoPrevistoNoMes.add(saldoLiberado);
	BigDecimal inss = saldoLiberado.multiply(Constants.TARIFA_INSS);
	BigDecimal saldoComDescontos = saldoLiberado.subtract(inss);

	SaldoDTO saldoDTO = new SaldoDTO();
	saldoDTO.setSaldoPrevistoNoMes(saldoPrevistoNoMes);
	saldoDTO.setSaldoDoMesAtual(saldoDoMesAtual);
	saldoDTO.setSaldoPrevistoTotal(saldoPrevistoTotal);
	saldoDTO.setSaldoLiberado(saldoLiberado);
	saldoDTO.setSaldoComDescontos(saldoComDescontos);
	saldoDTO.setGanhosAteHoje(ganhosAteHoje);
	saldoDTO.setInss(inss);
	saldoDTO.setExtratoDoMes(extratoDoMes);
	saldoDTO.setBonusPrimeiraCompraNoMes(bonusPrimeiraCompraNoMes);
	saldoDTO.setBonusDeAdesaoDePontoDeApoioNoMes(bonusDeAdesaoDePontoDeApoioNoMes);
	saldoDTO.setBonusLinearNoMes(bonusLinearNoMes);

	return saldoDTO;
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

		if (e1.getData().getTimeInMillis() < e2.getData().getTimeInMillis())
		    return -1;

		if (e1.getData().getTimeInMillis() > e2.getData().getTimeInMillis())
		    return 1;

		return 0;
	    }
	});

	return extrato;
    }
}
