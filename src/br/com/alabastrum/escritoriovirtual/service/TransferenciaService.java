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

    public List<ExtratoDTO> obterTransferenciasDeCredito(Integer idCodigo) {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	Transferencia transferenciaFiltro = new Transferencia();
	transferenciaFiltro.setPara(idCodigo);
	List<Transferencia> transferencias = hibernateUtil.buscar(transferenciaFiltro);

	for (Transferencia transferencia : transferencias) {
	    extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(transferencia.getPara())), transferencia.getData(), transferencia.getValor(), transferencia.getTipo(), transferencia.getDescricao()));
	}

	return extratos;
    }

    public List<ExtratoDTO> obterTransferenciasDeDebito(Integer idCodigo) {

	List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

	Transferencia transferenciaFiltro = new Transferencia();
	transferenciaFiltro.setDe(idCodigo);
	List<Transferencia> transferencias = hibernateUtil.buscar(transferenciaFiltro);

	for (Transferencia transferencia : transferencias) {
	    extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(transferencia.getDe())), transferencia.getData(), transferencia.getValor().multiply(new BigDecimal(-1)), transferencia.getTipo(), transferencia.getDescricao()));
	}

	return extratos;
    }
}