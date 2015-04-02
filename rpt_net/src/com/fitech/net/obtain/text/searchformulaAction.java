package com.fitech.net.obtain.text;

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

import com.cbrc.smis.action.ViewMChildReportAction;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

/**
 

 */
public final class searchformulaAction extends Action {
	   private static FitechException log = new FitechException(ViewMChildReportAction.class);

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
		AddFormulaForm addFormulaForm = (AddFormulaForm) form;
	
		   RequestUtils.populate(addFormulaForm,request);
		   
		   int recordCount =0; //记录总数
	         int offset=0; //偏移量
	         int limit=0;  //每页显示的记录条数
		
	   List list=null;
	   
	   String child=addFormulaForm.getChildReportId();
	   String Reprotname=addFormulaForm.getReportname();
	   String version=addFormulaForm.getVersionId() ;
	   if(request.getParameter("version")!=null)
	   {
		   version=request.getParameter("version");
		   addFormulaForm.setVersionId(version);
	   }
	   if(request.getParameter("Rep")!=null)
	   {
		   Reprotname=request.getParameter("Rep");
		   addFormulaForm.setReportname(Reprotname);
	   }
	   // System.out.println(""+request.getParameter("version"));
// System.out.println(""+child);
// System.out.println(""+Reprotname);
// System.out.println(""+version);


try{
	 //        	取得记录总数
    recordCount = StructsSearchFormulaDelegate.getFormulaCount(child,version,Reprotname);
}catch(Exception ex){
	 log.printStackTrace(ex);
    messages.add(resources.getMessage("log.select.fail"));  
}
ApartPage aPage=new ApartPage();
aPage.setCount(recordCount);
String strCurPage=request.getParameter("curPage");
if(strCurPage!=null && !strCurPage.equals("")){
	 int curPage = 1;
	 try{
		 curPage = Integer.parseInt(strCurPage);
		 if(curPage > aPage.getPages())
			 curPage = aPage.getPages();
	 }catch(Exception ex){
		 curPage = 1;
	 }
	 aPage.setCurPage(curPage);
}
else
    aPage.setCurPage(1);
//计算偏移量
offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
limit = Config.PER_PAGE_ROWS;   


      try
      {
    	 
    	  list=StructsSearchFormulaDelegate.getFormulaList(child,version,Reprotname,offset,limit);
      }
     catch(Exception e)
     {
    	 log.printStackTrace(e);
         messages.add(resources.getMessage("log.select.fail")); 
     }
     FitechUtil.removeAttribute(mapping,request);
     if(list!=null && list.size()!=0)
         request.setAttribute("formulas",list);
    // aPage.setTerm(this.getTerm(addFormulaForm));
     request.setAttribute("term",this.getTerm(addFormulaForm));
     request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
     
     
       String forw=String.valueOf( request.getParameter("forword"));
       String message1=(String)request.getAttribute("message");
       if(message1!=null){
    	   
       ArrayList listm=new ArrayList();
       listm.add(message1);
       messages.setMessages(listm);
       request.setAttribute("Message",messages);
       }
       request.setAttribute("reportname",Reprotname);
       request.setAttribute("versionId",version);
    	       return mapping.findForward("getdata");
       
   }
	
	 public String getTerm(AddFormulaForm addFormulaForm)
	    {
	        String term="";
	        
	        String reportName = addFormulaForm.getReportname();
	              
	        if(reportName!=null)
	        {
	            term += (term.indexOf("?")>=0 ? "&" : "?");
	            term += "Rep="+reportName.toString();   
	        }
	        
	        String version=addFormulaForm.getVersionId();
	        if(version!=null)
	        {
	            term += (term.indexOf("?")>=0 ? "&" : "?");
	            term += "version="+version.toString();   
	        }
	        term="../../obtain/text/viewformula2.do"+term;
	            
	       
	        return term;
	        
	    }   
	    
}

