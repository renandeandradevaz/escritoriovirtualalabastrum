package br.com.alabastrum.escritoriovirtual.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.dto.PesquisaRelatorioHistoricoDeBonusDTO;
import br.com.alabastrum.escritoriovirtual.dto.ResultadoRelatorioHistoricoDeBonusDTO;
import br.com.alabastrum.escritoriovirtual.dto.SaldoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.ExtratoService;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RelatorioHistoricoDeBonusController {

    private Result result;
    private HibernateUtil hibernateUtil;

    public RelatorioHistoricoDeBonusController(Result result, HibernateUtil hibernateUtil) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
    }

    @Funcionalidade(administrativa = "true")
    public void acessarRelatorioHistoricoDeBonus() {
    }

    @Funcionalidade(administrativa = "true")
    public void pesquisarPedidos(PesquisaRelatorioHistoricoDeBonusDTO pesquisaRelatorioHistoricoDeBonusDTO) throws Exception {

	List<ResultadoRelatorioHistoricoDeBonusDTO> bonificacoes = new ArrayList<ResultadoRelatorioHistoricoDeBonusDTO>();

	if (Util.preenchido(pesquisaRelatorioHistoricoDeBonusDTO.getApelido())) {
	    Usuario usuario = new Usuario();
	    usuario.setApelido(pesquisaRelatorioHistoricoDeBonusDTO.getApelido());
	    usuario = this.hibernateUtil.selecionar(usuario);
	    gerarBonificacoes(bonificacoes, usuario, pesquisaRelatorioHistoricoDeBonusDTO);
	} else {
	    List<Usuario> usuarios = hibernateUtil.buscar(new Usuario());
	    for (Usuario usuario : usuarios) {
		gerarBonificacoes(bonificacoes, usuario, pesquisaRelatorioHistoricoDeBonusDTO);
	    }
	}

	bonificacoes = ordenarExtratoPorDataCrescente(bonificacoes);
    }

    private void gerarBonificacoes(List<ResultadoRelatorioHistoricoDeBonusDTO> bonificacoes, Usuario usuario, PesquisaRelatorioHistoricoDeBonusDTO pesquisaRelatorioHistoricoDeBonusDTO) throws Exception {

	SaldoDTO saldoDTO = new ExtratoService(hibernateUtil).gerarSaldoEExtrato(usuario.getId_Codigo(), Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH), Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR));

	for (ExtratoDTO extratoDTO : saldoDTO.getExtratoCompleto()) {

	    if (Util.vazio(pesquisaRelatorioHistoricoDeBonusDTO.getTipoDeBonus()) || extratoDTO.getDiscriminador().equals(pesquisaRelatorioHistoricoDeBonusDTO.getTipoDeBonus())) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date dataInicial = simpleDateFormat.parse(pesquisaRelatorioHistoricoDeBonusDTO.getDataInicial());
		Calendar dataInicialGregorianCalendar = new GregorianCalendar();
		dataInicialGregorianCalendar.setTime(dataInicial);
		Date dataFinal = simpleDateFormat.parse(pesquisaRelatorioHistoricoDeBonusDTO.getDataFinal());
		Calendar dataFinalGregorianCalendar = new GregorianCalendar();
		dataFinalGregorianCalendar.setTime(dataFinal);
		// dataFinalGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

		if (Util.vazio(pesquisaRelatorioHistoricoDeBonusDTO.getDataInicial()) || Util.vazio(pesquisaRelatorioHistoricoDeBonusDTO.getDataInicial()) || (extratoDTO.getData().after(dataInicialGregorianCalendar) && extratoDTO.getData().before(dataFinalGregorianCalendar))) {

		    ResultadoRelatorioHistoricoDeBonusDTO resultadoRelatorioHistoricoDeBonusDTO = new ResultadoRelatorioHistoricoDeBonusDTO();
		    resultadoRelatorioHistoricoDeBonusDTO.setExtratoDTO(extratoDTO);
		    resultadoRelatorioHistoricoDeBonusDTO.setUsuarioRecebedorDoBonus(usuario);
		    bonificacoes.add(resultadoRelatorioHistoricoDeBonusDTO);
		}
	    }
	}
    }

    private List<ResultadoRelatorioHistoricoDeBonusDTO> ordenarExtratoPorDataCrescente(List<ResultadoRelatorioHistoricoDeBonusDTO> bonificacoes) {

	Collections.sort(bonificacoes, new Comparator<ResultadoRelatorioHistoricoDeBonusDTO>() {

	    public int compare(ResultadoRelatorioHistoricoDeBonusDTO r1, ResultadoRelatorioHistoricoDeBonusDTO r2) {

		if (r1.getExtratoDTO().getData().getTimeInMillis() < r2.getExtratoDTO().getData().getTimeInMillis())
		    return -1;

		if (r1.getExtratoDTO().getData().getTimeInMillis() > r2.getExtratoDTO().getData().getTimeInMillis())
		    return 1;

		return 0;
	    }
	});

	return bonificacoes;
    }
}
