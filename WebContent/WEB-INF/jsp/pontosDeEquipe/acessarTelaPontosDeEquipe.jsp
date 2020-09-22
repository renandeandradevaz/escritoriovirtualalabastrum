<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Relatório de Pontos de Equipe</h4>
</div>
<br>
<div class="fundo-branco">
	<form action="<c:url value="/pontosDeEquipe/gerarRelatorioPontosDeEquipe/${sessaoUsuario.usuario.id_Codigo}"/>" method="post">
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
		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Avançar</button>
	</form>
	<br>
	<br>
	<br>
</div>
<script>
	$('#mes').val('${mes}');
	$('#ano').val('${ano}');
</script>
