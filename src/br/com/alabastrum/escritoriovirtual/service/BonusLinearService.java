package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class BonusLinearService {

    public static final String BÔNUS_LINEAR = "Bônus linear";

    private HibernateUtil hibernateUtil;

    public BonusLinearService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public List<ExtratoDTO> obterBonificacoesLineares(Integer idCodigo) throws Exception {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMap = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(idCodigo, 1);

	for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEntry : arvoreHierarquicaMap.entrySet()) {

	    List<Pontuacao> pontuacoes = new PontuacaoService(hibernateUtil).buscarPontuacoes(arvoreHierarquicaEntry.getKey());

	    for (Pontuacao pontuacao : pontuacoes) {

		if (pontuacao.getPntProduto().compareTo(BigDecimal.ZERO) > 0) {

		    extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(arvoreHierarquicaEntry.getKey())), pontuacao.getDt_Pontos(), pontuacao.getPntProduto().multiply(new BigDecimal("0.1")), BÔNUS_LINEAR));
		}
	    }
	}

	return extratos;
    }
}