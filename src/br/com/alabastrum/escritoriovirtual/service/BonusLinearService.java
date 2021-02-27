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
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class BonusLinearService {

    public static final String BÔNUS_LINEAR = "Bônus linear";

    private HibernateUtil hibernateUtil;

    public BonusLinearService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public List<ExtratoDTO> obterBonificacoesLineares(Integer idCodigo, TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMap) throws Exception {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEntry : arvoreHierarquicaMap.entrySet()) {

	    if (arvoreHierarquicaEntry.getValue().getNivel() <= 1) {

		List<Pontuacao> pontuacoes = new PontuacaoService(hibernateUtil).buscarPontuacoes(arvoreHierarquicaEntry.getKey());

		for (Pontuacao pontuacao : pontuacoes) {

		    GregorianCalendar primeiroOutubro2020 = new GregorianCalendar(2020, Calendar.OCTOBER, 1);

		    if (pontuacao.getDt_Pontos().before(primeiroOutubro2020)) {

			BigDecimal pontuacaoParaCalculo = null;

			BigDecimal pntLinear = pontuacao.getPntLinear();
			if (pntLinear != null && pntLinear.compareTo(BigDecimal.ZERO) > 0) {
			    pontuacaoParaCalculo = pntLinear;
			} else {
			    pontuacaoParaCalculo = pontuacao.getPntProduto();
			}

			if (pontuacaoParaCalculo != null && pontuacaoParaCalculo.compareTo(BigDecimal.ZERO) > 0) {

			    extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(arvoreHierarquicaEntry.getKey())), pontuacao.getDt_Pontos(), pontuacaoParaCalculo.multiply(new BigDecimal("0.1")), BÔNUS_LINEAR));
			}
		    }
		}
	    }
	}

	System.out.println("=============== BONUS LINEAR ===============");

	for (ExtratoDTO extrato : extratos) {
	    System.out.println(extrato.getData().get(Calendar.MONTH));
	    System.out.println(extrato.getValor());
	    System.out.println();
	}

	System.out.println("=============== BONUS LINEAR ===============");

	return extratos;
    }
}