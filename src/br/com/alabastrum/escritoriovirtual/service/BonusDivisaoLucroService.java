package br.com.alabastrum.escritoriovirtual.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.alabastrum.escritoriovirtual.dto.ExtratoDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.ParametroVip;
import br.com.alabastrum.escritoriovirtual.modelo.Pedido;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.util.Util;

public class BonusDivisaoLucroService {

	private static final BigDecimal PORCENTAGEM_LUCRO_A_SER_DIVIDIDA = BigDecimal.valueOf(15);

	private HibernateUtil hibernateUtil;

	public BonusDivisaoLucroService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public List<ExtratoDTO> obterBonificacoesDivisaoLucro(Integer idCodigo) {

		List<ExtratoDTO> extratos = new ArrayList<ExtratoDTO>();

		List<ParametroVip> parametrosVip = hibernateUtil.buscar(new ParametroVip());
		for (ParametroVip parametroVip : parametrosVip) {

			GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(parametroVip.getData());
			GregorianCalendar ultimoDiaDoMes = Util.getUltimoDiaDoMes(parametroVip.getData());

			if (new AtividadeService(hibernateUtil).isAtivo(idCodigo, primeiroDiaDoMes, ultimoDiaDoMes)) {

				List<Pedido> pedidosDoDistribuidor = new PedidoService(hibernateUtil).getPedidosDoDistribuidor(idCodigo, primeiroDiaDoMes, ultimoDiaDoMes);

				BigDecimal totalPedidosDistribuidor = BigDecimal.ZERO;

				for (Pedido pedido : pedidosDoDistribuidor) {
					totalPedidosDistribuidor = totalPedidosDistribuidor.add(new PedidoService(hibernateUtil).calcularTotalPedidoParaBonificacao(pedido));
				}

				if (totalPedidosDistribuidor.compareTo(parametroVip.getValor()) >= 0) {

					List<Pedido> pedidosTodaRede = new PedidoService(hibernateUtil).getPedidosTodaRede(primeiroDiaDoMes, ultimoDiaDoMes);

					BigDecimal totalPedidosTodaRede = BigDecimal.ZERO;

					for (Pedido pedido : pedidosTodaRede) {
						totalPedidosTodaRede = totalPedidosTodaRede.add(new PedidoService(hibernateUtil).calcularTotalPedidoIgnorandoMaterialDeApoio(pedido));
					}

					BigDecimal lucroASerDividido = totalPedidosTodaRede.multiply(PORCENTAGEM_LUCRO_A_SER_DIVIDIDA).divide(new BigDecimal(100));

				//	extratos.add(new ExtratoDTO((Usuario) hibernateUtil.selecionar(new Usuario(pedido.getIdCodigo())), pedido.getData(), bonus, "xxxxxxxxxxxxxxxxxxx"));
				}
			}
		}

		return extratos;
	}
}
