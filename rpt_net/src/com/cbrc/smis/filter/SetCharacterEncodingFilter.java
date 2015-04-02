/**
* Title        \u89E3\u51B3\u4E2D\u6587\u4E71\u7801\u95EE\u9898<br>
* Description  <br>
* Copyright    Copyright (c) 2004<br>
* Company      \u5357\u4EAC\u79D1\u878D<br>
* @author      \u623F\u9E4F
* @version     1.0
*/

package com.cbrc.smis.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SetCharacterEncodingFilter implements Filter {

  protected String encoding = null;

  protected FilterConfig filterConfig = null;

  protected boolean ignore = true;

  public void destroy() {
    this.encoding = null;
    this.filterConfig = null;
  }

  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {

    // Conditionally select and set the character encoding to be used
    if (ignore || (request.getCharacterEncoding() == null)) {
      String encoding = selectEncoding(request);
      if (encoding != null) {
        request.setCharacterEncoding(encoding);
      }
    }
  
    // Pass control on to the next filter
    chain.doFilter(request, response);

  }

  public void init(FilterConfig filterConfig) throws ServletException {
    this.filterConfig = filterConfig;
    this.encoding = filterConfig.getInitParameter("encoding");
    String value = filterConfig.getInitParameter("ignore");
    if (value == null) {
      this.ignore = true;
    }
    else if (value.equalsIgnoreCase("true")) {
      this.ignore = true;
    }
    else if (value.equalsIgnoreCase("yes")) {
      this.ignore = true;
    }
    else {
      this.ignore = false;
    }

  }

  protected String selectEncoding(ServletRequest request) {
    return (this.encoding);
  }

}

