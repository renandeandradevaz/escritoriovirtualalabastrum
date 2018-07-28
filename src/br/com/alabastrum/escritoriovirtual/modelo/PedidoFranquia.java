package br.com.alabastrum.escritoriovirtual.modelo;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.alabastrum.escritoriovirtual.hibernate.Entidade;
import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;

@Entity
public class PedidoFranquia implements Entidade {

	@Id
	@GeneratedValue
	private Integer id;

	private GregorianCalendar data;
	private Integer idFranquia;
	private String status;

	public Franquia obterFranquia() {

		HibernateUtil hibernateUtil = new HibernateUtil();
		Franquia franquia = new Franquia();
		franquia.setId_Estoque(idFranquia);
		franquia = hibernateUtil.selecionar(franquia);
		hibernateUtil.fecharSessao();
		return franquia;
	}

	public BigDecimal obterValorTotal() {

		HibernateUtil hibernateUtil = new HibernateUtil();

		BigDecimal valorTotal = BigDecimal.ZERO;

		ItemPedidoFranquia filtro = new ItemPedidoFranquia();
		filtro.setPedidoFranquia(this);
		List<ItemPedidoFranquia> itens = hibernateUtil.buscar(filtro);

		for (ItemPedidoFranquia item : itens) {
			valorTotal = valorTotal.add(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
		}

		hibernateUtil.fecharSessao();

		return valorTotal;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public GregorianCalendar getData() {
		return data;
	}

	public void setData(GregorianCalendar data) {
		this.data = data;
	}

	public Integer getIdFranquia() {
		return idFranquia;
	}

	public void setIdFranquia(Integer idFranquia) {
		this.idFranquia = idFranquia;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}