package com.cbrc.org.action;

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

import com.cbrc.org.adapter.StrutsMOrgDelegate;
import com.cbrc.org.form.MOrgClForm;
import com.cbrc.org.form.MOrgForm;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * 报表的填报范围设定操作类
 *
 * @author 唐磊
 */
public final class UpdateMOrgAction extends Action {
	private FitechException log=new FitechException(InsertMOrgAction.class);
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
      throws IOException, ServletException {  Locale locale = getLocale(request);
      MessageResources resources = getResources(request);
      FitechMessages messages = new FitechMessages();

      boolean result = false;

      MOrgClForm mOrgForm = (MOrgClForm) form;
      RequestUtils.populate(mOrgForm, request);
      
      try 
      {
          
          if (mOrgForm != null) 
          {
              /**删除以前的填报范围*/
              boolean deleteResult = StrutsMRepRangeDelegate.removeOne(mOrgForm.getVersionId(), mOrgForm.getChildRepId());
              if(deleteResult == false)
              {
                  messages.add(FitechResource.getMessage(locale,resources,"tbfw.save.failed"));
              }
              else
              {
                  String[] selectOrgClsIds = mOrgForm.getSelectOrgClsIds();
                  //// System.out.println("orgClssweLwengsd===="+selectOrgClsIds.length);
                  String orgCls = "";
                  if (selectOrgClsIds != null && selectOrgClsIds.length > 0) 
                  {
                     for (int i = 0; i < selectOrgClsIds.length; i++) 
                     {
                         //// System.out.println("orgClsswe42323===="+selectOrgClsIds[i]);
                          orgCls += (orgCls.equals("") ? "" : ",") + "'"
                                  + selectOrgClsIds[i] + "'";
                     }
                  }
                  // List orgIds = MOrgUtil.getOrgs(orgCls);
                  List orgIds = this.getOrgList(orgCls, request.getSession());
    
                  if (orgIds != null && orgIds.size() != 0) 
                  {
                      MOrgForm _mOrgForm = null;
                      for (int j = 0; j < orgIds.size(); j++) 
                      {
                          MRepRangeForm mRepRangeForm = new MRepRangeForm();
                          _mOrgForm = (MOrgForm) orgIds.get(j);
                          mRepRangeForm.setOrgId(_mOrgForm.getOrgId());
                          mRepRangeForm.setVersionId(mOrgForm.getVersionId());
                          mRepRangeForm.setChildRepId(mOrgForm.getChildRepId());
                          mRepRangeForm.setOrgClsId(_mOrgForm.getOrgClsId());
    
                          if (StrutsMRepRangeDelegate.create(mRepRangeForm) != true) 
                          {
                              result = false;
                              break;
                          }
                          result = true;
                      }
                      
                  }
    
                  if (result == true) 
                  {
                      messages.add(FitechResource.getMessage(locale, resources,
                              "tbfw.save.success"));
                      StrutsMChildReportDelegate.setFlag(
                              mOrgForm.getChildRepId(), mOrgForm.getVersionId(),
                              StrutsMChildReportDelegate.REP_RANGE_FLAG,
                              Config.SET_FLAG);
                  }
                  else 
                  {
                      messages.add(FitechResource.getMessage(locale, resources,
                              "tbfw.save.failed"));
                  }
              }
          }
          else
          {
              messages.add(FitechResource.getMessage(locale, resources,
              "tbfw.save.failed"));
              result = false;
          }
      } 
      catch (Exception e) 
      {
          log.printStackTrace(e);
          /*messages.add(FitechResource.getMessage(locale, resources,
                  "errors.system"));
          return mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE);*/
          messages.add(FitechResource.getMessage(locale, resources,
          "tbfw.save.failed"));
          result = false;
      }

      if (messages != null && messages.getSize() > 0)
          request.setAttribute(Config.MESSAGES, messages);

      String path = "/template/viewMChildReport.do";
      if (result == true)
      { // 操作成功，返回频度设定页
          path = mapping.findForward("main").getPath();
      } 
      else { // 操作失败，返回原页面
          path = mapping.findForward("view").getPath();
      }

      String url = "";
      if (mOrgForm.getChildRepId() != null)
          url += (url.equals("") ? "" : "&") + "childRepId="
                  + mOrgForm.getChildRepId();
      if (mOrgForm.getVersionId() != null)
          url += (url.equals("") ? "" : "&") + "versionId="
                  + mOrgForm.getVersionId();
      if (mOrgForm.getReportName() != null)
          url += (url.equals("") ? "" : "&") + "reportName="
                  + mOrgForm.getReportName();

      return new ActionForward(path + (url.equals("") ? "" : "?" + url));
  }

