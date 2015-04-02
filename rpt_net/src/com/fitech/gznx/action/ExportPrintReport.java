package com.fitech.gznx.action;

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
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFReportDealDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.StrutsViewCustom;
import com.fitech.gznx.util.DateFromExcel;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.util.ReportUtils;

public class ExportPrintReport extends Action {
    private static FitechException log = new FitechException(ExportPrintReport.class);

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
        HttpServletResponse response)  throws IOException, ServletException{
    	/** 报表选中标志 **/
		String reportFlg = "0";
		HttpSession session = request.getSession();
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		
		Operator operator = null;
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		String operatorId = String.valueOf(operator.getOperatorId());
		// 清理打印文件夹
		String deleteFilePath = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "printRaq"+ File.separator + operatorId;
   		File outfile = new File(deleteFilePath);
   		if(outfile.exists())
			deleteUploadFile(deleteFilePath);
		
		outfile.mkdir();
		FitechMessages messages = new FitechMessages();
		DateFromExcel dataFrom = new DateFromExcel();
		dataFrom.setPrintPath(operatorId);
		dataFrom.setMessages(messages);
		String repInIdString = request.getParameter("repInIds") ;	
		
		String date = request.getParameter("date");
		


		String rdate = date.replaceAll("-", "");
		String orgId = request.getParameter("orgId") != null ? request.getParameter("orgId") : "";
		if(repInIdString != null && !repInIdString.equals("")){
			String[] repFreqIds = repInIdString.split(Config.SPLIT_SYMBOL_COMMA);
    		StringBuffer report2 = new StringBuffer();
    		report2.append("");
    		if(repFreqIds != null && repFreqIds.length > 0){
				/**复选下载*/
				
				for(int i=0;i<repFreqIds.length;i++){
					String repInId = repFreqIds[i].split(":")[0];
	        		String printorgId = repFreqIds[i].split(":")[1];
	        		String templateId = "";
	        		String versionId = "";
	        		String curid ="";
	        		String repFrq = "";
	        		String RangeID ="";
	        		if(repInId.equals("0")){
            			String freqId = repFreqIds[i].split(":")[2];
            			templateId = repFreqIds[i].split(":")[3];
            			versionId = repFreqIds[i].split(":")[4];
            			curid = repFreqIds[i].split(":")[5];

            			String printfile =  Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator +"Raq"+ File.separator+templateId+"_"+versionId  + ".raq";
            			try {
							ReportDefine rd = (ReportDefine) ReportUtils.read(printfile);
							printfile = deleteFilePath + 
							File.separator+templateId+"_"+versionId + ".raq";
							ReportUtils.write(printfile,rd);
						} catch (Exception e) {
							
							e.printStackTrace();
						}
            			String rangeID ="";
		        		if(reportFlg.equals("1")){
		        			rangeID = StrutsViewCustom.getdatarangeId(templateId,versionId,freqId);
		        		}else{
		        			rangeID = "1";
		        		}
        				         				
        				report2.append("{"+(operatorId +File.separator +File.separator +templateId+"_"+versionId + ".raq")+"(OrgID="
								+ printorgId
								+ ";ReptDate="
								+ rdate
								+ ";CCY="
								+ curid
								+ ";Freq="
								+ freqId
								+ ";RangeID="
								+rangeID + ")}");
						
            			
            		}else{
	        		if(reportFlg.equals("1")){
	        			ReportInForm reportInForm = StrutsReportInDelegate.getReportIn(new Integer(repInId));
	        			templateId = reportInForm.getChildRepId();
						versionId = reportInForm.getVersionId();
	        			RangeID = String.valueOf(reportInForm.getDataRangeId());
	        			
	        			if(reportInForm.getRepFreqId()!= null){
	        				repFrq = String.valueOf(reportInForm.getRepFreqId());
	        			} else {
	        				repFrq = StrutsViewCustom.getRepFreqId(templateId,versionId,RangeID);
	        			}
						 curid = String.valueOf(reportInForm.getCurId());

	        		}else{
	        			AFReportForm reportForm = AFReportDealDelegate.getReportIn(Integer.valueOf(repInId));

	        			RangeID = "1";
	        			repFrq = reportForm.getRepFreqId();

						 curid = reportForm.getCurId();
						

						templateId = reportForm.getTemplateId();
						versionId = reportForm.getVersionId();

	        		}
	
					AfTemplate template = AFTemplateDelegate.getTemplate(templateId,versionId);
					if(template == null){
						continue;
					}
					// 模板路径
					String printfile = "";
					// 清单式报表
					if(template.getReportStyle() != null && 
							com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(template.getReportStyle()))){
						printfile = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + 
						"printRaq"+ File.separator +"qdfile" + File.separator + templateId+"_"+versionId + ".raq";
						try {
							ReportDefine rd = (ReportDefine) ReportUtils.read(printfile);
							printfile = deleteFilePath + 
							File.separator+templateId+"_"+versionId + ".raq";
							ReportUtils.write(printfile,rd);
						} catch (Exception e) {								
							e.printStackTrace();
						}
						printfile =  operatorId +File.separator +File.separator +templateId+"_"+versionId + ".raq";
						
					} else {
						String raqFileName="";
						if(!template.getIsReport().equals(3)){
							raqFileName = dataFrom.saveFromExcel(repInId,templateId,versionId, reportFlg);
						} else {
							raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "Raq"+ 
							File.separator+templateId+"_"+versionId + ".raq";
							try {
								ReportDefine rd = (ReportDefine) ReportUtils.read(raqFileName);
								raqFileName = deleteFilePath + 
								File.separator+templateId+"_"+versionId + ".raq";
								ReportUtils.write(raqFileName,rd);
							} catch (Exception e) {
								
								e.printStackTrace();
							}
							
						}
		
						File file = new File(raqFileName);
						if(!file.exists()){
							continue;                  					
						}

						if(!template.getIsReport().equals(3)){
							printfile =  operatorId +"/" +repInId + ".raq";
						}else{
							printfile =  operatorId +"/" +templateId+"_"+versionId + ".raq";
						}
					}
					
						
					report2.append("{"+printfile+"(OrgID="
										+ printorgId
										+ ";ReptDate="
										+ rdate
										+ ";CCY="
										+ curid
										+ ";Freq="
										+ repFrq
										+ ";RangeID="
										+RangeID);
					if(template.getReportStyle() != null && 
							com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(template.getReportStyle()))){
						report2.append(";RepID="
								+ repInId
								+ ")}");
					} else{
						report2.append(")}");
					}

				}
				}
			}
    		report2.append("prompt=yes");
    		request.setAttribute("reportParam", report2.toString());
//			String url = "/gznx/reportsearch/directprint1.jsp";
//
//			ActionForward af = new ActionForward(url);
//			return af;
    		return mapping.findForward("print");
    	}
		return mapping.findForward("index");	 
	}
    
    // 删除上传的ZIP文件及解压文件夹
    private void deleteUploadFile(String filePath) {

		File f = new File(filePath);
		if (f.isDirectory()) {
		    File[] fileList = f.listFiles();
		    if(fileList != null && fileList.length>0){
			    for (int i = 0; i < fileList.length; i++) {
					File excelFile = fileList[i];			
					excelFile.delete();
			    }
		    }
		}
		
    }
}
