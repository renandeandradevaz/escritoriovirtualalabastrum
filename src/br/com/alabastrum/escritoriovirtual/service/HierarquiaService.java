package br.com.alabastrum.escritoriovirtual.service;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class HierarquiaService {

	private HibernateUtil hibernateUtil;

	public HierarquiaService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public TreeMap<Integer, ArvoreHierarquicaDTO> obterArvoreHierarquicaAteNivelEspecifico(Integer codigoUsuario, Integer nivel) {

		TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaTodosOsNiveis = obterArvoreHierarquicaTodosOsNiveis(codigoUsuario);

		TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaAteNivelEspecifico = new TreeMap<Integer, ArvoreHierarquicaDTO>();

		for (Entry<Integer, ArvoreHierarquicaDTO> usuarioEntry : arvoreHierarquicaTodosOsNiveis.entrySet()) {

			if (usuarioEntry.getValue().getNivel() <= nivel) {
				arvoreHierarquicaAteNivelEspecifico.put(usuarioEntry.getKey(), usuarioEntry.getValue());
			}
		}

		return arvoreHierarquicaAteNivelEspecifico;
	}

	public TreeMap<Integer, ArvoreHierarquicaDTO> obterArvoreHierarquicaTodosOsNiveis(Integer codigoUsuario) {

		TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquica = new TreeMap<Integer, ArvoreHierarquicaDTO>();
		pesquisarComRecursividade(codigoUsuario, arvoreHierarquica, 1);
		return arvoreHierarquica;
	}

	public List<Integer> obterArvoreHierarquicaAscendente(Integer idCodigo, GregorianCalendar data) {

		List<Integer> arvoreHierarquicaAscendente = new ArrayList<Integer>();
		arvoreHierarquicaAscendente.add(idCodigo);

		montarArvoreHierarquicaAscendenteComRecursividade(arvoreHierarquicaAscendente, idCodigo, data);

		return arvoreHierarquicaAscendente;
	}

	private void montarArvoreHierarquicaAscendenteComRecursividade(List<Integer> arvoreHierarquicaAscendente, Integer idCodigo, GregorianCalendar data) {

		Usuario usuario = hibernateUtil.selecionar(new Usuario(idCodigo));
		Usuario lider = hibernateUtil.selecionar(new Usuario(usuario.getId_lider()));

		if (usuario.getId_Codigo().equals(lider.getId_Codigo())) {
			return;
		}

		arvoreHierarquicaAscendente.add(lider.getId_Codigo());

		montarArvoreHierarquicaAscendenteComRecursividade(arvoreHierarquicaAscendente, lider.getId_Codigo(), data);
	}

	private void pesquisarComRecursividade(Integer codigo, TreeMap<Integer, ArvoreHierarquicaDTO> arvoreHierarquica, Integer nivel) {

		Usuario filtro = new Usuario();
		filtro.setId_Indicante(codigo);
		List<Usuario> usuariosFromDatabase = hibernateUtil.buscar(filtro);

		for (Usuario usuarioFromDatabase : usuariosFromDatabase) {

			if (!codigo.equals(usuarioFromDatabase.getId_Codigo()) && !arvoreHierarquica.containsKey(usuarioFromDatabase.getId_Codigo())) {

				arvoreHierarquica.put(usuarioFromDatabase.getId_Codigo(), new ArvoreHierarquicaDTO(usuarioFromDatabase, nivel));

				pesquisarComRecursividade(usuarioFromDatabase.getId_Codigo(), arvoreHierarquica, nivel + 1);
			}
		}
	}
}