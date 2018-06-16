package br.com.alabastrum.escritoriovirtual.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class Franquia implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private Integer id_Estoque;
	private String estqNome;
	private String estqEndereco;
	private String estqBairro;
	private String estqCidade;
	private String estqUF;
	private String estqCEP;
	private String estqTelefone;
	private String estqEmail;

	public Franquia() {
	}

	public Franquia(Integer id_Estoque) {
		this.id_Estoque = id_Estoque;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_Estoque() {
		return id_Estoque;
	}

	public void setId_Estoque(Integer id_Estoque) {
		this.id_Estoque = id_Estoque;
	}

	public String getEstqNome() {
		return estqNome;
	}

	public void setEstqNome(String estqNome) {
		this.estqNome = estqNome;
	}

	public String getEstqEndereco() {
		return estqEndereco;
	}

	public void setEstqEndereco(String estqEndereco) {
		this.estqEndereco = estqEndereco;
	}

	public String getEstqBairro() {
		return estqBairro;
	}

	public void setEstqBairro(String estqBairro) {
		this.estqBairro = estqBairro;
	}

	public String getEstqCidade() {
		return estqCidade;
	}

	public void setEstqCidade(String estqCidade) {
		this.estqCidade = estqCidade;
	}

	public String getEstqUF() {
		return estqUF;
	}

	public void setEstqUF(String estqUF) {
		this.estqUF = estqUF;
	}

	public String getEstqCEP() {
		return estqCEP;
	}

	public void setEstqCEP(String estqCEP) {
		this.estqCEP = estqCEP;
	}

	public String getEstqTelefone() {
		return estqTelefone;
	}

	public void setEstqTelefone(String estqTelefone) {
		this.estqTelefone = estqTelefone;
	}

	public String getEstqEmail() {
		return estqEmail;
	}

	public void setEstqEmail(String estqEmail) {
		this.estqEmail = estqEmail;
	}
}
