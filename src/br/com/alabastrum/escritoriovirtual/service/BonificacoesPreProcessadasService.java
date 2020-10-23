package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class BonificacoesPreProcessadasService {

    private HibernateUtil hibernateUtil;

    public BonificacoesPreProcessadasService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public List<Bonificacao> buscarBonificacoesNoMes(Integer codigo, String tipo, GregorianCalendar dataInicial, GregorianCalendar dataFinal) {

	List<Criterion> restricoes = new ArrayList<Criterion>();
	restricoes.add(Restrictions.between("data", dataInicial, dataFinal));
	Bonificacao filtro = new Bonificacao();
	filtro.setIdCodigo(codigo);
	filtro.setTipo(tipo);
	return hibernateUtil.buscar(filtro, restricoes);
    }

    public List<Bonificacao> buscarBonificacoesNoMes(String tipo, GregorianCalendar dataInicial, GregorianCalendar dataFinal) {

	List<Criterion> restricoes = new ArrayList<Criterion>();
	restricoes.add(Restrictions.between("data", dataInicial, dataFinal));
	Bonificacao filtro = new Bonificacao();
	filtro.setTipo(tipo);
	return hibernateUtil.buscar(filtro, restricoes);
    }

    public List<ExtratoDTO> obterBonificacoesPreProcessadas(Integer idCodigo) throws Exception {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	List<Bonificacao> bonificacoes = buscarBonificacoes(idCodigo);

	for (Bonificacao bonificacao : bonificacoes) {
	    extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(idCodigo)), bonificacao.getData(), bonificacao.getValor(), bonificacao.getTipo()));
	}

	return extratos;
    }

    private List<Bonificacao> buscarBonificacoes(Integer codigo) {

	Bonificacao filtro = new Bonificacao();
	filtro.setIdCodigo(codigo);
	return hibernateUtil.buscar(filtro);
    }

    public void salvarBonificacao(GregorianCalendar data, GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes, Integer idCodigo, BigDecimal bonus, String tipoDeBonus) {
	salvarBonificacao(data, primeiroDiaDoMes, ultimoDiaDoMes, idCodigo, bonus, tipoDeBonus, true);
    }

    public void salvarBonificacao(GregorianCalendar data, GregorianCalendar primeiroDiaDoMes, GregorianCalendar ultimoDiaDoMes, Integer idCodigo, BigDecimal bonus, String tipoDeBonus, boolean deletarAnteriores) {

	if (deletarAnteriores) {

	    List<Bonificacao> bonificacoes = buscarBonificacoesNoMes(idCodigo, tipoDeBonus, primeiroDiaDoMes, ultimoDiaDoMes);

	    if (Util.preenchido(bonificacoes)) {
		hibernateUtil.deletar(bonificacoes);
	    }
	}

	Bonificacao bonificacao = new Bonificacao();
	bonificacao.setIdCodigo(idCodigo);
	bonificacao.setData(data);
	bonificacao.setTipo(tipoDeBonus);
	bonificacao.setValor(bonus);
	hibernateUtil.salvarOuAtualizar(bonificacao);
    }

}