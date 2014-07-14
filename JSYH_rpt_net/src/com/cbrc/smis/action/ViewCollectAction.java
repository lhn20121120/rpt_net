package com.cbrc.smis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.dataCollect.DB2ExcelHandler;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.net.adapter.StrutsCollectDelegate;
import com.fitech.net.adapter.StrutsCollectReportDelegate;
import com.fitech.net.adapter.VorgCollectDelegate;
import com.fitech.net.hibernate.CollectReal;

/**
 * oralce语法 需要修改 卞以刚 2011-12-22
 * @ 数据汇总统计Action
 * @ author gen
 *
 */
public final class ViewCollectAction extends Action {
	private FitechException log=new FitechException(ViewCollectAction.class);
	
   /**
    * @param result 查询返回标志,如果成功返回true,否则返回false
    * @param ReportInForm 
    * @param request 
    * @exception Exception 有异常捕捉并抛出
    */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
	)throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();	
		MessageResources resources = getResources(request);	
		ReportInForm reportInForm = (ReportInForm)form ;	   
		RequestUtils.populate(reportInForm, request);
		List resList = null;
		List list = null;
		List vresList=null;
		
		HttpSession session = request.getSession();
		String ss = (String)request.getAttribute("checkTask");
		if(ss != null&&!ss.equals("")){
			resList = null;
			//recordCount = 0 ;
			String dd = "";
			if(reportInForm.getDate() == null || reportInForm.getDate().equals("")){
				dd = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			}else{
				dd = reportInForm.getDate();
			}
			messages.add(dd+resources.getMessage(ss));
			request.setAttribute(Config.MESSAGES,messages);
			return mapping.findForward("view");	 
		}	 
		String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
		if(reportInForm.getDate() ==null || reportInForm.getDate().equals("")){
			reportInForm.setDate(yestoday.substring(0, 7));	
		if(reportInForm.getYear() == null || reportInForm.getYear().equals(""))
			reportInForm.setYear(new Integer(yestoday.substring(0,4)));	   
		if(reportInForm.getTerm() == null || reportInForm.getTerm().equals(""))			   
			reportInForm.setTerm(new Integer(yestoday.substring(5, 7)));
		}else{
			if(reportInForm.getYear() == null || reportInForm.getYear().equals(""))
				reportInForm.setYear(new Integer(reportInForm.getDate().split("-")[0]));
			if(reportInForm.getTerm() == null || reportInForm.getTerm().equals(""))			   
				reportInForm.setTerm(new Integer(reportInForm.getDate().split("-")[1]));
		}
		
