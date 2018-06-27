package br.com.alabastrum.escritoriovirtual.service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Pontuacao;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class AtividadeService {

	private HibernateUtil hibernateUtil;

	public AtividadeService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public boolean isAtivo(Integer codigo) {

		GregorianCalendar primeiroDiaDoMes = Util.getTempoCorrenteAMeiaNoite();
		primeiroDiaDoMes.set(Calendar.DAY_OF_MONTH, 1);

		GregorianCalendar ultimoDiaDoMes = Util.getTempoCorrenteAMeiaNoite();
		ultimoDiaDoMes.set(Calendar.DAY_OF_MONTH, ultimoDiaDoMes.getActualMaximum(Calendar.DAY_OF_MONTH));

		return isAtivo(codigo, primeiroDiaDoMes, ultimoDiaDoMes);
	}

	public boolean isAtivo(Integer codigo, GregorianCalendar dataInicial, GregorianCalendar dataFinal) {

		List<Pontuacao> pontuacoes = new PontuacaoService(hibernateUtil).buscarPontuacoes(codigo, dataInicial, dataFinal);

		if (pontuacoes.size() > 0) {

			Integer parametroAtividade = pontuacoes.get(0).getParametroAtividade().intValue();
			Integer somaPontosAtividade = 0;

			for (Pontuacao pontuacao : pontuacoes) {
				somaPontosAtividade += pontuacao.getPntAtividade().intValue();
			}

			if (somaPontosAtividade >= parametroAtividade) {
				return true;
			}
		}

		return false;
	}
}
