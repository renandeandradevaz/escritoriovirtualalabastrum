<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<br>
	<h5>Valor final: R$ ${precoFinal}</h5>
	<br>
	<a class="btn btn-success" href="<c:url value="/pedido/concluirPedido"/>">Avan�ar</a>
	<br>
	<br>
	<a class="btn btn-info" href="<c:url value="/pedido/escolherProdutos"/>"> Escolher mais produtos</a>
</div>
