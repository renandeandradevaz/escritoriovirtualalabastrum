package br.com.alabastrum.escritoriovirtual.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Adesao;

public class BonusDePrimeiraCompraService {

    private HibernateUtil hibernateUtil;

    public BonusDePrimeiraCompraService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public List<ExtratoDTO> obterBonificacoesDePrimeiraCompra(Integer idCodigo) {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMap = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(idCodigo);

	List<Adesao> adesoes = hibernateUtil.buscar(new Adesao());
	for (Adesao adesao : adesoes) {

	    // GregorianCalendar primeiroDiaDoMes =
	    // Util.getPrimeiroDiaDoMes(adesao.getData_referencia());
	    // GregorianCalendar ultimoDiaDoMes =
	    // Util.getUltimoDiaDoMes(adesao.getData_referencia());

	    for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEntry : arvoreHierarquicaMap.entrySet()) {

		int nivel = arvoreHierarquicaEntry.getValue().getNivel();
		// melhorar essa busca repetitiva aqui com map
		Adesao adesaoNoNivel = new AdesaoService(hibernateUtil).buscarAdesao(adesao.getData_referencia(), nivel);

		if (adesaoNoNivel != null) {

		    // procurar pedidos com o tipo ADESAO

//					BigDecimal valorIngresso = new PontuacaoService(hibernateUtil).getValorIngresso(qualificacao.getId_Codigo(), qualificacao.getData());
//					BigDecimal valor = valorIngresso.multiply(parametroIngresso.getPorcentagem()).divide(new BigDecimal(100));
//					extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(qualificacao.getId_Codigo())), qualificacao.getData(), valor, "Indicação indireta"));
		}
	    }

	}

	return extratos;
    }
}