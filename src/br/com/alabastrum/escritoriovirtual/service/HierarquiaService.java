package br.com.alabastrum.escritoriovirtual.service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class HierarquiaService {

	private HibernateUtil hibernateUtil;

	public HierarquiaService(HibernateUtil hibernateUtil) {

		this.hibernateUtil = hibernateUtil;
	}

	public Map<Integer, ArvoreHierarquicaDTO> obterArvoreHierarquicaTodosOsNiveis(Integer codigoUsuario) {

		Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquica = new HashMap<Integer, ArvoreHierarquicaDTO>();

		pesquisarComRecursividade(codigoUsuario, arvoreHierarquica, 1);

		return arvoreHierarquica;
	}

	private void pesquisarComRecursividade(Integer codigo, Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquica, int nivel) {

		Usuario usuario = new Usuario();

		try {

			Field field = usuario.getClass().getDeclaredField("id_lider");

			field.setAccessible(true);

			field.set(usuario, codigo);
		}

		catch (Exception e) {

			e.printStackTrace();
		}

		List<Usuario> usuariosFromDatabase = hibernateUtil.buscar(usuario);

		for (Usuario usuarioFromDatabase : usuariosFromDatabase) {

			if (!codigo.equals(usuarioFromDatabase.getId_Codigo())) {

				if (!arvoreHierarquica.containsKey(usuarioFromDatabase.getId_Codigo())) {

					arvoreHierarquica.put(usuarioFromDatabase.getId_Codigo(), new ArvoreHierarquicaDTO(usuarioFromDatabase, nivel));

					pesquisarComRecursividade(usuarioFromDatabase.getId_Codigo(), arvoreHierarquica, nivel + 1);
				}
			}
		}
	}
}