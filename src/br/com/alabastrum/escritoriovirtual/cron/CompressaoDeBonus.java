package br.com.alabastrum.escritoriovirtual.cron;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.service.CompressaoDeBonusService;
import it.sauronsoftware.cron4j.Scheduler;

public class CompressaoDeBonus implements Runnable {

	public void run() {

		HibernateUtil hibernateUtil = new HibernateUtil();
		try {
			new CompressaoDeBonusService(hibernateUtil).gerarCompressao();
		} catch (Exception e) {
		}
		hibernateUtil.fecharSessao();
	}

	public void iniciarRotina() {

		CompressaoDeBonus task = new CompressaoDeBonus();
		Scheduler scheduler = new Scheduler();
		scheduler.schedule("30 4 1 * *", task);
		scheduler.start();
	}
}
