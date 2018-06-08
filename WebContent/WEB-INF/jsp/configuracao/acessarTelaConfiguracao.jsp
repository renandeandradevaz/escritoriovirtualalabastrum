<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<form action="<c:url value="/configuracao/salvarConfiguracoes"/>" method="post">
		<fieldset>
			<legend>Configurações</legend>
			<div class="control-group warning">
				<label class="control-label">Direcionamento de novos cadastros</label>
				<div class="controls">
					<select id='direcionamentoNovosCadastros' name="configuracaoDTO.direcionamentoNovosCadastros" style="width: 160px;">
						<option value="">Selecione</option>
						<option value="todas">Todas posições</option>
						<option value="esquerda">Esquerda</option>
						<option value="meio">Meio</option>
						<option value="direita">Direita</option>
					</select>
				</div>
			</div>
			<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Salvar</button>
		</fieldset>
	</form>
</div>
<script>
	$('#direcionamentoNovosCadastros').val(
			'${configuracaoDTO.direcionamentoNovosCadastros}');
</script>
