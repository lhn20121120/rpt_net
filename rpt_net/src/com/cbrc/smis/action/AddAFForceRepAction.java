package com.cbrc.smis.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.entity.AFForceRep;
import com.cbrc.smis.form.AFForceRepForm;
import com.cbrc.smis.service.IAFForceRepService;
import com.cbrc.smis.service.impl.AFForceRepServiceImpl;

public class AddAFForceRepAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String repInIds = request.getParameter("repInIds");
		
		
		IAFForceRepService repService = new AFForceRepServiceImpl();
		PrintWriter out = response.getWriter();
		try {
			if(repInIds.indexOf(",")>0){//多个报表
				String[] idArray = repInIds.split(",");
				for(int i=0;i<idArray.length;i++){
					AFForceRep rep = repService.findAFForceRepByRepInId(Integer.valueOf(idArray[i]));
					if(rep==null){
						AFForceRepForm repForm = new AFForceRepForm();
						repForm.setRepInId(Integer.valueOf(idArray[i]));
						repService.addAFForceRep(repForm);
					}
				}	
			}else{
				AFForceRep rep = repService.findAFForceRepByRepInId(Integer.valueOf(repInIds));
				if(rep==null){
					AFForceRepForm repForm = new AFForceRepForm();
					repForm.setRepInId(Integer.valueOf(repInIds));
					repService.addAFForceRep(repForm);
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
