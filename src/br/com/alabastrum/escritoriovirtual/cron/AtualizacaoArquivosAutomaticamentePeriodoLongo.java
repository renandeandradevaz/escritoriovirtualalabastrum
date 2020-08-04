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

public class AtualizacaoArquivosAutomaticamentePeriodoLongo implements Runnable {

    public void run() {

	List<String> pastaAtualizacaoCSV = Arrays.asList(new File(ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO).list());

	if (pastaAtualizacaoCSV.contains("tblPosicoes.csv") //
		&& pastaAtualizacaoCSV.contains("tblCDA.csv") //
		&& pastaAtualizacaoCSV.contains("tblCategorias.csv") //
		&& pastaAtualizacaoCSV.contains("tblProdutos.csv") //
		&& pastaAtualizacaoCSV.contains("tblDivisaoLucro.csv") //
		&& pastaAtualizacaoCSV.contains("tblReceitaDivisaoLucro.csv") //
		&& pastaAtualizacaoCSV.contains("tblVIP.csv") //
		&& pastaAtualizacaoCSV.contains("tblUnilevel.csv") //
		&& pastaAtualizacaoCSV.contains("tblAtividade.csv") //
		&& pastaAtualizacaoCSV.contains("tblPontuacao.csv")) {

	    try {
		HibernateUtil hibernateUtil = new HibernateUtil();
		new AtualizacaoArquivosService(hibernateUtil).processarArquivosPeriodoLongo();
		moverArquivos();
		hibernateUtil.fecharSessao();
	    } catch (Exception e) {
		e.printStackTrace();
		Mail.enviarEmail("Erro ao processar arquivos de período longo", "Erro: " + Util.getExceptionMessage(e));
	    }
	}
    }

    private void moverArquivos() {

	File folder = new File(ArquivoService.PASTA_ATUALIZACAO_CSV_PERIODO_LONGO);

	for (File file : folder.listFiles()) {

	    file.renameTo(new File(ArquivoService.PASTA_BACKUP_CSV_PERIODO_LONGO + file.getName()));
	}
    }

    public void iniciarRotina() {

	System.out.println(new Date() + ". Iniciando rotina AtualizacaoArquivosAutomaticamentePeriodoLongo");

	AtualizacaoArquivosAutomaticamentePeriodoLongo task = new AtualizacaoArquivosAutomaticamentePeriodoLongo();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("0 */4 * * *", task);

	scheduler.start();
    }
}
