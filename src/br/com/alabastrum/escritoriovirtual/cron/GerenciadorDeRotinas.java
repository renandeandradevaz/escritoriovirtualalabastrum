package br.com.alabastrum.escritoriovirtual.cron;

import br.com.alabastrum.escritoriovirtual.hibernate.ThreadRestartHibernate;

public class GerenciadorDeRotinas {

	public static void iniciarRotinas() {

		ThreadRestartHibernate.iniciarThread();
		new AtualizacaoArquivosAutomaticamente().iniciarRotina();
		new CancelarPedidosPendentes().iniciarRotina();
		new CompressaoDeBonus().iniciarRotina();
	}
}
