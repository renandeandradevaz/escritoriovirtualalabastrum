package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroAtividade;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class BonusAtivacaoService {

	private HibernateUtil hibernateUtil;

	public BonusAtivacaoService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public List<ExtratoDTO> obterBonificacoesPorAtivacao(Integer idCodigo) {

		List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

		for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaDTOEntry : new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(idCodigo).entrySet()) {

			Pontuacao pontuacaoFiltro = new Pontuacao();
			pontuacaoFiltro.setId_Codigo(arvoreHierarquicaDTOEntry.getKey());
			List<Pontuacao> pontuacoes = hibernateUtil.buscar(pontuacaoFiltro);

			for (Pontuacao pontuacao : pontuacoes) {

				if (pontuacao.getParametroAtividade().compareTo(BigDecimal.ZERO) > 0 && pontuacao.getPntAtividade().compareTo(pontuacao.getParametroAtividade()) >= 0) {

					List<Integer> arvoreHierarquicaAtivaAscendente = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAtivaAscendente(pontuacao.getId_Codigo(), pontuacao.getDt_Pontos());

					if (arvoreHierarquicaAtivaAscendente.contains(idCodigo)) {

						ParametroAtividade parametroAtividade = new ParametroAtividadeService(hibernateUtil).buscarParametroAtividade(pontuacao.getDt_Pontos(), arvoreHierarquicaAtivaAscendente.indexOf(idCodigo));

						if (parametroAtividade == null) {
							continue;
						}

						extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(pontuacao.getId_Codigo())), pontuacao.getDt_Pontos(), parametroAtividade.getBonusAtividade(), "Ativação"));
					}
				}
			}
		}

		return extratos;
	}
}