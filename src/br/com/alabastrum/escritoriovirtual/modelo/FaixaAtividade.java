package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class FaixaAtividade implements Entidade {

    @Id
    @GeneratedValue
    private Integer id;

    private GregorianCalendar data_referencia;
    private BigDecimal valor;
    private BigDecimal min_pessoas;
    private BigDecimal max_pessoas;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public GregorianCalendar getData_referencia() {
	return data_referencia;
    }

    public void setData_referencia(GregorianCalendar data_referencia) {
	this.data_referencia = data_referencia;
    }

    public BigDecimal getValor() {
	return valor;
    }

    public void setValor(BigDecimal valor) {
	this.valor = valor;
    }

    public BigDecimal getMin_pessoas() {
	return min_pessoas;
    }

    public void setMin_pessoas(BigDecimal min_pessoas) {
	this.min_pessoas = min_pessoas;
    }

    public BigDecimal getMax_pessoas() {
	return max_pessoas;
    }

    public void setMax_pessoas(BigDecimal max_pessoas) {
	this.max_pessoas = max_pessoas;
    }
}