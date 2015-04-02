package com.fitech.gznx.action;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.form.CodeLibForm;
import com.fitech.gznx.service.StrutsCodeLibDelegate;

public class ViewCodeLibAction extends Action {
	private static FitechException log = new FitechException(
			ViewCodeLibAction.class);
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-28
	 * 影响对象：AfCodelib
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();

		CodeLibForm codeLibForm = (CodeLibForm) form;
		RequestUtils.populate(codeLibForm, request);
		List list = null;
		if (request.getAttribute("codeTypeId") != null) {
			codeLibForm.setCodeTypeId((String) request.getParameter("codeTypeId"));
		}
		codeLibForm.setCodeTypeValue(request.getParameter("codeTypeValue"));
		int recordCount = 0; // 记录总数
		ApartPage aPage = new ApartPage();
		int curPage = 1;// 设置当前页
		String strCurPage = request.getParameter("curPage");
		if (strCurPage != null) {
			if (!strCurPage.equals(""))
				curPage = new Integer(strCurPage).intValue();
			aPage.setCurPage(new Integer(strCurPage).intValue());
		} else {
			aPage.setCurPage(1);
		}
		try {
			// 显示分页后的记录
			/**已使用hibernate 卞以刚 2011-12-28
			 * 影响对象：AfCodelib*/
			PageListInfo pageList = StrutsCodeLibDelegate.select(codeLibForm,
					curPage);
			request.setAttribute("typeName", codeLibForm.getTypeName());
			// list = StrutsCodeLibDelegate.select(codeLibForm);
			list = pageList.getList();
			recordCount = (int) pageList.getRowCount();
			if (codeLibForm != null)
				request.setAttribute("QueryForm", codeLibForm);
		} catch (Exception e) {
			log.printStackTrace(e);
		}
		aPage.setCount(recordCount);
		// 移除request或session范围内的属性
		FitechUtil.removeAttribute(mapping, request);
		// 把ApartPage对象存放在request范围内
        aPage.setTerm(this.getTerm(codeLibForm));
        request.setAttribute(Config.APART_PAGE_OBJECT,aPage);

		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		if (list != null && list.size() != 0)
			request.setAttribute(Config.RECORDS, list);
		return mapping.findForward("codeLib_mgr");
	}
	
	
	
	 public String getTerm(CodeLibForm codeLibForm)
	    {
	        String term="";
	        
	        String codeTypeId = codeLibForm.getCodeTypeId();
	        if(codeTypeId!=null)
	        {
	            term += (term.indexOf("?")>=0 ? "&" : "?");
	            term += "codeTypeId="+codeTypeId.toString();   
	        }
	        if(term.indexOf("?")>=0)
	            term = term.substring(term.indexOf("?")+1);
	        return term;
	        
	    }

}
