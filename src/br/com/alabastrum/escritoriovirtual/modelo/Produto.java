package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;

@Entity
public class Produto implements Entidade {

    @Id
    @GeneratedValue
    private Integer id;

    private String id_Produtos;
    private Integer id_Categoria;
    private String prdNome;
    private BigDecimal prdPreco_Unit;
    private BigDecimal prdTamanho;
    private BigDecimal prdPeso;
    private String prdComissionado;
    private String prdPromocao;
    private String prdMatApoio;
    private BigDecimal PntProduto;
    private BigDecimal PntQualificacao;
    private String periodoParaCompraDeInativos;

    public Produto() {
    }

    public Produto(String id_Produtos) {
	this.id_Produtos = id_Produtos;
    }

    public Categoria obterCategoria() {

	HibernateUtil hibernateUtil = new HibernateUtil();
	Categoria categoria = new Categoria();
	categoria.setId_Categoria(id_Categoria);
	categoria = hibernateUtil.selecionar(categoria);
	hibernateUtil.fecharSessao();
	return categoria;
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

    public String getPrdComissionado() {
	return prdComissionado;
    }

    public void setPrdComissionado(String prdComissionado) {
	this.prdComissionado = prdComissionado;
    }

    public String getPrdPromocao() {
	return prdPromocao;
    }

    public void setPrdPromocao(String prdPromocao) {
	this.prdPromocao = prdPromocao;
    }

    public String getPrdMatApoio() {
	return prdMatApoio;
    }

    public void setPrdMatApoio(String prdMatApoio) {
	this.prdMatApoio = prdMatApoio;
    }

    public BigDecimal getPrdTamanho() {
	return prdTamanho;
    }

    public void setPrdTamanho(BigDecimal prdTamanho) {
	this.prdTamanho = prdTamanho;
    }

    public BigDecimal getPrdPeso() {
	return prdPeso;
    }

    public void setPrdPeso(BigDecimal prdPeso) {
	this.prdPeso = prdPeso;
    }

    public BigDecimal getPntProduto() {
	return PntProduto;
    }

    public void setPntProduto(BigDecimal pntProduto) {
	PntProduto = pntProduto;
    }

    public BigDecimal getPntQualificacao() {
	return PntQualificacao;
    }

    public void setPntQualificacao(BigDecimal pntQualificacao) {
	PntQualificacao = pntQualificacao;
    }

    public String getPeriodoParaCompraDeInativos() {
	return periodoParaCompraDeInativos;
    }

    public void setPeriodoParaCompraDeInativos(String periodoParaCompraDeInativos) {
	this.periodoParaCompraDeInativos = periodoParaCompraDeInativos;
    }
}
