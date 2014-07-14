package com.fitech.net.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import com.cbrc.smis.adapter.StrutsOrgDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsJobLogDelegate;
import com.fitech.net.hibernate.JobLog;
import com.fitech.net.hibernate.JobLogKey;
import com.fitech.net.hibernate.OrgNet;
/**
*
* <p>标题: JobLogMgrAction</p>
*
* <p>描述: ETL数据监控查询，修改请求的DispatchAction </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2007-11-15
* @version 1.0
* @version 1.1 2008-02-20 修改调用分页查询 ETL数据监控日志的方法
*/


public class JobLogMgrAction extends DispatchAction 
{
    private FitechException log=new FitechException(JobLogMgrAction.class);
    
    /**
     * 响应用户查询ETL数据提取情况的请求导航至/collectReport/monitorETL.jsp
     * @param mapping       ActionMapping
     * @param form          ActionForm
     * @param request       HttpServletRequest
     * @param response      HttpServletResponse
     * @return  ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward findJob(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException
    {
        //获取查询请求的参数
        String orgId = request.getParameter("orgId");
        String year = request.getParameter("year");
        String term = request.getParameter("term");
        String repName = request.getParameter("repName");
        String jobSts = request.getParameter("jobSts");
        String curPage=request.getParameter("curPage");
        String urlTerm = "";
        //构造JobLog对象
       JobLog job = new JobLog();
       JobLogKey id = new JobLogKey();
        
        // JobLog对象集合和所属子机构集合      
        List jobLogLst=null;
        List orgLst = null;
        ApartPage apartPage=new ApartPage();        
        
        if(StringUtils.isNotEmpty(curPage))
        {
            apartPage.setCurPage(Integer.parseInt(curPage));
        }         
        if(StringUtils.isNotEmpty(year))
        {
            id.setYear(new Integer(year));
            urlTerm += "&year=" + year;
        }
        if(StringUtils.isNotEmpty(term))
        {
            id.setTerm(new Integer(term));
            urlTerm +="&term=" + term;
        }
        if(StringUtils.isNotEmpty(repName))
        {
            job.setRepName(repName);
            urlTerm +="&repName=" + repName;
        }
        if(StringUtils.isNotEmpty(jobSts))
        {
            job.setJobSts(jobSts);
            urlTerm +="&jobSts=" + jobSts;
        }
        if(StringUtils.isNotEmpty(orgId))
        {
            OrgNet org = new OrgNet();
            org.setOrgId(orgId);
            id.setOrg(org);
            urlTerm += "&orgId=" + orgId;
        }
        //设置供页面修改ETL数据提取状态的URL
        request.setAttribute("modUrl",urlTerm +"&curPage=" + apartPage.getCurPage());
        if(StringUtils.isNotEmpty(urlTerm))
        {
            urlTerm = urlTerm.substring(1);
        }
        apartPage.setTerm(urlTerm);
        
        job.setId(id);
        //获取所属的下级机构
        if(request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
        {
            Operator operator = (Operator)request.getSession()
            .getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
            orgLst = StrutsOrgDelegate.findSubOrgs(operator.getOrgId());
            request.setAttribute("orgLst",orgLst); 
        }                
        //获取所属的下级机构ETL数据提取情况
        //2008-02-20修改方法为 findJobLogByHql
        jobLogLst = StrutsJobLogDelegate.findJobLogByHql(job,orgLst,apartPage);     
        if(jobLogLst!=null && !jobLogLst.isEmpty())
        {      
            request.setAttribute("jobLogLst",jobLogLst);     
        }           
           
        request.setAttribute(Config.APART_PAGE_OBJECT,apartPage);
        return mapping.findForward("jobMgr_find_job");          
    }
    /**
     * 响应用户进入查询ETL数据提取情况页面的请求，
     * 导航至/collectReport/monitorETL.jsp
     * @param mapping       ActionMapping
     * @param form          ActionForm
     * @param request       HttpServletRequest
     * @param response      HttpServletResponse
     * @return  ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward toFindJob(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException
    {
        //获取所属的下级机构集合
        if(request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
        {
            Operator operator = (Operator)request.getSession()
            .getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
            List orgLst = StrutsOrgDelegate.findSubOrgs(operator.getOrgId());
            request.setAttribute("orgLst",orgLst); 
        }                
        return  mapping.findForward("jobMgr_to_find"); 
    }
    
    /**
     * 响应用户修改ETL数据提取状态的请求
     * 导航至/collectReport/monitorETL.jsp
     * @param mapping       ActionMapping
     * @param form          ActionForm
     * @param request       HttpServletRequest
     * @param response      HttpServletResponse
     * @return  ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward updateJob(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException
    {
        FitechMessages messages = new FitechMessages();    
        MessageResources resources = getResources(request); 
        Locale locale = request.getLocale();
        //从request请求中获取修改JobLog对象的参数
        String sQuery = request.getQueryString();
        if(sQuery.indexOf("&") != -1 || sQuery.indexOf("-") != -1)
        {
            sQuery = sQuery.substring(sQuery.indexOf("&"),sQuery.lastIndexOf("-"));
            String arrQuery[] = sQuery.split("-");
            int length = arrQuery.length;
         
            List jobLogLst = new ArrayList();
            for (int i = 0; i < length; i++)
            {
                String paramValue[] = arrQuery[i].split("&");
                JobLog job = new JobLog();
                JobLogKey id = new JobLogKey();
                job.setJobLog("人工修改状态");
                //循环取出参数  形式： &sRepId=SF5100
                for (int j = 0; j < paramValue.length; j++)
                {         
                    String temp = paramValue[j];
                    if(temp.startsWith("sRepId") && temp.split("=").length == 2)
                    {         
                        id.setRepId(temp.substring(temp.indexOf("=")+1));
                    }
                    else  if(temp.startsWith("sVersionId") && temp.split("=").length == 2)
                    {
                        id.setVersionId(temp.substring(temp.indexOf("=")+1));
                    }
                    else  if(temp.startsWith("sYear") && temp.split("=").length == 2)
                    {
                        id.setYear(new Integer(temp.substring(temp.indexOf("=")+1)));
                    }
                    else  if(temp.startsWith("sTerm") && temp.split("=").length == 2)
                    {
                        id.setTerm(new Integer(temp.substring(temp.indexOf("=")+1)));
                    }
                    else if(temp.startsWith("sOrgId") && temp.split("=").length == 2)
                    {
                        OrgNet org = new OrgNet();
                        org.setOrgId(temp.substring(temp.indexOf("=") + 1));
                        id.setOrg(org);
                    }
                    else if(temp.startsWith("sDataRangId") && temp.split("=").length == 2)
                    {
                    	MDataRgType dataRange=new MDataRgType();
                    	dataRange.setDataRangeId(new Integer(temp.substring(temp.indexOf("=") + 1)));
                        id.setDataRange(dataRange);
                    }
                    else if(temp.startsWith("sCurId") && temp.split("=").length == 2)
                    {
                    	MCurr cur=new MCurr();
                       cur.setCurId(new Integer(temp.substring(temp.indexOf("=") + 1)));
                        id.setCur(cur);
                    }
                    else if(temp.indexOf("sJobSts") != -1)
                    {
                        job.setJobSts(temp.substring(temp.indexOf("=") + 1));
                    }   
                      
                }
                job.setId(id);
                jobLogLst.add(job);
            }
            //修改Job的状态
            if(StrutsJobLogDelegate.updateJobSts(jobLogLst))
                messages.add(resources.getMessage(locale,"etl.update.success"));
            else
                messages.add(resources.getMessage(locale,"etl.update.failed"));    
            request.setAttribute("Message",messages);
        }
        else
        {
            messages.add(resources.getMessage(locale,"etl.update.failed"));    
        }
        return findJob(mapping,form,request,response);
    }
    
    /**
     * 响应用户查看ETL数据提取状态为ERROR的请求
     * 导航至/collectReport/view_error.jsp
     * @param mapping       ActionMapping
     * @param form          ActionForm
     * @param request       HttpServletRequest
     * @param response      HttpServletResponse
     * @return  ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward viewError(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException
    {
        System.out.println("request------------->");
        String orgId = request.getParameter("sOrgId");
        String year = request.getParameter("sYear");
        String term = request.getParameter("sTerm");
        String repId = request.getParameter("sRepId");
        String versionId = request.getParameter("sVersionId");
        System.out.println(orgId+year+term+repId+versionId);
//      构造JobLog对象
        JobLog job = new JobLog();
        JobLogKey id = new JobLogKey();
                     
         if(StringUtils.isNotEmpty(year))
         {
             id.setYear(new Integer(year));
         }
         if(StringUtils.isNotEmpty(term))
         {
             id.setTerm(new Integer(term));
         }       
      
         if(StringUtils.isNotEmpty(orgId))
         {
             OrgNet org = new OrgNet();
             org.setOrgId(orgId);
             id.setOrg(org);
         }
         if(StringUtils.isNotEmpty(repId))
         {
             id.setRepId(repId);
         }
         if(StringUtils.isNotEmpty(versionId))
         {
             id.setVersionId(versionId);
         }
         job.setId(id);
         job = StrutsJobLogDelegate.findJobLogById(job);     
         if(job!=null)
         {      
             request.setAttribute("jobLog",job);     
         }         
         return mapping.findForward("jobMgr_view_error");
    }
}
