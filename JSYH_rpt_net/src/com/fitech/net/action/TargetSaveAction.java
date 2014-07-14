package com.fitech.net.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsTargetDefineDelegate;
import com.fitech.net.config.Config;
import com.fitech.net.form.TargetDefineForm;

public class TargetSaveAction extends Action {

	private static FitechException log=new FitechException(TargetSaveAction.class);
	   /**
	    * @param result 查询返回标志,如果成功返回true,否则返回false
	    * @param targetForm 
	    * @param request 
	    * @exception Exception 有异常捕捉并抛出
	    */
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			   	HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {
		
		TargetDefineForm targetForm = (TargetDefineForm)form ;
		if(targetForm.getTargetDefineId()!= null && !targetForm.getFormula().trim().equals("")){
			String formu = targetForm.getFormula();
			targetForm = StrutsTargetDefineDelegate.selectone(targetForm);
			targetForm.setFormula(formu);
		}
		
		String fileName = Config.REAL_ROOT_PATH + File.separator + "targetFumal" + File.separator + "temp" + File.separator + Config.TARGET_EXCEL_NAME;		
				
		FileInputStream inStream = null;
		HSSFWorkbook book = null;
		HSSFSheet sheet = null;
		String formu = "";
		
		try {
			inStream = new FileInputStream(fileName);
			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
			book = new HSSFWorkbook(srcFile);
			if(book.getNumberOfSheets() > 0) {
				sheet = book.getSheetAt(0);
			}
			inStream.close();
			
			HSSFRow row = null;
			HSSFCell cell = null;
			if(sheet != null){
				for(Iterator iter=sheet.rowIterator();iter.hasNext();){
					row = (HSSFRow)iter.next();
					if(row == null) continue;
					for(Iterator cellIter=row.cellIterator();cellIter.hasNext();){
						cell = (HSSFCell)cellIter.next();
						if(cell == null) continue;
						if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
							formu = cell.getCellFormula();
						}
					}
				}
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		formu = formu.replaceAll("!","_");
		targetForm.setFormula(formu);
	    
	    String page = request.getParameter("pageForward");
	    
	    if(!"".equals(page) && page.equals("add"))
	    	return mapping.findForward("addView");
	    else if(!"".equals(page) && page.equals("edit")){
	    	request.setAttribute("ObjForm",targetForm);
	    	return mapping.findForward("editView"); 
	    }	    
	    return mapping.findForward("view"); 
	}
}
