<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Novo pedido</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pedido/escolherProdutos?escolherCategoriaPadrao=1"/>" method="post">
		<c:choose>
			<c:when test="${sessaoUsuario.usuario.donoDeFranquia}">
				<br>
				<h5>Informe o nickname do distribuidor que irá realizar o pedido</h5>
				<input type="text" name="nickname" placeholder="nickname" value="${sessaoUsuario.usuario.apelido}">
			</c:when>
			<c:otherwise>
				<h5>Como deseja receber os produtos?</h5>
				<select name='formaDeEntrega' id='formaDeEntrega'>
					<option value="">Selecione</option>
					<option value="receberEmCasa">Receber em casa</option>
					<option value="receberNoPA">Retirar pessoalmente no Ponto de Apoio</option>
				</select>
				<h5>De qual Ponto de Apoio deseja comprar?</h5>
				<select name='idFranquia' id='franquia'>
					<option value="">Selecione</option>
					<c:forEach items="${franquias}" var="item">
						<option value="${item.id_Estoque}">${item.estqUF}-${item.estqCidade}-${item.estqBairro}</option>
					</c:forEach>
				</select>
				<br>
				<c:if test="${not empty kitsAdesao}">
					<h5>Kits e seus valores mínimos:</h5>
					<select name='idKit'>
						<option value="">Selecione</option>
						<c:forEach items="${kitsAdesao}" var="item">
							<option value="${item.id}">${item.kit}-Mínimo:R$
								<fmt:formatNumber value="${item.valor}" pattern="#,##0.00">
								</fmt:formatNumber>
							</option>
						</c:forEach>
					</select>
					<br>
					<br>
				</c:if>
				<input type="checkbox" name='adesaoPontoDeApoio' />
				<span>Desejo fazer uma adesão de Ponto de Apoio (Obs: Para adquirir um P.A. é necessário fazer uma compra de produtos no valor mínimo de R$2.500,00) </span>
				<br>
				<br>
				<br>
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
			</c:otherwise>
		</c:choose>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Avançar</button>
	</form>
</div>
<script>
	$("#formaDeEntrega").change(function() {
		if ($(this).val() === 'receberEmCasa') {
			$("#franquia").prop("disabled", true);
		}
	});
</script>
