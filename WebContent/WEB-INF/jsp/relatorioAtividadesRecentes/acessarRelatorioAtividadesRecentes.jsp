<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Atividades recentes</h4>
</div>
<br>
<div class="fundo-branco">
	<c:if test="${pesquisa}">
		<button class="btn btn-basic" onclick="$('#form-pesquisa').show()">Exibir filtros de pesquisa</button>
	</c:if>
	<form id="form-pesquisa" action="<c:url value="/relatorioAtividadesRecentes/pesquisarAtividadesRecentes"/>" method="post" <c:if test="${pesquisa}"> style="display:none"  </c:if>>
		<fieldset>
			<legend>Pesquisa</legend>
			<div class="control-group">
				<label class="control-label">Por tipo de atividade</label>
				<div class="controls">
					<select name='pesquisaRelatorioAtividadesRecentesDTO.tipoDeAtividade' id='tipoDeAtividade'>
						<option value="Novo cadastro">Novo cadastro</option>
						<option value="Adesão finalizada">Adesão finalizada</option>
						<option value="Pedido finalizado">Pedido finalizado</option>
						<option value="Pedido pendente">Pedido pendente</option>
						<option value="Pedido cancelado">Pedido cancelado</option>
						<option value="Pedido de PA pago">Pedido de PA pago</option>
						<option value="Pedido de PA pendente">Pedido de PA pendente</option>
						<option value="Solicitação de saque">Solicitação de saque</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Data inicial</label>
				<div class="controls">
					<input type="text" name="pesquisaRelatorioAtividadesRecentesDTO.dataInicial" value="${pesquisaRelatorioAtividadesRecentesDTO.dataInicial}" placeholder="03/11/2020" class="calendario" autocomplete="off" required="required">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Data final</label>
				<div class="controls">
					<input type="text" name="pesquisaRelatorioAtividadesRecentesDTO.dataFinal" value="${pesquisaRelatorioAtividadesRecentesDTO.dataFinal}" placeholder="25/04/2021" class="calendario" autocomplete="off" required="required">
				</div>
			</div>
			<br>
			<button type="submit" class="btn btn-primary">Pesquisar</button>
		</fieldset>
	</form>
</div>
<br>
<a class="btn btn-primary" id='imprimir'> Imprimir Tela </a>
<a class="btn btn-primary" href="#" onclick="download_table_as_csv('tabela-atividades-recentes');">Baixar CSV</a>
<br>
<br>
<div id='conteudo-impressao'>
	<table class="table table-striped table-bordered" id='tabela-atividades-recentes'>
		<thead>
			<tr>
				<th>Tipo de atividade</th>
				<th>Data</th>
				<th>Identificador</th>
				<c:if test="${pesquisaRelatorioAtividadesRecentesDTO.tipoDeAtividade == 'Novo cadastro'}">
					<th>Patrocinador</th>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${atividadesRecentes}" var="item">
				<tr>
					<td class="centralizado">${item.tipoDeAtividade}</td>
					<td class="centralizado">
						<fmt:formatDate value="${item.data.time}" type="DATE" />
					</td>
					<td class="centralizado">${item.identificador}</td>
					<c:if test="${pesquisaRelatorioAtividadesRecentesDTO.tipoDeAtividade == 'Novo cadastro'}">
						<td class="centralizado">${item.patrocinador}</td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br>
	<br>
	<br>
</div>
<script>
	$('#tipoDeAtividade').val(
			'${pesquisaRelatorioAtividadesRecentesDTO.tipoDeAtividade}');
</script>
