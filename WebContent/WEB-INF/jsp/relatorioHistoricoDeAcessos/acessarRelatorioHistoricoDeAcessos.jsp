<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML>
<html lang="pt-br">
<body>
	<div id="chartContainer" style="height: 370px; max-width: 920px; margin: 0px auto;"></div>
	<script src="https://canvasjs.com/assets/script/jquery-1.11.1.min.js"></script>
	<script src="https://canvasjs.com/assets/script/jquery.canvasjs.min.js"></script>
	<br>
	<br>
	<br>
	<a href="javascript:history.back()">Voltar</a>
	<br>
</body>
<script>
	window.onload = function() {

		var options = {
			animationEnabled : true,
			title : {
				text : "Histórico de acessos (Último ano)"
			},
			axisX : {
				valueFormatString : "MMM"
			},
			axisY : {
				title : "Acessos",
				prefix : ""
			},
			data : [ {
				yValueFormatString : "$#.###",
				xValueFormatString : "MMMM",
				type : "spline",
				dataPoints : [
					
					<c:forEach items="${datas}" var="item">
					{
						x : new Date(${item.ano}, ${item.mes}),
						y : ${item.quantidade}
					},
				</c:forEach>
					
					]
			} ]
		};
		$("#chartContainer").CanvasJSChart(options);

	}
</script>
</html>
