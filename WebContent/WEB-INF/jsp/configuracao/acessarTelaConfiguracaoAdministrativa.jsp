<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<form action="<c:url value="/configuracao/salvarConfiguracoesAdministrativas"/>" method="post">
		<fieldset>
			<legend>Configura��es administrativas</legend>
			<div class="control-group" style="display: none">
				<label class="control-label">Senha do email</label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.senhaEmail" value="${configuracaoAdministrativaDTO.senhaEmail}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Valor m�nimo de pontuacao pessoal para qualifica��o</label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.valorMinimoPontuacaoPessoalParaQualificacao" value="${configuracaoAdministrativaDTO.valorMinimoPontuacaoPessoalParaQualificacao}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> Pontua��o m�xima por linha (%)</label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.pontuacaoMaximaPorLinhaEmPorcentagem" value="${configuracaoAdministrativaDTO.pontuacaoMaximaPorLinhaEmPorcentagem}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> Pontua��o m�nima por linha (%) </label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.pontuacaoMinimaPorLinhaEmPorcentagem" value="${configuracaoAdministrativaDTO.pontuacaoMinimaPorLinhaEmPorcentagem}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> Quantidade de meses para somat�rio de pontua��o </label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.quantidadeDeMesesSomatorioPontuacao" value="${configuracaoAdministrativaDTO.quantidadeDeMesesSomatorioPontuacao}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> Valor m�nimo para pedidos de distribuidores na primeira posi��o </label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.valorMinimoPedidosPrimeiraPosicao" value="${configuracaoAdministrativaDTO.valorMinimoPedidosPrimeiraPosicao}">
				</div>
			</div>
			<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Salvar</button>
		</fieldset>
	</form>
</div>
