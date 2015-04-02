package com.cbrc.smis.action;

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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MRepTypeForm;
import com.cbrc.smis.other.InnerToGather;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * �������͵�ɾ��Action 
 *
 * @author ����
 *
 * @struts.action
 *    path="/struts/deleteMRepType"
 *
 * @struts.action-forward
 *    name="all"
 *    path="/struts/getAll.do"
 *    redirect="false"
 *

 */
public final class DeleteMRepTypeAction extends Action {
	private static FitechException log = new FitechException(DeleteMRepTypeAction.class);
   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException �Ƿ���IO�쳣��������׽���׳�
    * @exception ServletException �Ƿ���Servlet�쳣��������׽���׳�
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {

	   Locale locale=getLocale(request);
	   MessageResources resources=this.getResources(request);
	   FitechMessages messages=new FitechMessages();
	   
	   MRepTypeForm mRepTypeForm = new MRepTypeForm();
     	
     // ��request�����mCurUnitForm 
     RequestUtils.populate(mRepTypeForm, request);
     

		try
        {
			boolean result = com.cbrc.smis.adapter.StrutsMRepTypeDelegate
					.remove(mRepTypeForm);
			if (result == true) 
            {
               
				boolean deleteRepTypeFormGather = InnerToGather
						.removeMRepType(mRepTypeForm);
				if (deleteRepTypeFormGather == true) 
                {
				    /**����ɾ���ɹ�*/
                    String msg =FitechResource.getMessage(locale, resources,
                            "delete.success", "repType.msg", mRepTypeForm
                            .getRepTypeId().toString());
                    FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,msg);
				}
               /**����ɾ��ʧ��*/
                else 
                {
                    String msg =FitechResource.getMessage(locale, resources,
                            "delete.success", "repType.msg", mRepTypeForm
                            .getRepTypeId().toString());
                    FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,msg);
				}
                 messages.add(FitechResource.getMessage(locale, resources,
                            "delete.success", "repType.info"));
			}
            else
                messages.add(FitechResource.getMessage(locale, resources,
                        "delete.failed", "repType.info"));
		}
        catch (Exception e)
        {
			log.printStackTrace(e);
             messages.add(FitechResource.getMessage(locale, resources,
                     "delete.failed", "repType.info"));
        }

        if(messages.getMessages() != null && messages.getMessages().size() != 0)
               request.setAttribute(Config.MESSAGES,messages);
        String path="/config/ViewRepType.do";
	   
	  /* Integer repTypeId = mRepTypeForm.getRepTypeId();
	   String repTypeName = mRepTypeForm.getRepTypeName();
	   
	   if(repTypeId!=null)
	   {
		   path += (path.indexOf("?")>=0 ? "&" : "?");
		   path += "repTypeId="+repTypeId.toString();   
	   }
	   if(repTypeName!=null && !repTypeName.equals(""))
	   {
		   path += (path.indexOf("?")>=0 ? "&" : "?");
		   path += "repTypeName="+repTypeName.toString();   
	   }
	  
	   // System.out.println("Path= "+path);*/
	   return new ActionForward(path);
}

   
   /**
    * Get the named value as a string from the request parameter, or from the
    * request obj if not found.
    * 
    * @param req The request object.
    * @param name The name of the parameter.
    *
    * @return The value of the parameter as a String.
    */
   private String getParameter(HttpServletRequest req, String name) {
      String value = req.getParameter(name);
      if (value == null) {
         value = (String)req.getAttribute(name);
      }
      return value;
   }
}
