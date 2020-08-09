package br.com.alabastrum.escritoriovirtual.modelo;

import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Index;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class Pedido implements Entidade {

    @Id
    @GeneratedValue
    private Integer id;

    private GregorianCalendar data;
    private Boolean completed;
    private Integer idFranquia;
    private String status;
    private String tipo;
    private String formaDePagamento;
    private String formaDeEntrega;
    private String empresaParaEntrega;

    @ManyToOne
    private Comprador comprador;

    public Comprador getComprador() {
	return comprador;
    }

    public void setComprador(Comprador comprador) {
	this.comprador = comprador;
    }

    @Index(name = "index_idCodigo_pedido")
    private Integer idCodigo;

    public Pedido() {
    }

    public Pedido(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Boolean getCompleted() {
	return completed;
    }

    public void setCompleted(Boolean completed) {
	this.completed = completed;
    }

    public Integer getIdFranquia() {
	return idFranquia;
    }

    public void setIdFranquia(Integer idFranquia) {
	this.idFranquia = idFranquia;
    }

    public Integer getIdCodigo() {
	return idCodigo;
    }

    public void setIdCodigo(Integer idCodigo) {
	this.idCodigo = idCodigo;
    }

    public GregorianCalendar getData() {
	return data;
    }

    public void setData(GregorianCalendar data) {
	this.data = data;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getTipo() {
	return tipo;
    }

    public void setTipo(String tipo) {
	this.tipo = tipo;
    }

    public String getFormaDePagamento() {
	return formaDePagamento;
    }

    public void setFormaDePagamento(String formaDePagamento) {
	this.formaDePagamento = formaDePagamento;
    }

    public String getFormaDeEntrega() {
	return formaDeEntrega;
    }

    public void setFormaDeEntrega(String formaDeEntrega) {
	this.formaDeEntrega = formaDeEntrega;
    }

    public String getEmpresaParaEntrega() {
	return empresaParaEntrega;
    }

    public void setEmpresaParaEntrega(String empresaParaEntrega) {
	this.empresaParaEntrega = empresaParaEntrega;
    }
}