package com.fitech.net.action;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

/**
 *����������ͳ��
 *���ڣ�2008-3-4
 *���ߣ��ܷ���
 */
public class ViewReportStatAction extends Action {
    private static FitechException log = new FitechException(ViewReportAction.class);

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
        ReportInInfoForm reportInInfoForm = (ReportInInfoForm)form ;
        RequestUtils.populate(reportInInfoForm, request);
        
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, -1);
    
	    
		int recordCount = 0; // ��¼����
		int offset = 0; // ƫ����
		int limit = 0; // ÿҳ��ʾ�ļ�¼����
		
		List resList = null;
		ApartPage aPage = new ApartPage();	   	
		String strCurPage = request.getParameter("curPage");
		if(strCurPage!=null){		
			if(!strCurPage.equals(""))		    
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}
		else
			aPage.setCurPage(1);
		//����ƫ����
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 		
		limit = Config.PER_PAGE_ROWS;
		
        HttpSession session = request.getSession();
        Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
		String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
        if(reportInInfoForm.getDate() ==null || reportInInfoForm.getDate().equals("")){
        	reportInInfoForm.setDate(yestoday.substring(0, 7));
        	reportInInfoForm.setYear(new Integer(yestoday.substring(0,4)));
        	reportInInfoForm.setTerm(new Integer(yestoday.substring(5,7)));
	    }else{
	    	reportInInfoForm.setYear(new Integer(reportInInfoForm.getDate().split("-")[0]));
	    	reportInInfoForm.setTerm(new Integer(reportInInfoForm.getDate().split("-")[1]));
	    }
