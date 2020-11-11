package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.PedidoDTO;
import br.com.alabastrum.escritoriovirtual.dto.PesquisaRelatorioPedidosDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.PedidoService;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RelatorioDePedidosController {

    private Result result;
    private HibernateUtil hibernateUtil;

    public RelatorioDePedidosController(Result result, HibernateUtil hibernateUtil) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
    }

    @Funcionalidade(administrativa = "true")
    public void acessarRelatorioDePedidos() {
	result.include("franquias", hibernateUtil.buscar(new Franquia()));
    }

    @Funcionalidade(administrativa = "true")
    public void pesquisarPedidos(PesquisaRelatorioPedidosDTO pesquisaRelatorioPedidosDTO) throws Exception {

	Pedido pedidoFiltro = new Pedido();
	pedidoFiltro.setCompleted(true);
	pedidoFiltro.setStatus(pesquisaRelatorioPedidosDTO.getStatus());

	if (Util.preenchido(pesquisaRelatorioPedidosDTO.getNumeroPedido())) {
	    pedidoFiltro.setId(Integer.valueOf(pesquisaRelatorioPedidosDTO.getNumeroPedido()));
	}

	if (Util.preenchido(pesquisaRelatorioPedidosDTO.getApelido())) {
	    Usuario usuario = new Usuario();
	    usuario.setApelido(pesquisaRelatorioPedidosDTO.getApelido());
	    usuario = this.hibernateUtil.selecionar(usuario);
	    if (usuario != null) {
		pedidoFiltro.setIdCodigo(usuario.getId_Codigo());
	    }
	}

	List<Criterion> restricoes = new ArrayList<Criterion>();

	if (Util.preenchido(pesquisaRelatorioPedidosDTO.getOrigem())) {

	    if (pesquisaRelatorioPedidosDTO.getOrigem().equals("internet")) {
		pedidoFiltro.setFormaDeEntrega("receberEmCasa");
	    } else {
		List<Integer> idFranquias = new ArrayList<Integer>();
		idFranquias.add(Integer.valueOf(pesquisaRelatorioPedidosDTO.getOrigem()));
		restricoes.add(Restrictions.in("idFranquia", idFranquias));
	    }
	}

	if (Util.preenchido(pesquisaRelatorioPedidosDTO.getDataInicial()) && Util.preenchido(pesquisaRelatorioPedidosDTO.getDataFinal())) {
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date dataInicial = simpleDateFormat.parse(pesquisaRelatorioPedidosDTO.getDataInicial());
	    Calendar dataInicialGregorianCalendar = new GregorianCalendar();
	    dataInicialGregorianCalendar.setTime(dataInicial);
	    Date dataFinal = simpleDateFormat.parse(pesquisaRelatorioPedidosDTO.getDataFinal());
	    Calendar dataFinalGregorianCalendar = new GregorianCalendar();
	    dataFinalGregorianCalendar.setTime(dataFinal);
	    dataFinalGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

	    restricoes.add(Restrictions.between("data", dataInicialGregorianCalendar, dataFinalGregorianCalendar));
	}

	if (pesquisaRelatorioPedidosDTO.getOrigem().equals("internet")) {
	    pedidoFiltro.setFormaDeEntrega("receberEmCasa");
	} else {
	    if (Util.preenchido(pesquisaRelatorioPedidosDTO.getOrigem())) {
		List<Integer> idFranquias = new ArrayList<Integer>();
		idFranquias.add(Integer.valueOf(pesquisaRelatorioPedidosDTO.getOrigem()));
		restricoes.add(Restrictions.in("idFranquia", idFranquias));
	    }
	}

	List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro, restricoes);
	List<PedidoDTO> pedidosDTO = new ArrayList<PedidoDTO>();

	BigDecimal total = BigDecimal.ZERO;
	for (Pedido pedido : pedidos) {
	    Usuario usuario = this.hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));
	    PedidoDTO pedidoDTO = new PedidoDTO(pedido, (Franquia) hibernateUtil.selecionar(new Franquia(pedido.getIdFranquia())), new PedidoService(hibernateUtil).calcularTotais(pedido).getValorTotal(), null, null, null, usuario);
	    pedidosDTO.add(pedidoDTO);
	    total = total.add(pedidoDTO.getValorTotal());
	}

	pedidosDTO = ordenar(pedidosDTO, pesquisaRelatorioPedidosDTO.getOrdenacao());

	result.include("pesquisa", true);
	result.include("pedidosDTO", pedidosDTO);
	result.include("pesquisaRelatorioPedidosDTO", pesquisaRelatorioPedidosDTO);
	result.include("quantidadeDePedidos", pedidosDTO.size());
	result.include("total", total);

	result.forwardTo(this).acessarRelatorioDePedidos();
    }

    private List<PedidoDTO> ordenar(List<PedidoDTO> pedidosDTO, String ordencacao) {

	if (ordencacao.equals("numero")) {

	    Collections.sort(pedidosDTO, new Comparator<PedidoDTO>() {

		public int compare(PedidoDTO p1, PedidoDTO p2) {

		    if (p1.getPedido().getId() < p2.getPedido().getId())
			return -1;

		    if (p1.getPedido().getId() > p2.getPedido().getId())
			return 1;

		    return 0;
		}
	    });
	}

	if (ordencacao.equals("valor")) {

	    Collections.sort(pedidosDTO, new Comparator<PedidoDTO>() {

		public int compare(PedidoDTO p1, PedidoDTO p2) {

		    if (p1.getValorTotal().intValue() < p2.getValorTotal().intValue())
			return -1;

		    if (p1.getValorTotal().intValue() > p2.getValorTotal().intValue())
			return 1;

		    return 0;
		}
	    });
	}

	if (ordencacao.equals("nome")) {
	    Collections.sort(pedidosDTO, new Comparator<PedidoDTO>() {
		public int compare(PedidoDTO p1, PedidoDTO p2) {
		    return p1.getDistribuidor().getvNome().compareTo(p2.getDistribuidor().getvNome());
		}
	    });
	}

	if (ordencacao.equals("formaDePagamento")) {
	    Collections.sort(pedidosDTO, new Comparator<PedidoDTO>() {
		public int compare(PedidoDTO p1, PedidoDTO p2) {
		    return p1.getPedido().getFormaDePagamento().compareTo(p2.getPedido().getFormaDePagamento());
		}
	    });
	}

	if (ordencacao.equals("data")) {

	    Collections.sort(pedidosDTO, new Comparator<PedidoDTO>() {

		public int compare(PedidoDTO p1, PedidoDTO p2) {

		    if (p1.getPedido().getData().getTimeInMillis() < p2.getPedido().getData().getTimeInMillis())
			return -1;

		    if (p1.getPedido().getData().getTimeInMillis() > p2.getPedido().getData().getTimeInMillis())
			return 1;

		    return 0;
		}
	    });
	}

	return pedidosDTO;
    }
}
