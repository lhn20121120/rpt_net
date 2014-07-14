package com.fitech.gznx.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFReportDealDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.StrutsViewCustom;
import com.fitech.gznx.util.DateFromExcel;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.util.ReportUtils;

public class EditAFReportAction extends Action {

	    /** 
	     * 有jdbc语法 不需要修改 卞以刚 2011-12-27
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
	    	
	    	FitechMessages messages = new FitechMessages();
			AFReportForm reportForm = (AFReportForm)form ;
			RequestUtils.populate(reportForm, request);
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
    		DateFromExcel dataFrom = new DateFromExcel();
    		dataFrom.setPrintPath(operatorId);
    		dataFrom.setMessages(messages);
			String requestUrl = request.getQueryString();
			String repInIdString = request.getParameter("repInId") ;	
			request.setAttribute("repInId", repInIdString);
			String year = request.getParameter("year");
			String term = request.getParameter("term");
			String day = request.getParameter("day") ;
			String printorgId =request.getParameter("orgId");
			String statusFlg = request.getParameter("statusFlg");
			String searchOrgId = request.getParameter("searchOrgId");
			//类型 为了区分是审核查看 还是报表上报之前的查看修改
			String type=request.getParameter("type");
			//放入request作用域
			if(type!=null && !type.equals(""))
				request.setAttribute("type", type);
			if(day != null && day.length() == 1 && day.indexOf("0") != 0){
				day = "0"+day;
			}
			if(term != null && term.length() == 1 && term.indexOf("0") != 0){
				term = "0"+term;
			}
			if(StringUtil.isEmpty(day)){
				day = DateUtil.getMonthLastDayStr(Integer.valueOf(year),Integer.valueOf(term)).substring(8, 10);
			}
			String rdate = year +term+day;
			
			
			
			/**获取报表备注  卞以刚 2012-01-20*/
			if(repInIdString!=null && !repInIdString.equals("")){
				
				String repDesc = "";
				//银监会报表
				if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
					repDesc= StrutsReportInDelegate.getReportInByReportInId(Integer.valueOf(repInIdString)).getRepDesc();
				}else//人行
					repDesc=AFReportDealDelegate.getAFReportByRepId(Long.valueOf(repInIdString)).getRepDesc();
				request.setAttribute("repDesc", repDesc);
			}
			//处理返回页面
			String backQry = request.getParameter("backQry") != null ? request.getParameter("backQry") : "";
			
			if(backQry!=null && !backQry.equals("")){
				
				String[] splitQry = backQry.split("_");					
				if(splitQry.length==5)
					backQry = "date=" + splitQry[0]
					             + "&repName="+ splitQry[1]
					             + "&orgId="+ splitQry[2]
					             + "&repFreqId="+ splitQry[3]
					             + "&curPage=" + splitQry[4]
								 + "&type="+type;
				else if(splitQry.length==4)
					backQry = "date=" + splitQry[0]
					             + "&repName="+ splitQry[1]
					    		 + "&orgId="+ splitQry[2]					                                     
					             + "&curPage=" + splitQry[3]
				 				 + "&type="+type;
				else if(splitQry.length==7)
					backQry = "date=" + splitQry[0]
									  + "&repName="+ splitQry[1]
									  + "&orgId="+ splitQry[2]
									  + "&repFreqId="+ splitQry[3]
									  + "&templateId=" + splitQry[4]
									  + "&bak1=" + splitQry[5]
									  + "&curPage=" + splitQry[6]
									  + "&type="+type;
				backQry +="&searchOrgId="+searchOrgId;
				session.setAttribute("backQry", backQry);
				request.setAttribute("backQry", backQry);
			}
			
			
			// 报表查询
			if(!StringUtil.isEmpty(statusFlg) && statusFlg.equals("1")){

				if(repInIdString != null && !repInIdString.equals("")){
		
		        	
		    		StringBuffer report2 = new StringBuffer();
		    		Map report = new HashMap();
		    		report2.append("");
					String repInId = repInIdString;
	        		// 为查询类报表
					if(repInId.equals("0")){
						String templateId = request.getParameter("templateId");
		        		String versionId = request.getParameter("versionId");
		        		String curid =request.getParameter("curId");
		        		String repFrq = request.getParameter("repFreqId");
		        		String rangeID ="";
		        		// 当为1104报表
		        		if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
		        			/**已使用hibernate 卞以刚 2011-12-21
		        			 * 影响对象：MActuRep**/
		        			rangeID = StrutsViewCustom.getdatarangeId(templateId,versionId,repFrq);
		        			report.put("RangeID", rangeID);
		        		}else{
		        			report.put("RangeID", 1);
		        		}
		        		report.put("Freq", repFrq);
		        		
						report.put("CCY",curid );
			
						report.put("templateId",templateId );
						report.put("versionId",versionId );
		        		report.put("ReptDate", rdate);
		        		report.put("OrgID", printorgId);
		        		String raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "Raq"+ 
						File.separator+templateId+"_"+versionId + ".raq";
		        		File raqfile = new File(raqFileName);
		           		if(!raqfile.exists()){
		           			System.out.println(raqFileName+"路径不存在模板");
		           					           			
		           		}
						try {
							ReportDefine rd = (ReportDefine) ReportUtils.read(raqFileName);
							raqFileName = deleteFilePath + 
							File.separator+templateId+"_"+versionId + ".raq";
							ReportUtils.write(raqFileName,rd);
						} catch (Exception e) {
							
							e.printStackTrace();
						}
		        		report.put("filename",operatorId + "/"+templateId+"_"+versionId + ".raq" );
		        		
						request.setAttribute("reportParam", report);
//						String url = "/gznx/reportadd/viewReport.jsp?";
//
//						ActionForward af = new ActionForward(url);
//						return af;
						return mapping.findForward("search");
					}else{
						// 报送或分析类报表
						String templateId = "";
		        		String versionId = "";
		        		String curid ="";
		        		String repFrq = "";
		        		String RangeID ="";
		        		// 当为1104报表时
		        		if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
		        			ReportInForm reportInForm = StrutsReportInDelegate.getReportIn(new Integer(repInId));
		        			RangeID = String.valueOf(reportInForm.getDataRangeId());
		        			report.put("RangeID", reportInForm.getDataRangeId());
		        			templateId = reportInForm.getChildRepId();
							versionId = reportInForm.getVersionId();
		        			if(reportInForm.getRepFreqId()!= null){
		        				repFrq = String.valueOf(reportInForm.getRepFreqId());
		        			} else {
		        				/**已使用hibernate 卞以刚 2011-12-21**/
		        				repFrq = StrutsViewCustom.getRepFreqId(templateId,versionId,RangeID);
		        			}
		        			
		        			report.put("Freq", repFrq);
							 curid = String.valueOf(reportInForm.getCurId());
							
							report.put("CCY",curid );
							
							report.put("templateId",templateId );
							report.put("versionId",versionId );
		        		}else{
		        			/**已使用hibernate 卞以刚 2011-12-21**/
		        			AFReportForm reportInForm = AFReportDealDelegate.getReportIn(Integer.valueOf(repInId));
		        			RangeID = "1";
		        			report.put("RangeID", 1);
		        			repFrq = reportInForm.getRepFreqId();
		        			report.put("Freq", repFrq);
							 curid = reportInForm.getCurId();
							
							report.put("CCY",curid );
							templateId = reportInForm.getTemplateId();
							versionId = reportInForm.getVersionId();
							report.put("templateId",templateId );
							report.put("versionId",versionId );
		        		}
		        		report.put("ReptDate", rdate);
		        		report.put("OrgID", printorgId);
		        		// 取得报表信息
		        		/**已使用hibernate 卞以刚 2011-12-21**/
						AfTemplate template = AFTemplateDelegate.getTemplate(templateId,versionId);
						if(template == null){
							if(Config.ISOLDHENJI)
								return mapping.findForward("oldindex");
							else
								return mapping.findForward("index");	 
						}
						String raqFileName=null;
						String url = "";
						// 当为清单式报表，则多传两个参数
						if(template.getReportStyle() != null && 
								com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(template.getReportStyle()))){

							report.put("RepID",repInId);
							
							raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + 
											"printRaq"+ File.separator +"qdfile" + 
											File.separator + templateId+"_"+ versionId + ".raq";
							File raqfile = new File(raqFileName);
			           		if(!raqfile.exists()){
			           			System.out.println(raqFileName+"路径不存在模板");			           			
			           		}
							try {
								ReportDefine rd = (ReportDefine) ReportUtils.read(raqFileName);
								raqFileName = deleteFilePath + 
								File.separator +templateId+"_"+versionId + ".raq";
								ReportUtils.write(raqFileName,rd);
							} catch (Exception e) {								
								e.printStackTrace();
							}
			        		report.put("filename",operatorId + "/"+templateId+"_"+versionId + ".raq" );

							// 银监会报表
							if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){								
								request.setAttribute("reportParam", report);
								url = "search";
							} else {
								if(session.getAttribute("reportParam")!=null){
									session.removeAttribute("reportParam");
								}
								session.setAttribute("reportParam", report);
								url = "searchqd";
							}
						} else {
							raqFileName = dataFrom.viewFromExcel(repInId,templateId,versionId, reportFlg,template.getIsReport().intValue());
							if(raqFileName == null && dataFrom.getMessages().getSize()>0){
								request.setAttribute(Config.MESSAGES, dataFrom.getMessages());			           			
							}
							// 模板存放地址
							report.put("filename",raqFileName );
				    		request.setAttribute("reportParam", report);
							url = "search";
						}
