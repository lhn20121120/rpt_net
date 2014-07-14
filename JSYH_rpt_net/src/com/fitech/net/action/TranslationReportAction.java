package com.fitech.net.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.excel.DB2Excel;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsCollectDelegate;
import com.fitech.net.adapter.StrutsReportInDelegate;
import com.fitech.net.config.Config;


/**
*
* <p>Title: TranslationReportAction </p>
*
* <p>Description 汇总数据转上报数据Action</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2009-09-14
* @version 1.0
*
*/
public final class TranslationReportAction extends Action {
	
   /**
    * @param    mapping         ActionMapping
    * @param    form            ActionForm
    * @param    request         HttpServletRequest
    * @param    response        HttpServletResponse
    * IOEception  有异常捕捉并抛出
    * SeverletException
    */
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request
			,HttpServletResponse response)throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();
		ReportInForm reportInForm = (ReportInForm)form ;	   
		RequestUtils.populate(reportInForm, request);
        
     //下段注释的内容为备用方法，如果下方代码不能完成功能需求可启用此段代码调试
     /* String childRepId = null;
        String versionId  = null;
        String reportName = null;
        String dataRangeId = null;
        String dataRangeDesc = null;
        String year = null;
        String month = null;
        String curId = null;
        //汇总上报情况主键
        CollectReportPK key = new CollectReportPK();
       
        //内网表单表实际子报表Id
         if(request.getParameter("childRepId") != null){
            childRepId = request.getParameter("childRepId");
            key.setChildRepId(childRepId);
         }
         //版本号
        if(request.getParameter("versionId") != null){
            versionId  = request.getParameter("versionId");
            key.setVersionId(versionId);
        }
        //报表名
        if(request.getParameter("reportName") != null)
            reportName = request.getParameter("reportName");
        //数据范围Id
        if(request.getParameter("dataRangeId") != null)
            dataRangeId = request.getParameter("dataRangeId");
        //数据范围描述
        if(request.getParameter("dataRanageDesc") != null)
            dataRangeDesc = request.getParameter("dataRanageDesc");
        //报表年份
        if(request.getParameter("year") != null){
            year = request.getParameter("year");
            key.setYear(new Integer(year));
        }
        //报表期数
        if(request.getParameter("month") != null){
            month = request.getParameter("month");
            key.setTerm(new Integer(month));
        }
        //货币Id
        if(request.getParameter("curId") != null)
            curId = request.getParameter("curId");*/
       
		/** 取得当前用户的权限信息 */
		HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		
        Integer repInId = reportInForm.getRepInId();       
    //    if(operator != null)
    //         key.setOrgId(operator.getOrgId());
        
        //根据汇总情况表是否有对应的本期的已汇总的数据判断是否转上报数据
//        CollectReport report = StrutsCollectReportDelegate.findById(key);
        
        //if(report == null){
         //   messages.add("没有汇总数据，不能转上报数据！");
       // 根据汇总情况表是否有对应的本期的已汇总的数据判断是否转上报数据
        if(repInId == null){
                messages.add("没有汇总数据，不能转上报数据！");
        }else{
            //ReportIn reportIn = StrutsReportInDelegate.findById(report.getRepInId());
            //查找有无已转上报数据的记录
            ReportIn _reportIn = StrutsReportInDelegate.findHasTranslation(repInId,operator.getOrgId());
            if(_reportIn != null && Config.CHECK_FLAG_PASS.equals(_reportIn.getCheckFlag())){
                messages.add("该期报表已审核，不可转上报数据！");
            }else if(_reportIn != null && Config.CHECK_FLAG_UNCHECK.equals(_reportIn.getCheckFlag())){
                messages.add("该期报表已上报，不可转上报数据");
            }else{       
                ReportIn reportIn = StrutsReportInDelegate.findById(repInId);
               // ExecuteCollect collect = new ExecuteCollect();
                DB2Excel db2Excel=new DB2Excel(reportIn.getRepInId());
                if(!new File(Config.getReleaseTemplatePath()+com.cbrc.smis.common.Config.FILESEPARATOR+reportIn.getYear()+"_"+reportIn.getTerm()+com.cbrc.smis.common.Config.FILESEPARATOR+operator.getOrgId()).exists()){
                	new File(Config.getReleaseTemplatePath()+com.cbrc.smis.common.Config.FILESEPARATOR+reportIn.getYear()+"_"+reportIn.getTerm()).mkdir();
                	new File(Config.getReleaseTemplatePath()+com.cbrc.smis.common.Config.FILESEPARATOR+reportIn.getYear()+"_"+reportIn.getTerm()+com.cbrc.smis.common.Config.FILESEPARATOR+operator.getOrgId()).mkdir();
                }
                boolean createExcel=db2Excel.createExcel(Config.getReleaseTemplatePath()+com.cbrc.smis.common.Config.FILESEPARATOR+reportIn.getYear()+"_"+reportIn.getTerm()+com.cbrc.smis.common.Config.FILESEPARATOR+operator.getOrgId()+com.cbrc.smis.common.Config.FILESEPARATOR+reportIn.getMChildReport().getComp_id().getChildRepId()+"_"+reportIn.getMChildReport().getComp_id().getVersionId()+".xls");
                
                //if(createExcel&&collect.collectReports(childRepId,versionId,reportName,new Integer(dataRangeId),dataRangeDesc,Integer.parseInt(year),Integer.parseInt(month),operator,new Integer(curId),false,report.getRepInId())){
                if(createExcel && StrutsCollectDelegate.transCollectDatat(reportIn)){   
                	messages.add("汇总数据转上报数据成功！");
                }
            }
        }
        
        String term = mapping.findForward("view").getPath();
        /**加入报表编号条件*/
		if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "childRepId=" + reportInForm.getChildRepId();
		}
		/**加入报表名称条件*/
		if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			/**若是WebLogic则不需要进行转码，直接作为参数传递*/
			term += "repName=" + reportInForm.getRepName();
			/**若是WebSphere则需要先进行转码，再作为参数传递*/
			//term += "repName=" + new String(reportInForm.getRepName().getBytes("gb2312"), "iso-8859-1");
		}
		/**加入模板类型条件*/
		if(reportInForm.getFrOrFzType() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "frOrFzType=" + reportInForm.getFrOrFzType();
		}			
		/**加入报表年份条件*/
		if(reportInForm.getYear() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "year=" + reportInForm.getYear();				
		}
		/**加入报表期数条件*/
		if(reportInForm.getTerm() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "term=" + reportInForm.getTerm();
		}
        request.setAttribute(Config.MESSAGE,messages);
	 	return  new ActionForward(term);
	}
}
