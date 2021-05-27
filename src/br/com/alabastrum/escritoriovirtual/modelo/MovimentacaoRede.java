package br.com.alabastrum.escritoriovirtual.modelo;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.GregorianCalendar;

@Entity
public class MovimentacaoRede implements Entidade {

    @Id
    @GeneratedValue
    private Integer id;

    private GregorianCalendar data;
    private Integer nivel;
    private BigDecimal porcentagem;

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

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public BigDecimal getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(BigDecimal porcentagem) {
        this.porcentagem = porcentagem;
    }
}