<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Dunastes</title>
<link type="text/css" href="css/login.css" rel="stylesheet" />
</head>
<body>
	<c:if test="${not empty errors}">
		<div class="error">
			<c:forEach items="${errors }" var="error">
				<strong>${error.category }</strong> - ${error.message } <br>
			</c:forEach>
		</div>
	</c:if>
	<div id="corpo-form">
		<img src="css/images/logo.png">
		<h1>Escritório Virtual</h1>
		<form method="post" action="<c:url value="/login/efetuarLogin"/>">
			<input type="text" placeholder="NICKNAME" name="usuario.apelido">
			<input type="password" placeholder="SENHA" name="usuario.informacoesFixasUsuario.senha">
			<br />
			<input type="submit" value="ACESSAR" onclick="this.disabled=true;this.form.submit();">
			<a href="<c:url value="/atualizacaoDados/acessarTelaCadastro"/>">
				Ainda não é inscrito?
				<br />
				<strong>Cadastre-se</strong>
			</a>
			<br>
			<a href="<c:url value="/login/esqueciMinhaSenha"/>">
				<strong>Esqueci minha senha</strong>
			</a>
		</form>
	</div>
</body>
</html>