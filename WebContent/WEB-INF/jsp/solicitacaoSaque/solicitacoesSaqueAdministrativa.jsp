<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Solicitações de Saque</h4>
</div>
<div class="fundo-branco">
	<c:if test="${pesquisa}">
		<button class="btn btn-basic" onclick="$('#form-pesquisa').show()">Exibir filtros de pesquisa</button>
	</c:if>
	<form id="form-pesquisa" action="<c:url value="/solicitacaoSaque/solicitacoesSaqueAdministrativa"/>" method="post" <c:if test="${pesquisa}"> style="display:none"  </c:if>>
		<fieldset>
			<legend>Pesquisa</legend>
			<div class="control-group">
				<label class="control-label">Por status</label>
				<div class="controls">
					<select name='status' id='status'>
						<option value="PENDENTE">PENDENTE</option>
						<option value="FINALIZADO">FINALIZADO</option>
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
<a class="btn btn-primary" href="#" onclick="download_table_as_csv('tabela-solicitacoes');">Baixar CSV</a>
<br>
<br>
<div id='conteudo-impressao'>
	<div class="fundo-branco">
		<table class="table table-striped table-bordered" id='tabela-solicitacoes'>
			<thead>
				<tr>
					<th>Data</th>
					<th>Status</th>
					<th>Nickname</th>
					<th>Nome</th>
					<th>Valor bruto solicitado (R$)</th>
					<th>Valor final com descontos (R$)</th>
					<th>CPF</th>
					<th>Banco</th>
					<th>Agência</th>
					<th>Nº da Conta</th>
					<th>Adm que aprovou</th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${solicitacoes}" var="item">
					<tr>
						<td class="centralizado">
							<fmt:formatDate value="${item.data.time}" type="DATE" />
						</td>
						<td class="centralizado">${item.status}</td>
						<td class="centralizado">${item.usuario.apelido}</td>
						<td class="centralizado">${item.usuario.vNome}</td>
						<td class="centralizado">
							<fmt:formatNumber value="${item.valorBrutoSolicitado}" pattern="#,##0.00" />
						</td>
						<td class="centralizado">
							<fmt:formatNumber value="${item.valorFinalComDescontos}" pattern="#,##0.00" />
						</td>
						<td class="centralizado">${item.usuario.CPF}</td>
						<td class="centralizado">${item.usuario.cadBanco}</td>
						<td class="centralizado">${item.usuario.cadAgencia}</td>
						<td class="centralizado">${item.usuario.cadCCorrente}</td>
						<td class="centralizado">
							<c:if test="${not empty item.usuarioAdm}">
							${item.usuarioAdm.apelido}
							<br>
                             -
                            <br>
							${item.usuarioAdm.vNome}
						</c:if>
						</td>
						<td style="text-align: center;">
							<c:if test="${item.status == 'PENDENTE'}">
								<a class="btn btn-danger" href="<c:url value="/solicitacaoSaque/cancelarSolicitacao/${item.id}"/>"> Cancelar </a>
							</c:if>
						</td>
						<td style="text-align: center;">
							<c:if test="${item.status == 'PENDENTE'}">
								<a class="btn btn-success" href="<c:url value="/solicitacaoSaque/confirmarSolicitacao/${item.id}"/>"> Confirmar </a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		<a href="javascript:history.back()">Voltar</a>
		<br>
	</div>
</div>
<script>
	$('#status').val('${status}');
</script>
