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
				<option value="pagarComDinheiro">Pagar em dinheiro diretamente no centro de distribui��o</option>
				<option value="pagarComCartaoDeDebitoNoPA">Pagar com cart�o de d�bito diretamente no centro de distribui��o</option>
				<option value="pagarComCartaoDeCreditoNoPA">Pagar com cart�o de cr�dito diretamente no centro de distribui��o</option>
			</c:if>
			<c:if test="${formaDeEntrega == 'receberEmCasa'}">
				<option value="pagarComBoleto">Pagar com boleto</option>
				<!-- 			<option value="pagarComCartaoDeDebitoOnline">Pagar com cart�o de d�bito online</option> -->
				<!-- 			<option value="pagarComCartaoDeCreditoOnline">Pagar com cart�o de cr�dito online</option> -->
			</c:if>
		</select>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Avan�ar</button>
	</form>
	<c:if test="${mostrarDialogoDescontos}">
		<div style="border: 1px solid black; border-radius: 10px; padding: 5px; max-width: 500px">
			<h5>* Pagamento com saldo de bonifica��es: Desconto de 50%</h5>
			<h5>* Pagamento com dinheiro vivo: Desconto de 50%</h5>
			<h5>* Pagamento com boleto: Desconto de 47%</h5>
			<h5>* Pagamento com cart�o de d�bito: Desconto de 47%</h5>
			<h5>* Pagamento com cart�o de cr�dito: Desconto de 44%</h5>
		</div>
	</c:if>
</div>
