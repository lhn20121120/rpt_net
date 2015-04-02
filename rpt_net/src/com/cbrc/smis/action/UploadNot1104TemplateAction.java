package com.cbrc.smis.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

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
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsMCurUnitDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
/**
 * ������ģ���ļ�������������
 * 
 * @author rds
 * @date 2005-12-5
 */
public class UploadNot1104TemplateAction extends Action {
	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Locale locale = getLocale(request);
		MessageResources resources=getResources(request);
		FitechMessages messages = new FitechMessages();

		MChildReportForm mChildReportForm = (MChildReportForm) form;
		FormFile tmplFile = mChildReportForm.getTemplateFile();
		
		String title = null;
		String curUnitName = null;
		boolean flag=false;
		if(tmplFile!=null && tmplFile.getFileSize()>0){
			if(tmplFile.getContentType().equals(Config.FILE_CONTENTTYPE_EXCEL)){
				if(tmplFile.getFileSize()<Config.FILE_MAX_SIZE){
					String fileName = Config.WEBROOTPATH + tmplFile.getFileName();
					InputStream inStream = tmplFile.getInputStream();
					FileOutputStream outStream = new FileOutputStream(fileName);
					byte[] bytes=new byte[1024];        
					int index=0;       
					while((index = inStream.read(bytes))!=-1){ 	  
						outStream.write(bytes,0,index);
					}
					outStream.close();
					inStream.close();
				
					FileInputStream fileInStream = null;
					HSSFWorkbook sourceWb = null;
					HSSFSheet sheet = null;
					
					try {
						fileInStream = new FileInputStream(fileName);
						POIFSFileSystem srcFile = new POIFSFileSystem(fileInStream);
						sourceWb = new HSSFWorkbook(srcFile);
						if (sourceWb.getNumberOfSheets() > 0) {
							sheet = sourceWb.getSheetAt(0);
						}
						fileInStream.close();
					}catch (FileNotFoundException e) {
						flag = false;
						e.printStackTrace();
					} catch (IOException e) {
						flag = false;
						e.printStackTrace();
					}	
					
					HSSFRow row = null;
				    HSSFCell cell = null;
				   
				    row = sheet.getRow(0);
				    if(row == null) messages.add(FitechResource.getMessage(locale,resources,"template.file.tableName.error"));
				    for(short i=row.getFirstCellNum(),n=row.getLastCellNum();i<n;i++){
				    	cell = (HSSFCell)row.getCell(i);
						if (cell == null) continue;
						if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
							title = cell.getStringCellValue();
							break;
						}
					}
				    if(title == null || title.trim().equals("")) messages.add(FitechResource.getMessage(locale,resources,"template.file.tableName.error"));
				    
				    row = sheet.getRow(1);
				    if(row == null) messages.add("template.file.rowNum.error"); 
				    boolean bool_bskj = false;
			    	boolean bool_bbrq = false;
			    	boolean bool_hbdw = false;
			    	String bskj = "���Ϳھ���";
			    	String bbrq = "�������ڣ�";
			    	String hbdw = "���ҵ�λ��";
				    for(short i=row.getFirstCellNum(),n=row.getLastCellNum();i<n;i++){
				    	if(bool_bskj == true && bool_bbrq == true && bool_hbdw == true) break;
				    	cell = (HSSFCell)row.getCell(i);
				    	
				    	if(cell == null) continue;
				    	if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
				    		String string = cell.getStringCellValue();
				    		if(string.trim().indexOf(bskj) > -1) {
				    			bool_bskj = true;
				    			continue;
				    		}
				    		if(string.trim().indexOf(bbrq) > -1){
				    			bool_bbrq = true;
				    			continue;
				    		}
				    		if(string.trim().indexOf(hbdw) > -1){
				    			curUnitName = string.trim().substring(string.trim().indexOf(hbdw) + hbdw.length());
				    			bool_hbdw = true;
				    			continue;
				    		}
				    	}
				    }
					if(bool_bskj != true){	//�Զ��屨��"���Ϳھ���"���벻��ȷ
						messages.add(FitechResource.getMessage(locale,resources,"template.file.bskj.error"));
						flag = false;
					}
					if(bool_bbrq != true){	//�Զ��屨��"�������ڣ�"���벻��ȷ
						messages.add(FitechResource.getMessage(locale,resources,"template.file.bbrq.error"));
						flag = false;
					}
					if(bool_hbdw != true){	//�Զ��屨��"���ҵ�λ��"���벻��ȷ
						if(curUnitName == null || curUnitName.trim().equals(""))
							messages.add(FitechResource.getMessage(locale,resources,"template.file.nohbdw.error"));
						else messages.add(FitechResource.getMessage(locale,resources,"template.file.hbdw.error"));
						flag = false;
					}else{
						Integer curUnitId = StrutsMCurUnitDelegate.getCurUnitID(curUnitName);
						if(curUnitId == null){
							messages.add(FitechResource.getMessage(locale,resources,"template.file.hbdw.notExist"));
							bool_hbdw = false;
							flag = false;
						}
					}
					if(bool_bskj == true && bool_bbrq == true && bool_hbdw == true){
						request.setAttribute("ReportTitle",title);
						request.setAttribute("ReportCurUnit",curUnitName);
						request.setAttribute("ReportStyle",Config.REPORT_STYLE_DD);  //Excel����ģ�������
						request.setAttribute("ReportName",tmplFile.getFileName());
						
						messages.add(FitechResource.getMessage(locale,resources,"upload.template.success"));
						flag = true;
					}					
				}else{  //���صı���ģ���ļ�����1M
					messages.add(FitechResource.getMessage(locale,resources,"upload.template.extreme"));
					flag = false;
				}
			}else{  //ѡ���ϴ��ı���ģ���ļ�����EXCEL���� 
				messages.add(FitechResource.getMessage(locale,resources,"template.file.contentTypeExcel.error"));
				flag = false;
			}
		}else{ //�ϴ��ı���ģ���ļ�������
			messages.add(FitechResource.getMessage(locale,resources,"get.upload.template.file.error"));
			flag = false;
		}
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
		
		if(flag==true)	//���뱨��ģ���ļ��ɹ������뱣�汣��ҳ��
			//if(reportStyle.compareTo(Config.REPORT_STYLE_DD)==0)
				return mapping.findForward("saveTmpt");  //��Ե�ʽ����ı���
			//}else{
			//	return mapping.findForward("saveTmptQD"); //�嵥ʽ����ģ��ı���
			//}
		else	//���뱨��ģ���ļ�ʧ�ܣ�����ԭҳ��
			return mapping.getInputForward();
		
//		if(tmplFile!=null){
//			session.setAttribute("tmptFile",tmplFile);
//			request.setAttribute("ReportTitle",null);
//			request.setAttribute("ReportCurUnit","��Ԫ");
//			request.setAttribute("ReportVesion","0512");
//			request.setAttribute("TmpFileName",tmplFile.getFileName());
//			request.setAttribute("ReportStyle",null);
//			flag=true;
//		}		
	}
}
