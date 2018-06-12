package br.com.alabastrum.escritoriovirtual.sessao;

import java.util.HashMap;

import br.com.alabastrum.escritoriovirtual.util.Util;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class SessaoGeral {

	private HashMap<String, Object> hashSessao;

	private HashMap<String, Object> getHashSessao() {

		if (Util.vazio(this.hashSessao)) {
			this.hashSessao = new HashMap<String, Object>();
		}

		return hashSessao;
	}

	public Object getValor(String chave) {

		HashMap<String, Object> hashSessao = getHashSessao();

		if (hashSessao.containsKey(chave)) {

			return hashSessao.get(chave);
		}

		else
			return null;
	}

	public void adicionar(String chave, Object valor) {

		if (Util.vazio(this.hashSessao)) {
			this.hashSessao = new HashMap<String, Object>();
		}

		this.hashSessao.put(chave, valor);

	}

}