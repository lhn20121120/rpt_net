package com.cbrc.auth.action;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.auth.adapter.StrutsToolSettingDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;

/**
 * �˵���Ϣ�鿴
 *
 * @author gujie 
 *
 * @struts.action
 *    path="/struts/viewToolSetting"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewToolSetting.jsp"
 *    redirect="false"
 *

 */
public final class ViewToolSettingAction extends Action {
    private static FitechException log = new FitechException(ViewRoleAction.class);
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
	   MessageResources resources = getResources(request);
       FitechMessages messages = new FitechMessages();
       Locale locale = request.getLocale();
       
       int recordCount =0; //��¼����
       int offset=0; //ƫ����
       int limit=0;  //ÿҳ��ʾ�ļ�¼����
       List list = null;
       
       ApartPage aPage=new ApartPage();
       String strCurPage=request.getParameter("curPage");
       
       if(strCurPage!=null && !strCurPage.equals(""))
       {
           aPage.setCurPage(new Integer(strCurPage).intValue());
       }
       else
           aPage.setCurPage(1);
       //����ƫ����
       offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
       limit = Config.PER_PAGE_ROWS;   
       
       try 
       {
           //ȡ�ü�¼����
           recordCount = StrutsToolSettingDelegate.getRecordCount();
           //// System.out.println("List size is" + recordCount);
           //��ʾ��ҳ��ļ�¼
           if(recordCount > 0)
               list = StrutsToolSettingDelegate.select(offset,limit); 
       }
       catch (Exception e) 
       {
           log.printStackTrace(e);
           messages.add(FitechResource.getMessage(locale,resources,"select.fail","toolSetting.info"));      
       }
       //�Ƴ�request��session��Χ�ڵ�����
       FitechUtil.removeAttribute(mapping,request);
       //��ApartPage��������request��Χ��
       aPage.setCount(recordCount);
       request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
       
       if(messages.getMessages() != null && messages.getMessages().size() > 0)
         request.setAttribute(Config.MESSAGES,messages);
       if(list!=null && list.size()!=0)
          request.setAttribute(Config.RECORDS,list);
	   return mapping.findForward("menu_mgr");
      
   }
}
