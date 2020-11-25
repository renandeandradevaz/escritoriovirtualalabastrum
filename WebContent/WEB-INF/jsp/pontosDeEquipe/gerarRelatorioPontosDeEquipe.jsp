<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Relatório de Pontos de Equipe</h4>
</div>
<br>
<a class="btn btn-primary" id='imprimir'> Imprimir Tela </a>
<a class="btn btn-primary" href="#" onclick="download_table_as_csv('tabela-pontuacoes');">Baixar CSV</a>
<br>
<br>
<br>
<div id='conteudo-impressao'>
	<h6>Pontos do Distribuidor</h6>
	<div class="fundo-branco">
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<th>Distribuidor</th>
					<th>Nickname</th>
					<th>Posição</th>
					<th>UF</th>
					<th>Telefone</th>
					<th>VIP</th>
					<th>VIQ</th>
					<th>VP</th>
					<th>VQ</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="centralizado">${pontosUsuarioSelecionado.usuario.vNome}</td>
					<td class="centralizado">${pontosUsuarioSelecionado.usuario.apelido}</td>
					<td class="centralizado">${pontosUsuarioSelecionado.usuario.posAtual}</td>
					<td class="centralizado">${pontosUsuarioSelecionado.usuario.cadUF}</td>
					<td class="centralizado">${pontosUsuarioSelecionado.usuario.cadCelular}</td>
					<td class="centralizado">${pontosUsuarioSelecionado.volumeIndividualPagavel}</td>
					<td class="centralizado">${pontosUsuarioSelecionado.volumeIndividualQualificavel}</td>
					<td class="centralizado">${pontosUsuarioSelecionado.volumePagavelTodosOsNiveis}</td>
					<td class="centralizado">${pontosUsuarioSelecionado.volumeQualificavelTodosOsNiveis}</td>
				</tr>
			</tbody>
		</table>
	</div>
	<br>
	<h6>Pontos da Equipe</h6>
	<div class="fundo-branco">
		<table class="table table-striped table-bordered" id="tabela-pontuacoes">
			<thead>
				<tr>
					<th>Distribuidor</th>
					<th>Nickname</th>
					<th>Posição</th>
					<th>UF</th>
					<th>Telefone</th>
					<th>VIP</th>
					<th>VIQ</th>
					<th>VP</th>
					<th>VQ</th>
					<th>Ações</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${pontosEquipeDTOLista}" var="item">
					<tr>
						<td class="centralizado">${item.usuario.vNome}</td>
						<td class="centralizado">${item.usuario.apelido}</td>
						<td class="centralizado">${item.usuario.posAtual}</td>
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
</div>
<br>
<a href="javascript:history.back()">Voltar</a>
<br>
<br>
<br>
<br>
