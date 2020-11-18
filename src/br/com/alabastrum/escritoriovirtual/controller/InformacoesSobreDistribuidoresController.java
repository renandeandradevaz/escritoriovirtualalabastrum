package br.com.alabastrum.escritoriovirtual.controller;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.alabastrum.escritoriovirtual.anotacoes.Funcionalidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;
import br.com.alabastrum.escritoriovirtual.service.AtividadeService;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class InformacoesSobreDistribuidoresController {

    private Result result;
    private HibernateUtil hibernateUtil;

    public InformacoesSobreDistribuidoresController(Result result, HibernateUtil hibernateUtil) {

	this.result = result;
	this.hibernateUtil = hibernateUtil;
    }

    @Funcionalidade(administrativa = "true")
    public void acessarInformacoesSobreDistribuidores() {

	List<Usuario> usuarios = this.hibernateUtil.buscar(new Usuario());
	int totalDeUsuarios = usuarios.size();

	int ativos = 0;
	int inativos = 0;
	int homens = 0;
	int mulheres = 0;
	int descontaInss = 0;
	int naoDescontaInss = 0;
	int entre18E25Anos = 0;
	int entre26E35Anos = 0;
	int entre36E45Anos = 0;
	int entre46E55Anos = 0;
	int entre56E65Anos = 0;
	int acimaDe66Anos = 0;
	int solteiro = 0;
	int casado = 0;
	int divorciado = 0;
	int viuvo = 0;
	Map<String, Integer> usuariosPorUf = new HashMap<String, Integer>();

	for (Usuario usuario : usuarios) {

	    if (new AtividadeService(hibernateUtil).isAtivo(usuario.getId_Codigo()))
		ativos++;
	    else
		inativos++;

	    if ("Feminino".equalsIgnoreCase(usuario.getCadSexo()))
		mulheres++;
	    else if ("Masculino".equalsIgnoreCase(usuario.getCadSexo()))
		homens++;

	    if (Integer.valueOf(1).equals(usuario.getDescontaInss()))
		descontaInss++;
	    else
		naoDescontaInss++;

	    if (usuario.getDt_Nasc().split("/").length == 3) {

		Integer anoCadastro = Integer.valueOf(usuario.getDt_Nasc().split("/")[2]);
		Integer anoAtual = new GregorianCalendar().get(Calendar.YEAR);
		int idade = anoAtual - anoCadastro;

		if (idade < 26)
		    entre18E25Anos++;
		else if (idade < 36)
		    entre26E35Anos++;
		else if (idade < 46)
		    entre36E45Anos++;
		else if (idade < 56)
		    entre46E55Anos++;
		else if (idade < 66)
		    entre56E65Anos++;
		else
		    acimaDe66Anos++;
	    }

	    if ("Solteiro(a)".equalsIgnoreCase(usuario.getCadEstCivil()))
		solteiro++;
	    if ("Casado(a)".equalsIgnoreCase(usuario.getCadEstCivil()))
		casado++;
	    if ("Divorciado(a)".equalsIgnoreCase(usuario.getCadEstCivil()))
		divorciado++;
	    if ("Viúvo(a)".equalsIgnoreCase(usuario.getCadEstCivil()))
		viuvo++;

	    if (usuariosPorUf.get(usuario.getCadUF()) == null)
		usuariosPorUf.put(usuario.getCadUF(), 1);
	    else
		usuariosPorUf.put(usuario.getCadUF(), usuariosPorUf.get(usuario.getCadUF()) + 1);
	}

	double ativosPorcentagem = (double) ativos / totalDeUsuarios * 100;
	double inativosPorcentagem = (double) inativos / (totalDeUsuarios) * 100;

	double homensPorcentagem = (double) homens / totalDeUsuarios * 100;
	double mulheresPorcentagem = (double) mulheres / totalDeUsuarios * 100;

	double descontaInssPorcentagem = (double) descontaInss / totalDeUsuarios * 100;
	double naoDescontaInssPorcentagem = (double) naoDescontaInss / (totalDeUsuarios) * 100;

	double entre18E25AnosPorcentagem = (double) entre18E25Anos / totalDeUsuarios * 100;
	double entre26E35AnosPorcentagem = (double) entre26E35Anos / totalDeUsuarios * 100;
	double entre36E45AnosPorcentagem = (double) entre36E45Anos / totalDeUsuarios * 100;
	double entre46E55AnosPorcentagem = (double) entre46E55Anos / totalDeUsuarios * 100;
	double entre56E65AnosPorcentagem = (double) entre56E65Anos / totalDeUsuarios * 100;
	double acimaDe66AnosPorcentagem = (double) acimaDe66Anos / totalDeUsuarios * 100;

	double solteiroPorcentagem = (double) solteiro / totalDeUsuarios * 100;
	double casadoPorcentagem = (double) casado / totalDeUsuarios * 100;
	double divorciadoPorcentagem = (double) divorciado / totalDeUsuarios * 100;
	double viuvoPorcentagem = (double) viuvo / totalDeUsuarios * 100;

	Map<String, String> usuariosPorUfResultado = new TreeMap<String, String>();

	for (Entry<String, Integer> usuarioEntry : usuariosPorUf.entrySet()) {
	    usuariosPorUfResultado.put(usuarioEntry.getKey(), usuarioEntry.getValue() + " - " + (new DecimalFormat("#.##").format((double) usuarioEntry.getValue() / totalDeUsuarios * 100)) + "%");
	}

	result.include("totalDeUsuarios", totalDeUsuarios);
	result.include("ativos", ativos);
	result.include("inativos", inativos);
	result.include("homens", homens);
	result.include("mulheres", mulheres);
	result.include("descontaInss", descontaInss);
	result.include("naoDescontaInss", naoDescontaInss);
	result.include("entre18E25Anos", entre18E25Anos);
	result.include("entre26E35Anos", entre26E35Anos);
	result.include("entre36E45Anos", entre36E45Anos);
	result.include("entre46E55Anos", entre46E55Anos);
	result.include("entre56E65Anos", entre56E65Anos);
	result.include("acimaDe66Anos", acimaDe66Anos);
	result.include("solteiro", solteiro);
	result.include("casado", casado);
	result.include("divorciado", divorciado);
	result.include("viuvo", viuvo);
	result.include("ativosPorcentagem", ativosPorcentagem);
	result.include("inativosPorcentagem", inativosPorcentagem);
	result.include("homensPorcentagem", homensPorcentagem);
	result.include("mulheresPorcentagem", mulheresPorcentagem);
	result.include("descontaInssPorcentagem", descontaInssPorcentagem);
	result.include("naoDescontaInssPorcentagem", naoDescontaInssPorcentagem);
	result.include("entre18E25AnosPorcentagem", entre18E25AnosPorcentagem);
	result.include("entre26E35AnosPorcentagem", entre26E35AnosPorcentagem);
	result.include("entre36E45AnosPorcentagem", entre36E45AnosPorcentagem);
	result.include("entre46E55AnosPorcentagem", entre46E55AnosPorcentagem);
	result.include("entre56E65AnosPorcentagem", entre56E65AnosPorcentagem);
	result.include("acimaDe66AnosPorcentagem", acimaDe66AnosPorcentagem);
	result.include("solteiroPorcentagem", solteiroPorcentagem);
	result.include("casadoPorcentagem", casadoPorcentagem);
	result.include("divorciadoPorcentagem", divorciadoPorcentagem);
	result.include("viuvoPorcentagem", viuvoPorcentagem);
	result.include("usuariosPorUfResultado", usuariosPorUfResultado);
    }
}
