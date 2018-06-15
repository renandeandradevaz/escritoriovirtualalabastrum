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
	<div class="fundo-preto letra-branca">
		<h4>
			Valor: R$
			<fmt:formatNumber value="${totais.valorTotal}" pattern="#,##0.00" />
			- Total de itens: ${totais.totalItens} - Pontos: ${totais.totalPontos}
		</h4>
		<br>
		<a href="<c:url value="/pedido/acessarCarrinho"/>">
			<span style="border: 1px solid; border-color: white; padding: 5px; color: white; border-radius: 3px;"> Ir para carrinho</span>
		</a>
	</div>
</c:if>
<br>
<c:if test="${not empty produtos}">
	<div class="fundo-branco">
		<c:forEach items="${produtos}" var="item">
			<div>
				<p>${item.prdNome}</p>
				<p>
					Preço: R$
					<fmt:formatNumber value="${item.prdPreco_Unit}" pattern="#,##0.00" />
				</p>
				<p>Pontos:
				<p>${item.prdPontos}</p>
				<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/Flag_of_Syrian_Democratic_Forces.svg/300px-Flag_of_Syrian_Democratic_Forces.svg.png">
			</div>
		</c:forEach>
	</div>
</c:if>
