package com.fitech.gznx.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.action.UpdateMActuRepAction;
import com.cbrc.smis.adapter.StrutsMUserGroupDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.AbnormityChangeForm;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.form.UpdateBSFWForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.StrutsAFActuRepDelegate;

public class InsertAFTemRepAction extends Action {
	private static FitechException log = new FitechException(UpdateMActuRepAction.class);
	
	   /**
	    * Performs action.
	    * @param mapping Action mapping.
	    * @param form Action form.
	    * @param request HTTP request.
	    * @param response HTTP response.
	    * @exception IOException if an input/output error occurs
	    * @exception ServletException if a servlet exception occurs
	    */
	   public ActionForward execute(
	      ActionMapping mapping,
	      ActionForm form,
	      HttpServletRequest request,
	      HttpServletResponse response
	   )
	      throws IOException, ServletException {
		   MessageResources resources=getResources(request);
			  FitechMessages messages = new FitechMessages();
			  //取得request范围内的请求参数，并存放在logInForm内
			  MActuRepForm mActuRepForm = new MActuRepForm();
			  RequestUtils.populate(mActuRepForm, request);
			  
			  String childRepId = mActuRepForm.getChildRepId();
			  String versionId = mActuRepForm.getVersionId();
			  Integer reportStyle=mActuRepForm.getReportStyle();
			  String reportName=mActuRepForm.getReportName();
			  UpdateBSFWForm bsfwForm = new UpdateBSFWForm();
			  bsfwForm.setReportName(reportName);
			  try 
			  {	  
				  boolean result = StrutsAFActuRepDelegate.create(mActuRepForm);
				  if(result){
					  result= AFTemplateDelegate.updateUsingFlag(childRepId,versionId,1); 
				  }
				  if(result == false)//保存失败则返回原页面
				  {
					  messages.add(resources.getMessage("template.bspl.fail"));  
					  request.setAttribute(Config.MESSAGES,messages);
					  request.setAttribute("ChildRepID",childRepId);
					  request.setAttribute("VersionID",versionId);
					  request.setAttribute("ReportStyle",reportStyle);
					  request.setAttribute("ReportName",reportName);
					  request.setAttribute("ObjForm",bsfwForm);
					  return mapping.getInputForward();
				  }
		          else //保存成功则去改变机构表中的频度设定标志
		          {
		             // StrutsMChildReportDelegate.setFlag(childRepId,versionId,StrutsMChildReportDelegate.ACTU_REP_FLAG,Config.SET_FLAG);
		              try {
						StrutsMUserGroupDelegate.processMPurBanklevelPow(childRepId);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
		        	  AbnormityChangeForm abnormityChangeForm =new AbnormityChangeForm();
		        	  abnormityChangeForm.setChildRepId(childRepId);
		        	  abnormityChangeForm.setVersionId(versionId);
		        	  abnormityChangeForm.setReportStyle(reportStyle);
		        	  abnormityChangeForm.setReportName(reportName);
		        	  request.setAttribute("ObjForm",abnormityChangeForm);
		          }
			  }
			  catch (Exception e) 
			  {
				  log.printStackTrace(e);
				  messages.add(resources.getMessage("template.bspl.fail"));  
				  request.setAttribute(Config.MESSAGES,messages);
				  request.setAttribute("ChildRepID",childRepId);
				  request.setAttribute("VersionID",versionId);
				  request.setAttribute("ReportStyle",reportStyle);
				  request.setAttribute("ReportName",reportName);
				  request.setAttribute("ObjForm",bsfwForm);
				  return mapping.getInputForward();	
			  }

			  //移除request范围内的信息
			  FitechUtil.removeAttribute(mapping,request);
			  //在request范围内存放信息
			  //messages.add(resources.getMessage("template.bspl.success"));
			  messages.add("报表并表范围及报送频度时间设定保存成功！");

			  if(messages.getMessages() != null && messages.getMessages().size() > 0)
				  request.setAttribute(Config.MESSAGES,messages);

			  String path=mapping.findForward("ycbhSet").getPath();
			//  String path=mapping.findForward("bspl").getPath();
			  
			  String url="";
//		      if(mActuRepForm.getChildRepId()!=null) url+=(url.equals("")?"":"&") + "childRepId=" + mActuRepForm.getChildRepId();
//			  if(mActuRepForm.getVersionId()!=null) url+=(url.equals("")?"":"&") + "versionId=" + mActuRepForm.getVersionId();
//			  if(mActuRepForm.getReportName()!=null) url+=(url.equals("")?"":"&") + "reportName=" + mActuRepForm.getReportName();
//			  if(mActuRepForm.getReportStyle()!=null) url+=(url.equals("")?"":"&") + "&reportStyle=" + mActuRepForm.getReportStyle();
			   url+=(url.equals("")?"":"&") + "childRepId=" + mActuRepForm.getChildRepId();
			   url+=(url.equals("")?"":"&") + "versionId=" + mActuRepForm.getVersionId();
			   url+=(url.equals("")?"":"&") + "ReportName=" + mActuRepForm.getReportName();
			   url+=(url.equals("")?"":"&") + "&reportStyle=" + mActuRepForm.getReportStyle();
			  request.setAttribute("ReportName", mActuRepForm.getReportName());
		      //return new ActionForward(path + (url.equals("")?"":"?" + url));  

			 return mapping.findForward("addtemplate");	 
			  // return new ActionForward("/template/add/bspl.jsp?childRepId=" + childRepId + "&versionId=" + versionId);
	   }
}
