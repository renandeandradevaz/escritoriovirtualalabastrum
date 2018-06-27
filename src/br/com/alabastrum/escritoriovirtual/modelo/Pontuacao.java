package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class Pontuacao implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private GregorianCalendar Dt_Pontos;
	private BigDecimal ParametroAtividade;
	private BigDecimal PntAtividade;
	private BigDecimal PntIngresso;
	private BigDecimal PntProduto;
	private BigDecimal valorIngresso;

	@Index(name = "index_id_Codigo_pontuacao")
	private Integer id_Codigo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public GregorianCalendar getDt_Pontos() {
		return Dt_Pontos;
	}

	public void setDt_Pontos(GregorianCalendar dt_Pontos) {
		Dt_Pontos = dt_Pontos;
	}

	public BigDecimal getParametroAtividade() {
		return ParametroAtividade;
	}

	public void setParametroAtividade(BigDecimal parametroAtividade) {
		ParametroAtividade = parametroAtividade;
	}

	public BigDecimal getPntAtividade() {
		return PntAtividade;
	}

	public void setPntAtividade(BigDecimal pntAtividade) {
		PntAtividade = pntAtividade;
	}

	public Integer getId_Codigo() {
		return id_Codigo;
	}

	public void setId_Codigo(Integer id_Codigo) {
		this.id_Codigo = id_Codigo;
	}

	public BigDecimal getPntIngresso() {
		return PntIngresso;
	}

	public void setPntIngresso(BigDecimal pntIngresso) {
		PntIngresso = pntIngresso;
	}

	public BigDecimal getPntProduto() {
		return PntProduto;
	}

	public void setPntProduto(BigDecimal pntProduto) {
		PntProduto = pntProduto;
	}

	public BigDecimal getValorIngresso() {
		return valorIngresso;
	}

	public void setValorIngresso(BigDecimal valorIngresso) {
		this.valorIngresso = valorIngresso;
	}
}
