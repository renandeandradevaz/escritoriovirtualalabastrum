<%@ include file="/base.jsp"%>
<form class="form-horizontal" action="<c:url value="/login/salvarTrocarSenhaPrimeiroAcesso"/>" method="post">
	<fieldset>
		<p class="tituloTrocarSenha">TROQUE SUA SENHA</p>
		<div class="control-group warning">
			<label class="control-label">Nova senha</label>
			<div class="controls">
				<input type="password" class="input-xlarge" name="senhaNova">
			</div>
		</div>
		<div class="control-group warning">
			<label class="control-label">Confirmação</label>
			<div class="controls">
				<input type="password" class="input-xlarge" name="confirmacaoSenhaNova">
			</div>
		</div>
		<div class="control-group warning">
			<label class="control-label">CPF</label>
			<div class="controls">
				<input type="text" class="input-xlarge numero-inteiro" name="cpf">
			</div>
		</div>
		<div class="control-group warning">
			<label class="control-label">E-mail</label>
			<div class="controls">
				<input type="email" class="input-xlarge" name="email">
			</div>
		</div>
		<div style="margin-left: 20px;">
			<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Salvar</button>
			<a class="btn btn-danger" href="<c:url value="/"/>"> Cancelar </a>
		</div>
	</fieldset>
</form>