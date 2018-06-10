<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Equipe</h4>
</div>
<br>
<div class="fundo-branco">
	<c:if test="${pesquisa}">
		<button class="btn btn-basic" onclick="$('#form-pesquisa').show()">Exibir filtros de pesquisa</button>
	</c:if>
	<form id="form-pesquisa" action="<c:url value="/equipe/pesquisar"/>" method="post" <c:if test="${pesquisa}"> style="display:none"  </c:if>>
		<fieldset>
			<legend>Pesquisa</legend>
			<div class="control-group" style="margin-top: 5px;">
				<label class="control-label">Por ID</label>
				<div class="controls">
					<input type="text" name="pesquisaEquipeDTO.idCodigo" value="${pesquisaEquipeDTO.idCodigo}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Por nível</label>
				<div class="controls">
					<input type="text" name="pesquisaEquipeDTO.nivel" value="${pesquisaEquipeDTO.nivel}">
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
				<label class="control-label">Por mês de aniversário</label>
				<div class="controls">
					<select id='mesAniversario' name="pesquisaEquipeDTO.mesAniversario">
						<option value="">Selecione</option>
						<option value="01">Janeiro</option>
						<option value="02">Fevereiro</option>
						<option value="03">Março</option>
						<option value="04">Abril</option>
						<option value="05">Maio</option>
						<option value="06">Junho</option>
						<option value="07">Julho</option>
						<option value="08">Agosto</option>
						<option value="09">Setembro</option>
						<option value="10">Outubro</option>
						<option value="11">Novembro</option>
						<option value="12">Dezembro</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Ativos</label>
				<div class="controls">
					<select id='ativos' name="pesquisaEquipeDTO.ativos">
						<option value="">Selecione</option>
						<option value="true">Sim</option>
						<option value="false">Não</option>
					</select>
				</div>
			</div>
			<br>
			<div class="control-group">
				<input <c:if test="${pesquisaEquipeDTO.apenasIndicados}"> checked="checked" </c:if> type="checkbox" name="pesquisaEquipeDTO.apenasIndicados">
				<span> Apenas indicados </span>
			</div>
			<br>
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
	$('#ativos').val('${pesquisaEquipeDTO.ativos}');
	$('#mesAniversario').val('${pesquisaEquipeDTO.mesAniversario}');
</script>
