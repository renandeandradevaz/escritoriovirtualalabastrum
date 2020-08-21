<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Escolher forma de pagamento</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pedido/mostrarValorFinalPedido"/>" method="post">
		<select name='formaDePagamento'>
			<option value="">Selecione</option>
			<c:if test="${pagamentoComSaldoHabilitado}">
				<option value="pagarComSaldo">Pagar com saldo</option>
			</c:if>
			<c:if test="${formaDeEntrega == 'receberNoPA'}">
				<option value="pagarComDinheiro">Pagar em dinheiro diretamente no centro de distribuição</option>
				<option value="pagarComCartaoDeDebitoNoPA">Pagar com cartão de débito diretamente no centro de distribuição</option>
				<option value="pagarComCartaoDeCreditoNoPA">Pagar com cartão de crédito diretamente no centro de distribuição</option>
			</c:if>
			<c:if test="${formaDeEntrega == 'receberEmCasa'}">
				<option value="pagarComBoleto">Pagar com boleto</option>
				<!-- 			<option value="pagarComCartaoDeDebitoOnline">Pagar com cartão de débito online</option> -->
				<!-- 			<option value="pagarComCartaoDeCreditoOnline">Pagar com cartão de crédito online</option> -->
			</c:if>
		</select>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Avançar</button>
	</form>
	<c:if test="${mostrarDialogoDescontos}">
		<div style="border: 1px solid black; border-radius: 10px; padding: 5px; max-width: 500px">
			<h5>* Pagamento com saldo de bonificações: Desconto de 50%</h5>
			<h5>* Pagamento com dinheiro vivo: Desconto de 50%</h5>
			<h5>* Pagamento com boleto: Desconto de 47%</h5>
			<h5>* Pagamento com cartão de débito: Desconto de 47%</h5>
			<h5>* Pagamento com cartão de crédito: Desconto de 44%</h5>
		</div>
	</c:if>
</div>
