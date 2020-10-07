package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.SaldoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.ExtratoService;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class SaldoGeralController {

    private Result result;
    private HibernateUtil hibernateUtil;

    public SaldoGeralController(Result result, HibernateUtil hibernateUtil) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
    }

    @Funcionalidade(administrativa = "true")
    public void acessarTelaSaldoGeral() {

	result.include("mes", Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH));
	result.include("ano", Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR));
    }

    @Funcionalidade(administrativa = "true")
    public void gerarSaldoGeral(Integer mes, Integer ano) throws Exception {

	List<Usuario> usuarios = hibernateUtil.buscar(new Usuario());
	List<SaldoDTO> saldos = new ArrayList<SaldoDTO>();
	for (Usuario usuario : usuarios) {
	    SaldoDTO saldoDTO = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(usuario.getId_Codigo(), mes, ano);
	    saldoDTO.setUsuario(usuario);
	    if (saldoDTO.getSaldoLiberado().compareTo(BigDecimal.ZERO) > 0) {
		saldos.add(saldoDTO);
	    }
	}

	Collections.sort(saldos, new Comparator<SaldoDTO>() {

	    public int compare(SaldoDTO saldo1, SaldoDTO saldo2) {
		return saldo1.getUsuario().getvNome().compareTo(saldo2.getUsuario().getvNome());
	    }
	});

	result.include("saldos", saldos);
	result.include("mes", mes);
	result.include("ano", ano);
    }
}
