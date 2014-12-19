package ua.kiev.doctorvera.filter;

import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.kiev.doctorvera.managedbeans.UserLoginView;
import ua.kiev.doctorvera.web.resources.Mapping;

public class AuthenFilter implements Filter {

	private final static Logger LOG = Logger.getLogger(AuthenFilter.class.getName());
	private final String LOGIN_PAGE = Mapping.getInstance().getProperty(Mapping.Page.LOGIN_PAGE);
	private final String REGISTER_PAGE = Mapping.getInstance().getProperty(Mapping.Page.REGISTER_PAGE);
	private final String APPLICATION_ROOT_PATH = Mapping.getInstance().getProperty(Mapping.Path.APPLICATION_ROOT_PATH);
	
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
		
		if (url.contains(LOGIN_PAGE) || url.contains(REGISTER_PAGE)) {
			// bruteReveal(request);
			// Pass request back down the filter chain
			chain.doFilter(request, response);
		} else if (isAuththenticated(request)){
			LOG.info("Time " + new Date().toString() + " Session is authorised");
			chain.doFilter(request, response);
		} else{
			LOG.info("Time " + new Date().toString() + " Session is not authorised");
			response.sendRedirect(APPLICATION_ROOT_PATH);
		}
		
	}
	
	//Checks if user is authenticated in session
	private Boolean isAuththenticated(HttpServletRequest request){
		UserLoginView bean = (UserLoginView) request.getSession().getAttribute("userLoginView");
		if ( bean != null && bean.getAuthorizedUser() != null) return true;
		else 
			return false;
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