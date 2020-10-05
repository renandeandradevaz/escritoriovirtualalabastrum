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
	<h5 class="corForte atualizacaoDadosCadastrais">ATUALIZA��O DE DADOS CADASTRAIS</h5>
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
			<label class="labelFormulario">Munic�pio de nascimento: </label>
			<input type="text" class="data" name="usuario.municipioNascimento" value="${usuario.municipioNascimento}" required="required">
			<br>
			<label class="labelFormulario">Estado de nascimento: </label>
			<input type="text" class="data" name="usuario.estadoNascimento" value="${usuario.estadoNascimento}" required="required">
			<br>
			<label class="labelFormulario">Pa�s de nascimento: </label>
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
				<option value="Vi�vo(a)" <c:if test="${usuario.cadEstCivil == 'Vi�vo(a)'}"> selected="selected" </c:if>>Vi�vo(a)</option>
			</select>
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario"> Cor/Ra�a: </label>
			<input type="text" name="usuario.corRaca" value="${usuario.corRaca}" required="required">
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario"> Grau de instru��o: </label>
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
			<label class="labelFormulario"> Data de expedi��o (dd/mm/yyyy): </label>
			<input type="text" name="usuario.dataExpedicao" value="${usuario.dataExpedicao}" required="required">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario"> N�mero da carteira de trabalho: </label>
			<input type="text" name="usuario.numeroCarteiraTrabalho" value="${usuario.numeroCarteiraTrabalho}" required="required">
			<br>
			<label class="labelFormulario"> S�rie da carteira de trabalho: </label>
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
			<label class="labelFormulario">N�mero do endere�o: </label>
			<input type="text" name="usuario.numeroEndereco" value="${usuario.numeroEndereco}" required="required">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Complemento do endere�o: </label>
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
				<option value="AP">Amap�</option>
				<option value="AM">Amazonas</option>
				<option value="BA">Bahia</option>
				<option value="CE">Cear�</option>
				<option value="DF">Distrito Federal</option>
				<option value="ES">Espirito Santo</option>
				<option value="GO">Goi�s</option>
				<option value="MA">Maranh�o</option>
				<option value="MT">Mato Grosso</option>
				<option value="MS">Mato Grosso do Sul</option>
				<option value="MG">Minas Gerais</option>
				<option value="PA">Par�</option>
				<option value="PB">Paraiba</option>
				<option value="PR">Paran�</option>
				<option value="PE">Pernambuco</option>
				<option value="PI">Piau�</option>
				<option value="RJ">Rio de Janeiro</option>
				<option value="RN">Rio Grande do Norte</option>
				<option value="RS">Rio Grande do Sul</option>
				<option value="RO">Rond�nia</option>
				<option value="RR">Roraima</option>
				<option value="SC">Santa Catarina</option>
				<option value="SP">S�o Paulo</option>
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
			<label class="labelFormulario">Seu PASEP est� ativo? </label>
			<select name="usuario.pasepAtivo">
				<option value="Sim" <c:if test="${usuario.pasepAtivo == 'Sim'}"> selected="selected" </c:if>>Sim</option>
				<option value="Nao" <c:if test="${usuario.pasepAtivo == 'Nao'}"> selected="selected" </c:if>>Nao</option>
			</select>
			<br>
			<label class="labelFormulario">CNPJ: </label>
			<input type="text" name="usuario.cnpj" value="${usuario.cnpj}">
			<label class="labelFormulario">Raz�o social: </label>
			<input type="text" name="usuario.razaoSocial" value="${usuario.razaoSocial}">
			<br>
			<label class="labelFormulario">Nome fantasia: </label>
			<input type="text" name="usuario.nomeFantasia" value="${usuario.nomeFantasia}">
			<br>
			<label class="labelFormulario">Inscri��o estadual: </label>
			<input type="text" name="usuario.inscricaoEstadual" value="${usuario.inscricaoEstadual}">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Nome da m�e </label>
			<input type="text" name="usuario.nomeMae" value="${usuario.nomeMae}">
			<br>
			<label class="labelFormulario">Nome do pai: </label>
			<input type="text" name="usuario.nomePai" value="${usuario.nomePai}">
		</div>
		<div class="divFormulario">
			<label class="labelFormulario">
				<b> Dados banc�rios para pagamento de bonifica��o </b>
			</label>
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Banco: </label>
			<input type="text" name="usuario.cadBanco" value="${usuario.cadBanco}">
			<label class="labelFormulario">Tipo de conta: </label>
			<select name="usuario.cadTipoConta">
				<option value="Conta corrente" <c:if test="${usuario.cadTipoConta == 'Conta corrente'}"> selected="selected" </c:if>>Conta corrente</option>
				<option value="Conta poupan�a" <c:if test="${usuario.cadTipoConta == 'Conta poupan�a'}"> selected="selected" </c:if>>Conta poupan�a</option>
				<option value="Conta sal�rio" <c:if test="${usuario.cadTipoConta == 'Conta sal�rio'}"> selected="selected" </c:if>>Conta sal�rio</option>
			</select>
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">N� Ag�ncia: </label>
			<input type="text" name="usuario.cadAgencia" value="${usuario.cadAgencia}">
			<label class="labelFormulario">N� da Conta: </label>
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
			<label class="labelFormulario">N� Ag�ncia: </label>
			<input type="text" name="usuario.agenciaBancoEspecifico" value="${usuario.agenciaBancoEspecifico}">
			<label class="labelFormulario">N� da Conta: </label>
			<input type="text" name="usuario.contaBancoEspecifico" value="${usuario.contaBancoEspecifico}">
		</div>
		<div class="divFormulario">
			<label class="labelFormulario">
				<b> Dados banc�rios Pessoa Jur�dica </b>
			</label>
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Banco: </label>
			<input type="text" name="usuario.bancoPessoaJuridica" value="${usuario.bancoPessoaJuridica}">
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">N� Ag�ncia: </label>
			<input type="text" name="usuario.agenciaPessoaJuridica" value="${usuario.agenciaPessoaJuridica}">
			<label class="labelFormulario">N� da Conta: </label>
			<input type="text" name="usuario.contaPessoaJuridica" value="${usuario.contaPessoaJuridica}">
		</div>
		<div class="divFormulario">
			<label class="labelFormulario">
				<b> Conta Nubank Pessoa Jur�dica </b>
			</label>
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Banco: </label>
			<input type="text" disabled="disabled" value="260 - Nubank">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">N� Ag�ncia: </label>
			<input type="text" name="usuario.agenciaPessoaJuridicaBancoEspecifico" value="${usuario.agenciaPessoaJuridicaBancoEspecifico}">
			<label class="labelFormulario">N� da Conta: </label>
			<input type="text" name="usuario.contaPessoaJuridicaBancoEspecifico" value="${usuario.contaPessoaJuridicaBancoEspecifico}">
		</div>
		<div class="corClara divFormulario" style="margin: 5%; border: 1px solid black;">
			<h5>Ap�s finalizar a atualiza��o dos seus dados aqui no Escrit�rio virtual, voc� deve enviar as fotos dos seus documentos.</h5>
			<h5>Para enviar, basta acessar o link abaixo e preencher todos os campos do formul�rio.</h5>
			<br>
			<br>
			<a href="http://miguelprado.com.br">CLIQUE AQUI PARA ENVIAR SUA DOCUMENTA��O</a>
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
