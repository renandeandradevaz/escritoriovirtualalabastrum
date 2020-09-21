<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Meu carrinho</h4>
</div>
<br>
<div class="fundo-branco">
	<c:if test="${not empty itensPedidoDTO}">
		<table class="table table-striped table-bordered" style="background-color: white">
			<thead>
				<tr>
					<th>Nome</th>
					<th>Quantidade</th>
					<th>Preço unitário</th>
					<th></th>
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
						<td style="text-align: center;">
							<a class="btn btn-danger" href="<c:url value="/pedido/removerProduto/${item.produto.id_Produtos}"/>"> Remover </a>
						</td>
				</c:forEach>
			</tbody>
		</table>
		<h2 style="float: right;">
			Total: R$
			<fmt:formatNumber value="${totais.valorTotal}" pattern="#,##0.00" />
		</h2>
		<br>
		<br>
		<br>
		<c:if test="${mostrarPontuacao}">
			<h4 style="float: right;">Pontos pagáveis: ${totais.totalPontosPagaveis}</h4>
			<h4 style="float: right;">Pontos Qualificáveis: ${totais.totalPontosQualificacao}</h4>
		</c:if>
		<br>
		<br>
		<br>
		<br>
		<c:if test="${sessaoUsuario.usuario.id_Codigo != null}">
			<c:choose>
				<c:when test="${formaDeEntrega == 'receberEmCasa'}">
					<a href="<c:url value="/pedido/escolherFormaDeEnvio"/>" class="btn btn-success">Escolher forma de envio</a>
				</c:when>
				<c:otherwise>
					<a href="<c:url value="/pedido/escolherFormaDePagamento"/>" class="btn btn-success">Escolher forma de pagamento</a>
				</c:otherwise>
			</c:choose>
			<c:if test="${sessaoUsuario.usuario.id_Codigo == null}">
				<a href="<c:url value="/pedido/informarDadosComprador"/>" class="btn btn-success">Avançar</a>
			</c:if>
		</c:if>
	</c:if>
	<c:if test="${empty itensPedidoDTO}">
		<h4>Seu carrinho está vazio.</h4>
	</c:if>
	<br>
	<br>
	<a class="btn btn-info" href="<c:url value="/pedido/escolherProdutos"/>"> Escolher mais produtos</a>
	<br>
	<br>
	<br>
	<a class="btn btn-danger" href="<c:url value="/pedido/cancelarPedidoAtual"/>"> Cancelar pedido atual</a>
</div>
