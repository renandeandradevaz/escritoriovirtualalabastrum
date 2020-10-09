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
	extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasDeCredito(idCodigo));
	extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasDeDebito(idCodigo));
	extratoCompleto = ordenarExtratoPorDataCrescente(extratoCompleto);

	GregorianCalendar tempoCorrente = Util.getTempoCorrenteAMeiaNoite();

	if (compressaoDeBonus) {
	    tempoCorrente.add(Calendar.MONTH, -1);
	}

	GregorianCalendar dataPesquisada = new GregorianCalendar(ano, mes, 1);

	BigDecimal saldoLiberado = BigDecimal.ZERO;
	BigDecimal saldoAnteriorAoMesPesquisado = BigDecimal.ZERO;
	BigDecimal ganhosNoMesPesquisado = BigDecimal.ZERO;
	BigDecimal gastosNoMesPesquisado = BigDecimal.ZERO;
	BigDecimal ganhosAteHoje = BigDecimal.ZERO;
	BigDecimal bonusPrimeiraCompraNoMes = BigDecimal.ZERO;
	BigDecimal bonusDeAdesaoDePontoDeApoioNoMes = BigDecimal.ZERO;
	BigDecimal bonusLinearNoMes = BigDecimal.ZERO;
	BigDecimal bonusTrinarioNoMes = BigDecimal.ZERO;
	BigDecimal bonusFilaUnicaNoMes = BigDecimal.ZERO;
	BigDecimal bonusGlobalNoMes = BigDecimal.ZERO;
	BigDecimal bonusReconhecimentoNoMes = BigDecimal.ZERO;
	BigDecimal bonusDesempenhoNoMes = BigDecimal.ZERO;

	List<ExtratoDTO> extratoDoMes = new ArrayList<ExtratoDTO>();
	for (ExtratoDTO extratoDTO : extratoCompleto) {

	    if (extratoDTO.getValor().compareTo(BigDecimal.ZERO) != 0) {

		if (extratoDTO.getDiscriminador().startsWith("Transferência")) {

		    adicionarNoExtratoDoMes(mes, ano, extratoDoMes, extratoDTO);

		    saldoLiberado = saldoLiberado.add(extratoDTO.getValor());

		    if (extratoDTO.getData().before(dataPesquisada)) {
			saldoAnteriorAoMesPesquisado = saldoAnteriorAoMesPesquisado.add(extratoDTO.getValor());
		    }

		    if (extratoDTO.getData().get(Calendar.MONTH) == mes && extratoDTO.getData().get(Calendar.YEAR) == ano) {
			gastosNoMesPesquisado = gastosNoMesPesquisado.add(extratoDTO.getValor());
		    }

		    if (extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_POR_COMPRESSAO_DE_BONUS)) {
			ganhosAteHoje = ganhosAteHoje.add(extratoDTO.getValor());
		    }

		} else {

		    if (isHabilitadoParaBonus(idCodigo, extratoDTO)) {

			adicionarNoExtratoDoMes(mes, ano, extratoDoMes, extratoDTO);

			BigDecimal valorComDescontos = extratoDTO.getValor().subtract(extratoDTO.getValor().multiply(Constants.TARIFA_INSS));

			saldoLiberado = saldoLiberado.add(valorComDescontos);
			ganhosAteHoje = ganhosAteHoje.add(extratoDTO.getValor());

			if (extratoDTO.getData().before(dataPesquisada)) {
			    saldoAnteriorAoMesPesquisado = saldoAnteriorAoMesPesquisado.add(valorComDescontos);
			}

			if (extratoDTO.getData().get(Calendar.MONTH) == mes && extratoDTO.getData().get(Calendar.YEAR) == ano) {

			    ganhosNoMesPesquisado = ganhosNoMesPesquisado.add(extratoDTO.getValor());

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

			    if (extratoDTO.getDiscriminador().equals(Bonificacao.BONUS_DE_RECONHECIMENTO)) {
				bonusReconhecimentoNoMes = bonusReconhecimentoNoMes.add(extratoDTO.getValor());
			    }

			    if (extratoDTO.getDiscriminador().equals(Bonificacao.BONUS_DE_DESEMPENHO)) {
				bonusDesempenhoNoMes = bonusDesempenhoNoMes.add(extratoDTO.getValor());
			    }
			}
		    }
		}
	    }
	}

	SaldoDTO saldoDTO = new SaldoDTO();
	saldoDTO.setSaldoLiberado(saldoLiberado);
	saldoDTO.setGanhosAteHoje(ganhosAteHoje);
	saldoDTO.setExtratoDoMes(extratoDoMes);
	saldoDTO.setBonusPrimeiraCompraNoMes(bonusPrimeiraCompraNoMes);
	saldoDTO.setBonusDeAdesaoDePontoDeApoioNoMes(bonusDeAdesaoDePontoDeApoioNoMes);
	saldoDTO.setBonusLinearNoMes(bonusLinearNoMes);
	saldoDTO.setBonusTrinarioNoMes(bonusTrinarioNoMes);
	saldoDTO.setBonusFilaUnicaNoMes(bonusFilaUnicaNoMes);
	saldoDTO.setBonusGlobalNoMes(bonusGlobalNoMes);
	saldoDTO.setBonusReconhecimentoNoMes(bonusReconhecimentoNoMes);
	saldoDTO.setBonusDesempenhoNoMes(bonusDesempenhoNoMes);
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

    private void adicionarNoExtratoDoMes(Integer mes, Integer ano, List<ExtratoDTO> extratoDoMes, ExtratoDTO extratoDTO) {

	if (extratoDTO.getData().get(Calendar.MONTH) == mes && extratoDTO.getData().get(Calendar.YEAR) == ano) {
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
