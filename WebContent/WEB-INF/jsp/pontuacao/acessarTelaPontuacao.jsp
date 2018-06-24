<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Pontuação</h4>
</div>
<br>
<div class="fundo-branco">
	<c:if test="${empty pontuacaoDTO}">
		<h5>Sua rede ainda não possui as 3 primeiras linhas para exibição deste relatório (Esquerda, meio, direita)</h5>
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
					<td>Pontuação pessoal</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesPessoais}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontuação pessoal para qualificação</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesPessoaisParaQualificacao}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontuação linha da esquerda</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesLinhaDaEsquerda}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontuação linha da esquerda para qualificação</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesLinhaDaEsquerdaParaQualificacao}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontuação linha do meio</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesLinhaDoMeio}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontuação linha do meio para qualificação</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesLinhaDoMeioParaQualificacao}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontuação linha da direita</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesLinhaDaDireita}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>Pontuação linha da direita para qualificação</td>
					<c:forEach items="${pontuacaoDTO.pontuacoesLinhaDaDireitaParaQualificacao}" var="item">
						<td class="centralizado">${item}</td>
					</c:forEach>
				</tr>
				<tr>
					<td>
						<b> Pontuação total </b>
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
					<td>Pontuação para o próximo nível: ${pontuacaoDTO.pontuacaoParaOProximoNivel}</td>
				</tr>
				<tr>
					<td>Pontuação máxima por linha: ${pontuacaoDTO.pontuacaoMaximaPorLinha}</td>
				</tr>
				<tr>
					<td>Pontuação mínima por linha: ${pontuacaoDTO.pontuacaoMinimaPorLinha}</td>
				</tr>
				<tr>
					<td>Valor mínimo de pontuação pessoal para qualificação: ${pontuacaoDTO.valorMinimoPontuacaoPessoalParaQualificacao}</td>
				</tr>
			</tbody>
		</table>
	</c:if>
</div>
