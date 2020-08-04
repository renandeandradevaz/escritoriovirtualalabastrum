<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<br>
	<h5>
		Valor final: R$
		<fmt:formatNumber value="${precoFinal}" pattern="#,##0.00" />
	</h5>
	<br>
	<a class="btn btn-success" href="<c:url value="/pedido/concluirPedido"/>">Avançar</a>
	<br>
	<br>
	<a class="btn btn-info" href="<c:url value="/pedido/escolherProdutos"/>"> Escolher mais produtos</a>
</div>
