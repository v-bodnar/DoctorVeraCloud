package ua.kiev.doctorvera.filter;

import java.io.IOException;
import java.util.Date;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class LogFilter implements Filter {
	
	private static Logger log = Logger.getLogger(AuthenFilter.class.getName());

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {

		//Loading logging configuration
		try {
			//Different target of logging
			LogManager.getLogManager().readConfiguration(LogFilter.class.getResourceAsStream("/logging.properties"));
			//LogManager.getLogManager().readConfiguration(LogFilter.class.getResourceAsStream("/log_console.properties"));
		} catch (IOException e) {
			System.err.println("Could not setup logger configuration: " + e.toString());
		}
		
		//Setting request encoding
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		System.setProperty("file.encoding", "UTF-8");

		// Get request parameters
		String ipAddress = request.getRemoteAddr();
		String url = ((HttpServletRequest) request).getRequestURL().toString();
		String queryString = ((HttpServletRequest) request).getQueryString();

		// Log the IP address and current timestamp.
		log.fine("Time " + new Date().toString() + " Client IP: " + ipAddress);
		log.fine("Time " + new Date().toString() + " Url: " + url + "?" + queryString);
		// Pass request back down the filter chain
		chain.doFilter(request, response);
	}

	public void destroy() {
		/*
		 * Called before the Filter instance is removed from service by the web
		 * container
		 */
	}
}