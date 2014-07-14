package com.fitech.net.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.Procedure;
import com.cbrc.smis.adapter.StrutsMCellDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.form.MCellForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.config.Config;

/**
 * 在线报表校验
 * 
 * @author Yao
 * 
 */
public class SelectCellIdAction extends Action
{
	private FitechException log = new FitechException(SelectCellIdAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		
		boolean resultBL = false;
		boolean resultBJ = false;
		String  childRepId = null;
		String  versionId = "0690";
		String result="";
		try
		{

			// 如果没有传递报表ID，则根据相关信息到数据库中查询出相应的ID
			if (request.getParameter("childRepId") != null)
			{
				childRepId =request.getParameter("childRepId");
				List list = StrutsMCellDelegate.getCells(childRepId,versionId);
				if(list!=null && list.size()>0){					
					MCellForm mCellForm = null;
					result+="{select name='cellID' id='cellID'  onchange='changeCellID(this)'>";
					result+="{option value='-1'>------{/option>";
					for(int i=0;i<list.size();i++){
						mCellForm=(MCellForm)list.get(i);
						
						result+="{option value='"+mCellForm.getCellId()+"'>"+mCellForm.getCellName()+"{/option>";
					}
					result+="{/select>";
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resultBL = false;
			resultBJ = false;
		}

		PrintWriter out = response.getWriter();

		response.setContentType("text/xml");
		response.setHeader("Cache-control", "no-cache");
	//	result=result.replaceAll("<", "{");
		out.println("<response><result>" + result + "</result></response>");

		out.close();

		return null;
	}
	
}
