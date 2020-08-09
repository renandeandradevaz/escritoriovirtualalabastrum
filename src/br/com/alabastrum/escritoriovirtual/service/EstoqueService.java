package br.com.alabastrum.escritoriovirtual.service;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.Estoque;

public class EstoqueService {

    private HibernateUtil hibernateUtil;

    public EstoqueService(HibernateUtil hibernateUtil) {

	this.hibernateUtil = hibernateUtil;
    }

    public Integer getQuantidadeEmEstoque(String idProduto, Integer idFranquia) {

	Estoque estoque = new Estoque();
	estoque.setIdFranquia(idFranquia);
	estoque.setIdProduto(idProduto);
	estoque = hibernateUtil.selecionar(estoque);

	if (estoque == null)
	    return 0;
	else
	    return estoque.getQuantidade();
    }

    public void retirarDoEstoque(String idProduto, Integer idFranquia, Integer quantidade) {

	Estoque estoque = new Estoque();
	estoque.setIdFranquia(idFranquia);
	estoque.setIdProduto(idProduto);
	estoque = hibernateUtil.selecionar(estoque);

	if (estoque != null) {
	    estoque.setQuantidade(estoque.getQuantidade() - quantidade);
	    hibernateUtil.salvarOuAtualizar(estoque);
	}
    }

    public void adicionarAoEstoque(String idProduto, Integer idFranquia, Integer quantidade) {

	Estoque estoque = new Estoque();
	estoque.setIdFranquia(idFranquia);
	estoque.setIdProduto(idProduto);
	estoque = hibernateUtil.selecionar(estoque);

	if (estoque == null) {
	    estoque = new Estoque();
	    estoque.setIdFranquia(idFranquia);
	    estoque.setIdProduto(idProduto);
	    estoque.setQuantidade(0);
	}

	estoque.setQuantidade(estoque.getQuantidade() + quantidade);
	hibernateUtil.salvarOuAtualizar(estoque);
    }
}
