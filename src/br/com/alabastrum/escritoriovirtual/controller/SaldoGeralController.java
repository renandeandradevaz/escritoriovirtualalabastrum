package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
    }

    @Funcionalidade(administrativa = "true")
    public void gerarSaldoGeral() throws Exception {

	List<Usuario> usuarios = hibernateUtil.buscar(new Usuario());

	List<SaldoDTO> saldos = new ArrayList<SaldoDTO>();
	BigDecimal saldoPrevistoNoMesSomatorio = BigDecimal.ZERO;
	BigDecimal saldoPrevistoTotalSomatorio = BigDecimal.ZERO;
	BigDecimal saldoLiberadoSomatorio = BigDecimal.ZERO;

	for (Usuario usuario : usuarios) {

	    SaldoDTO saldoDTO = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(usuario.getId_Codigo(), Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR));
	    saldoDTO.setUsuario(usuario);

	    if (saldoDTO.getSaldoPrevistoTotal().compareTo(BigDecimal.ZERO) > 0) {

		saldos.add(saldoDTO);

		saldoPrevistoNoMesSomatorio = saldoPrevistoNoMesSomatorio.add(saldoDTO.getSaldoPrevistoNoMes());
		saldoPrevistoTotalSomatorio = saldoPrevistoTotalSomatorio.add(saldoDTO.getSaldoPrevistoTotal());
		saldoLiberadoSomatorio = saldoLiberadoSomatorio.add(saldoDTO.getSaldoLiberado());
	    }
	}

	result.include("saldos", saldos);
	result.include("saldoPrevistoNoMesSomatorio", saldoPrevistoNoMesSomatorio);
	result.include("saldoPrevistoTotalSomatorio", saldoPrevistoTotalSomatorio);
	result.include("saldoLiberadoSomatorio", saldoLiberadoSomatorio);
    }
}
