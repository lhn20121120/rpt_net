package com.fitech.net.action;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.auth.adapter.StrutsMPurBankLevelDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.service.StrutsTemplateOrgRelationDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;

/** 
* 查看,设定机构报送范围
* @author 吴昊
* 
* XDoclet definition:
* @struts.action validate="true"
*/
public class ShowOrgBSFWAction extends Action {

	private static FitechException log = new FitechException(ShowOrgBSFWAction.class); 
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-22
	 */
	public ActionForward execute(
       ActionMapping mapping,
       ActionForm form,
       HttpServletRequest request,
       HttpServletResponse response)  throws IOException, ServletException{

		MessageResources resources=getResources(request);     
		FitechMessages messages = new FitechMessages();     
		Locale locale = request.getLocale();
		OrgNetForm orgNetForm = new OrgNetForm();     
		RequestUtils.populate(orgNetForm, request);
		//判断是否是机构列表
		String curPage=request.getParameter("curPage");
		if(curPage!=null){
			request.setAttribute("curPage", curPage);
		}

		//判断是否是机构列表
		String orgId=request.getParameter("org_id");
		if(!StringUtil.isEmpty(orgId))
			orgNetForm.setOrg_id(orgId);
		/**该机构已经有的报表*/     
		List orgReportList = null;     
		/**所有报表信息*/     
		List cbrclist = null;
		List pboclist = null;
        List otherlist = null;
     
		try{        
			/**查询该机构的上级机构报表模板列表*/         
			//cbrclist = StrutsMRepRangeDelegate.getPreOrgMRepRange(orgNetForm.getOrg_id()); 
			/**已使用hibernate 卞以刚 2011-12-22**/
			cbrclist = StrutsTemplateOrgRelationDelegate.getPreOrgRepRange(com.fitech.gznx.common.Config.CBRC_REPORT);
			pboclist = StrutsTemplateOrgRelationDelegate.getPreOrgRepRange(orgNetForm.getOrg_id(),
					com.fitech.gznx.common.Config.PBOC_REPORT);
        	otherlist = StrutsTemplateOrgRelationDelegate.getPreOrgRepRange(orgNetForm.getOrg_id(),
        			com.fitech.gznx.common.Config.OTHER_REPORT);
         
			/**查询该机构已经有的报表权限信息*/       
        	/**已使用hibernate 卞以刚 2011-12-22**/
			orgReportList = StrutsMRepRangeDelegate.getOrgHaveReport(orgNetForm.getOrg_id());

		}catch (Exception e){        
			log.printStackTrace(e);         
			messages.add(FitechResource.getMessage(locale,resources,"select.template.failed"));    
		}
          
		/**机构编号*/     
		if(orgNetForm.getOrg_id()!=null)        
			request.setAttribute("org_id",orgNetForm.getOrg_id());
     
		/**机构名称*/     
		OrgNet orgNet=null;  
		/**已使用hibernate 卞以刚 2011-12-22**/
		orgNet=StrutsOrgNetDelegate.selectOne(orgNetForm.getOrg_id());     
		if(orgNet!=null)    	
			if(orgNet.getOrgName()!=null)         
				request.setAttribute("org_name",orgNet.getOrgName());     
    
		/**机构已经有的报表权限*/     
		if(orgReportList!=null && orgReportList.size()!=0)        
			request.setAttribute("orgHaveReport",orgReportList);     
     
		/**所有报表*/     
		if(cbrclist!=null && cbrclist.size()!=0)
	          request.setAttribute("cbrc",cbrclist);
        if(pboclist!=null && pboclist.size()!=0)
            request.setAttribute("pboc",pboclist);
        if(otherlist!=null && otherlist.size()!=0)
            request.setAttribute("other",otherlist);
    
		if(messages.getMessages() != null && messages.getMessages().size() > 0)        
			request.setAttribute(Config.MESSAGES,messages);
		
		return mapping.findForward("show_BSFW");
	}
}

