package br.com.alabastrum.escritoriovirtual.cron;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.*;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.alabastrum.escritoriovirtual.service.BonificacoesPreProcessadasService;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.service.PontuacaoService;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
import it.sauronsoftware.cron4j.Scheduler;
import org.hibernate.criterion.Order;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

public class BonusMovimentacaoDeRedeRotina implements Runnable {

    private HibernateUtil hibernateUtil = null;

    public void run() {

	hibernateUtil = new HibernateUtil();

	try {

	    GregorianCalendar ontem = new GregorianCalendar();
	    ontem.add(Calendar.DATE, -1);
	    GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(ontem);
	    GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(ontem);

	    List<Usuario> usuariosHabilitados = buscarUsuariosHabilitados(ontem);

	    for (Usuario usuario : usuariosHabilitados) {

		Integer idCodigo = usuario.getId_Codigo();
		Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaCompletaPorIdLider = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(idCodigo, "id_lider");

	    }
	} catch (Exception e) {

	    e.printStackTrace();
	    String errorString = Util.getExceptionMessage(e);
	    Mail.enviarEmail("Exception ao rodar rotina de bonus movimentação de rede", errorString);

	}
	hibernateUtil.fecharSessao();
    }

    private List<Usuario> buscarUsuariosHabilitados(GregorianCalendar ontem) {

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

	BonusMovimentacaoDeRedeRotina task = new BonusMovimentacaoDeRedeRotina();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("30 8 1 * *", task);

	scheduler.start();
    }
}
