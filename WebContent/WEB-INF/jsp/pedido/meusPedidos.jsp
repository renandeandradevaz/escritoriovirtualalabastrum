<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Meus pedidos</h4>
</div>
<br>
<div class="fundo-branco">
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th>Info</th>
				<th>Franquia</th>
				<th>Valor</th>
				<th></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${pedidosDTO}" var="item">
				<tr>
					<td class="centralizado">${item.pedido.id}
						<br>
						${item.pedido.status}
						<br>
						<fmt:formatDate value="${item.pedido.data.time}" type="DATE" />
					</td>
					<td class="centralizado">${item.franquia.estqUF}-${item.franquia.estqCidade}-${item.franquia.estqBairro}-${item.franquia.estqEndereco}-${item.franquia.estqTelefone}</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.valorTotal}" pattern="#,##0.00" />
					</td>
					<td style="text-align: center;">
						<a class="btn btn-info" href="<c:url value="/pedido/verItens/${item.pedido.id}"/>"> Detalhar </a>
					</td>
					<td style="text-align: center;">
						<a class="btn btn-info" href="<c:url value="/pedido/imprimirPedido/${item.pedido.id}"/>"> Imprimir </a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>