<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML>
<html lang="pt-br">
<head>
<meta charset="utf-8">
<!-- <meta name="viewport" content="width=device-width, initial-scale=1.0"> -->
<title>DNT Connection</title>
<script type="text/javascript" src="<c:url value="/js/jquery-1.8.3.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/qrcode.min.js"/>"></script>
<c:if test="${empty cartaoVisita}">
	<style>
@import
	url('https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300&display=swap')
	;

* {
	padding: 0px;
	border: 0px;
	margin: 0px;
	box-sizing: border-box;
	font-family: "Poppins", sans-serif;
}

body {
	background: linear-gradient(45deg, #FBC02D, #FFF9C4);
	background-repeat: no-repeat;
	min-height: 100vh;
	min-width: 90vw;
	display: flex;
	align-items: center;
	justify-content: center;
	overflow-x: hidden;
}

/* Inicio - ajuste para celula de resolução 320 a 768 */
@media only screen and (min-device-width: 320px) and (max-device-width:
	768px) {
	/*container celular */
	.container {
		background: #fff;
		min-width: 90%;
		min-height: 40vh;
		padding: 2rem;
		box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.5);
		border-radius: 8px;
		margin: 35px 0px;
	}

	/* logo Dunastes Celular */
	.logo {
		position: relative;
		width: 55%;
		left: 50%;
		margin-left: -230px;
		margin-bottom: 50px;
		margin-top: 90px;
	}

	/* Texto do container Celular */
	.container h1 {
		font-size: 55px;
		font-weight: 400;
		text-align: center;
		margin-bottom: 40px;
	}
	.input-field input[type="text"] {
		padding: 35px 25px;
		margin-bottom: 15px;
		font-size: 250%;
		border-radius: 3px;
		width: 99%;
		color: #000;
		border: 1px solid rgba(0, 0, 0, 0.6);
	}
	.submit {
		background: linear-gradient(to right, #1e3c72, #2a5298);
		padding: 50px 30px;
		width: 99%;
		border-radius: 7px;
		color: #fff;
		margin-top: 40px;
		margin-bottom: 40px;
		cursor: pointer;
		font-family: tahoma;
		font-size: 3rem;
	}
	.modelo {
		display: none;
	}
} /* fim - ajuste para celula de resolução 320 a 768 */

/*   Ipad PRo*/
@media only screen and (min-device-width: 1025px) and (max-device-width:
	1366px) {
	* { /* display: none; */
		
	}
}

/*desktop*/
/* Inidio - juste da largura do container quando redimensionado ao minimo */
@media ( max-width : 767px) {
	.container {
		min-width: 400px;
	}
}
/* fim - juste da largura do container quando redimensionado ao minimo */

/*  Inicio - tamanho do titulo do form  */
@media only screen and (min-device-width: 768px) and (max-device-width:
	1024px) {
	.container h1 {
		font-size: 50px;
		text-align: center;
	}
} /*  Fim - tamanho do titulo do form  */

/* ************** */
@media only screen and (min-device-width: 1025px) and (max-device-width:
	2000px) {
	.container {
		background: #fff;
		width: 40vw;
		min-height: 40vh;
		padding: 2rem;
		box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.5);
		border-radius: 8px;
		margin: 35px 0px;
	}
	.logo {
		width: 40%;
		position: relative;
		left: 50%;
		margin-left: -20%;
		margin-bottom: 10px;
	}
	.container h1 {
		font-size: 20px;
		text-align: center;
		padding: 20px;
	}
	.input-field input[type="text"] {
		padding: 12px 20px;
		margin-bottom: 7px;
		border: 1px solid rgba(0, 0, 0, 0.2);
		border-radius: 3px;
		width: 99%;
		font-size: 14px;
	}
	.submit {
		background: linear-gradient(to right, #1e3c72, #2a5298);
		padding: 15px 30px;
		width: 99%;
		border-radius: 3px;
		color: #fff;
		margin-top: 20px;
		cursor: pointer;
		font-size: 1rem;
	}
	.modelo { /*imagemdo casal fixa no final da pagina */
		/*position: absolute; bottom:-230px; right: 60px; width: 55%; opacity: 0.2; z-index:-10;"*/
		position: fixed;
		bottom: -30px;
		right: 60px;
		width: 55%;
		opacity: 0.2;
		z-index: -10;
	}
}

/* escondendo imagem do casal para resolucao menor */
@media only screen and (max-width: 900px) {
	.modelo { /*imagemdo casal*/
		display: none;
	}
}
/* Fim ajuste desktop */
</style>
</c:if>
<c:if test="${not empty cartaoVisita}">
	<style>
@import
	url('https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300&display=swap')
	;

* {
	padding: 0px;
	border: 0px;
	margin: 0px;
	box-sizing: border-box;
	font-family: "Poppins", sans-serif;
}

body {
	background: linear-gradient(45deg, #FBC02D, #FFF9C4);
	background-repeat: no-repeat;
	min-height: 100vh;
	min-width: 70vw;
	display: flex;
	align-items: center;
	justify-content: center;
	overflow-x: hidden;
	/*border: 3px solid red;*/
}

/************************************* Inicio - ajuste para celula de resolução 320 a 768 */
@media only screen and (min-device-width: 320px) and (max-device-width:
	768px) {
	body {
		background: linear-gradient(45deg, #FBC02D, #FFF9C4);
		background-repeat: no-repeat;
		min-height: 0vh;
		min-width: 70vw;
		display: flex;
		align-items: center;
		justify-content: center;
		overflow-x: hidden;
	}

	/*****************************container celular */
	.container {
		/*border: 0px solid #fff;*/
		background: #fff;
		min-width: 90%;
		min-height: 40vh;
		padding: 2rem; */
		box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.5); */
		/*border-radius: 8px;*/
		margin: 0px 0px;
		background: linear-gradient(to bottom, #FBC02D, #FFF9C4, #fff, #fff, #fff, #fff,
			#fff, #fff);
	}
	.compartilhar {
		position: relative;
		right: -820px;
		padding-top: 15px;
		top: 20px;
		width: 2rem; /*border: 1px solid cyan; */
		width: 5em;
	}
	/*.compartilhar{ position: relative; right: -850px; top: 40px; width: 50px;}*/
	.foto-usuario {
		position: relative;
		top: 20px;
		margin: 0 auto;
		/*display: flex;  
		justify-content: center;
		*/
		width: 21em;
		height: 21em;
		/*overflow:hidden;*/
		/*border-radius: 200px;*/
		/*border: 1px solid red;*/
	}
	.foto-usuario img {
		width: 100%;
		margin: 0 auto;
		display: flex;
		justify-content: center;
		/* 		border-radius: 200px; */
		border: 10px solid #fff;
		box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.2);
	}
	.Nome-tel p {
		position: relative;
		top: 20px;
		text-align: center;
		color: bloack;
		font-size: 38pt;
		font-weight: 700;
	}
	.Nome-tel .p2 {
		position: relative;
		top: 15px;
		font-size: 52px;
		font-weight: 400;
		text-align: center;
		cursor: pointer;
	}
	.Nome-tel .p3 {
		position: relative;
		top: 15px;
		font-size: 40px;
		margin-bottom: 25px;
		font-weight: 400;
		text-align: center;
	}
	.Nome-tel .p4 {
		position: relative;
		top: 15px;
		margin-bottom: 25px;
		font-size: 45px;
		font-weight: 400;
		text-align: center;
	}

	/************************** logo Dunastes Celular */
	.logo {
		position: relative;
		width: 55%;
		left: 50%;
		margin-left: -230px;
		margin-bottom: 50px;
		margin-top: 90px;
	}
	/********************icones redes celular */
	.box-icones {
		display: flex;
		justify-content: center;
		flex-wrap: wrap;
		/*flex-direction: column;*/
		align-items: center;
		/*border: 1px solid red;*/
		box-sizing: border-box;
	}
	.box-icones .icones {
		flex: 0 1 19%;
		width: 8em;
		/* padding: 14px 35px;*/
		margin: 25px;
		box-sizing: border-box;
	}
	.logoDNT {
		/*border:1px solid red;*/
		padding: 0px 170px 30px 170px;
		display: table;
		/*border-bottom: 1px solid rgba(0,0,0,0.3);*/
		width: 150%;
	}

	/* icones da DNT celular comum*/
	.box-icones2 {
		display: flex;
		justify-content: center;
		flex-wrap: wrap;
		/*flex-direction: column;*/
		align-items: center;
		/*border: 1px solid red;*/
		padding: 30px;
		margin-top: 20px;
		min-device-width: 200px;
		box-sizing: border-box;
	}
	.icones2 {
		/*border: 1px solid red;*/
		flex: 0 1 14%;
		width: 6em;
		padding: 5px;
		margin: 10px 10px;
	}
	.salvar {
		margin: 10px auto 120px auto;
		width: 24rem;
		text-align: center;
		border: 1px solid rgba(0, 0, 0, 0.2);
		font-weight: 500;
		border-radius: 3px;
		background-color: black;
		color: #fff;
		font-size: 3em;
		cursor: pointer;
	}
	/* fim icones da DNT celular comum */

	/* *********************************************************** */

	/************* Texto do container Celular ********************/
	.container h1 {
		font-size: 55px;
		font-weight: 400;
		text-align: center;
		margin-bottom: 40px;
	}

	/************icones celular *********************/
	.container form .box-icones  .input-field input {
		display: none;
	}
	.icones {
		flex: 0 1 4%;
		width: 18em;
		padding: 10px 25px;
	}
	/*fim icones celular */
	.input-field input[type="text"] {
		padding: 35px 25px;
		margin-bottom: 15px;
		font-size: 250%;
		border-radius: 3px;
		width: 99%;
		color: #000;
		border: 1px solid rgba(0, 0, 0, 0.6);
	}
	.submit {
		background: linear-gradient(to right, #1e3c72, #2a5298);
		padding: 50px 30px;
		width: 99%;
		border-radius: 7px;
		color: #fff;
		margin-top: 40px;
		margin-bottom: 40px;
		cursor: pointer;
		font-family: tahoma;
		font-size: 3rem;
	}
	.modelo {
		display: none;
	}
}
/************ fim - ajuste para celula de resolução 320 a 768 ***************/

/**************   Ipad PRo**************/
/*@media only screen and (min-device-width: 1025px) and (max-device-width: 1366px) {*/
/*@media only screen and (min-width: 1025px) and (max-width: 1366px) {
		*{/*border:1px solid red;*/
}
.container .compartilhar {
	position: relative;
	width: 2rem;
	right: -390px;
}

}
*
/

/*desktop*/
/********* Inidio - juste da largura do container quando redimensionado ao minimo ****************/















































		














































@media ( max-width : 767px) {
	.container {
		/*border: 1px solid red;*/
		min-width: 300px;
	}
}
/******************** fim - juste da largura do container quando redimensionado ao minimo ****************/

/*******************  Inicio - tamanho do titulo do form  ******************/
@media only screen and (min-device-width: 768px) and (max-device-width:
	1024px) {
}

@media only screen and (min-device-width: 1025px) and (max-device-width:
	2880px) {
	* { /*border: 1px solid red;*/
		
	}
	.container {
		/*border: 4px solid pink;*/
		width: 40vw;
	}
	.container .foto-usuario {
		width: 200px;
		height: 200px;
		/*border: 1px solid red;*/
		background: black;
	}
	.container h1 {
		font-size: 50px;
		text-align: center;
		box-sizing: border-box;
	}
}
/***********************  Fim - tamanho do titulo do form  *******************/

/*.container .compartilhar{ position: relative; right: -380px; top: 10px; width: 2rem; border: 1px solid red;}*/

/*icones redes desktop */
.box-icones {
	/* border: 1px solid red;  */
	display: flex;
	justify-content: center;
	flex-wrap: wrap;
	/*flex-direction: column;*/
	align-items: center;
	border-top: 1px solid rgba(0, 0, 0, 0.2);
	padding: 20px 2px 2px 2px;
	margin-top: 20px;
	min-device-width: 200px;
	box-sizing: border-box;
	background: url(DNT-diamante.png);
	background-repeat: no-repeat;
	background-position: center center;
}

#i {
	/*border: 1px solid rgba(0,0,0,0.1);*/
	width: 90%;
	margin: 0 auto;
	/*background-color: #fafafa;*/
}

