<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Escolha a forma de envio</h4>
</div>
<br>
<div class="fundo-branco">
	<c:if test="${not empty opcoesDeFrete}">
		<table class="table table-striped table-bordered" style="background-color: white">
			<thead>
				<tr>
					<th></th>
					<th>Forma de envio</th>
					<th>Valor</th>
					<th>Tempo para entrega aproximado (dias úteis)</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${opcoesDeFrete}" var="item">
					<tr>
						<td class="centralizado">
							<img style="width: 50px" src="${item.company.picture}" />
						</td>
						<td class="centralizado">${item.company.name}</td>
						<td class="centralizado">
							R$
							<fmt:formatNumber value="${item.price}" pattern="#,##0.00" />
						</td>
						<td class="centralizado">${item.deliveryTime}</td>
						<td style="text-align: center;">
							<a class="btn" href="<c:url value="/pedido/escolherFormaDeEnvioPorId/${item.id}"/>"> Escolher </a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
</div>
