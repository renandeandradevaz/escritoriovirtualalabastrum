package br.com.alabastrum.escritoriovirtual.service;

import java.util.Map;
import java.util.TreeMap;

import br.com.alabastrum.escritoriovirtual.dto.ArvoreHierarquicaDTO;

public class MatrizService {

    public TreeMap<Integer, Integer> calcularQuantidadesExistentes(Map<Integer, ArvoreHierarquicaDTO> arvoreHierarquicaCompleta) {

	TreeMap<Integer, Integer> quantidadesExistentes = new TreeMap<Integer, Integer>();
	for (ArvoreHierarquicaDTO arvoreHierarquicaDTO : arvoreHierarquicaCompleta.values()) {

	    Integer nivel = arvoreHierarquicaDTO.getNivel();

	    if (nivel <= 10) {
		Integer quantidadeExistente = quantidadesExistentes.get(nivel);
		if (quantidadeExistente == null) {
		    quantidadesExistentes.put(nivel, 1);
		} else {
		    quantidadesExistentes.put(nivel, quantidadeExistente + 1);
		}
	    }
	}

	return quantidadesExistentes;
    }
}