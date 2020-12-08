<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Informe seus dados pessoais</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pedido/salvarDadosComprador"/>" method="post">
		<fieldset>
			<div class="control-group">
				<label class="control-label">Nome completo</label>
				<div class="controls">
					<input type="text" name="nome" required="required">
					<span style="color: grey">Ex: Ana Clara Farias de Almeida</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">CPF</label>
				<div class="controls">
					<input type="text" name="cpf" required="required">
					<span style="color: grey">Ex: 136.289.507-03</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">E-mail</label>
				<div class="controls">
					<input type="email" name="email" required="required">
					<span style="color: grey">Ex: acf.almeida@gmail.com</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Telefone celular com DDD</label>
				<div class="controls">
					<input type="text" name="telefone" required="required">
					<span style="color: grey">Ex: (21) 98316-7924</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">CEP</label>
				<div class="controls">
					<input type="text" name="cep" required="required">
					<span style="color: grey">Ex: 25235-006</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Bairro</label>
				<div class="controls">
					<input type="text" name="bairro" required="required">
					<span style="color: grey">Ex: Copacabana</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Cidade</label>
				<div class="controls">
					<input type="text" name="cidade" required="required">
					<span style="color: grey">Ex: Nova Igua�u</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Estado</label>
				<div class="controls">
					<input type="text" name="uf" required="required">
					<span style="color: grey">Ex: RJ</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Endere�o</label>
				<div class="controls">
					<input type="text" name="endereco" required="required">
					<span style="color: grey">Ex: Rua Carlota Vasconcelos</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">N�mero do endere�o</label>
				<div class="controls">
					<input type="text" name="numeroEndereco" required="required">
					<span style="color: grey">Ex: 1029</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Complemento do endere�o</label>
				<div class="controls">
					<input type="text" name="complementoEndereco" required="required">
					<span style="color: grey">Ex: Apartamento 504</span>
				</div>
			</div>
			<button type="submit" class="btn btn-primary">Avan�ar</button>
		</fieldset>
	</form>
</div>