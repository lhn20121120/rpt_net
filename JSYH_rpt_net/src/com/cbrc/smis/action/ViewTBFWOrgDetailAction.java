//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.0.1/xslt/JavaClass.xsl

package com.cbrc.smis.action;

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

import com.cbrc.org.adapter.StrutsMOrgDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

/** 
 * 模板填报范围机构详细信息查看
 * @author 姚捷

 * @struts.action path="/viewTBFWOrgDetail" name="mRepRangeForm" scope="request" validate="true"
 * @struts.action-forward name="view" path="/template/view/tbfw_orgDetail.jsp"
 */
public class ViewTBFWOrgDetailAction extends Action {
    private static FitechException log = new FitechException(ViewTBFWOrgDetailAction.class);
    // --------------------------------------------------------- Instance Variables

    // --------------------------------------------------------- Methods

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
        HttpServletResponse response)  throws IOException, ServletException 
    {

        FitechMessages messages = new FitechMessages();
        MessageResources resources = getResources(request);
        
        MRepRangeForm mRepRangeForm = new MRepRangeForm();
        RequestUtils.populate(mRepRangeForm,request);
     
        int recordCount =0; //记录总数
        int offset=0; //偏移量
        int limit=0;  //每页显示的记录条数
        List result = null;
        
        ApartPage aPage=new ApartPage();
        String strCurPage=request.getParameter("curPage");
        if(strCurPage!=null)
        {
            if(!strCurPage.equals(""))
              aPage.setCurPage(new Integer(strCurPage).intValue());
        }
        else
            aPage.setCurPage(1);
        //计算偏移量
        offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
        limit = Config.PER_PAGE_ROWS;   
        
        String childRepId = mRepRangeForm.getChildRepId();
        String versionId = mRepRangeForm.getVersionId();
        String orgClsId = mRepRangeForm.getOrgClsId();
        String orgClsName = mRepRangeForm.getOrgClsName();
        String reportName = mRepRangeForm.getReportName();
        if(reportName==null )
        {
        	if(childRepId!=null)
        	{
        		
        	reportName=StrutsMChildReportDelegate.getname(childRepId,versionId);
        	mRepRangeForm.setReportName(reportName);
        	}
        }
        
        try 
        {
            //取得记录总数
            recordCount = StrutsMRepRangeDelegate.getOrgIdsCount(childRepId,versionId,orgClsId);
            
            //显示分页后的记录
            if(recordCount > 0)
            {
                String orgIds = StrutsMRepRangeDelegate.getOrgIds(childRepId,versionId,orgClsId,offset,limit);
                result = StrutsMOrgDelegate.getOrgInfoByOrgIdString(orgIds);
               //// System.out.println("Record Count == "+result.size());
            }
            
            
        }
        catch (Exception e) 
        {
            log.printStackTrace(e);
            messages.add(resources.getMessage("BSFW.select.failed"));      
        }
        //移除request或session范围内的属性
        FitechUtil.removeAttribute(mapping,request);
        //把ApartPage对象存放在request范围内
        aPage.setTerm(this.getTerm(mRepRangeForm));
        aPage.setCount(recordCount);
        
        request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
        
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
          request.setAttribute(Config.MESSAGES,messages);
        
        if(result!=null && result.size()>0)
        {
            request.setAttribute(Config.RECORDS,result);
        }
        //// System.out.println("ReportName ==="+ reportName);
        request.setAttribute("OrgClsName",orgClsName);
        request.setAttribute("ReportName",reportName);
        request.setAttribute("ChildRepId",childRepId);
        request.setAttribute("VersionId",versionId);
        //request.setAttribute("OrgClsName",orgClsName);
        
        return mapping.findForward("view");
     }
    /**
     * 取得查询条件url  
     * @param logInForm
     * @return
     */
    public String getTerm(MRepRangeForm mRepRangeForm)
    {
       String term="";
       String childRepId = mRepRangeForm.getChildRepId();
       String versionId = mRepRangeForm.getVersionId();
       String orgClsId = mRepRangeForm.getOrgClsId();
       String reportName = mRepRangeForm.getReportName();
       String orgClsName = mRepRangeForm.getOrgClsName();
       
       if(childRepId!=null)
       {
           term += (term.indexOf("?")>=0 ? "&" : "?");
           term += "childRepId="+childRepId.toString();   
       }
       if(versionId!=null && !versionId.equals(""))
       {
           term += (term.indexOf("?")>=0 ? "&" : "?");
           term += "versionId="+versionId.toString();   
       }
       if(orgClsId!=null && !orgClsId.equals(""))
       {
           term += (term.indexOf("?")>=0 ? "&" : "?");
           term += "orgClsId="+orgClsId.toString();   
       }
       if(reportName!=null && !reportName.equals(""))
       {
           term += (term.indexOf("?")>=0 ? "&" : "?");
           term += "reportName="+reportName.toString();   
           
       }
       if(orgClsName!=null && !orgClsName.equals(""))
       {
           term += (term.indexOf("?")>=0 ? "&" : "?");
           term += "orgClsName="+orgClsName.toString();   
           
       }
       if(term.indexOf("?")>=0)
           term = term.substring(term.indexOf("?")+1);
           
 //      // System.out.println("term"+term);
       return term;
       
    }

}

