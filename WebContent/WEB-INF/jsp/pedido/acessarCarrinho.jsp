<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Meu carrinho</h4>
</div>
<br>
<div class="fundo-branco">
	<c:if test="${not empty itensPedidoDTO}">
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<th>Nome</th>
					<th>Quantidade</th>
					<th>Preço unitário</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${itensPedidoDTO}" var="item">
					<tr>
						<td class="centralizado">${item.produto.prdNome}</td>
						<td class="centralizado">${item.quantidade}</td>
						<td class="centralizado">
							R$
							<fmt:formatNumber value="${item.produto.prdPreco_Unit}" pattern="#,##0.00" />
						</td>
						<td style="text-align: center;">
							<a class="btn btn-danger" href="<c:url value="/pedido/removerProduto/${item.produto.id_Produtos}"/>"> Remover </a>
						</td>
				</c:forEach>
			</tbody>
		</table>
		<h2 style="float: right;">
			Total: R$
			<fmt:formatNumber value="${totais.valorTotal}" pattern="#,##0.00" />
		</h2>
		<br>
		<br>
		<br>
		<h4 style="float: right;">Pontos: ${totais.totalPontos}</h4>
		<br>
		<br>
		<br>
		<br>
		<a href="<c:url value="/pedido/concluirPedido"/>" class="btn btn-success" style="float: right;">Concluir</a>
	</c:if>
	<c:if test="${empty itensPedidoDTO}">
		<h2>Seu carrinho está vazio.</h2>
	</c:if>
	<a class="btn btn-info" href="<c:url value="/pedido/escolherProdutos"/>"> Escolher mais produtos</a>
</div>
