package com.fitech.gznx.action;

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
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateTypeDelegate;

/**
 * 农信报表查询
 * @author Dennis Yee
 *
 */
public class ViewReportNXStatAction extends Action {
	
    private static FitechException log = new FitechException(ViewReportNXStatAction.class);

    /** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request
    		,HttpServletResponse response)  throws IOException, ServletException{
        
    	
    	if(request.getParameter("method")!=null){
    		return export(mapping,form,request,response);
    	}
    	
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        
        AFReportForm reportInInfoForm = (AFReportForm) form ;
        RequestUtils.populate(reportInInfoForm, request);
         

		if(request.getParameter("isLeader")!=null && !request.getParameter("isLeader").equals("")){
			reportInInfoForm.setIsLeader("1");
    	}
		int recordCount = 0; // 记录总数
		int offset = 0; // 偏移量
		int limit = 0; // 每页显示的记录条数
		
		List resList = null;
		ApartPage aPage = new ApartPage();	   	
		String strCurPage = request.getParameter("curPage");
		if(strCurPage!=null){		
			if(!strCurPage.equals(""))		    
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}
		else
			aPage.setCurPage(1);
		
		//计算偏移量
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 		
		limit = Config.PER_PAGE_ROWS;
		
        HttpSession session = request.getSession();
        Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
		if(reportInInfoForm.getDate() == null || reportInInfoForm.getDate().equals("")) {
			//获得昨天时间
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			reportInInfoForm.setDate(yestoday);	
		}
		//设置报表类型为不是补录的报表
		reportInInfoForm.setIsReport(Integer.valueOf(com.fitech.gznx.common.Config.TEMPLATE_REPORT));
        /** 报表选中标志 **/
		String reportFlg = "";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		//hbyh添加功能块
		if(request.getParameter("gatherFlg")!=null && request.getParameter("gatherFlg").equals("1"))
			reportFlg = com.fitech.gznx.common.Config.OTHER_REPORT;
		
        //取得模板类型
		reportInInfoForm.setTemplateType(reportFlg);
        
        try{
        	//取得记录总数
        	/**JDBC技术 无特殊oracle语法 卞以刚 2011-12-27
        	 * 影响表：VIEW_AF_REPORT af_report VIEW_ORG_REP*/
	   		recordCount = AFReportDelegate.getReportStatCount(reportInInfoForm,operator);	  
	   		//显示分页后的记录
	   		if(recordCount > 0)
	   			/**jdbc技术 oracle语法(rownum) 需要修改 卞以刚 2011-12-22
	   			 * 影响表：VIEW_AF_REPORT af_report ORG_REP_ID
	   			 * 已修改为sqlserver top  卞以刚 2011-12-27 待测试
	   			 * 已增加数据库判断*/
		   	    resList = AFReportDelegate.getReportStatRecord(reportInInfoForm,offset,limit,operator);
	   		
        }catch(Exception ex){
        	log.printStackTrace(ex);
        	messages.add(resources.getMessage("select.dataReport.failed"));  
        }
        
        //把ApartPage对象存放在request范围内
	 	aPage.setTerm(this.getTerm(reportInInfoForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
//	 	if(!StringUtil.isEmpty(reportInInfoForm.getBak1()) && StringUtil.isEmpty(reportInInfoForm.getTemplateTypeName())){
//       	 String templateName = AFTemplateTypeDelegate.getTemplateTypeName(reportInInfoForm.getBak1(), reportFlg);
//       	reportInInfoForm.setTemplateTypeName(templateName);
//        }
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
        	request.setAttribute(Config.MESSAGES,messages);
        if(resList != null && resList.size() > 0)
        	request.setAttribute(Config.RECORDS,resList);
         if(!StringUtil.isEmpty(reportInInfoForm.getIsLeader()) && reportInInfoForm.getIsLeader().equals("1")){
        	 request.setAttribute("isLeader","1");
         }
        return mapping.findForward("view");
    }
    
    
    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward export(ActionMapping mapping,ActionForm form,HttpServletRequest request
    		,HttpServletResponse response)  throws IOException, ServletException{
        
        
        AFReportForm reportInInfoForm = (AFReportForm)form ;        
        Calendar calendar = Calendar.getInstance();
//        if(reportInInfoForm.getYear() == null || reportInInfoForm.getYear().equals(""))			   
//			reportInInfoForm.setYear(new Integer(calendar.get(Calendar.YEAR)));		   
//		if(reportInInfoForm.getTerm() == null || reportInInfoForm.getTerm().equals(""))			   
//			reportInInfoForm.setTerm(new Integer(calendar.get(Calendar.MONTH)));	  
	    if(reportInInfoForm.getDate() == null || reportInInfoForm.getDate().equals(""))			   
	  		reportInInfoForm.setDate(calendar.get(Calendar.YEAR) + "-"
	  				+ calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.DATE));	
	    
		
        HttpSession session = request.getSession();
        Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
        String newFile=Config.TEMP_DIR+System.currentTimeMillis();

        try{
		   List	 resList = AFReportDelegate.getReportStatRecord(reportInInfoForm,0,60000,operator);
		   String template="";
		   if(request.getRealPath("/").endsWith(Config.FILESEPARATOR)){
			   template=request.getRealPath("/");
		   }else{
			   template=request.getRealPath("/")+Config.FILESEPARATOR;
		   }
		  template+="report_mgr"+Config.FILESEPARATOR+"excel"+Config.FILESEPARATOR+"State.xls"; 
		  //createExcel(resList,template,newFile);
        }catch(Exception ex){
        	log.printStackTrace(ex);
        }
        
       String forwardPath="/servlets/DownloadServlet?fileName="+reportInInfoForm.getYear()
       +"-"+reportInInfoForm.getTerm()+".xls&filePath="+newFile+"&deleteFile=1";
        return new ActionForward(forwardPath);
    }
    
