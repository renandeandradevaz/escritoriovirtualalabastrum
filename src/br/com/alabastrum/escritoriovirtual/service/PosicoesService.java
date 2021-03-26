package br.com.alabastrum.escritoriovirtual.service;

import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Posicao;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class PosicoesService {

    private HibernateUtil hibernateUtil;

    public PosicoesService(HibernateUtil hibernateUtil) {
	this.hibernateUtil = hibernateUtil;
    }

    public String obterNomeProximaPosicao(String posicaoAtual, GregorianCalendar data) {

	Posicao proximaPosicao = obterProximaPosicao(posicaoAtual, data);

	if (Util.preenchido(proximaPosicao)) {
	    return proximaPosicao.getNome();
	}

	return "";
    }

    public Posicao obterProximaPosicao(String posicaoAtual, GregorianCalendar data) {

	Posicao filtro = new Posicao();
	filtro.setNome(posicaoAtual);
	filtro.setData_referencia(Util.getPrimeiroDiaDoMes(data));
	Posicao posicao = this.hibernateUtil.selecionar(filtro, MatchMode.EXACT);
	filtro = new Posicao();
	filtro.setPosicao(posicao.getPosicao() + 1);
	filtro.setData_referencia(Util.getPrimeiroDiaDoMes(data));
	return this.hibernateUtil.selecionar(filtro);
    }

    public String obterNomeDaPosicao(Integer posicao, GregorianCalendar data) {

	Posicao filtro = new Posicao();
	filtro.setPosicao(posicao);
	filtro.setData_referencia(Util.getPrimeiroDiaDoMes(data));
	return ((Posicao) this.hibernateUtil.selecionar(filtro)).getNome();
    }

    public Posicao obterPosicaoPorOrdemNumerica(Integer posicao, GregorianCalendar data) {

	Posicao filtro = new Posicao();
	filtro.setPosicao(posicao);
	filtro.setData_referencia(Util.getPrimeiroDiaDoMes(data));
	return this.hibernateUtil.selecionar(filtro);
    }

    public Posicao obterPosicaoPorNome(String nome, GregorianCalendar data) {

	Posicao filtro = new Posicao();
	filtro.setNome(nome);
	filtro.setData_referencia(Util.getPrimeiroDiaDoMes(data));
	return this.hibernateUtil.selecionar(filtro);
    }

    public List<Posicao> obterPosicesNoMes(GregorianCalendar data) {
	Posicao filtro = new Posicao();
	filtro.setData_referencia(Util.getPrimeiroDiaDoMes(data));
	return this.hibernateUtil.buscar(filtro);
    }
}