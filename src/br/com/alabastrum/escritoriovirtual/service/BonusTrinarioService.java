package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroAtividade;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class BonusTrinarioService {

    public static final String BÔNUS_TRINARIO = "Bônus Trinário";

    private HibernateUtil hibernateUtil;

    public BonusTrinarioService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public List<ExtratoDTO> obterBonificacoesTrinarias(Integer idCodigo) throws Exception {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMap = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(idCodigo, "id_lider");

	for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEntry : arvoreHierarquicaMap.entrySet()) {

	    List<Pontuacao> pontuacoes = new PontuacaoService(hibernateUtil).buscarPontuacoes(arvoreHierarquicaEntry.getKey());

	    for (Pontuacao pontuacao : pontuacoes) {

		if (pontuacao.getPntAtividade().compareTo(BigDecimal.ONE) > 0) {

		    ParametroAtividade parametroAtividade = new ParametroAtividadeService(hibernateUtil).buscarParametroAtividade(pontuacao.getDt_Pontos(), arvoreHierarquicaEntry.getValue().getNivel());

		    if (parametroAtividade != null) {
			extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(arvoreHierarquicaEntry.getKey())), pontuacao.getDt_Pontos(), parametroAtividade.getBonusAtividade(), BÔNUS_TRINARIO));
		    }
		}
	    }
	}

	return extratos;
    }
}