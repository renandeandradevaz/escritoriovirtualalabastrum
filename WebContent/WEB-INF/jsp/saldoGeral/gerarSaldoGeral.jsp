<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Saldo geral de ${mes + 1}/${ano}</h4>
</div>
<br>
<div class="fundo-branco">
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th>Distribuidor</th>
				<th>Saldo previsto atual</th>
				<th>Saldo atual</th>
				<th>Saldo previsto total atual</th>
				<th>Saldo liberado atual</th>
				<th>Ganhos até hoje</th>
				<th>Bonificações no mês ${mes + 1}/${ano}</th>
				<th>INSS no mês ${mes + 1}/${ano}</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${saldos}" var="item">
				<tr>
					<td class="centralizado">${item.usuario.apelido}
						<br>
						${item.usuario.vNome}
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.saldoPrevistoNoMes}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.saldoDoMesAtual}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.saldoPrevistoTotal}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.saldoComDescontos}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.ganhosAteHoje}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.bonificacoesNoMes}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.inssNoMes}" pattern="#,##0.00" />
					</td>
			</c:forEach>
		</tbody>
		<!-- 		<tfoot style="font-weight: bold"> -->
		<!-- 			<tr> -->
		<!-- 				<td class="centralizado">Total</td> -->
		<!-- 				<td class="centralizado"> -->
		<!-- 					R$ -->
		<%-- 					<fmt:formatNumber value="${saldoLiberadoSomatorio}" pattern="#,##0.00" /> --%>
		<!-- 				</td> -->
		<!-- 				<td class="centralizado"> -->
		<!-- 					R$ -->
		<%-- 					<fmt:formatNumber value="${saldoPrevistoNoMesSomatorio}" pattern="#,##0.00" /> --%>
		<!-- 				</td> -->
		<!-- 				<td class="centralizado"> -->
		<!-- 					R$ -->
		<%-- 					<fmt:formatNumber value="${saldoPrevistoTotalSomatorio}" pattern="#,##0.00" /> --%>
		<!-- 				</td> -->
		<!-- 		</tfoot> -->
	</table>
</div>
