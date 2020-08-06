package br.com.alabastrum.escritoriovirtual.controller;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.anotacoes.Public;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class AtualizacaoDadosController {

    private Result result;
    private SessaoUsuario sessaoUsuario;
    private HibernateUtil hibernateUtil;
    private Validator validator;

    public AtualizacaoDadosController(Result result, SessaoUsuario sessaoUsuario, HibernateUtil hibernateUtil, Validator validator) {

	this.result = result;
	this.sessaoUsuario = sessaoUsuario;
	this.hibernateUtil = hibernateUtil;
	this.validator = validator;
    }

    @Funcionalidade
    public void acessarTelaAtualizacaoDados() {

	Usuario usuario = this.hibernateUtil.selecionar(new Usuario(this.sessaoUsuario.getUsuario().getId_Codigo()));
	result.include("usuario", usuario);
    }

    @Public
    @Path("/cadastro")
    public void acessarTelaCadastro() {
    }

    @Funcionalidade
    public void salvarAtualizacaoDados(Usuario usuario) throws Exception {

	String textoArquivo = "id_codigo: \'" + this.sessaoUsuario.getUsuario().getId_Codigo() + "\'\r\n";
	textoArquivo += "CPF: \'" + this.sessaoUsuario.getUsuario().getCPF() + "\'\r\n";
	textoArquivo += "Nome: \'" + usuario.getvNome() + "\'\r\n";
	textoArquivo += "Data_de_nascimento: \'" + usuario.getDt_Nasc() + "\'\r\n";
	textoArquivo += "RG: \'" + usuario.getCadRG() + "\'\r\n";
	textoArquivo += "Emissor: \'" + usuario.getCadOrgaoExpedidor() + "\'\r\n";
	textoArquivo += "Sexo: \'" + usuario.getCadSexo() + "\'\r\n";
	textoArquivo += "Estado_civil: \'" + usuario.getCadEstCivil() + "\'\r\n";
	textoArquivo += "CEP: \'" + usuario.getCadCEP() + "\'\r\n";
	textoArquivo += "Endereco: \'" + usuario.getCadEndereco() + "\'\r\n";
	textoArquivo += "Bairro: \'" + usuario.getCadBairro() + "\'\r\n";
	textoArquivo += "Cidade: \'" + usuario.getCadCidade() + "\'\r\n";
	textoArquivo += "Estado: \'" + usuario.getCadUF() + "\'\r\n";
	textoArquivo += "Telefone_residencial: \'" + usuario.getTel() + "\'\r\n";
	textoArquivo += "Telefone_celular: \'" + usuario.getCadCelular() + "\'\r\n";
	textoArquivo += "Email: \'" + usuario.geteMail() + "\'\r\n";
	textoArquivo += "banco: \'" + usuario.getCadBanco() + "\'\r\n";
	textoArquivo += "tipo_conta: \'" + usuario.getCadTipoConta() + "\'\r\n";
	textoArquivo += "agencia: \'" + usuario.getCadAgencia() + "\'\r\n";
	textoArquivo += "conta: \'" + usuario.getCadCCorrente() + "\'\r\n";
	textoArquivo += "apelido: \'" + usuario.getApelido() + "\'\r\n";
	textoArquivo += "pisMis: \'" + usuario.getPisMis() + "\'\r\n";
	textoArquivo += "pasep: \'" + usuario.getPasep() + "\'\r\n";
	textoArquivo += "cnpj: \'" + usuario.getCnpj() + "\'\r\n";
	textoArquivo += "razaoSocial: \'" + usuario.getRazaoSocial() + "\'\r\n";
	textoArquivo += "nomeFantasia: \'" + usuario.getNomeFantasia() + "\'\r\n";
	textoArquivo += "inscricaoEstadual: \'" + usuario.getInscricaoEstadual() + "\'\r\n";
	textoArquivo += "agenciaBancoEspecifico: \'" + usuario.getAgenciaBancoEspecifico() + "\'\r\n";
	textoArquivo += "contaBancoEspecifico: \'" + usuario.getContaBancoEspecifico() + "\'\r\n";
	textoArquivo += "bancoPessoaJuridica: \'" + usuario.getBancoPessoaJuridica() + "\'\r\n";
	textoArquivo += "agenciaPessoaJuridica: \'" + usuario.getAgenciaPessoaJuridica() + "\'\r\n";
	textoArquivo += "contaPessoaJuridica: \'" + usuario.getContaPessoaJuridica() + "\'\r\n";
	textoArquivo += "agenciaPessoaJuridicaBancoEspecifico: \'" + usuario.getAgenciaPessoaJuridicaBancoEspecifico() + "\'\r\n";
	textoArquivo += "contaPessoaJuridicaBancoEspecifico: \'" + usuario.getContaPessoaJuridicaBancoEspecifico() + "\'\r\n";

	Usuario usuarioBanco = this.hibernateUtil.selecionar(new Usuario(this.sessaoUsuario.getUsuario().getId_Codigo()));
	usuarioBanco.setCPF(this.sessaoUsuario.getUsuario().getCPF());
	usuarioBanco.setvNome(usuario.getvNome());
	usuarioBanco.setDt_Nasc(usuario.getDt_Nasc());
	usuarioBanco.setCadRG(usuario.getCadRG());
	usuarioBanco.setCadOrgaoExpedidor(usuario.getCadOrgaoExpedidor());
	usuarioBanco.setCadSexo(usuario.getCadSexo());
	usuarioBanco.setCadEstCivil(usuario.getCadEstCivil());
	usuarioBanco.setCadCEP(usuario.getCadCEP());
	usuarioBanco.setCadEndereco(usuario.getCadEndereco());
	usuarioBanco.setCadBairro(usuario.getCadBairro());
	usuarioBanco.setCadCidade(usuario.getCadCidade());
	usuarioBanco.setCadUF(usuario.getCadUF());
	usuarioBanco.setTel(usuario.getTel());
	usuarioBanco.setCadCelular(usuario.getCadCelular());
	usuarioBanco.seteMail(usuario.geteMail());
	usuarioBanco.setCadBanco(usuario.getCadBanco());
	usuarioBanco.setCadTipoConta(usuario.getCadTipoConta());
	usuarioBanco.setCadAgencia(usuario.getCadAgencia());
	usuarioBanco.setCadCCorrente(usuario.getCadCCorrente());
	usuarioBanco.setPisMis(usuario.getPisMis());
	usuarioBanco.setPasep(usuario.getPasep());
	usuarioBanco.setCnpj(usuario.getCnpj());
	usuarioBanco.setRazaoSocial(usuario.getRazaoSocial());
	usuarioBanco.setNomeFantasia(usuario.getNomeFantasia());
	usuarioBanco.setInscricaoEstadual(usuario.getInscricaoEstadual());
	usuarioBanco.setAgenciaBancoEspecifico(usuario.getAgenciaBancoEspecifico());
	usuarioBanco.setContaBancoEspecifico(usuario.getContaBancoEspecifico());
	usuarioBanco.setBancoPessoaJuridica(usuario.getBancoPessoaJuridica());
	usuarioBanco.setAgenciaPessoaJuridica(usuario.getAgenciaPessoaJuridica());
	usuarioBanco.setContaPessoaJuridica(usuario.getContaPessoaJuridica());
	usuarioBanco.setAgenciaPessoaJuridicaBancoEspecifico(usuario.getAgenciaPessoaJuridicaBancoEspecifico());
	usuarioBanco.setContaPessoaJuridicaBancoEspecifico(usuario.getContaPessoaJuridicaBancoEspecifico());
	this.hibernateUtil.salvarOuAtualizar(usuarioBanco);

	ArquivoService.criarArquivoNoDisco(textoArquivo, ArquivoService.PASTA_ATUALIZACAO_DADOS);

	result.include("sucesso", "Seus dados foram alterados com sucesso");

	result.redirectTo(HomeController.class).home();
    }

    @Public
    public void salvarPreCadastroDistribuidor(Usuario preCadastro) throws Exception {

	result.include("preCadastro", preCadastro);

	Usuario usuario = preCadastro;
	usuario.setCPF(usuario.getCPF().replaceAll(" ", "").replaceAll("\\.", "").replaceAll("-", ""));

	String apelido = Normalizer.normalize(usuario.getApelido().toLowerCase().replaceAll(" ", ""), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

	Usuario usuarioFiltro = new Usuario();
	usuarioFiltro.setApelido(usuario.getApelido());
	if (hibernateUtil.contar(usuarioFiltro) != 0) {
	    validator.add(new ValidationMessage("Já existe alguém cadastrado com este nickname: " + usuario.getApelido() + ". Por favor, escolha outro.", "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaCadastro();
	    return;
	}

	usuarioFiltro = new Usuario();
	usuarioFiltro.setCPF(usuario.getCPF());
	if (hibernateUtil.contar(usuarioFiltro) != 0) {
	    validator.add(new ValidationMessage("Já existe alguem cadastrado com este CPF: " + usuario.getCPF(), "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaCadastro();
	    return;
	}

	usuarioFiltro = new Usuario();
	usuarioFiltro.setApelido(usuario.getNicknameQuemIndicou());
	if (hibernateUtil.contar(usuarioFiltro) != 1) {
	    validator.add(new ValidationMessage("Nao foi encontrado ninguém com este nickname: " + usuario.getNicknameQuemIndicou(), "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaCadastro();
	    return;
	}

	if (!Util.isValidCPF(usuario.getCPF())) {
	    validator.add(new ValidationMessage("CPF incorreto: " + usuario.getCPF(), "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaCadastro();
	    return;
	}

	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	try {
	    format.setLenient(false);
	    format.parse(usuario.getDt_Nasc());
	} catch (Exception e) {
	    validator.add(new ValidationMessage("Data de nascimento no formato incorreto. O formato correto é: dd/MM/yyyy", "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaCadastro();
	    return;
	}

	Integer anoCadastro = Integer.valueOf(usuario.getDt_Nasc().split("/")[2]);
	Integer anoAtual = new GregorianCalendar().get(Calendar.YEAR);

	if (anoAtual - anoCadastro < 18) {
	    validator.add(new ValidationMessage("Você precisa ser maior de 18 anos pra se cadastrar", "Erro"));
	    validator.onErrorRedirectTo(this).acessarTelaCadastro();
	    return;
	}

	Usuario usuarioQuemIndicou = hibernateUtil.selecionar(usuarioFiltro);

	String textoArquivo = "Nome: \'" + usuario.getvNome() + "\'\r\n";
	textoArquivo += "apelido: \'" + apelido + "\'\r\n";
	textoArquivo += "Data_de_nascimento: \'" + usuario.getDt_Nasc() + "\'\r\n";
	textoArquivo += "CPF: \'" + usuario.getCPF() + "\'\r\n";
	textoArquivo += "RG: \'" + usuario.getCadRG() + "\'\r\n";
	textoArquivo += "Emissor: \'" + usuario.getCadOrgaoExpedidor() + "\'\r\n";
	textoArquivo += "Sexo: \'" + usuario.getCadSexo() + "\'\r\n";
	textoArquivo += "Estado_civil: \'" + usuario.getCadEstCivil() + "\'\r\n";
	textoArquivo += "CEP: \'" + usuario.getCadCEP() + "\'\r\n";
	textoArquivo += "Endereco: \'" + usuario.getCadEndereco() + "\'\r\n";
	textoArquivo += "Bairro: \'" + usuario.getCadBairro() + "\'\r\n";
	textoArquivo += "Cidade: \'" + usuario.getCadCidade() + "\'\r\n";
	textoArquivo += "Estado: \'" + usuario.getCadUF() + "\'\r\n";
	textoArquivo += "Telefone_residencial: \'" + usuario.getTel() + "\'\r\n";
	textoArquivo += "Telefone_celular: \'" + usuario.getCadCelular() + "\'\r\n";
	textoArquivo += "Email: \'" + usuario.geteMail() + "\'\r\n";
	textoArquivo += "codigo_quem_indicou: \'" + usuarioQuemIndicou.getId_Codigo() + "\'\r\n";

	ArquivoService.criarArquivoNoDisco(textoArquivo, ArquivoService.PASTA_PRE_CADASTRO);

	result.redirectTo(this).sucessoCadastro();
    }

    @Public
    public void sucessoCadastro() {
    }
}
