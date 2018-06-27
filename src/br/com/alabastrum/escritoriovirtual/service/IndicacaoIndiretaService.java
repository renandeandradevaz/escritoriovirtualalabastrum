package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.dto.QualificacaoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroIngresso;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class IndicacaoIndiretaService {


	private static final Integer POSICAO_DISTRIBUIDOR = 2;

	private HibernateUtil hibernateUtil;

	public IndicacaoIndiretaService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public List<ExtratoDTO> obterIndicacoesIndiretas(Integer idCodigo) {

		
		for(Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaDTOEntry : new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(idCodigo).entrySet()) {
			Qualificacao qualificacao = new QualificacaoService(hibernateUtil).obterQualificacaoNaPosicao(arvoreHierarquicaDTOEntry.getKey(), POSICAO_DISTRIBUIDOR);
			if(qualificacao != null) {
				
				arvoreHierarquicaDTOEntry.getValue().getUsuario().getId_lider();
			}
		}
		
		
		
		
		
		

		 List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();
		return extratos;
	}
}