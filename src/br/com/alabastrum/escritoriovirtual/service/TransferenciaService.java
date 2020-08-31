package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Transferencia;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class TransferenciaService {

    private HibernateUtil hibernateUtil;

    public TransferenciaService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public List<ExtratoDTO> obterTransferenciasPorCompressaoDeBonus(Integer idCodigo) {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	Transferencia transferenciaFiltro = new Transferencia();
	transferenciaFiltro.setPara(idCodigo);
	transferenciaFiltro.setTipo(Transferencia.TRANSFERENCIA_POR_COMPRESSAO_DE_BONUS);
	List<Transferencia> transferencias = hibernateUtil.buscar(transferenciaFiltro);

	for (Transferencia transferencia : transferencias) {
	    extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(transferencia.getDe())), transferencia.getData(), transferencia.getValor(), transferencia.getTipo()));
	}

	return extratos;
    }

    public List<ExtratoDTO> obterTransferenciasDeOutroDistribuidor(Integer idCodigo) {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	Transferencia transferenciaFiltro = new Transferencia();
	transferenciaFiltro.setPara(idCodigo);
	transferenciaFiltro.setTipo(Transferencia.TRANSFERENCIA_PARA_OUTRO_DISTRIBUIDOR);
	List<Transferencia> transferencias = hibernateUtil.buscar(transferenciaFiltro);

	for (Transferencia transferencia : transferencias) {
	    extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(transferencia.getDe())), transferencia.getData(), transferencia.getValor(), transferencia.getTipo()));
	}

	return extratos;
    }

    public List<ExtratoDTO> obterTransferenciasParaOutroDistribuidor(Integer idCodigo) {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	Transferencia transferenciaFiltro = new Transferencia();
	transferenciaFiltro.setDe(idCodigo);
	transferenciaFiltro.setTipo(Transferencia.TRANSFERENCIA_PARA_OUTRO_DISTRIBUIDOR);
	List<Transferencia> transferencias = hibernateUtil.buscar(transferenciaFiltro);

	for (Transferencia transferencia : transferencias) {
	    extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(transferencia.getPara())), transferencia.getData(), transferencia.getValor().multiply(new BigDecimal(-1)), transferencia.getTipo()));
	}

	return extratos;
    }

    public List<ExtratoDTO> obterTransferenciasParaAlabastrumCard(Integer idCodigo) {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	Transferencia transferenciaFiltro = new Transferencia();
	transferenciaFiltro.setDe(idCodigo);
	transferenciaFiltro.setTipo(Transferencia.TRANSFERENCIA_PARA_ALABASTRUM_CARD);
	List<Transferencia> transferencias = hibernateUtil.buscar(transferenciaFiltro);

	for (Transferencia transferencia : transferencias) {
	    extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(transferencia.getDe())), transferencia.getData(), transferencia.getValor().multiply(new BigDecimal(-1)), transferencia.getTipo()));
	}

	return extratos;
    }

    public List<ExtratoDTO> obterTransferenciasParaPagamentoDePedido(Integer idCodigo) {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	Transferencia transferenciaFiltro = new Transferencia();
	transferenciaFiltro.setDe(idCodigo);
	transferenciaFiltro.setTipo(Transferencia.TRANSFERENCIA_PARA_PAGAMENTO_DE_PEDIDO);
	List<Transferencia> transferencias = hibernateUtil.buscar(transferenciaFiltro);

	for (Transferencia transferencia : transferencias) {
	    extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(transferencia.getDe())), transferencia.getData(), transferencia.getValor().multiply(new BigDecimal(-1)), transferencia.getTipo()));
	}

	return extratos;
    }

    public List<ExtratoDTO> obterTransferenciasParaSaque(Integer idCodigo) {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	Transferencia transferenciaFiltro = new Transferencia();
	transferenciaFiltro.setDe(idCodigo);
	transferenciaFiltro.setTipo(Transferencia.TRANSFERENCIA_PARA_SAQUE);
	List<Transferencia> transferencias = hibernateUtil.buscar(transferenciaFiltro);

	for (Transferencia transferencia : transferencias) {
	    extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(transferencia.getDe())), transferencia.getData(), transferencia.getValor().multiply(new BigDecimal(-1)), transferencia.getTipo()));
	}

	return extratos;
    }
}