package br.com.alabastrum.escritoriovirtual.cron;

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
import br.com.alabastrum.escritoriovirtual.dto.PedidoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
import br.com.alabastrum.escritoriovirtual.modelo.BonusAdesao;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.BonusAdesaoService;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.service.PedidoService;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
import it.sauronsoftware.cron4j.Scheduler;

public class BonusDePrimeiraCompraComKitsRotina implements Runnable {

    public static final String BÔNUS_DE_PRIMEIRA_COMPRA = "Bônus de primeira compra";

    public void run() {

	HibernateUtil hibernateUtil = new HibernateUtil();

	try {
	    GregorianCalendar ontem = new GregorianCalendar();
	    ontem.add(Calendar.DATE, -1);
	    GregorianCalendar ontemMeiaNoite = new GregorianCalendar(ontem.get(Calendar.YEAR), ontem.get(Calendar.MONTH), ontem.get(Calendar.DAY_OF_MONTH));

	    GregorianCalendar hoje = new GregorianCalendar();
	    GregorianCalendar hojeMeiaNoite = new GregorianCalendar(hoje.get(Calendar.YEAR), hoje.get(Calendar.MONTH), hoje.get(Calendar.DAY_OF_MONTH));

	    List<Usuario> usuarios = hibernateUtil.buscar(new Usuario());

	    for (Usuario usuario : usuarios) {

		TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMap = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(usuario.getId_Codigo());

		for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEntry : arvoreHierarquicaMap.entrySet()) {

		    int nivel = arvoreHierarquicaEntry.getValue().getNivel();

		    List<Criterion> restricoes = new ArrayList<Criterion>();
		    restricoes.add(Restrictions.between("data", ontemMeiaNoite, hojeMeiaNoite));

		    Pedido pedidoFiltro = new Pedido();
		    pedidoFiltro.setIdCodigo(arvoreHierarquicaEntry.getValue().getUsuario().getId_Codigo());
		    pedidoFiltro.setStatus(PedidoService.FINALIZADO);
		    pedidoFiltro.setTipo(PedidoService.ADESAO);

		    List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro, restricoes);

		    if (Util.preenchido(pedidos)) {
			for (Pedido pedido : pedidos) {

			    PedidoDTO pedidoDTO = new PedidoService(hibernateUtil).calcularTotais(pedido);
			    BigDecimal valorTotal = pedidoDTO.getValorTotal();

			    if (valorTotal.compareTo(new BigDecimal("120")) != -1) {

				BonusAdesao kitBonusDoUsuarioPrincipal = new BonusAdesaoService(hibernateUtil).buscarBonusAdesao(pedido.getData(), nivel, usuario.getNome_kit());

				Usuario usuarioDoPedido = hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));
				BonusAdesao kitBonusDoUsuarioDoPedido = new BonusAdesaoService(hibernateUtil).buscarBonusAdesao(pedido.getData(), nivel, usuarioDoPedido.getNome_kit());

				if (Util.preenchido(kitBonusDoUsuarioPrincipal) && Util.preenchido(kitBonusDoUsuarioDoPedido)) {

				    BigDecimal bonusAdesaoValor = null;

				    if (kitBonusDoUsuarioPrincipal.getBonusAdesao().intValue() >= kitBonusDoUsuarioDoPedido.getBonusAdesao().intValue()) {
					bonusAdesaoValor = kitBonusDoUsuarioDoPedido.getBonusAdesao();
				    } else {
					bonusAdesaoValor = kitBonusDoUsuarioPrincipal.getBonusAdesao();
				    }

				    Bonificacao bonificacao = new Bonificacao();
				    bonificacao.setIdCodigo(usuario.getId_Codigo());
				    bonificacao.setIdCodigoOrigemBonus(usuarioDoPedido.getId_Codigo());
				    bonificacao.setData(pedido.getData());
				    bonificacao.setTipo(BÔNUS_DE_PRIMEIRA_COMPRA);
				    bonificacao.setValor(bonusAdesaoValor);
				    hibernateUtil.salvarOuAtualizar(bonificacao);
				}
			    }
			}
		    }
		}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    String errorString = Util.getExceptionMessage(e);
	    Mail.enviarEmail("Exception ao rodar rotina de bonus de primeira compra com kits", errorString);
	}
	hibernateUtil.fecharSessao();
    }

    public void iniciarRotina() {

	BonusDePrimeiraCompraComKitsRotina task = new BonusDePrimeiraCompraComKitsRotina();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("30 8 * * *", task);

	scheduler.start();
    }
}
