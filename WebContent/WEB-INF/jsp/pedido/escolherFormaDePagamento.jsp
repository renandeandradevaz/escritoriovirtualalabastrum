<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Escolher forma de pagamento</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pedido/concluirPedido"/>" method="post">
		<select name='formaDePagamento'>
			<option value="">Selecione</option>
			<option value="pagarNoCentroDeDistribuicao">Pagar diretamente no centro de distribuição (Pagamento presencial com dinheiro vivo ou cartão)</option>
			<option value="pagarComSaldo">Pagar com saldo</option>
		</select>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Concluir</button>
	</form>
</div>
