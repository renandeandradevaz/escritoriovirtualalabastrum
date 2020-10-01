package br.com.alabastrum.escritoriovirtual.cron;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;

public class CompressaoDeBonus implements Runnable {

    public void run() {

	HibernateUtil hibernateUtil = new HibernateUtil();
	try {
	    // new CompressaoDeBonusService(hibernateUtil).gerarCompressao();
	} catch (Exception e) {
	}
	hibernateUtil.fecharSessao();
    }

    public void iniciarRotina() {

	// CompressaoDeBonus task = new CompressaoDeBonus();
	// Scheduler scheduler = new Scheduler();
	// scheduler.schedule("30 5 1 * *", task);
	// scheduler.start();
    }
}
