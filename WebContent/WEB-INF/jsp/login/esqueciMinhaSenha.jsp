<%@ include file="/base.jsp"%>
<form class="form-horizontal" action="<c:url value="/login/verificarExistenciaCodigoEsqueciMinhaSenha"/>" method="post">
	<fieldset>
		<legend>Esqueci minha senha</legend>
		<div class="control-group warning">
			<label class="control-label">Informe seu código</label>
			<div class="controls">
				<input type="text" class="numero-inteiro" name="codigoUsuario">
			</div>
		</div>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Avançar</button>
		<a class="btn btn-danger" href="<c:url value="/"/>"> Cancelar </a>
	</fieldset>
</form>