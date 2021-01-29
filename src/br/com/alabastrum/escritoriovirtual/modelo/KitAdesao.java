package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class KitAdesao implements Entidade {

    public static final String AFILIADO = "afiliado";

    @Id
    @GeneratedValue
    private Integer id;

    private String kit;
    private GregorianCalendar data_referencia;
    private BigDecimal valor;
    private BigDecimal valor_atividade;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getKit() {
	return kit;
    }

    public void setKit(String kit) {
	this.kit = kit;
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

    public BigDecimal getValor_atividade() {
	return valor_atividade;
    }

    public void setValor_atividade(BigDecimal valor_atividade) {
	this.valor_atividade = valor_atividade;
    }
}