<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>${nomeMatriz}</h4>
</div>
<br>
<c:if test="${not empty quantidadesExistentes}">
	<br>
	<div>
		<table class="table table-striped table-bordered">
			<tr>
				<td class="centralizado">
					<b>N�vel </b>
				</td>
				<td class="centralizado">1</td>
				<td class="centralizado">2</td>
				<td class="centralizado">3</td>
				<td class="centralizado">4</td>
				<td class="centralizado">5</td>
				<td class="centralizado">6</td>
				<td class="centralizado">7</td>
			</tr>
			<tr>
				<td class="centralizado">
					<b>Previsto </b>
				</td>
				<td class="centralizado">5</td>
				<td class="centralizado">25</td>
				<td class="centralizado">125</td>
				<td class="centralizado">625</td>
				<td class="centralizado">3125</td>
				<td class="centralizado">15625</td>
				<td class="centralizado">78125</td>
			</tr>
			<tr>
				<td class="centralizado">
					<b>Existente </b>
				</td>
				<c:forEach items="${quantidadesExistentes}" var="entry">
					<td class="centralizado">${entry.value}</td>
				</c:forEach>
			</tr>
		</table>
	</div>
	<br>
</c:if>
<div class="fundo-branco">
	<br>
	<a href="<c:url value="/matriz/acessarTelaMatriz${tipoDeMatriz}"/>"> Voltar ao topo </a>
	<br>
	<br>
	<p>Clique em algum distribuidor para ver detalhes de sua rede</p>
	<br>
</div>
<br>
<h6 style="margin-left: 10px;">Obs: Caso esteja visualizando pelo celular, ser� necess�rio arrastar a tela do celular para o lado porque a �rvore matriz pode ser grande.</h6>
<div class="chart" id="matriz"></div>
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
				
			<c:if test="${tipoDeMatriz == 'Trinaria'}"> parent : user${item.usuario.id_lider}, </c:if>
			<c:if test="${tipoDeMatriz == 'Multilevel'}"> parent : user${item.usuario.id_Indicante}, </c:if>	
				
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