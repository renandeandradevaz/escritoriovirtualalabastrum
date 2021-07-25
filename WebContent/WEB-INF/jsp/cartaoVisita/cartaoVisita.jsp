<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML>
<html lang="pt-br">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Título aqui</title>
<link type="text/css" href="<c:url value="/css/estilo-qualquer.css"/>" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="/js/jquery-1.8.3.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/qrcode.min.js"/>"></script>
</head>
<body>
	<div>
		<c:if test="${empty cartaoVisita}">
			<form action="<c:url value="/cartao-visita/salvarCartaoVisita"/>" method="post">
				<input type="hidden" name="cartaoVisita.codigo" value="${codigo}" />
				<label>Nome</label>
				<br>
				<input type="text" name="cartaoVisita.nome">
				<br>
				<br>
				<label>Telefone / Whatsapp / Telegram (DDD + NUMERO) </label>
				<br>
				<input type="text" name="cartaoVisita.telefone">
				<br>
				<br>
				<label>Email </label>
				<br>
				<input type="text" name="cartaoVisita.email">
				<br>
				<br>
				<label>Site </label>
				<br>
				<input type="text" name="cartaoVisita.site">
				<br>
				<br>
				<label>Facebook </label>
				<br>
				<input type="text" name="cartaoVisita.facebook">
				<br>
				<br>
				<label>Instagram </label>
				<br>
				<input type="text" name="cartaoVisita.instagram">
				<br>
				<br>
				<label>Twitter </label>
				<br>
				<input type="text" name="cartaoVisita.twitter">
				<br>
				<br>
				<label>Tiktok </label>
				<br>
				<input type="text" name="cartaoVisita.tiktok">
				<br>
				<br>
				<label>Link de cadastro </label>
				<br>
				<input type="text" name="cartaoVisita.linkCadastro">
				<br>
				<br>
				<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Salvar</button>
			</form>
		</c:if>
		<c:if test="${not empty cartaoVisita}">
			<br>
			<br>
			<br>
			<input id="qrcodeurl" type="hidden" value="http://dntconnection.com.br/cartao-visita/${cartaoVisita.codigo}" />
			<div id="qrcodebox" style="width: 300px"></div>
			<br>
			<br>
			<br>
			<h3>${cartaoVisita.nome}</h3>
			<br>
			<h5>Telefone / Whatsapp / Telegram: ${cartaoVisita.telefone}</h5>
			<br>
			<h5>Email: ${cartaoVisita.email}</h5>
			<br>
			<a href="//${cartaoVisita.site}"> Site pessoal</a>
			<br>
			<a href="//${cartaoVisita.facebook}"> Facebook</a>
			<br>
			<a href="//${cartaoVisita.instagram}"> Instagram</a>
			<br>
			<a href="//${cartaoVisita.twitter}"> Twitter</a>
			<br>
			<a href="//${cartaoVisita.tiktok}"> Tiktok</a>
			<br>
			<a href="//${cartaoVisita.linkCadastro}"> Link de cadastro</a>
			<br>
			<br>
			<br>
		</c:if>
	</div>
	<script type="text/javascript">
		var qrcode = new QRCode(document.getElementById("qrcodebox"), {
			width : 300,
			height : 300
		});

		function makeCode() {
			var elText = document.getElementById("qrcodeurl");
			qrcode.makeCode(elText.value);
		}

		makeCode();
	</script>
</body>
</html>