.icones {
	/*border: 1px solid red;  */
	flex: 0 1 14%;
	width: 4rem;
	padding: 5px;
	margin: 5px 10px;
}

/* icones da DNT desktop 1*/
.box-icones2 {
	display: flex;
	justify-content: center;
	flex-wrap: wrap;
	/*flex-direction: column;*/
	align-items: center;
	border-top: 1px solid #f4f4f4;
	/*border: 1px solid red; */
	padding: 30px;
	margin-top: 20px;
	min-device-width: 200px;
	box-sizing: border-box;
}

.container .icones2 {
	/*border: 1px solid red;*/
	flex: 0 1 14%;
	width: 6em;
	padding: 5px;
	margin: 5px 5px;
}
/* fim icones da DNT desktop 1*/

/* ********************************************************************************** */
/* ipad */
/* ipad */
/* ********************************************************************************** */
@media only screen and (min-device-width: 1025px) and (max-device-width:
	2000px) {
	.containerCor {
		position: absolute;
		top: 0px;
		z-index: -10000;
		width: 100%;
		height: 30%;
		background-image: url(gold.jpg);
		background-repeat: no-repeat;
		background-position: 0px -250px;
		background-size: 100%;

		/*background: rgba(0,0,0,0.7);*/
	}
	.container {
		/*width: 30vw;*/
		width: 30rem;
		min-height: 40vh;
		padding: 2rem;
		box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.5);
		border-radius: 8px;
		margin: 40px auto;
		/*border: 3px solid red;*/
		background: linear-gradient(to bottom, #FBC02D, #FFF9C4, #fff, #fff, #fff, #fff,
			#fff, #fff);
	}
	.compartilhar {
		position: relative;
		right: -380px;
		top: 10px;
		width: 2rem; /*border: 1px solid pink;*/
	}
	.foto-usuario {
		position: relative;
		top: -30px;
		margin: 0 auto;
		display: flex;
		justify-content: center;
		width: 9rem;
		height: 9rem;
		/* 		border-radius: 150px; */
		border: 5px solid #fff;
		overflow: hidden;
		box-shadow: 0px 0px 9px #888;
	}
	.Nome-tel p {
		position: relative;
		top: -19px;
		text-align: center;
		color: bloack;
		font-size: 16pt;
		font-weight: 700;
	}
	.Nome-tel .p2 {
		position: relative;
		top: -15px;
		font-size: 20px;
		font-weight: 400;
		text-align: center;
		cursor: pointer;
	}
	.Nome-tel .p3 {
		position: relative;
		top: -15px;
		font-size: 14px;
		font-weight: 400;
		text-align: center;
	}
	.Nome-tel .p4 {
		position: relative;
		margin-bottom: 15px;
		font-size: 18px;
		font-weight: 400;
		text-align: center;
	}
	.logo {
		width: 40%;
		position: relative;
		left: 50%;
		margin-left: -20%;
		margin-bottom: 10px;
	}
	.container h1 {
		font-size: 20px;
		text-align: center;
		padding: 20px;
	}
	.box-icones {
		/* border: 1px solid red;  */
		display: flex;
		justify-content: center;
		flex-wrap: wrap;
		/*flex-direction: column;*/
		align-items: center;
		border-top: 1px solid rgba(0, 0, 0, 0.2);
		padding: 20px 2px 2px 2px;
		margin-top: 20px;
		min-device-width: 200px;
		box-sizing: border-box;
		background: url(DNT-diamante.png);
		background-repeat: no-repeat;
		background-position: center center;
		background-size: 90%;
	}

	/* icones da DNT desktop2*/
	.box-icones2 {
		display: flex;
		justify-content: center;
		flex-wrap: wrap;
		/*flex-direction: column;*/
		align-items: center;
		border-top: 1px solid rgba(0, 0, 0, 0.1);
		/*padding: 20px 20px;*/
		margin-top: 20px;
		min-device-width: 200px;
		box-sizing: border-box;
		/*border: 1px solid red;*/
	}
	.logoDNT {
		/*border:1px solid red; */
		padding: 0px 70px 30px 70px;
		display: table;
		/*border-bottom: 1px solid rgba(0,0,0,0.3);*/
	}
	.container .icones2 {
		/*border: 1px solid red; */
		flex: 1 1 14%;
		width: 2.5em;
		padding: 3px;
		margin: 5px 3px;
	}
	/* fim icones da DNT desktop2 */
	.salvar {
		margin: 10px auto 30px;
		width: 12rem;
		text-align: center;
		border: 1px solid rgba(0, 0, 0, 0.2);
		font-weight: 500;
		border-radius: 3px;
		background-color: black;
		color: #fff;
		cursor: pointer;
	}
	.modelo { /*imagemdo casal fixa no final da pagina */
		/*position: absolute; bottom:-230px; right: 60px; width: 55%; opacity: 0.2; z-index:-10;"*/
		position: fixed;
		bottom: -30px;
		right: 60px;
		width: 55%;
		opacity: 0.2;
		z-index: -10;
		"
	}
}

