<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Lançamento de Débito e Crédito</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/creditoEDebito/salvarCreditoOuDebito"/>" method="post">
		<div class="control-group">
			<label class="control-label">Tipo</label>
			<div class="controls">
				<select name="tipo">
					<option value="Transferência por crédito">Crédito (Adicionar ao saldo)</option>
					<option value="Transferência por débito">Débito (Remover do saldo)</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Nickname</label>
			<div class="controls">
				<input type="text" name="apelido" required="required">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Descrição</label>
			<div class="controls">
				<input type="text" name="descricao" required="required">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Valor</label>
			<div class="controls">
				<input type="number" name="valor" value="0" onkeypress='return event.charCode >= 48 && event.charCode <= 57'>
			</div>
		</div>
		<br>
		<button type="submit" class="btn btn-primary">Ok</button>
	</form>
</div>
