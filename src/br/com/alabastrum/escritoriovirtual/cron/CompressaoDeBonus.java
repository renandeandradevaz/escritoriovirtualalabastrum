package br.com.alabastrum.escritoriovirtual.cron;

import it.sauronsoftware.cron4j.Scheduler;

public class CompressaoDeBonus implements Runnable {

	public void run() {

		System.out.println("rodou compressão de bonus");
	}

	public void iniciarRotina() {

		CompressaoDeBonus task = new CompressaoDeBonus();

		Scheduler scheduler = new Scheduler();

		// scheduler.schedule("30 4 1 * *", task);
		scheduler.schedule("23 13 16 * *", task);

		scheduler.start();
	}
}
