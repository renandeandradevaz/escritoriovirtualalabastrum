package br.com.alabastrum.escritoriovirtual.sessao;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class SessaoAtualizacaoDados {

	private String CPF;
	private String PosAtual;
	private String vNome;
	private String Tel;
	private String eMail;
	private String Dt_Nasc;
	private String cadSexo;
	private String cadEstCivil;
	private String cadCEP;
	private String cadEndereco;
	private String cadBairro;
	private String cadCidade;
	private String cadUF;
	private String cadCelular;
	private String cadCCorrente;
	private String cadBanco;
	private String cadAgencia;
	private String cadTipoConta;
	private String cadRG;
	private String cadOrgaoExpedidor;
	private String codigoQuemIndicou;
	private String nomeQuemIndicou;
	private String apelido;

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

	public String getCadEndereco() {
		return cadEndereco;
	}

	public void setCadEndereco(String cadEndereco) {
		this.cadEndereco = cadEndereco;
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

	public String getCodigoQuemIndicou() {
		return codigoQuemIndicou;
	}

	public void setCodigoQuemIndicou(String codigoQuemIndicou) {
		this.codigoQuemIndicou = codigoQuemIndicou;
	}

	public String getNomeQuemIndicou() {
		return nomeQuemIndicou;
	}

	public void setNomeQuemIndicou(String nomeQuemIndicou) {
		this.nomeQuemIndicou = nomeQuemIndicou;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
}