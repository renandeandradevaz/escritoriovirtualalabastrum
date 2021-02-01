package br.com.alabastrum.escritoriovirtual.cron;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.KitAdesao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
import it.sauronsoftware.cron4j.Scheduler;

public class BonusDeAtivacaoRotina implements Runnable {

    public void run() {

	HibernateUtil hibernateUtil = new HibernateUtil();

	try {

	    GregorianCalendar ontem = new GregorianCalendar();
	    ontem.add(Calendar.DATE, -1);
	    // GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(ontem);
	    // GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(ontem);

	    List<Usuario> usuariosHabilitados = buscarUsuariosHabilitados(hibernateUtil, ontem);

	    for (Usuario usuario : usuariosHabilitados) {

		int distribuidoresDiretosAtivos = encontrarQuantidadeDeDistribuidoresDiretosAtivos(hibernateUtil, ontem, usuario);

		System.out.println(distribuidoresDiretosAtivos);
		
		
	    }

	} catch (Exception e) {

	    e.printStackTrace();
	    String errorString = Util.getExceptionMessage(e);
	    Mail.enviarEmail("Exception ao rodar rotina de bonus de ativacao", errorString);

	}
	hibernateUtil.fecharSessao();
    }

    private int encontrarQuantidadeDeDistribuidoresDiretosAtivos(HibernateUtil hibernateUtil, GregorianCalendar ontem, Usuario usuario) {

	int distribuidoresDiretosAtivos = 0;

	TreeMap<Integer, ArvoreHierarquicaDTO> arvore = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(usuario.getId_Codigo(), 1);
	for (Entry<Integer, ArvoreHierarquicaDTO> distribuidorDiretoEntry : arvore.entrySet()) {

	    Usuario distribuidorDireto = distribuidorDiretoEntry.getValue().getUsuario();

	    if (KitAdesao.DISTRIBUIDOR.equalsIgnoreCase(distribuidorDireto.getNome_kit()) && new AtividadeService(hibernateUtil).isAtivo(distribuidorDireto.getId_Codigo(), ontem))
		distribuidoresDiretosAtivos++;
	}

	return distribuidoresDiretosAtivos;
    }

    private List<Usuario> buscarUsuariosHabilitados(HibernateUtil hibernateUtil, GregorianCalendar ontem) {

	List<Usuario> usuariosHabilitados = new ArrayList<Usuario>();

	List<Usuario> usuarios = hibernateUtil.buscar(new Usuario());
	for (Usuario usuario : usuarios) {
	    if (new AtividadeService(hibernateUtil).isAtivo(usuario.getId_Codigo(), ontem)) {
		usuariosHabilitados.add(usuario);
	    }
	}

	return usuariosHabilitados;
    }

    public void iniciarRotina() {

	BonusDeAtivacaoRotina task = new BonusDeAtivacaoRotina();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("30 8 1 * *", task);

	scheduler.start();
    }
}
