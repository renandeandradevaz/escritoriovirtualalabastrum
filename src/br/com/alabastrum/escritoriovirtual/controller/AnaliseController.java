package br.com.alabastrum.escritoriovirtual.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.Days;

import br.com.alabastrum.escritoriovirtual.anotacoes.Public;
import br.com.alabastrum.escritoriovirtual.dto.AnaliseDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.DadosFake;
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

	public static void main(String args[]) throws Exception {

		BufferedReader br = new BufferedReader(new FileReader(new File("/home/renan/Downloads/sp.cepaberto_parte_1/sp.cepaberto_parte_1.csv")));
		String line;
		while ((line = br.readLine()) != null) {
			
			if(line.split(",").length == 5) {
				
				String cep = line.split(",")[0];
				String rua = line.split(",")[1];
				String bairro = line.split(",")[2];
				
				if (cep != null && rua != null && bairro != null && !cep.equals("") && !rua.equals("") && !bairro.equals("")) {
					;
					int numero = new Random().nextInt(1000 - 100) + 100;
					
					String telefone = "989" + (new Random().nextInt(999999 - 100000) + 100000);
					
					if (telefone.length() == 9) {
						
						if(!bairro.toLowerCase().contains("sé") && !bairro.toLowerCase().contains("rua")) {
							
							GregorianCalendar dataNascimento = new GregorianCalendar();
							
							int year = randBetween(1960, 2000);
							
							dataNascimento.set(dataNascimento.YEAR, year);
							
							int dayOfYear = randBetween(1, dataNascimento.getActualMaximum(dataNascimento.DAY_OF_YEAR));
							
							dataNascimento.set(dataNascimento.DAY_OF_YEAR, dayOfYear);
							
							DadosFake df = new DadosFake();
							df.setBairro(bairro);
							df.setCep(cep);
							df.setDataNascimento(dataNascimento.getTime());
							df.setNumero(String.valueOf(numero));
							df.setRua(rua);
							df.setTelefone(telefone);
							
							HibernateUtil hu = new HibernateUtil();
							hu.salvarOuAtualizar(df);
							hu.fecharSessao();
						}
						
					}
			}

			}
		}
	}

	public static int randBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
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
