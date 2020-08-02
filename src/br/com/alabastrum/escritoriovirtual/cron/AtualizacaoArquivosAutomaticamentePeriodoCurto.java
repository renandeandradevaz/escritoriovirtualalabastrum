package br.com.alabastrum.escritoriovirtual.cron;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.emailSender.BoasVindasEmailSender;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.alabastrum.escritoriovirtual.service.AtualizacaoArquivosService;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import it.sauronsoftware.cron4j.Scheduler;

public class AtualizacaoArquivosAutomaticamentePeriodoCurto implements Runnable {

    public void run() {

	List<String> pastaAtualizacaoCSV = Arrays.asList(new File(ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_CURTO).list());

	if (pastaAtualizacaoCSV.contains("tblRelacionamentos.csv")//
		&& pastaAtualizacaoCSV.contains("tblQualificacoes.csv")) {

	    try {
		HibernateUtil hibernateUtil = new HibernateUtil();
		new AtualizacaoArquivosService(hibernateUtil).processarArquivosPeriodoCurto();
		moverArquivos();
		BoasVindasEmailSender.enviarEmail();
		hibernateUtil.fecharSessao();
	    } catch (Exception e) {
		e.printStackTrace();
		Mail.enviarEmail("Erro ao processar arquivos", "Erro: " + e.getMessage());
	    }
	}
    }

    private void moverArquivos() {

	File folder = new File(ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_CURTO);

	for (File file : folder.listFiles()) {

	    file.renameTo(new File(ArquivoService.PASTA_BACKUP_CSV_PERIODO_CURTO + file.getName()));
	}
    }

    public void iniciarRotina() {

	AtualizacaoArquivosAutomaticamentePeriodoCurto task = new AtualizacaoArquivosAutomaticamentePeriodoCurto();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("*/5 * * * *", task);

	scheduler.start();
    }
}
