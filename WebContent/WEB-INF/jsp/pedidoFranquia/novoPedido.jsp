<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Novo pedido</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pedidoFranquia/listarProdutos"/>" method="post">
		<h5>Escolha a franquia:</h5>
		<select name='idFranquia'>
			<c:forEach items="${franquias}" var="item">
				<option value="${item.id_Estoque}">${item.estqUF}-${item.estqCidade}-${item.estqBairro}</option>
			</c:forEach>
		</select>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Selecionar</button>
	</form>
</div>
