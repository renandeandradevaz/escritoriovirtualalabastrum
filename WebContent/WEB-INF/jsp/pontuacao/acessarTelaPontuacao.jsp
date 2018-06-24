<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Pontua��o</h4>
</div>
<br>
<div class="fundo-branco">
	<c:if test="${empty pontuacaoDTO}">
		<h5>Sua rede ainda n�o possui as 3 primeiras linhas para exibi��o deste relat�rio (Esquerda, meio, direita)</h5>
	</c:if>
	<c:if test="${!empty pontuacaoDTO}">
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<th></th>
					<c:forEach items="${pontuacaoDTO.meses}" var="item">
						<th>${item}</th>
					</c:forEach>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>Pontua��o pessoal</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesPessoais}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontua��o pessoal para qualifica��o</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesPessoaisParaQualificacao}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontua��o linha da esquerda</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesLinhaDaEsquerda}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontua��o linha da esquerda para qualifica��o</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesLinhaDaEsquerdaParaQualificacao}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontua��o linha do meio</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesLinhaDoMeio}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontua��o linha do meio para qualifica��o</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesLinhaDoMeioParaQualificacao}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontua��o linha da direita</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesLinhaDaDireita}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontua��o linha da direita para qualifica��o</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesLinhaDaDireitaParaQualificacao}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>
						<b> Pontua��o total </b>
					</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesTotais}" var="item">
						<td class="centralizado">
							<b> ${item} </b>
						</td>
					</c:forEach>
				</tr>
				<tr>
					<td>
						<h3>
							<b> Total: ${pontuacaoDTO.total} </b>
						</h3>
					</td>
					<c:forEach items="${pontuacaoDTO.meses}" var="item">
						<td class="centralizado"></td>
					</c:forEach>
				</tr>
			</tbody>
		</table>
		<br>
		<br>
		<table class="table">
			<tbody>
				<tr>
					<td>Pontua��o para o pr�ximo n�vel: ${pontuacaoDTO.pontuacaoParaOProximoNivel}</td>
				</tr>
				<tr>
					<td>Pontua��o m�xima por linha: ${pontuacaoDTO.pontuacaoMaximaPorLinha}</td>
				</tr>
				<tr>
					<td>Pontua��o m�nima por linha: ${pontuacaoDTO.pontuacaoMinimaPorLinha}</td>
				</tr>
				<tr>
					<td>Valor m�nimo de pontua��o pessoal para qualifica��o: ${pontuacaoDTO.valorMinimoPontuacaoPessoalParaQualificacao}</td>
				</tr>
			</tbody>
		</table>
	</c:if>
</div>
