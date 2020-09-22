<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Relatório de Pontos de Equipe</h4>
</div>
<br>
<div class="fundo-branco">
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th>Distribuidor</th>
				<th>UF</th>
				<th>Telefone</th>
				<th>VI (VP)</th>
				<th>VI (VQ)</th>
				<th>VP</th>
				<th>VQ</th>
				<th>Ações</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${pontosEquipeDTOLista}" var="item">
				<tr>
					<td class="centralizado">${item.usuario.vNome}</td>
					<td class="centralizado">${item.usuario.cadUF}</td>
					<td class="centralizado">${item.usuario.cadCelular}</td>
					<td class="centralizado">${item.volumeIndividualPagavel}</td>
					<td class="centralizado">${item.volumeIndividualQualificavel}</td>
					<td class="centralizado">${item.volumePagavelTodosOsNiveis}</td>
					<td class="centralizado">${item.volumeQualificavelTodosOsNiveis}</td>
					<td>
						<a href="<c:url value="/pontosDeEquipe/gerarRelatorioPontosDeEquipe/${item.usuario.id_Codigo}"/>"> Detalhar </a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<br>
<a href="javascript:history.back()">Voltar</a>
<br>
<br>
<br>
<br>
