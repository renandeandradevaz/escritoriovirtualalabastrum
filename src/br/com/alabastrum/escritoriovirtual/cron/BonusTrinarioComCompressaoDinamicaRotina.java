package br.com.alabastrum.escritoriovirtual.cron;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroAtividade;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.alabastrum.escritoriovirtual.service.BonificacoesPreProcessadasService;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.service.ParametroAtividadeService;
import br.com.alabastrum.escritoriovirtual.service.PontuacaoService;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
import it.sauronsoftware.cron4j.Scheduler;

public class BonusTrinarioComCompressaoDinamicaRotina implements Runnable {

    public void run() {

	HibernateUtil hibernateUtil = new HibernateUtil();

	try {

	    GregorianCalendar ontem = new GregorianCalendar();
	    ontem.add(Calendar.DATE, -1);
	    GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(ontem);
	    GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(ontem);

	    List<Usuario> usuariosHabilitados = buscarUsuariosHabilitados(hibernateUtil, ontem);

	    for (Usuario usuario : usuariosHabilitados) {

		TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMap = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(usuario.getId_Codigo(), "id_lider");

		for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEntry : arvoreHierarquicaMap.entrySet()) {

		    List<Pontuacao> pontuacoes = new PontuacaoService(hibernateUtil).buscarPontuacoes(arvoreHierarquicaEntry.getKey(), primeiroDiaDoMes, ultimoDiaDoMes);

		    for (Pontuacao pontuacao : pontuacoes) {

			if (pontuacao.getPntAtividade().compareTo(BigDecimal.ONE) > 0) {

			    List<Integer> arvoreHierarquicaAscendente = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAscendente(arvoreHierarquicaEntry.getKey());

			    int nivelAscendente = 1;
			    for (Integer idLider : arvoreHierarquicaAscendente) {

				if (idLider.equals(usuario.getId_Codigo())) {

				    ParametroAtividade parametroAtividade = new ParametroAtividadeService(hibernateUtil).buscarParametroAtividade(pontuacao.getDt_Pontos(), nivelAscendente);

				    if (parametroAtividade != null) {
					new BonificacoesPreProcessadasService(hibernateUtil).salvarBonificacao(ontem, primeiroDiaDoMes, ultimoDiaDoMes, usuario.getId_Codigo(), parametroAtividade.getBonusAtividade(), Bonificacao.BÔNUS_TRINARIO);
				    }
				} else {
				    if (new AtividadeService(hibernateUtil).isAtivo(idLider, ontem)) {
					nivelAscendente++;
				    }
				}
			    }

			}

		    }
		}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    String errorString = Util.getExceptionMessage(e);
	    Mail.enviarEmail("Exception ao rodar rotina de bonus trinário com compressão dinamica", errorString);
	}
	hibernateUtil.fecharSessao();
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

	BonusTrinarioComCompressaoDinamicaRotina task = new BonusTrinarioComCompressaoDinamicaRotina();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("30 6 1 * *", task);

	scheduler.start();
    }
}
