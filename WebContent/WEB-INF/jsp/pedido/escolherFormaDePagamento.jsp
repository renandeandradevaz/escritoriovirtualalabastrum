<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Escolher forma de pagamento</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pedido/mostrarValorFinalPedido"/>" method="post">
		<select name='formaDePagamento'>
			<option value="">Selecione</option>
			<!-- 			<option value="pagarNoCentroDeDistribuicao" selected="selected">Transferência bancária</option> -->
			<option value="pagarComDinheiro">Pagar em dinheiro diretamente no centro de distribuição</option>
			<option value="pagarComCartaoDeDebito">Pagar com cartão de débito diretamente no centro de distribuição</option>
			<option value="pagarComCartaoDeCredito">Pagar online com cartão de crédito</option>
			<option value="pagarComBoleto">Pagar com boleto</option>
			<c:if test="${pagamentoComSaldoHabilitado}">
				<option value="pagarComSaldo">Pagar com saldo</option>
			</c:if>
		</select>
		<!-- 		<br> -->
		<!-- 		<div style="margin: 5%; border: 1px solid black; padding: 10px"> -->
		<!-- 			<h5>A transferência bancária deve ser feita para a seguinte conta:</h5> -->
		<!-- 			<br> -->
		<!-- 			<h5>DUNASTES MULTINIVEL DO BRASIL COSMÉTICOS EIRELI</h5> -->
		<!-- 			<h5>CNPJ: 37.268.755/0001-81</h5> -->
		<!-- 			<h5>* Banco: SANTANDER</h5> -->
		<!-- 			<h5>* Agência: 2093</h5> -->
		<!-- 			<h5>* Conta Corrente: 13-001999-0</h5> -->
		<!-- 		</div> -->
		<!-- 		<br> -->
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Avançar</button>
	</form>
	<c:if test="${mostrarDialogoDescontos}">
		<div style="border: 1px solid black; border-radius: 10px; padding: 5px; max-width: 500px">
			<h5>* Pagamento com dinheiro: Desconto de 50%</h5>
			<h5>* Pagamento com cartão de débito ou boleto: Desconto de 48%</h5>
			<h5>* Pagamento com cartão de crédito: Desconto de 45%</h5>
		</div>
	</c:if>
</div>
