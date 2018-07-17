<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Transferir para outro distribuidor</h4>
</div>
<br>
<div class="fundo-branco">
	<h3>
		Saldo liberado atual: R$
		<fmt:formatNumber value="${saldoLiberado}" pattern="#,##0.00" />
	</h3>
	<br>
	<form action="<c:url value="/transferencia/transferirParaOutroDistribuidor"/>" method="post">
		<div class="control-group">
			<label class="control-label">Valor</label>
			<div class="controls">
				<input type="number" name="valor" value="0" onkeypress='return event.charCode >= 48 && event.charCode <= 57'>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Código do outro distribuidor que irá receber</label>
			<div class="controls">
				<input type="number" name="codigoOutroDistribuidor" value="0">
			</div>
		</div>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Transferir</button>
	</form>
</div>
