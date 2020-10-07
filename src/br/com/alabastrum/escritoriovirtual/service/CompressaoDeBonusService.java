package br.com.alabastrum.escritoriovirtual.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Transferencia;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class CompressaoDeBonusService {

    private HibernateUtil hibernateUtil;

    public CompressaoDeBonusService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public void gerarCompressao() throws Exception {

	try {
	    GregorianCalendar mesPassado = Util.getTempoCorrenteAMeiaNoite();
	    mesPassado.add(Calendar.MONTH, -1);

	    List<Transferencia> transferencias = new ArrayList<Transferencia>();

	    List<Usuario> usuarios = hibernateUtil.buscar(new Usuario());

	    for (Usuario usuario : usuarios) {

		if (!new AtividadeService(hibernateUtil).isAtivo(usuario.getId_Codigo(), mesPassado)) {

		    // BigDecimal saldoPrevistoNoMes = new
		    // ExtratoService(hibernateUtil).gerarSaldoEExtrato(usuario.getId_Codigo(),
		    // Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH),
		    // Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR),
		    // true).getSaldoPrevistoNoMes();
		    BigDecimal saldoPrevistoNoMes = BigDecimal.ZERO;

		    if (saldoPrevistoNoMes.compareTo(BigDecimal.ZERO) > 0) {

			List<Integer> arvoreHierarquicaAscendente = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAscendente(usuario.getId_Codigo(), null);

			for (Integer idCodigoAscendente : arvoreHierarquicaAscendente) {

			    if (arvoreHierarquicaAscendente.indexOf(idCodigoAscendente) > 0 && new AtividadeService(hibernateUtil).isAtivo(idCodigoAscendente, mesPassado)) {

				Transferencia transferencia = new Transferencia();
				transferencia.setData(Util.getUltimoDiaDoMes(mesPassado));
				transferencia.setDe(usuario.getId_Codigo());
				transferencia.setPara(idCodigoAscendente);
				transferencia.setValor(saldoPrevistoNoMes);
				transferencia.setTipo(Transferencia.TRANSFERENCIA_POR_COMPRESSAO_DE_BONUS);
				transferencias.add(transferencia);
				break;
			    }
			}
		    }
		}
	    }

	    hibernateUtil.salvarOuAtualizar(transferencias);

	    Mail.enviarEmail("Compressão de bonus - Sucesso", transferencias.size() + " transferencias realizadas");

	} catch (Exception e) {

	    StringWriter stringWriter = new StringWriter();
	    e.printStackTrace(new PrintWriter(stringWriter));
	    String errorString = stringWriter.toString();

	    Mail.enviarEmail("Compressão de bonus - Erro!", errorString);
	}
    }
}