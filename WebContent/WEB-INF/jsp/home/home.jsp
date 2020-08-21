<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<div style="margin: 5%; border: 1px solid black; border-radius: 5px; padding: 10px">
		<h3>Fila única</h3>
		<br>
		<h5>Total abaixo: ${totalAbaixoFilaUnica}</h5>
	</div>
	<br>
	<br>
	<h6>Quer indicar alguém?</h6>
	<h6>Passe o link abaixo para a pessoa que você quer indicar. Para ela poder realizar o cadastro:</h6>
	<input type="text" value="https://ev.dunastes.com.br/cadastro?nickname=${sessaoUsuario.usuario.apelido}" id="copylink">
	<br>
	<button class="btn" onclick="copiarLink()">Copiar link</button>
	<script>
		function copiarLink() {
			var copyText = document.getElementById("copylink");
			copyText.select();
			copyText.setSelectionRange(0, 99999)
			document.execCommand("copy");
		}
	</script>
</div>