<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Transferir para Alabastrum Card</h4>
</div>
<br>
<div class="fundo-branco">
	<h3>
		Saldo atual: R$
		<fmt:formatNumber value="${saldo}" pattern="#,##0.00" />
	</h3>
	<br>
	<form action="<c:url value="/transferencia/transferirParaAlabastrumCard"/>" method="post">
		<div class="control-group">
			<label class="control-label">Valor</label>
			<div class="controls">
				<input type="number" name="valor" value="0" onkeypress='return event.charCode >= 48 && event.charCode <= 57'>
			</div>
		</div>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Transferir</button>
	</form>
</div>
