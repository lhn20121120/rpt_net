/*
 * Created on 2006-5-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.obtain.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.common.FileParser;
import com.fitech.net.config.Config;
import com.fitech.net.obtain.excel.dao.ObtainManager;
import com.fitech.net.obtain.text.Formula;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SheetHandle {
	private String repID;
	private String versionID;	
	public static int sheetIndex = 0;
	
	public SheetHandle(String repID, String versionID){
		this.setRepID(repID);
		this.setVersionID(versionID);
	}
	
	public boolean releaseTemplate(String ObtainTemplates, String releaseTemplates, String excelName){
		boolean resultFlag = false;
		FileInputStream inStream = null;
		FileInputStream inStream1 = null;
		HSSFWorkbook targetWb = null;
		HSSFWorkbook sourceWb = null;
		FileOutputStream outStream = null;
		String srcExcleUrl = ObtainTemplates;
		String objExcelUrl = releaseTemplates;
		try {
			inStream1 = new FileInputStream(srcExcleUrl);
			inStream = new FileInputStream(objExcelUrl);	
			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
			POIFSFileSystem srcFile1= new POIFSFileSystem(inStream1);
			targetWb = new HSSFWorkbook(srcFile);
			sourceWb = new HSSFWorkbook(srcFile1);
			HSSFSheet sourceSheet = sourceWb.getSheetAt(0);
			HSSFSheet targetSheet = targetWb.getSheetAt(0);
			copySingleSheetValue(sourceSheet, targetSheet);
			//copyRows(targetWb, sourceWb, targetSheet, sourceSheet);
			outStream = new FileOutputStream(objExcelUrl);
			targetWb.write(outStream);
			outStream.flush();
			inStream.close();
			inStream1.close();
			outStream.close();
			
			//按机构生成下载文件
			createExcelAccordingOrg(objExcelUrl, excelName);
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				inStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}		
		
		return resultFlag;
	}
	
	/**
	 * 发布模板 将取完数的模板分离出来并拷贝到相应文件夹
	 * @param templateFileName
	 * @return
	 */
	public boolean releaseTemplate(HSSFWorkbook sourceWb, String templateName, String releaseFileName){
		boolean resultFlag = false;
		
		FileInputStream inStream = null;
		//FileInputStream inStream1 = null;
		HSSFWorkbook targetWb = null;
		//HSSFWorkbook sourceWb = null;
		FileOutputStream outStream = null;
		//String srcExcleUrl = templateName;
		String objExcelUrl = Config.getReleaseTemplatePath() + File.separator + releaseFileName;
		try {
			//inStream1 = new FileInputStream(srcExcleUrl);
			inStream = new FileInputStream(objExcelUrl);	
			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
			//POIFSFileSystem srcFile1= new POIFSFileSystem(inStream1);
			targetWb = new HSSFWorkbook(srcFile);
			//sourceWb = new HSSFWorkbook(srcFile1);
			HSSFSheet sourceSheet = sourceWb.getSheetAt(0);
			HSSFSheet targetSheet = targetWb.getSheetAt(0);
			//copySingleSheetValue1(sourceSheet, targetSheet);
			copyRows(targetWb, sourceWb, targetSheet, sourceSheet);
			outStream = new FileOutputStream(objExcelUrl);
			targetWb.write(outStream);
			outStream.flush();
			inStream.close();
			//inStream1.close();
			outStream.close();
			
			//按机构生成下载文件
			createExcelAccordingOrg(objExcelUrl, releaseFileName);
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				inStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resultFlag;
	}
	
	private void createExcelAccordingOrg(String objExcelUrl, String releaseFileName){
		String repID = this.getRepID();
		String versionID = this.getVersionID();
		ArrayList al = ObtainManager.getOrgId(repID, versionID);
		for(int i=0,n=al.size(); i<n; i++){
			String folderName = Config.DATA_SOURCE_EXCEL + File.separator + (String)al.get(i);
			/*
			 * 判断该目录是否存在，不存在则建立
			 */
			File checkFile = new File(folderName);
			if (!checkFile.exists()) {
				checkFile.mkdir();
			}
			checkFile = null;
			String fileName = folderName + File.separator + releaseFileName;
			FileParser.copyFile(objExcelUrl, fileName);
			// System.out.println("ok");
		}
		
	}
	
	public void createOrgExcel(String srcExcelUrl,String objExcelUrl,String fileName){
		String mostLowerOrgIds = StrutsOrgNetDelegate.getMostLowerOrgIds();
		String[] orgIds = mostLowerOrgIds.split(",");
		for(int i=0;i<orgIds.length;i++){
			String orgWhere = objExcelUrl + File.separator + orgIds[i];
			File file = new File(orgWhere);
			if(!file.exists()){
				file.mkdir();
			}
			FileParser.copyFile(srcExcelUrl + File.separator + fileName,orgWhere + File.separator + fileName);
		}
	}
	
	/**
	 *　从配置文件中读取数据源名称获取其值并复制到对应Ｓｈｅｅｔ中	 
	 *@param templateFileName 模板名
	 *@return 执行状态，如果拷贝失败则返回ｆａｌｓｅ成功返回ｔｒｕｅ
	 */
	public  boolean copySheetsValue(String guid, String templateFileName, String releaseFileName){
		boolean resultFlag = false;
		
		/*
		 * 校验该template节点下有无datasource子节点的state属性为temp
		 * 如有直接返回false
		 */
		TemplateObtainConfigure templateObjConfigure = new TemplateObtainConfigure(
																	this.getRepID(), this.getVersionID());
		if (templateObjConfigure.checkDataSourceElement() == false) {
			return false;
		}else{
			/*
			 * 拷贝值
			 */
			FileInputStream inStream = null;
			HSSFWorkbook targetWb = null;
			FileOutputStream outStream = null;
			HashMap dsoXlsNameMap = templateObjConfigure.getDatasourceXlsName();
			try {
				inStream = new FileInputStream(templateFileName);
				
				POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
				targetWb = new HSSFWorkbook(srcFile);
				for(int i=1,n=targetWb.getNumberOfSheets(); i<n; i++){
					HSSFSheet targetSheet = targetWb.getSheetAt(i);
					String sourceSheetGuid = targetSheet.getHeader().getCenter();
					//取对应的包含数据的Excel文件对应的位置
					String xlsName = Config.DATA_SOURCE_EXCEL + File.separator + dsoXlsNameMap.get(sourceSheetGuid);
					
					HSSFSheet sourceSheet = getFirstSheet(xlsName);
					copySingleSheetValue(sourceSheet, targetSheet);
				}
				outStream = new FileOutputStream(templateFileName);
				inStream.close();
				targetWb.write(outStream);
				outStream.flush();
				outStream.close();
				
				/*
				 * 将值复制到发布文件中去
				 */
//				inStream = new FileInputStream(templateFileName);
//				srcFile = new POIFSFileSystem(inStream);
//				targetWb = new HSSFWorkbook(srcFile);
//				String sheetName = targetWb.getSheetName(0);				
//				HSSFSheet sheet = targetWb.getSheetAt(0);
				//releaseTemplate(targetWb, templateFileName, releaseFileName);
				//inStream.close();
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}		
		
		return resultFlag;
	}
	
	/**
	 *　从配置文件中读取数据源名称获取其值并复制到对应Ｓｈｅｅｔ中	 
	 *@param templateFileName 模板名
	 *@return 执行状态，如果拷贝失败则返回ｆａｌｓｅ成功返回ｔｒｕｅ
	 */
	public  boolean copySheetsValue2(String guid, String templateFileName, String releaseFileName,String orgId){
		boolean resultFlag = false;
		
		/*
		 * 校验该template节点下有无datasource子节点的state属性为temp
		 * 如有直接返回false
		 */
		TemplateObtainConfigure templateObjConfigure = new TemplateObtainConfigure(
																	this.getRepID(), this.getVersionID());
		if (templateObjConfigure.checkDataSourceElement() == false) {
			return false;
		}else{
			/*
			 * 拷贝值
			 */
			FileInputStream inStream = null;
			HSSFWorkbook targetWb = null;
			FileOutputStream outStream = null;
			HashMap dsoXlsNameMap = templateObjConfigure.getDatasourceXlsName();
			try {
				inStream = new FileInputStream(templateFileName);
				
				POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
				targetWb = new HSSFWorkbook(srcFile);
				for(int i=1,n=targetWb.getNumberOfSheets(); i<n; i++){
					HSSFSheet targetSheet = targetWb.getSheetAt(i);
					String sourceSheetGuid = targetSheet.getHeader().getCenter();
					//取对应的包含数据的Excel文件对应的位置
					String xlsName = Config.DATA_SOURCE_EXCEL + File.separator + orgId + File.separator + dsoXlsNameMap.get(sourceSheetGuid);
					File file = new File(xlsName);
					if(!file.exists()) {
						inStream.close();
						return resultFlag;
					}
					HSSFSheet sourceSheet = getFirstSheet(xlsName);
					if(sourceSheet == null) continue;
					copySingleSheetValue(sourceSheet, targetSheet);
				}
				outStream = new FileOutputStream(templateFileName);
				inStream.close();
				targetWb.write(outStream);
				outStream.flush();
				outStream.close();
				resultFlag = true;
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}		
		
		return resultFlag;
	}
	
	/**
	 *　从配置文件中读取数据源名称获取其值并复制到对应Ｓｈｅｅｔ中	 
	 *@param templateFileName 模板名
	 *@return 执行状态，如果拷贝失败则返回ｆａｌｓｅ成功返回ｔｒｕｅ
	 */
	public  boolean copyCellForum(String guid, String templateFileName, String releaseFileName){
		boolean resultFlag = false;
		
		/*
		 * 校验该template节点下有无datasource子节点的state属性为temp
		 * 如有直接返回false
		 */
		TemplateObtainConfigure templateObjConfigure = new TemplateObtainConfigure(this.getRepID(), this.getVersionID());
		if (templateObjConfigure.checkDataSourceElement() == false) {
			return false;
		}else{
			/*
			 * 拷贝值
			 */
			FileInputStream inStream = null;
			HSSFWorkbook targetWb = null;
			FileOutputStream outStream = null;
			HashMap dsoXlsNameMap = templateObjConfigure.getDatasourceXlsName();
			try {
				inStream = new FileInputStream(templateFileName);
				
				POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
				targetWb = new HSSFWorkbook(srcFile);
				
				for(int i=1,n=targetWb.getNumberOfSheets(); i<n; i++){
					HSSFSheet targetSheet = targetWb.getSheetAt(i);
					String sourceSheetGuid = targetSheet.getHeader().getCenter();
					//取对应的包含数据的Excel文件对应的位置
					String xlsName = Config.DATA_SOURCE_EXCEL + File.separator + dsoXlsNameMap.get(sourceSheetGuid);
					
					HSSFSheet sourceSheet = getFirstSheet(xlsName);
					copySingleSheetValue(sourceSheet, targetSheet);
				}
				outStream = new FileOutputStream(templateFileName);
				inStream.close();
				targetWb.write(outStream);
				outStream.flush();
				outStream.close();
				
				
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	
		/**
		 * while(sourceRowIter.hasNext()){		
			sourceRow = (HSSFRow)sourceRowIter.next();
			if (sourceRow == null) {
				continue;
			}
			for(short i=sourceRow.getFirstCellNum(),n=sourceRow.getLastCellNum(); i<n; i++){
				sourceCell = (HSSFCell)sourceRow.getCell(i);
				
				if (sourceCell == null) {
					continue;
				}
			   
			    cType = sourceCell.getCellType();		    		    			    
			    switch (cType){
			       case HSSFCell.CELL_TYPE_BLANK:
			       					break;
			       case HSSFCell.CELL_TYPE_BOOLEAN: 
			       					       					
			       					break;
			       case HSSFCell.CELL_TYPE_ERROR:
			       					
			        				break;            
			       case HSSFCell.CELL_TYPE_FORMULA:
			    	   				String s = sourceCell.getCellFormula();
			    	   				// System.out.println(s);
			        				break;
			       case HSSFCell.CELL_TYPE_NUMERIC:
							     
			        				break;
			       case HSSFCell.CELL_TYPE_STRING:	
			             
			        				break;	
			    }				
			}
		}
		 */
		
		/**
		 * 
		 * String mostLowerOrgIds = StrutsOrgNetDelegate.getMostLowerOrgIds();
		String[] orgIds = mostLowerOrgIds.split(",");
		for(int i=0;i<orgIds.length;i++){
			String orgWhere = objExcelUrl + File.separator + orgIds[i];
			File file = new File(orgWhere);
			if(!file.exists()){
				file.mkdir();
			}
			FileParser.copyFile(srcExcelUrl + File.separator + fileName,orgWhere + File.separator + fileName);
		}
		 */
		
		return resultFlag;
	}
	
	/**
	 * 得到一个ｘｌｓ文件的第一个ｓｈｅｅｔ
	 * @param xlsName　ｘｌｓ文件名
	 * @return　HSSFSheet
	 */
	private HSSFSheet getFirstSheet(String xlsName){
		FileInputStream inStream = null;
		HSSFWorkbook sourceWb = null;
		HSSFSheet sourceSheet = null;
		
		try {
			inStream = new FileInputStream(xlsName);
			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
			sourceWb = new HSSFWorkbook(srcFile);
			if (sourceWb.getNumberOfSheets() > 0) {
				sourceSheet = sourceWb.getSheetAt(0);
			}
			inStream.close();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return sourceSheet;
	}
	

	/**
	 * 将sourceSheet的值完全复制到targetSheet 中，只拷贝值
	 * @param sourceSheet　源ｓｈｅｅｔ
	 * @param targetSheet　目标ｓｈｅｅｔ
	 */
	private void copySingleSheetValue1(HSSFSheet sourceSheet, HSSFSheet targetSheet){
		
	    /*
	     * 如果sheet为null直接返回
	     */
	    if (targetSheet == null || sourceSheet == null) {
	    	return;
	    }
	    
		HSSFRow sourceRow = null;
	    HSSFRow targetRow = null;
	    HSSFCell sourceCell = null;
	    HSSFCell targetCell = null;		
	    int cType;
	    //拷贝行并填充数据
	    Iterator sourceRowIter = sourceSheet.rowIterator();
	    Iterator targetRowIter = targetSheet.rowIterator();
		short rowIndex = 0;
		while(sourceRowIter.hasNext() && targetRowIter.hasNext()){	
			
			sourceRow = (HSSFRow)sourceRowIter.next();
			targetRow = (HSSFRow)targetRowIter.next(); 
			if (sourceRow == null || targetRow == null) {
				continue;
			}
			for(short i=sourceRow.getFirstCellNum(),n=sourceRow.getLastCellNum(); i<n; i++){
				sourceCell = (HSSFCell)sourceRow.getCell(i);
				targetCell = (HSSFCell)targetRow.getCell(i);
				if (sourceCell == null || targetCell == null) {
					continue;
				}
			    /*
			     * 根据不同数据类型设置值
			     */
			    cType = sourceCell.getCellType();	
//			    targetCell.setEncoding(HSSFCell.ENCODING_UTF_16);
//			    targetCell.setCellType(cType);
//			    targetCell.getCellStyle().setDataFormat(sourceCell.getCellStyle().getDataFormat());				
			    		    			    
			    switch (cType){
			       case HSSFCell.CELL_TYPE_BLANK:
			       					break;
			       case HSSFCell.CELL_TYPE_BOOLEAN: 
			       					targetCell.setCellValue(sourceCell.getBooleanCellValue());			       					
			       					break;
			       case HSSFCell.CELL_TYPE_ERROR:
			       					targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
			        				break;            
			       case HSSFCell.CELL_TYPE_FORMULA:
			       					/*
			       					 * 读出公式储存格计算後的值,
			       					 * 此举迫不得已，因为poi对公式支持欠佳。
			       					 * 对apache的崇拜因此降低不少(wunaigang批注,呵呵)			       					 
			       					 */
			       	sourceCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
// System.out.println("*****************************CELL_TYPE_FORMULA.getNumericCellValue() = " + sourceCell.getNumericCellValue());
// System.out.println("*****************************CELL_TYPE_FORMULA.getNumericCellValue() = " + sourceCell.getStringCellValue());
// System.out.println("*****************************CELL_TYPE_FORMULA.toString() = " + sourceCell.toString());
			       
		       						targetCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		       						targetCell.setCellValue(sourceCell.getNumericCellValue());
			        				break;
			       case HSSFCell.CELL_TYPE_NUMERIC:
// System.out.println("*****************************sourceCell.getNumericCellValue() = " + sourceCell.getNumericCellValue());
							       	targetCell.setCellValue(sourceCell.getNumericCellValue());
			        				break;
			       case HSSFCell.CELL_TYPE_STRING:	
			       	targetCell.setCellValue(String.valueOf(sourceCell.getStringCellValue()));			       
			        				break;	
			    }				
			}
		}
	}

	/**
	 * 将sourceSheet的值完全复制到targetSheet 中，只拷贝值
	 * @param sourceSheet　源ｓｈｅｅｔ
	 * @param targetSheet　目标ｓｈｅｅｔ
	 */
	private void copySingleSheetValue(HSSFSheet sourceSheet, HSSFSheet targetSheet){
		
	    /*
	     * 如果sheet为null直接返回
	     */
	    if (targetSheet == null || sourceSheet == null) {
	    	return;
	    }
	    
		HSSFRow sourceRow = null;
	    HSSFRow targetRow = null;
	    HSSFCell sourceCell = null;
	    HSSFCell targetCell = null;		
	    int cType;
	    //拷贝行并填充数据
	    Iterator sourceRowIter = sourceSheet.rowIterator();
	    Iterator targetRowIter = targetSheet.rowIterator();
		short rowIndex = 0;
		while(sourceRowIter.hasNext() && targetRowIter.hasNext()){	
			
			sourceRow = (HSSFRow)sourceRowIter.next();
			targetRow = (HSSFRow)targetRowIter.next(); 
			if (sourceRow == null || targetRow == null) {
				continue;
			}
			for(short i=sourceRow.getFirstCellNum(),n=sourceRow.getLastCellNum(); i<n; i++){
				sourceCell = (HSSFCell)sourceRow.getCell(i);
				targetCell = (HSSFCell)targetRow.getCell(i);
				if (sourceCell == null || targetCell == null) {
					continue;
				}
			    /*
			     * 根据不同数据类型设置值
			     */
			    cType = sourceCell.getCellType();	
//			    targetCell.setEncoding(HSSFCell.ENCODING_UTF_16);
//			    targetCell.setCellType(cType);
//			    targetCell.getCellStyle().setDataFormat(sourceCell.getCellStyle().getDataFormat());				
			    		    			    
			    switch (cType){
			       case HSSFCell.CELL_TYPE_BLANK:
			       					break;
			       case HSSFCell.CELL_TYPE_BOOLEAN: 
			       					targetCell.setCellValue(sourceCell.getBooleanCellValue());			       					
			       					break;
			       case HSSFCell.CELL_TYPE_ERROR:
			       					targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
			        				break;            
			       case HSSFCell.CELL_TYPE_FORMULA:
			       					/*
			       					 * 读出公式储存格计算後的值,
			       					 * 此举迫不得已，因为poi对公式支持欠佳。
			       					 * 对apache的崇拜因此降低不少(wunaigang批注,呵呵)			       					 
			       					 */
		       						//targetCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		       						targetCell.setCellValue(sourceCell.getNumericCellValue());
			        				break;
			       case HSSFCell.CELL_TYPE_NUMERIC:
							       	targetCell.setCellValue(sourceCell.getNumericCellValue());
			        				break;
			       case HSSFCell.CELL_TYPE_STRING:	
			       	targetCell.setCellValue(String.valueOf(sourceCell.getStringCellValue()));			       
			        				break;	
			    }				
			}
		}
	}	
	
	/**
	 * 将sourceSheet的值完全复制到targetSheet 中，只拷贝值
	 * @param sourceSheet　源ｓｈｅｅｔ
	 * @param targetSheet　目标ｓｈｅｅｔ
	 */
	private void copySingleSheetValue(HSSFWorkbook book,HSSFSheet sourceSheet, HSSFSheet targetSheet){
		
	    /*
	     * 如果sheet为null直接返回
	     */
	    if (targetSheet == null || sourceSheet == null) {
	    	return;
	    }
	    
		HSSFRow sourceRow = null;
	    HSSFRow targetRow = null;
	    HSSFCell sourceCell = null;
	    HSSFCell targetCell = null;		
	    int cType;
	    //拷贝行并填充数据
	    Iterator sourceRowIter = sourceSheet.rowIterator();
	    Iterator targetRowIter = targetSheet.rowIterator();
		short rowIndex = 0;
		while(sourceRowIter.hasNext() && targetRowIter.hasNext()){	
			
			sourceRow = (HSSFRow)sourceRowIter.next();
			targetRow = (HSSFRow)targetRowIter.next(); 
			if (sourceRow == null || targetRow == null) {
				continue;
			}
			for(short i=sourceRow.getFirstCellNum(),n=sourceRow.getLastCellNum(); i<n; i++){
				sourceCell = (HSSFCell)sourceRow.getCell(i);
				targetCell = (HSSFCell)targetRow.getCell(i);
				if (sourceCell == null || targetCell == null) {
					continue;
				}
			    /*
			     * 根据不同数据类型设置值
			     */
			    cType = sourceCell.getCellType();				
			    		    			    
			    switch (cType){
			       case HSSFCell.CELL_TYPE_BLANK:
			       					break;
			       case HSSFCell.CELL_TYPE_BOOLEAN: 
			       					targetCell.setCellValue(sourceCell.getBooleanCellValue());			       					
			       					break;
			       case HSSFCell.CELL_TYPE_ERROR:
			       					targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
			        				break;            
			       case HSSFCell.CELL_TYPE_FORMULA:
			       					String formu = sourceCell.getCellFormula();
			       					try{
			       						if((formu.indexOf("+") > -1 || formu.indexOf("-") > -1 || formu.indexOf("*") >-1 || formu.indexOf("/") > -1) || formu.indexOf("!") > -1) 
				       						targetCell.setCellValue(this.getValue(book,formu));
//				       					else{
//				       						targetCell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
//				       						targetCell.setCellFormula(sourceCell.getNumericCellValue());
//				       					}
			       					}catch(Exception ex){
			       						
			       					}
			        				break;
			       case HSSFCell.CELL_TYPE_NUMERIC:
							       	targetCell.setCellValue(sourceCell.getNumericCellValue());
			        				break;
			       case HSSFCell.CELL_TYPE_STRING:	
			       	targetCell.setCellValue(String.valueOf(sourceCell.getStringCellValue()));			       
			        				break;	
			    }				
			}
		}
	}	
	
	/**
	 * 复制源Excel文件里面的Sheet到目标Excel文件 srcExcelName复制到objExcelUrl
	 * @param srcExcelUrl 数据源Excel文件
	 * @param objExcelUrl 目标Excel文件
	 * @param srcExcelRealName 数据源Excel事实文件名
	 * 
	 */
	public void copySheet(String srcExcelName, String objExcelUrl, String srcExcelRealName){
		
		FileInputStream inStream = null;
		FileInputStream inStream1 = null;
		HSSFWorkbook sourceWb = null;
		HSSFWorkbook targetWb = null;
		FileOutputStream outStream = null;
		String srcExcelUrl = Config.getTemplateTempFolderRealPath()+ File.separator + srcExcelName;
		
		try {
			inStream = new FileInputStream(srcExcelUrl);
			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
			sourceWb = new HSSFWorkbook(srcFile);			
			targetWb = new HSSFWorkbook();
			inStream.close();
			inStream1 = new FileInputStream(objExcelUrl);
			POIFSFileSystem targetFile = new POIFSFileSystem(inStream1);			
			targetWb = new HSSFWorkbook(targetFile);
			inStream1.close();
			/*
			 * 强制性规定一个Excel只能有一个sheet
			 */
			if (sourceWb.getNumberOfSheets() > 0) {				
				HSSFSheet sourceSheet = sourceWb.getSheetAt(0);
				String targetSheetName = "T_" + (String.valueOf(System.currentTimeMillis()));
				HSSFSheet targetSheet = targetWb.createSheet(targetSheetName);
				copyRows(targetWb, sourceWb, targetSheet, sourceSheet);
				copySingleSheetValue(sourceSheet,targetSheet);
				//设置为只读
				//targetSheet.setProtect(true);	
				/*
				 * 将GUID写入该sheet的header页眉，
				 * 为了建立sheet与其母体的映射关系，
				 * 此举迫不得已，防止用户更新sheet名
				 */
				HSSFHeader header = targetSheet.getHeader();
				String headerCenter = header.getCenter();
				/*
				 * 如果是第一次读将该guid写入head中作为的唯一标识
				 */
				if (headerCenter == null || headerCenter.length() != 32){
			//		header.setCenter(targetSheet.getGuid());
					//// System.out.println("targetSheet.getGuid() = " + targetSheet.getGuid());
				}
				
				DataSourceObj dso = new DataSourceObj();
			//	dso.setGuid(targetSheet.getGuid());
				dso.setSheetName(targetSheetName);
				dso.setState(Config.DATA_SOURCE_STATE_TEMP);
				
				dso.setXlsName(srcExcelRealName);
				
				outStream = new FileOutputStream(objExcelUrl);
				targetWb.write(outStream);
				outStream.flush();
				outStream.close();
				/*
				 * 写配置文件
				 */
				TemplateObtainConfigure template = new TemplateObtainConfigure(this.getRepID(), this.getVersionID());
			//	template.updateTemplateDatasource(targetSheet.getGuid(), dso);
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 复制源Excel文件里面的Sheet到目标Excel文件 srcExcelName复制到objExcelUrl
	 * @param srcExcelUrl 数据源Excel文件
	 * @param objExcelUrl 目标Excel文件
	 * @param srcExcelRealName 数据源Excel事实文件名
	 * 
	 */
	public void copySheetToRelease(String srcExcelName, String objExcelName){
		
		FileInputStream inStream = null;
		FileInputStream inStream1 = null;
		HSSFWorkbook sourceWb = null;
		HSSFWorkbook targetWb = null;
		FileOutputStream outStream = null;
		String srcExcelUrl = Config.getObtainTemplateFolderRealPath() + File.separator + srcExcelName;
		String objExcelUrl = Config.getReleaseTemplatePath() + File.separator + objExcelName;
		
		
		try {
			inStream = new FileInputStream(srcExcelUrl);
			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
			sourceWb = new HSSFWorkbook(srcFile);			
			targetWb = new HSSFWorkbook();
			inStream.close();
			inStream1 = new FileInputStream(objExcelUrl);
			POIFSFileSystem targetFile = new POIFSFileSystem(inStream1);			
			targetWb = new HSSFWorkbook(targetFile);
			
			if(targetWb.getNumberOfSheets() > 0){
				for(int i=0;i<targetWb.getNumberOfSheets();i++){
					targetWb.removeSheetAt(0);
				}
			}
			inStream1.close();
			/*
			 * 强制性规定一个Excel只能有一个sheet
			 */
			if (sourceWb.getNumberOfSheets() > 0) {				
				HSSFSheet sourceSheet = sourceWb.getSheetAt(0);
				String targetSheetName = sourceWb.getSheetName(0);
				HSSFSheet targetSheet = targetWb.createSheet(targetSheetName);
				copyRows(targetWb, sourceWb, targetSheet, sourceSheet);
				
				HSSFHeader header = targetSheet.getHeader();
				String headerCenter = header.getCenter();
				
				if (headerCenter == null || headerCenter.length() != 32){
				//	header.setCenter(targetSheet.getGuid());
				}
								
				outStream = new FileOutputStream(objExcelUrl);
				targetWb.write(outStream);
				outStream.flush();
				outStream.close();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 复制源Excel文件里面的Sheet到目标Excel文件 srcExcelName复制到objExcelUrl
	 * @param srcExcelUrl 数据源Excel文件
	 * @param objExcelUrl 目标Excel文件
	 * @param srcExcelRealName 数据源Excel事实文件名
	 * 
	 */
	public String copySheetValueToRelease(String srcExcelName, String objExcelName){
		
		FileInputStream inStream = null;
		HSSFWorkbook targetWb = null;
		FileOutputStream outStream = null;
		String srcExcelUrl = Config.getObtainTemplateFolderRealPath() + File.separator + srcExcelName;
		String objExcelUrl = Config.getReleaseTemplatePath() + File.separator + objExcelName;
		
		FileInputStream sourceInputStream = null;
		HSSFWorkbook book = null;
		
		try{
			inStream = new FileInputStream(objExcelUrl);
			sourceInputStream = new FileInputStream(srcExcelUrl);
			
			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
			POIFSFileSystem sourceFile = new POIFSFileSystem(sourceInputStream);
			
			targetWb = new HSSFWorkbook(srcFile);
			book = new HSSFWorkbook(sourceFile);
			
			if(targetWb.getNumberOfSheets() > 0 && book.getNumberOfSheets() >0){
				HSSFSheet targetSheet = targetWb.getSheetAt(0);
				HSSFSheet sourceSheet = book.getSheetAt(0);
				
				copySingleSheetValue(book,sourceSheet, targetSheet);
			}
			outStream = new FileOutputStream(objExcelUrl);
			inStream.close();
			sourceInputStream.close();
			targetWb.write(outStream);
			outStream.flush();
			outStream.close();
			
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		/**
		 * FileInputStream inStream = null;
		HSSFWorkbook sourceWb = null;
		HSSFSheet sourceSheet = null;
		
		try {
			inStream = new FileInputStream(xlsName);
			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
			sourceWb = new HSSFWorkbook(srcFile);
			if (sourceWb.getNumberOfSheets() > 0) {
				sourceSheet = sourceWb.getSheetAt(0);
			}
			inStream.close();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		 */
		
		
		/**
		 * FileInputStream inStream = null;
		HSSFWorkbook targetWb = null;
		FileOutputStream outStream = null;
		HashMap dsoXlsNameMap = templateObjConfigure.getDatasourceXlsName();
		try {
			inStream = new FileInputStream(templateFileName);
			
			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
			targetWb = new HSSFWorkbook(srcFile);
			for(int i=1,n=targetWb.getNumberOfSheets(); i<n; i++){
				HSSFSheet targetSheet = targetWb.getSheetAt(i);
				String sourceSheetGuid = targetSheet.getHeader().getCenter();
				//取对应的包含数据的Excel文件对应的位置
				String xlsName = Config.DATA_SOURCE_EXCEL + File.separator + orgId + File.separator + dsoXlsNameMap.get(sourceSheetGuid);
				
				HSSFSheet sourceSheet = getFirstSheet(xlsName);
				if(sourceSheet == null) continue;
				copySingleSheetValue(sourceSheet, targetSheet);
			}
			outStream = new FileOutputStream(templateFileName);
			inStream.close();
			targetWb.write(outStream);
			outStream.flush();
			outStream.close();
		 */
		
		/**
		 * FileInputStream inStream = null;
	HSSFWorkbook sourceWb = null;
	HSSFSheet sourceSheet = null;
	
	try {
		inStream = new FileInputStream(xlsName);
		POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
		sourceWb = new HSSFWorkbook(srcFile);
		if (sourceWb.getNumberOfSheets() > 0) {
			sourceSheet = sourceWb.getSheetAt(0);
		}
		inStream.close();
	}catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	
	return sourceSheet;

		 */
//		FileInputStream inStream = null;
//		FileInputStream inStream1 = null;
//		HSSFWorkbook sourceWb = null;
//		HSSFWorkbook targetWb = null;
//		FileOutputStream outStream = null;
//		String srcExcelUrl = Config.getObtainTemplateFolderRealPath() + File.separator + srcExcelName;
//		String objExcelUrl = Config.getReleaseTemplatePath() + File.separator + objExcelName;
//		
//		try {
//			inStream = new FileInputStream(srcExcelUrl);
//			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
//			sourceWb = new HSSFWorkbook(srcFile);			
//			inStream.close();
//			inStream1 = new FileInputStream(objExcelUrl);
//			POIFSFileSystem targetFile = new POIFSFileSystem(inStream1);			
//			targetWb = new HSSFWorkbook(targetFile);
//			
//			inStream1.close();
//			/*
//			 * 强制性规定一个Excel只能有一个sheet
//			 */
//			if (sourceWb.getNumberOfSheets() > 0) {				
//				HSSFSheet sourceSheet = sourceWb.getSheetAt(0);
//				HSSFSheet targetSheet = targetWb.getSheetAt(0);
//				
//				copySingleSheetValue(sourceSheet, targetSheet);
//				
//				outStream = new FileOutputStream(objExcelUrl);
//				targetWb.write(outStream);
//				outStream.flush();
//				outStream.close();
//			}
//			
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}catch(IOException e){
//			e.printStackTrace();
//		}
		return objExcelUrl;
	}	
	
	
	/**
	 * 检查配置文件及配置文件的合法性,删除配置文件中state属性对应的Sheet
	 * @param templateFileName templateFileName 模板文件名
	 */
	public void checkConfigValidity(String templateFileName){
		FileInputStream inStream = null;
		HSSFWorkbook sourceWb = null;
		
		try {
			inStream = new FileInputStream(templateFileName);
			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
			sourceWb = new HSSFWorkbook(srcFile);
			TemplateObtainConfigure templateObjConfigure = new TemplateObtainConfigure(this.getRepID(), this.getVersionID());
			//得到有问题的数据源属性Guid
			HashMap invalidGuidMap = templateObjConfigure.checkAndDelInvalidEle();
			
			for(int i=0,n=sourceWb.getNumberOfSheets(); i<n; i++){
				HSSFSheet sourceSheet = sourceWb.getSheetAt(i);
				String sourceSheetGuid = sourceSheet.getHeader().getCenter();
				if (invalidGuidMap.get(sourceSheetGuid) != null) {
					/*
					 * 删除对应sheet
					 */
					sourceWb.removeSheetAt(i);
				}				
			}			
			inStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	/**
	 * 检查配置文件及配置文件的合法性,删除配置文件中state属性对应的Sheet
	 * @param templateFileName templateFileName 模板文件名
	 */
	public void checkConfigValidity(HSSFWorkbook sourceWb){		
		
			TemplateObtainConfigure templateObjConfigure = new TemplateObtainConfigure(this.getRepID(), this.getVersionID());
			//得到有问题的数据源属性Guid
			HashMap invalidGuidMap = templateObjConfigure.checkAndDelInvalidEle();
			
			for(int i=0,n=sourceWb.getNumberOfSheets(); i<n; i++){
				HSSFSheet sourceSheet = sourceWb.getSheetAt(i);
				String sourceSheetGuid = sourceSheet.getHeader().getCenter();
				if (invalidGuidMap.get(sourceSheetGuid) != null) {
					/*
					 * 删除对应sheet
					 */
					sourceWb.removeSheetAt(i);
				}				
			}			
	}
	
	
	
	/**
	 * 保存取数配置
	 * 
	 * @param templateFileName 模板文件名	 
	 */
	public boolean saveObtainConfigure(String templateFileName){
		FileInputStream inStream = null;
		HSSFWorkbook sourceWb = null;
		boolean resultFlag = true;
		try {
			inStream = new FileInputStream(templateFileName);
			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
			sourceWb = new HSSFWorkbook(srcFile);
			TemplateObtainConfigure templateObjConfigure = new TemplateObtainConfigure(this.getRepID(), this.getVersionID());
			TemplateObj templateObj = templateObjConfigure.getTemplateObj();
			HashMap datasourceMap = templateObj.getDataSourceMap();
			
			for(int i=0,n=sourceWb.getNumberOfSheets(); i<n; i++){
				HSSFSheet sourceSheet = sourceWb.getSheetAt(i);
				String sourceSheetGuid = sourceSheet.getHeader().getCenter();
				/*
				 * 如果第一个sheet不是源文件sheet直接报错
				 */
				if (i == 0 && !sourceSheetGuid.equals(templateObj.getGuid())) {
					/*
					 * 此处应该删除ObtainTemplates中的文件和配置文件中对英节点
					 * 
					 * 
					 * 
					 * 
					 */
					return false;
				}
				DataSourceObj dso = (DataSourceObj)datasourceMap.get(sourceSheetGuid);
				/*
				 * 如果dso==null 证明该源文件没有被保留，需要在配置文件中删除				 
				 */
				if (dso == null) {
					templateObjConfigure.deleteDataSource(sourceSheetGuid);
				}else{
					templateObjConfigure.updateDataSourceAttribute(sourceSheetGuid, "state", Config.DATA_SOURCE_STATE_SAVED);
				}
			}
			//保存文件
			templateObjConfigure.saveTemplateDoc();
			inStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return resultFlag;
	}

	/**
	 * 设置数据源GUID
	 *@param templateFileName 模板文件名
	 */
	public void setDataSourceGuid(String templateFileName){
		FileInputStream inStream = null;
		HSSFWorkbook sourceWb = null;
		FileOutputStream outStream = null;
		try {
			inStream = new FileInputStream(templateFileName);
			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
			sourceWb = new HSSFWorkbook(srcFile);

			if (sourceWb.getNumberOfSheets() > 0) {
				HSSFSheet sourceSheet = sourceWb.getSheetAt(0);
				/*
				 * 将GUID写入该sheet的header页眉，
				 * 为了建立sheet与其母体的映射关系，
				 * 此举迫不得已，防止用户更新sheet名
				 */
				HSSFHeader header = sourceSheet.getHeader();
				String headerCenter = header.getCenter();

				/*
				 * 如果是第一次读将该guid写入head中作为的唯一标识
				 */				
				if (headerCenter == null || headerCenter.length() != 32){
				//	header.setCenter(sourceSheet.getGuid());
					/*
					 * 将该数据源信息写入配置文件
					 */
					TemplateObtainConfigure toc = new TemplateObtainConfigure(this.getRepID(), this.getVersionID());					
				//	toc.createNewTemplateNode(sourceSheet.getGuid());
				}
				//检查合法性
				checkConfigValidity(sourceWb);
				outStream = new FileOutputStream(templateFileName);
				sourceWb.write(outStream);
				outStream.flush();
				outStream.close();	
				inStream.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * 行拷贝
	 * @param targetSheet 目标Excel工作表
	 * @param sourceSheet 待拷贝的Excel工作表
	 */
	private	void copyRows(HSSFWorkbook targetWb, 
						  HSSFWorkbook sourceWb,
						  HSSFSheet targetSheet, 
						  HSSFSheet sourceSheet){		
		HSSFRow sourceRow = null;
	    HSSFRow targetRow = null;
	    HSSFCell sourceCell = null;
	    HSSFCell targetCell = null;	    
	    Region region = null;
	    int cType;
	    int targetRowFrom;
	    int targetRowTo;	    

	    /*
	     * 如果sheet为null直接返回
	     */
	    if (targetSheet == null || sourceSheet == null) {
	    	return;
	    }
	    
	    /*
	     * 拷贝合并的单元格
	     */
	    for(int i=0,n=sourceSheet.getNumMergedRegions(); i<n; i++){
	    	region = sourceSheet.getMergedRegionAt(i);
	    	targetRowFrom = region.getRowFrom();
	    	
	    	targetSheet.addMergedRegion(region);
	    }
	    
	    
	    //拷贝行并填充数据
	    Iterator rowIter = sourceSheet.rowIterator();

		short rowIndex = 0;
		while(rowIter.hasNext()){			
			sourceRow = (HSSFRow)rowIter.next();
			if (sourceRow == null) {
				continue;
			}
			targetRow = targetSheet.createRow(rowIndex++);
			targetRow.setHeight(sourceRow.getHeight());			
						
			for(short i=sourceRow.getFirstCellNum(),n=sourceRow.getLastCellNum(); i<n; i++){
				sourceCell = (HSSFCell)sourceRow.getCell(i);
				if (sourceCell == null) {
					continue;
				}
				/*
				 * 建立单元格配置各项参数
				 */
				targetCell = targetRow.createCell(i);
				targetSheet.setColumnWidth((i), sourceSheet.getColumnWidth(i));
				targetCell.setEncoding(HSSFCell.ENCODING_UTF_16);
				HSSFCellStyle cellStyle = targetWb.createCellStyle();				
			    copyStyle(cellStyle, sourceCell.getCellStyle());			    
			    targetCell.setCellStyle(cellStyle);
			    cellStyle = null;
			    
			    /*
			     * 判断该font是否已经存在，如果相同就不在创建新的样式，否则新建
			     */
			    HSSFFont sourceFont = sourceWb.getFontAt(sourceCell.getCellStyle().getFontIndex());
			    HSSFFont targetFont = null;
			    short fontIndex = getExistFontIndex(targetWb, sourceFont);
			    if (fontIndex != -1) {
			    	targetFont = targetWb.getFontAt(fontIndex);
			    }else{
			    	targetFont = targetWb.createFont();
			    	copyFont(targetFont, sourceFont);			    	
			    }
			    targetCell.getCellStyle().setFont(targetFont);
			    sourceFont = null;
			    targetFont = null;
			    
			    /*
			     * 根据不同数据类型设置值
			     */
			    cType = sourceCell.getCellType();			    
			    			    
			    switch (cType){
			       case HSSFCell.CELL_TYPE_BOOLEAN: 
			       					targetCell.setCellType(cType);
			       					targetCell.setCellValue(sourceCell.getBooleanCellValue());			       					
			       					break;
			       case HSSFCell.CELL_TYPE_ERROR:
			       					targetCell.setCellType(cType);
			       					targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
			        				break;            
			       case HSSFCell.CELL_TYPE_FORMULA:
			       					/*
			       					 * 读出公式储存格计算後的值,
			       					 * 此举迫不得已，因为poi对公式支持欠佳。
			       					 * 对apache的崇拜因此降低不少(wunaigang批注,呵呵)			       					 
			       					 */
			       					targetCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			       					targetCell.setCellValue(sourceCell.getNumericCellValue());
			        				break;
			       case HSSFCell.CELL_TYPE_NUMERIC:
			       					targetCell.setCellType(cType);
			       					targetCell.setCellValue(sourceCell.getNumericCellValue());
			        				break;
			       case HSSFCell.CELL_TYPE_STRING:	
			       					targetCell.setCellType(cType);
									targetCell.setCellValue(String.valueOf(sourceCell.getStringCellValue()));			       
			        				break;	
			    }
			}			
		}
	}
	
	/**
	 * 如果公式里面的函数不带参数，比如now()或today()，
	 * 那么通过getCellFormula()取出来的值就是now(ATTR(semiVolatile))和today(ATTR(semiVolatile))，
	 * 这样的值写入Excel是会出错的，parseFormula这个函数的功能很简单，就是把ATTR(semiVolatile)删掉
	 * @param pPOIFormula 公式字符串
	 * @return
	 */
	private String parseFormula(String pPOIFormula)  {
		final String cstReplaceString = "ATTR(semiVolatile)"; //$NON-NLS-1$
		StringBuffer result = new StringBuffer();
		int index = pPOIFormula.indexOf(cstReplaceString);
        if (index >= 0) {
        	result.append(pPOIFormula.substring(0, index));
        	result.append(pPOIFormula.substring(index + cstReplaceString.length()));
        }else{
        	result.append(pPOIFormula);
        }
    
        return result.toString();
    }
 
	/**
	 * 复制样式
	 * @param targetStyle  目标单元格样式
	 * @param sourceStyle  源单元格样式
	 */
	private void copyStyle( HSSFCellStyle targetStyle, HSSFCellStyle sourceStyle){
		
			targetStyle.setAlignment(sourceStyle.getAlignment());
			targetStyle.setBorderBottom(sourceStyle.getBorderBottom());
			targetStyle.setBorderLeft(sourceStyle.getBorderLeft());
			targetStyle.setBorderRight(sourceStyle.getBorderRight());
			targetStyle.setBorderTop(sourceStyle.getBorderTop());
			targetStyle.setBottomBorderColor(sourceStyle.getBottomBorderColor());
			targetStyle.setDataFormat(sourceStyle.getDataFormat());
			targetStyle.setFillBackgroundColor(sourceStyle.getFillBackgroundColor());
			targetStyle.setFillForegroundColor(sourceStyle.getFillForegroundColor());
			targetStyle.setFillPattern(sourceStyle.getFillPattern());
			targetStyle.setHidden(sourceStyle.getHidden());
			targetStyle.setIndention(sourceStyle.getIndention());
			targetStyle.setLeftBorderColor(sourceStyle.getLeftBorderColor());
			targetStyle.setRightBorderColor(sourceStyle.getRightBorderColor());
			targetStyle.setRotation(sourceStyle.getRotation());
			targetStyle.setTopBorderColor(sourceStyle.getTopBorderColor());			
			targetStyle.setVerticalAlignment(sourceStyle.getVerticalAlignment());
			targetStyle.setWrapText(sourceStyle.getWrapText());
			
	}
		
	/**
	 * 复制字体
	 * @param targetFont
	 * @param sourceFont
	 */
	private void copyFont(HSSFFont targetFont, HSSFFont sourceFont){
		
		targetFont.setBoldweight(sourceFont.getBoldweight());
		targetFont.setColor(sourceFont.getColor());
		targetFont.setFontHeight(sourceFont.getFontHeight());
		targetFont.setFontHeightInPoints(sourceFont.getFontHeightInPoints());
		targetFont.setFontName(sourceFont.getFontName());
		targetFont.setItalic(sourceFont.getItalic());
		targetFont.setStrikeout(sourceFont.getStrikeout());
		targetFont.setTypeOffset(sourceFont.getTypeOffset());
		targetFont.setUnderline(sourceFont.getUnderline());
		
	}
	
	/**
	 * 判断字体对象是否一致
	 * @param targetFont 　　　   目标字体
	 * @param sourceFont　源字体
	 * @return
	 */
	private boolean fontEquals(HSSFFont targetFont, HSSFFont sourceFont){
		boolean resultFlag = false;
		
		if (targetFont == sourceFont){
			resultFlag = true;
		}
		
		if (sourceFont.getBoldweight() == targetFont.getBoldweight() &&
				sourceFont.getColor() == targetFont.getColor() && 
				sourceFont.getFontHeight() == targetFont.getFontHeight() && 
				sourceFont.getFontHeightInPoints() == targetFont.getFontHeightInPoints() && 
				sourceFont.getFontName() == targetFont.getFontName() && 
				sourceFont.getItalic() == targetFont.getItalic() && 
				sourceFont.getStrikeout() == targetFont.getStrikeout() && 
				sourceFont.getTypeOffset() == targetFont.getTypeOffset() && 
				sourceFont.getUnderline() == targetFont.getUnderline()) {
			resultFlag = true;
		}
		
		return resultFlag;
	}
	
	/**
	 * 判断该字体对象在工作簿中是否已经存在,如果不存在返回-1
	 * @param wb
	 * @param sourceFont
	 * @return
	 */
	private short getExistFontIndex(HSSFWorkbook wb, HSSFFont sourceFont){
		boolean resultFlag = false;
		short index = -1;
		if (wb == null || sourceFont == null) {
			return -1;
		}
		
		HSSFFont tmpFont = null;
		for(short i=0,n=wb.getNumberOfFonts(); i<n; i++){
			tmpFont = wb.getFontAt(i);
			if (fontEquals(sourceFont, tmpFont)) {
				index = i;
				break;
			}
		}
		
		return index;
	}
	
	public static void main(String args[]){
		SheetHandle test = new SheetHandle("", "");
		//test.setDataSourceGuid("d:\\test\\111.xls");
		//// System.out.println("this ok");
		//test.copySheetToRelease("S1010_0512.xls","S1010_0512.xls");//.createExcelAccordingOrg("D:\\testResin\\webapps\\obtain\\Reports\\releaseTemplates\\G11.xls", "G11.xls");
//		FileInputStream inStream;
//		try {
//			inStream = new FileInputStream("D:\\src\\S1010_0512.xls");
//			POIFSFileSystem srcFile = new POIFSFileSystem(inStream);
//			//POIFSFileSystem srcFile1= new POIFSFileSystem(inStream1);
//			HSSFWorkbook targetWb = new HSSFWorkbook(srcFile);
//			test.releaseTemplate2(targetWb,"S1010_0512.xls","S1010_0512.xls");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		test.createOrgExcel("D:\\src","D:\\obj","S1010_0512.xls");
		
	}

	/**
	 * @return Returns the repID.
	 */
	public String getRepID() {
		return repID;
	}
	/**
	 * @param repID The repID to set.
	 */
	public void setRepID(String repID) {
		this.repID = repID;
	}
	/**
	 * @return Returns the versionID.
	 */
	public String getVersionID() {
		return versionID;
	}
	/**
	 * @param versionID The versionID to set.
	 */
	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}
	
private double getValue(HSSFWorkbook book,String formuStrParam){
		
		HSSFSheet sheet = null;
		String[] ARRCOLS={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
		String formuStr = new String(formuStrParam);
		String[] signs = {"+","-","*","/"};
		int index = -1;
		StringBuffer buffer = new StringBuffer();
		do{
			index = -1;
			boolean bool = false;
			int[] signIndex = new int[4];
			int[] backIndex = new int[4];
			signIndex[0] = formuStr.indexOf(signs[0]);
			signIndex[1] = formuStr.indexOf(signs[1]);
			signIndex[2] = formuStr.indexOf(signs[2]);
			signIndex[3] = formuStr.indexOf(signs[3]);
			
			for(int i=0;i<backIndex.length;i++){
				backIndex[i] = signIndex[i];
			}
				
			
			for(int i=0;i<signIndex.length-1;i++){
				if(signIndex[i+1] == -1) continue;
				if(signIndex[0] > signIndex[i+1] || signIndex[0] == -1){
					int temp = signIndex[0];
					signIndex[0] = signIndex[i+1];
					signIndex[i+1] = temp;
				}
			}
			index = signIndex[0];
			String sign = "";
			double value = 0 ;
			for(int i=0;i<backIndex.length;i++){
				if(backIndex[i] == index && index != -1){
					sign = signs[i];
					break;
				}
			}
			
			String formu = (index == -1) ? formuStr : formuStr.substring(0,index);
			if(formu.indexOf("(") > -1){
				formu = formu.substring(1,formu.length());
				buffer.append("(");
			}
			if(formu.indexOf(")") > -1){
				formu = formu.substring(0,formu.length()-1);
				//buffer.append(")");
				bool = true;
			}
			
			String[] str = formu.split("!");
			if(str.length == 1){
				String temp = str[0];
				str = new String[2];
				str[1] = temp;
				if(book.getNumberOfSheets() > 0){
					str[0] = book.getSheetName(0);
				}
			}
			int colNum = -1;
			int rowNum = -1; 
			
			char[] charArray = str[1].toCharArray();
			for(int i=charArray.length -1 ;i >= 0;i--){
				try{
					Integer.parseInt(String.valueOf(charArray[i]));
				}catch(Exception ex){
					String backStr = new String(str[1]);//F5
					String rowStr = backStr.substring(i+1,charArray.length);
					backStr = new String(str[1]);
					String colStr = backStr.substring(0,i+1);
					for(int j=0;j<ARRCOLS.length;j++){
						if(ARRCOLS[j].equals(colStr)){
							colNum = j;
							break;
						}
					}
					rowNum = Integer.parseInt(rowStr)-1;
				}
			}
			
			try{
				if(book.getNumberOfSheets() > 0){
					sheet = book.getSheet(str[0]);
				}
				if(sheet == null) continue;
				HSSFRow sourceRow = null;
			    HSSFCell sourceCell = null;
				
			    Iterator sourceRowIter = sheet.rowIterator();
				while(sourceRowIter.hasNext()){	
					sourceRow = (HSSFRow)sourceRowIter.next();
					if (sourceRow == null) continue;
					if(sourceRow.getRowNum() == rowNum){
						sourceCell = (HSSFCell)sourceRow.getCell((short)colNum);
						if (sourceCell == null) continue;
					    int cType = sourceCell.getCellType();		    		    			    
					    switch (cType){
					       case HSSFCell.CELL_TYPE_FORMULA:
					    	   value = sourceCell.getNumericCellValue();
					    	   break;
					       case HSSFCell.CELL_TYPE_NUMERIC:
					    	   value = sourceCell.getNumericCellValue();
					    	   break;
					    }	
					}		
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}	
			
			buffer.append(String.valueOf(value)).append(sign);
			if(bool == true) buffer.append(")");
			if(index != -1){
				formuStr = formuStr.substring(index+1);
			}
		}while(index != -1);
		
		Formula formula = new Formula(buffer.toString());
		return formula.getResult();
	}
	
	
}

