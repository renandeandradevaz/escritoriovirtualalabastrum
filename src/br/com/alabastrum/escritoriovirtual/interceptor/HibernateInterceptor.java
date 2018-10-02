package br.com.alabastrum.escritoriovirtual.interceptor;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Mail;
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
	private HttpServletRequest request;

	public HibernateInterceptor(Result result, HibernateUtil hibernateUtil, SessaoUsuario sessaoUsuario, HttpServletRequest request) {

		this.result = result;
		this.hibernateUtil = hibernateUtil;
		this.sessaoUsuario = sessaoUsuario;
		this.request = request;
	}

	public void intercept(InterceptorStack stack, ResourceMethod method, Object instance) throws InterceptionException {

		try {

			stack.next(method, instance);

			hibernateUtil.fecharSessao();
		}

		catch (RuntimeException e1) {

			hibernateUtil.fecharSessao();

			StringWriter stringWriter = new StringWriter();
			e1.printStackTrace(new PrintWriter(stringWriter));
			String errorString = stringWriter.toString();

			result.include("exception", errorString);

			try {
				if (!errorString.contains("Broken pipe")) {

					String userAgent = "User-Agent: " + request.getHeader("User-Agent") + "<br> <br> <br>";

					//Mail.enviarEmail("Exception no EV para o usuario com codigo = " + sessaoUsuario.getUsuario().getId_Codigo(), userAgent + errorString);
				}
			} catch (Exception e2) {
			}

			throw e1;
		}
	}

	public boolean accepts(ResourceMethod method) {
		return true;
	}

}