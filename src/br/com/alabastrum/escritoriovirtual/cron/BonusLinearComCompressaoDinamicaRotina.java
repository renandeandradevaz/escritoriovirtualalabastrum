package br.com.alabastrum.escritoriovirtual.cron;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.alabastrum.escritoriovirtual.service.BonificacoesPreProcessadasService;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.service.PontuacaoService;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
import it.sauronsoftware.cron4j.Scheduler;

public class BonusLinearComCompressaoDinamicaRotina implements Runnable {

    public void run() {

	HibernateUtil hibernateUtil = new HibernateUtil();

	try {

	    GregorianCalendar ontem = new GregorianCalendar();
	    ontem.add(Calendar.DATE, -1);
	    GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(ontem);
	    GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(ontem);

	    List<Usuario> usuarios = hibernateUtil.buscar(new Usuario());

	    for (Usuario usuario : usuarios) {

		List<Pontuacao> pontuacoes = new PontuacaoService(hibernateUtil).buscarPontuacoes(usuario.getId_Codigo(), primeiroDiaDoMes, ultimoDiaDoMes);

		if (Util.preenchido(pontuacoes)) {

		    List<Integer> arvoreHierarquicaAscendente = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAscendentePorIndicante(usuario.getId_Codigo());

		    for (Integer idIndicante : arvoreHierarquicaAscendente) {

			if (new AtividadeService(hibernateUtil).isAtivo(idIndicante, ontem)) {

			    for (Pontuacao pontuacao : pontuacoes) {

				if (pontuacao.getPntProduto().compareTo(BigDecimal.ZERO) > 0) {

				    new BonificacoesPreProcessadasService(hibernateUtil).salvarBonificacao(ontem, primeiroDiaDoMes, ultimoDiaDoMes, idIndicante, pontuacao.getPntProduto().multiply(new BigDecimal("0.1")), Bonificacao.BÔNUS_LINEAR);
				}
			    }

			    break;
			}
		    }
		}
	    }

	} catch (

	Exception e) {
	    e.printStackTrace();
	    String errorString = Util.getExceptionMessage(e);
	    Mail.enviarEmail("Exception ao rodar rotina de bonus linear com compressão dinamica", errorString);
	}
	hibernateUtil.fecharSessao();
    }

    public void iniciarRotina() {

	BonusLinearComCompressaoDinamicaRotina task = new BonusLinearComCompressaoDinamicaRotina();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("30 7 1 * *", task);

	scheduler.start();
    }
}