//        String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
//        if(reportInInfoForm.getYear() == null || reportInInfoForm.getYear().equals(""))			   
//			reportInInfoForm.setYear(new Integer(yestoday.substring(0, 4)));		   
//		if(reportInInfoForm.getTerm() == null || reportInInfoForm.getTerm().equals(""))
//			reportInInfoForm.setTerm(new Integer(yestoday.substring(5, 7)));	
        
        try{
        	//ȡ�ü�¼����
        	/**ʹ��jdbc ��Ҫ�޸�  ���Ը� 2011-12-22**/
	   		recordCount = StrutsReportInDelegate.getReportStatCount(reportInInfoForm,operator);	  
	   		//��ʾ��ҳ��ļ�¼
	   		if(recordCount > 0)
	   			/**jdbc���� oracle�﷨ ��Ҫ�޸� ���Ը� 2011-12-21**/
		   	    resList = StrutsReportInDelegate.getReportStatRecord(reportInInfoForm,offset,limit,operator);
	   		
        }catch(Exception ex){
        	log.printStackTrace(ex);
        	messages.add(resources.getMessage("select.dataReport.failed"));  
        }
        
        //��ApartPage��������request��Χ��
	 	aPage.setTerm(this.getTerm(reportInInfoForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);	 	
         
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
        	request.setAttribute(Config.MESSAGES,messages);
        if(resList != null && resList.size() > 0)
        	request.setAttribute(Config.RECORDS,resList);
                
        return mapping.findForward("view");
    }
    public ActionForward export(ActionMapping mapping,ActionForm form,HttpServletRequest request
    		,HttpServletResponse response)  throws IOException, ServletException{
        
        
        ReportInInfoForm reportInInfoForm = (ReportInInfoForm)form ;        
        Calendar calendar = Calendar.getInstance();
        if(reportInInfoForm.getYear() == null || reportInInfoForm.getYear().equals(""))			   
			reportInInfoForm.setYear(new Integer(calendar.get(Calendar.YEAR)));		   
		if(reportInInfoForm.getTerm() == null || reportInInfoForm.getTerm().equals(""))			   
			reportInInfoForm.setTerm(new Integer(calendar.get(Calendar.MONTH)));	    
	    
		
        HttpSession session = request.getSession();
        Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        String newFile=Config.TEMP_DIR+System.currentTimeMillis();

        try{
		   List	 resList = StrutsReportInDelegate.getReportStatRecord(reportInInfoForm,0,60000,operator);
		   String template="";
		   if(request.getRealPath("/").endsWith(Config.FILESEPARATOR)){
			   template=request.getRealPath("/");
		   }else{
			   template=request.getRealPath("/")+Config.FILESEPARATOR;
		   }
		  template+="report_mgr"+Config.FILESEPARATOR+"excel"+Config.FILESEPARATOR+"State.xls"; 
		  createExcel(resList,template,newFile);
        }catch(Exception ex){
        	log.printStackTrace(ex);
        }
        
       String forwardPath="/servlets/DownloadServlet?fileName="+reportInInfoForm.getYear()
       +"-"+reportInInfoForm.getTerm()+".xls&filePath="+newFile+"&deleteFile=1";
        return new ActionForward(forwardPath);
    }
    
    /**
	 * ���ò�ѯ���ݲ���
	 * 
	 * @param reportInInfoForm
	 * @return String ��ѯ��URL
	 */
	public String getTerm(ReportInInfoForm reportInForm){	   
		String term="";
		
		/**���뱨��������*/
		if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "childRepId=" + reportInForm.getChildRepId();
		}
		/**���뱨����������*/
		if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName=" + reportInForm.getRepName();
		}
		/**����ģ����������*/
		if(reportInForm.getFrOrFzType() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "frOrFzType=" + reportInForm.getFrOrFzType();
		}
		/**���뱨��Ƶ������*/
		if(reportInForm.getRepFreqId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId=" + reportInForm.getRepFreqId();
		}
		/**���뱨���������*/
		if(reportInForm.getYear() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "year=" + reportInForm.getYear();
		}
		/**���뱨����������*/
		if(reportInForm.getTerm() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "term=" + reportInForm.getTerm();
		}	
		/**���뱨����������*/
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
	 *<p>����:����ͳ�Ʊ��� ����Excel·��</p>
	 *<p>����:modelPath ���ɱ���ģ��·����newFileDir���ɱ���·��</p>
	 *<p>���ڣ�2008-3-4</p>
	 *<p>���ߣ��ܷ���</p>
	 */
	 private String createExcel(List list,String modelPath,String newFileDir) throws Exception
	   {      
		 String result=null;
		 if(list==null||modelPath==null||newFileDir==null){
			 return null;
		 }
		 try{
		HSSFWorkbook sourceWb = null;
		HSSFSheet sheet = null;
		FileInputStream inStream = new FileInputStream(modelPath);
		POIFSFileSystem srcPOIFile = new POIFSFileSystem(inStream);
		sourceWb = new HSSFWorkbook(srcPOIFile);
		
		if (sourceWb.getNumberOfSheets() > 0)
		{
			sheet = sourceWb.getSheetAt(0);
		}
		inStream.close();
		for(int i=0;i<list.size();i++){
			Aditing aditing  =(Aditing)list.get(i);
			HSSFRow row=sheet.createRow(i+1);
			HSSFCell cell0=row.createCell((short)0);
			cell0.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell0.setCellValue(aditing.getChildRepId());
			
			HSSFCell cell1=row.createCell((short)1);
			cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell1.setCellValue(aditing.getRepName());
			
			HSSFCell cell2=row.createCell((short)2);
			cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell2.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell2.setCellValue(aditing.getVersionId());
			
			HSSFCell cell3=row.createCell((short)3);
			cell3.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell3.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell3.setCellValue(aditing.getDataRgTypeName());
			
			HSSFCell cell4=row.createCell((short)4);
			cell4.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell4.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell4.setCellValue(aditing.getCurrName());
			
			HSSFCell cell5=row.createCell((short)5);
			cell5.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell5.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell5.setCellValue(aditing.getActuFreqName());
			
			HSSFCell cell6=row.createCell((short)6);
			cell6.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell6.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell6.setCellValue(aditing.getYear().toString()+"-"+aditing.getTerm().toString());
			
			HSSFCell cell7=row.createCell((short)7);
			cell7.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell7.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell7.setCellValue(aditing.getOrgName());
			
			HSSFCell cell8=row.createCell((short)8);
			cell8.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell8.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(aditing.getCheckFlag()==null){
				cell8.setCellValue("δ����");
			} else
			if(aditing.getCheckFlag().equals(Short.valueOf("1"))){
				cell8.setCellValue("���ͨ��");
			} else
			if(aditing.getCheckFlag().equals(Short.valueOf("-1"))){
				cell8.setCellValue("���δͨ��");
			}else
			if(aditing.getCheckFlag().equals(Short.valueOf("0"))){
				cell8.setCellValue("δ���");
			}else{
				cell8.setCellValue("δ����");
			}
			
			
		}
	          
		FileOutputStream stream = new FileOutputStream(newFileDir);
		sourceWb.write(stream);
		stream.close();
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	    return result;
	   }
}
