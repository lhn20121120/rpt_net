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

public class GetDataAction extends Action{
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		
		String  id=String.valueOf(request.getParameter("id"));
		String message=null;
		manage man=new manage();
		List list=StructsSearchFormulaDelegate.GetFormulaList(id);
		obtaintext d=null;
		if(list!=null&&list.size()>0)
		{
			 d = (obtaintext)list.get(0);
			 
				man.setFormula(d.getFormula());
				man.setFenge(d.getSplitChar().trim().charAt(0));
				man.setFilename(Config.GetTextPath()+"\\"+d.getDataSourceEname());
				
				man.setGigoucolumn(Integer.parseInt(d.getOrgId()));
				man.setReportname(d.getChildReportId().trim()+"_"+d.getVersionId().trim()+".xls");
				
				man.setRowcolumn(d.getRowColumn());
			 if(man.check()==0)
				{
					
					if(man.GetAll()==true)
					{
						StructsSearchFormulaDelegate.UpdataFlag("1",d) ;
						message="取数成功";
					}
					else
					{
						StructsSearchFormulaDelegate.UpdataFlag("0",d) ;
						message="取数失败";
					}
				}
				else
				{
					message="校验失败";
				}
		}
		request.setAttribute("message",message);
		
		
		return new ActionForward("/obtain/text/viewformula.do");
	}
	
}
