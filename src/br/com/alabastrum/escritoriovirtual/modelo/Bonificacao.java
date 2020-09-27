package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class Bonificacao implements Entidade {

    public static final String BONUS_DE_FILA_UNICA = "Bônus de Fila Única";

    @Id
    @GeneratedValue
    private Integer id;

    private GregorianCalendar data;
    private BigDecimal valor;
    private String tipo;

    @Index(name = "index_bonus_usuario_id")
    private Integer idCodigo;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public GregorianCalendar getData() {
	return data;
    }

    public void setData(GregorianCalendar data) {
	this.data = data;
    }

    public BigDecimal getValor() {
	return valor;
    }

    public void setValor(BigDecimal valor) {
	this.valor = valor;
    }

    public String getTipo() {
	return tipo;
    }

    public void setTipo(String tipo) {
	this.tipo = tipo;
    }

    public Integer getIdCodigo() {
	return idCodigo;
    }

    public void setIdCodigo(Integer idCodigo) {
	this.idCodigo = idCodigo;
    }
}