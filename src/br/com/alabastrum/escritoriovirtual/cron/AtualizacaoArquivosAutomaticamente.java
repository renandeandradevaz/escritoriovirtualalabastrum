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
				&& pastaAtualizacaoCSV.contains("tblPontuacao.csv")) {

			try {
				HibernateUtil hibernateUtil = new HibernateUtil();
				new AtualizacaoArquivosService(hibernateUtil).processarArquivos();
				apagarArquivos();
				BoasVindas.enviarEmail();
				hibernateUtil.fecharSessao();
			} catch (Exception e) {
			}
		}
	}

	private void apagarArquivos() {

		File folder = new File(ArquivoService.PASTA_ATUALIZACAO_CSV);

		File[] files = folder.listFiles();

		for (File file : files) {

			file.delete();
		}
	}

	public void iniciarRotina() {

		AtualizacaoArquivosAutomaticamente task = new AtualizacaoArquivosAutomaticamente();

		Scheduler scheduler = new Scheduler();

		scheduler.schedule("*/5 * * * *", task);

		scheduler.start();
	}
}
