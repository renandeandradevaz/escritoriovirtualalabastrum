<%@ include file="/base.jsp"%>
<form class="form-horizontal" action="<c:url value="/login/verificarExistenciaCodigoEsqueciMinhaSenha"/>" method="post">
	<fieldset>
		<legend>Esqueci minha senha</legend>
		<div class="control-group warning">
			<label class="control-label">Informe seu nickname</label>
			<div class="controls">
				<input type="text" name="apelido">
			</div>
		</div>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Avan�ar</button>
		<a class="btn btn-danger" href="<c:url value="/"/>"> Cancelar </a>
	</fieldset>
</form>