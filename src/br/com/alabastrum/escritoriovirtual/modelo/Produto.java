package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class Produto implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private String id_Produtos;
	private Integer id_Categoria;
	private String prdNome;
	private BigDecimal prdPreco_Unit;
	private Integer prdPontos;

	public Produto() {
	}

	public Produto(String id_Produtos) {
		this.id_Produtos = id_Produtos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getId_Produtos() {
		return id_Produtos;
	}

	public Integer getPrdPontos() {
		return prdPontos;
	}

	public void setPrdPontos(Integer prdPontos) {
		this.prdPontos = prdPontos;
	}

	public void setId_Produtos(String id_Produtos) {
		this.id_Produtos = id_Produtos;
	}

	public Integer getId_Categoria() {
		return id_Categoria;
	}

	public void setId_Categoria(Integer id_Categoria) {
		this.id_Categoria = id_Categoria;
	}

	public String getPrdNome() {
		return prdNome;
	}

	public void setPrdNome(String prdNome) {
		this.prdNome = prdNome;
	}

	public BigDecimal getPrdPreco_Unit() {
		return prdPreco_Unit;
	}

	public void setPrdPreco_Unit(BigDecimal prdPreco_Unit) {
		this.prdPreco_Unit = prdPreco_Unit;
	}
}