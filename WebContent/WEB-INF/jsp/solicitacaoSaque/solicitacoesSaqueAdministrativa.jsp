<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Solicitações de Saque</h4>
</div>
<c:if test="${not empty comprador}">
	<br>
	<div class="fundo-branco">
		<h4>Dados do comprador</h4>
		<p>Nome: ${comprador.nome}</p>
		<p>Email: ${comprador.email}</p>
		<p>Telefone: ${comprador.telefone}</p>
	</div>
</c:if>
<br>
<div class="fundo-branco">
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th>Nickname</th>
				<th>Nome</th>
				<th>Valor bruto solicitado</th>
				<th>Valor final com descontos</th>
				<th>CPF</th>
				<th>Banco</th>
				<th>Agência</th>
				<th>Nº da Conta</th>
				<th></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${solicitacoes}" var="item">
				<tr>
					<td class="centralizado">${item.usuario.apelido}</td>
					<td class="centralizado">${item.usuario.vNome}</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.valorBrutoSolicitado}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${item.valorFinalComDescontos}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">${item.usuario.CPF}</td>
					<td class="centralizado">${item.usuario.cadBanco}</td>
					<td class="centralizado">${item.usuario.cadAgencia}</td>
					<td class="centralizado">${item.usuario.cadCCorrente}</td>
					<td style="text-align: center;">
						<a class="btn btn-danger" href="<c:url value="/solicitacaoSaque/cancelarSolicitacao/${item.id}"/>"> Cancelar </a>
					</td>
					<td style="text-align: center;">
						<a class="btn btn-success" href="<c:url value="/solicitacaoSaque/confirmarSolicitacao/${item.id}"/>"> Confirmar </a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br>
	<a href="javascript:history.back()">Voltar</a>
	<br>
</div>