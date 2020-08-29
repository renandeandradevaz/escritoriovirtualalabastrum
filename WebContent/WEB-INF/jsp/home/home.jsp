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
</style>
<div class="fundo-branco">
	<div style="margin: 5%; border: 1px solid black; border-radius: 5px; padding: 10px; max-width: 300px;">
		<h3>Fila Única</h3>
		<br>
		<h5>Total abaixo: ${totalAbaixoFilaUnica}</h5>
	</div>
	<div style="margin: 5%; border: 1px solid black; border-radius: 5px; padding: 10px; max-width: 300px;" id='graduacao-mensal'>
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
			<div style="display: inline-block; width: 50%; margin-left: -20px">
				<h5>ATUAL</h5>
				<h4>${graduacaoMensal.pontuacaoDaPosicaoAtual}</h4>
				<h5>${graduacaoMensal.posicaoAtual}</h5>
			</div>
			<div style="display: inline-block; width: 50%; float: right; margin-left: 20px">
				<h5>PRÓXIMA</h5>
				<h4>${graduacaoMensal.pontuacaoDaProximaPosicao}</h4>
				<h5>${graduacaoMensal.proximaPosicao}</h5>
			</div>
		</div>
		<br>
		<br>
		<p style="text-align: left;">Pontos aproveitados: ${graduacaoMensal.pontosAproveitados}</p>
		<p style="text-align: left;">Pontos restantes para a próxima posição: ${graduacaoMensal.pontosRestantesParaProximaPosicao}</p>
		<p></p>
	</div>
	<h6>Quer indicar alguém?</h6>
	<h6>Passe o link abaixo para a pessoa que você quer indicar. Para ela poder realizar o cadastro:</h6>
	<input type="text" value="https://ev.dunastes.com.br/cadastro?nickname=${sessaoUsuario.usuario.apelido}" id="copylink">
	<br>
	<button class="btn" onclick="copiarLink()">Copiar link</button>
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