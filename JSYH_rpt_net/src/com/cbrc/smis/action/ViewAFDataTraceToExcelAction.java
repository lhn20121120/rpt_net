package com.cbrc.smis.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.AFDataTraceForm;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.service.IAFDataTraceService;
import com.cbrc.smis.service.impl.AFDataTraceServiceImpl;
import com.cbrc.smis.util.FitechException;

public class ViewAFDataTraceToExcelAction extends Action {
	private static FitechException log=new FitechException(ViewAFDataTraceToExcelAction.class);
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/**��ȡ��ѯ����-------��ʼ*/
		String reportTerm = request.getParameter("reportTerm");
		String repName = request.getParameter("repName");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String orgId = request.getParameter("orgId");
		AFDataTraceForm traceForm = (AFDataTraceForm)form;
		if(traceForm==null)
			traceForm = new AFDataTraceForm();
		
		traceForm.setReportTerm(reportTerm);
		traceForm.setRepName(repName);
		traceForm.setBeginDate(beginDate);
		traceForm.setEndDate(endDate);
		/**��ȡ��ѯ����-------����*/
		
		IAFDataTraceService traceService = new AFDataTraceServiceImpl();//������
		List<AFDataTraceForm> fromList = null;
	
	    /***
         * ȡ�õ�ǰ�û���Ȩ����Ϣ
         */   
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);          

		int recordCount =0; //��¼����		
		ReportInForm reportInForm = new ReportInForm();
		
		if(traceForm.getReportTerm() == null || traceForm.getReportTerm().equals("")){//��������
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			reportInForm.setYear(new Integer(yestoday.substring(0,4)));		   
			reportInForm.setTerm(new Integer(yestoday.substring(5, 7)));
			reportInForm.setDay(new Integer(yestoday.substring(8,10)));
			reportInForm.setDate(yestoday.substring(0, 10));
			traceForm.setReportTerm(yestoday.substring(0, 10));

		}else{
			reportInForm.setYear(new Integer(traceForm.getReportTerm().substring(0, 4)));
			reportInForm.setTerm(new Integer(traceForm.getReportTerm().substring(5, 7)));
			reportInForm.setDate(traceForm.getReportTerm());//���뱨������
		}
		reportInForm.setOrgId(operator.getOrgId());//�����û�����
		//List����ĳ�ʼ��
		List<Aditing> resList=null;
		try{
			if(reportInForm.getOrgId() == null)
				reportInForm.setOrgId(operator.getOrgId());
			StrutsMRepRangeDelegate delegate=new StrutsMRepRangeDelegate();
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			recordCount=delegate.selectDBReportCount(reportInForm, operator);
			if(recordCount > 0){
				/**��ʹ��hibernate ���Ը� 2011-12-21**/
				resList=delegate.selectDBReportRecord(reportInForm, operator);
			}
		}catch(Exception ex){
			log.printStackTrace(ex);
		}
		if(resList!=null && resList.size()>0){
			String repInIds = "(";
			for(int i=0;i<resList.size();i++){
				Aditing a = resList.get(i);
				repInIds +="'"+a.getRepInId()+"'";
				if(i!=resList.size()-1)
					repInIds +=",";
			}
			repInIds += ")";
			traceForm.setRepInIds(repInIds);
		}
	    fromList = traceService.findListByAFDataTrace(traceForm);

		List<Object[]> objList = new ArrayList<Object[]>();
		if(fromList!=null && fromList.size()>0){
			for(AFDataTraceForm f : fromList){
				Object[] obj = new Object[8];
	
				obj[0] = f.getRepName();//��������
				obj[1] = f.getReportTerm();//����
				obj[2] = f.getUsername();//�޸���
				obj[3] = f.getDateTime();//�޸�ʱ��
				obj[4] = f.getOriginalData();//ԭʼֵ
				obj[5] = f.getChangeData();//����ֵ
				obj[6] = f.getFinalData();//����ֵ
				obj[7] = f.getDescTrace();//��ע
				objList.add(obj);
			}
		}
		
		//д����ʱ�ļ�
		String srcFileName=Config.TEMP_DIR+File.separator+"trace_list_"+System.currentTimeMillis()+".xls";
		File file = new File(srcFileName);
		if(file.exists())
			file.delete();
		
		//����excel
		//createAFDataTraceToExcel(objList,srcFileName);
		updateAFDataTraceToExcel(objList,srcFileName);
		
		//����
		
		response.reset();
	    response.setContentType("application/octet-stream;charset=gb2312"); 
	    response.addHeader("Content-Disposition","attachment; filename="+ URLEncoder.encode("trace_data_list.xls","gb2312"));
	    response.setHeader("Accept-ranges", "bytes");
		
	    FileInputStream inStream = new FileInputStream(srcFileName);
	    int len;
		byte[] buffer = new byte[100];
			
		while((len = inStream.read(buffer)) > 0){
			response.getOutputStream().write(buffer,0,len);
		}
		inStream.close();
		//ɾ����ʱ�ļ�
		
		if(file.exists())
			file.delete();
		
		return null;
	}
	
	
	private void createAFDataTraceToExcel(List<Object[]> objList,String path)throws Exception{
		HSSFWorkbook wbCreat = new HSSFWorkbook();
		HSSFSheet sheetCreate = wbCreat.createSheet("Sheet1");
		//�����½�excel Sheet���е�ͷ��
		HSSFRow rowHead = sheetCreate.createRow(0);

		String[] headStr = {"�������� ","����","�޸���","�޸�ʱ��","ԭʼֵ","����ֵ","�������","��ע"};
		
		for(int i=0;i<headStr.length;i++){
			HSSFCell cellHead = rowHead.createCell((short)i);
			HSSFCellStyle cellStyle = cellHead.getCellStyle();
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			HSSFFont font = wbCreat.createFont();
			//font.setFontHeight(HSSFFont.);
			//font.setBoldweight(HSSFFont.SS_SUB);
			cellStyle.setFont(font);
			cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			
			cellStyle.setFillBackgroundColor(HSSFColor.AQUA.index);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setBottomBorderColor(HSSFColor.BLUE.index);
			cellHead.setCellStyle(cellStyle);
			cellHead.setEncoding(HSSFWorkbook.ENCODING_UTF_16);
			cellHead.setCellValue(headStr[i]);
		}
		
		if(objList!=null && objList.size()>0){
			for(int i=0;i<objList.size();i++){
				//�����½�excel Sheet����
				HSSFRow rowCreat = sheetCreate.createRow(i+1);
				Object[] obj = objList.get(i);
				
				for(int j=0;j<obj.length;j++){
					//�µĵ�Ԫ��
					
					HSSFCell cellCreat = rowCreat.createCell((short)j);
					HSSFCellStyle cellStyle = cellCreat.getCellStyle();
					cellStyle.setFillPattern(HSSFCellStyle.NO_FILL);
					cellCreat.setCellStyle(cellStyle);
					cellCreat.setEncoding(HSSFWorkbook.ENCODING_UTF_16);
					
					cellCreat.setCellValue(obj[j]==null?"-":obj[j].toString());
				}
				
			}
		}
		
		 FileOutputStream fileOut = new FileOutputStream(path);
	     wbCreat.write(fileOut);
	     fileOut.close();
		
	}
	
	public void updateAFDataTraceToExcel(List<Object[]> objList,String path)throws Exception{
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(Config.TRACEFILEPATH)));
		HSSFSheet sheet = workbook.getSheetAt(0);
		for(int i=0;i<objList.size();i++){
			//�����½�excel Sheet����
			HSSFRow rowCreat = sheet.createRow(i+1);
			Object[] obj = objList.get(i);
			
			for(int j=0;j<obj.length;j++){
				//�µĵ�Ԫ��
				
				HSSFCell cellCreat = rowCreat.createCell((short)j);
				HSSFCellStyle cellStyle = cellCreat.getCellStyle();
				cellStyle.setFillPattern(HSSFCellStyle.NO_FILL);
				cellCreat.setCellStyle(cellStyle);
				cellCreat.setEncoding(HSSFWorkbook.ENCODING_UTF_16);
				
				cellCreat.setCellValue(obj[j]==null?"-":obj[j].toString());
			}
			
		}
		FileOutputStream fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
	    fileOut.close();
		
	}
}
