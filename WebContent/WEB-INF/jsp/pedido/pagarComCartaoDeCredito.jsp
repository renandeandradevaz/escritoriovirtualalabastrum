<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Pagamento com cartão de crédito</h4>
</div>
<br>
<div class="fundo-branco">
	<input type="text" id="pagseguroSessionId" value="${pagseguroSessionId}" />
	<input type="text" id="numeroCartao" value="5555666677778884" />
</div>
<script type="text/javascript" src="https://stc.sandbox.pagseguro.uol.com.br/pagseguro/api/v2/checkout/pagseguro.directpayment.js"></script>
<script>    
	jQuery(document).ready(function() {
		
			PagSeguroDirectPayment.setSessionId(jQuery('#pagseguroSessionId').val());
			
			PagSeguroDirectPayment.onSenderHashReady(function(response){
			    if(response.status == 'error') {
			        console.log(response.message);
			        return false;
			    }

			    var hash = response.senderHash;
			    
			    console.log(hash);	
			    
			    PagSeguroDirectPayment.getBrand({
			    	cardBin: jQuery('#numeroCartao').val(),
			    		success: function(response) {
			    			console.log("sucesso");
			    			console.log(response.brand.name);
			    			
			    			
			    			
			    			
			    			PagSeguroDirectPayment.getInstallments({	
			    				amount: 200,
			    				brand: response.brand.name,
			    				maxInstallmentNoInterest: 2,
			    				success: function(response) {
			    					console.log(response.installments);
			    			},
			    				error: function(response) {
			    					console.log("erro: " + response);
			    				},
			    				complete: function(response) {
			    				}
			    			});
			    			
			    			
			    			
			    			
			    			
			    			
			    			PagSeguroDirectPayment.createCardToken({
			    				cardNumber: "5555666677778884",
			    				brand: response.brand.name,
			    				cvv: "123",
			    				expirationMonth: "12",
			    				expirationYear: "2022",
			    				success: function(response) {
			    					console.log("cartao de credito token");
					    			console.log(response.card);
					    		},
					    		error: function(response) {
					    			console.log("erro");
					    			console.log(response);
					    		},
					    		complete: function(response) {
					    		}
			    			});
			    			
			    		},
			    		error: function(response) {
			    			console.log("erro");
			    			console.log(response);
			    		},
			    		complete: function(response) {
			    		}
			    });
			});		
	});
</script>