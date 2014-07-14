package com.fitech.net.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsTargetDefineDelegate;
import com.fitech.net.common.CommMethod;
import com.fitech.net.config.Config;
import com.fitech.net.form.TargetDefineForm;



public class TargetDefineAction extends Action {

	private static FitechException log=new FitechException(TargetDefineAction.class);
	
	protected static Log logs = LogFactory.getLog(DispatchAction.class);
	   /**
	    * @param result 查询返回标志,如果成功返回true,否则返回false
	    * @param ReportInForm 
	    * @param request 
	    * @exception Exception 有异常捕捉并抛出
	    */
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			   	HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {		
		
		TargetDefineForm targetForm = (TargetDefineForm)form ;		
		
		RequestUtils.populate(targetForm, request);	
		
		response.addHeader("Pragma", "no-cache");
	    response.addHeader("Cache-Control", "no-cache");
	    response.addHeader("Expires", "Thu,01 Jan 1970 00:00:01 GMT");	  	    
	    
		String path="targetFumal/" + Config.TARGET_EXCEL_NAME;
		String pageForward = "add";
		
		if(targetForm.getTargetDefineId() != null && !targetForm.getTargetDefineId().toString().trim().equals("")){
			pageForward = "edit";
			String formu = targetForm.getFormula();
			targetForm = StrutsTargetDefineDelegate.selectone(targetForm);
			targetForm.setFormula(formu);
		}
		if(targetForm.getFormula() != null && !targetForm.getFormula().trim().equals("")){
			
			try{
				String inFileName = Config.REAL_ROOT_PATH + File.separator + "targetFumal" + File.separator + Config.TARGET_EXCEL_NAME;
				FileInputStream inStream = new FileInputStream(inFileName);
				String outFileName = Config.REAL_ROOT_PATH + File.separator + "targetFumal" + File.separator + "temp" +File.separator + Config.TARGET_EXCEL_NAME;
				FileOutputStream outStream = new FileOutputStream(outFileName);
				byte[] bytes=new byte[1024];        
				int index=0;       
				while((index=inStream.read(bytes))!=-1){ 	  
					outStream.write(bytes,0,index);
				}
				outStream.close();
				inStream.close();
				
				HSSFSheet sheet = null;
				inStream = new FileInputStream(outFileName);
				POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
				HSSFWorkbook book = new HSSFWorkbook(srcFile);
				if(book.getNumberOfSheets() > 0) {
					sheet = book.getSheetAt(0);
				}
				inStream.close();
				HSSFRow row = null;
			    HSSFCell cell = null;

			    row = sheet.getRow(1);
			    if(row == null) row = sheet.createRow(1);
				if(row != null){
					cell = (HSSFCell)row.getCell((short)1);
					if(cell == null) cell = row.createCell((short)1);
					if(cell != null){
						String formu = targetForm.getFormula().trim();
						formu = formu.replaceAll("@","%");
						formu = formu.replaceAll(" ","+");
						formu = formu.replaceAll("_","!");
						cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
						cell.setCellFormula(formu);
					}
				}    
				
				FileOutputStream stream = new FileOutputStream(outFileName);
				book.write(stream);
				stream.close();
				 
				path = "targetFumal/temp/" + Config.TARGET_EXCEL_NAME;
			}catch(Exception ex){
				ex.printStackTrace();
				path="targetFumal/" + Config.TARGET_EXCEL_NAME;
			}
			
		}
		
		String fumalUrl = CommMethod.getAbsolutePath(request, path);
		
		request.setAttribute("fumalUrl", fumalUrl);
		request.setAttribute("saveUrl", CommMethod.getAbsolutePath(request, "target/add/targetUploadDoc.jsp"));		
		request.setAttribute("reportName",Config.TARGET_EXCEL_NAME);		
		
		String hrefUrl = CommMethod.getAbsolutePath(request,  "/target/targetSave.do?page=add&defineName="+targetForm.getDefineName()
				+ "&startDate="+targetForm.getStartDate()+"&endDate="+targetForm.getEndDate()+"&formula="+targetForm.getFormula()
				+"&law="+targetForm.getLaw()+"&des="+targetForm.getDes()+"&pageForward="+pageForward);
		if(targetForm.getTargetDefineId() != null && !targetForm.getTargetDefineId().toString().trim().equals(""))
			hrefUrl += "&targetDefineId=" + targetForm.getTargetDefineId();
		request.setAttribute("hrefUrl", hrefUrl);		
		
	    CommMethod.buidPageInfo(request);		    
	    return mapping.findForward("view");
	}
}
