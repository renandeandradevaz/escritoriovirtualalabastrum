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

<div style="width: 800px">

	<br>
	<br>
	<h1>Dunastes - Cadastro</h1>
	<br>
	<br>

	<form
		action="https://ev.dunastes.com.br/atualizacaoDados/salvarPreCadastroDistribuidorPeloSite">

		<div class="divFormulario">
			<label class="labelFormulario"> <b> Quem te indicou? </b></label>
		</div>

		<div class="corMedia divFormulario">
			<label class="labelFormulario">Código: </label> <input type="text"
				style="width: 150px; height: 45px; border-radius: 5px;"
				name="sessaoAtualizacaoDados.codigoQuemIndicou" value=""
				required="required" id='codigoQuemIndicou'
				placeholder="Informe o codigo">

			<button style="margin: 5px 0px;" id='descobrir-nome'
				class="btn btn-primary">Buscar</button>
			<br /> <label class="labelFormulario">Nome: </label> <input
				type="text"
				style="width: 350px; height: 45px; border-radius: 5px; margin: 5px 0px 5px 0px;"
				name="sessaoAtualizacaoDadosnomeQuemIndicou" value=""
				disabled="disabled" id="nomeQuemIndicou">
		</div>

		<div class="divFormulario">
			<label class="labelFormulario"> <b> Seus dados: </b></label>
		</div>

		<div class="corMedia divFormulario">
			<label class="labelFormulario">Nome: </label> <input type="text"
				style="width: 585px; height: 45px; border-radius: 5px;"
				name="sessaoAtualizacaoDados.vnome" value="" required="required">
		</div>

		<div class="corClara divFormulario">
			<label class="labelFormulario">Data nasc.: </label> <input
				type="text" style="width: 120px; height: 45px; border-radius: 5px;"
				class="data" name="sessaoAtualizacaoDados.dt_Nasc" value=""
				onkeypress="mascaraData( this, event )"> <label
				class="labelFormulario">Sexo: </label> <select
				name="sessaoAtualizacaoDados.cadSexo"
				style="width: 120px; height: 45px; border-radius: 5px;">
				<option value="Masculino">Masc</option>
				<option value="Feminino">Fem</option>
			</select> <label class="labelFormulario">Estado civil: </label> <select
				name="sessaoAtualizacaoDados.cadEstCivil"
				style="width: 150px; height: 45px; border-radius: 5px;">
				<option value="Solteiro(a)">Solteiro(a)</option>
				<option value="Casado(a)">Casado(a)</option>
				<option value="Divorciado(a)">Divorciado(a)</option>
				<option value="ViÃºvo(a)">Viúvo(a)</option>
			</select>
		</div>

		<div class="corMedia divFormulario">
			<label class="labelFormulario"> CPF: </label> <input type="text"
				style="width: 150px; height: 45px; border-radius: 5px;"
				name="sessaoAtualizacaoDados.CPF" value=""
				onBlur="Verifica_campo_CPF(this)"> <label
				class="labelFormulario"> RG: </label> <input type="text"
				style="width: 150px; height: 45px; border-radius: 5px;"
				name="sessaoAtualizacaoDados.cadRG" value=""> <label
				class="labelFormulario"> Emissor: </label> <input type="text"
				style="width: 140px; height: 45px; border-radius: 5px;"
				name="sessaoAtualizacaoDados.cadOrgaoExpedidor" value="">
		</div>

		<div class="corClara divFormulario">
			<label class="labelFormulario">CEP: </label> <input type="text"
				style="width: 120px; height: 45px; border-radius: 5px;"
				name="sessaoAtualizacaoDados.cadCEP" value=""
				onkeypress="mascara(this, '#####-###')"> <a
				style="margin-left: 30px;"
				href="http://www.buscacep.correios.com.br" target="_blank">
				Pesquisar CEP </a>
		</div>

		<div class="corMedia divFormulario">
			<label class="labelFormulario">End. Resid.: </label> <input
				type="text" style="width: 550px; height: 45px; border-radius: 5px;"
				name="sessaoAtualizacaoDados.cadEndereco" value="">
		</div>

		<div class="corClara divFormulario">
			<label class="labelFormulario">Bairro: </label> <input type="text"
				style="width: 585px; height: 45px; border-radius: 5px;"
				name="sessaoAtualizacaoDados.cadBairro" value="">
		</div>

		<div class="corMedia divFormulario">
			<label class="labelFormulario">Cidade: </label> <input type="text"
				style="width: 340px; height: 45px; border-radius: 5px;"
				name="sessaoAtualizacaoDados.cadCidade" value=""> <label
				class="labelFormulario">Estado: </label> <select
				name="sessaoAtualizacaoDados.cadUF"
				style="width: 160px; height: 45px; border-radius: 5px;">
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
				<option value="PI">Piauí­</option>
				<option value="RJ" selected="selected">Rio de Janeiro</option>
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
			<label class="labelFormulario">Tel. resid.: </label> <input
				type="text" style="width: 225px; height: 45px; border-radius: 5px;"
				name="sessaoAtualizacaoDados.tel" value=""> <label
				class="labelFormulario">Tel. Cel.: </label> <input type="text"
				style="width: 235px; height: 45px; border-radius: 5px;"
				name="sessaoAtualizacaoDados.cadCelular" value="">

		</div>

		<div class="corMedia divFormulario">
			<label class="labelFormulario">Email: </label> <input type="text"
				style="width: 585px; height: 45px; border-radius: 5px;"
				name="sessaoAtualizacaoDados.email" value="" required="required">
		</div>


		<div style="margin-top: 20px; margin-left: 20px">
			<button type="submit" class="btn btn-primary"
				onclick="return validar()">Enviar</button>

		</div>
	</form>
</div>


<script type="text/javascript">
	jQuery(document).ready(function() {

		var currentPage = window.location.href;

		if (currentPage.includes("codigo=")) {
			var codigo = currentPage.split("codigo=")[1];
			jQuery("#codigoQuemIndicou").val(codigo);
		}
	});

	jQuery("#descobrir-nome")
			.click(
					function() {
						jQuery
								.ajax({
									'async' : false,
									'global' : false,
									'crossDomain' : true,
									'url' : "https://ev.dunastes.com.br/distribuidor/obterNomeDistribuidor?codigo=" + jQuery('#codigoQuemIndicou')
											+ jQuery('#codigoQuemIndicou')
													.val(),
									dataType : 'json',
									'success' : function(data) {

										jQuery('#nomeQuemIndicou').val(
												data.string);
									}
								});
					});
</script>