<%@ include file="/base.jsp"%>
<style>
.topo, .menu-hamburguer, .menu, #sair {
	display: none;
}

.conteudo {
	width: 100%;
}
</style>
<div id='imprimir-pedido'>
	<div class="fundo-branco">
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<th>Distribuidor</th>
					<th>Id Pedido</th>
					<th>Tipo do pedido</th>
					<th>Data</th>
					<th>Status</th>
					<th>Forma de pagamento</th>
					<th>Forma de entrega</th>
					<th>Empresa para entrega</th>
					<th>Valor total</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="centralizado">${usuarioPedido.vNome}</td>
					<td class="centralizado">${pedido.id}</td>
					<td class="centralizado">${pedido.tipo}</td>
					<td class="centralizado">
						<fmt:formatDate value="${pedido.data.time}" type="DATE" />
					</td>
					<td class="centralizado">${pedido.status}</td>
					<td class="centralizado">${pedido.formaDePagamento}</td>
					<td class="centralizado">${pedido.formaDeEntrega}</td>
					<td class="centralizado">${pedido.empresaParaEntrega}</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${valorTotal}" pattern="#,##0.00" />
					</td>
				</tr>
			</tbody>
		</table>
		<br>
		<c:if test="${pedido.formaDeEntrega == 'receberEmCasa'}">
			<h4>Endereço para entrega</h4>
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<th>CEP</th>
						<th>Bairro</th>
						<th>Cidade</th>
						<th>UF</th>
						<th>Endereço</th>
						<th>Número</th>
						<th>Complemento</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="centralizado">${usuarioPedido.cadCEP}</td>
						<td class="centralizado">${usuarioPedido.cadBairro}</td>
						<td class="centralizado">${usuarioPedido.cadCidade}</td>
						<td class="centralizado">${usuarioPedido.cadUF}</td>
						<td class="centralizado">${usuarioPedido.cadEndereco}</td>
						<td class="centralizado">${usuarioPedido.numeroEndereco}</td>
						<td class="centralizado">${usuarioPedido.complementoEndereco}</td>
					</tr>
				</tbody>
			</table>
		</c:if>
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
