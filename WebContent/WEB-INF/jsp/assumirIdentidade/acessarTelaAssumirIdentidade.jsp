<%@ include file="/base.jsp" %> 

<form action="<c:url value="/assumirIdentidade/assumirIdentidade"/>" method="post" >
	<fieldset>
    	<legend>Assumir identidade</legend>	

		<div class="control-group warning">
	    	<label class="control-label">Código</label>
	    	<div class="controls">
	    		<input type="text" class="numero-inteiro" name="codigo"/>
			</div>
	    </div>

		<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();" >Assumir identidade</button>
		<a class="btn btn-danger" href="<c:url value="/home/home"/>" > Cancelar </a>
		
	</fieldset>
</form>

