package br.com.alabastrum.escritoriovirtual.cron;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import it.sauronsoftware.cron4j.Scheduler;

public class CancelarPedidosPendentes implements Runnable {

	public void run() {

		HibernateUtil hibernateUtil = new HibernateUtil();

		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, -3);

		Pedido pedidoFiltro = new Pedido();
		pedidoFiltro.setStatus("PENDENTE");
		pedidoFiltro.setCompleted(true);
		List<Pedido> pedidosPendentes = hibernateUtil.buscar(pedidoFiltro);

		for (Pedido pedidoPendente : pedidosPendentes) {

			if (pedidoPendente.getData().before(calendar)) {

				pedidoPendente.setStatus("CANCELADO");
				hibernateUtil.salvarOuAtualizar(pedidoPendente);
			}
		}

		hibernateUtil.fecharSessao();
	}

	public void iniciarRotina() {

		CancelarPedidosPendentes task = new CancelarPedidosPendentes();

		Scheduler scheduler = new Scheduler();

		scheduler.schedule("30 3 * * *", task);

		scheduler.start();
	}
}
