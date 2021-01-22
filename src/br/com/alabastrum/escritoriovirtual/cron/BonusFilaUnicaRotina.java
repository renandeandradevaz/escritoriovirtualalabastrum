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
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.alabastrum.escritoriovirtual.service.BonificacoesPreProcessadasService;
import br.com.alabastrum.escritoriovirtual.service.PontuacaoService;
import br.com.alabastrum.escritoriovirtual.service.PosicoesService;
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

	    List<Usuario> usuariosHabilitados = buscarUsuariosHabilitados(hibernateUtil, ontem);
	    int quantidadeUsuariosHabilitados = usuariosHabilitados.size();

	    if (quantidadeUsuariosHabilitados > 0) {

		Integer pontuacaoFilaUnicaDoMes = new PontuacaoService(hibernateUtil).calcularPontuacaoFilaUnicaTodaEmpresa(primeiroDiaDoMes, ultimoDiaDoMes);
		Integer pontuacaoTotalNoMes = pontuacaoFilaUnicaDoMes;
		BigDecimal valorASerDivididoNoMes = new BigDecimal(pontuacaoTotalNoMes).multiply(new BigDecimal(0.05));
		BigDecimal valorCota = calcularCota(quantidadeUsuariosHabilitados, valorASerDivididoNoMes);

		for (int i = 0; i < usuariosHabilitados.size(); i++) {

		    Usuario usuarioHabilitado = usuariosHabilitados.get(i);
		    int quantidadeDeCotasDoUsuario = quantidadeUsuariosHabilitados - i;
		    BigDecimal valorParaPagamento = valorCota.multiply(new BigDecimal(quantidadeDeCotasDoUsuario));
		    new BonificacoesPreProcessadasService(hibernateUtil).salvarBonificacao(ontem, primeiroDiaDoMes, ultimoDiaDoMes, usuarioHabilitado.getId_Codigo(), null, valorParaPagamento, Bonificacao.BONUS_DE_FILA_UNICA);
		}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    String errorString = Util.getExceptionMessage(e);
	    Mail.enviarEmail("Exception ao rodar rotina de bonus de fila unica", errorString);
	}
	hibernateUtil.fecharSessao();
    }

    private BigDecimal calcularCota(int quantidadeUsuariosHabilitados, BigDecimal valorASerDivididoNoMesBigDecimal) {
	return new BigDecimal(((valorASerDivididoNoMesBigDecimal.doubleValue() / quantidadeUsuariosHabilitados) / (((double) quantidadeUsuariosHabilitados / 2) + 0.5)));
    }

    private List<Usuario> buscarUsuariosHabilitados(HibernateUtil hibernateUtil, GregorianCalendar ontem) {

	Integer pontuacaoMinima = new PosicoesService(hibernateUtil).obterPosicaoPorNome("BRONZE", ontem).getPontuacao();

	List<Usuario> usuariosHabilitados = new ArrayList<Usuario>();

	List<Usuario> usuarios = hibernateUtil.buscar(new Usuario());
	for (Usuario usuario : usuarios) {
	    if (new AtividadeService(hibernateUtil).isAtivo(usuario.getId_Codigo(), ontem) && new AtividadeService(hibernateUtil).possuiIndicadosDiretosAtivos(usuario.getId_Codigo(), ontem, 3)) {
		GraduacaoMensalDTO graduacaoMensal = new PontuacaoService(hibernateUtil).calcularGraduacaoMensal(usuario.getId_Codigo(), ontem);
		if (graduacaoMensal.getPontosAproveitados() >= pontuacaoMinima) {
		    usuariosHabilitados.add(usuario);
		}
	    }
	}
	Collections.sort(usuariosHabilitados, new Comparator<Usuario>() {
	    public int compare(Usuario usuario1, Usuario usuario2) {
		return usuario1.getFila_unica().compareTo(usuario2.getFila_unica());
	    }
	});

	return usuariosHabilitados;
    }

    public void iniciarRotina() {

	BonusFilaUnicaRotina task = new BonusFilaUnicaRotina();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("30 3 1 * *", task);

	scheduler.start();
    }
}
