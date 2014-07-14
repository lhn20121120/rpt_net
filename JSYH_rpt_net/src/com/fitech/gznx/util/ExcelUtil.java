package com.fitech.gznx.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jxls.XLSTransformer;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.excel.StyleCellProcessor;
import com.cbrc.smis.form.ReportInForm;
import com.fitech.gznx.common.FileUtil;

public class ExcelUtil {
	
	public static String Key_TemplateInfo = "Key_TemplateInfo";
	
	public static void writeDataExcel(Map map,HttpServletResponse response) throws Exception{
		String fileName=writeExcel(map);
		if(fileName!=null){
			downLoadExcel(fileName, response);
			
			new File(fileName).delete();
		}
	}
	
	/**
	 * �������ݿ��в�ѯ�����ݷ���map�У���д��excel
	 * @param cellMap
	 * @return
	 * @throws Exception
	 */
	public static String writeExcel(Map cellMap) throws Exception
	{
		String path=(String)cellMap.get("path");
		String temp=Config.TEMP_DIR;
		/*����Ŀ¼*/
		String newpath=path.substring(path.lastIndexOf(Config.FILESEPARATOR)+1,path.length());
		File fl=new File(path);
		if(!fl.exists()){throw new Exception(path+"�޴�excelģ��!");}
		File file = new File(new File(temp),newpath);
		if(!file.getParentFile().exists()){file.getParentFile().mkdirs();}
		FileUtil.copyFile(fl, file);
		String[] ARRCOLS =
		{ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
				"U", "V", "W", "X", "Y", "Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
		
		HSSFWorkbook sourceWb = null;
		HSSFSheet sheet = null;
		FileInputStream inStream = new FileInputStream(path);
		POIFSFileSystem srcPOIFile = new POIFSFileSystem(inStream);
		sourceWb = new HSSFWorkbook(srcPOIFile);
		if (sourceWb.getNumberOfSheets() > 0)
		{
			sheet = sourceWb.getSheetAt(0);
		}
		inStream.close();

		HSSFRow row = null;
		HSSFCell cell = null;
		String tempateInfo = (String)cellMap.get(Key_TemplateInfo);//�õ�������
		for (Iterator iter = sheet.rowIterator(); iter.hasNext();)
		{
			row = (HSSFRow) iter.next();
			for (short i = row.getFirstCellNum(), n = row.getLastCellNum(); i < n; i++)
			{
				cell = (HSSFCell) row.getCell(i);
				if (cell == null)
					continue;
				/* ����Ƿ��Ƿǹ�ʽ��Ԫ�� */
//				HSSFCellStyle cs=cell.getCellStyle();
//				cs.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
				if (cell.getCellType() != HSSFCell.CELL_TYPE_FORMULA)
				{
					if (cell.getCellNum() >= ARRCOLS.length)
						continue;
					String cellName = ARRCOLS[cell.getCellNum()]
							+ (row.getRowNum() + 1);
					/*����õ�Ԫ����Map����ֵ���ֵд��õ�Ԫ��*/
					if (cellMap.get(cellName) != null)
					{
						String cellValue = (String) cellMap.get(cellName);
						try
						{
							//2013-11-08 LYF:�������ʮ�ҵ�13ϵ�к�14ϵ�У���Ҫ���⴦��--��ֹ���ֿ�ѧ���㷨�����⣬ͬʱ��Ҫ������0��ͷ���ַ���(��000855956������Ӧ��Ϊ855956)
							if(((tempateInfo.startsWith("G13")||tempateInfo.startsWith("GF13"))&&cell.getCellNum()==2)//13ϵ�е�C��
								||((tempateInfo.startsWith("G14")||tempateInfo.startsWith("GF14"))&&cell.getCellNum()==3)){//14ϵͳ��D��
								cell.setCellValue(cellValue);//ֱ��ʹ�����ݿ��е�ֵ��������ֵת�����ֵ��쳣���
							}else{/*��ֵ�͵�Ԫ��*/
								Double d = Double.valueOf(cellValue);
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								cell.setCellValue(d);
							}
						}
						catch (Exception ex)
						{
							/*�ַ��͵�Ԫ��*/
							cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(cellValue);
						}
					}
				}
			}
		}
		FileOutputStream stream = new FileOutputStream(file);
		
		sourceWb.write(stream);
		stream.flush();
		stream.close();
		
		//�򿪵�����Excel�����¼������еĹ�ʽ���򲢱���
		POI2Util.excelFormulaEval(file);
		
		//�趨Excel��ʱ��������ռλ��Ϊ�����ʱ������
		setHeadAndFooter(file,cellMap);

		return file.getPath();
	}

	/**
	 * �趨Excel��ʱ��������ռλ��Ϊ�����ʱ������
	 * @param exclFilePath
	 * @param date
	 * @throws Exception
	 */
	public static void setHeadAndFooter(File exclFilePath, Map cellMap) throws Exception {
		String repInId=(String)cellMap.get("repInId");
		String reportFlg=(String)cellMap.get("reportFlg");
		
		if(repInId==null||repInId.length()==0||reportFlg==null||reportFlg.length()==0){
//				||!reportFlg.equals("1")){//Ŀǰ�趨ΪֻӰ��1104����
			return;
		}
		
		FileInputStream fis=null;		//�ļ�������
		BufferedInputStream bis=null;	//����������
		FileOutputStream fos= null;		//�ļ������
		try {
			fis=new FileInputStream(exclFilePath);           
			bis=new BufferedInputStream(fis);
			
			Map beans=new HashMap();
			ReportInForm reportInForm=null;
			if(reportFlg.equals("1")){//���౨��
				reportInForm=StrutsReportInDelegate.getReportIn(new Integer(repInId));
			}else{//�����౨��
				reportInForm=com.fitech.gznx.service.AFReportDelegate.getReportIn(new Integer(repInId));
			}
			//�趨�������ƣ����ڵ�����ʱ����ʾ
			if(reportInForm.getOrgId()!=null && !reportInForm.getOrgId().trim().equals("")){
				com.fitech.gznx.po.AfOrg org = com.fitech.gznx.service.AFOrgDelegate.getOrgInfo(reportInForm.getOrgId());
				if(org!=null && reportInForm!=null){
					reportInForm.setOrgName(org.getOrgName());
				}
			}
			beans.put("report",reportInForm);
			//���ݸ�ʽ��:Ŀǰ��"�������ڣ� ${report.year} ��${report.term} ��"�滻Ϊ"�������ڣ�2013 ��6 ��"
			XLSTransformer transformer = new XLSTransformer();
			transformer.registerCellProcessor(new StyleCellProcessor());
			HSSFWorkbook sourceWb=transformer.transformXLS(bis, beans);
					
			fos = new FileOutputStream(exclFilePath);
			sourceWb.write(fos);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}finally{
			if(fis!=null) fis.close();
			if(bis!=null) bis.close();
			if(fos!=null) fos.close();
		}
	}
	/**
	 * ����excel
	 * @param fileName
	 * @param response
	 */
	public static void downLoadExcel(String fileName,HttpServletResponse response) {
		try {
			String newpath=fileName.substring(fileName.lastIndexOf(Config.FILESEPARATOR)+1,fileName.lastIndexOf(".xls"));
			String path = java.net.URLDecoder.decode(fileName, "utf-8");
			File file = new File(path);
			if (!file.exists()) {
				response.setContentType("text/html;charset=utf-8");
				System.out.println("ָ�����ļ�"+file.getPath()+"������!");
				return;
			}
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + newpath+".xls");
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			OutputStream out = response.getOutputStream();
			long j=0;
			byte[] b=new byte[1024];
			while(j<file.length()){
				int k=bis.read(b, 0, 1024);
				j+=k;
				out.write(b, 0, k);
			}
			out.flush();
			out.close();
			bis.close();
			fis.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
