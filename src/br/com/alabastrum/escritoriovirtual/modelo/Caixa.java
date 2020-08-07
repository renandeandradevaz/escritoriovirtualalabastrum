package br.com.alabastrum.escritoriovirtual.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class Caixa implements Entidade {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer larguraCaixa;
    private Integer alturaCaixa;
    private Integer comprimentoCaixa;
    private Integer pesoCaixa;
    private Integer capacidadeCaixa;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Integer getLarguraCaixa() {
	return larguraCaixa;
    }

    public void setLarguraCaixa(Integer larguraCaixa) {
	this.larguraCaixa = larguraCaixa;
    }

    public Integer getAlturaCaixa() {
	return alturaCaixa;
    }

    public void setAlturaCaixa(Integer alturaCaixa) {
	this.alturaCaixa = alturaCaixa;
    }

    public Integer getComprimentoCaixa() {
	return comprimentoCaixa;
    }

    public void setComprimentoCaixa(Integer comprimentoCaixa) {
	this.comprimentoCaixa = comprimentoCaixa;
    }

    public Integer getPesoCaixa() {
	return pesoCaixa;
    }

    public void setPesoCaixa(Integer pesoCaixa) {
	this.pesoCaixa = pesoCaixa;
    }

    public Integer getCapacidadeCaixa() {
	return capacidadeCaixa;
    }

    public void setCapacidadeCaixa(Integer capacidadeCaixa) {
	this.capacidadeCaixa = capacidadeCaixa;
    }
}