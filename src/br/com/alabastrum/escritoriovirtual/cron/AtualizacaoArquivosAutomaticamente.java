package br.com.alabastrum.escritoriovirtual.cron;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.emailSender.BoasVindas;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.alabastrum.escritoriovirtual.service.AtualizacaoArquivosService;
import it.sauronsoftware.cron4j.Scheduler;

public class AtualizacaoArquivosAutomaticamente implements Runnable {

	public void run() {

		List<String> pastaAtualizacaoCSV = Arrays.asList(new File(ArquivoService.PASTA_ATUALIZACAO_CSV).list());

		if (pastaAtualizacaoCSV.contains("tblRelacionamentos.csv") //
				&& pastaAtualizacaoCSV.contains("tblPosicoes.csv") //
				&& pastaAtualizacaoCSV.contains("tblQualificacoes.csv") //
				&& pastaAtualizacaoCSV.contains("tblCDA.csv") //
				&& pastaAtualizacaoCSV.contains("tblCategorias.csv") //
				&& pastaAtualizacaoCSV.contains("tblProdutos.csv") //
				&& pastaAtualizacaoCSV.contains("tblParametrosIngresso.csv") //
				&& pastaAtualizacaoCSV.contains("tblDivisaoLucro.csv") //
				&& pastaAtualizacaoCSV.contains("tblReceitaDivisaoLucro.csv") //
				&& pastaAtualizacaoCSV.contains("tblVIP.csv") //
				&& pastaAtualizacaoCSV.contains("tblUnilevel.csv") //
				&& pastaAtualizacaoCSV.contains("tblAtividade.csv") //
				&& pastaAtualizacaoCSV.contains("tblPontuacao.csv")) {

			try {
				HibernateUtil hibernateUtil = new HibernateUtil();
				new AtualizacaoArquivosService(hibernateUtil).processarArquivos();
				moverArquivos();
				BoasVindas.enviarEmail();
				hibernateUtil.fecharSessao();
			} catch (Exception e) {
			}
		}
	}

	private void moverArquivos() {

		File folder = new File(ArquivoService.PASTA_ATUALIZACAO_CSV);

		for (File file : folder.listFiles()) {

			file.renameTo(new File(ArquivoService.PASTA_BACKUP_CSV + file.getName()));
		}
	}

	public void iniciarRotina() {

		AtualizacaoArquivosAutomaticamente task = new AtualizacaoArquivosAutomaticamente();

		Scheduler scheduler = new Scheduler();

		scheduler.schedule("*/5 * * * *", task);

		scheduler.start();
	}
}
