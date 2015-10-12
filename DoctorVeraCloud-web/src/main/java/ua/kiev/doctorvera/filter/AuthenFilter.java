package ua.kiev.doctorvera.filter;

import ua.kiev.doctorvera.resources.Mapping;
import ua.kiev.doctorvera.resources.Message;
import ua.kiev.doctorvera.security.SecurityPolicy;
import ua.kiev.doctorvera.security.SecurityUtils;
import ua.kiev.doctorvera.views.SessionParams;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class AuthenFilter implements Filter {

	@Inject
	private SessionParams sessionparams;

	@Inject
	private SecurityUtils securityUtils;

	private final static Logger LOG = Logger.getLogger(AuthenFilter.class.getName());
	private final String LOGIN_PAGE = Mapping.getInstance().getString("LOGIN_PAGE");
	private final String REGISTER_PAGE = Mapping.getInstance().getString("REGISTER_PAGE");
	private static final String SECURITY_POLICY_PARAM_NAME = "securityPolicy";
	
	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws java.io.IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		final String url = request.getRequestURL().toString();

		//Setting request encoding
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		System.setProperty("file.encoding", "UTF-8");

		String securityPolicy = request.getParameter(SECURITY_POLICY_PARAM_NAME);

		if (url.contains(LOGIN_PAGE) || url.contains(REGISTER_PAGE)) {
			// bruteReveal(request);
			// Pass request back down the filter chain
			chain.doFilter(request, response);
		} else if (sessionparams == null || sessionparams.getAuthorizedUser() == null) {
			LOG.info("Session is not authorised");
			response.sendRedirect(LOGIN_PAGE);
		}else if(securityPolicy == null){
			LOG.info("Session is authorised, security policy is not set");
			chain.doFilter(request, response);
		} else if (sessionparams != null && securityPolicy != null && sessionparams.getAuthorizedUser() != null &&
				securityUtils.checkPermissions(SecurityPolicy.valueOf(securityPolicy))) {
			LOG.info("Session is authorised, permission granted");
			chain.doFilter(request, response);
		}
		
	}

	/*
	private void bruteReveal(HttpServletRequest request) {
		Date lastCall;
		String lastIp;
	
		try {
			if (lastCall != null && (lastCall.getTime() - new Date().getTime()) <= 30000 && lastIp.equals(getClientIpAddr(request)))
				Thread.sleep(5000);
			Thread.sleep(1000);
			lastCall = new Date();
			lastIp = getClientIpAddr(request);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	private static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	*/
	public void destroy() {
		/*
		 * Called before the Filter instance is removed from service by the web
		 * container
		 */
	}
}