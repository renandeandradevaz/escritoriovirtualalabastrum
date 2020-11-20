<%@ include file="/base.jsp"%>
<div class="fundo-branco">
	<h4>Vendas por PA</h4>
</div>
<br>
<div class="fundo-branco">
	<c:if test="${pesquisa}">
		<button class="btn btn-basic" onclick="$('#form-pesquisa').show()">Exibir filtros de pesquisa</button>
	</c:if>
	<form id="form-pesquisa" action="<c:url value="/relatorioVendasPorPA/pesquisarVendasPorPA"/>" method="post" <c:if test="${pesquisa}"> style="display:none"  </c:if>>
		<fieldset>
			<legend>Pesquisa</legend>
			<div class="control-group">
				<label class="control-label">Data inicial</label>
				<div class="controls">
					<input type="text" name="pesquisaRelatorioVendasPorPADTO.dataInicial" value="${pesquisaRelatorioVendasPorPADTO.dataInicial}" placeholder="03/11/2020" class="calendario" autocomplete="off" required="required">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Data final</label>
				<div class="controls">
					<input type="text" name="pesquisaRelatorioVendasPorPADTO.dataFinal" value="${pesquisaRelatorioVendasPorPADTO.dataFinal}" placeholder="25/04/2021" class="calendario" autocomplete="off" required="required">
				</div>
			</div>
			<div class="control-group" style="border: 1px solid black; padding: 10px; border-radius: 5px; max-width: 400px">
				<label class="control-label">Selecione as franquias</label>
				<div class="controls">
					<c:forEach items="${franquias}" var="item">
						<input type="checkbox" name="pesquisaRelatorioVendasPorPADTO.idsFranquias['${item.id_Estoque}']" />
						<span> ${item.estqNome} </span>
						<br>
					</c:forEach>
				</div>
			</div>
			<br>
			<button type="submit" class="btn btn-primary" onclick="this.disabled=true;this.form.submit();">Pesquisar</button>
		</fieldset>
	</form>
</div>
