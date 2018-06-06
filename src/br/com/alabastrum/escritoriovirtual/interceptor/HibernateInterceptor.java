package br.com.alabastrum.escritoriovirtual.interceptor;

import java.io.PrintWriter;
import java.io.StringWriter;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.JavaMailApp;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;

@RequestScoped
@Intercepts(after = InterceptadorDeAutorizacao.class)
public class HibernateInterceptor implements Interceptor {

	private Result result;
	private HibernateUtil hibernateUtil;
	private SessaoUsuario sessaoUsuario;

	public HibernateInterceptor(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario) {

		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.sessaoUsuario = sessaoUsuario;
	}

	public void intercept(InterceptorStack stack, ResourceMethod method, Object instance) throws InterceptionException {

		try {

			stack.next(method, instance);

			hibernateUtil.fecharSessao();
		}

		catch (RuntimeException e) {

			hibernateUtil.fecharSessao();

			StringWriter writerStack = new StringWriter();
			PrintWriter printWriterStack = new PrintWriter(writerStack);
			e.printStackTrace(printWriterStack);
			String errorString = writerStack.toString();

			result.include("exception", errorString);
			JavaMailApp.enviarEmail("Exception no EV para o usuario com codigo = " + sessaoUsuario.getUsuario().getId_Codigo(), "renanandrade_rj@hotmail.com", errorString);

			throw e;
		}

	}

	public boolean accepts(ResourceMethod method) {
		return true;
	}

}