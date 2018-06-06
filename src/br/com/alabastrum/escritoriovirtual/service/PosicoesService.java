package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;

import org.hibernate.criterion.MatchMode;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class PosicoesService {

	private HibernateUtil hibernateUtil;

	public PosicoesService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public String obterProximaPosicao(String posicaoAtual) {

		Posicao filtro = new Posicao();
		filtro.setNome(posicaoAtual);
		Posicao posicao = this.hibernateUtil.selecionar(filtro, MatchMode.EXACT);
		filtro = new Posicao();
		filtro.setPosicao(posicao.getPosicao().add(BigDecimal.ONE));
		Posicao proximaPosicao = this.hibernateUtil.selecionar(filtro);

		if (Util.preenchido(proximaPosicao)) {
			return proximaPosicao.getNome();
		}

		return "";
	}
}