<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Itens do pedido</h4>
</div>
<c:if test="${not empty comprador}">
	<br>
	<div class="fundo-branco">
		<h4>Dados do comprador</h4>
		<p>Nome: ${comprador.nome}</p>
		<p>Email: ${comprador.email}</p>
		<p>Telefone: ${comprador.telefone}</p>
	</div>
</c:if>
<br>
<div class="fundo-branco">
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th>Nome</th>
				<th>Quantidade</th>
				<th>Preço unitário</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${itensPedidoDTO}" var="item">
				<tr>
					<td class="centralizado">${item.produto.prdNome}</td>
					<td class="centralizado">${item.quantidade}</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.precoUnitario}" pattern="#,##0.00" />
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br>
	<a href="javascript:history.back()">Voltar</a>
	<br>
</div>