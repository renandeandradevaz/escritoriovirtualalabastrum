<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Transferências para Alabastrum Card</h4>
</div>
<br>
<div class="fundo-branco">
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th>Distribuidor</th>
				<th>Data</th>
				<th>Valor</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${transferencias}" var="item">
				<tr>
					<td class="centralizado">${item.de}</td>
					<td class="centralizado">
						<fmt:formatDate value="${item.data.time}" type="DATE" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.valor}" pattern="#,##0.00" />
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
