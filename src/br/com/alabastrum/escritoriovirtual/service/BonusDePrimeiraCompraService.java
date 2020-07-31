package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.dto.PedidoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Adesao;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class BonusDePrimeiraCompraService {

    private HibernateUtil hibernateUtil;

    public BonusDePrimeiraCompraService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public List<ExtratoDTO> obterBonificacoesDePrimeiraCompra(Integer idCodigo) throws Exception {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMap = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(idCodigo);

	List<Adesao> adesoes = hibernateUtil.buscar(new Adesao());
	for (Adesao adesao : adesoes) {

	    GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(adesao.getData_referencia());
	    GregorianCalendar primeiroDiaDoProximoMes = (GregorianCalendar) primeiroDiaDoMes.clone();
	    primeiroDiaDoProximoMes.add(Calendar.MONTH, 1);

	    for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEntry : arvoreHierarquicaMap.entrySet()) {

		int nivel = arvoreHierarquicaEntry.getValue().getNivel();
		// melhorar essa busca repetitiva aqui com map
		Adesao adesaoNoNivel = new AdesaoService(hibernateUtil).buscarAdesao(adesao.getData_referencia(), nivel);

		if (adesaoNoNivel != null) {

		    Pedido pedidoFiltro = new Pedido();
		    pedidoFiltro.setIdCodigo(idCodigo);
		    pedidoFiltro.setStatus(PedidoService.FINALIZADO);
		    pedidoFiltro.setTipo(PedidoService.ADESAO);

		    List<Criterion> restricoes = new ArrayList<Criterion>();
		    restricoes.add(Restrictions.between("data", primeiroDiaDoMes, primeiroDiaDoProximoMes));

		    List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro, restricoes);

		    if (Util.preenchido(pedidos)) {
			for (Pedido pedido : pedidos) {
			    PedidoDTO pedidoDTO = new PedidoService(hibernateUtil).calcularTotais(pedido);
			    BigDecimal valorTotal = pedidoDTO.getValorTotal();

			    if (valorTotal.compareTo(new BigDecimal("120")) == -1) {
				throw new Exception(String.format("Valor total do pedido %s é menor do que 120 reais", pedido.getId()));
			    }

			    extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo())), pedido.getData(), adesaoNoNivel.getBonusAdesao(), "Bônus de primeira compra"));
			}
		    }
		}
	    }

	}

	return extratos;
    }
}