package com.fitech.framework.core.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.fitech.framework.core.common.Config;

/**
 * @author 石昊东
 */
public class SystemServlet extends HttpServlet {

	/**
	 * ServletContext
	 */
	private ServletContext context = null;

	/**
	 * 
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * 
	 * @return void
	 * @exception IOException
	 *                ,ServletException
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = config.getServletContext();

		Config.WEBROOTPATH = context.getRealPath("/");
		Config.PageSize = Integer.parseInt(config.getInitParameter("PageSize"));
	}

	public void destroy() {

	}
}
