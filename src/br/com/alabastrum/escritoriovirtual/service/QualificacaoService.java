package br.com.alabastrum.escritoriovirtual.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

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

	public List<QualificacaoDTO> obterIndicadosDiretosNaPosicao(Integer idCodigo, Integer posicao) {

		Usuario usuarioFiltro = new Usuario();
		usuarioFiltro.setId_Indicante(idCodigo);
		List<Usuario> usuarios = hibernateUtil.buscar(usuarioFiltro);

		List<QualificacaoDTO> qualificacoes = new ArrayList<QualificacaoDTO>();

		for (Usuario usuario : usuarios) {

			Qualificacao qualificacaoFiltro = new Qualificacao();
			qualificacaoFiltro.setId_Codigo(usuario.getId_Codigo());
			List<Qualificacao> qualificacoesDoDistribuidor = hibernateUtil.buscar(qualificacaoFiltro);

			if (qualificacoesDoDistribuidor.size() >= posicao) {
				Qualificacao qualificacaoNaPosicao = qualificacoesDoDistribuidor.get(posicao - 1);
				qualificacoes.add(new QualificacaoDTO(usuario, qualificacaoNaPosicao));
			}
		}

		return qualificacoes;
	}

	public Qualificacao obterQualificacaoNaPosicao(Integer idCodigo, Integer posicao) {

		Qualificacao qualificacaoFiltro = new Qualificacao();
		qualificacaoFiltro.setId_Codigo(idCodigo);
		List<Qualificacao> qualificacoesDoDistribuidor = hibernateUtil.buscar(qualificacaoFiltro);

		if (qualificacoesDoDistribuidor.size() >= posicao) {
			return qualificacoesDoDistribuidor.get(posicao - 1);
		}

		return null;
	}

	public List<QualificacaoDTO> obterUltimosQualificados(TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquica) {

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

	public List<QualificacaoDTO> obterUltimosCadastros(TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquica) {

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

	public Qualificacao obterUltimaQualificacao(Integer idCodigo) {

		Qualificacao qualificacaoFiltro = new Qualificacao();
		qualificacaoFiltro.setId_Codigo(idCodigo);
		List<Qualificacao> qualificacoesDoDistribuidor = hibernateUtil.buscar(qualificacaoFiltro);
		return qualificacoesDoDistribuidor.get(qualificacoesDoDistribuidor.size() - 1);
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
