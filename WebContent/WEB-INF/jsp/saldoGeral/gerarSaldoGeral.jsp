<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Saldo geral</h4>
</div>
<br>
<div class="fundo-branco">
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th>Distribuidor</th>
				<th>Saldo liberado</th>
				<th>Saldo previsto no mês</th>
				<th>Saldo previsto total</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${saldos}" var="item">
				<tr>
					<td class="centralizado">${item.usuario.id_Codigo}-${item.usuario.vNome}</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.saldoLiberado}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.saldoPrevistoNoMes}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.saldoPrevistoTotal}" pattern="#,##0.00" />
					</td>
			</c:forEach>
		</tbody>
		<tfoot style="font-weight: bold">
			<tr>
				<td class="centralizado">Total</td>
				<td class="centralizado">
					R$
					<fmt:formatNumber value="${saldoLiberadoSomatorio}" pattern="#,##0.00" />
				</td>
				<td class="centralizado">
					R$
					<fmt:formatNumber value="${saldoPrevistoNoMesSomatorio}" pattern="#,##0.00" />
				</td>
				<td class="centralizado">
					R$
					<fmt:formatNumber value="${saldoPrevistoTotalSomatorio}" pattern="#,##0.00" />
				</td>
		</tfoot>
	</table>
</div>
