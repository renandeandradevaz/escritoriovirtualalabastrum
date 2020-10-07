package br.com.alabastrum.escritoriovirtual.cron;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
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
	    ontem.add(Calendar.DATE, -10);
	    GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(ontem);
	    GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(ontem);

	    List<Qualificacao> qualificacoes = new QualificacaoService(hibernateUtil).obterQualificacoes(primeiroDiaDoMes, ultimoDiaDoMes);
	    HashMap<Integer, Integer> qualificadosNaoRepetidos = new HashMap<Integer, Integer>();

	    for (Qualificacao qualificacao : qualificacoes) {
		qualificadosNaoRepetidos.put(qualificacao.getId_Codigo(), 1);
	    }

	    for (Entry<Integer, Integer> qualificado : qualificadosNaoRepetidos.entrySet()) {
		Integer idCodigo = qualificado.getKey();
		Posicao posicao = new PosicoesService(hibernateUtil).obterPosicaoPorNome(new PontuacaoService(hibernateUtil).calcularGraduacaoMensal(idCodigo, ontem).getPosicaoAtual(), ontem);
		if (posicao.getBonusReconhecimento().intValue() > 0) {

		    List<Bonificacao> bonificacoes = new BonificacoesPreProcessadasService(hibernateUtil).buscarBonificacoesNoMes(idCodigo, Bonificacao.BONUS_DE_RECONHECIMENTO, primeiroDiaDoMes, ultimoDiaDoMes);

		    if (Util.preenchido(bonificacoes)) {
			hibernateUtil.deletar(bonificacoes);
		    }

		    Bonificacao bonificacao = new Bonificacao();
		    bonificacao.setIdCodigo(idCodigo);
		    bonificacao.setData(ontem);
		    bonificacao.setTipo(Bonificacao.BONUS_DE_RECONHECIMENTO);
		    bonificacao.setValor(posicao.getBonusReconhecimento());
		    hibernateUtil.salvarOuAtualizar(bonificacao);
		}
	    }
	} catch (

	Exception e) {
	    e.printStackTrace();
	    String errorString = Util.getExceptionMessage(e);
	    Mail.enviarEmail("Exception ao rodar rotina de bonus de reconhecimento e desempenho", errorString);
	}
	hibernateUtil.fecharSessao();
    }

    public static void main(String[] args) {

	new BonusReconhecimentoEDesempenhoRotina().run();

    }

    public void iniciarRotina() {

	BonusReconhecimentoEDesempenhoRotina task = new BonusReconhecimentoEDesempenhoRotina();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("30 5 1 * *", task);

	scheduler.start();
    }
}
