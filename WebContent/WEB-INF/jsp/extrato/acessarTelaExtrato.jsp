<%@ include file="/base.jsp"%>
<style>
@media only screen and (min-width: 1200px) {
	.detalhar {
		margin-left: 40%;
	}
}
</style>
<div class="fundo-branco">
	<h4>Extrato detalhado</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/extrato/gerarExtrato"/>" method="post">
		<div class="control-group">
			<label class="control-label">Mês</label>
			<div class="controls">
				<select name="mes" id='mes'>
					<option value="0">Janeiro</option>
					<option value="1">Fevereiro</option>
					<option value="2">Março</option>
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
<c:if test="${not empty extrato}">
	<div class="fundo-branco">
		<h6 style="text-align: center;">Saldos</h6>
		<span style="font-size: 16px;"> Saldo previsto no mês atual: </span>
		<span style="font-weight: bold; font-size: 18px;">
			R$
			<fmt:formatNumber value="${extrato.saldoPrevistoNoMes}" pattern="#,##0.00" />
		</span>
		<br>
		<br>
		<span style="font-size: 16px;"> Saldo do mês atual: </span>
		<span style="font-weight: bold; font-size: 18px;">
			R$
			<fmt:formatNumber value="${extrato.saldoDoMesAtual}" pattern="#,##0.00" />
		</span>
		<br>
		<br>
		<span style="font-size: 16px;"> Saldo previsto total atual: </span>
		<span style="font-weight: bold; font-size: 18px;">
			R$
			<fmt:formatNumber value="${extrato.saldoPrevistoTotal}" pattern="#,##0.00" />
		</span>
		<br>
		<br>
		<span style="font-size: 16px;"> INSS: </span>
		<span style="font-weight: bold; font-size: 18px;">
			R$
			<fmt:formatNumber value="${extrato.inss}" pattern="#,##0.00" />
		</span>
		<br>
		<br>
		<span style="font-size: 16px;"> Saldo liberado atual: </span>
		<span style="font-weight: bold; font-size: 18px;">
			R$
			<fmt:formatNumber value="${extrato.saldoComDescontos}" pattern="#,##0.00" />
		</span>
		<br>
		<br>
		<span style="font-size: 16px;"> Ganhos até hoje: </span>
		<span style="font-weight: bold; font-size: 18px;">
			R$
			<fmt:formatNumber value="${extrato.ganhosAteHoje}" pattern="#,##0.00" />
		</span>
		<br>
		<br>
		<br>
		<h6>Observações:</h6>
		<h6>* Para receber qualquer tipo de bônus, é necessário ficar ativo.</h6>
		<h6>* Para receber o Bônus Trinário, além de estar ativo, é necessário ter, no mínimo, 3 indicados diretos ativos no mês.</h6>
		<h6>* Para receber o Bônus De Fila Única, além de estar ativo, e ter 3 indicados ativos diretos, é necessário ter pontuação de qualificação suficiente (Consulte os valores exatos com seu líder).</h6>
		<br>
	</div>
	<br>
	<div class="fundo-branco">
		<h6 style="text-align: center;">
			Bônus Totais (Mês:
			<fmt:formatNumber value="${mes + 1}" pattern="##00" />
			/ ${ano})
		</h6>
		<table class="table table-striped table-bordered" style="font-size: 10px">
			<tbody>
				<tr>
					<td class="centralizado">Bônus de Primeira Compra</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${extrato.bonusPrimeiraCompraNoMes}" pattern="#,##0.00" />
					</td>
					<td>
						<form action="<c:url value="/extrato/detalharBonificacao"/>" method="post">
							<input type="hidden" name="bonificacao" value="Bônus de primeira compra">
							<button type="submit" class="btn btn-primary detalhar" onclick="this.disabled=true;this.form.submit();">Detalhar</button>
						</form>
					</td>
				</tr>
				<tr>
					<td class="centralizado">Bônus de Adesão de Ponto de Apoio</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${extrato.bonusDeAdesaoDePontoDeApoioNoMes}" pattern="#,##0.00" />
					</td>
					<td>
						<form action="<c:url value="/extrato/detalharBonificacao"/>" method="post">
							<input type="hidden" name="bonificacao" value="Bônus de adesão de ponto de apoio">
							<button type="submit" class="btn btn-primary detalhar" onclick="this.disabled=true;this.form.submit();">Detalhar</button>
						</form>
					</td>
				</tr>
				<tr>
					<td class="centralizado">Bônus Linear</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${extrato.bonusLinearNoMes}" pattern="#,##0.00" />
					</td>
					<td>
						<form action="<c:url value="/extrato/detalharBonificacao"/>" method="post">
							<input type="hidden" name="bonificacao" value="Bônus linear">
							<button type="submit" class="btn btn-primary detalhar" onclick="this.disabled=true;this.form.submit();">Detalhar</button>
						</form>
					</td>
				</tr>
				<tr>
					<td class="centralizado">Bônus Trinário</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${extrato.bonusTrinarioNoMes}" pattern="#,##0.00" />
					</td>
					<td>
						<form action="<c:url value="/extrato/detalharBonificacao"/>" method="post">
							<input type="hidden" name="bonificacao" value="Bônus Trinário">
							<button type="submit" class="btn btn-primary detalhar" onclick="this.disabled=true;this.form.submit();">Detalhar</button>
						</form>
					</td>
				</tr>
				<tr>
					<td class="centralizado">Bônus De Fila Única</td>
					<td class="centralizado">
						R$
						<fmt:formatNumber value="${extrato.bonusFilaUnicaNoMes}" pattern="#,##0.00" />
					</td>
					<td>
						<form action="<c:url value="/extrato/detalharBonificacao"/>" method="post">
							<input type="hidden" name="bonificacao" value="Bônus de Fila Única">
							<button type="submit" class="btn btn-primary detalhar" onclick="this.disabled=true;this.form.submit();">Detalhar</button>
						</form>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</c:if>
<br>
<c:if test="${not empty extrato.extratoDoMes}">
	<div class="fundo-branco">
		<h6 style="text-align: center;">Extrato</h6>
		<table class="table table-striped table-bordered" style="font-size: 10px">
			<thead>
				<tr>
					<th>Data</th>
					<th>Histórico</th>
					<th>Distribuidor</th>
					<th>Valor</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${extrato.extratoDoMes}" var="item">
					<tr>
						<td class="centralizado">
							<fmt:formatDate value="${item.data.time}" type="DATE" />
						</td>
						<td class="centralizado">${item.discriminador}</td>
						<td class="centralizado">${item.usuario.apelido}
							<br>
							${item.usuario.vNome}
						</td>
						<td class="centralizado">
							R$
							<fmt:formatNumber value="${item.valor}" pattern="#,##0.00" />
						</td>
				</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>
<script>
	$('#mes').val('${mes}');
	$('#ano').val('${ano}');
</script>