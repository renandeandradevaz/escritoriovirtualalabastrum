<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<br>
<br>
<br>
Ultima atualizcao do sistema: ${ultimaAtualizacaoSistema}
<br>
<br>
Sessoes abertas hibernate: ${sessoesAbertasHibernate}
<br>
Sessoes fechadas hibernate: ${sessoesFechadasHibernate}
<br>
<br>
<h4>Total de acessos: ${total}</h4>
<h4>Media diaria: ${mediaDiaria}</h4>
<br>
<br>
<h3 style="text-align: center">Usuarios que mais acessam</h3>
<br>
<table style="width: 100%">
	<thead>
		<tr>
			<th>Usuario</th>
			<th>Quantidade de acessos</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="acesso" items="${acessosUsuariosOrdenadosPorMaisAtivos}">
			<tr>
				<td style="text-align: center">${acesso.usuario.id_Codigo}- ${acesso.usuario.vNome}</td>
				<td style="text-align: center">${acesso.contagemAcessos}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
