package br.com.alabastrum.escritoriovirtual.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import br.com.alabastrum.escritoriovirtual.util.Util;

public class TagSimNao extends TagSupport {

	private static final long serialVersionUID = -9149033488270695019L;

	private String valor;

	public int doStartTag() throws JspException {

		String resultado = "";

		if (Util.vazio(this.valor) || this.valor.equals("false")) {

			resultado = "Não";
		}

		else {

			resultado = "Sim";
		}

		try {

			pageContext.getOut().println(resultado);

		} catch (IOException e) {

			e.printStackTrace();
		}

		return (EVAL_BODY_INCLUDE);
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
