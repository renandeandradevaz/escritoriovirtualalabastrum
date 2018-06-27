<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Extrato detalhado</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/extrato/gerarExtrato"/>" method="post">
		<div class="control-group">
			<label class="control-label">M�s</label>
			<div class="controls">
				<select name="mes" id='mes'>
					<option value="0">Janeiro</option>
					<option value="1">Fevereiro</option>
					<option value="2">Mar�o</option>
					<option value="3">Abril</option>
					<option value="4">Maio</option>
					<option value="5">Junho</option>
					<option value="6">Julho</option>
					<option value="7">Agosto</option>
					<option value="8">Setembro</option>
					<option value="9">Outubro</option>
					<option value="10">Novembro</option>
					<option value="11">Dezembro</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Ano</label>
			<div class="controls">
				<select name="ano" id='ano'>
					<option value="2017">2017</option>
					<option value="2018">2018</option>
					<option value="2019">2019</option>
					<option value="2020">2020</option>
					<option value="2021">2021</option>
					<option value="2022">2022</option>
					<option value="2023">2023</option>
				</select>
			</div>
		</div>
		<br>
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Ok</button>
	</form>
</div>
<br>
<c:if test="${not empty extratoDoMes}">
	<div class="fundo-branco">
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<th>Data</th>
					<th>Hist�rico</th>
					<th>Distribuidor</th>
					<th>Valor</th>
					<th>Saldo</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${extratoDoMes}" var="item">
					<tr>
						<td class="centralizado">
							<fmt:formatDate value="${item.data.time}" type="DATE" />
						</td>
						<td class="centralizado">${item.discriminador}</td>
						<td class="centralizado">${item.usuario.id_Codigo}-${item.usuario.vNome}</td>
						<td class="centralizado">
							R$
							<fmt:formatNumber value="${item.valor}" pattern="#,##0.00" />
						</td>
						<td class="centralizado">
							R$
							<fmt:formatNumber value="${item.saldo}" pattern="#,##0.00" />
						</td>
				</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>
<c:if test="${not empty saldo}">
	<div class="fundo-branco">
		<h3>
			Saldo atual: R$
			<fmt:formatNumber value="${saldo}" pattern="#,##0.00" />
		</h3>
	</div>
</c:if>
<script>
	$('#mes').val('${mes}');
	$('#ano').val('${ano}');
</script>