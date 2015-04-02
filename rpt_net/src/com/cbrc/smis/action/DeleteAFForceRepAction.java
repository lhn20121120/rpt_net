package com.cbrc.smis.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.entity.AFForceRep;
import com.cbrc.smis.service.IAFForceRepService;
import com.cbrc.smis.service.impl.AFForceRepServiceImpl;

public class DeleteAFForceRepAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String repInIds = request.getParameter("repInIds");
		
		IAFForceRepService repService = new AFForceRepServiceImpl();
		PrintWriter out = response.getWriter();
		try {
			if(repInIds!=null && !repInIds.equals("")){
				if(repInIds.indexOf(",")>0){//多个报表
					String[] repArray = repInIds.split(",");
					for(int i=0;i<repArray.length;i++)
						repService.deleteAFForceRep(Integer.valueOf(repArray[i]));
				}else{//单个报表
					repService.deleteAFForceRep(Integer.valueOf(repInIds));
				}
			}
			

			response.setContentType("text/xml");
			response.setHeader("Cache-control", "no-cache");
			out.println("<response><result>" + true + "</result></response>");
			
		} catch (Exception e) {
			e.printStackTrace();
			out.println("<response><result>" + false + "</result></response>");
		}finally{
			out.close();
		}
		return null;
	}
	
}
