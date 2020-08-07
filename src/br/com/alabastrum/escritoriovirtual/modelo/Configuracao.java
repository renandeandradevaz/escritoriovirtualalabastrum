package br.com.alabastrum.escritoriovirtual.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.caelum.vraptor.Resource;

@Resource
@Entity
public class Configuracao implements Entidade {

    @Id
    @GeneratedValue
    private Integer id;

    @Index(name = "index_chave_configuracao")
    private String chave;

    @Column(name = "valor", columnDefinition = "TEXT")
    private String valor;

    @Index(name = "index_id_Codigo_configuracao")
    private Integer id_Codigo;

    public String retornarConfiguracao(String chave) {

	HibernateUtil hibernateUtil = new HibernateUtil();

	Configuracao configuracao = new Configuracao();
	configuracao.setChave(chave);

	configuracao = hibernateUtil.selecionar(configuracao);

	hibernateUtil.fecharSessao();

	return configuracao.getValor();
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getChave() {
	return chave;
    }

    public void setChave(String chave) {
	this.chave = chave;
    }

    public String getValor() {
	return valor;
    }

    public void setValor(String valor) {
	this.valor = valor;
    }

    public Integer getId_Codigo() {
	return id_Codigo;
    }

    public void setId_Codigo(Integer id_Codigo) {
	this.id_Codigo = id_Codigo;
    }
}
