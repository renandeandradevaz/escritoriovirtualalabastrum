package br.com.alabastrum.escritoriovirtual.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;

@Entity
public class Comprador implements Entidade {

    @Id
    @GeneratedValue
    private Integer id;

    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String cep;
    private String bairro;
    private String cidade;
    private String uf;
    private String endereco;
    private String numeroEndereco;
    private String complementoEndereco;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public String getCpf() {
	return cpf;
    }

    public void setCpf(String cpf) {
	this.cpf = cpf;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getTelefone() {
	return telefone;
    }

    public void setTelefone(String telefone) {
	this.telefone = telefone;
    }

    public String getCep() {
	return cep;
    }

    public void setCep(String cep) {
	this.cep = cep;
    }

    public String getBairro() {
	return bairro;
    }

    public void setBairro(String bairro) {
	this.bairro = bairro;
    }

    public String getCidade() {
	return cidade;
    }

    public void setCidade(String cidade) {
	this.cidade = cidade;
    }

    public String getUf() {
	return uf;
    }

    public void setUf(String uf) {
	this.uf = uf;
    }

    public String getEndereco() {
	return endereco;
    }

    public void setEndereco(String endereco) {
	this.endereco = endereco;
    }

    public String getNumeroEndereco() {
	return numeroEndereco;
    }

    public void setNumeroEndereco(String numeroEndereco) {
	this.numeroEndereco = numeroEndereco;
    }

    public String getComplementoEndereco() {
	return complementoEndereco;
    }

    public void setComplementoEndereco(String complementoEndereco) {
	this.complementoEndereco = complementoEndereco;
    }
}
