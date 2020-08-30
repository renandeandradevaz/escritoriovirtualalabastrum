package br.com.alabastrum.escritoriovirtual.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;
import org.hibernate.criterion.MatchMode;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.util.Util;

@Entity
public class Usuario implements Entidade {

    @Id
    @GeneratedValue
    private Integer id;

    private String CPF;
    private String PosAtual;
    private String PosAbrev;
    private String vNome;
    private String Tel;
    private String eMail;
    private String Dt_Nasc;
    private String EV;
    private String cadSexo;
    private String cadEstCivil;
    private String cadCEP;
    private String cadBairro;
    private String cadCidade;
    private String cadUF;
    private String cadCelular;
    private String cadEndereco;
    private String cadCCorrente;
    private String cadBanco;
    private String cadAgencia;
    private String cadTipoConta;
    private String cadRG;
    private String cadOrgaoExpedidor;
    private String apelido;
    private String pisMis;
    private String pasep;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoEstadual;
    private String agenciaBancoEspecifico;
    private String contaBancoEspecifico;
    private String bancoPessoaJuridica;
    private String agenciaPessoaJuridica;
    private String contaPessoaJuridica;
    private String agenciaPessoaJuridicaBancoEspecifico;
    private String contaPessoaJuridicaBancoEspecifico;
    private Integer cadPreCadastro;
    private Integer documentacaoEnviada;
    private Integer Fila_unica;

    @Index(name = "index_id_Codigo")
    private Integer id_Codigo;

    @Index(name = "index_id_lider")
    private Integer id_lider;

    @Index(name = "index_id_Indicante")
    private Integer id_Indicante;

    @Transient
    private String nicknameQuemIndicou;

    @Transient
    private Boolean donoDeFranquia;

    @Transient
    private InformacoesFixasUsuario informacoesFixasUsuario;

    public Usuario() {

    }

    public Usuario(Integer id_Codigo) {
	this.id_Codigo = id_Codigo;
    }

    public InformacoesFixasUsuario obterInformacoesFixasUsuario() {

	if (Util.preenchido(informacoesFixasUsuario)) {

	    return informacoesFixasUsuario;
	}

	InformacoesFixasUsuario informacoesFixasUsuario = new InformacoesFixasUsuario();
	informacoesFixasUsuario.setCodigoUsuario(this.id_Codigo);

	HibernateUtil hibernateUtil = new HibernateUtil();

	informacoesFixasUsuario = hibernateUtil.selecionar(informacoesFixasUsuario, MatchMode.EXACT);
	this.setInformacoesFixasUsuario(informacoesFixasUsuario);

	hibernateUtil.fecharSessao();

	return informacoesFixasUsuario;
    }

