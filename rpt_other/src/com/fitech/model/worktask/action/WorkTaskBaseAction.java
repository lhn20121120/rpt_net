package com.fitech.model.worktask.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fitech.framework.core.web.PageResults;
import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.security.IOperator;

public abstract class WorkTaskBaseAction extends DefaultBaseAction {

	private Logger log = Logger.getLogger(this.getClass());
	
	protected int pageNo = 1;

	protected int pageSize = 5;

	private PageResults pageResults;
	
	public void printStackTrace(Exception e) {
		log.info(e.getMessage(), e);
	}

	public void println(String mes) {
		log.info(mes);
	}

	public void responseFile(HttpServletResponse response, File file)
			throws Exception {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(file);
			output = response.getOutputStream();
			byte[] buffer = new byte[8192];
			while (input.read(buffer) != -1)
				output.write(buffer);
			output.flush();
		} catch (Exception e) {
			this.printStackTrace(e);
		} finally {
			if (input != null)
				input.close();
			if (output != null)
				output.close();
		}
	}
	/**
	 * 返回存入session中的operator接口
	 * @return
	 * @throws Exception
	 */
	public IOperator getOperator()throws Exception{
		return (IOperator)this.getHttpSession().getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
	}
	public abstract String initMethod() throws Exception;

	public PageResults getPageResults() {
		return pageResults;
	}

	public void setPageResults(PageResults pageResults) {
		this.pageResults = pageResults;
	}
}
