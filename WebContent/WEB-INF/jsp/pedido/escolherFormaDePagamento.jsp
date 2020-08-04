<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Escolher forma de pagamento</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pedido/mostrarValorFinalPedido"/>" method="post">
		<select name='formaDePagamento'>
			<option value="">Selecione</option>
			<option value="pagarComDinheiro">Pagar em dinheiro diretamente no centro de distribuição</option>
			<option value="pagarComBoleto">Pagar com boleto</option>
			<c:if test="${pagamentoComSaldoHabilitado}">
				<option value="pagarComSaldo">Pagar com saldo</option>
			</c:if>
			<option value="pagarComCartaoDeDebito">Pagar com cartão de débito diretamente no centro de distribuição</option>
			<!-- 			<option value="pagarComCartaoDeCredito">Pagar online com cartão de crédito</option> -->
		</select>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Avançar</button>
	</form>
	<c:if test="${mostrarDialogoDescontos}">
		<div style="border: 1px solid black; border-radius: 10px; padding: 5px; max-width: 500px">
			<h5>* Pagamento com dinheiro vivo: Desconto de 50%</h5>
			<h5>* Pagamento com boleto: Desconto de 50%</h5>
			<h5>* Pagamento com saldo: Desconto de 50%</h5>
			<h5>* Pagamento com cartão de débito: Desconto de 48%</h5>
			<h5>* Pagamento com cartão de crédito: Desconto de 45%</h5>
		</div>
	</c:if>
</div>
