package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.service.BonusAtivacaoService;
import br.com.alabastrum.escritoriovirtual.service.BonusUnilevelService;
import br.com.alabastrum.escritoriovirtual.service.IndicacaoDiretaService;
import br.com.alabastrum.escritoriovirtual.service.IndicacaoIndiretaService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ExtratoController {

	private Result result;
	private HibernateUtil hibernateUtil;
	private SessaoUsuario sessaoUsuario;

	public ExtratoController(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario) {

		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.sessaoUsuario = sessaoUsuario;
	}

	@Funcionalidade
	public void acessarTelaExtrato(Integer mes, Integer ano) {

		if (mes == null || ano == null) {
			mes = Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH);
			ano = Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR);
		}

		result.include("mes", mes);
		result.include("ano", ano);
	}

	@Funcionalidade
	public void gerarExtrato(Integer mes, Integer ano) {

		Integer idCodigo = this.sessaoUsuario.getUsuario().getId_Codigo();

		List<ExtratoDTO> extratoCompleto = new ArrayList<ExtratoDTO>();
		extratoCompleto.addAll(new IndicacaoDiretaService(hibernateUtil).obterIndicacoesDiretas(idCodigo));
		extratoCompleto.addAll(new IndicacaoIndiretaService(hibernateUtil).obterIndicacoesIndiretas(idCodigo));
		extratoCompleto.addAll(new BonusAtivacaoService(hibernateUtil).obterBonificacoesPorAtivacao(idCodigo));
		extratoCompleto.addAll(new BonusUnilevelService(hibernateUtil).obterBonificacoesUnilevel(idCodigo));
		extratoCompleto = ordenarExtratoPorDataCrescente(extratoCompleto);

		BigDecimal saldo = BigDecimal.ZERO;

		List<ExtratoDTO> extratoDoMes = new ArrayList<ExtratoDTO>();
		for (ExtratoDTO extratoDTO : extratoCompleto) {

			saldo = saldo.add(extratoDTO.getValor());

			if (extratoDTO.getValor().compareTo(BigDecimal.ZERO) > 0 && extratoDTO.getData().get(Calendar.MONTH) == mes && extratoDTO.getData().get(Calendar.YEAR) == ano) {
				extratoDTO.setSaldo(saldo);
				extratoDoMes.add(extratoDTO);
			}
		}

		result.include("saldo", saldo);
		result.include("extratoDoMes", extratoDoMes);
		result.forwardTo(this).acessarTelaExtrato(mes, ano);
	}

	private List<ExtratoDTO> ordenarExtratoPorDataCrescente(List<ExtratoDTO> extrato) {

		Collections.sort(extrato, new Comparator<ExtratoDTO>() {

			public int compare(ExtratoDTO e1, ExtratoDTO e2) {

				if (e1.getData().getTimeInMillis() < e2.getData().getTimeInMillis()) {
					return -1;
				}
				return 1;
			}
		});

		return extrato;
	}
}
