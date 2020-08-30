<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Solicitar saque</h4>
</div>
<br>
<div class="fundo-branco">
	<h4>Valor final com descontos:</h4>
	R$
	<fmt:formatNumber value="${valorFinalComDescontos}" pattern="#,##0.00" />
	<br>
	<br>
	<a class="btn btn-primary" href="<c:url value="/solicitacaoSaque/confirmar"/>"> Confirmar </a>
</div>
