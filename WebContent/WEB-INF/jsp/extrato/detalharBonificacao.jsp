<%@ include file="/base.jsp"%>
<c:if test="${not empty extratoDetalhado}">
	<div class="fundo-branco">
		<table class="table table-striped table-bordered" style="font-size: 10px">
			<thead>
				<tr>
					<th>Data</th>
					<th>Histórico</th>
					<th>Distribuidor</th>
					<th>Valor</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${extratoDetalhado}" var="item">
					<tr>
						<td class="centralizado">
							<fmt:formatDate value="${item.data.time}" type="DATE" />
						</td>
						<td class="centralizado">${item.discriminador}</td>
						<td class="centralizado">${item.usuario.apelido}
							<br>
							${item.usuario.vNome}
						</td>
						<td class="centralizado">
							R$
							<fmt:formatNumber value="${item.valor}" pattern="#,##0.00" />
						</td>
				</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>
<br>
<a href="javascript:history.back()">Voltar</a>
<br>
