<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Hist�rico de b�nus</h4>
</div>
<br>
<div class="fundo-branco">
	<c:if test="${pesquisa}">
		<button class="btn btn-basic" onclick="$('#form-pesquisa').show()">Exibir filtros de pesquisa</button>
	</c:if>
	<form id="form-pesquisa" action="<c:url value="/relatorioHistoricoDeBonus/pesquisarHistoricoDeBonus"/>" method="post" <c:if test="${pesquisa}"> style="display:none"  </c:if>>
		<fieldset>
			<legend>Pesquisa</legend>
			<div class="control-group" style="margin-top: 5px;">
				<label class="control-label">Por nickname</label>
				<div class="controls">
					<input type="text" name="pesquisaRelatorioHistoricoDeBonusDTO.apelido" value="${pesquisaRelatorioHistoricoDeBonusDTO.apelido}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Por tipo de b�nus</label>
				<div class="controls">
					<select name='pesquisaRelatorioHistoricoDeBonusDTO.tipoDeBonus' id='tipoDeBonus'>
						<option value="">TODOS</option>
						<option value="B�nus de primeira compra">B�nus de primeira compra</option>
						<option value="B�nus de ades�o de ponto de apoio">B�nus de ades�o de ponto de apoio</option>
						<option value="B�nus linear">B�nus linear</option>
						<option value="B�nus Trin�rio">B�nus Trin�rio</option>
						<option value="B�nus de Fila �nica">B�nus de Fila �nica</option>
						<option value="B�nus Global">B�nus Global</option>
						<option value="B�nus de Reconhecimento">B�nus de Reconhecimento</option>
						<option value="B�nus de Desempenho">B�nus de Desempenho</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Data inicial</label>
				<div class="controls">
					<input type="text" name="pesquisaRelatorioHistoricoDeBonusDTO.dataInicial" value="${pesquisaRelatorioHistoricoDeBonusDTO.dataInicial}" placeholder="03/11/2020" class="calendario" autocomplete="off">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Data final</label>
				<div class="controls">
					<input type="text" name="pesquisaRelatorioHistoricoDeBonusDTO.dataFinal" value="${pesquisaRelatorioHistoricoDeBonusDTO.dataFinal}" placeholder="25/04/2021" class="calendario" autocomplete="off">
				</div>
			</div>
			<br>
			<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Pesquisar</button>
		</fieldset>
	</form>
</div>
<br>
<a class="btn btn-primary" id='imprimir'> Imprimir Tela </a>
<a class="btn btn-primary" href="#" onclick="download_table_as_csv('tabela-historico-bonus');">Baixar CSV</a>
<br>
<br>
<div id='conteudo-impressao'>
	<table class="table table-striped table-bordered" id='tabela-historico-bonus'>
		<thead>
			<tr>
				<th>C�digo do pedido</th>
				<th>Tipo do pedido</th>
				<th>Distribuidor</th>
				<th>Franquia</th>
				<th>Data</th>
				<th>Valor (R$)</th>
				<th>Status</th>
				<th>Forma de pagamento</th>
				<th>Forma de entrega</th>
				<th>Empresa para entrega</th>
				<th>A��es</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${pedidosDTO}" var="item">
				<tr>
					<td class="centralizado">${item.pedido.id}</td>
					<td class="centralizado">${item.pedido.tipo}</td>
					<td class="centralizado">${item.distribuidor.apelido}
						<br>
						-
						<br>
						${item.distribuidor.vNome}
					</td>
					<td class="centralizado">${item.franquia.estqUF}-${item.franquia.estqCidade}-${item.franquia.estqBairro}</td>
					<td class="centralizado">
						<fmt:formatDate value="${item.pedido.data.time}" type="DATE" />
					</td>
					<td class="centralizado">
						<fmt:formatNumber value="${item.valorTotal}" pattern="#,##0.00" />
					</td>
					<td class="centralizado">${item.pedido.status}</td>
					<td class="centralizado">${item.pedido.formaDePagamento}</td>
					<td class="centralizado">${item.pedido.formaDeEntrega}</td>
					<td class="centralizado">${item.pedido.empresaParaEntrega}</td>
					<td style="text-align: center;">
						<a class="btn btn-default" href="<c:url value="/pedido/verItens/${item.pedido.id}"/>"> Detalhar </a>
						<br>
						<br>
						<c:if test="${item.pedido.status == 'PENDENTE'}">
							<a class="btn btn-success" href="<c:url value="/pedido/alterarStatus/${item.pedido.id}/FINALIZADO"/>"> Marcar como pago e finalizar pedido </a>
							<br>
							<br>
							<a class="btn btn-danger" href="<c:url value="/pedido/alterarStatus/${item.pedido.id}/CANCELADO"/>"> Cancelar </a>
							<br>
							<br>
						</c:if>
						<a class="btn btn-info" href="<c:url value="/pedido/imprimirPedido/${item.pedido.id}"/>"> Imprimir pedido</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br>
	<br>
</div>
<script>
	$('#tipoDeBonus')
			.val('${pesquisaRelatorioHistoricoDeBonusDTO.tipoDeBonus}');
</script>
