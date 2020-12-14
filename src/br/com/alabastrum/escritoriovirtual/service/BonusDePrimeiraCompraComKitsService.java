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
import br.com.alabastrum.escritoriovirtual.dto.PedidoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.BonusAdesao;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class BonusDePrimeiraCompraComKitsService {

    public static final String BÔNUS_DE_PRIMEIRA_COMPRA = "Bônus de primeira compra";

    private HibernateUtil hibernateUtil;

    public BonusDePrimeiraCompraComKitsService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public List<ExtratoDTO> obterBonificacoesDePrimeiraCompraComKits(Integer idCodigo, TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMap) throws Exception {

	Usuario usuario = this.hibernateUtil.selecionar(new Usuario(idCodigo));

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEntry : arvoreHierarquicaMap.entrySet()) {

	    int nivel = arvoreHierarquicaEntry.getValue().getNivel();

	    Pedido pedidoFiltro = new Pedido();
	    pedidoFiltro.setIdCodigo(arvoreHierarquicaEntry.getValue().getUsuario().getId_Codigo());
	    pedidoFiltro.setStatus(PedidoService.FINALIZADO);
	    pedidoFiltro.setTipo(PedidoService.ADESAO);

	    List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro);

	    if (Util.preenchido(pedidos)) {
		for (Pedido pedido : pedidos) {

		    PedidoDTO pedidoDTO = new PedidoService(hibernateUtil).calcularTotais(pedido);
		    BigDecimal valorTotal = pedidoDTO.getValorTotal();

		    if (valorTotal.compareTo(new BigDecimal("120")) != -1) {

			GregorianCalendar vinteDezembro2020 = new GregorianCalendar(2020, Calendar.DECEMBER, 20);
			if (pedido.getData().after(vinteDezembro2020)) {

			    BonusAdesao kitBonusDoUsuarioPrincipal = new BonusAdesaoService(hibernateUtil).buscarBonusAdesao(pedido.getData(), nivel, usuario.getNome_kit());

			    Usuario usuarioDoPedido = this.hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));
			    BonusAdesao kitBonusDoUsuarioDoPedido = new BonusAdesaoService(hibernateUtil).buscarBonusAdesao(pedido.getData(), nivel, usuarioDoPedido.getNome_kit());

			    if (Util.preenchido(kitBonusDoUsuarioPrincipal) && Util.preenchido(kitBonusDoUsuarioDoPedido)) {

				BigDecimal bonusAdesaoValor = null;

				if (kitBonusDoUsuarioPrincipal.getBonusAdesao().intValue() >= kitBonusDoUsuarioDoPedido.getBonusAdesao().intValue()) {
				    bonusAdesaoValor = kitBonusDoUsuarioDoPedido.getBonusAdesao();
				} else {
				    bonusAdesaoValor = kitBonusDoUsuarioPrincipal.getBonusAdesao();
				}

				extratos.add(new ExtratoDTO(usuarioDoPedido, pedido.getData(), bonusAdesaoValor, BÔNUS_DE_PRIMEIRA_COMPRA));
			    }
			}
		    }
		}
	    }
	}

	return extratos;
    }
}