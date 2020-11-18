<%@ include file="/base.jsp"%>
<style>
.caixinha {
	margin: 3%;
	border: 1px solid black;
	border-radius: 5px;
	padding: 10px;
	max-width: 1000px;
}
</style>
<div class="fundo-branco">
	<div class='caixinha' style="text-align: center;">
		<h2>TOTAL DE DISTRIBUIDORES</h2>
		<h2>${totalDeUsuarios}</h2>
	</div>
	<div class='caixinha' style="text-align: center;">
		<h2>STATUS</h2>
		<br>
		<h4>
			Ativos: ${ativos} -
			<fmt:formatNumber value="${ativosPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
		<h4>
			Inativos: ${inativos} -
			<fmt:formatNumber value="${inativosPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
	</div>
	<div class='caixinha' style="text-align: center;">
		<h2>SEXO</h2>
		<br>
		<h4>
			Masculino: ${homens} -
			<fmt:formatNumber value="${homensPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
		<h4>
			Feminino: ${mulheres} -
			<fmt:formatNumber value="${mulheresPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
	</div>
	<div class='caixinha' style="text-align: center;">
		<h2>DESCONTA INSS?</h2>
		<br>
		<h4>
			Sim: ${descontaInss} -
			<fmt:formatNumber value="${descontaInssPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
		<h4>
			Não: ${naoDescontaInss} -
			<fmt:formatNumber value="${naoDescontaInssPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
	</div>
	<div class='caixinha' style="text-align: center;">
		<h2>FAIXA ETÁRIA</h2>
		<br>
		<h4>
			Entre 18 e 25 anos: ${entre18E25Anos} -
			<fmt:formatNumber value="${entre18E25AnosPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
		<h4>
			Entre 26 e 35 anos: ${entre26E35Anos} -
			<fmt:formatNumber value="${entre26E35AnosPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
		<h4>
			Entre 36 e 45 anos: ${entre36E45Anos} -
			<fmt:formatNumber value="${entre36E45AnosPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
		<h4>
			Entre 46 e 55 anos: ${entre46E55Anos} -
			<fmt:formatNumber value="${entre46E55AnosPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
		<h4>
			Entre 56 e 65 anos: ${entre56E65Anos} -
			<fmt:formatNumber value="${entre56E65AnosPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
		<h4>
			Acima de 66 anos: ${acimaDe66Anos} -
			<fmt:formatNumber value="${acimaDe66AnosPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
	</div>
	<div class='caixinha' style="text-align: center;">
		<h2>ESTADO CIVIL</h2>
		<br>
		<h4>
			Solteiro(a): ${solteiro} -
			<fmt:formatNumber value="${solteiroPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
		<h4>
			Casado(a): ${casado} -
			<fmt:formatNumber value="${casadoPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
		<h4>
			Divorciado(a): ${divorciado} -
			<fmt:formatNumber value="${divorciadoPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
		<h4>
			Viúvo(a): ${viuvo} -
			<fmt:formatNumber value="${viuvoPorcentagem}" pattern="#,##0.00" />
			%
		</h4>
	</div>
	<div class='caixinha' style="text-align: center;">
		<h2>ESTADO</h2>
		<br>
		<c:forEach items="${usuariosPorUfResultado}" var="entry">
			<h4>${entry.key}:${entry.value}</h4>
		</c:forEach>
	</div>
</div>
