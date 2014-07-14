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

import com.fitech.net.config.Config;

public class GetDataAllAction extends Action{
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		List list=StructsSearchFormulaDelegate.getFormulaList(null,null,null,0,10000);
		String message=null;
		for(int i=0;i<list.size();i++)
		{
			obtaintext d = ( obtaintext)list.get(i);
			manage man=new manage();
			man.setFormula(d.getFormula());
			man.setFenge(d.getSplitChar().trim().charAt(0));
			man.setFilename(Config.GetTextPath()+"\\"+d.getDataSourceEname());
			
			man.setGigoucolumn(Integer.parseInt(d.getOrgId())-1);
			man.setReportname(d.getReportname());
			
			man.setRowcolumn(d.getRowColumn());
			if(man.check()==0)
			{
			 
				if(man.GetAll()==true)
				{
					StructsSearchFormulaDelegate.UpdataFlag("1",d) ;
				    message="取数成功";
				}
				message="取数失败";
			}
		}
		
		request.setAttribute("message",message);
	return new ActionForward("/obtain/text/viewformula.do");
	}
}
