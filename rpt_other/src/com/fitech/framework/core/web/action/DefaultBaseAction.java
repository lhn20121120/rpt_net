package com.fitech.framework.core.web.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionSupport;

public abstract class DefaultBaseAction extends ActionSupport{
	
	public static final String[] SELECT_ALL = {"-1","全部"};
	
    protected int pageNo = 1;

    protected int pageSize = 20;

    protected String getWebApplicationAbsolutePath() {
        String realPath = getServletContext().getRealPath("/");
        if (realPath != null) {
            if (!realPath.endsWith("/"))
                return realPath + "/";
        }
        return realPath;
    }
    
    protected ServletContext getServletContext() {
        return ServletActionContext.getServletContext();
    }

    protected HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    protected HttpSession getHttpSession() {
        return ServletActionContext.getRequest().getSession();
    }

    protected HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    public Object getBean(String name) {
        ServletContext servletContext = ServletActionContext.getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        return wac.getBean(name);
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public boolean isEmpty(Object o){
    	if(o==null)
    		return true;
    	boolean result = false;
    	String str = o.toString().trim();
    	if(str.equals("") || str.equals(this.SELECT_ALL[0]))
    		result= true;
    	return result;
    }
}
