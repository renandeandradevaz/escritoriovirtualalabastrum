package br.com.alabastrum.escritoriovirtual.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.Order;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.dto.TransferenciaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Transferencia;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class CreditoEDebitoController {

    private Result result;
    private HibernateUtil hibernateUtil;
    private Validator validator;
    private SessaoUsuario sessaoUsuario;

    public CreditoEDebitoController(Result result, HibernateUtil hibernateUtil, Validator validator, SessaoUsuario sessaoUsuario) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
	this.validator = validator;
	this.sessaoUsuario = sessaoUsuario;
    }

    @Funcionalidade(administrativa = "true")
    public void acessarTelaCreditoEDebito() {
    }

    @Funcionalidade(administrativa = "true")
    public void salvarCreditoOuDebito(String tipo, String apelido, String descricao, BigDecimal valor) {

	Usuario usuario = new Usuario();
	usuario.setApelido(apelido);
	usuario = this.hibernateUtil.selecionar(usuario);

	if (usuario == null) {
	    validator.add(new ValidationMessage(String.format("Distribuidor com nickname " + apelido + " nao existe"), "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaCreditoEDebito();
	    return;
	}

	if (valor.intValue() <= 0) {
	    validator.add(new ValidationMessage(String.format("O valor precisa ser maior do que zero"), "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaCreditoEDebito();
	    return;
	}

	Transferencia transferencia = new Transferencia();
	transferencia.setTipo(tipo);
	transferencia.setValor(valor);
	transferencia.setData(new GregorianCalendar());
	transferencia.setDescricao(descricao);
	transferencia.setCodigoDoResponsavelPelaTransferencia(this.sessaoUsuario.getUsuario().getId_Codigo());

	if (tipo.equals(Transferencia.TRANSFERENCIA_POR_CREDITO))
	    transferencia.setPara(usuario.getId_Codigo());

	else if (tipo.equals(Transferencia.TRANSFERENCIA_POR_DEBITO))
	    transferencia.setDe(usuario.getId_Codigo());

	this.hibernateUtil.salvarOuAtualizar(transferencia);

	result.forwardTo(this).listarTransferenciasCreditoEDebito();
    }

    @Funcionalidade(administrativa = "true")
    public void listarTransferenciasCreditoEDebito() {

	List<Transferencia> todasTransferencias = this.hibernateUtil.buscar(new Transferencia(), Order.desc("id"));
	List<TransferenciaDTO> transferenciasDeDebitoECredito = new ArrayList<TransferenciaDTO>();

	for (Transferencia transferencia : todasTransferencias) {
	    if (transferencia.getCodigoDoResponsavelPelaTransferencia() != null) {

		TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
		transferenciaDTO.setData(transferencia.getData());
		transferenciaDTO.setDescricao(transferencia.getDescricao());
		transferenciaDTO.setTipo(transferencia.getTipo());
		transferenciaDTO.setValor(transferencia.getValor());
		transferenciaDTO.setUsuarioResponsavelPelaTransferencia((Usuario) this.hibernateUtil.selecionar(new Usuario(transferencia.getCodigoDoResponsavelPelaTransferencia())));

		if (transferencia.getDe() != null) {
		    transferenciaDTO.setDistribuidor((Usuario) this.hibernateUtil.selecionar(new Usuario(transferencia.getDe())));
		}

		if (transferencia.getPara() != null) {
		    transferenciaDTO.setDistribuidor((Usuario) this.hibernateUtil.selecionar(new Usuario(transferencia.getPara())));
		}

		transferenciasDeDebitoECredito.add(transferenciaDTO);
	    }
	}

	result.include("transferencias", transferenciasDeDebitoECredito);
    }
}
