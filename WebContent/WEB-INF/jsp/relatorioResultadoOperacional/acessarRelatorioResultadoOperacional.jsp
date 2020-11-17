<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Resultado operacional</h4>
</div>
<br>
<div class="fundo-branco">
	<c:if test="${pesquisa}">
		<button class="btn btn-basic" onclick="$('#form-pesquisa').show()">Exibir filtros de pesquisa</button>
	</c:if>
	<form id="form-pesquisa" action="<c:url value="/relatorioResultadoOperacional/pesquisarResultadoOperacional"/>" method="post" <c:if test="${pesquisa}"> style="display:none"  </c:if>>
		<fieldset>
			<legend>Pesquisa</legend>
			<div class="control-group">
				<label class="control-label">Por origem</label>
				<div class="controls">
					<select id='origem' name="pesquisaRelatorioResultadoOperacionalDTO.origem">
						<option value="">Geral</option>
						<option value="internet">Internet</option>
						<c:forEach items="${franquias}" var="item">
							<option value="${item.id_Estoque}">${item.estqUF}-${item.estqCidade}-${item.estqBairro}</option>
						</c:forEach>
					</select>
					<span style="font-size: 10px"> Observação: Este filtro não é aplicado à coluna de saque realizado </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Data inicial</label>
				<div class="controls">
					<input type="text" name="pesquisaRelatorioResultadoOperacionalDTO.dataInicial" value="${pesquisaRelatorioResultadoOperacionalDTO.dataInicial}" placeholder="03/11/2020" class="calendario" autocomplete="off">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Data final</label>
				<div class="controls">
					<input type="text" name="pesquisaRelatorioResultadoOperacionalDTO.dataFinal" value="${pesquisaRelatorioResultadoOperacionalDTO.dataFinal}" placeholder="25/04/2021" class="calendario" autocomplete="off">
				</div>
			</div>
			<br>
			<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Pesquisar</button>
		</fieldset>
	</form>
</div>
<br>
<a class="btn btn-primary" id='imprimir'> Imprimir Tela </a>
<a class="btn btn-primary" href="#" onclick="download_table_as_csv('tabela-resultado-operacional');">Baixar CSV</a>
<br>
<br>
<div id='conteudo-impressao'>
	<table class="table table-striped table-bordered" id='tabela-resultado-operacional'>
		<thead>
			<tr>
				<th>Faturamento total (R$)</th>
				<th>Compras realizadas com saldo (R$)</th>
				<th>Saques realizados (Valor bruto) (R$)</th>
				<th>Saques realizados (Valor descontado) (R$)</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="centralizado">
					<fmt:formatNumber value="${faturamentoTotal}" pattern="#,##0.00" />
				</td>
				<td class="centralizado">
					<fmt:formatNumber value="${comprasRealizadasComSaldo}" pattern="#,##0.00" />
				</td>
				<td class="centralizado">
					<fmt:formatNumber value="${saquesRealizadosValorBruto}" pattern="#,##0.00" />
				</td>
				<td class="centralizado">
					<fmt:formatNumber value="${saquesRealizadosValorComDesconto}" pattern="#,##0.00" />
				</td>
			</tr>
		</tbody>
	</table>
	<br>
	<br>
	<br>
</div>
<script>
	$('#origem').val('${pesquisaRelatorioResultadoOperacionalDTO.origem}');
</script>
