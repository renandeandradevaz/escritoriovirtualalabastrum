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
						<fmt:formatNumber value="${item.saldoLiberado}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.ganhosAteHoje}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.ganhosNoMesPesquisado}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.inssNoMesPesquisado}" pattern="#,##0.00" />
					</td>
			</c:forEach>
		</tbody>
	</table>
</div>
