package br.com.alabastrum.escritoriovirtual.cron;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.hibernate.criterion.Order;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
import br.com.alabastrum.escritoriovirtual.modelo.FaixaBonusAtividade;
import br.com.alabastrum.escritoriovirtual.modelo.KitAdesao;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.alabastrum.escritoriovirtual.service.BonificacoesPreProcessadasService;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.service.PontuacaoService;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
import it.sauronsoftware.cron4j.Scheduler;

public class BonusDeAtivacaoRotina implements Runnable {

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
		int distribuidoresDiretosAtivos = encontrarQuantidadeDeDistribuidoresDiretosAtivos(ontem, idCodigo);
		List<FaixaBonusAtividade> faixasBonusAtividadeValidas = encontrarFaixasBonusAtividadeValidas(idCodigo, primeiroDiaDoMes, distribuidoresDiretosAtivos, arvoreHierarquicaCompletaPorIdLider);
		Map<Integer, List<ArvoreHierarquicaDTO>> arvoreAgrupadaPorNivel = agruparArvorePorNivel(arvoreHierarquicaCompletaPorIdLider);

		if (faixasBonusAtividadeValidas.size() > 0) {

		    BigDecimal bonificacaoASerRecebida = BigDecimal.ZERO;

		    for (FaixaBonusAtividade faixaBonusAtividade : faixasBonusAtividadeValidas) {

			int index = faixasBonusAtividadeValidas.indexOf(faixaBonusAtividade) + 1;
			if (arvoreAgrupadaPorNivel.containsKey(index)) {

			    BigDecimal pontuacaoPorNivel = BigDecimal.ZERO;

			    List<ArvoreHierarquicaDTO> redePorIdLiderEPorNivelEspecifico = arvoreAgrupadaPorNivel.get(index);
			    for (ArvoreHierarquicaDTO distribuidorDaRede : redePorIdLiderEPorNivelEspecifico) {

				BigDecimal pontuacaoDoDistribuidor = calcularPontuacaoPorPessoa(primeiroDiaDoMes, ultimoDiaDoMes, distribuidorDaRede.getUsuario().getId_Codigo());
				pontuacaoPorNivel = pontuacaoPorNivel.add(pontuacaoDoDistribuidor);

				Map<Integer, ArvoreHierarquicaDTO> arvoreNivel1 = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(distribuidorDaRede.getUsuario().getId_Codigo(), 1);
				for (Entry<Integer, ArvoreHierarquicaDTO> arvoreEntry : arvoreNivel1.entrySet()) {
				    if (KitAdesao.AFILIADO.equalsIgnoreCase(arvoreEntry.getValue().getUsuario().getNome_kit())) {
					BigDecimal pontuacaoDoAfiliado = calcularPontuacaoPorPessoa(primeiroDiaDoMes, ultimoDiaDoMes, arvoreEntry.getValue().getUsuario().getId_Codigo());
					pontuacaoPorNivel = pontuacaoPorNivel.add(pontuacaoDoAfiliado);
				    }
				}
			    }

			    bonificacaoASerRecebida = bonificacaoASerRecebida.add(pontuacaoPorNivel.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).multiply(faixaBonusAtividade.getPercentual()));
			}
		    }

		    if (bonificacaoASerRecebida.intValue() > 0) {
			new BonificacoesPreProcessadasService(hibernateUtil).salvarBonificacao(ontem, primeiroDiaDoMes, ultimoDiaDoMes, idCodigo, null, bonificacaoASerRecebida, Bonificacao.BONUS_DE_ATIVACAO);
		    }
		}
	    }
	} catch (Exception e) {

	    e.printStackTrace();
	    String errorString = Util.getExceptionMessage(e);
	    Mail.enviarEmail("Exception ao rodar rotina de bonus de ativacao", errorString);

	}
	hibernateUtil.fecharSessao();
    }

    private BigDecimal calcularPontuacaoPorPessoa(GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes, Integer idCodigo) {

	BigDecimal pontuacaoPorPessoa = BigDecimal.ZERO;
	List<Pontuacao> pontuacoes = new PontuacaoService(hibernateUtil).buscarPontuacoes(idCodigo, primeiroDiaDoMes, ultimoDiaDoMes);
	for (Pontuacao pontuacao : pontuacoes) {
	    pontuacaoPorPessoa = pontuacaoPorPessoa.add(pontuacao.getPntProduto());
	}

	return pontuacaoPorPessoa;
    }

    private Map<Integer, List<ArvoreHierarquicaDTO>> agruparArvorePorNivel(Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaCompletaPorIdLider) {

	Map<Integer, List<ArvoreHierarquicaDTO>> arvoreAgrupadaPorNivel = new HashMap<Integer, List<ArvoreHierarquicaDTO>>();

	for (Entry<Integer, ArvoreHierarquicaDTO> arvoreEntry : arvoreHierarquicaCompletaPorIdLider.entrySet()) {
	    if (!arvoreAgrupadaPorNivel.containsKey(arvoreEntry.getValue().getNivel()))
		arvoreAgrupadaPorNivel.put(arvoreEntry.getValue().getNivel(), new ArrayList<ArvoreHierarquicaDTO>());

	    arvoreAgrupadaPorNivel.get(arvoreEntry.getValue().getNivel()).add(arvoreEntry.getValue());
	}

	return arvoreAgrupadaPorNivel;
    }

    private int encontrarQuantidadeDeDistribuidoresDiretosAtivos(GregorianCalendar ontem, Integer idCodigo) {

	int distribuidoresDiretosAtivos = 0;

	TreeMap<Integer, ArvoreHierarquicaDTO> arvore = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(idCodigo, 1);
	for (Entry<Integer, ArvoreHierarquicaDTO> distribuidorDiretoEntry : arvore.entrySet()) {

	    Usuario distribuidorDireto = distribuidorDiretoEntry.getValue().getUsuario();

	    if (KitAdesao.DISTRIBUIDOR.equalsIgnoreCase(distribuidorDireto.getNome_kit()) && new AtividadeService(hibernateUtil).isAtivo(distribuidorDireto.getId_Codigo(), ontem))
		distribuidoresDiretosAtivos++;
	}

	return distribuidoresDiretosAtivos;
    }

    private List<FaixaBonusAtividade> encontrarFaixasBonusAtividadeValidas(Integer codigo, GregorianCalendar data, int distribuidoresDiretosAtivos, Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaCompletaPorIdLider) {

	int tamanhoDaArvore = arvoreHierarquicaCompletaPorIdLider.size();

	FaixaBonusAtividade faixaBonusAtividade = new FaixaBonusAtividade();
	faixaBonusAtividade.setData_referencia(data);
	List<FaixaBonusAtividade> faixasBonusAtividade = this.hibernateUtil.buscar(faixaBonusAtividade, Order.asc("max_pessoas"));

	List<FaixaBonusAtividade> faixasBonusAtividadeValidas = new ArrayList<FaixaBonusAtividade>();

	for (FaixaBonusAtividade fba : faixasBonusAtividade) {
	    if (tamanhoDaArvore >= fba.getMin_pessoas().intValue() && distribuidoresDiretosAtivos >= fba.getAtivos_diretos().intValue()) {
		faixasBonusAtividadeValidas.add(fba);
	    }
	}

	return faixasBonusAtividadeValidas;
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

	BonusDeAtivacaoRotina task = new BonusDeAtivacaoRotina();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("30 8 1 * *", task);

	scheduler.start();
    }
}