//
//						ActionForward af = new ActionForward(url);
//						return af;
						return mapping.findForward(url);
					}
	        		
		    	}
				
			} else if(!StringUtil.isEmpty(statusFlg) && statusFlg.equals("2")){	// 在线填报修改
			if(repInIdString != null && !repInIdString.equals("")){
	

	    		StringBuffer report2 = new StringBuffer();
	    		Map report = new HashMap();
	    		report2.append("");
				String repInId = repInIdString;
        		

        		String templateId = "";
        		String versionId = "";
        		String curid ="";
        		String repFrq = "";
        		String RangeID ="";
        		// 保存标志位
        		String saveFlg = StringUtil.isEmpty(request.getParameter("saveFlg"))?"":request.getParameter("saveFlg");
        		
        		if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
        			/**已使用hibernate 卞以刚 2011-12-22**/
        			ReportInForm reportInForm = StrutsReportInDelegate.getReportIn(new Integer(repInId));
        			RangeID = String.valueOf(reportInForm.getDataRangeId());
        			report.put("RangeID", reportInForm.getDataRangeId());
        			templateId = reportInForm.getChildRepId();
					versionId = reportInForm.getVersionId();
        			if(reportInForm.getRepFreqId() == null){        
        				/**已使用hibernate 卞以刚 2011-12-22**/
        				repFrq = StrutsViewCustom.getRepFreqId(templateId,versionId,String.valueOf(reportInForm.getDataRangeId()));
        				report.put("Freq", repFrq);
        			} else {
        				report.put("Freq", reportInForm.getRepFreqId());
        			}
        			
					 curid = String.valueOf(reportInForm.getCurId());
					
					report.put("CCY",curid );
					
					report.put("templateId",templateId );
					report.put("versionId",versionId );

        		}else{
        			AFReportForm reportInForm = AFReportDealDelegate.getReportIn(Integer.valueOf(repInId));
        			RangeID = "1";
        			report.put("RangeID", 1);
        			repFrq = reportInForm.getRepFreqId();
        			report.put("Freq", repFrq); 
					 curid = reportInForm.getCurId();
					
					report.put("CCY",curid );
					templateId = reportInForm.getTemplateId();
					versionId = reportInForm.getVersionId();
					report.put("templateId",templateId );
					report.put("versionId",versionId );
					
        		}
        		if(saveFlg.equals("1")){
        			/**已使用hibernate 卞以刚 2011-12-22**/
        			AFReportDelegate.updateNewReport(repInId,reportFlg);
				}
        		report.put("ReptDate", rdate);
        		report.put("OrgID", printorgId);
				AfTemplate template = AFTemplateDelegate.getTemplate(templateId,versionId);
				if(template == null){
					if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
						ActionForward af = new ActionForward("/viewDataReport.do?" + backQry);
						return af;
					}
					ActionForward af = new ActionForward("/viewNXDataReport.do?" +backQry);
					return af;
				}
				String raqFileName="";
				
				/**jdbc技术 无特殊oracle语法 不需要修改  卞以刚  2011-12-21*/
				raqFileName = dataFrom.saveFromExcel(repInId,templateId,versionId, reportFlg,template.getIsReport().intValue());
				if(raqFileName == null && dataFrom.getMessages().getSize()>0){
					request.setAttribute(Config.MESSAGES, dataFrom.getMessages());
					if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
						ActionForward af = new ActionForward("/viewDataReport.do?" + backQry);
						return af;
					}
					ActionForward af = new ActionForward("/viewNXDataReport.do?" +backQry);
					return af;
				}
				boolean isdata = AFReportDealDelegate.isReportData(Integer.valueOf(repInId),reportFlg);
				
				String repname = template.getTemplateName();
				report.put("repname", repname);
				report.put("repId",repInId );
				report.put("filename",raqFileName );
				report.put("requestUrl",requestUrl );
				
				if(isdata){
					report.put("isdata", "1");
				}else{
					report.put("isdata", "0");
				}
				
	    		request.setAttribute("reportParam", report);
