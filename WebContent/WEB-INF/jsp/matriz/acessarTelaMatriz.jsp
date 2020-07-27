<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>${nomeMatriz}</h4>
</div>
<br>
<div class="fundo-branco">
	<p>Clique em algum distribuidor para ver detalhes de sua rede</p>
	<br>
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
			title: '${lider.apelido}'
		},
	};
	chart_config.push(user${lider.id_Codigo});
	
	<c:forEach items="${arvoreHierarquica}" var="item">	
		var user${item.usuario.id_Codigo} = {
			parent : user${item.usuario.id_lider},
			text : {
				name : ${item.usuario.id_Codigo},
				title: '${item.usuario.apelido}'
			},
			link: {
	            href: "<c:url value="/matriz/acessarTelaMatriz${tipoDeMatriz}?codigo=${item.usuario.id_Codigo}"/>"
	        }
		};
		chart_config.push(user${item.usuario.id_Codigo});
	</c:forEach>

	new Treant(chart_config);
</script>