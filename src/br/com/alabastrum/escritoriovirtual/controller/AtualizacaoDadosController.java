package br.com.alabastrum.escritoriovirtual.controller;

import java.text.Normalizer;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.anotacoes.Public;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.ArquivoService;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
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

	ArquivoService.criarArquivoNoDisco(textoArquivo, ArquivoService.PASTA_ATUALIZACAO_DADOS);

	result.include("sucesso", "Sua solicitação de alteração de dados cadastrais foi feita com sucesso. Estamos avaliando seus dados, e em breve faremos a atualização no sistema.");

	result.redirectTo(HomeController.class).home();
    }

    @Public
    public void salvarPreCadastroDistribuidor(Usuario preCadastro) throws Exception {

	result.include("preCadastro", preCadastro);

	Usuario usuario = preCadastro;

	String apelido = Normalizer.normalize(usuario.getApelido().toLowerCase().replaceAll(" ", ""), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

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

	Usuario usuarioFiltro = new Usuario();
	usuarioFiltro.setApelido(usuario.getApelido());
	if (hibernateUtil.contar(usuarioFiltro) != 0) {
	    validator.add(new ValidationMessage("Já existe alguém cadastrado com este nickname: " + usuario.getApelido() + ". Por favor, escolha outro.", "Erro"));
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

	Usuario usuarioQuemIndicou = hibernateUtil.selecionar(usuarioFiltro);
	textoArquivo += "codigo_quem_indicou: \'" + usuarioQuemIndicou.getId_Codigo() + "\'\r\n";

	ArquivoService.criarArquivoNoDisco(textoArquivo, ArquivoService.PASTA_PRE_CADASTRO);

	result.redirectTo(this).sucessoCadastro();
    }

    @Public
    public void sucessoCadastro() {
    }
}
