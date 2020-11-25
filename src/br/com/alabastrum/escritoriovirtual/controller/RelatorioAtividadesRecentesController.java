package br.com.alabastrum.escritoriovirtual.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.PesquisaRelatorioAtividadesRecentesDTO;
import br.com.alabastrum.escritoriovirtual.dto.ResultadoRelatorioAtividadesRecentesDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.PedidoFranquia;
import br.com.alabastrum.escritoriovirtual.modelo.Qualificacao;
import br.com.alabastrum.escritoriovirtual.modelo.SolicitacaoSaque;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.PedidoService;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RelatorioAtividadesRecentesController {

    private Result result;
    private HibernateUtil hibernateUtil;

    public RelatorioAtividadesRecentesController(Result result, HibernateUtil hibernateUtil) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
    }

    @Funcionalidade(administrativa = "true")
    public void acessarRelatorioAtividadesRecentes() {
    }

    @Funcionalidade(administrativa = "true")
    public void pesquisarAtividadesRecentes(PesquisaRelatorioAtividadesRecentesDTO pesquisaRelatorioAtividadesRecentesDTO) throws Exception {

	List<ResultadoRelatorioAtividadesRecentesDTO> atividadesRecentes = new ArrayList<ResultadoRelatorioAtividadesRecentesDTO>();

	if (pesquisaRelatorioAtividadesRecentesDTO.getTipoDeAtividade().equals("Novo cadastro")) {

	    Usuario usuarioFiltro = new Usuario();
	    usuarioFiltro.setCadPreCadastro(1);
	    List<Usuario> usuarios = this.hibernateUtil.buscar(usuarioFiltro);

	    for (Usuario usuario : usuarios) {
		Qualificacao qualificacaoFiltro = new Qualificacao();
		qualificacaoFiltro.setId_Codigo(usuario.getId_Codigo());
		List<Qualificacao> qualificacoes = this.hibernateUtil.buscar(qualificacaoFiltro);
		if (qualificacoes.size() == 1) {

		    Calendar dataInicialGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataInicial());
		    Calendar dataFinalGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataFinal());
		    dataInicialGregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
		    dataFinalGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

		    if (qualificacoes.get(0).getData().after(dataInicialGregorianCalendar) && qualificacoes.get(0).getData().before(dataFinalGregorianCalendar)) {

			adicionarALista(atividadesRecentes, usuario.getApelido() + " - " + usuario.getvNome(), "Novo cadastro", qualificacoes.get(0).getData(), usuario.getId_Indicante());
		    }
		}
	    }
	}

	if (pesquisaRelatorioAtividadesRecentesDTO.getTipoDeAtividade().equals("Adesão finalizada")) {

	    Pedido pedidoFiltro = new Pedido();
	    pedidoFiltro.setCompleted(true);
	    pedidoFiltro.setStatus("FINALIZADO");
	    pedidoFiltro.setTipo(PedidoService.ADESAO);

	    Calendar dataInicialGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataInicial());
	    Calendar dataFinalGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataFinal());
	    dataFinalGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

	    List<Criterion> restricoes = new ArrayList<Criterion>();
	    restricoes.add(Restrictions.between("data", dataInicialGregorianCalendar, dataFinalGregorianCalendar));

	    List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro, restricoes);
	    for (Pedido pedido : pedidos) {
		Usuario usuario = this.hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));
		adicionarALista(atividadesRecentes, usuario.getApelido() + " - " + usuario.getvNome(), "Adesão finalizada", pedido.getData());
	    }
	}

	if (pesquisaRelatorioAtividadesRecentesDTO.getTipoDeAtividade().equals("Pedido finalizado")) {

	    Pedido pedidoFiltro = new Pedido();
	    pedidoFiltro.setCompleted(true);
	    pedidoFiltro.setStatus("FINALIZADO");

	    Calendar dataInicialGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataInicial());
	    Calendar dataFinalGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataFinal());
	    dataFinalGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

	    List<Criterion> restricoes = new ArrayList<Criterion>();
	    restricoes.add(Restrictions.between("data", dataInicialGregorianCalendar, dataFinalGregorianCalendar));

	    List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro, restricoes);
	    for (Pedido pedido : pedidos) {
		Usuario usuario = this.hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));
		adicionarALista(atividadesRecentes, usuario.getApelido() + " - " + usuario.getvNome(), "Pedido finalizado", pedido.getData());
	    }
	}

	if (pesquisaRelatorioAtividadesRecentesDTO.getTipoDeAtividade().equals("Pedido pendente")) {

	    Pedido pedidoFiltro = new Pedido();
	    pedidoFiltro.setCompleted(true);
	    pedidoFiltro.setStatus("PENDENTE");

	    Calendar dataInicialGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataInicial());
	    Calendar dataFinalGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataFinal());
	    dataFinalGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

	    List<Criterion> restricoes = new ArrayList<Criterion>();
	    restricoes.add(Restrictions.between("data", dataInicialGregorianCalendar, dataFinalGregorianCalendar));

	    List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro, restricoes);
	    for (Pedido pedido : pedidos) {
		Usuario usuario = this.hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));
		adicionarALista(atividadesRecentes, usuario.getApelido() + " - " + usuario.getvNome(), "Pedido pendente", pedido.getData());
	    }
	}

	if (pesquisaRelatorioAtividadesRecentesDTO.getTipoDeAtividade().equals("Pedido cancelado")) {

	    Pedido pedidoFiltro = new Pedido();
	    pedidoFiltro.setCompleted(true);
	    pedidoFiltro.setStatus("CANCELADO");

	    Calendar dataInicialGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataInicial());
	    Calendar dataFinalGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataFinal());
	    dataFinalGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

	    List<Criterion> restricoes = new ArrayList<Criterion>();
	    restricoes.add(Restrictions.between("data", dataInicialGregorianCalendar, dataFinalGregorianCalendar));

	    List<Pedido> pedidos = hibernateUtil.buscar(pedidoFiltro, restricoes);
	    for (Pedido pedido : pedidos) {
		Usuario usuario = this.hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo()));
		adicionarALista(atividadesRecentes, usuario.getApelido() + " - " + usuario.getvNome(), "Pedido cancelado", pedido.getData());
	    }
	}

	if (pesquisaRelatorioAtividadesRecentesDTO.getTipoDeAtividade().equals("Pedido de PA pago")) {

	    PedidoFranquia filtro = new PedidoFranquia();
	    filtro.setStatus("ENTREGUE");

	    Calendar dataInicialGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataInicial());
	    Calendar dataFinalGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataFinal());
	    dataFinalGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

	    List<Criterion> restricoes = new ArrayList<Criterion>();
	    restricoes.add(Restrictions.between("data", dataInicialGregorianCalendar, dataFinalGregorianCalendar));

	    List<PedidoFranquia> pedidos = hibernateUtil.buscar(filtro, restricoes);
	    for (PedidoFranquia pedido : pedidos) {
		Franquia franquia = this.hibernateUtil.selecionar(new Franquia(pedido.getIdFranquia()));
		adicionarALista(atividadesRecentes, franquia.getEstqNome(), "Pedido de PA pago", pedido.getData());
	    }
	}

	if (pesquisaRelatorioAtividadesRecentesDTO.getTipoDeAtividade().equals("Pedido de PA pendente")) {

	    PedidoFranquia filtro = new PedidoFranquia();
	    filtro.setStatus("PENDENTE");

	    Calendar dataInicialGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataInicial());
	    Calendar dataFinalGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataFinal());
	    dataFinalGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

	    List<Criterion> restricoes = new ArrayList<Criterion>();
	    restricoes.add(Restrictions.between("data", dataInicialGregorianCalendar, dataFinalGregorianCalendar));

	    List<PedidoFranquia> pedidos = hibernateUtil.buscar(filtro, restricoes);
	    for (PedidoFranquia pedido : pedidos) {
		Franquia franquia = this.hibernateUtil.selecionar(new Franquia(pedido.getIdFranquia()));
		adicionarALista(atividadesRecentes, franquia.getEstqNome(), "Pedido de PA pendente", pedido.getData());
	    }
	}

	if (pesquisaRelatorioAtividadesRecentesDTO.getTipoDeAtividade().equals("Solicitação de saque")) {

	    SolicitacaoSaque solicitacaoSaqueFiltro = new SolicitacaoSaque();
	    solicitacaoSaqueFiltro.setStatus("PENDENTE");

	    Calendar dataInicialGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataInicial());
	    Calendar dataFinalGregorianCalendar = Util.getDateByString(pesquisaRelatorioAtividadesRecentesDTO.getDataFinal());
	    dataFinalGregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

	    List<Criterion> restricoes = new ArrayList<Criterion>();
	    restricoes.add(Restrictions.between("data", dataInicialGregorianCalendar, dataFinalGregorianCalendar));

	    List<SolicitacaoSaque> solicitacoes = hibernateUtil.buscar(solicitacaoSaqueFiltro, restricoes);
	    for (SolicitacaoSaque solicitacaoSaque : solicitacoes) {
		Usuario usuario = this.hibernateUtil.selecionar(new Usuario(solicitacaoSaque.getIdCodigo()));
		adicionarALista(atividadesRecentes, usuario.getApelido() + " - " + usuario.getvNome(), "Solicitação de saque", solicitacaoSaque.getData());
	    }
	}

	result.include("atividadesRecentes", ordenarPorDataCrescente(atividadesRecentes));
	result.include("pesquisaRelatorioAtividadesRecentesDTO", pesquisaRelatorioAtividadesRecentesDTO);
	result.include("pesquisa", true);

	result.forwardTo(this).acessarRelatorioAtividadesRecentes();
    }

    private void adicionarALista(List<ResultadoRelatorioAtividadesRecentesDTO> atividadesRecentes, String identificador, String tipoDeAtividade, GregorianCalendar data) {

	adicionarALista(atividadesRecentes, identificador, tipoDeAtividade, data, null);
    }

    private void adicionarALista(List<ResultadoRelatorioAtividadesRecentesDTO> atividadesRecentes, String identificador, String tipoDeAtividade, GregorianCalendar data, Integer idIndicante) {

	ResultadoRelatorioAtividadesRecentesDTO resultadoRelatorioAtividadesRecentesDTO = new ResultadoRelatorioAtividadesRecentesDTO();
	resultadoRelatorioAtividadesRecentesDTO.setData(data);
	resultadoRelatorioAtividadesRecentesDTO.setIdentificador(identificador);
	resultadoRelatorioAtividadesRecentesDTO.setTipoDeAtividade(tipoDeAtividade);

	if (idIndicante != null) {
	    Usuario usuario = this.hibernateUtil.selecionar(new Usuario(idIndicante));
	    resultadoRelatorioAtividadesRecentesDTO.setPatrocinador(usuario.getvNome());
	}

	atividadesRecentes.add(resultadoRelatorioAtividadesRecentesDTO);
    }

    private List<ResultadoRelatorioAtividadesRecentesDTO> ordenarPorDataCrescente(List<ResultadoRelatorioAtividadesRecentesDTO> atividadesRecentes) {

	Collections.sort(atividadesRecentes, new Comparator<ResultadoRelatorioAtividadesRecentesDTO>() {

	    public int compare(ResultadoRelatorioAtividadesRecentesDTO r1, ResultadoRelatorioAtividadesRecentesDTO r2) {

		if (r1.getData().getTimeInMillis() < r2.getData().getTimeInMillis())
		    return -1;

		if (r1.getData().getTimeInMillis() > r2.getData().getTimeInMillis())
		    return 1;

		return 0;
	    }
	});

	return atividadesRecentes;
    }
}
