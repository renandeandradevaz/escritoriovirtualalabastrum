<%@ include file="/base.jsp"%>
<style>
.topo, .menu-hamburguer, .menu, #sair {
	display: none;
}
</style>
<div id='imprimir-pedido'>
	<div class="fundo-branco">
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<th>Distribuidor</th>
					<th>Id Pedido</th>
					<th>Valor total</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="centralizado">${usuarioPedido.vNome}</td>
					<td class="centralizado">${pedido.id}</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${valorTotal}" pattern="#,##0.00" />
					</td>
				</tr>
			</tbody>
		</table>
		<br>
		<br>
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
	</div>
</div>
<script>
	$(document).ready(function() {
		var restorepage = $('body').html();
		var printcontent = $('#imprimir-pedido').clone();
		$('body').empty().html(printcontent);
		window.print();
		$('body').html(restorepage);
	});
</script>
