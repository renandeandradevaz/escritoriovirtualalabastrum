<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Transferências</h4>
</div>
<br>
<div class="fundo-branco">
	<br>
	<a href="<c:url value="/creditoEDebito/acessarTelaCreditoEDebito"/>" class="btn"> Realizar movimentação </a>
	<br>
	<br>
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th>Data</th>
				<th>Nickname</th>
				<th>Tipo</th>
				<th>Descrição</th>
				<th>Valor</th>
				<th>Responsável</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${transferencias}" var="item">
				<tr>
					<td class="centralizado">
						<fmt:formatDate value="${item.data.time}" type="DATE" />
					</td>
					<td class="centralizado">${item.distribuidor.apelido}</td>
					<td class="centralizado">${item.tipo}</td>
					<td class="centralizado">${item.descricao}</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.valor}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">${item.usuarioResponsavelPelaTransferencia.apelido}</td>
			</c:forEach>
		</tbody>
	</table>
</div>
