package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;

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

    @Index(name = "index_solicitacao_saque_id_codigo")
    private Integer idCodigo;

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

}