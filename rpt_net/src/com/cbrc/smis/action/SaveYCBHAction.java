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
 * 报表异常变化设定保存操作类
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
			if(abnormityChangeForm.getStandard()==null){ //获取异常变化设定标准失败
				messages.add(FitechResource.getMessage(locale,resources,"ycbh.get.standard.failed"));
			}else if(abnormityChangeForm.getOrgCls()==null && session.getAttribute("SelectedOrgIds")==null){ //获取异常变化设定标准适用的机构类型失败
				messages.add(FitechResource.getMessage(locale,resources,"ycbh.get.orgCls.failed"));
			}else if(abnormityChangeForm.getReportStyle()==null ||
					abnormityChangeForm.getChildRepId()==null ||
					abnormityChangeForm.getVersionId()==null){  //获取需要设定异常变化标准的报表信息失败
				messages.add(FitechResource.getMessage(locale,resources,"ycbh.get.report.failed"));
			}else{
				List orgList=getOrgList(abnormityChangeForm.getOrgCls(),session);
				List forms=getACFormList(abnormityChangeForm.getStandard(),
						abnormityChangeForm.getChildRepId(),
						abnormityChangeForm.getVersionId(),
						abnormityChangeForm.getReportStyle());
				
				//// System.out.println("前"+orgList.size());
				//去除没有父机构的
				orgList=StrutsOrgNetDelegate.selectAllLowerOrgListBBB(orgList,((Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)).getOrgId());
			//	// System.out.println("后"+orgList.size());
				
				/*// System.out.println("orgList.size:" + orgList.size());
				// System.out.println("forms.size:"  + forms.size());*/
				if(abnormityChangeForm.getReportStyle().compareTo(Config.REPORT_STYLE_DD)==0){ //点对点
					flag=StrutsAbnormityChangeDelegate.savePatch(orgList,forms);
				}else if(abnormityChangeForm.getReportStyle().compareTo(Config.REPORT_STYLE_QD)==0){ //清单
					flag=StrutsColAbnormityChangeDelegate.savePatch(orgList,forms);
				}
				
				if(flag==true){ //操作成功
					messages.add(FitechResource.getMessage(locale,resources,"ycbh.save.success"));
				}else{ //操作失败
					messages.add(FitechResource.getMessage(locale,resources,"ycbh.save.failed"));
				}
			}
		}catch(Exception e){ //系统错误
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
		
		if(flag==true){	 //初始化成功,转入报表发布页面
			return mapping.findForward("bpfbSet");
		}else{  //初始化失败，转入系统错误页面
			return mapping.getInputForward();
		}
	}
	
	/**
	 * 获取异常变化设定标准适用的机构列表,<br>
	 * 
	 * @param orgCls String 机构类型串
	 * @param session HttpSession 
	 * @return List 如果没有适用的机构列表信息，返回null
	 * @exception exception
	 */
	public List getOrgList(String orgCls,HttpSession session) throws Exception{
		
		if(orgCls==null && session==null) {return null;}
		
		List orgList=new ArrayList();
				
		
		
		
		if(session.getAttribute("SelectedOrgIds")!=null){
			HashMap hMap=(HashMap)session.getAttribute("SelectedOrgIds");
			
			//处理orgcls中的机构
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
			session.setAttribute("SelectedOrgIds",null);  //清空Session中的选中的机构信息列表
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
	 * 根据设定的异常变化返回AbnormityChangeForm列表
	 * 
	 * @param standard String 
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号
	 * @param reportstyle Integer 报表类别
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
