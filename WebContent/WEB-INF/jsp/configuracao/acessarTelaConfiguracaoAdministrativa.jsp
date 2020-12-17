<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<form action="<c:url value="/configuracao/salvarConfiguracoesAdministrativas"/>" method="post">
		<fieldset>
			<legend>Configurações administrativas</legend>
			<div class="control-group" style="display: none">
				<label class="control-label">Senha do email</label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.senhaEmail" value="${configuracaoAdministrativaDTO.senhaEmail}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Valor mínimo de pontuacao pessoal para qualificação</label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.valorMinimoPontuacaoPessoalParaQualificacao" value="${configuracaoAdministrativaDTO.valorMinimoPontuacaoPessoalParaQualificacao}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Valor mínimo de pedido de adesão</label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.valorMinimoPedidoAdesao" value="${configuracaoAdministrativaDTO.valorMinimoPedidoAdesao}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Valor máximo de pedido de adesão</label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.valorMaximoPedidoAdesao" value="${configuracaoAdministrativaDTO.valorMaximoPedidoAdesao}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Valor mínimo de pedido de atividade</label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.valorMinimoPedidoAtividade" value="${configuracaoAdministrativaDTO.valorMinimoPedidoAtividade}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Valor máximo de pedido de atividade</label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.valorMaximoPedidoAtividade" value="${configuracaoAdministrativaDTO.valorMaximoPedidoAtividade}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> Pontuação máxima por linha (%)</label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.pontuacaoMaximaPorLinhaEmPorcentagem" value="${configuracaoAdministrativaDTO.pontuacaoMaximaPorLinhaEmPorcentagem}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> Pontuação mínima por linha (%) </label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.pontuacaoMinimaPorLinhaEmPorcentagem" value="${configuracaoAdministrativaDTO.pontuacaoMinimaPorLinhaEmPorcentagem}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> Quantidade de meses para somatório de pontuação </label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.quantidadeDeMesesSomatorioPontuacao" value="${configuracaoAdministrativaDTO.quantidadeDeMesesSomatorioPontuacao}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> Valor mínimo para pedidos de distribuidores na primeira posição </label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.valorMinimoPedidosPrimeiraPosicao" value="${configuracaoAdministrativaDTO.valorMinimoPedidosPrimeiraPosicao}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> Email pagseguro </label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.emailPagseguro" value="${configuracaoAdministrativaDTO.emailPagseguro}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> Token pagseguro </label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.tokenPagseguro" value="${configuracaoAdministrativaDTO.tokenPagseguro}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> Token Melhor envio </label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.tokenMelhorEnvio" value="${configuracaoAdministrativaDTO.tokenMelhorEnvio}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> Api key Pagar.me </label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.apiKeyPagarMe" value="${configuracaoAdministrativaDTO.apiKeyPagarMe}">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> Token EV</label>
				<div class="controls">
					<input type="text" name="configuracaoAdministrativaDTO.tokenEV" value="${configuracaoAdministrativaDTO.tokenEV}">
				</div>
			</div>
			<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Salvar</button>
		</fieldset>
	</form>
</div>
