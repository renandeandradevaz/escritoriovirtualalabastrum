package br.com.alabastrum.escritoriovirtual.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.QualificacaoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class QualificacaoService {

	private HibernateUtil hibernateUtil;

	public QualificacaoService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public List<QualificacaoDTO> obterUltimosQualificados(Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquica) {

		List<QualificacaoDTO> qualificacoes = new ArrayList<QualificacaoDTO>();

		for (Entry<Integer, ArvoreHierarquicaDTO> entry : arvoreHierarquica.entrySet()) {

			Usuario usuario = entry.getValue().getUsuario();

			Qualificacao filtro = new Qualificacao();
			filtro.setId_Codigo(usuario.getId_Codigo());
			List<Qualificacao> qualificacoesDoDistribuidor = hibernateUtil.buscar(filtro);

			if (qualificacoesDoDistribuidor.size() > 2) {
				Qualificacao ultimaQualificacao = qualificacoesDoDistribuidor.get(qualificacoesDoDistribuidor.size() - 1);
				qualificacoes.add(new QualificacaoDTO(usuario, ultimaQualificacao));
			}
		}

		qualificacoes = ordenarQualificacoesPorDataDescrescente(qualificacoes);

		if (qualificacoes.size() > 7) {
			return qualificacoes.subList(0, 6);
		}

		return qualificacoes;
	}

	public List<QualificacaoDTO> obterUltimosCadastros(Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquica) {

		List<QualificacaoDTO> qualificacoes = new ArrayList<QualificacaoDTO>();

		for (Entry<Integer, ArvoreHierarquicaDTO> entry : arvoreHierarquica.entrySet()) {

			Usuario usuario = entry.getValue().getUsuario();

			Qualificacao filtro = new Qualificacao();
			filtro.setId_Codigo(usuario.getId_Codigo());
			List<Qualificacao> qualificacoesDoDistribuidor = hibernateUtil.buscar(filtro);

			if (qualificacoesDoDistribuidor.size() <= 2) {
				Qualificacao qualificacao = qualificacoesDoDistribuidor.get(0);
				qualificacoes.add(new QualificacaoDTO(usuario, qualificacao));
			}
		}

		qualificacoes = ordenarQualificacoesPorDataDescrescente(qualificacoes);

		if (qualificacoes.size() > 20) {
			return qualificacoes.subList(0, 19);
		}

		return qualificacoes;
	}

	private List<QualificacaoDTO> ordenarQualificacoesPorDataDescrescente(List<QualificacaoDTO> qualificacoes) {

		Collections.sort(qualificacoes, new Comparator<QualificacaoDTO>() {

			public int compare(QualificacaoDTO q1, QualificacaoDTO q2) {

				if (q1.getQualificacao().getData().getTimeInMillis() > q2.getQualificacao().getData().getTimeInMillis()) {
					return -1;
				}
				return 1;
			}
		});

		return qualificacoes;
	}
}
