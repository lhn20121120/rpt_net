package com.fitech.gznx.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.cbrc.smis.adapter.StrutsMCellFormuDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.other.Expression;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfValidateformula;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.StrutsAFCellFormuDelegate;

public class EditBJGXAction extends Action {
	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		
		FitechMessages messages=new FitechMessages();
		
		MCellFormuForm mCellFormuForm=new MCellFormuForm();
		RequestUtils.populate(mCellFormuForm,request);
		
		String curPage=null;
		
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
		
		List resList=StrutsAFCellFormuDelegate.selectAllFormula(mCellFormuForm.getChildRepId(),mCellFormuForm.getVersionId());
		
		//List expressions=getExpressions(resList);
		
		//表内表间关系表达式列表
		//if(expressions!=null) request.setAttribute("Expressions",expressions);
		if(resList!=null){
			request.setAttribute(Config.RECORDS,resList);
		}
			

		AfTemplate aftemplate = AFTemplateDelegate.getTemplate(mCellFormuForm.getChildRepId(),mCellFormuForm.getVersionId());
		if(aftemplate!=null){
			// 清单式报表
			if(aftemplate.getReportStyle() != null &&
					com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(aftemplate.getReportStyle()))){
				mCellFormuForm.setReportStyle(Config.REPORT_STYLE_QD);
			}else{
				mCellFormuForm.setReportStyle(Config.REPORT_STYLE_DD);
			}
			
			mCellFormuForm.setReportName(aftemplate.getTemplateName());
		}
		
		request.setAttribute("ObjForm",mCellFormuForm);

		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages); 
		request.setAttribute("CurPage",curPage);
		
		return mapping.findForward("view");
	}
	
	/**
	 * 将MCellFormuForm信息列表转换成Expression对象信息列表
	 * 
	 * @param mCellFormuForms List 
	 * @return List
	 */
	private List getExpressions(List mCellFormuForms){
		List listExp=null;
		
		if(mCellFormuForms==null) return listExp;
		// System.out.println("mCellFormuForms.size:" + mCellFormuForms.size());
		Iterator it=mCellFormuForms.iterator();
		if(it!=null) listExp=new ArrayList();
		MCellFormuForm form=null;
		while(it.hasNext()){
			form=(MCellFormuForm)it.next();
			Expression expression=new Expression();
			expression.setContent(form.getCellFormu());
			expression.setType(form.getFormuType());
			listExp.add(expression);
		}
		
		return listExp;
	}
}
