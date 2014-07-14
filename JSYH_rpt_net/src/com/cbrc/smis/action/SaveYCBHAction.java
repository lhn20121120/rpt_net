package com.cbrc.smis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsAbnormityChangeDelegate;
import com.cbrc.smis.adapter.StrutsColAbnormityChangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.AbnormityChangeForm;
import com.cbrc.smis.form.ColAbnormityChangeForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;

/**
 * �����쳣�仯�趨���������
 * 
 * @author rds
 * @serialData 2005-12-11 14:15
 */
public class SaveYCBHAction extends Action {
	private FitechException log=new FitechException(AddYCBHInitAction.class);
	
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
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		
		HttpSession session=request.getSession();
		
		FitechMessages messages = new FitechMessages();
		
		AbnormityChangeForm abnormityChangeForm=(AbnormityChangeForm)form;
		RequestUtils.populate(abnormityChangeForm,request);
		
		/*// System.out.println("standard:" + abnormityChangeForm.getStandard());
		// System.out.println("OrgCls:" + abnormityChangeForm.getOrgCls());*/
		
		boolean flag=false;
		
		try{
			if(abnormityChangeForm.getStandard()==null){ //��ȡ�쳣�仯�趨��׼ʧ��
				messages.add(FitechResource.getMessage(locale,resources,"ycbh.get.standard.failed"));
			}else if(abnormityChangeForm.getOrgCls()==null && session.getAttribute("SelectedOrgIds")==null){ //��ȡ�쳣�仯�趨��׼���õĻ�������ʧ��
				messages.add(FitechResource.getMessage(locale,resources,"ycbh.get.orgCls.failed"));
			}else if(abnormityChangeForm.getReportStyle()==null ||
					abnormityChangeForm.getChildRepId()==null ||
					abnormityChangeForm.getVersionId()==null){  //��ȡ��Ҫ�趨�쳣�仯��׼�ı�����Ϣʧ��
				messages.add(FitechResource.getMessage(locale,resources,"ycbh.get.report.failed"));
			}else{
				List orgList=getOrgList(abnormityChangeForm.getOrgCls(),session);
				List forms=getACFormList(abnormityChangeForm.getStandard(),
						abnormityChangeForm.getChildRepId(),
						abnormityChangeForm.getVersionId(),
						abnormityChangeForm.getReportStyle());
				
				//// System.out.println("ǰ"+orgList.size());
				//ȥ��û�и�������
				orgList=StrutsOrgNetDelegate.selectAllLowerOrgListBBB(orgList,((Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)).getOrgId());
			//	// System.out.println("��"+orgList.size());
				
				/*// System.out.println("orgList.size:" + orgList.size());
				// System.out.println("forms.size:"  + forms.size());*/
				if(abnormityChangeForm.getReportStyle().compareTo(Config.REPORT_STYLE_DD)==0){ //��Ե�
					flag=StrutsAbnormityChangeDelegate.savePatch(orgList,forms);
				}else if(abnormityChangeForm.getReportStyle().compareTo(Config.REPORT_STYLE_QD)==0){ //�嵥
					flag=StrutsColAbnormityChangeDelegate.savePatch(orgList,forms);
				}
				
				if(flag==true){ //�����ɹ�
					messages.add(FitechResource.getMessage(locale,resources,"ycbh.save.success"));
				}else{ //����ʧ��
					messages.add(FitechResource.getMessage(locale,resources,"ycbh.save.failed"));
				}
			}
		}catch(Exception e){ //ϵͳ����
			log.printStackTrace(e);
			messages.add(FitechResource.getMessage(locale,resources,"errors.system"));
			return mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE);
		}
		
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
		
		request.setAttribute("ObjForm",abnormityChangeForm);
		
		request.setAttribute("ChildRepId",abnormityChangeForm.getChildRepId());
		request.setAttribute("VersionId",abnormityChangeForm.getVersionId());
		request.setAttribute("ReportStyle",abnormityChangeForm.getReportStyle());
		request.setAttribute("ReportName",abnormityChangeForm.getReportName());
		
		if(flag==true){	 //��ʼ���ɹ�,ת�뱨����ҳ��
			return mapping.findForward("bpfbSet");
		}else{  //��ʼ��ʧ�ܣ�ת��ϵͳ����ҳ��
			return mapping.getInputForward();
		}
	}
	
	/**
	 * ��ȡ�쳣�仯�趨��׼���õĻ����б�,<br>
	 * 
	 * @param orgCls String �������ʹ�
	 * @param session HttpSession 
	 * @return List ���û�����õĻ����б���Ϣ������null
	 * @exception exception
	 */
	public List getOrgList(String orgCls,HttpSession session) throws Exception{
		
		if(orgCls==null && session==null) {return null;}
		
		List orgList=new ArrayList();
				
		
		
		
		if(session.getAttribute("SelectedOrgIds")!=null){
			HashMap hMap=(HashMap)session.getAttribute("SelectedOrgIds");
			
			//����orgcls�еĻ���
			String OrgIds[]=orgCls.split(",");
			
			List lowerOrgList=StrutsOrgNetDelegate.selectLowerOrgList(((Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)).getOrgId(),session);
			for(int i=0;i<lowerOrgList.size();i++)
			{
				
				hMap.remove(((OrgNet)lowerOrgList.get(i)).getOrgId());
			}
			if(OrgIds!=null)
			{
				 for(int i=0;i<OrgIds.length;i++)
		         {
					 if(OrgIds[i].trim()!=null && !OrgIds[i].trim().equals(">>"))
					 {
		             hMap.put(OrgIds[i].trim(),"ok");
					 
					 }
		         }
			}
			
			if(hMap!=null){
				Iterator it=hMap.keySet().iterator();
				while(it.hasNext()){						
                    orgList.add(StrutsOrgNetDelegate.selectOne((String)it.next()));
				}
			}
			session.setAttribute("SelectedOrgIds",null);  //���Session�е�ѡ�еĻ�����Ϣ�б�
		}
		
//		List list=null;
//		if(orgCls!=null && !orgCls.equals("")) list=com.cbrc.org.util.MOrgUtil.getOrgs(orgCls);
//		if(list!=null && list.size()>0){
//			for(int i=0;i<list.size();i++){
//				MOrgForm mOrgForm=(MOrgForm)list.get(i);
//				/*// System.out.println("orgId:" + mOrgForm.getOrgId());
//				// System.out.println("orgName:" + mOrgForm.getOrgName());*/
//				//orgList.add(mOrgForm.getOrgId());
//				orgList.add(mOrgForm);
//			}
//		}
//		list=null;
//		
		return orgList;
	}
	
	/**
	 * �����趨���쳣�仯����AbnormityChangeForm�б�
	 * 
	 * @param standard String 
	 * @param childRepId String �ӱ���ID
	 * @param versionId String �汾��
	 * @param reportstyle Integer �������
	 * @return List
	 * @exception Exception
	 */
	public List getACFormList(String standard,String childRepId,String versionId,Integer reportStyle) throws Exception{
		List acFormList=null;
		
		if(standard==null || standard.trim().equals("")) { System.out.println("standard is null");return acFormList;}
		// System.out.println(standard);
		String arr[]=standard.split(Config.SPLIT_SYMBOL_ESP);
		if(arr!=null && arr.length>0){
			acFormList=new ArrayList();
			for(int i=0;i<arr.length;i++){
				String item[]=arr[i].split(Config.SPLIT_SYMBOL_COMMA);
				
				if(item.length<2) break;
				if(reportStyle.compareTo(Config.REPORT_STYLE_DD)==0){
					AbnormityChangeForm form=new AbnormityChangeForm();
					form.setCellId(Integer.valueOf(item[0]));
					form.setPrevRiseStandard(item.length>1?Float.valueOf(item[1]):new Float(0.0));
					form.setPrevFallStandard(item.length>2?Float.valueOf(item[2]):new Float(0.0));
					form.setSameRiseStandard(item.length>3?Float.valueOf(item[3]):new Float(0.0));
					form.setSameFallStandard(item.length>4?Float.valueOf(item[4]):new Float(0.0));
					form.setChildRepId(childRepId);
					form.setVersionId(versionId);
					acFormList.add(form);
				}else if(reportStyle.compareTo(Config.REPORT_STYLE_QD)==0){
					ColAbnormityChangeForm form=new ColAbnormityChangeForm();
					form.setColName(item[0]);
					form.setPrevRiseStandard(item.length>1?Float.valueOf(item[1]):new Float(0.0));
					form.setPrevFallStandard(item.length>2?Float.valueOf(item[2]):new Float(0.0));
					form.setSameRiseStandard(item.length>3?Float.valueOf(item[3]):new Float(0.0));
					form.setSameFallStandard(item.length>4?Float.valueOf(item[4]):new Float(0.0));
					form.setChildRepId(childRepId);
					form.setVersionId(versionId);
					acFormList.add(form);
				}
			}
		}
		
		return acFormList;
	}
}
