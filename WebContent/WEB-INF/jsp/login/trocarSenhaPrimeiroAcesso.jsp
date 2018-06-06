<%@ include file="/base.jsp" %> 

<form class="form-horizontal trocaSenhaPrimeiroAcesso" action="<c:url value="/login/salvarTrocarSenhaPrimeiroAcesso"/>" method="post">
  <fieldset>
    <p class="tituloTrocarSenha" > TROQUE SUA SENHA </p>
    
    <div class="control-group warning" style="margin-top: 20px;" >
    	<label class="control-label labelTrocarSenha">Nova senha</label>
    	<div class="controls">
    		<input type="password" class="input-xlarge inputTelaPrimeiroAcesso" name="senhaNova"  >
		</div>
    </div>
    
    <div class="control-group warning" style="margin-top: 45px;" >
    	<label class="control-label labelTrocarSenha">Confirmação</label>
    	<div class="controls">
    		<input type="password" class="input-xlarge inputTelaPrimeiroAcesso" name="confirmacaoSenhaNova"  >
		</div>
    </div>
    
    <div class="control-group warning" style="margin-top: 45px;" >
    	<label class="control-label labelTrocarSenha">CPF</label>
    	<div class="controls">
    		<input type="text" class="input-xlarge inputTelaPrimeiroAcesso numero-inteiro" name="cpf"  >
		</div>
    </div>
    
    <div class="control-group warning" style="margin-top: 50px;" >
    	<label class="control-label labelTrocarSenha">E-mail</label>
    	<div class="controls">
    		<input type="email" class="input-xlarge inputTelaPrimeiroAcesso" name="email"  >
		</div>
    </div> 
    
    <div style="margin-top: 40px; margin-left: 20px;" >
	    <button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Salvar</button>
	    <a class="btn btn-danger" href="<c:url value="/"/>" > Cancelar </a>
    </div>
            
  </fieldset>
</form>