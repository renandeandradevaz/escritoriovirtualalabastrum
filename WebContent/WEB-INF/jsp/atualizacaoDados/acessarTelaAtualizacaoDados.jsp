<%@ include file="/base.jsp"%>
<style>
.divAtualizacaoDados {
	width: 710px;
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
<div class="divAtualizacaoDados" style="background-color: #e3efef;">
	<div class="divInformacoesUsuarios">
		<h4>DADOS CADASTRAIS</h4>
		<hr>
		<label class="labelEsquerda"> Código de identificação: </label>
		<span class="spanDireita"> ${usuario.id_Codigo} </span>
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
			<input type="text" style="width: 400px;" name="usuario.vnome" value="${usuario.vNome}">
			<label class="labelFormulario">Nickname: </label>
			  <input type="text" style="width: 200px;" name="usuario.nickname" value="${usuario.nickname}">
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Data nasc.: </label>
			<input type="text" style="width: 90px;" class="data" name="usuario.dt_Nasc" value="${usuario.dt_Nasc}">
			<label class="labelFormulario">Sexo: </label>
			<select name="usuario.cadSexo" style="width: 130px;">
				<option value="Masculino" <c:if test="${usuario.cadSexo == 'Masc'}"> selected="selected" </c:if>>Masc</option>
				<option value="Feminino" <c:if test="${usuario.cadSexo == 'Fem'}"> selected="selected" </c:if>>Fem</option>
			</select>
			<label class="labelFormulario">Estado civil: </label>
			<select name="usuario.cadEstCivil" style="width: 170px;">
				<option value="Solteiro(a)" <c:if test="${usuario.cadEstCivil == 'Solteiro(a)'}"> selected="selected" </c:if>>Solteiro(a)</option>
				<option value="Casado(a)" <c:if test="${usuario.cadEstCivil == 'Casado(a)'}"> selected="selected" </c:if>>Casado(a)</option>
				<option value="Divorciado(a)" <c:if test="${usuario.cadEstCivil == 'Divorciado(a)'}"> selected="selected" </c:if>>Divorciado(a)</option>
				<option value="Viúvo(a)" <c:if test="${usuario.cadEstCivil == 'Viúvo(a)'}"> selected="selected" </c:if>>Viúvo(a)</option>
			</select>
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario"> RG: </label>
			<input type="text" style="width: 250px;" name="usuario.cadRG" value="${usuario.cadRG}">
			<label class="labelFormulario"> Emissor: </label>
			<input type="text" style="width: 260px;" name="usuario.cadOrgaoExpedidor" value="${usuario.cadOrgaoExpedidor}">
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">CEP: </label>
			<input type="text" style="width: 90px;" name="usuario.cadCEP" value="${usuario.cadCEP}">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">End. Resid.: </label>
			<input type="text" style="width: 550px;" name="usuario.cadEndereco" value="${usuario.cadEndereco}">
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Bairro: </label>
			<input type="text" style="width: 585px;" name="usuario.cadBairro" value="${usuario.cadBairro}">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Cidade: </label>
			<input type="text" style="width: 340px;" name="usuario.cadCidade" value="${usuario.cadCidade}">
			<label class="labelFormulario">Estado: </label>
			<select id='uf' name="usuario.cadUF" style="width: 160px;">
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
			<input type="text" style="width: 225px;" name="usuario.tel" value="${usuario.tel}">
			<label class="labelFormulario">Tel. Cel.: </label>
			<input type="text" style="width: 235px;" name="usuario.cadCelular" value="${usuario.cadCelular}">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Email: </label>
			<input type="text" style="width: 585px;" name="usuario.email" value="${usuario.eMail}">
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">PIS/MIS: </label>
			<input type="text" style="width: 200px;" name="usuario.pisMis" value="${usuario.pisMis}">
			<label class="labelFormulario">PASEP: </label>
			<input type="text" style="width: 200px;" name="usuario.pasep" value="${usuario.pasep}">
			<label class="labelFormulario">CNPJ: </label>
			<input type="text" style="width: 200px;" name="usuario.cnpj" value="${usuario.cnpj}">
			<label class="labelFormulario">Razão social: </label>
			<input type="text" style="width: 200px;" name="usuario.razaoSocial" value="${usuario.razaoSocial}">
			<label class="labelFormulario">Nome fantasia: </label>
			<input type="text" style="width: 200px;" name="usuario.nomeFantasia" value="${usuario.nomeFantasia}">
			<label class="labelFormulario">Inscrição estadual: </label>
			<input type="text" style="width: 200px;" name="usuario.inscricaoEstadual" value="${usuario.inscricaoEstadual}">
		</div>
		<div class="divFormulario">
			<label class="labelFormulario">
				<b> Dados bancários para pagamento de bonificação </b>
			</label>
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Banco: </label>
			<input type="text" style="width: 200px;" name="usuario.cadBanco" value="${usuario.cadBanco}">
			<label class="labelFormulario">Tipo de conta: </label>
			<select name="usuario.cadTipoConta" style="width: 200px;">
				<option value="Conta corrente" <c:if test="${usuario.cadTipoConta == 'Conta corrente'}"> selected="selected" </c:if>>Conta corrente</option>
				<option value="Conta poupança" <c:if test="${usuario.cadTipoConta == 'Conta poupança'}"> selected="selected" </c:if>>Conta poupança</option>
				<option value="Conta salário" <c:if test="${usuario.cadTipoConta == 'Conta salário'}"> selected="selected" </c:if>>Conta salário</option>
			</select>
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Nº Agência: </label>
			<input type="text" style="width: 170px;" name="usuario.cadAgencia" value="${usuario.cadAgencia}">
			<label class="labelFormulario">Nº da Conta: </label>
			<input type="text" style="width: 200px;" name="usuario.cadCCorrente" value="${usuario.cadCCorrente}">
		</div>
		
		<div class="divFormulario">
			<label class="labelFormulario">
				<b> Conta nubank </b>
			</label>
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Banco: </label>
			<input type="text" disabled="disabled" style="width: 200px;" value="260 - Nubank">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Nº Agência: </label>
			<input type="text" style="width: 170px;" name="usuario.agenciaBancoEspecifico" value="${usuario.agenciaBancoEspecifico}">
			<label class="labelFormulario">Nº da Conta: </label>
			<input type="text" style="width: 200px;" name="usuario.contaBancoEspecifico" value="${usuario.contaBancoEspecifico}">
		</div>
		
		<div class="divFormulario">
			<label class="labelFormulario">
				<b> Dados bancários Pessoa jurídica </b>
			</label>
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Banco: </label>
			<input type="text" style="width: 200px;" name="usuario.bancoPessoaJuridica" value="${usuario.bancoPessoaJuridica}">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Nº Agência: </label>
			<input type="text" style="width: 170px;" name="usuario.agenciaPessoaJuridica" value="${usuario.agenciaPessoaJuridica}">
			<label class="labelFormulario">Nº da Conta: </label>
			<input type="text" style="width: 200px;" name="usuario.contaPessoaJuridica" value="${usuario.contaPessoaJuridica}">
		</div>
		
		<div class="divFormulario">
			<label class="labelFormulario">
				<b> Conta nubank Pessoa jurídica </b>
			</label>
		</div>
		<div class="corClara divFormulario">
			<label class="labelFormulario">Banco: </label>
			<input type="text" disabled="disabled" style="width: 200px;" value="260 - Nubank">
		</div>
		<div class="corMedia divFormulario">
			<label class="labelFormulario">Nº Agência: </label>
			<input type="text" style="width: 170px;" name="usuario.agenciaPessoaJuridicaBancoEspecifico" value="${usuario.agenciaPessoaJuridicaBancoEspecifico}">
			<label class="labelFormulario">Nº da Conta: </label>
			<input type="text" style="width: 200px;" name="usuario.contaPessoaJuridicaBancoEspecifico" value="${usuario.contaPessoaJuridicaBancoEspecifico}">
		</div>
		
		<div style="margin-top: 20px; margin-left: 20px">
			<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Enviar</button>
			<a class="btn btn-danger" href="<c:url value="/home/home"/>"> Cancelar </a>
		</div>
	</form>
</div>
<script>
	$('#uf').val('${usuario.cadUF}');
</script>
