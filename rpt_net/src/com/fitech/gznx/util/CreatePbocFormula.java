package com.fitech.gznx.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class CreatePbocFormula {

	
	/**
	 * ��ȡҪ�޸ĵ�excel��
	 * @param name
	 * 			Ҫ�޸ĵ�excel�������
	 * @param sheetName
	 * 			sheet����
	 * @return
	 */
	public static boolean readExcel(String name,String sheetName){
		if(name == null || name.equals("")){
			return false;
		}
		//Ҫ�޸ĵ�excel���·��
		String templateFilePath ="E:/excel/"+name;
		//Ҫ��ȡ��excel����ֵ��·��
		String templateFilePath2 = "E:/excel/У�鹫ʽ(��������������).xls";
		//Ҫ�޸ĵ�excel�������
		String rankName = "���";  
		try {
			HSSFRow row = null;
			HSSFCell cell = null;
			String msg = null;
			String message = null;
			FileInputStream stream = new FileInputStream(templateFilePath);
			POIFSFileSystem fs = new POIFSFileSystem(stream);
			// ������Excel�������ļ�������
			HSSFWorkbook book = new HSSFWorkbook(fs);
			// ��Excel�ĵ��У���һ�Ź������ȱʡ������0��
			HSSFSheet sheet = book.getSheet(sheetName);
			stream.close();
			//ѭ����ȡ����
			for(int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++){
				// ��ȡ���϶˵�Ԫ
				row = (HSSFRow) sheet.getRow(i); //��ȡ��ǰ�е�ֵ
				cell = (HSSFCell) row.getCell((short)0); //��ȡ��ǰ�е�ֵ
				//�ж�excel��ֵ������String����
				if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
					msg = cell.getStringCellValue();
				}
				//double����
				if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
					Double msg2 = cell.getNumericCellValue();
					if(msg2 == msg2.longValue()){
						msg = String.valueOf(msg2.longValue());
					}
				}
				System.out.println("msg:"+msg);
				message = getExcel(templateFilePath2, msg);
				if(message != null){
					writeExcel(message, msg, sheetName, rankName, templateFilePath);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return false;
	}
	/**
	 * ��ȡexcel�е�ֵ
	 * @param templateFilePath
	 * @param message
	 * @return
	 */
	public static String getExcel(String templateFilePath,String message){
		if(templateFilePath == null || templateFilePath.equals("")){
			return "";
		}
		String msg = null;
		String array = null;
		String info="";
		Integer id =0;
		Integer VERSION_ID =0;
		try {
			FileInputStream stream = new FileInputStream(templateFilePath);
			POIFSFileSystem fs = new POIFSFileSystem(stream);
			// ������Excel�������ļ�������
			HSSFWorkbook book = new HSSFWorkbook(fs);
			// ��Excel�ĵ��У���һ�Ź������ȱʡ������0��
			HSSFSheet sheet = book.getSheetAt(0);
			stream.close();
			HSSFRow row = null;
			HSSFCell cell = null;
			//ѭ����ȡ����
			for(int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++){
				// ��ȡ���϶˵�Ԫ
				row = sheet.getRow(i);  //��ȡÿ�е�ֵ
				cell = (HSSFCell) row.getCell((short)2); //��ȡָ���е�ֵ
				msg = cell.getStringCellValue();
				if(msg.indexOf("=")!=-1){  //�ж�ֵ���Ƿ���=��  
					if(msg.split("=")[0].indexOf("+") > 0){ //�жϽ�ȡ֮���ֵ�Ƿ���+��
						array = msg.split("=")[0].toString().split("]")[0].substring(3);
						if(array.equals(message)){
							cell = (HSSFCell) row.getCell((short)6); //��ȡָ���汾��ֵ
							if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
								id = Integer.valueOf(cell.getStringCellValue());
							}
							if(id > VERSION_ID){ //ѡ��汾��ߵ�У�鹫ʽ
								VERSION_ID = id;
								info = msg;
							}
							continue;
						}
					}else{
						array = msg.substring(msg.split("=")[0].indexOf("[")+3,msg.split("=")[0].lastIndexOf("]"));
						if(array.equals(message)){
							cell = (HSSFCell) row.getCell((short)6); //��ȡָ���汾��ֵ
							if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
								id = Integer.valueOf(cell.getStringCellValue());
							}
							if(id > VERSION_ID){ //ѡ��汾��ߵ�У�鹫ʽ
								VERSION_ID = id;
								info = msg;
							}
							continue;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return info;
	}
	/**
	 * ��ֵд�뵽excel��
	 */
	public static void writeExcel(String message,String id,String sheetName,String rankName,String templateFilePath){
		try {
			HSSFRow row = null;
			HSSFCell cell = null;
			String msg = null;
			int count = 0;  //��¼Ҫд�뵽��һ�е��±�
			int bian = 0;  //��¼Ҫд�뵽��һ�е��±�
			FileInputStream stream = new FileInputStream(templateFilePath);
			POIFSFileSystem fs = new POIFSFileSystem(stream);
			// ������Excel�������ļ�������
			HSSFWorkbook book = new HSSFWorkbook(fs);
			// ��Excel�ĵ��У���һ�Ź�����sheetmingc��
			HSSFSheet sheet = book.getSheet(sheetName);
			stream.close();
			//ѭ���ж�ҪҪд�뵽��һ��
			for(int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++){
				row = (HSSFRow) sheet.getRow(i);
				if(row != null){
					for(int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++){
						cell = row.getCell((short)j);
						if(cell != null){
							if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
								msg = cell.getStringCellValue();
							}
							if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
								Double msg2 = cell.getNumericCellValue();
								if(msg2 == msg2.longValue()){
									msg = String.valueOf(msg2.longValue());
								}
							}
							if(msg.equals(rankName)){   //�ж��Ƿ�Ҫд�뵽��һ��
								count = j;
								break;
							}
							if(msg.equals(id)){ //�ж��Ƿ�����һ��
								bian = j;
							}
						}
					}
					cell = row.getCell((short)bian);
					if(cell != null){
						if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
							msg = cell.getStringCellValue();
						}
						if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
							Double msg2 = cell.getNumericCellValue();
							if(msg2 == msg2.longValue()){
								msg = String.valueOf(msg2.longValue());
							}
						}
						if(msg.equals(id)){
							cell = row.getCell((short)count);
							msg = cell.getStringCellValue();
							if(msg.equals(rankName)){
								continue;
							}
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(message);
							FileOutputStream outstream = new FileOutputStream(templateFilePath);
							book.write(outstream);
							outstream.close();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] args) throws IOException {
//		CreatePbocFormula c =new CreatePbocFormula();
//		c.readExcel("A1411.XLS","<nverify>");
		//E:/excel/A3421.XLS  <wverify>  <nverify>
		//c.getExcel("E:/excel/У�鹫ʽ(��������������).xls", "");
		//writeExcel("","");
//		System.out.println("������Ҫ�޸ĵ�excel����:");
//		BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
//		String amend = reader1.readLine();
//		System.out.println("������Ҫ�޸ĵ�excel���ڵ�sheet����:");
//		BufferedReader reader4 = new BufferedReader(new InputStreamReader(System.in));
//		String sheetName = reader4.readLine();
//		c.readExcel(amend, sheetName);
		
//		HSSFRow row = null;
//		HSSFCell cell = null;
//		String msg = null;
//		FileInputStream stream = new FileInputStream("F:/����bug�嵥.xls");
//		POIFSFileSystem fs = new POIFSFileSystem(stream);
//		// ������Excel�������ļ�������
//		HSSFWorkbook book = new HSSFWorkbook(fs);
//		// ��Excel�ĵ��У���һ�Ź�����sheetmingc��
//		HSSFSheet sheet = book.getSheetAt(1);
//		stream.close();
//		row = sheet.createRow(0);
//		cell= row.createCell((short) 0);
//		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//		cell.setCellValue("00000877723");
//		FileOutputStream outstream = new FileOutputStream("F:/����bug�嵥.xls");
//		book.write(outstream);
//		outstream.close();
	}
}
