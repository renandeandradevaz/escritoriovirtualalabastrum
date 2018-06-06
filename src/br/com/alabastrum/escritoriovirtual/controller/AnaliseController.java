package br.com.alabastrum.escritoriovirtual.controller;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import br.com.alabastrum.escritoriovirtual.anotacoes.Public;
import br.com.alabastrum.escritoriovirtual.dto.AnaliseDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.HistoricoAcesso;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class AnaliseController {

	private final Result result;

	public AnaliseController(Result result) {

		this.result = result;
	}

	@Public
	@Path("/analise")
	public void analisar() {

		File file = new File("/" + new File(getClass().getResource(getClass().getSimpleName() + ".class").toString().replaceAll("file:/", "")).getPath());

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		result.include("ultimaAtualizacaoSistema", sdf.format(file.lastModified()));

		HibernateUtil.gerarEstatisticas();

		result.include("sessoesAbertasHibernate", HibernateUtil.getQuantidadeSessoesAbertasHibernate());
		result.include("sessoesFechadasHibernate", HibernateUtil.getQuantidadeSessoesFechadasHibernate());

		HibernateUtil hibernateUtil = new HibernateUtil();

		@SuppressWarnings("rawtypes")
		List acessosUsuariosOrdenadosPorMaisAtivos = hibernateUtil.buscaPorHQL("SELECT codigoUsuario, count(codigoUsuario) FROM HistoricoAcesso group by codigoUsuario order by count(codigoUsuario) desc");
		List<AnaliseDTO> acessos = new ArrayList<AnaliseDTO>();

		for (Object acesso : acessosUsuariosOrdenadosPorMaisAtivos) {

			AnaliseDTO analiseDTO = new AnaliseDTO();

			Object[] acessoArray = (Object[]) acesso;

			analiseDTO.setCodigoUsuario((Integer) acessoArray[0]);
			analiseDTO.setContagemAcessos((Long) acessoArray[1]);

			acessos.add(analiseDTO);
		}
		result.include("acessosUsuariosOrdenadosPorMaisAtivos", acessos);

		Integer total = hibernateUtil.contar(new HistoricoAcesso());
		result.include("total", total);

		HistoricoAcesso historicoAcesso = new HistoricoAcesso();
		historicoAcesso.setId(1);
		historicoAcesso = hibernateUtil.selecionar(historicoAcesso);

		DateTime primeiroAcesso = new DateTime(historicoAcesso.getDataHora());
		DateTime hoje = new DateTime();

		int quantidadeDias = Days.daysBetween(primeiroAcesso, hoje).getDays();

		result.include("mediaDiaria", BigDecimal.valueOf(total).divide(BigDecimal.valueOf(quantidadeDias), 2, BigDecimal.ROUND_HALF_UP));

		hibernateUtil.fecharSessao();
	}
}
