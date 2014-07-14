package com.cbrc.smis.action;

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

import com.cbrc.smis.adapter.StrutsMActuRepDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

/**
 * 报表并表范围及报送频度更新设定
 *
 * @author rds
 * @serialData 2005-12-22 14:37
 */
public final class UpdateMActuRepAction extends Action {
   private static FitechException log = new FitechException(UpdateMActuRepAction.class); 
   
   /**
    * 已使用hibernate 卞以刚 2011-12-22
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
	  
	  boolean flag=false;
	  
	  try {	  
		  /**已使用hibernate 卞以刚 2011-12-22**/
		  boolean result = StrutsMActuRepDelegate.update(mActuRepForm);
		  if(result == false){  //保存失败则返回原页面
			 
			  messages.add("保存失败"); 
		  }else{  //保存成功则去改变机构表中的频度设定标志
			  /**已使用hibernate 卞以刚 2011-12-22**/
              StrutsMChildReportDelegate.setFlag(childRepId,versionId,
                          StrutsMChildReportDelegate.ACTU_REP_FLAG,Config.SET_FLAG);
              messages.add("更新成功");
              flag=true;
          }
	  }catch (Exception e){
		  log.printStackTrace(e);
		  messages.add(resources.getMessage("bspl.update.failed")); 
	  }

	  //移除request范围内的信息
	  FitechUtil.removeAttribute(mapping,request);

	  if(messages.getMessages() != null && messages.getMessages().size() > 0)
		  request.setAttribute(Config.MESSAGES,messages);
	  
	  String path="";
	  if(flag==true){
		  path=mapping.findForward("view").getPath();
	  }else{
		  path=mapping.getInputForward().getPath();
		  
		  request.setAttribute("ChildRepID",childRepId);
		  request.setAttribute("VersionID",versionId);
		  request.setAttribute("ReportStyle",reportStyle);
		  request.setAttribute("ReportName",reportName);
		  if(request.getParameter("curPage")!=null)
			  request.setAttribute("curPage",request.getParameter("curPage"));
		  
		  return new ActionForward(path);
	  }
	  
	  path = "/template/viewTemplateDetail.do?childRepId="+childRepId+"&versionId="+versionId+"&bak2=2";
	  
	  if(request.getParameter("curPage")!=null) {
		  request.setAttribute("curPage",request.getParameter("curPage"));
		  path = path + "&curPage=" + request.getParameter("curPage");
	  }
	  return new ActionForward(path);
   }
}
