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
		<h5>Como deseja receber os produtos?</h5>
		<select name='formaDeEntrega'>
			<option value="">Selecione</option>
			<option value="receberNoPA">Receber pessoalmente no Ponto de Apoio</option>
			<option value="receberEmCasa">Receber em casa</option>
		</select>
		<div style="border: 1px black solid; padding: 10px; max-width: 300px; border-radius: 10px">
			<h6>Obs: Para receber os seus produtos em casa, é necessário que o seu endereço esteja devidamente cadastrado e revisado nos seus dados cadastrais.</h6>
			<h6>Os seus dados para entrega são:</h6>
			<h6>* CEP: ${sessaoUsuario.usuario.cadCEP}</h6>
			<h6>* Endereço: ${sessaoUsuario.usuario.cadEndereco}</h6>
			<h6>* Bairro: ${sessaoUsuario.usuario.cadBairro}</h6>
			<h6>* Cidade: ${sessaoUsuario.usuario.cadCidade}</h6>
			<h6>* Estado: ${sessaoUsuario.usuario.cadUF}</h6>
			<h6></h6>
			<h6>Caso algum dado de entrega não esteja correto. Por favor, atualize o seu cadastro antes de continuar com o pedido.</h6>
		</div>
		<br>
		<c:if test="${sessaoUsuario.usuario.informacoesFixasUsuario.administrador || sessaoUsuario.usuario.donoDeFranquia}">
			<br>
			<h5 style="display: none">Escolha o código do distribuidor que irá realizar o pedido</h5>
			<input style="display: none" type="number" min="1" name="idCodigo" placeholder="idCodigo" value="${sessaoUsuario.usuario.id_Codigo}">
			<br>
		</c:if>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Selecionar</button>
	</form>
</div>
