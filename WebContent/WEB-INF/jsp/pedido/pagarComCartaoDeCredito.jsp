<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Pagamento com cart�o de cr�dito</h4>
</div>
<br>
<div class="fundo-branco">
	<form>
		<fieldset>
			<legend>Cart�o de cr�dito</legend>
			<input type="hidden" id="pagseguroSessionId" value="${pagseguroSessionId}" />
			<div class="control-group">
				<label class="control-label">N�mero do cart�o</label>
				<div class="controls">
					<input type="text" id='numero'>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Nome no cart�o</label>
				<div class="controls">
					<input type="text" id='nome'>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">C�digo de seguran�a</label>
				<div class="controls">
					<input type="text" id='codigo'>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">M�s de expira��o</label>
				<div class="controls">
					<input type="text" id='mes'>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Ano de expira��o</label>
				<div class="controls">
					<input type="text" id='ano'>
				</div>
			</div>
			<a class="btn btn-info" id='pagar'> Avan�ar </a>
		</fieldset>
	</form>
</div>
<script type="text/javascript" src="https://stc.sandbox.pagseguro.uol.com.br/pagseguro/api/v2/checkout/pagseguro.directpayment.js"></script>

<script>    
	jQuery(document).ready(function() {
		
		$( "#pagar" ).click(function() {
				
			PagSeguroDirectPayment.setSessionId(jQuery('#pagseguroSessionId').val());
			
			PagSeguroDirectPayment.onSenderHashReady(function(response){
			    if(response.status == 'error') {
			        erro(response, 'onSenderHashReady');
			        return false;
			    }

			    var senderHash = response.senderHash;
			    
			    PagSeguroDirectPayment.getBrand({
			    	cardBin: jQuery('#numero').val(),
			    		success: function(response) {			    			

			    			PagSeguroDirectPayment.createCardToken({
			    				cardNumber: jQuery('#numero').val(),
			    				brand: response.brand.name,
			    				cvv: jQuery('#codigo').val(),
			    				expirationMonth: jQuery('#mes').val(),
			    				expirationYear: jQuery('#ano').val(),
			    				success: function(response) {
					    			window.location = '<c:url value="/pedido/pagarComCartaoDeCredito"/>?senderHash=' + senderHash + '&creditCardToken=' + response.card.token + '&nomeCartao=' + jQuery('#nome').val() ;
					    		},
					    		error: function(response) {
					    			erro(response, 'createCardToken');
					    		},
					    		complete: function(response) {
					    		}
			    			});
			    			
			    		},
			    		error: function(response) {
			    			erro(response, 'getBrand');
			    		},
			    		complete: function(response) {
			    		}
			    });
			});	
		});
	});
	
	function erro(response, method){
		console.log("Error on method: " + method + ":");
		console.log(response);
		alert('Ocorreu um erro. Por favor, tente novamente.');
	}
</script>
