<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Escolher forma de pagamento</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pedido/concluirPedido"/>" method="post">
		<select name='formaDePagamento'>
			<option value="">Selecione</option>
			<option value="pagarNoCentroDeDistribuicao" selected="selected">Transfer�ncia banc�ria</option>
			<!-- 			<option value="pagarNoCentroDeDistribuicao">Pagar diretamente no centro de distribui��o (Pagamento presencial com dinheiro vivo ou cart�o)</option> -->
			<%-- 			<c:if test="${pagamentoComSaldoHabilitado}"> --%>
			<!-- 				<option value="pagarComSaldo">Pagar com saldo</option> -->
			<%-- 			</c:if> --%>
			<!-- 			<option value="pagarComCartaoDeCredito">Pagar online com cart�o de cr�dito</option> -->
		</select>
		<br>
		<div style="margin: 5%; border: 1px solid black; padding: 10px">
			<h5>A transfer�ncia banc�ria deve ser feita para a seguinte conta:</h5>
			<br>
			<h5>DUNASTES MULTINIVEL DO BRASIL COSM�TICOS EIRELI</h5>
			<h5>CNPJ: 37.268.755/0001-81</h5>
			<h5>* Banco: SANTANDER</h5>
			<h5>* Ag�ncia: 2093</h5>
			<h5>* Conta Corrente: 13-001999-0</h5>
		</div>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Avan�ar</button>
	</form>
</div>
