package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMCellDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.form.IDataRelationForm;

/**
 *	 校验生成公式
 *
 * @author yaojie
 */
public final class CheckFormulaAction extends Action
{
	private static FitechException log = new FitechException(CheckFormulaAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		FitechMessages messages = new FitechMessages();
		IDataRelationForm IDataRelationForm = new IDataRelationForm();
		
		RequestUtils.populate(IDataRelationForm, request);
		String idrIdstr ="";
		String textAreaStr = "";
		String result = "";
		String reptDate ="";	
		
		/* 获得相应的参数 */
		if (request.getParameter("IdrId") != null)
		{
			idrIdstr = request.getParameter("IdrId");
		}
		if (request.getParameter("textAreaStr") != null)
		{
			textAreaStr = request.getParameter("textAreaStr");
		}
		if (request.getParameter("result") != null)
		{
			result = request.getParameter("result");
		}
		if (request.getParameter("reptDate") != null)
		{
			reptDate = request.getParameter("reptDate");
		}
		
		Integer idrId = null;
		if(idrIdstr!=""){
			idrId=Integer.valueOf(idrIdstr);
		}		
		MCell mCell = StrutsMCellDelegate.getMCell(idrId);
		/*单元格名称*/
		String cellName = mCell != null ? mCell.getCellName() : "";
		
		if(idrId!=null && cellName!=null)
		{
		try{
			//所需参数传给前台
			request.setAttribute("IdrId",idrId);
			request.setAttribute("CellName",cellName);			
			HttpSession session = request.getSession();
			/* 解析出该公式的值*/
			/*Formual formual=new Formual();
			formual.setSrc(textAreaStr);
			// System.out.println("公式为:"+formual.getSrc());
			formual.setRepdate(reptDate);
			// System.out.println("解析SQL：");
		//	// System.out.println(formual.parse());
			// System.out.println("解析数值为：");
			// System.out.println(formual.value());
			String value = formual.value();
			
			Float parseValue=new Float(value);
			Float resultValue=new Float(result);
			String message = "公式的值为:";	
			if(parseValue.floatValue()==resultValue.floatValue()){
				message+=value+"    正确!";
			}else{
				message+=value+"    错误!";
			}
			
			messages.add(message);*/
		}catch(Exception e){
			e.printStackTrace();
		}
		}
		else
		{
			messages.add("校验失败!");			
		}
		if(messages.getMessages() != null && messages.getMessages().size() != 0)               
			   request.setAttribute(Config.MESSAGES,messages);
		return mapping.findForward("view");	
	}
}