    /**
	 * 设置查询传递参数
	 * 
	 * @param reportInInfoForm
	 * @return String 查询的URL
	 */
	public String getTerm(AFReportForm reportInForm){	   
		String term="";
		
		if(reportInForm.getIsLeader() != null && !reportInForm.getIsLeader().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "isLeader=" + reportInForm.getIsLeader();
		}
		/**加入报表编号条件*/
		if(reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "templateId=" + reportInForm.getTemplateId();
		}
		/**加入报表名称条件*/
		if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName=" + reportInForm.getRepName();
		}
		/**加入模板类型条件*/
		if(reportInForm.getBak1() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "bak1=" + reportInForm.getBak1();
		}
		/**加入报送频度条件*/
		if(reportInForm.getRepFreqId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId=" + reportInForm.getRepFreqId();
		}
//		/**加入报表年份条件*/
//		if(reportInForm.getYear() != null){
//			term += (term.indexOf("?")>=0 ? "&" : "?");
//			term += "year=" + reportInForm.getYear();
//		}
//		/**加入报表期数条件*/
//		if(reportInForm.getTerm() != null){
//			term += (term.indexOf("?")>=0 ? "&" : "?");
//			term += "term=" + reportInForm.getTerm();
//		}	
		/**加入报表期数条件*/
		if(reportInForm.getDate() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "date=" + reportInForm.getDate();
		}	
		if(reportInForm.getOrgId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId=" + reportInForm.getOrgId();
		}
		String orgName = reportInForm.getOrgName();
		if(orgName!=null&&!orgName.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "orgName="+orgName;    
		}
		if(reportInForm.getCheckFlag() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "checkFlag=" + reportInForm.getCheckFlag();
		}
		if(term.indexOf("?")>=0)		
			term = term.substring(term.indexOf("?")+1);
		
		return term;   
	}
	
	/**
	 *<p>描述:生成统计报表 返回Excel路径</p>
	 *<p>参数:modelPath 生成报表模板路径，newFileDir生成报表路径</p>
	 *<p>日期：2008-3-4</p>
	 *<p>作者：曹发根</p>
	 */
//	 private String createExcel(List list,String modelPath,String newFileDir) throws Exception
//	   {      
//		 String result=null;
//		 if(list==null||modelPath==null||newFileDir==null){
//			 return null;
//		 }
//		 try{
//		HSSFWorkbook sourceWb = null;
//		HSSFSheet sheet = null;
//		FileInputStream inStream = new FileInputStream(modelPath);
//		POIFSFileSystem srcPOIFile = new POIFSFileSystem(inStream);
//		sourceWb = new HSSFWorkbook(srcPOIFile);
//		
//		if (sourceWb.getNumberOfSheets() > 0)
//		{
//			sheet = sourceWb.getSheetAt(0);
//		}
//		inStream.close();
//		for(int i=0;i<list.size();i++){
//			Aditing aditing  =(Aditing)list.get(i);
//			HSSFRow row=sheet.createRow(i+1);
//			HSSFCell cell0=row.createCell((short)0);
//			cell0.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell0.setCellValue(aditing.getChildRepId());
//			
//			HSSFCell cell1=row.createCell((short)1);
//			cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
//			cell1.setCellValue(aditing.getRepName());
//			
//			HSSFCell cell2=row.createCell((short)2);
//			cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell2.setEncoding(HSSFCell.ENCODING_UTF_16);
//			cell2.setCellValue(aditing.getVersionId());
//			
//			HSSFCell cell3=row.createCell((short)3);
//			cell3.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell3.setEncoding(HSSFCell.ENCODING_UTF_16);
//			cell3.setCellValue(aditing.getDataRgTypeName());
//			
//			HSSFCell cell4=row.createCell((short)4);
//			cell4.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell4.setEncoding(HSSFCell.ENCODING_UTF_16);
//			cell4.setCellValue(aditing.getCurrName());
//			
//			HSSFCell cell5=row.createCell((short)5);
//			cell5.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell5.setEncoding(HSSFCell.ENCODING_UTF_16);
//			cell5.setCellValue(aditing.getActuFreqName());
//			
//			HSSFCell cell6=row.createCell((short)6);
//			cell6.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell6.setEncoding(HSSFCell.ENCODING_UTF_16);
//			cell6.setCellValue(aditing.getYear().toString()+"-"+aditing.getTerm().toString());
//			
//			HSSFCell cell7=row.createCell((short)7);
//			cell7.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell7.setEncoding(HSSFCell.ENCODING_UTF_16);
//			cell7.setCellValue(aditing.getOrgName());
//			
//			HSSFCell cell8=row.createCell((short)8);
//			cell8.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell8.setEncoding(HSSFCell.ENCODING_UTF_16);
//			if(aditing.getCheckFlag()==null){
//				cell8.setCellValue("未报送");
//			} else
//			if(aditing.getCheckFlag().equals(Short.valueOf("1"))){
//				cell8.setCellValue("审核通过");
//			} else
//			if(aditing.getCheckFlag().equals(Short.valueOf("-1"))){
//				cell8.setCellValue("审核未通过");
//			}else
//			if(aditing.getCheckFlag().equals(Short.valueOf("0"))){
//				cell8.setCellValue("未审核");
//			}else{
//				cell8.setCellValue("未报送");
//			}
//			
//			
//		}
//	          
//		FileOutputStream stream = new FileOutputStream(newFileDir);
//		sourceWb.write(stream);
//		stream.close();
//		 }catch(Exception e){
//			 e.printStackTrace();
//		 }
//	    return result;
//	   }
}
