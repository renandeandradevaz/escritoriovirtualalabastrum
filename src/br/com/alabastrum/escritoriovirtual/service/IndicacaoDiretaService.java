package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.dto.QualificacaoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroIngresso;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class IndicacaoDiretaService {

	private static final Integer INDICACAO_DIRETA_NIVEL = 0;
	private static final Integer POSICAO_DISTRIBUIDOR = 2;

	private HibernateUtil hibernateUtil;

	public IndicacaoDiretaService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public List<ExtratoDTO> obterIndicacoesDiretas(Integer idCodigo) {

		List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

		for (QualificacaoDTO qualificacaoDTO : new QualificacaoService(hibernateUtil).obterIndicadosDiretosNaPosicao(idCodigo, POSICAO_DISTRIBUIDOR)) {

			GregorianCalendar data = qualificacaoDTO.getQualificacao().getData();

			GregorianCalendar primeiroDiaDoMes = Util.getTempoCorrenteAMeiaNoite();
			primeiroDiaDoMes.set(Calendar.MONTH, data.get(Calendar.MONTH));
			primeiroDiaDoMes.set(Calendar.YEAR, data.get(Calendar.YEAR));
			primeiroDiaDoMes.set(Calendar.DAY_OF_MONTH, 1);

			ParametroIngresso parametroIngressoFiltro = new ParametroIngresso();
			parametroIngressoFiltro.setData(primeiroDiaDoMes);
			parametroIngressoFiltro.setNivel(INDICACAO_DIRETA_NIVEL);
			ParametroIngresso parametroIngresso = hibernateUtil.selecionar(parametroIngressoFiltro);

			if (parametroIngresso == null) {
				continue;
			}

			BigDecimal valorIngresso = new PontuacaoService(hibernateUtil).getValorIngresso(qualificacaoDTO.getUsuario().getId_Codigo(), data);
			BigDecimal valor = valorIngresso.multiply(parametroIngresso.getPorcentagem()).divide(new BigDecimal(100));
			extratos.add(new ExtratoDTO(qualificacaoDTO.getUsuario(), data, valor, "Indicação direta"));
		}

		return extratos;
	}
}