<%@ include file="/base.jsp" %> 

<style>

#mostrarException{

	font-size: 20px;
	cursor: pointer;
	font-weight: bold;
}

#exception{

	display: none;
	width: 1000px;
	height: 1000px;
}

</style>


<h3 style="color: red"> &nbsp;  Ocorreu um erro no servidor </h3>


<br><br><br><br><br><br><br><br>

<a id="mostrarException">  Mostrar erro   </a>

<div id="exception" >
	${exception}
</div>


<script>

	jQuery("#mostrarException").click(function() {
		
		jQuery("#divconteudo").css("margin-right", "");
		jQuery("#mostrarException").hide();
		jQuery("#exception").show();
	});

</script>
