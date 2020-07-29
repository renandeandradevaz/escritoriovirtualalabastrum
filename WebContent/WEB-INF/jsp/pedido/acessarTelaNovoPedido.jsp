<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Novo pedido</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pedido/escolherProdutos"/>" method="post">
		<h5>Escolha a franquia para retirada</h5>
		<select name='idFranquia'>
			<option value="">Selecione</option>
			<c:forEach items="${franquias}" var="item">
				<option value="${item.id_Estoque}">${item.estqUF}-${item.estqCidade}-${item.estqBairro}</option>
			</c:forEach>
		</select>
		<br>
		<c:if test="${sessaoUsuario.usuario.informacoesFixasUsuario.administrador || sessaoUsuario.usuario.donoDeFranquia}">
			<h5 style="display: none">Escolha o código do distribuidor que irá realizar o pedido</h5>
			<input style="display: none" type="number" min="1" name="idCodigo" placeholder="idCodigo" value="${sessaoUsuario.usuario.id_Codigo}">
		</c:if>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Selecionar</button>
	</form>
</div>
