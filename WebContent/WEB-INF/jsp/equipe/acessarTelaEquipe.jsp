<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Equipe</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/equipe/acessarTelaEquipe"/>" method="post">
		<fieldset>
			<legend>Pesquisa</legend>
			<div class="control-group" style="margin-top: 5px;">
				<label class="control-label">Por ID</label>
				<div class="controls">
					<input type="text" name="pesquisaEquipeDTO.idCodigo" value="${pesquisaEquipeDTO.idCodigo}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Por posição</label>
				<div class="controls">
					<select id='posicao' name="pesquisaEquipeDTO.posicao">
						<option value="">Selecione</option>
						<c:forEach items="${posicoes}" var="item">
							<option value="${item.nome}">${item.nome}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Por nível</label>
				<div class="controls">
					<input type="text" name="pesquisaEquipeDTO.nivel" value="${pesquisaEquipeDTO.nivel}">
				</div>
			</div>
			<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Pesquisar</button>
		</fieldset>
	</form>
</div>
<br>
<div class="fundo-branco">
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
				<th>ID</th>
				<th>Posição</th>
				<th>Nome</th>
				<th>Nascimento</th>
				<th>Tel</th>
				<th>Celular</th>
				<th>Email</th>
				<th>Nivel</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${arvoreHierarquica}" var="item">
				<tr>
					<td class="centralizado">${item.usuario.id_Codigo}</td>
					<td class="centralizado">${item.usuario.posAtual}</td>
					<td class="centralizado">${item.usuario.vNome}</td>
					<td class="centralizado">${item.usuario.dt_Nasc}</td>
					<td class="centralizado">${item.usuario.tel}</td>
					<td class="centralizado">${item.usuario.cadCelular}</td>
					<td class="centralizado">${item.usuario.eMail}</td>
					<td class="centralizado">${item.nivel}</td>
			</c:forEach>
		</tbody>
	</table>
</div>
<script>
	$('#posicao').val('${pesquisaEquipeDTO.posicao}');
</script>
