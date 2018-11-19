package br.com.alabastrum.escritoriovirtual.interceptor;

import javax.servlet.http.HttpServletRequest;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.sessao.SessaoUsuario;
import br.com.alabastrum.escritoriovirtual.util.Mail;
import br.com.alabastrum.escritoriovirtual.util.Util;
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

			String errorString = Util.getExceptionMessage(e1);
			result.include("exception", errorString);

			try {
				if (!errorString.contains("Broken pipe")) {

					String userAgent = "User-Agent: " + request.getHeader("User-Agent") + "<br> <br> <br>";

					String titulo;

					if (sessaoUsuario != null && sessaoUsuario.getUsuario() != null) {
						titulo = "Exception no EV para o usuario com codigo = " + sessaoUsuario.getUsuario().getId_Codigo();
					} else {
						titulo = "Exception no EV";
					}

					//Mail.enviarEmail(titulo, userAgent + errorString);
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