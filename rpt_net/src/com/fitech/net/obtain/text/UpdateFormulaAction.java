package com.fitech.net.obtain.text;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.util.FitechMessages;

public class UpdateFormulaAction extends Action {

	public ActionForward execute(
		      ActionMapping mapping,
		      ActionForm form,
		      HttpServletRequest request,
		      HttpServletResponse response
		   )
		      throws IOException, ServletException {
		MessageResources resources=getResources(request);
		   FitechMessages messages = new FitechMessages();
		   
		AddFormulaForm addFormulaForm = (AddFormulaForm) form;
		  RequestUtils.populate(addFormulaForm,request);
		boolean result=false;
		   try
		{
			   
			   manage man =new manage();
			  
			   man.setFormula(addFormulaForm.getFormula());
			   man.setFilename(addFormulaForm.getDataSourceEname());
			   man.setGigoucolumn(Integer.parseInt(addFormulaForm.getOrgId().trim()));
			   int iresult=0;
			   try
			   {
			    iresult=man.check();
			   }
			   catch(Exception e)
			   {
				  iresult=4;
			   }
			   if(iresult!=0 && iresult!=1)
			   {
				   result=false;
				   
				   ArrayList list=new ArrayList();
				   if(iresult==1)
					   list.add("文件不存在");
				   if(iresult==2)
					   list.add("列号不是数字");
				   if(iresult==3)
					   list.add("机构列不能参加计算");
				   
					 if(iresult==4)
						 list.add("公式不对");
				   messages.setMessages(list);
				   
			   }
			   else
			  
	               result=StructsSearchFormulaDelegate.update(addFormulaForm);
		}
		catch(Exception e)
		{
			result=false;
			
		}
		if(result==true)
			messages.add("更新成功！");
		
		
		request.setAttribute("Message",messages);
		
		if (result==false)
		{
			request.setAttribute("ObjForm",addFormulaForm);
			return mapping.findForward("editformula");
		}
		else	
		{
		
		return new ActionForward("/obtain/text/viewformula2.do");
		}
	}
	
}
