<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Produtos</h4>
</div>
<br>
<div class="fundo-branco">
	<h5>Forma de entrega:</h5>
	<p>Retirar em ${franquia.estqUF} - ${franquia.estqCidade} - ${franquia.estqBairro} - ${franquia.estqEndereco} - ${franquia.estqTelefone}</p>
	<a class="btn btn-default" href="<c:url value="/pedido/acessarTelaNovoPedido"/>"> Trocar </a>
</div>
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
			- Total de itens: ${totais.totalItens} - Pontos: ${totais.totalPontos}
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
					Preço: R$
					<fmt:formatNumber value="${item.precoUnitario}" pattern="#,##0.00" />
				</p>
				<p>Pontos: ${item.produto.prdPontos}</p>
				<form action="<c:url value="/pedido/adicionarProduto/${item.produto.id_Produtos}"/>" method="post">
					<input type="number" min="0" max="${item.quantidadeEmEstoque}" name="quantidade" style="width: 90px; margin-top: 10px" placeholder="Quantidade" value="${item.quantidade}">
					<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Adicionar</button>
				</form>
			</div>
		</c:forEach>
	</div>
</c:if>
<script>
	$(document).scrollTop($("#totais").offset().top);
</script>
