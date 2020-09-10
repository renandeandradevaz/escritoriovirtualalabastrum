<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<br>
	<h5>
		Valor do pedido: R$
		<fmt:formatNumber value="${precoFinal}" pattern="#,##0.00" />
	</h5>
	<br>
	<c:if test="${not empty tarifas}">
		<h5>
			(+ Tarifas): R$
			<fmt:formatNumber value="${tarifas}" pattern="#,##0.00" />
		</h5>
		<br>
	</c:if>
	<a class="btn btn-success" href="<c:url value="/pedido/concluirPedido"/>">Avançar</a>
	<br>
	<br>
	<a class="btn btn-info" href="<c:url value="/pedido/escolherProdutos"/>"> Escolher mais produtos</a>
</div>
