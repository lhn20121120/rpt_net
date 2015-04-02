package com.fitech.net.template.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.system.cb.InputData;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

public class GroupUpLoadReportAction  extends Action {
	private static FitechException log = new FitechException(GroupUpLoadReportAction.class);

	/** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
						HttpServletRequest request,HttpServletResponse response)  throws IOException, ServletException{
	        
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
	    
		HttpSession session=request.getSession();
        /**获取当前用户*/
        Operator operator=(Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
		InputData inputData = new InputData();        		
		Calendar calendar = Calendar.getInstance();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	String date = format.format(calendar.getTime());
    	
        String repInIds = request.getParameter("repInIds");        
        if(repInIds != null && !repInIds.equals("")){
        	try{
        		String[] repInIdsArr = repInIds.split(",");
        		if(repInIdsArr != null && repInIdsArr.length > 0){
        			for(int i=0;i<repInIdsArr.length;i++){
        				ReportIn reportIn = StrutsReportInDelegate.getReportIn2(new Integer(repInIdsArr[i]));
        				boolean bool = StrutsReportInDelegate.updateReportIn(reportIn,date);
        				if(bool){
        					inputData.bnValidate(reportIn.getRepInId(),operator.getOperatorName());                    		
                    	}
        			}
        			messages.add(resources.getMessage("select.uponlineReport.success")); 
        		}else{
            		messages.add(resources.getMessage("select.uponlineReport.failed")); 
            	}
            }catch(Exception ex){
           	 	log.printStackTrace(ex);
                messages.add(resources.getMessage("select.uponlineReport.failed"));  
            }
                    
            if(messages.getMessages() != null && messages.getMessages().size() > 0)
              request.setAttribute(Config.MESSAGES,messages);
            
            return new ActionForward("/viewOnLineSJBS.do");
        }else{
        	try{
        		ReportInForm reportInForm = new ReportInForm();
            	try{
            		reportInForm.setYear(request.getParameter("year") != null ? new Integer(request.getParameter("year")) : null);
            	}catch(Exception ex){
            		reportInForm.setTerm(null);
            	}
            	try{
            		reportInForm.setTerm(request.getParameter("term") != null ? new Integer(request.getParameter("term")) : null);
            	}catch(Exception ex){
            		reportInForm.setTerm(null);
            	}
            	reportInForm.setRepName(request.getParameter("repName"));
            	reportInForm.setOrgId(operator.getOrgId());
            	
            	List ybList = StrutsReportInDelegate.getOnLineYBRecords(reportInForm,operator);
            	if(ybList != null && ybList.size() > 0){
            		for(int i=0;i<ybList.size();i++){
            			Aditing aditing = (Aditing)ybList.get(i);
            			ReportIn reportIn = StrutsReportInDelegate.getReportIn2(aditing.getRepInId());
        				boolean bool = StrutsReportInDelegate.updateReportIn(reportIn,date);
        				if(bool){
        					inputData.bnValidate(reportIn.getRepInId(),operator.getOperatorName());                    		
                    	}
        			}
        			messages.add(resources.getMessage("select.uponlineReport.success")); 
            	}else{
            		messages.add(resources.getMessage("select.uponlineReport.failed")); 
            	}
            }catch(Exception ex){
           	 	log.printStackTrace(ex);
                messages.add(resources.getMessage("select.uponlineReport.failed"));  
            }
                    
            if(messages.getMessages() != null && messages.getMessages().size() > 0)
              request.setAttribute(Config.MESSAGES,messages);
            
            return new ActionForward("/viewOnLineSJBS.do");
        } 
	}  	         
}



