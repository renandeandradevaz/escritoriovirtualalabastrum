<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Histórico de bônus</h4>
	<br>
	<span>(Observação: Este relatório trabalha com uma quantidade imensa de informações, e pode ser que demore pra exibir os resultados da busca. Tenha paciência enquanto o mesmo é carregado) </span>
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
				<label class="control-label">Por tipo de bônus</label>
				<div class="controls">
					<select name='pesquisaRelatorioHistoricoDeBonusDTO.tipoDeBonus' id='tipoDeBonus'>
						<option value="">TODOS</option>
						<option value="Bônus de primeira compra">Bônus de primeira compra</option>
						<option value="Bônus de adesão de ponto de apoio">Bônus de adesão de ponto de apoio</option>
						<option value="Bônus linear">Bônus linear</option>
						<option value="Bônus Trinário">Bônus Trinário</option>
						<option value="Bônus de Fila Única">Bônus de Fila Única</option>
						<option value="Bônus Global">Bônus Global</option>
						<option value="Bônus de Reconhecimento">Bônus de Reconhecimento</option>
						<option value="Bônus de Desempenho">Bônus de Desempenho</option>
						<option value="Bônus Loja Virtual">Bônus Loja Virtual</option>
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
				<th>Distribuidor beneficiado</th>
				<th>Data</th>
				<th>Histórico</th>
				<th>Distribuidor de origem</th>
				<th>Valor (R$)</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${bonificacoes}" var="item">
				<tr>
					<td class="centralizado">
						${item.usuarioRecebedorDoBonus.apelido}
						<br>
						-
						<br>
						${item.usuarioRecebedorDoBonus.vNome}
					</td>
					<td class="centralizado">
						<fmt:formatDate value="${item.extratoDTO.data.time}" type="DATE" />
					</td>
					<td class="centralizado">${item.extratoDTO.discriminador}</td>
					<td class="centralizado">
						<c:if test="${not empty item.extratoDTO.usuario}">
							${item.extratoDTO.usuario.apelido}
							<br>
                             -
                            <br>
							${item.extratoDTO.usuario.vNome}
						</c:if>
					</td>
					<td class="centralizado">
						<fmt:formatNumber value="${item.extratoDTO.valor}" pattern="#,##0.00" />
					</td>
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
	$('#tipoDeBonus')
			.val('${pesquisaRelatorioHistoricoDeBonusDTO.tipoDeBonus}');
</script>
