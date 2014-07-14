package com.fitech.net.obtain.text;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EditFormulaAction  extends Action{

	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		
		AddFormulaForm obtainForm=(AddFormulaForm)form;
		// System.out.println(request.getAttribute("column")+"============");
		String  id=String.valueOf(request.getParameter("id"));
		// System.out.println(id);
		
		List list=StructsSearchFormulaDelegate.getFormulaListById(id,obtainForm);
		request.setAttribute("ObjForm",obtainForm);
		//request.setAttribute("column",getcolumn(((obtaintext)list.get(0)).getFormula()));
		return mapping.findForward("editformula");
		
	}
	private String getcolumn(String formula)
	{
		String temp="";
		
		for (int i=0;i<formula.length();i++)
		{
			if(formula.charAt(i)>'0'&&formula.charAt(i)<'9')
				temp+=formula.charAt(i);
		}
		return temp;
	}
}