    public Boolean getDonoDeFranquia() {

	if (this.id_Codigo == null) {
	    return false;
	}

	if (Util.preenchido(this.donoDeFranquia)) {

	    return this.donoDeFranquia;
	}

	HibernateUtil hibernateUtil = new HibernateUtil();

	Franquia franquiaFiltro = new Franquia();
	franquiaFiltro.setId_Codigo(this.id_Codigo);
	List<Franquia> franquias = hibernateUtil.buscar(franquiaFiltro);

	if (franquias != null && franquias.size() > 0)
	    this.donoDeFranquia = true;
	else
	    this.donoDeFranquia = false;

	hibernateUtil.fecharSessao();

	return this.donoDeFranquia;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public InformacoesFixasUsuario getInformacoesFixasUsuario() {
	return this.informacoesFixasUsuario;
    }

    public void setInformacoesFixasUsuario(InformacoesFixasUsuario informacoesFixasUsuario) {
	this.informacoesFixasUsuario = informacoesFixasUsuario;
    }

    public String getCPF() {
	return CPF;
    }

    public void setCPF(String cPF) {
	CPF = cPF;
    }

    public String getPosAtual() {
	return PosAtual;
    }

    public void setPosAtual(String posAtual) {
	PosAtual = posAtual;
    }

    public String getPosAbrev() {
	return PosAbrev;
    }

    public void setPosAbrev(String posAbrev) {
	PosAbrev = posAbrev;
    }

    public String getvNome() {
	return vNome;
    }

    public void setvNome(String vNome) {
	this.vNome = vNome;
    }

    public String getTel() {
	return Tel;
    }

    public void setTel(String tel) {
	Tel = tel;
    }

    public String geteMail() {
	return eMail;
    }

    public void seteMail(String eMail) {
	this.eMail = eMail;
    }

    public String getDt_Nasc() {
	return Dt_Nasc;
    }

    public void setDt_Nasc(String dt_Nasc) {
	Dt_Nasc = dt_Nasc;
    }

    public String getEV() {
	return EV;
    }

    public void setEV(String eV) {
	EV = eV;
    }

    public String getCadSexo() {
	return cadSexo;
    }

    public void setCadSexo(String cadSexo) {
	this.cadSexo = cadSexo;
    }

    public String getCadEstCivil() {
	return cadEstCivil;
    }

    public void setCadEstCivil(String cadEstCivil) {
	this.cadEstCivil = cadEstCivil;
    }

    public String getCadCEP() {
	return cadCEP;
    }

    public void setCadCEP(String cadCEP) {
	this.cadCEP = cadCEP;
    }

    public String getCadBairro() {
	return cadBairro;
    }

    public void setCadBairro(String cadBairro) {
	this.cadBairro = cadBairro;
    }

    public String getCadCidade() {
	return cadCidade;
    }

    public void setCadCidade(String cadCidade) {
	this.cadCidade = cadCidade;
    }

    public String getCadUF() {
	return cadUF;
    }

    public void setCadUF(String cadUF) {
	this.cadUF = cadUF;
    }

    public String getCadCelular() {
	return cadCelular;
    }

    public void setCadCelular(String cadCelular) {
	this.cadCelular = cadCelular;
    }

    public String getCadEndereco() {
	return cadEndereco;
    }

    public void setCadEndereco(String cadEndereco) {
	this.cadEndereco = cadEndereco;
    }

    public String getCadCCorrente() {
	return cadCCorrente;
    }

    public void setCadCCorrente(String cadCCorrente) {
	this.cadCCorrente = cadCCorrente;
    }

    public String getCadBanco() {
	return cadBanco;
    }

    public void setCadBanco(String cadBanco) {
	this.cadBanco = cadBanco;
    }

    public String getCadAgencia() {
	return cadAgencia;
    }

    public void setCadAgencia(String cadAgencia) {
	this.cadAgencia = cadAgencia;
    }

    public String getCadTipoConta() {
	return cadTipoConta;
    }

    public void setCadTipoConta(String cadTipoConta) {
	this.cadTipoConta = cadTipoConta;
    }

    public String getCadRG() {
	return cadRG;
    }

    public void setCadRG(String cadRG) {
	this.cadRG = cadRG;
    }

    public String getCadOrgaoExpedidor() {
	return cadOrgaoExpedidor;
    }

    public void setCadOrgaoExpedidor(String cadOrgaoExpedidor) {
	this.cadOrgaoExpedidor = cadOrgaoExpedidor;
    }

    public String getApelido() {
	return apelido;
    }

    public void setApelido(String apelido) {
	this.apelido = apelido;
    }

    public Integer getId_Codigo() {
	return id_Codigo;
    }

    public void setId_Codigo(Integer id_Codigo) {
	this.id_Codigo = id_Codigo;
    }

    public Integer getId_lider() {
	return id_lider;
    }

    public void setId_lider(Integer id_lider) {
	this.id_lider = id_lider;
    }

    public Integer getId_Indicante() {
	return id_Indicante;
    }

    public void setId_Indicante(Integer id_Indicante) {
	this.id_Indicante = id_Indicante;
    }

    public String getPisMis() {
	return pisMis;
    }

    public void setPisMis(String pisMis) {
	this.pisMis = pisMis;
    }

    public String getPasep() {
	return pasep;
    }

    public void setPasep(String pasep) {
	this.pasep = pasep;
    }

    public String getCnpj() {
	return cnpj;
    }

    public void setCnpj(String cnpj) {
	this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
	return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
	this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
	return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
	this.nomeFantasia = nomeFantasia;
    }

    public String getInscricaoEstadual() {
	return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
	this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getAgenciaBancoEspecifico() {
	return agenciaBancoEspecifico;
    }

    public void setAgenciaBancoEspecifico(String agenciaBancoEspecifico) {
	this.agenciaBancoEspecifico = agenciaBancoEspecifico;
    }

    public String getContaBancoEspecifico() {
	return contaBancoEspecifico;
    }

    public void setContaBancoEspecifico(String contaBancoEspecifico) {
	this.contaBancoEspecifico = contaBancoEspecifico;
    }

    public String getBancoPessoaJuridica() {
	return bancoPessoaJuridica;
    }

    public void setBancoPessoaJuridica(String bancoPessoaJuridica) {
	this.bancoPessoaJuridica = bancoPessoaJuridica;
    }

    public String getAgenciaPessoaJuridica() {
	return agenciaPessoaJuridica;
    }

    public void setAgenciaPessoaJuridica(String agenciaPessoaJuridica) {
	this.agenciaPessoaJuridica = agenciaPessoaJuridica;
    }

    public String getContaPessoaJuridica() {
	return contaPessoaJuridica;
    }

    public void setContaPessoaJuridica(String contaPessoaJuridica) {
	this.contaPessoaJuridica = contaPessoaJuridica;
    }

    public String getAgenciaPessoaJuridicaBancoEspecifico() {
	return agenciaPessoaJuridicaBancoEspecifico;
    }

    public void setAgenciaPessoaJuridicaBancoEspecifico(String agenciaPessoaJuridicaBancoEspecifico) {
	this.agenciaPessoaJuridicaBancoEspecifico = agenciaPessoaJuridicaBancoEspecifico;
    }

    public String getContaPessoaJuridicaBancoEspecifico() {
	return contaPessoaJuridicaBancoEspecifico;
    }

    public void setContaPessoaJuridicaBancoEspecifico(String contaPessoaJuridicaBancoEspecifico) {
	this.contaPessoaJuridicaBancoEspecifico = contaPessoaJuridicaBancoEspecifico;
    }

    public String getNicknameQuemIndicou() {
	return nicknameQuemIndicou;
    }

    public void setNicknameQuemIndicou(String nicknameQuemIndicou) {
	this.nicknameQuemIndicou = nicknameQuemIndicou;
    }

    public Integer getCadPreCadastro() {
	return cadPreCadastro;
    }

    public void setCadPreCadastro(Integer cadPreCadastro) {
	this.cadPreCadastro = cadPreCadastro;
    }

    public Integer getFila_unica() {
	return Fila_unica;
    }

    public void setFila_unica(Integer fila_unica) {
	Fila_unica = fila_unica;
    }

    public Integer getDocumentacaoEnviada() {
	return documentacaoEnviada;
    }

    public void setDocumentacaoEnviada(Integer documentacaoEnviada) {
	this.documentacaoEnviada = documentacaoEnviada;
    }
}