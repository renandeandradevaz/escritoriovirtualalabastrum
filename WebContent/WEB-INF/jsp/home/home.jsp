<%@ include file="/base.jsp"%>
<style>
#svg circle {
	stroke-dashoffset: 0;
	transition: stroke-dashoffset 1s linear;
	stroke: #666;
	stroke-width: 1em;
}

#svg #bar {
	stroke: #FF9F1E;
}

#cont {
	display: block;
	height: 200px;
	width: 200px;
	margin: 2em auto;
	box-shadow: 0 0 1em black;
	border-radius: 100%;
	position: relative;
	text-align: center;
}

#cont:after {
	position: absolute;
	display: block;
	height: 160px;
	width: 160px;
	left: 50%;
	top: 50%;
	box-shadow: inset 0 0 1em black;
	content: attr(data-pct) "%";
	margin-top: -80px;
	margin-left: -80px;
	border-radius: 100%;
	line-height: 160px;
	font-size: 2em;
	text-shadow: 0 0 0.5em black;
}

#graduacao-mensal {
	text-align: center;
}

h3 {
	text-align: center;
}

.caixinha-home {
	margin: 3%;
	border: 1px solid black;
	border-radius: 5px;
	padding: 10px;
	max-width: 500px;
}

.table td {
	padding: 2px;
	font-size: 9px;
}

@media only screen and (max-width: 1200px) {
	.banner-home {
		width: 95%;
	}
}
</style>
<div class="fundo-branco">
	<br>
	<img class="banner-home" style="max-width: 500px; margin: 3%;" src="<c:url value="/download/banner-home.jpg"/>">
	<br>
	<div class='caixinha-home' style="text-align: center;">
		<h3>Reconhecimento</h3>
		<br>
		<img style="width: 250px; display: block; margin: 0 auto;" src="<c:url value="/css/images/${sessaoUsuario.usuario.posAtual}.png"/> ">
		<div style="display: inline-block; text-align: center;">
			<div style="display: inline-block; width: 70%; margin-left: -50px">
				<h5>Diretos</h5>
				<h4>${diretos}</h4>
			</div>
			<div style="display: inline-block; width: 30%; float: right; margin-left: 50px">
				<h5>Equipe</h5>
				<h4>${equipe}</h4>
			</div>
		</div>
	</div>
	<div class='caixinha-home' id='graduacao-mensal'>
		<h3>Graduação Mensal</h3>
		<br>
		<div id="cont" data-pct="100">
			<svg id="svg" width="200" height="200" viewPort="0 0 100 100" version="1.1" xmlns="http://www.w3.org/2000/svg">
  				<circle r="90" cx="100" cy="100" fill="transparent" stroke-dasharray="565.48" stroke-dashoffset="0"></circle>
 				<circle id="bar" r="90" cx="100" cy="100" fill="transparent" stroke-dasharray="565.48" stroke-dashoffset="0"></circle>
			</svg>
		</div>
		<input type="hidden" id="percent" name="percent" value="${graduacaoMensal.porcentagemConclusao}">
		<div style="display: inline-block;">
			<div style="display: inline-block; width: 70%; margin-left: -50px">
				<h5>ATUAL</h5>
				<h4>${graduacaoMensal.pontuacaoDaPosicaoAtual}</h4>
				<h5>${graduacaoMensal.posicaoAtual}</h5>
			</div>
			<div style="display: inline-block; width: 30%; float: right; margin-left: 50px">
				<h5>PRÓXIMA</h5>
				<h4>${graduacaoMensal.pontuacaoDaProximaPosicao}</h4>
				<h5>${graduacaoMensal.proximaPosicao}</h5>
			</div>
		</div>
		<br>
		<br>
		<p style="text-align: left;">
			Pontuação geral da equipe:
			<span style="font-weight: bold;">${graduacaoMensal.pontuacaoTotal} </span>
		</p>
		<p style="text-align: left;">
			Pontos aproveitados:
			<span style="font-weight: bold; color: green">${graduacaoMensal.pontosAproveitados} </span>
		</p>
		<p style="text-align: left;">
			Pontos restantes para a próxima posição:
			<span style="font-weight: bold; color: red">${graduacaoMensal.pontosRestantesParaProximaPosicao} </span>
		</p>
		<p></p>
	</div>
	<div class='caixinha-home'>
		<h3>Equipe</h3>
		<br>
		<table>
			<tr>
				<td style="width: 100px">
					<h4>ATIVOS:</h4>
				</td>
				<td style="width: 100px">
					<h4>${ativos}</h4>
				</td>
			</tr>
			<tr>
				<td style="width: 100px">
					<h4>INATIVOS:</h4>
				</td>
				<td style="width: 100px">
					<h4>${inativos}</h4>
				</td>
			</tr>
		</table>
	</div>
	<c:if test="${not empty quantidadesExistentes}">
		<div class='caixinha-home'>
			<h3>Trinário</h3>
			<br>
			<div>
				<table class="table table-striped table-bordered">
					<tr>
						<td class="centralizado">
							<b>Nível </b>
						</td>
						<td class="centralizado">1</td>
						<td class="centralizado">2</td>
						<td class="centralizado">3</td>
						<td class="centralizado">4</td>
						<td class="centralizado">5</td>
						<td class="centralizado">6</td>
						<td class="centralizado">7</td>
						<td class="centralizado">8</td>
						<td class="centralizado">9</td>
						<td class="centralizado">10</td>
					</tr>
					<tr>
						<td class="centralizado">
							<b>Previsto </b>
						</td>
						<td class="centralizado">3</td>
						<td class="centralizado">9</td>
						<td class="centralizado">27</td>
						<td class="centralizado">81</td>
						<td class="centralizado">243</td>
						<td class="centralizado">729</td>
						<td class="centralizado">2187</td>
						<td class="centralizado">6561</td>
						<td class="centralizado">19683</td>
						<td class="centralizado">59049</td>
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
		</div>
	</c:if>
	<div class='caixinha-home'>
		<h3>Fila Única</h3>
		<br>
		<h5>Total abaixo: ${totalAbaixoFilaUnica}</h5>
	</div>
	<div class='caixinha-home'>
		<br>
		<a href="http://miguelprado.com.br">CLIQUE AQUI PARA ENVIAR SUA DOCUMENTAÇÃO</a>
		<br>
		<br>
	</div>
	<div class='caixinha-home'>
		<h6>Quer indicar alguém?</h6>
		<h6>Passe o link abaixo para a pessoa que você quer indicar. Para ela poder realizar o cadastro:</h6>
		<input type="text" value="https://ev.dunastes.com.br/cadastro?nickname=${sessaoUsuario.usuario.apelido}" id="copylink">
		<br>
		<button class="btn" onclick="copiarLink()">Copiar link</button>
	</div>
	<script>
		$(document).ready(function() {
			var val = parseInt($('#percent').val());
			var $circle = $('#svg #bar');

			if (isNaN(val)) {
				val = 100;
			} else {
				var r = $circle.attr('r');
				var c = Math.PI * (r * 2);

				if (val < 0) {
					val = 0;
				}
				if (val > 100) {
					val = 100;
				}

				var pct = ((100 - val) / 100) * c;

				$circle.css({
					strokeDashoffset : pct
				});

				$('#cont').attr('data-pct', val);
			}
		});

		function copiarLink() {
			var copyText = document.getElementById("copylink");
			copyText.select();
			copyText.setSelectionRange(0, 99999)
			document.execCommand("copy");
		}
	</script>
</div>