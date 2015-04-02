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
	 * 读取要修改的excel表
	 * @param name
	 * 			要修改的excel表的名称
	 * @param sheetName
	 * 			sheet名称
	 * @return
	 */
	public static boolean readExcel(String name,String sheetName){
		if(name == null || name.equals("")){
			return false;
		}
		//要修改的excel表的路径
		String templateFilePath ="E:/excel/"+name;
		//要获取的excel表中值的路径
		String templateFilePath2 = "E:/excel/校验公式(徽商已上线人行).xls";
		//要修改的excel表的列名
		String rankName = "余额";  
		try {
			HSSFRow row = null;
			HSSFCell cell = null;
			String msg = null;
			String message = null;
			FileInputStream stream = new FileInputStream(templateFilePath);
			POIFSFileSystem fs = new POIFSFileSystem(stream);
			// 创建对Excel工作簿文件的引用
			HSSFWorkbook book = new HSSFWorkbook(fs);
			// 在Excel文档中，第一张工作表的缺省索引是0，
			HSSFSheet sheet = book.getSheet(sheetName);
			stream.close();
			//循环读取数据
			for(int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++){
				// 读取左上端单元
				row = (HSSFRow) sheet.getRow(i); //获取当前行的值
				cell = (HSSFCell) row.getCell((short)0); //获取当前列的值
				//判断excel中值的类型String类型
				if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
					msg = cell.getStringCellValue();
				}
				//double类型
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
	 * 获取excel中的值
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
			// 创建对Excel工作簿文件的引用
			HSSFWorkbook book = new HSSFWorkbook(fs);
			// 在Excel文档中，第一张工作表的缺省索引是0，
			HSSFSheet sheet = book.getSheetAt(0);
			stream.close();
			HSSFRow row = null;
			HSSFCell cell = null;
			//循环读取数据
			for(int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++){
				// 读取左上端单元
				row = sheet.getRow(i);  //获取每行的值
				cell = (HSSFCell) row.getCell((short)2); //获取指定列的值
				msg = cell.getStringCellValue();
				if(msg.indexOf("=")!=-1){  //判断值中是否有=号  
					if(msg.split("=")[0].indexOf("+") > 0){ //判断截取之后的值是否有+号
						array = msg.split("=")[0].toString().split("]")[0].substring(3);
						if(array.equals(message)){
							cell = (HSSFCell) row.getCell((short)6); //获取指定版本的值
							if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
								id = Integer.valueOf(cell.getStringCellValue());
							}
							if(id > VERSION_ID){ //选择版本最高的校验公式
								VERSION_ID = id;
								info = msg;
							}
							continue;
						}
					}else{
						array = msg.substring(msg.split("=")[0].indexOf("[")+3,msg.split("=")[0].lastIndexOf("]"));
						if(array.equals(message)){
							cell = (HSSFCell) row.getCell((short)6); //获取指定版本的值
							if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
								id = Integer.valueOf(cell.getStringCellValue());
							}
							if(id > VERSION_ID){ //选择版本最高的校验公式
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
	 * 把值写入到excel中
	 */
	public static void writeExcel(String message,String id,String sheetName,String rankName,String templateFilePath){
		try {
			HSSFRow row = null;
			HSSFCell cell = null;
			String msg = null;
			int count = 0;  //记录要写入到哪一列的下标
			int bian = 0;  //记录要写入到哪一行的下标
			FileInputStream stream = new FileInputStream(templateFilePath);
			POIFSFileSystem fs = new POIFSFileSystem(stream);
			// 创建对Excel工作簿文件的引用
			HSSFWorkbook book = new HSSFWorkbook(fs);
			// 在Excel文档中，第一张工作表sheetmingc，
			HSSFSheet sheet = book.getSheet(sheetName);
			stream.close();
			//循环判断要要写入到哪一列
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
							if(msg.equals(rankName)){   //判断是否要写入到这一列
								count = j;
								break;
							}
							if(msg.equals(id)){ //判断是否在这一行
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
		//c.getExcel("E:/excel/校验公式(徽商已上线人行).xls", "");
		//writeExcel("","");
//		System.out.println("请输入要修改的excel名称:");
//		BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
//		String amend = reader1.readLine();
//		System.out.println("请输入要修改的excel所在的sheet名称:");
//		BufferedReader reader4 = new BufferedReader(new InputStreamReader(System.in));
//		String sheetName = reader4.readLine();
//		c.readExcel(amend, sheetName);
		
//		HSSFRow row = null;
//		HSSFCell cell = null;
//		String msg = null;
//		FileInputStream stream = new FileInputStream("F:/副本bug清单.xls");
//		POIFSFileSystem fs = new POIFSFileSystem(stream);
//		// 创建对Excel工作簿文件的引用
//		HSSFWorkbook book = new HSSFWorkbook(fs);
//		// 在Excel文档中，第一张工作表sheetmingc，
//		HSSFSheet sheet = book.getSheetAt(1);
//		stream.close();
//		row = sheet.createRow(0);
//		cell= row.createCell((short) 0);
//		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//		cell.setCellValue("00000877723");
//		FileOutputStream outstream = new FileOutputStream("F:/副本bug清单.xls");
//		book.write(outstream);
//		outstream.close();
	}
}
