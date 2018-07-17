<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Realizar pagamento</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pedido/pagarEFinalizar"/>" method="post">
		<input type="hidden" name='idPedido' value='${idPedido}'>
		<h4>
			Saldo liberado atual: R$
			<fmt:formatNumber value="${saldoLiberado}" pattern="#,##0.00" />
		</h4>
		<div class="control-group">
			<label class="control-label">Valor a ser descontado do saldo do distribuidor:</label>
			<div class="controls">
				<input type="number" name="valor" value="0" onkeypress='return event.charCode >= 48 && event.charCode <= 57'>
			</div>
		</div>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Ok</button>
	</form>
</div>
