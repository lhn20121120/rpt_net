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
 *	 У�����ɹ�ʽ
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
		
		/* �����Ӧ�Ĳ��� */
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
		/*��Ԫ������*/
		String cellName = mCell != null ? mCell.getCellName() : "";
		
		if(idrId!=null && cellName!=null)
		{
		try{
			//�����������ǰ̨
			request.setAttribute("IdrId",idrId);
			request.setAttribute("CellName",cellName);			
			HttpSession session = request.getSession();
			/* �������ù�ʽ��ֵ*/
			/*Formual formual=new Formual();
			formual.setSrc(textAreaStr);
			// System.out.println("��ʽΪ:"+formual.getSrc());
			formual.setRepdate(reptDate);
			// System.out.println("����SQL��");
		//	// System.out.println(formual.parse());
			// System.out.println("������ֵΪ��");
			// System.out.println(formual.value());
			String value = formual.value();
			
			Float parseValue=new Float(value);
			Float resultValue=new Float(result);
			String message = "��ʽ��ֵΪ:";	
			if(parseValue.floatValue()==resultValue.floatValue()){
				message+=value+"    ��ȷ!";
			}else{
				message+=value+"    ����!";
			}
			
			messages.add(message);*/
		}catch(Exception e){
			e.printStackTrace();
		}
		}
		else
		{
			messages.add("У��ʧ��!");			
		}
		if(messages.getMessages() != null && messages.getMessages().size() != 0)               
			   request.setAttribute(Config.MESSAGES,messages);
		return mapping.findForward("view");	
	}
}