<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Matriz</h4>
</div>
<br>
<div class="fundo-branco">
	<p>Clique em algum distribuidor para ver detalhes de sua rede</p>
	<br>
	<a href="<c:url value="/matriz/acessarTelaMatriz"/>"> Voltar ao topo </a>
	<a class="btn btn-basic" style="float: right;" href="<c:url value="/configuracao/acessarTelaConfiguracao"/>"> Direcionamento de novos cadastros </a>
	<div class="chart" id="matriz"></div>
</div>
<link type="text/css" href="<c:url value="/css/treant.css"/>" rel="stylesheet" />
<link type="text/css" href="<c:url value="/css/treeview.css"/>" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="/js/raphael.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/treant.js"/>"></script>
<script>

	var chart_config = [];
	
	var config = {
		container : "#matriz",
		connectors : {
			type : 'step'
		},
		node : {
			HTMLclass : 'nodeExample1'
		}
	};
	chart_config.push(config);
	
	var user${lider.id_Codigo} = {
		text : {
			name : ${lider.id_Codigo},
			title: '${lider.vNome}'
		},
	};
	chart_config.push(user${lider.id_Codigo});
	
	<c:forEach items="${arvoreHierarquica}" var="item">	
		var user${item.usuario.id_Codigo} = {
			parent : user${item.usuario.id_lider},
			text : {
				name : ${item.usuario.id_Codigo},
				title: '${item.usuario.vNome}'
			},
			link: {
	            href: "<c:url value="/matriz/acessarTelaMatriz?codigo=${item.usuario.id_Codigo}"/>"
	        }
		};
		chart_config.push(user${item.usuario.id_Codigo});
	</c:forEach>

	new Treant(chart_config);
</script>