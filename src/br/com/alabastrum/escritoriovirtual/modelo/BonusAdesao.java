package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class BonusAdesao implements Entidade {

    @Id
    @GeneratedValue
    private Integer id;

    private String kit;
    private GregorianCalendar data_referencia;
    private BigDecimal bonusAdesao;
    private Integer geracao;

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

    public BigDecimal getBonusAdesao() {
	return bonusAdesao;
    }

    public void setBonusAdesao(BigDecimal bonusAdesao) {
	this.bonusAdesao = bonusAdesao;
    }

    public Integer getGeracao() {
	return geracao;
    }

    public void setGeracao(Integer geracao) {
	this.geracao = geracao;
    }
}