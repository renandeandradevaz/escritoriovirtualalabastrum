<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<br>
<br>
<br>
Última atualização do sistema: ${ultimaAtualizacaoSistema}
<br>
Sessões tomcat: ${sessoesTomcat}
<br>
Sessões abertas hibernate: ${sessoesAbertasHibernate}
<br>
Sessões fechadas hibernate: ${sessoesFechadasHibernate}

<br>
<br>

<h4> Total de acessos (Início da contagem em 01/03/2015): &nbsp;&nbsp;   ${total}  </h4>

<h4> Média diária: ${mediaDiaria}  </h4>

<br>
<br>

<h3 style="text-align: center"> Usuários que mais acessam </h3>

<br>

<table style="width: 100%">

	<thead>
		<tr>
			<th> Usuário </th>
			<th> Quantidade de acessos </th>
		</tr>
	</thead>
	
	<tbody>
		<c:forEach var="acesso" items="${acessosUsuariosOrdenadosPorMaisAtivos}">
			<tr>
				<td style="text-align: center"> ${acesso.usuario.codigoFormatado} - ${acesso.usuario.vNome} </td>
				<td style="text-align: center"> ${acesso.contagemAcessos} </td>
			</tr>
		</c:forEach>
	</tbody>

</table>