  /**
   * 获取填报范围设定的机构列表,<br>
   * 
   * @param orgCls
   *            String 机构类型串
   * @param session
   *            HttpSession
   * @return List 如果没有适用的机构列表信息，返回null
   * @exception exception
   */
  public List getOrgList(String orgCls, HttpSession session) throws Exception {
      if (orgCls == null && session == null)
          return null;
      //// System.out.println("orgCls===="+orgCls);
      List orgList = new ArrayList();

      if (session.getAttribute("SelectedOrgIds") != null) {
          HashMap hMap = (HashMap) session.getAttribute("SelectedOrgIds");

          if (hMap != null)
          {
              Iterator it = hMap.keySet().iterator();
              while (it.hasNext()) 
              {
                  try
                  {
                      MOrgForm mOrgForm = StrutsMOrgDelegate.getMOrg((String)it.next());
                      if(mOrgForm!=null)
                          orgList.add(mOrgForm);
                  }
                  catch(Exception e)
                  {
                      e.printStackTrace();
                      return null;
                  }
              }
          }
          session.removeAttribute("SelectedOrgIds"); // 清空Session中的选中的机构信息列表
      }

      List list = null;
      if (orgCls != null && !orgCls.equals(""))
          list = com.cbrc.org.util.MOrgUtil.getOrgs(orgCls);
      if (list != null && list.size() > 0) 
      {
          for (int i = 0; i < list.size(); i++) 
          {
              MOrgForm mOrgForm = (MOrgForm) list.get(i);
              orgList.add(mOrgForm);
          }
      }
      list = null;

      return orgList;
  }
}

/*	   Locale locale = getLocale(request);
	   MessageResources resources = getResources(request);
	   FitechMessages messages = new FitechMessages();
	   
	   boolean result=false;
	   
	   MOrgClForm mOrgForm = (MOrgClForm)form;	 
	   RequestUtils.populate(mOrgForm,request);
	   
       try {
    	  if (mOrgForm!=null){
    		  String[] selectOrgClsIds = mOrgForm.getSelectOrgClsIds();
    		  if (selectOrgClsIds!=null && selectOrgClsIds.length>0){
    			      			 
    			  boolean removeResult=StrutsMRepRangeDelegate.removeOne(mOrgForm.getVersionId(), mOrgForm.getChildRepId());
    			  if(removeResult!=true){
    				  return mapping.findForward("update");
    			  }    			  
    			  
    			  for(int i=0;i<selectOrgClsIds.length;i++){
    				  MRepRangeForm mRepRangeForm = new MRepRangeForm();
    				  mRepRangeForm.setOrgId(StrutsMOrgDelegate.findOrgIdByOrgClsId(mOrgForm.getSelectOrgClsIds()[i]));
    				  mRepRangeForm.setVersionId(mOrgForm.getVersionId());
    				  mRepRangeForm.setChildRepId(mOrgForm.getChildRepId());
    				  mRepRangeForm.setOrgClsId(mOrgForm.getSelectOrgClsIds()[i]);
						
    				  if (StrutsMRepRangeDelegate.create(mRepRangeForm) != true) {
						result = false;
						break;
    				  }
    				  
    				  StrutsMChildReportDelegate.setFlag(
 								mRepRangeForm.getChildRepId(),
 								mRepRangeForm.getVersionId(),
 								StrutsMChildReportDelegate.REP_RANGE_FLAG,
 								Config.SET_FLAG);
    				  
    				  result = true;
	    		  }    		 
	    		  
	    		  if(result==true){
	    			  messages.add(FitechResource.getMessage(locale,resources,"BSFW.update.success"));
	    		  }else{
	    			  messages.add(FitechResource.getMessage(locale,resources,"BSFW.update.failed"));	    			  
	    		  }
    		  }else{
    			  messages.add(FitechResource.getMessage(locale,resources,"BSFW.update.failed"));
    		  }
    	 }
      }catch (Exception e) {
        log.printStackTrace(e);
        messages.add(FitechResource.getMessage(locale,resources,"errors.system"));
		return mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE);
      }
      
      if(messages!=null && messages.getSize()>0)
    	  request.setAttribute(Config.MESSAGES,messages);
      
      String curPage=FitechUtil.getRequestParameter(request,"curPage");
      
      return new ActionForward("/template/viewMChildReport.do" + (curPage!=null?"?curPage=" + curPage:""));
   	}
}


*/