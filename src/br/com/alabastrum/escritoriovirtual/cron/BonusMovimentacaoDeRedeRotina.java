package br.com.alabastrum.escritoriovirtual.cron;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
import br.com.alabastrum.escritoriovirtual.modelo.MovimentacaoRede;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.*;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
import it.sauronsoftware.cron4j.Scheduler;

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
                BigDecimal totalBonificacao = BigDecimal.ZERO;

                Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaCompletaPorIdLider = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(idCodigo, "id_lider");
                for (Entry<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaEntry : arvoreHierarquicaCompletaPorIdLider.entrySet()) {
                    BigDecimal totalPedidosPessoais = BigDecimal.ZERO;
                    for (Pedido pedido : new PedidoService(hibernateUtil).getPedidosDoDistribuidor(arvoreHierarquicaEntry.getValue().getUsuario().getId_Codigo(), primeiroDiaDoMes, ultimoDiaDoMes)) {
                        totalPedidosPessoais = totalPedidosPessoais.add(new PedidoService(hibernateUtil).calcularTotalSemFrete(pedido));
                    }

                    MovimentacaoRede movimentacaoRede = new MovimentacaoDeRedeService(hibernateUtil).buscarMovimentacaoRede(ontem, arvoreHierarquicaEntry.getValue().getNivel());

                    if (movimentacaoRede != null) {
                        totalBonificacao = totalBonificacao.add(totalPedidosPessoais.multiply(movimentacaoRede.getPorcentagem()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                    }
                }
                if (totalBonificacao.intValue() > 0) {
                    new BonificacoesPreProcessadasService(hibernateUtil).salvarBonificacao(ontem, primeiroDiaDoMes, ultimoDiaDoMes, idCodigo, null, totalBonificacao, Bonificacao.BONUS_MOVIMENTACAO_DE_REDE);
                }
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
