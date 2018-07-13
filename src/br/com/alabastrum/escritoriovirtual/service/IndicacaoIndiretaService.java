package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroIngresso;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class IndicacaoIndiretaService {

	private static final Integer POSICAO_DISTRIBUIDOR = 2;

	private HibernateUtil hibernateUtil;

	public IndicacaoIndiretaService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public List<ExtratoDTO> obterIndicacoesIndiretas(Integer idCodigo) {

		List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

		for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaDTOEntry : new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(idCodigo).entrySet()) {

			Qualificacao qualificacao = new QualificacaoService(hibernateUtil).obterQualificacaoNaPosicao(arvoreHierarquicaDTOEntry.getKey(), POSICAO_DISTRIBUIDOR);

			if (qualificacao != null && !arvoreHierarquicaDTOEntry.getValue().getUsuario().getId_Indicante().equals(idCodigo)) {

				List<Integer> arvoreHierarquicaAscendente = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAscendente(qualificacao.getId_Codigo(), qualificacao.getData());

				if (arvoreHierarquicaAscendente.contains(idCodigo)) {

					ParametroIngresso parametroIngresso = new ParametroIngressoService(hibernateUtil).buscarParametroIngresso(qualificacao.getData(), arvoreHierarquicaAscendente.indexOf(idCodigo));

					if (parametroIngresso == null) {
						continue;
					}

					BigDecimal valorIngresso = new PontuacaoService(hibernateUtil).getValorIngresso(qualificacao.getId_Codigo(), qualificacao.getData());
					BigDecimal valor = valorIngresso.multiply(parametroIngresso.getPorcentagem()).divide(new BigDecimal(100));
					extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(qualificacao.getId_Codigo())), qualificacao.getData(), valor, "Indicação indireta"));
				}
			}
		}

		return extratos;
	}
}