<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Lan�amento de D�bito e Cr�dito</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/creditoEDebito/salvarCreditoOuDebito"/>" method="post">
		<div class="control-group">
			<label class="control-label">Tipo</label>
			<div class="controls">
				<select name="tipo">
					<option value="Transfer�ncia por cr�dito">Cr�dito (Adicionar ao saldo)</option>
					<option value="Transfer�ncia por d�bito">D�bito (Remover do saldo)</option>
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
			<label class="control-label">Descri��o</label>
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
