package br.com.alabastrum.escritoriovirtual.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.dto.PontosEquipeDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.service.HierarquiaService;
import br.com.alabastrum.escritoriovirtual.service.PontuacaoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoGeral;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class PontosDeEquipeController {

    private Result result;
    private HibernateUtil hibernateUtil;

    private SessaoGeral sessaoGeral;

    public PontosDeEquipeController(Result result, HibernateUtil hibernateUtil, SessaoGeral sessaoGeral) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
	this.sessaoGeral = sessaoGeral;
    }

    @Funcionalidade
    public void acessarTelaPontosDeEquipe() {

	result.include("mes", Util.getTempoCorrenteAMeiaNoite().get(Calendar.MONTH));
	result.include("ano", Util.getTempoCorrenteAMeiaNoite().get(Calendar.YEAR));
    }

    @Funcionalidade
    @Get("/pontosDeEquipe/gerarRelatorioPontosDeEquipe/{idCodigo}")
    public void gerarRelatorioPontosDeEquipe(Integer idCodigo) {
	gerarRelatorioPontosDeEquipe(idCodigo, null, null);
    }

    @Funcionalidade
    @Post("/pontosDeEquipe/gerarRelatorioPontosDeEquipe/{idCodigo}")
    public void gerarRelatorioPontosDeEquipe(Integer idCodigo, Integer mes, Integer ano) {

	if (mes == null) {
	    mes = (Integer) this.sessaoGeral.getValor("mes");
	}
	if (ano == null) {
	    ano = (Integer) this.sessaoGeral.getValor("ano");
	}

	this.sessaoGeral.adicionar("mes", mes);
	this.sessaoGeral.adicionar("ano", ano);

	GregorianCalendar data = Util.getTempoCorrenteAMeiaNoite();
	data.set(Calendar.MONTH, mes);
	data.set(Calendar.YEAR, ano);
	GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(data);
	GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(data);

	List<PontosEquipeDTO> pontosEquipeDTOLista = new ArrayList<PontosEquipeDTO>();

	Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaPrimeiroNivel = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaAteNivelEspecifico(idCodigo, 1);
	for (Entry<Integer, ArvoreHierarquicaDTO> usuarioEntry : arvoreHierarquicaPrimeiroNivel.entrySet()) {

	    Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquica = new HierarquiaService(hibernateUtil).obterArvoreHierarquicaTodosOsNiveis(usuarioEntry.getValue().getUsuario().getId_Codigo());

	    Integer volumeIndividualPagavel = new PontuacaoService(hibernateUtil).calcularPontuacaoDeProduto(primeiroDiaDoMes, ultimoDiaDoMes, usuarioEntry.getValue().getUsuario().getId_Codigo());
	    Integer volumeIndividualQualificavel = new PontuacaoService(hibernateUtil).calcularPontuacaoParaQualificacao(primeiroDiaDoMes, ultimoDiaDoMes, usuarioEntry.getValue().getUsuario().getId_Codigo());

	    Integer volumePagavelTodosOsNiveis = 0;
	    Integer volumeQualificavelTodosOsNiveis = 0;

	    for (Entry<Integer, ArvoreHierarquicaDTO> usuarioAbaixoEntry : arvoreHierarquica.entrySet()) {
		volumePagavelTodosOsNiveis += new PontuacaoService(hibernateUtil).calcularPontuacaoDeProduto(primeiroDiaDoMes, ultimoDiaDoMes, usuarioAbaixoEntry.getValue().getUsuario().getId_Codigo());
		volumeQualificavelTodosOsNiveis += new PontuacaoService(hibernateUtil).calcularPontuacaoParaQualificacao(primeiroDiaDoMes, ultimoDiaDoMes, usuarioAbaixoEntry.getValue().getUsuario().getId_Codigo());
	    }

	    volumePagavelTodosOsNiveis += volumeIndividualPagavel;
	    volumeQualificavelTodosOsNiveis += volumeIndividualQualificavel;

	    PontosEquipeDTO pontosEquipeDTO = new PontosEquipeDTO();
	    pontosEquipeDTO.setUsuario(usuarioEntry.getValue().getUsuario());
	    pontosEquipeDTO.setVolumeIndividualPagavel(volumeIndividualPagavel);
	    pontosEquipeDTO.setVolumeIndividualQualificavel(volumeIndividualQualificavel);
	    pontosEquipeDTO.setVolumePagavelTodosOsNiveis(volumePagavelTodosOsNiveis);
	    pontosEquipeDTO.setVolumeQualificavelTodosOsNiveis(volumeQualificavelTodosOsNiveis);

	    pontosEquipeDTOLista.add(pontosEquipeDTO);
	}

	result.include("pontosEquipeDTOLista", pontosEquipeDTOLista);
    }
}
