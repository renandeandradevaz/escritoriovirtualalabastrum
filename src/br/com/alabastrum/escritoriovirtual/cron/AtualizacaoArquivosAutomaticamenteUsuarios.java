package br.com.alabastrum.escritoriovirtual.cron;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.alabastrum.escritoriovirtual.service.AtualizacaoArquivosService;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
import it.sauronsoftware.cron4j.Scheduler;

public class AtualizacaoArquivosAutomaticamenteUsuarios implements Runnable {

    public void run() {

	List<String> pastaAtualizacaoCSV = Arrays.asList(new File(ArquivoService.PASTA_ATUALIZACAO_CSV_DISTRIBUIDOR).list());

	if (pastaAtualizacaoCSV.contains("tblRelacionamentos.csv")) {

	    try {
		HibernateUtil hibernateUtil = new HibernateUtil();
		new AtualizacaoArquivosService(hibernateUtil).processarArquivoAtualizacaoUsuario();
		moverArquivos();
		hibernateUtil.fecharSessao();
	    } catch (Exception e) {
		e.printStackTrace();
		Mail.enviarEmail("Erro ao processar arquivos de período curto", "Erro: " + Util.getExceptionMessage(e));
	    }
	}
    }

    private void moverArquivos() {

	File folder = new File(ArquivoService.PASTA_ATUALIZACAO_CSV_DISTRIBUIDOR);

	for (File file : folder.listFiles()) {

	    file.renameTo(new File(ArquivoService.PASTA_BACKUP_CSV_DISTRIBUIDOR + file.getName()));
	}
    }

    public void iniciarRotina() {

	System.out.println(new Date() + ". Iniciando rotina AtualizacaoArquivosAutomaticamenteUsuarios");

	AtualizacaoArquivosAutomaticamenteUsuarios task = new AtualizacaoArquivosAutomaticamenteUsuarios();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("*/30 * * * *", task);

	scheduler.start();
    }
}
