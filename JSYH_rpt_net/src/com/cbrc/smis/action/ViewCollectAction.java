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
 * oralce�﷨ ��Ҫ�޸� ���Ը� 2011-12-22
 * @ ���ݻ���ͳ��Action
 * @ author gen
 *
 */
public final class ViewCollectAction extends Action {
	private FitechException log=new FitechException(ViewCollectAction.class);
	
   /**
    * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
    * @param ReportInForm 
    * @param request 
    * @exception Exception ���쳣��׽���׳�
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
			/**ȡ�õ�ǰ�û���Ȩ����Ϣ*/			
			if(session.getAttribute("multi") != null){
				messages = (FitechMessages)session.getAttribute("multi");
				session.setAttribute("multi",null);
			}
			
			Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			String childOrgIds=operator.getChildOrgIds();
			String sql=operator.getChildRepReportPopedom();
			/**�����������Ϊ�գ�Ĭ��Ϊ�û�������*/
			if (reportInForm.getOrgId()==null || reportInForm.getOrgId().equals("")){
				reportInForm.setOrgId(operator.getOrgId());
				reportInForm.setOrgName(operator.getOrgName());
			}
			list = new ArrayList();
			/**ȫ������*/
			String opOrgIds=null;
			if (reportInForm.getOrgId().equals("-999")){
				opOrgIds=VorgCollectDelegate.getOrgIds(operator);
			}
			/**
			 * ��һ����
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
					 * �ж��Ƿ�Ϊ�������
					 * **/
					if (aforg.getOrgType().equals("-99")){  
						/**oralce�﷨ ��Ҫ�޸� ���Ը� 2011-12-22**/
						List aditingLst=VorgCollectDelegate.getVorgReport(orgArr[i], reportInForm.getYear().intValue(), reportInForm.getTerm().intValue());
						Aditing aditing=null;
						for(int j=0;j<aditingLst.size();j++){
							aditing=(Aditing)aditingLst.get(j);
							list.add(aditing);
						}
					}else{
						/**
						 * ��ʵ����
						 * **/
						reportInForm.setOrgId(orgArr[i]);
						/**
						 * ��ȡ�ӻ�����ƴ�ճ��ַ���
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
								//���Ҹñ���Ĺ�������
								CollectReal collectReal = StrutsCollectDelegate.getRealReportInfo(aditing.getChildRepId(),aditing.getVersionId(), aditing.getDataRgId(),aditing.getCurId());			
								if(collectReal==null ){
			//						messages.add("ϵͳæ�����Ժ�����!");
			//						if (messages.getMessages() != null && messages.getMessages().size() > 0)
			//							request.setAttribute(Config.MESSAGES, messages);
			//						request.setAttribute("year", year+"");
			//						request.setAttribute("mon", mon+"");
			//						return mapping.findForward("view");
								}else{
									String childRepArr[] = null;
									childRepArr =collectReal.getRealRepId().split(",");
									// Ӧ������������
									int  needOrgCount = 0;
									// ʵ����������
									int donum = 0 ;
									// ʵ��/Ӧ���ַ���
									String doNeedStr = "";
									//�õ�Ӧ�������б�
									if(childRepArr!=null && childRepArr.length>0){
										for(int j =0 ;j<childRepArr.length;j++){
											// �õ�����һ�ű���ı��ͻ�������
											needOrgCount = handler.getMustOrgCount(childRepArr[j],collectReal.getRealVerId(), childOrgIds);
											donum = handler.getAvailabilityOrgIdCount(childRepArr[j],collectReal.getRealVerId(),aditing.getDataRgId()
													,reportInForm.getYear().intValue(),reportInForm.getTerm().intValue(),aditing.getCurId(),childOrgIds);
											/**2013-11-01 LuYueFei ��Ϊ������ƴ�ӻ��ܵ����̣���˶���"ʵ��"�ı���������Ҫ�Ĵ���*/
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
			messages.add("�����������ʧ�ܣ�");		
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
	 * ��ѯ����
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