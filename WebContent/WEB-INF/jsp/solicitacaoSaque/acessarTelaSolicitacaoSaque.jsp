<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Solicitar saque</h4>
</div>
<br>
<div class="fundo-branco">
	<h4>Seu saldo atual disponível para saque: R$ ${saldo}</h4>
	<h4>Valor mínimo para saque: R$100,00</h4>
	<h4>Tarifa bancária para saque: R$10,00</h4>
	<h4>INSS a ser descontado: 11%</h4>
	<h4>IMPOSTO DE RENDA a ser descontado: 10%</h4>
	<br>
	<br>
	<form action="<c:url value="/solicitacaoSaque/solicitarSaque"/>" method="post">
		<input type="number" min="100" step="1" name="valorString" style="width: 90px; margin-top: 10px" placeholder="Valor">
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Solicitar</button>
	</form>
</div>
