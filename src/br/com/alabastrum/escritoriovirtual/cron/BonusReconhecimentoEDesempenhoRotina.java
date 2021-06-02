package br.com.alabastrum.escritoriovirtual.cron;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.alabastrum.escritoriovirtual.dto.GraduacaoMensalDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.alabastrum.escritoriovirtual.service.BonificacoesPreProcessadasService;
import br.com.alabastrum.escritoriovirtual.service.PontuacaoService;
import br.com.alabastrum.escritoriovirtual.service.PosicoesService;
import br.com.alabastrum.escritoriovirtual.service.QualificacaoService;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
import it.sauronsoftware.cron4j.Scheduler;

public class BonusReconhecimentoEDesempenhoRotina implements Runnable {

    public void run() {

	HibernateUtil hibernateUtil = new HibernateUtil();

	try {

	    GregorianCalendar ontem = new GregorianCalendar();
	    ontem.add(Calendar.DATE, -2);
	    GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(ontem);
	    GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(ontem);

	    Map<Integer, Integer> usuariosQueReceberamBonusDeReconhecimento = processarBonusReconhecimento(hibernateUtil, ontem, primeiroDiaDoMes, ultimoDiaDoMes);
	    processarBonusDesempenho(hibernateUtil, ontem, primeiroDiaDoMes, ultimoDiaDoMes, usuariosQueReceberamBonusDeReconhecimento);
	} catch (

	Exception e) {
	    e.printStackTrace();
	    String errorString = Util.getExceptionMessage(e);
	    Mail.enviarEmail("Exception ao rodar rotina de bonus de reconhecimento e desempenho", errorString);
	}
	hibernateUtil.fecharSessao();
    }

    private Map<Integer, Integer> processarBonusReconhecimento(HibernateUtil hibernateUtil, GregorianCalendar ontem, GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes) {

	Map<Integer, Integer> usuariosQueReceberamBonusDeReconhecimento = new HashMap<Integer, Integer>();

	List<Qualificacao> qualificacoes = new QualificacaoService(hibernateUtil).obterQualificacoes(primeiroDiaDoMes, ultimoDiaDoMes);
	HashMap<Integer, Integer> qualificadosNaoRepetidos = new HashMap<Integer, Integer>();

	for (Qualificacao qualificacao : qualificacoes) {
	    qualificadosNaoRepetidos.put(qualificacao.getId_Codigo(), 1);
	}

	for (Entry<Integer, Integer> qualificado : qualificadosNaoRepetidos.entrySet()) {
	    Integer idCodigo = qualificado.getKey();
	    Posicao posicao = new PosicoesService(hibernateUtil).obterPosicaoPorNome(new PontuacaoService(hibernateUtil).calcularGraduacaoMensal(idCodigo, ontem).getPosicaoAtual(), ontem);
	    BigDecimal bonus = posicao.getBonusReconhecimento();
	    if (bonus.intValue() > 0) {
		new BonificacoesPreProcessadasService(hibernateUtil).salvarBonificacao(ontem, primeiroDiaDoMes, ultimoDiaDoMes, idCodigo, null, bonus, Bonificacao.BONUS_DE_RECONHECIMENTO);
		usuariosQueReceberamBonusDeReconhecimento.put(idCodigo, 1);
	    }
	}

	return usuariosQueReceberamBonusDeReconhecimento;
    }

    private void processarBonusDesempenho(HibernateUtil hibernateUtil, GregorianCalendar ontem, GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes, Map<Integer, Integer> usuariosQueReceberamBonusDeReconhecimento) {

	List<Usuario> usuariosHabilitados = buscarUsuariosHabilitadosBonusDesempenho(hibernateUtil, ontem);

	for (Usuario usuario : usuariosHabilitados) {

	    Integer idCodigo = usuario.getId_Codigo();

	    if (!usuariosQueReceberamBonusDeReconhecimento.containsKey(idCodigo)) {

		GraduacaoMensalDTO graduacaoMensal = new PontuacaoService(hibernateUtil).calcularGraduacaoMensal(idCodigo, ontem, true);
		String posicaoAtual = graduacaoMensal.getPosicaoAtual();

		if (usuario.getPosAtual().equalsIgnoreCase(posicaoAtual)) {

		    Posicao posicao = new PosicoesService(hibernateUtil).obterPosicaoPorNome(posicaoAtual, ontem);

		    BigDecimal bonus = posicao.getBonusDesempenho();
		    if (bonus.intValue() > 0) {
			new BonificacoesPreProcessadasService(hibernateUtil).salvarBonificacao(ontem, primeiroDiaDoMes, ultimoDiaDoMes, idCodigo, null, bonus, Bonificacao.BONUS_DE_DESEMPENHO);
		    }
		}
	    }
	}
    }

    private List<Usuario> buscarUsuariosHabilitadosBonusDesempenho(HibernateUtil hibernateUtil, GregorianCalendar ontem) {

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

	BonusReconhecimentoEDesempenhoRotina task = new BonusReconhecimentoEDesempenhoRotina();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("30 5 2 * *", task);

	scheduler.start();
    }
}
