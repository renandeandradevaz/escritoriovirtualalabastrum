package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class SolicitacaoSaque implements Entidade {

    @Id
    @GeneratedValue
    private Integer id;

    private BigDecimal valorBrutoSolicitado;
    private BigDecimal valorFinalComDescontos;
    private GregorianCalendar data;
    private String status;

    @Index(name = "index_solicitacao_saque_id_codigo")
    private Integer idCodigo;

    private Integer idCodigoAdmAprovou;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public BigDecimal getValorBrutoSolicitado() {
	return valorBrutoSolicitado;
    }

    public void setValorBrutoSolicitado(BigDecimal valorBrutoSolicitado) {
	this.valorBrutoSolicitado = valorBrutoSolicitado;
    }

    public BigDecimal getValorFinalComDescontos() {
	return valorFinalComDescontos;
    }

    public void setValorFinalComDescontos(BigDecimal valorFinalComDescontos) {
	this.valorFinalComDescontos = valorFinalComDescontos;
    }

    public Integer getIdCodigo() {
	return idCodigo;
    }

    public void setIdCodigo(Integer idCodigo) {
	this.idCodigo = idCodigo;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public GregorianCalendar getData() {
	return data;
    }

    public void setData(GregorianCalendar data) {
	this.data = data;
    }

    public Integer getIdCodigoAdmAprovou() {
	return idCodigoAdmAprovou;
    }

    public void setIdCodigoAdmAprovou(Integer idCodigoAdmAprovou) {
	this.idCodigoAdmAprovou = idCodigoAdmAprovou;
    }

}