package br.com.alabastrum.escritoriovirtual.controller;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Franquia;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class PedidoController {

	private Result result;
	private HibernateUtil hibernateUtil;
	private SessaoUsuario sessaoUsuario;

	public PedidoController(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario) {

		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.sessaoUsuario = sessaoUsuario;
	}

	@Funcionalidade
	public void acessarTelaNovoPedido() {

		result.include("franquias", hibernateUtil.buscar(new Franquia()));
	}

	@Funcionalidade
	public void escolherProdutos(Integer idFranquia) {

		Pedido pedido = new Pedido();
		pedido.setIdCodigo(sessaoUsuario.getUsuario().getId_Codigo());
		pedido.setCompleted(false);
		pedido = hibernateUtil.selecionar(pedido);

		if (Util.vazio(pedido)) {
			pedido = new Pedido();
			pedido.setIdCodigo(sessaoUsuario.getUsuario().getId_Codigo());
			pedido.setCompleted(false);
		}

		pedido.setIdFranquia(idFranquia);
		hibernateUtil.salvarOuAtualizar(pedido);
	}
}
