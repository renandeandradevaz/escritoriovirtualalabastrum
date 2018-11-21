<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Informe seus dados pessoais</h4>
</div>
<br>
<div class="fundo-branco">
	<form>
		<fieldset>
			<legend>Cartão de crédito</legend>
			<input type="hidden" id="pagseguroSessionId" value="${pagseguroSessionId}" />
			<div class="control-group">
				<label class="control-label">Número do cartão</label>
				<div class="controls">
					<input type="text" id='numero'>
					<span style="color: grey">Ex: 4111 1234 5678 9000</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Nome no cartão</label>
				<div class="controls">
					<input type="text" id='nome'>
					<span style="color: grey">Ex: JOAO A SILVA</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Código de segurança</label>
				<div class="controls">
					<input type="text" id='codigo'>
					<span style="color: grey">Ex: 123</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Mês de expiração</label>
				<div class="controls">
					<input type="text" id='mes'>
					<span style="color: grey">Ex: 05</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Ano de expiração</label>
				<div class="controls">
					<input type="text" id='ano'>
					<span style="color: grey">Ex: 2023</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Quantidade de parcelas</label>
				<div class="controls">
					<select id='parcelas'>
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
					</select>
				</div>
			</div>
			<a class="btn btn-info" id='pagar'> Avançar </a>
		</fieldset>
	</form>
</div>
<script type="text/javascript" src="https://stc.pagseguro.uol.com.br/pagseguro/api/v2/checkout/pagseguro.directpayment.js"></script>
<script>
	jQuery(document)
			.ready(
					function() {

						$("#pagar")
								.click(
										function() {

											PagSeguroDirectPayment
													.setSessionId(jQuery(
															'#pagseguroSessionId')
															.val());

											PagSeguroDirectPayment
													.onSenderHashReady(function(
															response) {
														if (response.status == 'error') {
															erro(response,
																	'onSenderHashReady');
															return false;
														}

														var senderHash = response.senderHash;

														PagSeguroDirectPayment
																.getBrand({
																	cardBin : jQuery(
																			'#numero')
																			.val()
																			.replace(
																					/ /g,
																					""),
																	success : function(
																			response) {

																		PagSeguroDirectPayment
																				.createCardToken({
																					cardNumber : jQuery(
																							'#numero')
																							.val()
																							.replace(
																									/ /g,
																									""),
																					brand : response.brand.name,
																					cvv : jQuery(
																							'#codigo')
																							.val(),
																					expirationMonth : jQuery(
																							'#mes')
																							.val(),
																					expirationYear : jQuery(
																							'#ano')
																							.val(),
																					success : function(
																							response) {
																						window.location = '<c:url value="/pedido/pagarComCartaoDeCredito"/>?senderHash='
																								+ senderHash
																								+ '&creditCardToken='
																								+ response.card.token
																								+ '&nomeCartao='
																								+ jQuery(
																										'#nome')
																										.val()
																								+ "&parcelas="
																								+ jQuery(
																										'#parcelas')
																										.val();
																					},
																					error : function(
																							response) {
																						erro(
																								response,
																								'createCardToken');
																					},
																					complete : function(
																							response) {
																					}
																				});

																	},
																	error : function(
																			response) {
																		erro(
																				response,
																				'getBrand');
																	},
																	complete : function(
																			response) {
																	}
																});
													});
										});
					});

	function erro(response, method) {
		console.log("Error on method: " + method + ":");
		console.log(response);
		alert('Ocorreu um erro. Por favor, reveja os dados do seu cartao e tente novamente.');
	}
</script>