//				String url = "/gznx/reportadd/editReport.jsp?";
//
//				ActionForward af = new ActionForward(url);
//				return af;
	    		if(Config.ISOLDHENJI)
					return mapping.findForward("oldindex");
				else
					return mapping.findForward("index");
	    	}
			} else if(!StringUtil.isEmpty(statusFlg) && statusFlg.equals("3")){ // 在线填报添加

		    		StringBuffer report2 = new StringBuffer();
		    		Map report = new HashMap();
		    		report2.append("");
			
	        		String templateId = request.getParameter("templateId");
	        		String versionId = request.getParameter("versionId");
	        		String curid =request.getParameter("curId");
	        		String repFrq = request.getParameter("repFreqId");
	        		String rangeID ="";
	        		// 1104报表
	        		if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
	        			/**已使用hibernate  卞以刚 2011-12-21
	        			 * 影响对象：MActuRep**/
	        			rangeID = StrutsViewCustom.getdatarangeId(templateId,versionId,repFrq);
	        			report.put("RangeID", rangeID);
	        		}else{
	        			report.put("RangeID", 1);
	        		}
	        		report.put("Freq", repFrq);
	        		
					report.put("CCY",curid );
		
					report.put("templateId",templateId );
					report.put("versionId",versionId );
	        		report.put("ReptDate", rdate);
	        		report.put("OrgID", printorgId);
	        		/**已使用hibernate 卞以刚 2011-12-21
	        		 * 影响对象：AfTemplate**/
					AfTemplate template = AFTemplateDelegate.getTemplate(templateId,versionId);
					if(template == null){
						if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
							ActionForward af = new ActionForward("/viewDataReport.do?" + backQry);
							return af;
						}
						ActionForward af = new ActionForward("/viewNXDataReport.do?" + backQry);
						return af;
					}
					String raqFileName="";
					String repId = "";
					// 生成报表信息
					if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
						/**已使用Hibernate 卞以刚 2011-12-27
						 * 影响对象：ReportIn ReportIn.MChildReport ReportIn.MCurr ReportIn.MDataRgType**/
						repId = AFReportDelegate.insertYJHReport(report,operator);						
					}else{
						/** 已使用hibernate 卞以刚 2011-12-21
						 * 影响对象：AfTemplate AfReport**/
						repId = AFReportDelegate.insertNXReport(report,operator,reportFlg);
						
					}
					// 生出出错，返回页面
					if(StringUtil.isEmpty(repId)){
						if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
							ActionForward af = new ActionForward("/viewDataReport.do?" + backQry);
							return af;
						}
						ActionForward af = new ActionForward("/viewNXDataReport.do?" + backQry);
						return af;
					}
					/****
					 * 已使用hibernate 卞以刚 2011-12-27
					 * 影响对象：MCell || AfCellinfo
					 */
					raqFileName = dataFrom.saveFromRAQ(templateId,versionId,repId,reportFlg);
					if(raqFileName == null && dataFrom.getMessages().getSize()>0){
						request.setAttribute(Config.MESSAGES, dataFrom.getMessages());
						if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
							ActionForward af = new ActionForward("/viewDataReport.do?" + backQry);
							return af;
						}
						ActionForward af = new ActionForward("/viewNXDataReport.do?" + backQry);
						return af;
					}
					requestUrl = "statusFlg=2&repInId="+repId+"&year="+year+"&term="+term+"&day="+day+"&orgId="+printorgId+"&type="+type;
					report.put("filename",raqFileName );
					report.put("repId",repId );
					report.put("requestUrl",requestUrl );
					report.put("isdata", "0");
					
					String repname = template.getTemplateName();
					report.put("repname", repname);

					
		    		request.setAttribute("reportParam", report);
		    		if(Config.ISOLDHENJI)
						return mapping.findForward("oldindex");
					else
						return mapping.findForward("index");
//					String url = "/gznx/reportadd/editReport.jsp?";
//
//					ActionForward af = new ActionForward(url);
//					return af;
			}
			// 返回
			if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
				ActionForward af = new ActionForward("/viewDataReport.do?" + backQry);
				return af;
			}
			ActionForward af = new ActionForward("/viewNXDataReport.do?" + backQry);
			return af;
			
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
			    f.delete();
			}			
	    }
}
