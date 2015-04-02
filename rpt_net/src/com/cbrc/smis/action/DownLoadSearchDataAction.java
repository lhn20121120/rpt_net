package com.cbrc.smis.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.cbrc.smis.common.Config;

public class DownLoadSearchDataAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<Object[]> objList = (List<Object[]>)request.getSession().getAttribute("objList");
		//д����ʱ�ļ�
		String srcFileName=Config.TEMP_DIR+File.separator+"result_"+System.currentTimeMillis()+".xls";
		File file = new File(srcFileName);
		if(file.exists())
			file.delete();
		//����excel
		//createSearchDataExcel(objList,srcFileName);
		updateExcel(objList, srcFileName);
		//����
		
		response.reset();
	    response.setContentType("application/octet-stream;charset=gb2312"); 
	    response.addHeader("Content-Disposition","attachment; filename="+ URLEncoder.encode("result.xls","gb2312"));
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
	
	
	private void createSearchDataExcel(List<Object[]> objList,String path)throws Exception{
		HSSFWorkbook wbCreat = new HSSFWorkbook();
		HSSFSheet sheetCreate = wbCreat.createSheet("Sheet1");
		//�����½�excel Sheet���е�ͷ��
		HSSFRow rowHead = sheetCreate.createRow(0);
		String[] headStr = {"���� ","��Ԫ��","��Ԫ������","��Ԫ��ֵ","��������","�汾","����"};
		
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
		
		 FileOutputStream fileOut = new FileOutputStream(path);
	     wbCreat.write(fileOut);
	     fileOut.close();
		
	}
	
	private void updateExcel(List<Object[]> objList,String path) throws Exception{
		//���ݲ�ѯģ������excel�ļ�����
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(Config.XLSFILEPATH)));
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
