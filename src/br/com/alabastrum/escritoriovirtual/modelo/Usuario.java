package br.com.alabastrum.escritoriovirtual.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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

	@ManyToOne(fetch = FetchType.LAZY)
	private InformacoesFixasUsuario informacoesFixasUsuario;

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

	@Index(name = "index_id_Codigo")
	private Integer id_Codigo;

	@Index(name = "index_id_lider")
	private Integer id_lider;

	@Index(name = "index_id_Indicante")
	private Integer id_Indicante;

	@Transient
	private Integer codigoQuemIndicou;

	@Transient
	private String nomeQuemIndicou;

	@Transient
	private Boolean donoDeFranquia;

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
		return informacoesFixasUsuario;
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

	public Integer getCodigoQuemIndicou() {
		return codigoQuemIndicou;
	}

	public void setCodigoQuemIndicou(Integer codigoQuemIndicou) {
		this.codigoQuemIndicou = codigoQuemIndicou;
	}

	public String getNomeQuemIndicou() {
		return nomeQuemIndicou;
	}

	public void setNomeQuemIndicou(String nomeQuemIndicou) {
		this.nomeQuemIndicou = nomeQuemIndicou;
	}
}