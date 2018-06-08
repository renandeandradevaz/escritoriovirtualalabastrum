<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<br>
<br>
<br>
�ltima atualiza��o do sistema: ${ultimaAtualizacaoSistema}
<br>
<br>
Sess�es abertas hibernate: ${sessoesAbertasHibernate}
<br>
Sess�es fechadas hibernate: ${sessoesFechadasHibernate}
<br>
<br>
<h4>Total de acessos (In�cio da contagem em 07/06/2018): &nbsp;&nbsp; ${total}</h4>
<h4>M�dia di�ria: ${mediaDiaria}</h4>
<br>
<br>
<h3 style="text-align: center">Usu�rios que mais acessam</h3>
<br>
<table style="width: 100%">
	<thead>
		<tr>
			<th>Usu�rio</th>
			<th>Quantidade de acessos</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="acesso" items="${acessosUsuariosOrdenadosPorMaisAtivos}">
			<tr>
				<td style="text-align: center">${acesso.usuario.id_Codigo} - ${acesso.usuario.vNome}</td>
				<td style="text-align: center">${acesso.contagemAcessos}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
