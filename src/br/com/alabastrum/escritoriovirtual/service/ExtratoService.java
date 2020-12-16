package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.dto.SaldoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Transferencia;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
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

	Usuario usuario = this.hibernateUtil.selecionar(new Usuario(idCodigo));
	TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMap = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(idCodigo);

	Map<String, Boolean> atividadePorMesCache = new HashMap<String, Boolean>();
	Map<String, Boolean> indicadosDiretosAtivosPorMesCache = new HashMap<String, Boolean>();

	List<ExtratoDTO> extratoCompleto = new ArrayList<ExtratoDTO>();

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
	BigDecimal bonusLojaVirtualNoMes = BigDecimal.ZERO;

	List<ExtratoDTO> extratoDoMes = new ArrayList<ExtratoDTO>();

	if (!"revendedor".equalsIgnoreCase(usuario.getNome_kit())) {

	    extratoCompleto.addAll(new BonusDePrimeiraCompraService(hibernateUtil).obterBonificacoesDePrimeiraCompra(idCodigo, arvoreHierarquicaMap));
	    extratoCompleto.addAll(new BonusLinearService(hibernateUtil).obterBonificacoesLineares(idCodigo, arvoreHierarquicaMap));
	    extratoCompleto.addAll(new BonusTrinarioService(hibernateUtil).obterBonificacoesTrinarias(idCodigo));
	    extratoCompleto.addAll(new BonificacoesPreProcessadasService(hibernateUtil).obterBonificacoesPreProcessadas(idCodigo));
	    extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasDeCredito(idCodigo));
	    extratoCompleto.addAll(new TransferenciaService(hibernateUtil).obterTransferenciasDeDebito(idCodigo));
	    extratoCompleto = ordenarExtratoPorDataCrescente(extratoCompleto);

	    for (ExtratoDTO extratoDTO : extratoCompleto) {

		boolean isMesPesquisado = extratoDTO.getData().get(Calendar.MONTH) == mes && extratoDTO.getData().get(Calendar.YEAR) == ano;

		BigDecimal valor = BigDecimal.ZERO;

		if (extratoDTO.getValor().compareTo(BigDecimal.ZERO) > 0 && isHabilitadoParaBonus(idCodigo, extratoDTO, atividadePorMesCache, indicadosDiretosAtivosPorMesCache, arvoreHierarquicaMap)) {

		    adicionarNoExtratoDoMes(mes, ano, extratoDoMes, extratoDTO);

		    valor = extratoDTO.getValor().subtract(new TarifasService().calcularInss(usuario.getDescontaInss(), extratoDTO.getValor()));
		    ganhosAteHoje = ganhosAteHoje.add(valor);

		    if (isMesPesquisado) {
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

			if (extratoDTO.getDiscriminador().equals(Bonificacao.BÔNUS_LOJA_VIRTUAL)) {
			    bonusLojaVirtualNoMes = bonusLojaVirtualNoMes.add(extratoDTO.getValor());
			}
		    }
		} else if (extratoDTO.getValor().compareTo(BigDecimal.ZERO) < 0) {

		    adicionarNoExtratoDoMes(mes, ano, extratoDoMes, extratoDTO);

		    valor = extratoDTO.getValor();

		    if (isMesPesquisado) {
			gastosNoMesPesquisado = gastosNoMesPesquisado.add(valor);
		    }
		}

		saldoLiberado = saldoLiberado.add(valor);

		if (extratoDTO.getData().before(dataPesquisada)) {
		    saldoAnteriorAoMesPesquisado = saldoAnteriorAoMesPesquisado.add(valor);
		}
	    }
	}

	SaldoDTO saldoDTO = new SaldoDTO();
	saldoDTO.setSaldoLiberado(saldoLiberado);
	saldoDTO.setGanhosAteHoje(ganhosAteHoje);
	saldoDTO.setExtratoCompleto(extratoCompleto);
	saldoDTO.setExtratoDoMes(extratoDoMes);
	saldoDTO.setBonusPrimeiraCompraNoMes(bonusPrimeiraCompraNoMes);
	saldoDTO.setBonusDeAdesaoDePontoDeApoioNoMes(bonusDeAdesaoDePontoDeApoioNoMes);
	saldoDTO.setBonusLinearNoMes(bonusLinearNoMes);
	saldoDTO.setBonusTrinarioNoMes(bonusTrinarioNoMes);
	saldoDTO.setBonusFilaUnicaNoMes(bonusFilaUnicaNoMes);
	saldoDTO.setBonusGlobalNoMes(bonusGlobalNoMes);
	saldoDTO.setBonusReconhecimentoNoMes(bonusReconhecimentoNoMes);
	saldoDTO.setBonusDesempenhoNoMes(bonusDesempenhoNoMes);
	saldoDTO.setBonusLojaVirtualNoMes(bonusLojaVirtualNoMes);
	saldoDTO.setSaldoAnteriorAoMesPesquisado(saldoAnteriorAoMesPesquisado);
	saldoDTO.setGanhosNoMesPesquisado(ganhosNoMesPesquisado);
	saldoDTO.setInssNoMesPesquisado(new TarifasService().calcularInss(usuario.getDescontaInss(), ganhosNoMesPesquisado));
	saldoDTO.setGastosNoMesPesquisado(gastosNoMesPesquisado);

	return saldoDTO;
    }

    private boolean isHabilitadoParaBonus(Integer idCodigo, ExtratoDTO extratoDTO, Map<String, Boolean> atividadePorMesCacheMap, Map<String, Boolean> indicadosDiretosAtivosPorMesCacheMap, TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMap) {

	if (extratoDTO.getDiscriminador().equals(Transferencia.TRANSFERENCIA_POR_CREDITO) //
		|| extratoDTO.getDiscriminador().equals(BonusDePrimeiraCompraService.BÔNUS_DE_PRIMEIRA_COMPRA)//
		|| extratoDTO.getDiscriminador().equals(Bonificacao.BÔNUS_LOJA_VIRTUAL)) {
	    return true;
	}

	GregorianCalendar data = extratoDTO.getData();
	String mesEAno = data.get(Calendar.MONTH) + "/" + data.get(Calendar.YEAR);

	Boolean ativo = null;
	Boolean atividadePorMesCache = atividadePorMesCacheMap.get(mesEAno);
	if (atividadePorMesCache == null) {
	    ativo = new AtividadeService(hibernateUtil).isAtivo(idCodigo, data);
	    atividadePorMesCacheMap.put(mesEAno, ativo);
	} else {
	    ativo = atividadePorMesCache;
	}

	Boolean possuiIndicadosAtivosDiretos = null;
	Boolean indicadosDiretosAtivosPorMesCache = indicadosDiretosAtivosPorMesCacheMap.get(mesEAno);
	if (indicadosDiretosAtivosPorMesCache == null) {
	    possuiIndicadosAtivosDiretos = new AtividadeService(hibernateUtil).possuiIndicadosDiretosAtivos(idCodigo, data, 3, arvoreHierarquicaMap);
	    indicadosDiretosAtivosPorMesCacheMap.put(mesEAno, possuiIndicadosAtivosDiretos);
	} else {
	    possuiIndicadosAtivosDiretos = indicadosDiretosAtivosPorMesCache;
	}

	if (extratoDTO.getDiscriminador().equals(BonusTrinarioService.BÔNUS_TRINARIO)) {
	    return ativo && possuiIndicadosAtivosDiretos;
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
