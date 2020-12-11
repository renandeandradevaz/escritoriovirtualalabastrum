package br.com.alabastrum.escritoriovirtual.cron;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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

public class BonusGlobalRotina implements Runnable {

    private static final Integer QUANTIDADE_COTAS_PRATA = 1;
    private static final Integer QUANTIDADE_COTAS_OURO = 3;
    private static final Integer QUANTIDADE_COTAS_RUBI = 5;
    private static final Integer QUANTIDADE_COTAS_DIAMANTE = 10;

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

		int quantidadeCotas = 0;

		HashMap<Integer, String> posicoesAtuais = new HashMap<Integer, String>();

		for (int i = 0; i < usuariosHabilitados.size(); i++) {

		    Usuario usuarioHabilitado = usuariosHabilitados.get(i);
		    GraduacaoMensalDTO graduacaoMensal = new PontuacaoService(hibernateUtil).calcularGraduacaoMensal(usuarioHabilitado.getId_Codigo(), ontem);
		    posicoesAtuais.put(usuarioHabilitado.getId_Codigo(), graduacaoMensal.getPosicaoAtual());

		    if (graduacaoMensal.getPosicaoAtual().equalsIgnoreCase("prata")) {
			quantidadeCotas += QUANTIDADE_COTAS_PRATA;
		    }
		    if (graduacaoMensal.getPosicaoAtual().equalsIgnoreCase("ouro")) {
			quantidadeCotas += QUANTIDADE_COTAS_OURO;
		    }
		    if (graduacaoMensal.getPosicaoAtual().equalsIgnoreCase("rubi")) {
			quantidadeCotas += QUANTIDADE_COTAS_RUBI;
		    }
		    if (graduacaoMensal.getPosicaoAtual().equalsIgnoreCase("diamante")) {
			quantidadeCotas += QUANTIDADE_COTAS_DIAMANTE;
		    }
		}

		Integer pontuacaoGlobalCompletaDoMes = new PontuacaoService(hibernateUtil).calcularPontuacaoGlobalDeTodaEmpresa(primeiroDiaDoMes, ultimoDiaDoMes);
		BigDecimal valorASerDivididoNoMes = new BigDecimal(pontuacaoGlobalCompletaDoMes).multiply(new BigDecimal(0.15));

		BigDecimal valorCota = valorASerDivididoNoMes.divide(new BigDecimal(quantidadeCotas), 2, BigDecimal.ROUND_HALF_UP);

		for (Entry<Integer, String> posicaoAtualEntry : posicoesAtuais.entrySet()) {

		    Integer idCodigo = posicaoAtualEntry.getKey();
		    String posicaoAtual = posicaoAtualEntry.getValue();
		    int quantidadeDeCotasDoUsuario = 0;

		    if (posicaoAtual.equalsIgnoreCase("prata")) {
			quantidadeDeCotasDoUsuario = QUANTIDADE_COTAS_PRATA;
		    }
		    if (posicaoAtual.equalsIgnoreCase("ouro")) {
			quantidadeDeCotasDoUsuario = QUANTIDADE_COTAS_OURO;
		    }
		    if (posicaoAtual.equalsIgnoreCase("rubi")) {
			quantidadeDeCotasDoUsuario = QUANTIDADE_COTAS_RUBI;
		    }
		    if (posicaoAtual.equalsIgnoreCase("diamante")) {
			quantidadeDeCotasDoUsuario = QUANTIDADE_COTAS_DIAMANTE;
		    }

		    BigDecimal valorParaPagamento = valorCota.multiply(new BigDecimal(quantidadeDeCotasDoUsuario));
		    new BonificacoesPreProcessadasService(hibernateUtil).salvarBonificacao(ontem, primeiroDiaDoMes, ultimoDiaDoMes, idCodigo, null, valorParaPagamento, Bonificacao.BONUS_GLOBAL);
		}
	    }

	} catch (

	Exception e) {
	    e.printStackTrace();
	    String errorString = Util.getExceptionMessage(e);
	    Mail.enviarEmail("Exception ao rodar rotina de bonus global", errorString);
	}
	hibernateUtil.fecharSessao();
    }

    private List<Usuario> buscarUsuariosHabilitados(HibernateUtil hibernateUtil, GregorianCalendar ontem) {

	Integer pontuacaoMinima = new PosicoesService(hibernateUtil).obterPosicaoPorNome("PRATA", ontem).getPontuacao();

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

	return usuariosHabilitados;
    }

    public void iniciarRotina() {

	BonusGlobalRotina task = new BonusGlobalRotina();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("30 4 1 * *", task);

	scheduler.start();
    }
}
