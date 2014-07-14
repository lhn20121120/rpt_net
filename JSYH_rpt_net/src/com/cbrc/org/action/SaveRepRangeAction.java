package com.cbrc.org.action;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;



/**
 * ģ�飺���Χ�������������ϸ��
 * ��ѡ�еĻ���id���д���������ظ�ǰ̨
 *�������ҳ�����м�¼
 * @author ����
 *
 * @struts.action
 *    path="/template/operationOrgType"
 *
 * @struts.action-forward
 *    name="all"
 *    path="/struts/getAll.do"
 *    redirect="false"
 *

 */
public final class SaveRepRangeAction extends Action {
    private static FitechException log = new FitechException(OperationOrgTypeAction.class); 
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
	   Locale locale=getLocale(request);
       FitechMessages messages = new FitechMessages();
       MessageResources resources = getResources(request);
       MRepRangeForm mRepRangeForm=(MRepRangeForm)form;
      
       RequestUtils.populate(mRepRangeForm,request);
       
       if(mRepRangeForm!=null&& !mRepRangeForm.equals("")){
    	   try{
    		   //ɾ����ǰ���еĸû����������ļ�¼
    		   boolean resultRemove=StrutsMRepRangeDelegate.removeOfMRepRange(mRepRangeForm);
    		   if(resultRemove==true){
    			   //����ɹ��򷵻�true��ִ��ҳ���û��Ĳ������,����false
    			   boolean resultInsert=StrutsMRepRangeDelegate.InsertOfMRepRange(mRepRangeForm);
    			   
    			   if (resultInsert == true){	//���³ɹ�
   						messages.add(FitechResource.getMessage(locale,resources,"BSFW.update.success"));
   						StrutsMChildReportDelegate.setFlag(
   								mRepRangeForm.getChildRepId(),
   								mRepRangeForm.getVersionId(),
   								StrutsMChildReportDelegate.REP_RANGE_FLAG,
   								Config.SET_FLAG);
   					}else{						//����ʧ��
   						messages.add(FitechResource.getMessage(locale,resources,"BSFW.update.failed"));
   						return mapping.findForward("save");
   					}
    		   }
    	   }catch(Exception e){
    		   log.printStackTrace(e);
    		   messages.add(FitechResource.getMessage(locale, resources,"errors.system"));
    	   }
    	}
       
       if(messages.getMessages() != null && messages.getMessages().size() > 0) request.setAttribute(Config.MESSAGES,messages);
       
       String curPage=FitechUtil.getRequestParameter(request,"curPage");
       
       return new ActionForward("/template/viewMChildReport.do" + (curPage!=null?"?curPage=" + curPage:""));
   }
  
}
