package com.fitech.net.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsCollectTypeDelegate;
import com.fitech.net.form.CollectTypeForm;

/**
 * @ 汇总方式修改保存Action
 * @ author jcm
 *
 */
public final class UpdateCollectTypeAction extends Action {
	private FitechException log=new FitechException(UpdateCollectTypeAction.class);
	
	
   /**
    * @param result 查询返回标志,如果成功返回true,否则返回false
    * @param CollectTypeForm 
    * @param request 
    * @exception Exception 有异常捕捉并抛出
    */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
	)throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);
	   
		String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : "";
		//是否有Request
		CollectTypeForm collectTypeForm = (CollectTypeForm)form ;	   
		RequestUtils.populate(collectTypeForm, request);
			 
        try {
            if(collectTypeForm != null){
            	String failPath = "/collectType/editCollectType.do?orgId=" + collectTypeForm.getOrgId()+"&collectId=" + collectTypeForm.getCollectId();
            	
            	boolean bool = StrutsCollectTypeDelegate.isCollectTypeExist(collectTypeForm);            
            	if(bool){
            		messages.add(resources.getMessage("insert.collectType.exist"));	  
            		if (messages != null && messages.getSize() > 0)
                        request.setAttribute(Config.MESSAGES, messages);
            		request.setAttribute("collectName",collectTypeForm.getCollectName());
            		
            		return new ActionForward(failPath);
            	}
            	
            	bool = StrutsCollectTypeDelegate.remove(collectTypeForm);
            	if(bool){
            		String selectedCollectOrgs = collectTypeForm.getCollectOrgId();
                	String selectedCollectReport = collectTypeForm.getChildRepId();
                	
                	String[] scos = selectedCollectOrgs.split(",");
                	String[] scrs = selectedCollectReport.split(",");
                	            	
                	if(scos != null && scrs != null){
                		List collectTypeFormList = new ArrayList();
                		
                		for(int i=0;i<scos.length;i++){            			           		
                			String collectOrgId = scos[i];
                			for(int j=0;j<scrs.length;j++){
                				CollectTypeForm tempForm = new CollectTypeForm(); 
                				String[] repInfo = scrs[j].split("_");
                				tempForm.setChildRepId(repInfo[0]);
                				tempForm.setVersionId(repInfo[1]);
                				tempForm.setCollectId(collectTypeForm.getCollectId());
                				tempForm.setCollectOrgId(collectOrgId);
                				tempForm.setOrgId(collectTypeForm.getOrgId());
                				tempForm.setCollectName(collectTypeForm.getCollectName());
                				collectTypeFormList.add(tempForm);
                			}
                		}
                		
                		boolean result = StrutsCollectTypeDelegate.create(collectTypeFormList);
                		if(!result){            			
                			messages.add(resources.getMessage("update.collectType.failed"));	
                			if (messages != null && messages.getSize() > 0)
                	            request.setAttribute(Config.MESSAGES, messages);
                			request.setAttribute("collectName",collectTypeForm.getCollectName());
                			request.setAttribute("collectId",collectTypeForm.getCollectId());
                			
                			return new ActionForward(failPath);
                		}else
                			messages.add(resources.getMessage("update.collectType.success"));	
                	}else{            		
                		messages.add(resources.getMessage("update.collectType.failed"));	
                		if (messages != null && messages.getSize() > 0)
                            request.setAttribute(Config.MESSAGES, messages);
                		request.setAttribute("collectName",collectTypeForm.getCollectName());
                		request.setAttribute("collectId",collectTypeForm.getCollectId());
                		
                		return new ActionForward(failPath);
                	}    
            	}else{
            		messages.add(resources.getMessage("update.collectType.failed"));
            	}
            }else{
            	messages.add(resources.getMessage("update.collectType.failed"));	
            }
        }catch (Exception e){
            messages.add(resources.getMessage("update.collectType.failed"));		
            log.printStackTrace(e);
        }

        if (messages != null && messages.getSize() > 0)
            request.setAttribute(Config.MESSAGES, messages);
                
        String path = "/collectType/viewCollectType.do?orgId=&collectName=&curPage=" + curPage;

        return new ActionForward(path);
	}
}