/*		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		if(reportInForm.getYear() == null || reportInForm.getYear().equals(""))			   
			reportInForm.setYear(new Integer(calendar.get(Calendar.YEAR)));		   
		if(reportInForm.getTerm() == null || reportInForm.getTerm().equals(""))			   
			reportInForm.setTerm(new Integer(calendar.get(Calendar.MONTH)+1));
		*/
		try{
			/**取得当前用户的权限信息*/			
			if(session.getAttribute("multi") != null){
				messages = (FitechMessages)session.getAttribute("multi");
				session.setAttribute("multi",null);
			}
			
			Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			String childOrgIds=operator.getChildOrgIds();
			String sql=operator.getChildRepReportPopedom();
			/**如果机构名称为空，默认为用户本机构*/
			if (reportInForm.getOrgId()==null || reportInForm.getOrgId().equals("")){
				reportInForm.setOrgId(operator.getOrgId());
				reportInForm.setOrgName(operator.getOrgName());
			}
			list = new ArrayList();
			/**全部机构*/
			String opOrgIds=null;
			if (reportInForm.getOrgId().equals("-999")){
				opOrgIds=VorgCollectDelegate.getOrgIds(operator);
			}
			/**
			 * 单一机构
			 * */
			if (opOrgIds==null || opOrgIds.equals("")){
				opOrgIds=reportInForm.getOrgId();
			}
			String[] orgArr =opOrgIds.split(",");
			if (orgArr!=null && orgArr.length>0){
				AfOrg aforg=null;
				for(int i=0;i<orgArr.length;i++){
					aforg=AFOrgDelegate.getOrgInfo(orgArr[i]);
					/**
					 * 判断是否为虚拟机构
					 * **/
					if (aforg.getOrgType().equals("-99")){  
						/**oralce语法 需要修改 卞以刚 2011-12-22**/
						List aditingLst=VorgCollectDelegate.getVorgReport(orgArr[i], reportInForm.getYear().intValue(), reportInForm.getTerm().intValue());
						Aditing aditing=null;
						for(int j=0;j<aditingLst.size();j++){
							aditing=(Aditing)aditingLst.get(j);
							list.add(aditing);
						}
					}else{
						/**
						 * 真实机构
						 * **/
						reportInForm.setOrgId(orgArr[i]);
						/**
						 * 获取子机构，拼凑成字符串
						 * */
						List childOrg=AFOrgDelegate.getChildList(reportInForm.getOrgId());
						//AFOrgDelegate.getChildListByOrgId(reportInForm.getOrgId())
						childOrgIds="";
						if(childOrg!=null && childOrg.size()>0){
							
							for(int k=0;k<childOrg.size();k++){
								AfOrg org=(AfOrg)childOrg.get(k);
								if(childOrgIds!=null && !childOrgIds.equals("")){
									childOrgIds=childOrgIds+",'"+org.getOrgId()+"'";
								}else{
									childOrgIds="'"+org.getOrgId()+"'";
								}
							}
							
						}
						resList = StrutsCollectReportDelegate.selectAllCollectReports(reportInForm,operator);
			            
						if(resList!=null && resList.size()>0){
							DB2ExcelHandler handler = new DB2ExcelHandler();
							
							for(Iterator iter=resList.iterator();iter.hasNext();){
								Aditing aditing = (Aditing)iter.next();
								//查找该报表的关联报表
								CollectReal collectReal = StrutsCollectDelegate.getRealReportInfo(aditing.getChildRepId(),aditing.getVersionId(), aditing.getDataRgId(),aditing.getCurId());			
								if(collectReal==null ){
			//						messages.add("系统忙！请稍后再试!");
			//						if (messages.getMessages() != null && messages.getMessages().size() > 0)
			//							request.setAttribute(Config.MESSAGES, messages);
			//						request.setAttribute("year", year+"");
			//						request.setAttribute("mon", mon+"");
			//						return mapping.findForward("view");
								}else{
									String childRepArr[] = null;
									childRepArr =collectReal.getRealRepId().split(",");
									// 应报机构的数量
									int  needOrgCount = 0;
									// 实报机构数量
									int donum = 0 ;
									// 实报/应报字符串
									String doNeedStr = "";
									//得到应报机构列表
									if(childRepArr!=null && childRepArr.length>0){
										for(int j =0 ;j<childRepArr.length;j++){
											// 得到其中一张报表的报送机构数量
											needOrgCount = handler.getMustOrgCount(childRepArr[j],collectReal.getRealVerId(), childOrgIds);
											donum = handler.getAvailabilityOrgIdCount(childRepArr[j],collectReal.getRealVerId(),aditing.getDataRgId()
													,reportInForm.getYear().intValue(),reportInForm.getTerm().intValue(),aditing.getCurId(),childOrgIds);
											/**2013-11-01 LuYueFei 因为新增了拼接汇总的流程，因此对于"实报"的报表数做必要的处理*/
											donum = StrutsCollectDelegate.getJoinTemplateNum(donum,aditing,childOrgIds);
											doNeedStr +=childRepArr[j]+"["+donum+"/"+needOrgCount+"]" + "   ";
										}	
									}
									aditing.setDonum(new Integer(donum));
									aditing.setRepInFo(doNeedStr.trim());
									list.add(aditing);
								}
							}
						}
						if(list==null || list.size()<1 ) list = null;
					}
				}
			}
	   	}catch (Exception e){
			log.printStackTrace(e);
			messages.add("汇总数据浏览失败！");		
		}

	   	if(messages.getMessages() != null && messages.getMessages().size() > 0)
	   		request.setAttribute(Config.MESSAGES,messages);
	   	//if(resList!=null && resList.size()>0)
	   	if (list!=null && list.size()>0)
	   		request.setAttribute(Config.RECORDS,list);
	   	else
	   		request.setAttribute(Config.RECORDS,null);
	   	
	   	request.setAttribute("year", reportInForm.getYear().toString());
	   	request.setAttribute("term", reportInForm.getTerm().toString());
	   	return mapping.findForward("view");
	}
	/**
	 * 查询条件
	 * @param reportInForm ReportInForm
	 * 
	 * **/
	private String getTerm(ReportInForm reportInForm){
		String term=null;
		StringBuffer termSB=new StringBuffer();
		if (reportInForm.getChildRepId()!=null && !reportInForm.getChildRepId().equals(""))
			termSB.append(termSB.equals("")?"repInId="+reportInForm.getRepInId():"&repInId="+reportInForm.getRepInId());
		if (reportInForm.getRepName()!=null && !reportInForm.getRepName().equals(""))
			termSB.append(termSB.equals("")?"repName="+reportInForm.getRepName():"&repName="+reportInForm.getRepName());
		if (reportInForm.getOrgId()!=null && !reportInForm.getOrgId().equals(""))
			termSB.append(termSB.equals("")?"orgId="+reportInForm.getOrgId():"&orgId="+reportInForm.getOrgId());
		if (reportInForm.getOrgName()!=null && !reportInForm.getOrgName().equals(""))
			termSB.append(termSB.equals("")?"orgName="+reportInForm.getOrgName():"&orgName="+reportInForm.getOrgName());
		return term;
	}
}