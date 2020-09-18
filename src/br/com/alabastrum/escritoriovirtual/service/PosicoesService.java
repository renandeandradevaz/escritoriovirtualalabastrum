package br.com.alabastrum.escritoriovirtual.service;

import org.hibernate.criterion.MatchMode;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class PosicoesService {

    private HibernateUtil hibernateUtil;

    public PosicoesService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public String obterNomeProximaPosicao(String posicaoAtual) {

	Posicao proximaPosicao = obterProximaPosicao(posicaoAtual);

	if (Util.preenchido(proximaPosicao)) {
	    return proximaPosicao.getNome();
	}

	return "";
    }

    public Posicao obterProximaPosicao(String posicaoAtual) {

	Posicao filtro = new Posicao();
	filtro.setNome(posicaoAtual);
	Posicao posicao = this.hibernateUtil.selecionar(filtro, MatchMode.EXACT);
	filtro = new Posicao();
	filtro.setPosicao(posicao.getPosicao() + 1);
	return this.hibernateUtil.selecionar(filtro);
    }

    public String obterNomeDaPosicao(Integer posicao) {

	Posicao filtro = new Posicao();
	filtro.setPosicao(posicao);
	return ((Posicao) this.hibernateUtil.selecionar(filtro)).getNome();
    }

    public Posicao obterPosicaoPorOrdemNumerica(Integer posicao) {

	Posicao filtro = new Posicao();
	filtro.setPosicao(posicao);
	return this.hibernateUtil.selecionar(filtro);
    }

    public Posicao obterPosicaoPorNome(String nome) {

	Posicao filtro = new Posicao();
	filtro.setNome(nome);
	return this.hibernateUtil.selecionar(filtro);
    }
}