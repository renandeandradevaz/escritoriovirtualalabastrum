package br.com.alabastrum.escritoriovirtual.cron;

import br.com.alabastrum.escritoriovirtual.hibernate.ThreadRestartHibernate;

public class GerenciadorDeRotinas {

    private static boolean rotinasIniciadas = false;

    public static void iniciarRotinas() {

	if (!rotinasIniciadas) {

	    rotinasIniciadas = true;

	    ThreadRestartHibernate.iniciarThread();
	    new AtualizacaoArquivosAutomaticamentePeriodoLongo().iniciarRotina();
	    new AtualizacaoArquivosAutomaticamentePeriodoCurto().iniciarRotina();
	    new AtualizacaoArquivosAutomaticamenteUsuarios().iniciarRotina();
	    new CancelarPedidosPendentes().iniciarRotina();
	    //new CompressaoDeBonus().iniciarRotina();
	    new NotificarPedidosPagosNaoFinalizados().iniciarRotina();
	    new BonusFilaUnicaRotina().iniciarRotina();
	    new BonusGlobalRotina().iniciarRotina();
	    //new BonusReconhecimentoEDesempenhoRotina().iniciarRotina();
	    //new BonusTrinarioComCompressaoDinamicaRotina().iniciarRotina();
	    //new BonusLinearComCompressaoDinamicaRotina().iniciarRotina();
	    //new BonusDePrimeiraCompraComKitsRotina().iniciarRotina();
	}
    }
}
