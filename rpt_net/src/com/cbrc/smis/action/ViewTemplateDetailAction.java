//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.0.1/xslt/JavaClass.xsl

package com.cbrc.smis.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.action.SystemSchemaBaseAction;

/** 
 * 模板详细信息显示
 * @author 姚捷
 * Creation date: 12-15-2005
 * 
 * XDoclet definition:
 * @struts.action path="/template/viewTemplateDetail" name="mChildReportForm" scope="request" validate="true"
 * @struts.action-forward name="templateDetailView" path="/template/view/templateDetailView.jsp"
 */
public class ViewTemplateDetailAction extends SystemSchemaBaseAction {
    private static FitechException log = new FitechException(ViewTemplateDetailAction.class);

    /** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)  
    throws IOException, ServletException
   {
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        
        MChildReportForm mChildReportForm = new MChildReportForm();
        RequestUtils.populate(mChildReportForm,request);
        
        String type = request.getParameter("type");
        
        String isDelTemplateGroup = request.getParameter("isDelTemplateGroup");
        
        if(type!=null && "del".equals(type)){
        	
        	StrutsMChildReportDelegate.delTemplate(mChildReportForm.getChildRepId(), 
        			mChildReportForm.getVersionId(),isDelTemplateGroup);
        	
        	ActionForward forward = new ActionForward("/template/viewTemplate.do");
        	forward.setRedirect(true);
        	
        	return forward;
        }
         int recordCount =0; //记录总数
         int offset=0; //偏移量
         int limit=0;  //每页显示的记录条数
         List list = null;
         
         String reportName=mChildReportForm.getReportName();
         if(reportName==null )
         {
         	if(mChildReportForm.getChildRepId()!=null)
         	{
         		
         	reportName=StrutsMChildReportDelegate.getname(mChildReportForm.getChildRepId(),mChildReportForm.getVersionId());
         	mChildReportForm.setReportName(reportName);
         	
         	}
         }
        
        //	 request.setAttribute("ReportName",new String(mChildReportForm.getReportName().getBytes("8859_1"),"GBK"));
         
         
         ApartPage aPage=new ApartPage();
         String strCurPage=request.getParameter("curPage");
         if(strCurPage!=null){
             if(!strCurPage.equals(""))
               aPage.setCurPage(new Integer(strCurPage).intValue());
         }
         else
             aPage.setCurPage(1);
         //计算偏移量
         offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
         limit = Config.PER_PAGE_ROWS;   
         
         try 

         {
             //取得记录总数
             recordCount = StrutsMChildReportDelegate.getReporstByChildRepIdCount(mChildReportForm);
             //显示分页后的记录
             if(recordCount > 0)
                 list = StrutsMChildReportDelegate.getReporstByChildRepId(mChildReportForm,offset,limit);  
         }
         catch (Exception e) 
         {
             log.printStackTrace(e);
             messages.add(resources.getMessage("log.select.fail"));      
         }
         //移除request或session范围内的属性
         FitechUtil.removeAttribute(mapping,request);
         //把ApartPage对象存放在request范围内
         aPage.setTerm(this.getTerm(mChildReportForm));
         aPage.setCount(recordCount);
         request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
         
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         if(list!=null && list.size()!=0)
           request.setAttribute(Config.RECORDS,list);
         //将查询条件存放在request范围内
         
         //request.setAttribute("ReportName",new String(mChildReportForm.getReportName().getBytes("8859_1"),"GBK"));
         request.setAttribute("ReportName",mChildReportForm.getReportName());
         if(mChildReportForm.getBak2()!=null && mChildReportForm.getBak2().equals("2")){
        	 request.getSession().setAttribute("mChildReportForm", mChildReportForm);
             request.setAttribute("template_manage_flag",Config.TEMPLATE_MANAGE_FLAG);
        	 return mapping.findForward("templateDetailEdit");
         }
         return mapping.findForward("templateDetailView");
    }
    /**
     * 取得查询条件url  
     * @param logInForm
     * @return
     */
    public String getTerm(MChildReportForm mChildReportForm)
    {
        String term="";
        String childRepId = mChildReportForm.getChildRepId();
              
        if(childRepId!=null)
        {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "childRepId="+childRepId.toString();   
        }
        if(term.indexOf("?")>=0)
            term = term.substring(term.indexOf("?")+1);
            
        return term;
        
    }   
 }
