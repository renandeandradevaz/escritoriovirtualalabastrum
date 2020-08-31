package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class Transferencia implements Entidade {

    public static final String TRANSFERENCIA_PARA_OUTRO_DISTRIBUIDOR = "Transferência para outro distribuidor";
    public static final String TRANSFERENCIA_PARA_ALABASTRUM_CARD = "Transferência para Alabastrum Card";
    public static final String TRANSFERENCIA_PARA_SAQUE = "Transferência para Saque";
    public static final String TRANSFERENCIA_PARA_PAGAMENTO_DE_PEDIDO = "Transferência para pagamento de pedido";
    public static final String TRANSFERENCIA_POR_COMPRESSAO_DE_BONUS = "Transferência por compressão de bônus";

    @Id
    @GeneratedValue
    private Integer id;

    private GregorianCalendar data;
    private BigDecimal valor;
    private String tipo;

    @Index(name = "index_transferencia_de")
    private Integer de;

    @Index(name = "index_transferencia_para")
    private Integer para;

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

    public Integer getDe() {
	return de;
    }

    public void setDe(Integer de) {
	this.de = de;
    }

    public Integer getPara() {
	return para;
    }

    public void setPara(Integer para) {
	this.para = para;
    }
}