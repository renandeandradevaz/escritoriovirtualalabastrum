<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Pedidos para estoque</h4>
</div>
<br>
<div class="fundo-branco">
	<c:if test="${sessaoUsuario.usuario.donoDeFranquia}">
		<a class="btn btn-default" style="float: right;" href="<c:url value="/pedidoFranquia/novoPedido"/>"> Novo pedido </a>
	</c:if>
	<form action="<c:url value="/pedidoFranquia/${searchEndpoint}"/>" method="post">
		<h6>Status do pedido</h6>
		<select name='status' id='status'>
			<option value="PENDENTE">PENDENTE</option>
			<option value="ENTREGUE">ENTREGUE</option>
		</select>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Filtrar</button>
	</form>
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th>Código do pedido</th>
				<th>Franquia</th>
				<th>Data</th>
				<th>Valor</th>
				<th>Status</th>
				<th></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${pedidos}" var="item">
				<tr>
					<td class="centralizado">${item.pedidoFranquia.id}</td>
					<td class="centralizado">${item.franquia.estqUF}-${item.franquia.estqCidade}-${item.franquia.estqBairro}</td>
					<td class="centralizado">
						<fmt:formatDate value="${item.pedidoFranquia.data.time}" type="DATE" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.valorTotal}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">${item.pedidoFranquia.status}</td>
					<td style="text-align: center;">
						<a class="btn btn-default" href="<c:url value="/pedidoFranquia/verItens/${item.pedidoFranquia.id}"/>"> Detalhar </a>
					</td>
					<td style="text-align: center;">
						<c:if test="${sessaoUsuario.usuario.informacoesFixasUsuario.administrador && item.pedidoFranquia.status == 'PENDENTE'}">
							<a class="btn btn-default" href="<c:url value="/pedidoFranquia/marcarComoEntregue/${item.pedidoFranquia.id}"/>"> Marcar como entregue </a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<script>
	$('#status').val('${status}');
</script>