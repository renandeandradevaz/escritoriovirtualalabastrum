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
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
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
	extratoCompleto.addAll(new BonusTrinarioService(hibernateUtil).obterBonificacoesTrinarias(idCodigo));
	extratoCompleto.addAll(new BonificacoesPreProcessadasService(hibernateUtil).obterBonificacoesPreProcessadas(idCodigo));
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
	GregorianCalendar dataPesquisada = new GregorianCalendar(ano, mes, 1);

	BigDecimal saldoPrevistoNoMes = BigDecimal.ZERO;
	BigDecimal saldoDoMesAtual = BigDecimal.ZERO;
	BigDecimal saldoLiberado = BigDecimal.ZERO;
	BigDecimal ganhosAteHoje = BigDecimal.ZERO;
	BigDecimal bonusPrimeiraCompraNoMes = BigDecimal.ZERO;
	BigDecimal bonusDeAdesaoDePontoDeApoioNoMes = BigDecimal.ZERO;
	BigDecimal bonusLinearNoMes = BigDecimal.ZERO;
	BigDecimal bonusTrinarioNoMes = BigDecimal.ZERO;
	BigDecimal bonusFilaUnicaNoMes = BigDecimal.ZERO;
	BigDecimal bonusGlobalNoMes = BigDecimal.ZERO;
	BigDecimal saldoAnteriorAoMesPesquisado = BigDecimal.ZERO;
	BigDecimal ganhosNoMesPesquisado = BigDecimal.ZERO;
	BigDecimal gastosNoMesPesquisado = BigDecimal.ZERO;

	List<ExtratoDTO> extratoDoMes = new ArrayList<ExtratoDTO>();
	for (ExtratoDTO extratoDTO : extratoCompleto) {

	    if (extratoDTO.getValor().compareTo(BigDecimal.ZERO) != 0) {

		if (extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_PARA_OUTRO_DISTRIBUIDOR) //
			|| extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_PARA_ALABASTRUM_CARD) //
			|| extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_PARA_PAGAMENTO_DE_PEDIDO) //
			|| extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_PARA_SAQUE) //
			|| extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_POR_COMPRESSAO_DE_BONUS)) {

		    saldoLiberado = saldoLiberado.add(extratoDTO.getValor());
		    if (extratoDTO.getData().before(dataPesquisada)) {
			saldoAnteriorAoMesPesquisado = saldoAnteriorAoMesPesquisado.add(extratoDTO.getValor());
		    }
		    adicionarNoExtratoDoMes(mes, ano, saldoLiberado, extratoDoMes, extratoDTO);

		    if (extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_POR_COMPRESSAO_DE_BONUS)) {
			ganhosAteHoje = ganhosAteHoje.add(extratoDTO.getValor());
		    }

		    if (extratoDTO.getData().get(Calendar.MONTH) == mes && extratoDTO.getData().get(Calendar.YEAR) == ano) {
			gastosNoMesPesquisado = gastosNoMesPesquisado.add(extratoDTO.getValor());
		    }

		} else {

		    if (isHabilitadoParaBonus(idCodigo, extratoDTO)) {
			saldoLiberado = saldoLiberado.add(extratoDTO.getValor().subtract(extratoDTO.getValor().multiply(Constants.TARIFA_INSS)));

			if (extratoDTO.getData().before(dataPesquisada)) {
			    saldoAnteriorAoMesPesquisado = saldoAnteriorAoMesPesquisado.add(extratoDTO.getValor().subtract(extratoDTO.getValor().multiply(Constants.TARIFA_INSS)));
			}
			adicionarNoExtratoDoMes(mes, ano, saldoLiberado, extratoDoMes, extratoDTO);
			ganhosAteHoje = ganhosAteHoje.add(extratoDTO.getValor());

			if (extratoDTO.getData().get(Calendar.MONTH) == mesAtual && extratoDTO.getData().get(Calendar.YEAR) == anoAtual) {
			    saldoDoMesAtual = saldoDoMesAtual.add(extratoDTO.getValor());
			}

			if (extratoDTO.getData().get(Calendar.MONTH) == mes && extratoDTO.getData().get(Calendar.YEAR) == ano) {
			    ganhosNoMesPesquisado = ganhosNoMesPesquisado.add(extratoDTO.getValor());
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

			if (extratoDTO.getDiscriminador().equals(BonusTrinarioService.BÔNUS_TRINARIO)) {
			    bonusTrinarioNoMes = bonusTrinarioNoMes.add(extratoDTO.getValor());
			}

			if (extratoDTO.getDiscriminador().equals(Bonificacao.BONUS_DE_FILA_UNICA)) {
			    bonusFilaUnicaNoMes = bonusFilaUnicaNoMes.add(extratoDTO.getValor());
			}

			if (extratoDTO.getDiscriminador().equals(Bonificacao.BONUS_GLOBAL)) {
			    bonusGlobalNoMes = bonusGlobalNoMes.add(extratoDTO.getValor());
			}
		    }
		}
	    }
	}

	SaldoDTO saldoDTO = new SaldoDTO();
	saldoDTO.setSaldoPrevistoNoMes(saldoPrevistoNoMes);
	saldoDTO.setSaldoDoMesAtual(saldoDoMesAtual);
	saldoDTO.setSaldoLiberado(saldoLiberado);
	saldoDTO.setGanhosAteHoje(ganhosAteHoje);
	saldoDTO.setExtratoDoMes(extratoDoMes);
	saldoDTO.setBonusPrimeiraCompraNoMes(bonusPrimeiraCompraNoMes);
	saldoDTO.setBonusDeAdesaoDePontoDeApoioNoMes(bonusDeAdesaoDePontoDeApoioNoMes);
	saldoDTO.setBonusLinearNoMes(bonusLinearNoMes);
	saldoDTO.setBonusTrinarioNoMes(bonusTrinarioNoMes);
	saldoDTO.setBonusFilaUnicaNoMes(bonusFilaUnicaNoMes);
	saldoDTO.setBonusGlobalNoMes(bonusGlobalNoMes);
	saldoDTO.setBonificacoesNoMes(bonusPrimeiraCompraNoMes.add(bonusDeAdesaoDePontoDeApoioNoMes.add(bonusLinearNoMes.add(bonusTrinarioNoMes.add(bonusFilaUnicaNoMes)))));
	saldoDTO.setInssNoMes(saldoDTO.getBonificacoesNoMes().multiply(Constants.TARIFA_INSS));
	saldoDTO.setSaldoAnteriorAoMesPesquisado(saldoAnteriorAoMesPesquisado);
	saldoDTO.setGanhosNoMesPesquisado(ganhosNoMesPesquisado);
	saldoDTO.setInssNoMesPesquisado(ganhosNoMesPesquisado.multiply(Constants.TARIFA_INSS));
	saldoDTO.setGastosNoMesPesquisado(gastosNoMesPesquisado);

	return saldoDTO;
    }

    private boolean isHabilitadoParaBonus(Integer idCodigo, ExtratoDTO extratoDTO) {

	boolean ativo = new AtividadeService(hibernateUtil).isAtivo(idCodigo, extratoDTO.getData());

	if (extratoDTO.getDiscriminador().equals(BonusTrinarioService.BÔNUS_TRINARIO)) {
	    return ativo && new AtividadeService(hibernateUtil).possuiIndicadosDiretosAtivos(idCodigo, extratoDTO.getData(), 3);
	}

	return ativo;
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
