<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML>
<html>
<head>
<script>
	window.onload = function() {

		var options = {
				
				animationEnabled: true,
				theme: "light2",
				title:{
					text: "Vendas por PA (R$)"
				},
				axisX:{
					valueFormatString: "DD MMM"
				},
				axisY: {
					title: "Vendas por PA (R$)",
					valueFormatString: "#0",
					includeZero: false,
					suffix: "",
					prefix: "R$ ",
					minimum: 10
				},
				legend: {
					cursor: "pointer",
					itemclick: toogleDataSeries
				},
				toolTip: {
					shared: true
			    },
			    data : [ 

				<c:forEach items="${vendasPorPa}" var="pai">
				{
					type: "line",
					showInLegend: true,
					name: "${pai.franquia}",
					markerType: "square",
					xValueFormatString: "DD MMM, YYYY",
					dataPoints: [
						<c:forEach items="${pai.filhos}" var="filho">
						   { x: new Date(${filho.ano}, ${filho.mes}, ${filho.dia}), y: ${filho.valor} },
						</c:forEach>
						]
				},
			    </c:forEach>

			 ]
		};
		$("#chartContainer").CanvasJSChart(options);

		function toogleDataSeries(e) {
			if (typeof (e.dataSeries.visible) === "undefined"
					|| e.dataSeries.visible) {
				e.dataSeries.visible = false;
			} else {
				e.dataSeries.visible = true;
			}
			e.chart.render();
		}

	}
</script>
</head>
<body>
	<br>
	<br>
	<br>
	<a href="javascript:history.back()">Voltar</a>
	<br>
	<br>
	<br>
	<div id="chartContainer" style="height: 370px; width: 100%;"></div>
	<script src="https://canvasjs.com/assets/script/jquery-1.11.1.min.js"></script>
	<script src="https://canvasjs.com/assets/script/jquery.canvasjs.min.js"></script>
</body>
</html>
