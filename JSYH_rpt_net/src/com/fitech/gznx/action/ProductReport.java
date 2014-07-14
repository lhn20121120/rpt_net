package com.fitech.gznx.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFReportDealDelegate;
import com.fitech.gznx.service.AFReportProductDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;

public class ProductReport extends Action {	   /**
	    * @param result 查询返回标志,如果成功返回true,否则返回false
	    * @param ReportInForm 
	    * @param request 
	    * @exception Exception 有异常捕捉并抛出
	    */
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			   	HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {
		FitechMessages messages = new FitechMessages();
		HttpSession session = request.getSession();
		Operator operator = null;
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		/** 报表选中标志 **/
		String reportFlg = "0";
		
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		
		List fileList = null;

		try{
			String repNames = request.getParameter("repNames") != null ? request.getParameter("repNames") : null;	
			String type = request.getParameter("type") != null ? request.getParameter("type") : "";	
			
			String date = request.getParameter("date") != null ? request.getParameter("date") : "";			
			
    		String orgId = request.getParameter("orgId") != null ? request.getParameter("orgId") : "";
    		String repFreqId = request.getParameter("repFreqId") != null ? request.getParameter("repFreqId") : "";
    		
			// 如果type不等于downAll 就不是下载全部
			if(!type.equals("downAll")){
	        	if(repNames != null && !repNames.equals("")){
	        		String[] repNameArr = repNames.split(",");
	        		if(repNameArr != null && repNameArr.length > 0){

	        			/**复选下载*/
	        			fileList = new ArrayList();        				
        				for(int i=0;i<repNameArr.length;i++){
        					String[] repInfo = repNameArr[i].split("_");
        					Map report = new HashMap();
        					try{
        						report.put("templateid", repInfo[0]);
        						report.put("versionod", repInfo[1]);
        						if(StringUtil.isEmpty(repInfo[2])){
        							report.put("dataRgId", "1");
        						} else{
        							report.put("dataRgId", repInfo[2]);
        						}
        						
        						report.put("freqID", repInfo[3]);
        						report.put("curId", repInfo[4]);
        						report.put("orgId", repInfo[5]);

        					}catch(Exception e){
        						e.printStackTrace();
        					}
        					String raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "Raq"+ 
        					File.separator+repInfo[0]+"_"+repInfo[1]  + ".raq";
            				File file = new File(raqFileName);
            				if(!file.exists()){
            					messages.add(repInfo[0]+repInfo[1]+"没有找到模板文件");
            					continue;                  					
            				}
            				report.put("file", file);
            				fileList.add(report);
            			} 
	        		}
	        	}
			}else{
				String repName = request.getParameter("repName");
				if( repName != null){
//					repName = new String(repName.getBytes("ISO-8859-1"),"GB2312");
				}
				String templateType = request.getParameter("templateType") != null ? request.getParameter("templateType") : "";	
				AFReportForm reportInForm = new AFReportForm();
				reportInForm.setOrgId(orgId);            	
				reportInForm.setDate(date);
				reportInForm.setRepName(repName);
				reportInForm.setTemplateType(templateType);
				reportInForm.setRepFreqId(repFreqId);
            	List list = null;
            	// 银监会报表
            	if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
            		list = AFReportProductDelegate.selectYJHReportList(reportInForm,operator);
            	} else {
            		list = AFReportProductDelegate.selectNOTYJHReportList(reportInForm,operator,reportFlg);
            	}
            	// 取得模板生成条件参数
            	if(list != null && list.size() > 0){
            		fileList = new ArrayList();
            		for(int i=0;i<list.size();i++){
            			Aditing aditing = (Aditing)list.get(i);
            			Map report = new HashMap();
    					try{
    						report.put("templateid", aditing.getChildRepId());
    						report.put("versionod", aditing.getVersionId());
    						report.put("dataRgId", aditing.getDataRangeId());
    						report.put("freqID", aditing.getActuFreqID());
    						report.put("curId", aditing.getCurId());
    						report.put("orgId", aditing.getOrgId());

    					}catch(Exception e){
    						e.printStackTrace();
    					}
            			String raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "Raq" 
            			+ File.separator +  aditing.getChildRepId() + "_" + aditing.getVersionId()  + ".raq";
            			File file = new File(raqFileName);
            			if(!file.exists()){
                			messages.add(aditing.getRepName()+"没有找到模板文件");
            				continue;            				
            			}
            			
            			report.put("file", file);
        				fileList.add(report);
            		}
            	}
            	
			}
			if(fileList == null || fileList.size() <= 0){ 
				if(messages.getMessages() != null && messages.getMessages().size() > 0)		
					request.setAttribute(Config.MESSAGES,messages);
				
        	} else {
			// 生成excel文件
			for(int i=0;i<fileList.size();i++){
    			Map reportMap = (Map)fileList.get(i); 
    			InputStream inStream = null;
    			try{
    				String objDate = DateUtil.getFreqDateLast(date,Integer.valueOf(String.valueOf(reportMap.get("freqID"))));
    				
    				String templateid = (String) reportMap.get("templateid");
    				String versionod = (String) reportMap.get("versionod");
    				
    				String strorgId = (String) reportMap.get("orgId");
    				
    				AfTemplate aftemplate = AFTemplateDelegate.getTemplate(templateid,versionod);
    				if(aftemplate.getReportStyle() != null && 
    						com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(aftemplate.getReportStyle()))){
    					reportMap.put("QDFlg",  com.fitech.gznx.common.Config.PROFLG_SENCEN_QD);
    				}  
    				String excelFileName = null;
    //				if(aftemplate.getIsReport()!=null && aftemplate.getIsReport().intValue()==1){
    					File file = (File) reportMap.get("file");
        				inStream = new FileInputStream(file);
        				ReportDefine rd = (ReportDefine)ReportUtils.read(inStream);
        				Context cxt = new Context();  //构建报表引擎计算环境
        				cxt.setParamValue("OrgID", strorgId);
        				cxt.setParamValue("ReptDate", objDate.replace("-", ""));
        				String curid = String.valueOf(reportMap.get("curId"));
        				String freqId = String.valueOf(reportMap.get("freqID"));
        				
        				cxt.setParamValue("CCY", curid);

        				cxt.setParamValue("RangeID", reportMap.get("dataRgId"));
        				cxt.setParamValue("Freq", Integer.valueOf(freqId).intValue());
        				
        				
        				Engine engine = new Engine(rd, cxt);  //构造报表引擎
        				
        				IReport iReport = engine.calc();  //运算报表
        				excelFileName = Config.WEBROOTPATH +"reportFiles" + File.separator + 
        				templateid+"_"+versionod+"_"+strorgId+"_"+curid+"_"+freqId+"_"+objDate.replace("-", "") + ".xls";
        				
        				ReportUtils.exportToExcel(excelFileName, iReport, false);
        				/***
						 * 2012/12/31日修改，为能正确解析BIII报表IF公式
						 * 修改之后，此方法会将IF公式中的公式项“==”替换为“=”
						 *///LuYueFei:2014-01-05:DownloadALLReportServlet中对调用此方法，因此此类中的此语句注释
//        				AFReportDealDelegate.resoveExcelFormaule(excelFileName,file.getPath(),templateid);
        				
    			//	}
    				
    				reportMap.put("file", excelFileName);
    				reportMap.put("times", objDate.replace("-", ""));
    				reportMap.put("orgid", strorgId);
    				boolean result = false;
    				// 银监会报表处理
    				if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
//    					result = AFReportProductDelegate.insertYJHReport(reportMap,operator);//Nick:2013-08-18--由于不生成times为-2的记录，因此注释此语句，否则后台一直报错
    				}else{
//    					result = AFReportProductDelegate.insertNXReport(reportMap,operator,reportFlg);//Nick:2013-08-18--由于不生成times为-2的记录，因此注释此语句，否则后台一直报错				
    				}
    			}catch(Exception e){
    				e.printStackTrace();
    			}  catch (Throwable e1) {
    				e1.printStackTrace();
    			} finally {
    				if(inStream!=null){
    					inStream.close();
    				}
    			}
			}

        	}

			if(messages.getMessages() == null || messages.getMessages().size() == 0)
				messages.add("true");
		}catch(Exception e){
			e.printStackTrace();
		}
		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(Config.MESSAGES,messages);
		response.setCharacterEncoding("gb2312");
		PrintWriter out = response.getWriter();

		response.setContentType("text/xml");
		response.setHeader("Cache-control", "no-cache");

		out.println("<response><result>" + messages.getMessages() + "</result></response>");
		out.close();

		return null;
 
		}
	}
