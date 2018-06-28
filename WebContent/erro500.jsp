<%@ include file="/base.jsp"%>
<style>
#mostrarException {
	font-size: 15px;
	cursor: pointer;
	font-weight: bold;
}

#exception {
	display: none;
	width: 1000px;
	height: 1000px;
}
</style>
<h3 style="color: red">&nbsp; Ocorreu um erro no servidor</h3>
<br>
<br>
<h5>Fique tranquilo. Todos os erros que ocorrem no sistema são enviados para o nosso suporte. Já estamos trabalhando na correção deste erro. Volte mais tarde e tente novamente. Obrigado.</h5>
<br>
<br>
<a id="mostrarException"> Mostrar erro </a>
<div id="exception">${exception}</div>
<script>
	jQuery("#mostrarException").click(function() {

		jQuery("#divconteudo").css("margin-right", "");
		jQuery("#mostrarException").hide();
		jQuery("#exception").show();
	});
</script>
