<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>O escrit�rio ainda est� em constru��o. Em breve voc� ter� novidades.</h4>
	<h4>Mas voc� j� pode fazer a sua primeira compra clicando no bot�o abaixo:</h4>
	<a href="<c:url value="/pedido/acessarTelaNovoPedido"/>" class="btn btn-primary"> Primeira compra </a>
	<br>
	<br>
	<br>
	<h6>Quer indicar algu�m?</h6>
	<h6>Passe o link abaixo pra pessoa que voc� quer indicar. Para ela poder realizar o cadastro:</h6>
	<a style="font-size: 10px" href="<c:url value="/cadastro?nickname=${sessaoUsuario.usuario.apelido}"/>"> https://ev.dunastes.com.br/cadastro?nickname=${sessaoUsuario.usuario.apelido} </a>
</div>
<!-- <div class="fundo-branco"> -->
<!-- 	<h4>In�cio</h4> -->
<!-- </div> -->
<!-- <br> -->
<%-- <img style="max-width: 80%;" src="<c:url value="/css/images/banner-home.jpg"/>"> --%>
<!-- <br> -->
<!-- <br> -->
<!-- <table> -->
<!-- 	<tr> -->
<!-- 		<td> -->
<!-- 			<div class="fundo-branco letra-branca" style="width: 450px; padding: 10px"> -->
<!-- 				<div class="grupo quadradinho-alinhado" style="margin-left: 100px;"> -->
<!-- 					<h5>Afiliados</h5> -->
<%-- 					<span> ${quantidadeAfiliados} </span> --%>
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<br> -->
<!-- 			<div class="fundo-branco" style="width: 450px; padding: 10px; height: 200px;"> -->
<!-- 				<h4>Minha qualifica��o</h4> -->
<!-- 				<div class="quadradinho-alinhado" style="margin-left: 100px;"> -->
<!-- 					<h5 style="margin-left: 30px;">Atual</h5> -->
<%-- 					<img src="<c:url value="/css/images/${posicaoAtual}.jpg"/>" style="width: 100px;"> --%>
<!-- 				</div> -->
<%-- 				<c:if test="${proximaPosicao != ''}"> --%>
<!-- 					<div class="quadradinho-alinhado"> -->
<!-- 						<h5 style="margin-left: 20px;">Pr�xima</h5> -->
<%-- 						<img src="<c:url value="/css/images/${proximaPosicao}.jpg"/>" style="width: 100px;"> --%>
<!-- 					</div> -->
<%-- 				</c:if> --%>
<!-- 			</div> -->
<!-- 		</td> -->
<!-- 		<td> -->
<!-- 			<div class="fundo-branco" style="width: 300px; padding: 10px; margin-top: 15px; margin-left: 50px; min-height: 350px;"> -->
<!-- 				<h4>�LTIMOS QUALIFICADOS</h4> -->
<!-- 				<table class="table table-striped table-bordered"> -->
<!-- 					<thead> -->
<!-- 						<tr> -->
<!-- 							<th>ID</th> -->
<!-- 							<th>Data</th> -->
<!-- 							<th>Qualifica��o</th> -->
<!-- 						</tr> -->
<!-- 					</thead> -->
<!-- 					<tbody> -->
<%-- 						<c:forEach items="${ultimosQualificados}" var="item"> --%>
<!-- 							<tr> -->
<%-- 								<td class="centralizado">${item.usuario.id_Codigo}</td> --%>
<!-- 								<td class="centralizado"> -->
<%-- 									<fmt:formatDate value="${item.qualificacao.data.time}" type="DATE" /> --%>
<!-- 								</td> -->
<%-- 								<td class="centralizado">${item.usuario.posAtual}</td> --%>
<%-- 						</c:forEach> --%>
<!-- 					</tbody> -->
<!-- 				</table> -->
<!-- 			</div> -->
<!-- 		</td> -->
<!-- 	</tr> -->
<!-- 	<tr> -->
<!-- 		<td> -->
<!-- 			<div class="fundo-branco" style="width: 450px; padding: 10px; margin-top: 15px;"> -->
<!-- 				<h4>�LTIMOS CADASTROS</h4> -->
<!-- 				<table class="table table-striped table-bordered"> -->
<!-- 					<thead> -->
<!-- 						<tr> -->
<!-- 							<th>Data</th> -->
<!-- 							<th>ID</th> -->
<!-- 							<th>Nome</th> -->
<!-- 							<th>L�der</th> -->
<!-- 						</tr> -->
<!-- 					</thead> -->
<!-- 					<tbody> -->
<%-- 						<c:forEach items="${ultimosCadastros}" var="item"> --%>
<!-- 							<tr> -->
<!-- 								<td class="centralizado"> -->
<%-- 									<fmt:formatDate value="${item.qualificacao.data.time}" type="DATE" /> --%>
<!-- 								</td> -->
<%-- 								<td class="centralizado">${item.usuario.id_Codigo}</td> --%>
<%-- 								<td class="centralizado">${item.usuario.vNome}</td> --%>
<%-- 								<td class="centralizado">${item.usuario.id_lider}</td> --%>
<%-- 						</c:forEach> --%>
<!-- 					</tbody> -->
<!-- 				</table> -->
<!-- 			</div> -->
<!-- 		</td> -->
<!-- 	</tr> -->
<!-- </table> -->