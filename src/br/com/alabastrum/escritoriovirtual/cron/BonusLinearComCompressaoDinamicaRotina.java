package br.com.alabastrum.escritoriovirtual.cron;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.dto.GraduacaoMensalDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.alabastrum.escritoriovirtual.service.BonificacoesPreProcessadasService;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.service.PontuacaoService;
import br.com.alabastrum.escritoriovirtual.service.PosicoesService;
import br.com.alabastrum.escritoriovirtual.service.QualificacaoService;
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
	    
	    List<Usuario> usuariosHabilitados = buscarUsuariosHabilitados(hibernateUtil, ontem);
	    
	    
	    for(Usuario usuario: usuariosHabilitados) {
		
		TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaMap = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(usuario.getId_Codigo(), 1);

		for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEntry : arvoreHierarquicaMap.entrySet()) {

		    List<Pontuacao> pontuacoes = new PontuacaoService(hibernateUtil).buscarPontuacoes(arvoreHierarquicaEntry.getKey());

		    for (Pontuacao pontuacao : pontuacoes) {

			if (pontuacao.getPntProduto().compareTo(BigDecimal.ZERO) > 0) {

			//    extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(arvoreHierarquicaEntry.getKey())), pontuacao.getDt_Pontos(), pontuacao.getPntProduto().multiply(new BigDecimal("0.1")), BÔNUS_LINEAR));
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

	BonusLinearComCompressaoDinamicaRotina task = new BonusLinearComCompressaoDinamicaRotina();

	Scheduler scheduler = new Scheduler();

	scheduler.schedule("30 7 1 * *", task);

	scheduler.start();
    }
}
