package com.fitech.net.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;

/**
 *描述：机构设定Action显示机构列表
 *日期：2008-1-3
 *作者：曹发根
 */
public class SelectNetOrgAction extends Action {
	private FitechException log=new FitechException(ViewJYNotPassInfoAction.class);
	
	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		try{		
			HttpSession session = request.getSession();
			Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			OrgNetForm orgNetForm= (OrgNetForm)form;
			// gongming 2008-07-28 commont;
//			String select  = request.getParameter("select");
			
//			if(orgNetForm.getPre_org_id()==null||orgNetForm.getPre_org_id().equals("") )
//			{//	orgNetForm.setPre_org_id(operator.getOrgId());
//			  if(orgNetForm.getOrg_id()!=null && !"".equals(orgNetForm.getOrg_id()))
//			  {	  
//			      OrgNet	orgNet=StrutsOrgNetDelegate.selectOne(orgNetForm.getOrg_id());    
//			      if(orgNet != null ){
//					orgNetForm.setPre_org_id(orgNet.getPreOrgId());
//			      }else{
//			    	  orgNetForm.setPre_org_id(operator.getOrgId());
//			      }
//			  }else{
//				  orgNetForm.setPre_org_id(operator.getOrgId());
//			  }
//			}
            orgNetForm.setPre_org_id(operator.getOrgId());
			int recordCount = 0; // 记录总数
			int offset = 0; // 偏移量
			int limit = 0; // 每页显示的记录条数	   
			
			//List对象的初始化	   
			List resList=null;
			ApartPage aPage=new ApartPage();
		   	String strCurPage=request.getParameter("curPage");
			if(strCurPage!=null){
			    if(!strCurPage.equals(""))
			      aPage.setCurPage(new Integer(strCurPage).intValue());
			}
			else
				aPage.setCurPage(1);
			//计算偏移量
			offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
			limit = Config.PER_PAGE_ROWS;	
//           gongming 2008-07-28 commont;
//			if(select != null && !"".equals(select)){
//				recordCount=StrutsOrgNetDelegate.selectOrgNameCount(orgNetForm);
//				if(recordCount>0)resList = StrutsOrgNetDelegate.selectSubOrgByName(orgNetForm, offset, limit);
//			}else{
//				recordCount=StrutsOrgNetDelegate.selectSubOrgCount(orgNetForm);
//				if(recordCount>0)resList = StrutsOrgNetDelegate.selectSubOrg(orgNetForm, offset, limit);
//			}
			
			recordCount=StrutsOrgNetDelegate.selectSubOrgCount(orgNetForm,operator);
			if(recordCount>0)resList = StrutsOrgNetDelegate.selectSubOrg(orgNetForm, offset, limit);
			  
			aPage.setTerm(this.getTerm(orgNetForm));
		 	aPage.setCount(recordCount);
		 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		 	
		 	if(resList!=null && resList.size()!=0)
				  request.setAttribute(Config.RECORDS,resList);
			
		}catch(Exception e){
			log.printStackTrace(e);
		}		
		return mapping.findForward("view");
		

		
	}
	public String getTerm(OrgNetForm orgNetForm){	
		String term="";	   
		String org_name = orgNetForm.getOrg_name();

		if(org_name!=null&&!org_name.equals(""))		  
		{		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "org_name="+org_name.toString();   		   
		}
		
		if(term.indexOf("?")>=0)			
			term = term.substring(term.indexOf("?")+1);
		   
		return term;   
	}  
	
}