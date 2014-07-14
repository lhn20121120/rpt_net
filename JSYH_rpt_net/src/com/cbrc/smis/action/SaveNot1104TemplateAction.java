package com.cbrc.smis.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
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
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsNot1104TmplDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.form.MExcelChildReportForm;
import com.cbrc.smis.util.DataArea;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
/**
 * 
 * 
 * 
 * 
 */
public final class SaveNot1104TemplateAction extends Action {

   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {
	   Locale locale=getLocale(request);
	   MessageResources resources=getResources(request);
		
	   FitechMessages messages=new FitechMessages();
	   
//	   HttpSession session=request.getSession();
//	   FormFile tmptFile=(FormFile)session.getAttribute("tmptFile");
//	   if(tmptFile!=null) // System.out.println("tmptFile name is "+tmptFile.getFileName()+"  tmptFile size is "+tmptFile.getFileSize());
//	   boolean flag=false;
//	   MExcelChildReportForm mExcelChildReportForm=(MExcelChildReportForm)form;
//	   MChildReport mChildReport=null;
//	   
//	   if(mExcelChildReportForm!=null && tmptFile!=null){
//		   if(mExcelChildReportForm.getChildRepId().length()<5){
//			   mExcelChildReportForm=FitechEXCELReport.setMExcelChildReportFormRepId(mExcelChildReportForm);
//		   }
//		   flag=StrutsNot1104TmplDelegate.insertTemplate(mExcelChildReportForm,tmptFile);
//	   }
//	   if(flag==true){
//		   MCellFormuForm mCellForumForm=new MCellFormuForm();
//		   mCellForumForm.setReportName(tmptFile.getFileName());
//		   mCellForumForm.setChildRepId(mExcelChildReportForm.getChildRepId());
//		   mCellForumForm.setVersionId(mExcelChildReportForm.getVersionId());
//		   //mCellForumForm.setReportStyle(mExcelChildReportForm.getReportStyle());
//		   mCellForumForm.setReportStyle(mExcelChildReportForm.getRepStyleId());
//
//		   request.setAttribute("ObjForm",mCellForumForm);
//		   messages.add(FitechResource.getMessage(locale,resources,"save.success","template.msg"));
//	   }
//	   String saveEnd=null;
//	   if(flag==true){
//		   saveEnd="ģ�屣��ɹ���";
//		   request.setAttribute("saveEnd",saveEnd);
//	   }else{
//		   saveEnd="ģ�屣��ʧ�ܣ�";
//		   request.setAttribute("saveEnd",saveEnd);
//	   }
//	   
//	   // System.out.println(saveEnd);
////	   // System.out.println("flag is "+flag+" insertExcelTmpt is OK");
////	   if(flag==true) messages.add(FitechResource.getMessage(locale,resources,"ģ�屣��ɹ���"));
////	   else messages.add(FitechResource.getMessage(locale,resources,"ģ�屣��ʧ�ܣ�Ҫ�����ģ������Ѵ��ڣ�"));;
////	   request.setAttribute("Message",messages);
//	   return mapping.findForward("saveOK");
	   
	   MExcelChildReportForm mExcelChildReportForm=(MExcelChildReportForm)form;
	   RequestUtils.populate(mExcelChildReportForm,request);
	   
	   String tmplFile = mExcelChildReportForm.getTmpFileName()== null ? "" : mExcelChildReportForm.getTmpFileName();
	   boolean flag = false;
	   File file = null;
	   
	   if(!tmplFile.equals("")){ //���ݱ���İ汾�ţ�����ı���Ѵ��ڣ�������ֹ
		   if(mExcelChildReportForm.getReportName() != null && !mExcelChildReportForm.getReportName().trim().equals("")
				   && mExcelChildReportForm.getChildRepId() != null && !mExcelChildReportForm.getChildRepId().trim().equals("")
				   && mExcelChildReportForm.getVersionId() != null && !mExcelChildReportForm.getVersionId().trim().equals("")){
			   if(StrutsMChildReportDelegate.isChildReportExists(mExcelChildReportForm.getChildRepId()
					   ,mExcelChildReportForm.getVersionId()) == true){
				   messages.add(FitechResource.getMsg(locale,resources,"template.exists",
						   mExcelChildReportForm.getChildRepId(),
						   mExcelChildReportForm.getVersionId())
						   );
				   request.setAttribute("ReportTitle",request.getParameter("reportTitle"));
				   request.setAttribute("ReportCurUnit",request.getParameter("reportCurUnit"));
				   request.setAttribute("ReportVesion",mExcelChildReportForm.getVersionId());
				   request.setAttribute("ReportStyle",Config.REPORT_STYLE_DD);
				   request.setAttribute("ReportName",tmplFile);
				   request.setAttribute("TmpFileName",tmplFile);
			   }else{
				   
				   file = new File(Config.WEBROOTPATH + tmplFile);
				   
				   DataArea dataArea = new DataArea();
				   dataArea.setStartRow(mExcelChildReportForm.getStartRow());
				   dataArea.setStartCol(mExcelChildReportForm.getStartCol().toUpperCase());
				   dataArea.setEndRow(mExcelChildReportForm.getEndRow());
				   dataArea.setEndCol(mExcelChildReportForm.getEndCol().toUpperCase());
				    
				   /**��֤EXCEL���������Ч��*/
				   if(DataArea.isValid(dataArea)){
					   flag = StrutsNot1104TmplDelegate.insertTemplate(mExcelChildReportForm,file,dataArea);
					   
					   String msgKey = "";
					   if(flag==true){	//����ģ����Ϣ�ɹ�
						   msgKey="save.success";
						   MCellFormuForm mCellForumForm=new MCellFormuForm();
						   mCellForumForm.setReportName(mExcelChildReportForm.getReportName());
						   mCellForumForm.setChildRepId(mExcelChildReportForm.getChildRepId());
						   mCellForumForm.setVersionId(mExcelChildReportForm.getVersionId());
						   mCellForumForm.setReportStyle(mExcelChildReportForm.getRepStyleId());
				
						   request.setAttribute("ObjForm",mCellForumForm);
						   messages.add(FitechResource.getMessage(locale,resources,"save.success","template.msg"));
						   
						   //дģ�嵽reports/templatesĿ¼�£����ڻ�������ģ��
						   String templateFileName = Config.RAQ_TEMPLATE_PATH + com.fitech.net.config.Config.REPORT_NAME + File.separator 
		   							+ com.fitech.net.config.Config.TEMPLATE_NAME + File.separator + mExcelChildReportForm.getChildRepId() 
		   							+ "_" + mExcelChildReportForm.getVersionId() + ".xls";
						   FileInputStream inStream = new FileInputStream(file);
						   FileOutputStream outStream = new FileOutputStream(templateFileName);
						   byte[] bytes=new byte[1024];        
						   int index=0;       
						   while((index = inStream.read(bytes))!=-1){ 	  
							   outStream.write(bytes,0,index);
						   }
						   outStream.close();
						   inStream.close();
						   
						   //дģ�嵽report_mgr/excelĿ¼�£����ļ���Ҫ���ʵ����޸�
						   String excelFileName = Config.RAQ_TEMPLATE_PATH + "report_mgr" + File.separator + "excel" + File.separator 
						   			+ mExcelChildReportForm.getChildRepId() + mExcelChildReportForm.getVersionId() + ".xls";
						   try{
							   inStream = new FileInputStream(file);
							   POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
							   HSSFWorkbook book = new HSSFWorkbook(srcFile);
							   HSSFSheet sheet = null;
							   HSSFRow row = null;
							   HSSFCell cell = null;
							   if (book.getNumberOfSheets() > 0) {
									sheet = book.getSheetAt(0);
							   }
							   inStream.close();
							   for(Iterator iter=sheet.rowIterator();iter.hasNext();){
								   row = (HSSFRow)iter.next();
								   if(row == null) continue;
								   
								   String bskj = "���ͻ�����";
								   String bbrq = "�������ڣ�";
								   String tbr = "����ˣ�";
								   String fhr = "�����ˣ�";
								   String fzr = "�����ˣ�";
								   for(short i=row.getFirstCellNum(),n=row.getLastCellNum();i<n;i++){ 	
									   cell = (HSSFCell)row.getCell(i);
									   if(cell == null) continue;
									   if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
										   String string = cell.getStringCellValue();
										   
										   if(string.trim().equals(bskj)) {
											   cell.setEncoding(HSSFCell.ENCODING_UTF_16);
											   cell.setCellType(HSSFCell.CELL_TYPE_STRING);
											   cell.setCellValue(bskj+"${report.dataRangeName}");
											   continue;
										   }
										   if(string.trim().indexOf(bbrq) > -1){
											   cell.setEncoding(HSSFCell.ENCODING_UTF_16);
											   cell.setCellType(HSSFCell.CELL_TYPE_STRING);
											   cell.setCellValue(bbrq + "${report.year} ��${report.term} ��");
											   continue;
										   }
										   if(string.trim().indexOf(tbr) > -1){
											   cell.setEncoding(HSSFCell.ENCODING_UTF_16);
											   cell.setCellType(HSSFCell.CELL_TYPE_STRING);
											   cell.setCellValue(tbr + "${report.writer}");
											   continue;
										   }
										   if(string.trim().indexOf(fhr) > -1){
											   cell.setEncoding(HSSFCell.ENCODING_UTF_16);
											   cell.setCellType(HSSFCell.CELL_TYPE_STRING);
											   cell.setCellValue(fhr + "${report.checker}");
											   continue;
										   }if(string.trim().indexOf(fzr) > -1){
											   cell.setEncoding(HSSFCell.ENCODING_UTF_16);
											   cell.setCellType(HSSFCell.CELL_TYPE_STRING);
											   cell.setCellValue(fzr + "${report.principal}");
											   continue;
										   }
									   }
								   }
							   }
							   outStream = new FileOutputStream(excelFileName);
							   book.write(outStream);
							   outStream.close();
						   }catch(IOException e){
							   e.printStackTrace();
						   }catch (Exception e) {
								e.printStackTrace();
						   }
						   if(file.exists()) file.delete();
					   }else{	//����ģ����Ϣʧ��
						   request.setAttribute("ReportTitle",request.getParameter("reportTitle"));
						   request.setAttribute("ReportCurUnit",request.getParameter("reportCurUnit"));
						   request.setAttribute("ReportVesion",mExcelChildReportForm.getVersionId());
						   request.setAttribute("ReportStyle",mExcelChildReportForm.getRepStyleId());
						   request.setAttribute("ReportName",tmplFile);
						   request.setAttribute("TmpFileName",tmplFile);
						   msgKey = "save.fail";
						   messages.add(FitechResource.getMessage(locale,resources,msgKey,"template.msg"));
					   }
					   
					   FitechLog.writeLog(Config.LOG_OPERATION,
							   FitechResource.getMessage(locale,resources,msgKey,"template.msg"),
							   request);
				   }else{  //�����Excel��������Ч
					   request.setAttribute("ReportTitle",request.getParameter("reportTitle"));
					   request.setAttribute("ReportCurUnit",request.getParameter("reportCurUnit"));
					   request.setAttribute("ReportVesion",mExcelChildReportForm.getVersionId());
					   request.setAttribute("ReportStyle",mExcelChildReportForm.getRepStyleId());
					   request.setAttribute("ReportName",tmplFile);
					   request.setAttribute("TmpFileName",tmplFile);
					   messages.add(FitechResource.getMessage(locale,resources,"template.save.dataArea.error"));
				   } 
			   }
		   }else{  //δ��ȡ���������ơ��ӱ���ID���汾��
			   request.setAttribute("ReportTitle",request.getParameter("reportTitle"));
			   request.setAttribute("ReportCurUnit",request.getParameter("reportCurUnit"));
			   request.setAttribute("ReportVesion",mExcelChildReportForm.getVersionId());
			   request.setAttribute("ReportStyle",mExcelChildReportForm.getRepStyleId());
			   request.setAttribute("ReportName",tmplFile);
			   request.setAttribute("TmpFileName",tmplFile);
			   messages.add(FitechResource.getMessage(locale,resources,"template.save.param.error"));
		   }
	   }else{	//��ȡPDFģ���ļ��������ʱĿ¼�е��ļ���ʧ��
		   request.setAttribute("ReportTitle",request.getParameter("reportTitle"));
		   request.setAttribute("ReportCurUnit",request.getParameter("reportCurUnit"));
		   request.setAttribute("ReportVesion",mExcelChildReportForm.getVersionId());
		   request.setAttribute("ReportStyle",Config.REPORT_STYLE_DD);
		   request.setAttribute("ReportName",tmplFile);
		   request.setAttribute("TmpFileName",tmplFile);
		   messages.add(FitechResource.getMessage(locale,resources,"save.error","template.msg"));
	   }	
	   if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
	   
	   if(flag==true){	//���汨��ģ����Ϣ�ɹ���������ڱ���ϵ�趨ҳ��
		   return mapping.findForward("bjgxSet");
	   }else{	//���汨��ģ����Ϣʧ�ܣ�����ԭҳ��
		   return mapping.findForward("inputDD");
	   }
   }
}
