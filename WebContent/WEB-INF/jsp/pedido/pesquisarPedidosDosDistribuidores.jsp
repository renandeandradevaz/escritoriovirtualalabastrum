<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Pedidos</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pedido/pesquisarPedidosDosDistribuidores"/>" method="post">
		<h6>Status do pedido</h6>
		<select name='status' id='status'>
			<option value="PENDENTE">PENDENTE</option>
			<option value="PAGO">PAGO</option>
			<option value="FINALIZADO">FINALIZADO</option>
			<option value="CANCELADO">CANCELADO</option>
		</select>
		<br>
		<h6>C�digo do distribuidor</h6>
		<input type="number" min="1" name="idCodigo" placeholder="idCodigo" value="${idCodigo}">
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Filtrar</button>
	</form>
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th>C�digo do pedido</th>
				<th>Tipo do pedido</th>
				<th>Distribuidor</th>
				<th>Franquia</th>
				<th>Data</th>
				<th>Valor</th>
				<th>Status</th>
				<th>Forma de pagamento</th>
				<th>Forma de entrega</th>
				<th>Empresa para entrega</th>
				<th>A��es</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${pedidosDTO}" var="item">
				<tr>
					<td class="centralizado">${item.pedido.id}</td>
					<td class="centralizado">${item.pedido.tipo}</td>
					<td class="centralizado">${item.distribuidor.apelido}</td>
					<td class="centralizado">${item.franquia.estqUF}-${item.franquia.estqCidade}-${item.franquia.estqBairro}</td>
					<td class="centralizado">
						<fmt:formatDate value="${item.pedido.data.time}" type="DATE" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.valorTotal}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">${item.pedido.status}</td>
					<td class="centralizado">${item.pedido.formaDePagamento}</td>
					<td class="centralizado">${item.pedido.formaDeEntrega}</td>
					<td class="centralizado">${item.pedido.empresaParaEntrega}</td>
					<td style="text-align: center;">
						<a class="btn btn-default" href="<c:url value="/pedido/verItens/${item.pedido.id}"/>"> Detalhar </a>
						<br>
						<br>
						<c:if test="${item.pedido.status == 'PENDENTE' || item.pedido.status == 'PAGO'}">
							<a class="btn btn-success" href="<c:url value="/pedido/alterarStatus/${item.pedido.id}/FINALIZADO"/>"> Marcar como pago e finalizar pedido </a>
							<br>
							<br>
							<a class="btn btn-danger" href="<c:url value="/pedido/alterarStatus/${item.pedido.id}/CANCELADO"/>"> Cancelar </a>
							<br>
							<br>
						</c:if>
						<a class="btn btn-info" href="<c:url value="/pedido/imprimirPedido/${item.pedido.id}"/>"> Imprimir </a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<script>
	$('#status').val('${status}');
</script>