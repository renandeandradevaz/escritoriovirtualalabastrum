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
			<button type="submit" class="btn btn-primary">Avançar</button>
		</fieldset>
	</form>
</div>