package br.com.alabastrum.escritoriovirtual.service;

import java.util.ArrayList;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Bonificacao;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class BonificacoesPreProcessadasService {

    private HibernateUtil hibernateUtil;

    public BonificacoesPreProcessadasService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
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
}