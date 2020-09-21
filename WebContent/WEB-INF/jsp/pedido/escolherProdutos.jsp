<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Produtos</h4>
</div>
<c:if test="${sessaoUsuario.usuario.id_Codigo != null}">
	<br>
	<div class="fundo-branco">
		<p>Ponto de Apoio escolhido: ${franquia.estqUF} - ${franquia.estqCidade} - ${franquia.estqBairro} - ${franquia.estqEndereco} - ${franquia.estqTelefone}</p>
		<a class="btn btn-default" href="<c:url value="/pedido/acessarTelaNovoPedido"/>"> Trocar </a>
	</div>
</c:if>
<br>
<div class="fundo-azul letra-branca">
	<b>Selecione a categoria:</b>
	<br>
	<c:forEach items="${categorias}" var="item">
		<a href="<c:url value="/pedido/selecionarCategoria/${item.id_Categoria}"/>">
			<span class='categorias-menu'> ${item.catNome} </span>
		</a>
	</c:forEach>
</div>
<br>
<c:if test="${not empty totais}">
	<div class="fundo-preto letra-branca" id='totais' style="height: 100px">
		<h4>
			Valor: R$
			<fmt:formatNumber value="${totais.valorTotal}" pattern="#,##0.00" />
			- Total de itens: ${totais.totalItens}
			<c:if test="${mostrarPontuacao}"> - Pontos Pag�veis: ${totais.totalPontosPagaveis} - Pontos Qualific�veis: ${totais.totalPontosQualificacao}  </c:if>
		</h4>
		<br>
		<a href="<c:url value="/pedido/acessarCarrinho"/>" style="float: right;">
			<span style="border: 1px solid; border-color: white; padding: 10px; color: white; border-radius: 3px; font-size: 15px; background-color: #5f5d5d;"> Ir para carrinho</span>
		</a>
	</div>
</c:if>
<br>
<c:if test="${not empty itensPedidoDTO}">
	<div class="fundo-branco">
		<c:forEach items="${itensPedidoDTO}" var="item">
			<div class="produto">
				<img src="<c:url value="/download/imagem/produto/${item.produto.id_Produtos}.jpg"/>">
				<p style="height: 50px; font-weight: bold">${item.produto.prdNome}</p>
				<p>
					Pre�o: R$
					<fmt:formatNumber value="${item.precoUnitario}" pattern="#,##0.00" />
				</p>
				<c:if test="${mostrarPontuacao}">
					<p>Pontos Pag�veis: ${item.produto.pntProduto}</p>
					<p>Pontos Qualific�veis: ${item.produto.pntQualificacao}</p>
				</c:if>
				<form action="<c:url value="/pedido/adicionarProduto/${item.produto.id_Produtos}"/>" method="post">
					<input type="number" min="0" max="${item.quantidadeEmEstoque}" name="quantidade" style="width: 90px; margin-top: 10px" placeholder="Quantidade" value="${item.quantidade}">
					<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Adicionar</button>
				</form>
				<c:if test="${sessaoUsuario.usuario.informacoesFixasUsuario.administrador}">
					<br>
					<br>
					<p>---------------------</p>
					<br>
					<h4>REMOVER PRODUTO DO ESTOQUE</h4>
					<br>
					<h6>(Escolha a quantidade abaixo)</h6>
					<form action="<c:url value="/pedido/removerDoEstoque/${item.produto.id_Produtos}"/>" method="post">
						<input type="number" min="0" max="${item.quantidadeEmEstoque}" name="quantidade" style="width: 90px; margin-top: 10px" placeholder="Quantidade" value="${item.quantidade}">
						<button type="submit" class="btn btn-danger" onclick="this.disabled=true;this.form.submit();">REMOVER DO ESTOQUE</button>
					</form>
					<br>
					<br>
					<br>
				</c:if>
			</div>
		</c:forEach>
	</div>
</c:if>
<script>
	$(document).scrollTop($("#totais").offset().top);
</script>
