package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class Posicao implements Entidade {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer posicao;
    private String nome;
    private Integer pontuacao;
    private BigDecimal bonusReconhecimento;
    private BigDecimal pontosDesempenho;
    private BigDecimal bonusDesempenho;
    private GregorianCalendar data_referencia;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Integer getPosicao() {
	return posicao;
    }

    public void setPosicao(Integer posicao) {
	this.posicao = posicao;
    }

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public Integer getPontuacao() {
	return pontuacao;
    }

    public void setPontuacao(Integer pontuacao) {
	this.pontuacao = pontuacao;
    }

    public GregorianCalendar getData_referencia() {
	return data_referencia;
    }

    public void setData_referencia(GregorianCalendar data_referencia) {
	this.data_referencia = data_referencia;
    }

    public BigDecimal getBonusReconhecimento() {
	return bonusReconhecimento;
    }

    public void setBonusReconhecimento(BigDecimal bonusReconhecimento) {
	this.bonusReconhecimento = bonusReconhecimento;
    }

    public BigDecimal getPontosDesempenho() {
	return pontosDesempenho;
    }

    public void setPontosDesempenho(BigDecimal pontosDesempenho) {
	this.pontosDesempenho = pontosDesempenho;
    }

    public BigDecimal getBonusDesempenho() {
	return bonusDesempenho;
    }

    public void setBonusDesempenho(BigDecimal bonusDesempenho) {
	this.bonusDesempenho = bonusDesempenho;
    }
}