<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Relatórios</h4>
</div>
<br>
<div class="fundo-branco">
	<a href="<c:url value="/relatorioDePedidos/acessarRelatorioDePedidos"/>"> Relatório de pedidos </a>
	<br>
	<a href="<c:url value="/relatorioHistoricoDeBonus/acessarRelatorioHistoricoDeBonus"/>"> Histórico de bônus</a>
	<br>
	<a href="<c:url value="/solicitacaoSaque/solicitacoesSaqueAdministrativa"/>"> Solicitações de Saque</a>
	<br>
	<a href="<c:url value="/relatorioAtividadesRecentes/acessarRelatorioAtividadesRecentes"/>"> Atividades recentes </a>
	<br>
	<a href="<c:url value="/creditoEDebito/listarTransferenciasCreditoEDebito"/>"> Crédito e débito </a>
	<br>
	<a href="<c:url value="/saldoGeral/acessarTelaSaldoGeral"/>"> Saldo geral </a>
	<br>
	<a href="<c:url value="/relatorioResultadoOperacional/acessarRelatorioResultadoOperacional"/>"> Resultado operacional</a>
</div>
