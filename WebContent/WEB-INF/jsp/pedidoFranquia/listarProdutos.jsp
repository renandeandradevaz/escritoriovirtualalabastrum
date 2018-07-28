<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Produtos</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pedidoFranquia/concluirPedido"/>" method="post">
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Concluir</button>
		<br>
		<br>
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<th>Categoria</th>
					<th>Produto</th>
					<th>Valor unitário</th>
					<th>Quantidade em estoque atual</th>
					<th>Quantidade para compra</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${itens}" var="item">
					<tr>
						<td class="centralizado">${item.categoria.catNome}</td>
						<td class="centralizado">${item.produto.id_Produtos}-${item.produto.prdNome}</td>
						<td class="centralizado">
							R$
							<fmt:formatNumber value="${item.precoUnitario}" pattern="#,##0.00" />
						</td>
						<td class="centralizado">${item.quantidadeEmEstoque}</td>
						<td class="centralizado">
							<input type="number" min="0" name="quantidades[${item.produto.id_Produtos}]" value="0">
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Concluir</button>
	</form>
</div>
