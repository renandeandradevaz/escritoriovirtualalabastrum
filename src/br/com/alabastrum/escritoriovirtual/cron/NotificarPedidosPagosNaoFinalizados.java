package br.com.alabastrum.escritoriovirtual.cron;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import it.sauronsoftware.cron4j.Scheduler;

public class NotificarPedidosPagosNaoFinalizados implements Runnable {

	public void run() {

		HibernateUtil hibernateUtil = new HibernateUtil();

		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, -15);

		Pedido pedidoFiltro = new Pedido();
		pedidoFiltro.setStatus("PAGO");
		List<Pedido> pedidosPagosNaoFinalizados = hibernateUtil.buscar(pedidoFiltro);

		boolean enviarEmail = false;

		for (Pedido pedidoPagoNaoFinalizado : pedidosPagosNaoFinalizados) {
			if (pedidoPagoNaoFinalizado.getData().before(calendar)) {
				enviarEmail = true;
			}
		}

		if (enviarEmail) {
			String mensagem = "Existem pedidos pagos mas não finalizados com 15 dias ou mais de criação";
			try {
				Mail.enviarEmail(mensagem, mensagem);
			} catch (Exception e) {
			}
		}

		hibernateUtil.fecharSessao();
	}

	public void iniciarRotina() {

		NotificarPedidosPagosNaoFinalizados task = new NotificarPedidosPagosNaoFinalizados();

		Scheduler scheduler = new Scheduler();

		scheduler.schedule("0 6 * * *", task);

		scheduler.start();
	}
}
