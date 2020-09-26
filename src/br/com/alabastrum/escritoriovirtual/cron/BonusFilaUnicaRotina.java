package br.com.alabastrum.escritoriovirtual.cron;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.dto.GraduacaoMensalDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.alabastrum.escritoriovirtual.service.PontuacaoService;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
import it.sauronsoftware.cron4j.Scheduler;

public class BonusFilaUnicaRotina implements Runnable {

    public void run() {

	HibernateUtil hibernateUtil = new HibernateUtil();

	try {

	    GregorianCalendar ontem = new GregorianCalendar();
	    ontem.add(Calendar.DATE, -1);
	    GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(ontem);
	    GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(ontem);

	    Integer pontuacaoProdutoCompletaDoMes = new PontuacaoService(hibernateUtil).calcularPontuacaoDeProdutoTodaEmpresa(primeiroDiaDoMes, ultimoDiaDoMes);
	    Integer pontuacaoFilaUnicaDoMes = new PontuacaoService(hibernateUtil).calcularPontuacaoFilaUnicaTodaEmpresa(primeiroDiaDoMes, ultimoDiaDoMes);
	    Integer pontuacaoTotalNoMes = pontuacaoProdutoCompletaDoMes + pontuacaoFilaUnicaDoMes;

	    BigDecimal valorASerDivididoNoMes = new BigDecimal(pontuacaoTotalNoMes).multiply(new BigDecimal(0.12));

	    List<Usuario> usuariosHabilitados = new ArrayList<Usuario>();

	    List<Usuario> usuarios = hibernateUtil.buscar(new Usuario());
	    for (Usuario usuario : usuarios) {
		if (new AtividadeService(hibernateUtil).isAtivo(usuario.getId_Codigo(), ontem) && new AtividadeService(hibernateUtil).possuiIndicadosDiretosAtivos(usuario.getId_Codigo(), ontem, 3)) {
		    GraduacaoMensalDTO graduacaoMensal = new PontuacaoService(hibernateUtil).calcularGraduacaoMensal(usuario.getId_Codigo(), ontem);
		    if (graduacaoMensal.getPontosAproveitados() >= 2000) {
			usuariosHabilitados.add(usuario);
		    }
		}
	    }
	    Collections.sort(usuariosHabilitados, new Comparator<Usuario>() {
		public int compare(Usuario usuario1, Usuario usuario2) {
		    return usuario1.getFila_unica().compareTo(usuario2.getFila_unica());
		}
	    });
	    
	    int quantidadeUsuariosHbili

	} catch (Exception e) {
	    String errorString = Util.getExceptionMessage(e);
	    Mail.enviarEmail("Exception ao rodar rotina de bonus de fila unica", errorString);
	}
	hibernateUtil.fecharSessao();
    }

    public void iniciarRotina() {

	BonusFilaUnicaRotina task = new BonusFilaUnicaRotina();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("30 2 * * *", task);

	scheduler.start();
    }
}
