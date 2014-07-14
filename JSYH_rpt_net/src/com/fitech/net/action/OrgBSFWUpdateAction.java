package com.fitech.net.action;

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

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.service.StrutsTemplateOrgRelationDelegate;
/**
 * ��һ��jdbc ������Ҫ�޸� ���Ը� 2011-12-22
 * ����ģ����䱣��
 * @author Admin
 *
 */
public final class OrgBSFWUpdateAction extends Action {
    private static FitechException log = new FitechException(OrgBSFWUpdateAction.class); 

    public ActionForward execute(
       ActionMapping mapping,
       ActionForm form,
       HttpServletRequest request,
       HttpServletResponse response
    )
       throws IOException, ServletException {

        MessageResources resources=getResources(request);
        FitechMessages messages = new FitechMessages();
        Locale locale = request.getLocale();
        String curPage=request.getParameter("curPage");
        MRepRangeForm mRepRangeForm = (MRepRangeForm) form;
        RequestUtils.populate(mRepRangeForm, request);
              
        String orgId = mRepRangeForm.getOrgId();      
        String selectRepIds = mRepRangeForm.getSelectRepIds();       
        
        try{
        	/**��ʹ��hibernate ���Ը� 2011-12-22**/
        	int mRepRangeCount = StrutsMRepRangeDelegate.getMRepRangeByOrg(orgId);
        	/**��ʹ��hibernate ���Ը� 2011-12-22**/
        	int afRepRangeCount = StrutsTemplateOrgRelationDelegate.getRepRangeCountByOrg(orgId);
            if(mRepRangeCount>0 || afRepRangeCount>0){
            	boolean deleteResult =  true;
            	if(mRepRangeCount>0)
            		/**jdbc ������Ҫ�޸� ���Ը� 2011-12-22
            		 * ������oracle ����Ҫ�޸� ���Ը� 2011-12-28**/
            		deleteResult = StrutsMRepRangeDelegate.deleteMRepRangeByOrg(orgId);
            	if(afRepRangeCount>0 && deleteResult)
            		/**��ʹ��hibernate ���Ը� 2011-12-22**/
            		deleteResult = StrutsTemplateOrgRelationDelegate.deleteRepRangeByOrg(orgId);
            	if(deleteResult==false){
            		messages.add(FitechResource.getMessage(locale,resources,"BSFW.update.failed"));                   
            		request.setAttribute(Config.MESSAGES,messages);    

            		return new ActionForward("/showOrgBSFW.do?org_id="+orgId);                          
            	}
            }
            /**����û���ѡ��ı�����Ϣ*/
            boolean saveResult=false;
            if(selectRepIds!=null && selectRepIds.equals("")){
            	    /** saveResult=""  ��ʾ����˱��ͷ�Χ  */
            		saveResult=true;
            }else{
            	/**��ʹ��hibernate ���Ը� 2011-12-22**/
	             saveResult = StrutsMRepRangeDelegate.insertMRepRange(orgId,selectRepIds);    
            }
            if(saveResult==false){            
            	messages.add(FitechResource.getMessage(locale,resources,"BSFW.update.failed"));
                request.setAttribute(Config.MESSAGES,messages);
                

                return new ActionForward("/showOrgBSFW.do?org_id="+orgId);             
            }else
                messages.add(FitechResource.getMessage(locale,resources,"BSFW.update.success"));  
        }catch (Exception e){
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"BSFW.update.failed"));
            request.setAttribute(Config.MESSAGES,messages);

            return new ActionForward("/showOrgBSFW.do?org_id="+orgId);
        }
      
        if(messages.getMessages() != null && messages.getMessages().size() > 0)           
        	request.setAttribute(Config.MESSAGES,messages);
        	

        return new ActionForward("/system_mgr/OrgInfo/view.do");
    }    
}
