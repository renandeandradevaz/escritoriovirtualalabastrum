package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class Adesao implements Entidade {

    @Id
    @GeneratedValue
    private Integer id;

    private GregorianCalendar data_referencia;
    private BigDecimal bonusAdesao;
    private BigDecimal bonusAdesaoPA;
    private Integer geracao;

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

    public Integer getGeracao() {
	return geracao;
    }

    public void setGeracao(Integer geracao) {
	this.geracao = geracao;
    }

    public BigDecimal getBonusAdesao() {
	return bonusAdesao;
    }

    public void setBonusAdesao(BigDecimal bonusAdesao) {
	this.bonusAdesao = bonusAdesao;
    }

    public BigDecimal getBonusAdesaoPA() {
	return bonusAdesaoPA;
    }

    public void setBonusAdesaoPA(BigDecimal bonusAdesaoPA) {
	this.bonusAdesaoPA = bonusAdesaoPA;
    }
}