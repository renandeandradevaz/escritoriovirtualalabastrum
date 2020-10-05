<%@ include file="/base.jsp"%>
<style>
.divAtualizacaoDados {
	border: 1px solid rgb(152, 160, 233);
	padding-top: 10px;
	border-radius: 6px;
}

.divInformacoesUsuarios {
	padding-left: 20px;
	padding-right: 20px;
}

.labelEsquerda {
	display: inline;
	font-weight: bold;
}

.spanDireita {
	float: none;
	margin-left: 10px;
}

hr {
	margin: 10px 0;
	margin-bottom: 20px;
	border: 0;
	border-top: 1px solid #ccc;
	border-bottom: 1px solid #ffffff;
}

.atualizacaoDadosCadastrais {
	margin-top: 30px;
	height: 27px;
	padding-top: 8px;
	padding-left: 20px;
	margin-bottom: 1px;
}

.corForte {
	background-color: rgb(165, 165, 165);
}

.corMedia {
	background-color: rgb(210, 210, 210);
}

.corClara {
	background-color: rgb(233, 233, 233);
}

.labelFormulario {
	display: inline-block;
	margin-left: 20px;
}

.divFormulario {
	padding: 10px;
	padding-bottom: 5px;
	margin-bottom: 1px;
}
</style>
<div class="divAtualizacaoDados" style="background-color: #e3efef; max-width: 700px;">
	<div class="divInformacoesUsuarios">
		<h4>DADOS CADASTRAIS</h4>
		<hr>
		<label class="labelEsquerda"> Nickname: </label>
		<span class="spanDireita"> ${usuario.apelido} </span>
		<br>
		<br>
		<label class="labelEsquerda"> Nome completo: </label>
		<span class="spanDireita"> ${usuario.vNome} </span>
		<br>
		<br>
		<label class="labelEsquerda"> CPF: </label>
		<span class="spanDireita"> ${usuario.CPF} </span>
		<br>
	</div>
	<h5 class="corForte atualizacaoDadosCadastrais">ATUALIZAÇÃO DE DADOS CADASTRAIS</h5>
	<form action="<c:url value="/atualizacaoDados/salvarAtualizacaoDados"/>" method="post">
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Nome: </label>
			<input type="text" name="usuario.vnome" value="${usuario.vNome}" required="required">
			<input type="hidden" name="usuario.apelido" value="${usuario.apelido}" required="required" disabled="disabled">
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Data de nascimento: </label>
			<input type="text" class="data" name="usuario.dt_Nasc" value="${usuario.dt_Nasc}" required="required">
			<br>
			<label class="labelFormulario">Município de nascimento: </label>
			<input type="text" class="data" name="usuario.municipioNascimento" value="${usuario.municipioNascimento}" required="required">
			<br>
			<label class="labelFormulario">Estado de nascimento: </label>
			<input type="text" class="data" name="usuario.estadoNascimento" value="${usuario.estadoNascimento}" required="required">
			<br>
			<label class="labelFormulario">País de nascimento: </label>
			<input type="text" class="data" name="usuario.paisNascimento" value="${usuario.paisNascimento}" required="required">
			<br>
			<label class="labelFormulario">Sexo: </label>
			<select name="usuario.cadSexo">
				<option value="Masculino" <c:if test="${usuario.cadSexo == 'Masculino'}"> selected="selected" </c:if>>Masculino</option>
				<option value="Feminino" <c:if test="${usuario.cadSexo == 'Feminino'}"> selected="selected" </c:if>>Feminino</option>
			</select>
			<label class="labelFormulario">Estado civil: </label>
			<select name="usuario.cadEstCivil">
				<option value="Solteiro(a)" <c:if test="${usuario.cadEstCivil == 'Solteiro(a)'}"> selected="selected" </c:if>>Solteiro(a)</option>
				<option value="Casado(a)" <c:if test="${usuario.cadEstCivil == 'Casado(a)'}"> selected="selected" </c:if>>Casado(a)</option>
				<option value="Divorciado(a)" <c:if test="${usuario.cadEstCivil == 'Divorciado(a)'}"> selected="selected" </c:if>>Divorciado(a)</option>
				<option value="Viúvo(a)" <c:if test="${usuario.cadEstCivil == 'Viúvo(a)'}"> selected="selected" </c:if>>Viúvo(a)</option>
			</select>
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario"> Cor/Raça: </label>
			<input type="text" name="usuario.corRaca" value="${usuario.corRaca}" required="required">
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario"> Grau de instrução: </label>
			<input type="text" name="usuario.grauInstrucao" value="${usuario.grauInstrucao}" required="required">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario"> RG: </label>
			<input type="text" name="usuario.cadRG" value="${usuario.cadRG}" required="required">
			<br>
			<label class="labelFormulario"> Emissor: </label>
			<input type="text" name="usuario.cadOrgaoExpedidor" value="${usuario.cadOrgaoExpedidor}" required="required">
			<br>
			<label class="labelFormulario"> Estado Emissor: </label>
			<input type="text" name="usuario.estadoEmissor" value="${usuario.estadoEmissor}" required="required">
			<br>
			<label class="labelFormulario"> Data de expedição (dd/mm/yyyy): </label>
			<input type="text" name="usuario.dataExpedicao" value="${usuario.dataExpedicao}" required="required">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario"> Número da carteira de trabalho: </label>
			<input type="text" name="usuario.numeroCarteiraTrabalho" value="${usuario.numeroCarteiraTrabalho}" required="required">
			<br>
			<label class="labelFormulario"> Série da carteira de trabalho: </label>
			<input type="text" name="usuario.serieCarteiraTrabalho" value="${usuario.serieCarteiraTrabalho}" required="required">
			<br>
			<label class="labelFormulario"> UF da carteira de trabalho: </label>
			<input type="text" name="usuario.ufCarteiraTrabalho" value="${usuario.ufCarteiraTrabalho}" required="required">
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">CEP: </label>
			<input type="text" name="usuario.cadCEP" value="${usuario.cadCEP}" required="required">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">End. Resid.: </label>
			<input type="text" name="usuario.cadEndereco" value="${usuario.cadEndereco}" required="required">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Número do endereço: </label>
			<input type="text" name="usuario.numeroEndereco" value="${usuario.numeroEndereco}" required="required">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Complemento do endereço: </label>
			<input type="text" name="usuario.complementoEndereco" value="${usuario.complementoEndereco}" required="required">
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Bairro: </label>
			<input type="text" name="usuario.cadBairro" value="${usuario.cadBairro}" required="required">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Cidade: </label>
			<input type="text" name="usuario.cadCidade" value="${usuario.cadCidade}" required="required">
			<br>
			<label class="labelFormulario">Estado: </label>
			<select id='uf' name="usuario.cadUF">
				<option value="AC">Acre</option>
				<option value="AL">Alagoas</option>
				<option value="AP">Amapá</option>
				<option value="AM">Amazonas</option>
				<option value="BA">Bahia</option>
				<option value="CE">Ceará</option>
				<option value="DF">Distrito Federal</option>
				<option value="ES">Espirito Santo</option>
				<option value="GO">Goiás</option>
				<option value="MA">Maranhão</option>
				<option value="MT">Mato Grosso</option>
				<option value="MS">Mato Grosso do Sul</option>
				<option value="MG">Minas Gerais</option>
				<option value="PA">Pará</option>
				<option value="PB">Paraiba</option>
				<option value="PR">Paraná</option>
				<option value="PE">Pernambuco</option>
				<option value="PI">Piauí</option>
				<option value="RJ">Rio de Janeiro</option>
				<option value="RN">Rio Grande do Norte</option>
				<option value="RS">Rio Grande do Sul</option>
				<option value="RO">Rondônia</option>
				<option value="RR">Roraima</option>
				<option value="SC">Santa Catarina</option>
				<option value="SP">São Paulo</option>
				<option value="SE">Sergipe</option>
				<option value="TO">Tocantis</option>
			</select>
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Tel. resid.: </label>
			<input type="text" name="usuario.tel" value="${usuario.tel}" required="required">
			<label class="labelFormulario">Tel. Cel.: </label>
			<input type="text" name="usuario.cadCelular" value="${usuario.cadCelular}" required="required">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Email: </label>
			<input type="text" name="usuario.email" value="${usuario.eMail}" required="required">
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">PIS/MIS: </label>
			<input type="text" name="usuario.pisMis" value="${usuario.pisMis}">
			<label class="labelFormulario">PASEP: </label>
			<input type="text" name="usuario.pasep" value="${usuario.pasep}">
			<br>
			<label class="labelFormulario">Seu PASEP está ativo? </label>
			<select name="usuario.pasepAtivo">
				<option value="Sim" <c:if test="${usuario.pasepAtivo == 'Sim'}"> selected="selected" </c:if>>Sim</option>
				<option value="Nao" <c:if test="${usuario.pasepAtivo == 'Nao'}"> selected="selected" </c:if>>Nao</option>
			</select>
			<br>
			<label class="labelFormulario">CNPJ: </label>
			<input type="text" name="usuario.cnpj" value="${usuario.cnpj}">
			<label class="labelFormulario">Razão social: </label>
			<input type="text" name="usuario.razaoSocial" value="${usuario.razaoSocial}">
			<br>
			<label class="labelFormulario">Nome fantasia: </label>
			<input type="text" name="usuario.nomeFantasia" value="${usuario.nomeFantasia}">
			<br>
			<label class="labelFormulario">Inscrição estadual: </label>
			<input type="text" name="usuario.inscricaoEstadual" value="${usuario.inscricaoEstadual}">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Nome da mãe </label>
			<input type="text" name="usuario.nomeMae" value="${usuario.nomeMae}">
			<br>
			<label class="labelFormulario">Nome do pai: </label>
			<input type="text" name="usuario.nomePai" value="${usuario.nomePai}">
		</div>
		<div class="divFormulario">
			<label class="labelFormulario">
				<b> Dados bancários para pagamento de bonificação </b>
			</label>
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Banco: </label>
			<input type="text" name="usuario.cadBanco" value="${usuario.cadBanco}">
			<label class="labelFormulario">Tipo de conta: </label>
			<select name="usuario.cadTipoConta">
				<option value="Conta corrente" <c:if test="${usuario.cadTipoConta == 'Conta corrente'}"> selected="selected" </c:if>>Conta corrente</option>
				<option value="Conta poupança" <c:if test="${usuario.cadTipoConta == 'Conta poupança'}"> selected="selected" </c:if>>Conta poupança</option>
				<option value="Conta salário" <c:if test="${usuario.cadTipoConta == 'Conta salário'}"> selected="selected" </c:if>>Conta salário</option>
			</select>
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Nº Agência: </label>
			<input type="text" name="usuario.cadAgencia" value="${usuario.cadAgencia}">
			<label class="labelFormulario">Nº da Conta: </label>
			<input type="text" name="usuario.cadCCorrente" value="${usuario.cadCCorrente}">
		</div>
		<div class="divFormulario">
			<label class="labelFormulario">
				<b> Conta Nubank </b>
			</label>
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Banco: </label>
			<input type="text" disabled="disabled" value="260 - Nubank">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Nº Agência: </label>
			<input type="text" name="usuario.agenciaBancoEspecifico" value="${usuario.agenciaBancoEspecifico}">
			<label class="labelFormulario">Nº da Conta: </label>
			<input type="text" name="usuario.contaBancoEspecifico" value="${usuario.contaBancoEspecifico}">
		</div>
		<div class="divFormulario">
			<label class="labelFormulario">
				<b> Dados bancários Pessoa Jurídica </b>
			</label>
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Banco: </label>
			<input type="text" name="usuario.bancoPessoaJuridica" value="${usuario.bancoPessoaJuridica}">
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Nº Agência: </label>
			<input type="text" name="usuario.agenciaPessoaJuridica" value="${usuario.agenciaPessoaJuridica}">
			<label class="labelFormulario">Nº da Conta: </label>
			<input type="text" name="usuario.contaPessoaJuridica" value="${usuario.contaPessoaJuridica}">
		</div>
		<div class="divFormulario">
			<label class="labelFormulario">
				<b> Conta Nubank Pessoa Jurídica </b>
			</label>
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Banco: </label>
			<input type="text" disabled="disabled" value="260 - Nubank">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Nº Agência: </label>
			<input type="text" name="usuario.agenciaPessoaJuridicaBancoEspecifico" value="${usuario.agenciaPessoaJuridicaBancoEspecifico}">
			<label class="labelFormulario">Nº da Conta: </label>
			<input type="text" name="usuario.contaPessoaJuridicaBancoEspecifico" value="${usuario.contaPessoaJuridicaBancoEspecifico}">
		</div>
		<div class="corClara divFormulario" style="margin: 5%; border: 1px solid black;">
			<h5>Após finalizar a atualização dos seus dados aqui no Escritório virtual, você deve enviar as fotos dos seus documentos.</h5>
			<h5>Para enviar, basta acessar o link abaixo e preencher todos os campos do formulário.</h5>
			<br>
			<br>
			<a href="http://miguelprado.com.br">CLIQUE AQUI PARA ENVIAR SUA DOCUMENTAÇÃO</a>
			<br>
			<br>
		</div>
		<div style="margin-top: 20px; margin-left: 20px">
			<button type="submit" class="btn btn-primary">Enviar</button>
			<a class="btn btn-danger" href="<c:url value="/home/home"/>"> Cancelar </a>
		</div>
	</form>
</div>
<script>
	$('#uf').val('${usuario.cadUF}');
</script>
