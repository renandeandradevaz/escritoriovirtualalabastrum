<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML>
<html>
<head>
<script>
	window.onload = function() {

		var options = {
			animationEnabled : true,
			theme : "light2",
			title : {
				text : "Vendas por PA (R$)"
			},
			axisX : {
				valueFormatString : "DD MMM"
			},
			axisY : {
				title : "Vendas por PA (R$)",
				suffix : "",
				minimum : 1
			},
			toolTip : {
				shared : true
			},
			legend : {
				cursor : "pointer",
				verticalAlign : "bottom",
				horizontalAlign : "left",
				dockInsidePlotArea : true,
				itemclick : toogleDataSeries
			},
			data : [ 

				<c:forEach items="${vendasPorPa}" var="pai">
				{
					type: "line",
					showInLegend: true,
					name: "${pai.franquia}",
					markerType: "square",
					xValueFormatString: "DD MMM, YYYY",
					color: "#F08080",
					yValueFormatString: "#,##0K",
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
	<div id="chartContainer" style="height: 370px; width: 100%;"></div>
	<script src="https://canvasjs.com/assets/script/jquery-1.11.1.min.js"></script>
	<script src="https://canvasjs.com/assets/script/jquery.canvasjs.min.js"></script>
</body>
</html>