/* escondendo imagem do casal para resolucao menor */
@media only screen and (max-width: 900px) {
	.modelo { /*imagemdo casal*/
		display: none;
	}
}
/* Fim ajuste desktop */
</style>
</c:if>
</head>
<body>
	<c:if test="${empty cartaoVisita}">
		<div class="container">
			<img class="logo" src="<c:url value="/css/images/Dunastes_logo.png"/>" />
			<h1>Informe seus dados</h1>
			<form action="<c:url value="/cartao-visita/salvarCartaoVisita"/>" method="post" enctype="multipart/form-data">
				<input type="hidden" name="cartaoVisita.codigo" value="${codigo}" />
				<div class="input-field">
					<p>Selecione uma foto:</p>
					<input type="file" name="foto">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.nome" placeholder="Seu nome...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.celular" placeholder="Celular com DDD...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.telefoneFixo" placeholder="Tel Fixo com DDD...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.email" placeholder="Email...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.enderecoComercial" placeholder="Endere�o comercial...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.cargo" placeholder="Seu Cargo...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.frase" placeholder="Frase de Status...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.pix" placeholder="Seu Pix (CPF, Telefone, etc)">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.catalogo" placeholder="Cat�logo Virtual">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.whatsapp" placeholder="Seu WhatsApp com DDD..">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.telegram" placeholder="Username do telegram">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.site" placeholder="Seu site: https:// ...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.facebook" placeholder="Seu Facebook: https:// ...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.instagram" placeholder="Seu Instagram: https:// ...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.twitter" placeholder="Seu Twitter: https:// ...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.youtube" placeholder="Seu Youtube: https:// ...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.tiktok" placeholder="Seu Tik Tok: https:// ...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.linkedin" placeholder="Seu Linkedin: https:// ...">
				</div>
				<div class="input-field">
					<input type="text" name="cartaoVisita.linkCadastro" placeholder="Link de Cadastro Dunastes: https:// ...">
				</div>
				<input type="submit" class="submit" value="Cadastrar" onclick="this.disabled=true;this.form.submit();">
			</form>
		</div>
	</c:if>
	<c:if test="${not empty cartaoVisita}">
		<div class="containerCor"></div>
		<div class="container">
			<a href="whatsapp://send?text=http://dntconnection.com.br/cartao-visita/${cartaoVisita.codigo}" data-action="share/whatsapp/share" target="_blank">
				<img class="compartilhar" src="<c:url value="/css/images/compartilhar.png"/>"></img>
			</a>
			<div class="foto-usuario">
				<input id="qrcodeurl" type="hidden" value="http://dntconnection.com.br/cartao-visita/${cartaoVisita.codigo}" />
				<div id="qrcodebox" style="width: 300px"></div>
			</div>
			<div class="Nome-tel">
				<p>
					<img style="width: 5%; position: relative; top: 7px; margin-right: 10px" src="<c:url value="/css/images/user.png"/>" />${cartaoVisita.nome}</p>
				<h3 class="p3">${cartaoVisita.cargo}</h3>
				<h3 class="p2">
					<a href="tel:${cartaoVisita.celular}">
						<img style="width: 4%; position: relative; top: 2px; margin-right: 10px;" src="<c:url value="/css/images/ligar.png"/>" />
						${cartaoVisita.celular}
					</a>
				</h3>
				<h3 class="p3">Email: ${cartaoVisita.email}</h3>
				<%-- 				<h3 class="p3">Pix: ${cartaoVisita.pix}</h3> --%>
				<h3 class="p4">${cartaoVisita.frase}</h3>
			</div>
			<div id="i" class="box-icones">
				<a href="//${cartaoVisita.site}">
					<img class="icones" src="<c:url value="/css/images/site.png"/>" />
				</a>
				<a href="tel:${cartaoVisita.celular}">
					<img class="icones" src="<c:url value="/css/images/tel.png"/>" />
				</a>
				<a href="https://www.google.com/search?q=${cartaoVisita.enderecoComercial}">
					<img class="icones" src="<c:url value="/css/images/endereco2.png"/>" />
				</a>
				<a href="https://api.whatsapp.com/send?phone=55${cartaoVisita.whatsapp}">
					<img class="icones" src="<c:url value="/css/images/whatsapp.png"/>" />
				</a>
				<a href="https://telegram.me/${cartaoVisita.telegram}">
					<img class="icones" src="<c:url value="/css/images/telegram2.png"/>" />
				</a>
				<a href="//${cartaoVisita.facebook}">
					<img class="icones" src="<c:url value="/css/images/facebook.png"/>" />
				</a>
				<a href="//${cartaoVisita.instagram}">
					<img class="icones" src="<c:url value="/css/images/instagram.png"/>" />
				</a>
				<a href="//${cartaoVisita.twitter}">
					<img class="icones" src="<c:url value="/css/images/twitter.png"/>" />
				</a>
				<a href="//${cartaoVisita.youtube}">
					<img class="icones" src="<c:url value="/css/images/youtube.png"/>" />
				</a>
				<a href="//${cartaoVisita.tiktok}">
					<img class="icones" src="<c:url value="/css/images/tiktok.png"/>" />
				</a>
				<a href="//${cartaoVisita.linkedin}">
					<img class="icones" src="<c:url value="/css/images/linkedin.jpg"/>" />
				</a>
				<a href="//${cartaoVisita.catalogo}">
					<img class="icones" src="<c:url value="/css/images/catalogo.png"/>" />
				</a>
				<a href="//${cartaoVisita.linkCadastro}">
					<img class="icones" src="<c:url value="/css/images/DNT-diamante1.png"/>" />
				</a>
			</div>
			<div id="i" class="box-icones2">
				<img class="logoDNT" src="<c:url value="/css/images/Dunastes_logo.png"/>" />
				<a href="https://dunastes.com.br/home">
					<img class="icones2" src="<c:url value="/css/images/site_1.png"/>"></img>
				</a>
				<a href="https://api.whatsapp.com/send?phone=552132830966">
					<img class="icones2" src="<c:url value="/css/images/whatsapp_1.png"/>"></img>
				</a>
				<a href="https://www.instagram.com/dunastesoficial">
					<img class="icones2" src="<c:url value="/css/images/instagram_1.png"/>"></img>
				</a>
				<a href="https://twitter.com/DunastesOficial">
					<img class="icones2" src="<c:url value="/css/images/twitter_1.png"/>"></img>
				</a>
				<a href="https://www.tiktok.com/@dunastesoficial?">
					<img class="icones2" src="<c:url value="/css/images/tiktok_2.png"/>"></img>
				</a>
				<a href="https://www.facebook.com/dnt.dunastes">
					<img class="icones2" src="<c:url value="/css/images/FACE_1.png"/>"></img>
				</a>
				<a href="https://www.youtube.com/c/DunastesOficial">
					<img class="icones2" src="<c:url value="/css/images/youtube_1.png"/>"></img>
				</a>
				<a href="https://www.youtube.com/c/AlessandroSchimanski">
					<img class="icones2" src="<c:url value="/css/images/youtube_1.png"/>"></img>
				</a>
			</div>
			<a download="download" href="<c:url value="/cartao-visita/downloadVcard/${cartaoVisita.codigo}"/>">
				<h3 class="salvar">Salvar contato no telefone</h3>
			</a>
		</div>
	</c:if>
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
	<img class="modelo" src="<c:url value="/css/images/tema1.png"/>" />
</body>
</html>
