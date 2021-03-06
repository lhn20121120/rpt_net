package com.fitech.net.action;

import java.io.IOException;
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

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

/**
 * 查询数据维护的模板信息
 *
 * @author jcm
 */
public final class ViewProtectTemplateAction extends Action {
    private static FitechException log = new FitechException(ViewProtectTemplateAction.class);
   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		   HttpServletResponse response) throws IOException, ServletException { 
       MessageResources resources = getResources(request);
       FitechMessages messages = new FitechMessages();
       
       MChildReportForm mChildReportForm = (MChildReportForm) form;        
       RequestUtils.populate(mChildReportForm, request);
    
        
       int recordCount =0; //记录总数       
       int offset=0; //偏移量       
       int limit=0;  //每页显示的记录条数       
       List list = null;
                
       ApartPage aPage=new ApartPage();       
       String strCurPage=getParameter(request,"curPage");
       
       if(strCurPage!=null){       
    	   if(!strCurPage.equals(""))           
    		   aPage.setCurPage(new Integer(strCurPage).intValue());            
    	   else           
    		   aPage.setCurPage(1);        
       }else
    	   aPage.setCurPage(1);
        //计算偏移量
        offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
        limit = Config.PER_PAGE_ROWS;   
        
        try{
        	//取得记录总数
            recordCount = StrutsMChildReportDelegate.getMChildReportCount(mChildReportForm);
            //显示分页后的记录
            if(recordCount > 0){
                list = StrutsMChildReportDelegate.selectMChildReport(mChildReportForm,offset,limit);
            }
        }catch (Exception e){
            log.printStackTrace(e);
            messages.add("数据关系维护查询失败！");      
        }
        //移除request或session范围内的属性
        //FitechUtil.removeAttribute(mapping,request);
        //把ApartPage对象存放在request范围内
        aPage.setTerm(this.getTerm(mChildReportForm));
        aPage.setCount(recordCount);
        request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
        
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
          request.setAttribute(Config.MESSAGES,messages);
        if(list!=null && list.size()>0){
          request.setAttribute(Config.RECORDS,list);
        }
        //将查询条件存放在request范围内
        request.setAttribute("form",mChildReportForm);            
            
        return mapping.findForward("view");
   }
   
   /**
    * 获取参数的值
    * 
    * @param req HttpServletRequest 
    * @param name String 
    * @return String 
    */
   private String getParameter(HttpServletRequest req, String name) {
	   String value = req.getParameter(name);
	   if (value == null) {
		   if(req.getAttribute(name)!=null)
			   value = req.getAttribute(name).toString();
		   else
			   return null;
	   }
	   return value;
   }
   
   /**
    * 取得查询条件url  
    * @param logInForm
    * @return
    */
   public String getTerm(MChildReportForm mChildReportForm)
   {
       String term="";
       
       String reportName = mChildReportForm.getReportName();
       String versionId = mChildReportForm.getVersionId();
       
       if(reportName!=null)
       {
           term += (term.indexOf("?")>=0 ? "&" : "?");
           term += "reportName="+reportName.toString();   
       }
       if(versionId!=null && !versionId.equals(""))
       {
           term += (term.indexOf("?")>=0 ? "&" : "?");
           term += "versionId="+versionId.toString();   
       }
       if(term.indexOf("?")>=0)
           term = term.substring(term.indexOf("?")+1);
       
       return term;       
   }
}
