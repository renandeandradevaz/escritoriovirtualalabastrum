<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Novo pedido</h4>
</div>
<br>
<div class="fundo-branco">
	<h5>Escolha a franquia para retirada</h5>
	<form action="<c:url value="/pedido/escolherProdutos"/>" method="post">
		<select name='idFranquia'>
			<option value="">Selecione</option>
			<c:forEach items="${franquias}" var="item">
				<option value="${item.id_Codigo}">${item.estqUF}-${item.estqCidade}- ${item.estqBairro}</option>
			</c:forEach>
		</select>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Selecionar</button>
	</form>
</div>
