<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Relatório de pedidos</h4>
</div>
<br>
<div class="fundo-branco">
	<c:if test="${pesquisa}">
		<button class="btn btn-basic" onclick="$('#form-pesquisa').show()">Exibir filtros de pesquisa</button>
	</c:if>
	<form id="form-pesquisa" action="<c:url value="/relatorioDePedidos/pesquisarPedidos"/>" method="post" <c:if test="${pesquisa}"> style="display:none"  </c:if>>
		<fieldset>
			<legend>Pesquisa</legend>
			<div class="control-group" style="margin-top: 5px;">
				<label class="control-label">Por nickname</label>
				<div class="controls">
					<input type="text" name="pesquisaRelatorioPedidosDTO.apelido" value="${pesquisaRelatorioPedidosDTO.apelido}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Por número do pedido</label>
				<div class="controls">
					<input type="text" name="pesquisaRelatorioPedidosDTO.numeroPedido" value="${pesquisaRelatorioPedidosDTO.numeroPedido}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Por status</label>
				<div class="controls">
					<select name='pesquisaRelatorioPedidosDTO.status' id='status'>
						<option value="">TODOS</option>
						<option value="PENDENTE">PENDENTE</option>
						<option value="FINALIZADO">FINALIZADO</option>
						<option value="CANCELADO">CANCELADO</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Data inicial</label>
				<div class="controls">
					<input type="text" name="pesquisaRelatorioPedidosDTO.dataInicial" value="${pesquisaRelatorioPedidosDTO.dataInicial}" placeholder="03/11/2020" class="calendario" autocomplete="off">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Data final</label>
				<div class="controls">
					<input type="text" name="pesquisaRelatorioPedidosDTO.dataFinal" value="${pesquisaRelatorioPedidosDTO.dataFinal}" placeholder="25/04/2021" class="calendario" autocomplete="off">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Por origem</label>
				<div class="controls">
					<select id='origem' name="pesquisaRelatorioPedidosDTO.origem">
						<option value="">Geral</option>
						<option value="internet">Internet</option>
						<c:forEach items="${franquias}" var="item">
							<option value="${item.id_Estoque}">${item.estqUF}-${item.estqCidade}-${item.estqBairro}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<br>
			<div class="control-group">
				<label class="control-label">Ordenação</label>
				<div class="controls">
					<select id='ordenacao' name="pesquisaRelatorioPedidosDTO.ordenacao">
						<option value="numero">Por número do pedido</option>
						<option value="nome">Por nome</option>
						<option value="data">Por data</option>
						<option value="formaDePagamento">Por forma de pagamento</option>
						<option value="valor">Por valor</option>
					</select>
				</div>
			</div>
			<br>
			<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Pesquisar</button>
		</fieldset>
	</form>
</div>
<br>
<a class="btn btn-primary" id='imprimir'> Imprimir Tela </a>
<a class="btn btn-primary" href="#" onclick="download_table_as_csv('tabela-pedidos');">Baixar CSV</a>
<br>
<br>
<div id='conteudo-impressao'>
	<div class="fundo-branco">
		<h5>Contagem da busca: ${quantidadeDePedidos}</h5>
	</div>
	<br>
	<table class="table table-striped table-bordered" id='tabela-pedidos'>
		<thead>
			<tr>
				<th>Código do pedido</th>
				<th>Tipo do pedido</th>
				<th>Distribuidor</th>
				<th>Franquia</th>
				<th>Data</th>
				<th>Valor (R$)</th>
				<th>Status</th>
				<th>Forma de pagamento</th>
				<th>Forma de entrega</th>
				<th>Empresa para entrega</th>
				<th>Ações</th>
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
	<div class="fundo-branco">
		<h5>
			Valor total: R$
			<fmt:formatNumber value="${total}" pattern="#,##0.00" />
		</h5>
	</div>
	<br>
	<br>
	<br>
</div>
<script>
	$('#status').val('${pesquisaRelatorioPedidosDTO.status}');
	$('#origem').val('${pesquisaRelatorioPedidosDTO.origem}');
	$('#ordenacao').val('${pesquisaRelatorioPedidosDTO.ordenacao}');
</script>
