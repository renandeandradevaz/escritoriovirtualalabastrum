<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Novo pedido</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pedido/escolherProdutos"/>" method="post">
		<h5>De qual Ponto de Apoio deseja comprar?</h5>
		<select name='idFranquia'>
			<option value="">Selecione</option>
			<c:forEach items="${franquias}" var="item">
				<option value="${item.id_Estoque}">${item.estqUF}-${item.estqCidade}-${item.estqBairro}</option>
			</c:forEach>
		</select>
		<br>
		<br>
		<h5>Como deseja receber os produtos?</h5>
		<select name='formaDeEntrega'>
			<option value="">Selecione</option>
			<option value="receberNoPA">Pessoalmente no Ponto de Apoio</option>
			<option value="receberEmCasa">Receber em casa</option>
		</select>
		<br>
		<br>
		<c:if test="${sessaoUsuario.usuario.informacoesFixasUsuario.administrador || sessaoUsuario.usuario.donoDeFranquia}">
			<br>
			<h5 style="display: none">Escolha o código do distribuidor que irá realizar o pedido</h5>
			<input style="display: none" type="number" min="1" name="idCodigo" placeholder="idCodigo" value="${sessaoUsuario.usuario.id_Codigo}">
			<br>
		</c:if>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Selecionar</button>
	</form>
</div>
