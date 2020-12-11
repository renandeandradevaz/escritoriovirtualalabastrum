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
					<th>Pre�o unit�rio</th>
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
			<h4 style="float: right;">Pontos pag�veis: ${totais.totalPontosPagaveis}</h4>
			<h4 style="float: right;">Pontos Qualific�veis: ${totais.totalPontosQualificacao}</h4>
		</c:if>
		<br>
		<br>
		<br>
		<br>
		<c:if test="${not empty kitAdesao}">
			<div style="border: 1px black solid; padding: 10px; max-width: 300px; border-radius: 10px">
				<h6>De acordo com a quantidade e valores de produtos que voc� est� adquirindo, o seu Kit de ades�o atual �:</h6>
				<h3>${kitAdesao.kit}</h3>
				<h6>Se quiser um kit de ades�o melhor, � s� aumentar o valor total do pedido adquirindo mais produtos</h6>
			</div>
			<br>
			<br>
			<br>
		</c:if>
		<c:if test="${sessaoUsuario.usuario.id_Codigo != null}">
			<c:choose>
				<c:when test="${formaDeEntrega == 'receberEmCasa'}">
					<a href="<c:url value="/pedido/escolherFormaDeEnvio"/>" class="btn btn-success">Escolher forma de envio</a>
				</c:when>
				<c:otherwise>
					<a href="<c:url value="/pedido/escolherFormaDePagamento"/>" class="btn btn-success">Escolher forma de pagamento</a>
				</c:otherwise>
			</c:choose>
		</c:if>
		<c:if test="${sessaoUsuario.usuario.id_Codigo == null}">
			<a href="<c:url value="/pedido/informarDadosComprador"/>" class="btn btn-success">Avan�ar</a>
		</c:if>
	</c:if>
	<c:if test="${empty itensPedidoDTO}">
		<h4>Seu carrinho est� vazio.</h4>
	</c:if>
	<br>
	<br>
	<a class="btn btn-info" href="<c:url value="/pedido/escolherProdutos"/>"> Escolher mais produtos</a>
	<br>
	<br>
	<br>
	<c:if test="${sessaoUsuario.usuario.id_Codigo != null}">
		<a class="btn btn-danger" href="<c:url value="/pedido/cancelarPedidoAtual"/>"> Cancelar pedido atual</a>
	</c:if>
</div>
