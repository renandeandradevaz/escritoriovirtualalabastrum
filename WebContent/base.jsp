<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML>
<html lang="pt-br">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Dunastes</title>
<link rel="SHORTCUT ICON" href="<c:url value="/css/images/favico.png"/>" type="image/x-icon">
<link type="text/css" href="<c:url value="/css/bootstrap.css"/>" rel="stylesheet" />
<link type="text/css" href="<c:url value="/css/bootstrap-alterado.css"/>" rel="stylesheet" />
<link type="text/css" href="<c:url value="/css/bootstrap-responsive.css"/>" rel="stylesheet" />
<link type="text/css" href="<c:url value="/css/estilo.css?ver=5"/>" rel="stylesheet" />
<link type="text/css" href="<c:url value="/css/jquery-ui-1.9.2.custom.min.css"/>" rel="stylesheet" />
<link type="text/css" href="<c:url value="/css/menu-accordion.css"/>" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="/js/jquery-1.8.3.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery-ui-1.9.2.custom.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.collapse.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.collapse_storage.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.collapse_cookie_storage.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/my-scripts.js"/>"></script>
</head>
<body>
	<div class="topo">
		<p>${sessaoUsuario.usuario.apelido}</p>
	</div>
	<c:if test="${sessaoUsuario.usuario.apelido != null && sessaoUsuario.usuario.id_Codigo != null}">
		<a id="sair" style="float: right; padding-right: 15px; font-weight: bold; font-size: 12px; cursor: pointer;" href="https://dunastes.com.br"> Sair </a>
		<div class="menu-hamburguer"></div>
		<div class="menu" data-collapse="persist">
			<h3 class="menu-accordion" onclick="goTo('<c:url value="/home"/>')">Início</h3>
			<div></div>
			<h3 class="menu-accordion" onclick="goTo('<c:url value="/atualizacaoDados/acessarTelaAtualizacaoDados"/>')">Dados Cadastrais</h3>
			<div></div>
			<c:if test="${sessaoUsuario.usuario.nome_kit == 'distribuidor'}">
				<h3 class="menu-accordion has-child">Redes</h3>
				<div>
					<a href="<c:url value="/equipe/acessarTelaEquipe"/>" class="submenu-accordion"> Equipe </a>
					<a href="<c:url value="/matriz/acessarTelaMatrizMultilevel"/>" class="submenu-accordion"> Matriz Multilevel</a>
					<a href="<c:url value="/matriz/acessarTelaMatrizTrinaria"/>" class="submenu-accordion"> Matriz 5x7 </a>
					<a href="<c:url value="/pontosDeEquipe/acessarTelaPontosDeEquipe"/>" class="submenu-accordion"> Pontos de Equipe</a>
				</div>
			</c:if>
			<h3 class="menu-accordion has-child">Pedidos</h3>
			<div>
				<a href="<c:url value="/pedido/acessarTelaNovoPedido"/>" class="submenu-accordion"> Produtos </a>
				<a href="<c:url value="/pedido/acessarCarrinho"/>" class="submenu-accordion"> Meu Carrinho </a>
				<a href="<c:url value="/pedido/meusPedidos"/>" class="submenu-accordion"> Meus Pedidos </a>
				<c:if test="${sessaoUsuario.usuario.donoDeFranquia}">
					<a href="<c:url value="/pedido/pesquisarPedidosDosDistribuidores"/>" class="submenu-accordion"> Pedidos da franquia </a>
					<a href="<c:url value="/pedidoFranquia/pedidosFranquia"/>" class="submenu-accordion"> Pedidos para Estoque </a>
				</c:if>
			</div>
			<c:if test="${sessaoUsuario.usuario.nome_kit == 'distribuidor'}">
				<h3 class="menu-accordion has-child">Financeiro</h3>
				<div>
					<a href="<c:url value="/extrato/acessarTelaExtrato"/>" class="submenu-accordion"> Extrato </a>
					<a href="<c:url value="/solicitacaoSaque/acessarTelaSolicitacaoSaque"/>" class="submenu-accordion"> Solicitar Saque </a>
				</div>
			</c:if>
			<h3 class="menu-accordion has-child">Downloads</h3>
			<div>
				<a href="<c:url value="/download/downloads"/>" class="submenu-accordion"> Downloads </a>
			</div>
			<c:if test="${sessaoUsuario.usuario.informacoesFixasUsuario.administrador}">
				<h3 class="menu-accordion has-child">Administrativo</h3>
				<div>
					<a href="<c:url value="/assumirIdentidade/acessarTelaAssumirIdentidade"/>" class="submenu-accordion"> Assumir Identidade </a>
					<a href="<c:url value="/pedidoFranquia/todosPedidosFranquia"/>" class="submenu-accordion"> Gerenciar Pedidos para Estoque </a>
					<a href="<c:url value="/configuracao/acessarTelaConfiguracaoAdministrativa"/>" class="submenu-accordion"> Configurações Administrativas </a>
					<a href="<c:url value="/relatorios/acessarTelaRelatorios"/>" class="submenu-accordion"> Relatórios </a>
				</div>
			</c:if>
		</div>
	</c:if>
	<script>
		function goTo(url) {
			window.location.href = url;
		}

		jQuery(".menu-hamburguer").click(function() {
			jQuery(this).hide();
			jQuery(".menu").show();
		});
	</script>
	<div class="conteudo">
		<c:if test="${not empty sucesso}">
			<div class="alert alert-success">${sucesso}</div>
		</c:if>
		<c:if test="${not empty alerta}">
			<div class="alert alert-alerta">${alerta.message}</div>
		</c:if>
		<c:if test="${not empty errors}">
			<div class="alert alert-error">
				<c:forEach items="${errors }" var="error">
					<strong>${error.category }</strong> - ${error.message } <br>
				</c:forEach>
			</div>
		</c:if>