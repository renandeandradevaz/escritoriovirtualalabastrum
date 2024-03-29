package br.com.alabastrum.escritoriovirtual.cron;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.service.PedidoService;
import it.sauronsoftware.cron4j.Scheduler;

public class CancelarPedidosPendentes implements Runnable {

    public void run() {

	HibernateUtil hibernateUtil = new HibernateUtil();

	Calendar calendar = new GregorianCalendar();
	calendar.add(Calendar.DATE, -7);

	Pedido pedidoFiltro = new Pedido();
	pedidoFiltro.setStatus("PENDENTE");
	pedidoFiltro.setCompleted(true);
	List<Pedido> pedidosPendentes = hibernateUtil.buscar(pedidoFiltro);

	for (Pedido pedidoPendente : pedidosPendentes) {

	    if (pedidoPendente.getData().before(calendar)) {

		new PedidoService(hibernateUtil).cancelarPedido(pedidoPendente);

		pedidoPendente.setStatus("CANCELADO");
		hibernateUtil.salvarOuAtualizar(pedidoPendente);
	    }
	}

	hibernateUtil.fecharSessao();
    }

    public void iniciarRotina() {

	CancelarPedidosPendentes task = new CancelarPedidosPendentes();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("30 1 * * *", task);

	scheduler.start();
    }
}
