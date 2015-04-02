package com.fitech.gznx.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.util.MessageResources;
import org.jxls.XLSTransformer;


import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.db2xml.Db2XmlUtil;

import com.cbrc.smis.excel.StyleCellProcessor;
import com.cbrc.smis.excel.Utils;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportDealDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;

public class CreateExcel {
	FitechException log=new FitechException(CreateExcel.class);
	
	Locale LOCALE=Locale.CHINA;

	/**
	 * 列名
	 */
	private String[] ARRCOLS={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
	/**
	 * 列信息列表
	 */
	private List COLS=null;
	/**
	 * 报表的ID
	 */
	private Integer repInId=null;	
	/**
	 * 报表的类别
	 */
	private int reportStyle=0;	
	/**
	 * 报表模板的编号
	 */
	private String templateId=null;
	/**
	 * 报表模板的版本号
	 */
	private String versionId=null;
	/**
	 * 实际报表对象
	 */
	private AFReportForm reportInForm=null;

	/**
	 * 报表对应的Excel模板文件 
	 */
	private String excelFile=null;
	
	private String reportflg = null;

	/**
	 * 构造函数
	 * 
	 * @param repInId 报表ID
	 */
	public CreateExcel(Integer repInId){
		this.repInId=repInId;
		
		COLS=new ArrayList();
		for(int i=0;i<this.ARRCOLS.length;i++){
			COLS.add(ARRCOLS[i]);
		}
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-26
	 * 初始化
	 *
	 * @exception Exception
	 * @return void
	 */
	private void init() throws Exception{
		this.reportInForm=AFReportDealDelegate.getReportIn(this.repInId);
		if(this.reportInForm==null) return;
		
		this.templateId=reportInForm.getTemplateId();
		this.versionId=reportInForm.getVersionId();
		this.reportStyle=AFTemplateDelegate.getReportStyle(templateId,versionId);
		this.excelFile=getExcelFile(templateId,versionId);
		
	}
	
	/**
	 * 获取报表对应的Excel模板的文件
	 * 
	 * @param childRepId String PDF模板的编号
	 * @param versionId String 模板的版本号
	 * @return String
	 */
	private String getExcelFile(String childRepId,String versionId){
		if(childRepId==null || versionId==null) return null;
		return childRepId.trim()+"_"+versionId.trim()+".xls";
		//return getValue(CONFIGFILE,childRepId.trim() + versionId.trim());
	}
	
	/**
	 * jdbc技术 无特殊oracle语法 可能不需要修改
	 * 卞以刚 2011-12-26
	 * 获取报表的实际数据
	 * 
	 * @param repInId Integer
	 * @return List
	 */
	private List reportData(String reportFlg){
		reportflg = reportFlg;
		if(this.reportStyle==Config.REPORT_STYLE_DD.intValue()){
			return StrutsReportInInfoDelegate.getAllNXReportInInfo(this.repInId,reportFlg);
		}else{
			return null;
		}
	}
	
	
	public HSSFWorkbook createDataReport(String reportFlg,String templateFilePath){
		boolean res = true;
		HSSFWorkbook book = null;
		try{
			init();
			if(this.reportStyle!=Config.REPORT_STYLE_DD.intValue() && 
					this.reportStyle!=Config.REPORT_STYLE_QD.intValue())
				res=false;
			List cells = reportData(reportFlg);
			
			if(cells == null) res = true;
			if(res == true){
				book = writeExcel(cells,templateFilePath);
			}
			
		}catch(Exception e){
			res= false;
			e.printStackTrace();
		}
		return book;
	}
	
	
	/**
	 * 创建Excel文档
	 * 
	 * @return void 
	 */
	public HSSFWorkbook createDataReport(String reportFlg){
		boolean res = true;
		HSSFWorkbook book = null;
		try{
			init();
			if(this.reportStyle!=Config.REPORT_STYLE_DD.intValue() && 
					this.reportStyle!=Config.REPORT_STYLE_QD.intValue())
				res=false;
			List cells = reportData(reportFlg);
			
			if(cells == null) res = true;
			if(res == true){
				book = writeExcel(cells);
			}
			
		}catch(Exception e){
			res= false;
			e.printStackTrace();
		}
		return book;
	}
	public void createExcel(String year, String term){
		boolean res=true;
		
		try{
			init();
			if(this.reportStyle!=Config.REPORT_STYLE_DD.intValue() && 
					this.reportStyle!=Config.REPORT_STYLE_QD.intValue())
				res=false;
			
			List cells=reportData("");
			
			if(cells==null) res=true;
			
			if(res==true){	
				HSSFWorkbook book = writeExcel(cells);
				String fileName = com.fitech.net.config.Config.getCollectExcelFolder() + File.separator +year + "_" + term  + File.separator + this.reportInForm.getOrgId().trim();
				if(book != null){
					File temp = new File(fileName);
					if(!temp.exists()) temp.mkdirs();
					
				/*	File[] files = temp.listFiles();
					if(files != null){
						for(int i=0;i<files.length;i++){
							if(files[i].getName().indexOf(".xls") > -1)
								files[i].delete();
						}
					}	*/				
					File file = new File(fileName+File.separator + this.templateId + "_" + this.versionId+"_"+this.reportInForm.getDataRangeId()+"_"+this.reportInForm.getCurId() +"_"+ this.reportInForm.getOrgId()+".xls");
					FileOutputStream fos = new FileOutputStream(file);
					book.write(fos);
					fos.close();
				}
			}
			
		}catch(Exception e){
			res=false;
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 创建Excel文档
	 * 
	 * @param cells records 单元格信息列表
	 * @return boolean 创建成功，返回true;否则，返回false
	 */
	private HSSFWorkbook writeExcel(List records,String templateFilePath){
	
		HSSFWorkbook book=null;
		
		File file=null;
		FileInputStream excel=null;
		
		try{
			file=new File(templateFilePath);
			
    		if(file.exists()==false) return book;
    		
    		excel=new FileInputStream(file);
    		    		
    		if(reportStyle==Config.REPORT_STYLE_DD.intValue()){ //点对点式报表转Excel
				book=writeDDExcel(excel,records);
				if(book!=null) book=setReportHeaderAndFooter(book,this.reportInForm);
			}else if(this.reportStyle==Config.REPORT_STYLE_QD.intValue()){ //清单式报表转Excel
				book=writeQDExcel(excel,records,this.reportInForm);
				
			}
		}catch(Exception e){
			//result=false;
			e.printStackTrace();
		}finally{
			if(excel!=null) 
				try{
					excel.close();
				}catch(IOException ioe){
					log.printStackTrace(ioe);
				}
		}
		
		return book;
	}
	
	/**
	 * 创建Excel文档
	 * 
	 * @param cells records 单元格信息列表
	 * @return boolean 创建成功，返回true;否则，返回false
	 */
	private HSSFWorkbook writeExcel(List records){
	
		HSSFWorkbook book=null;
		
		File file=null;
		FileInputStream excel=null;
		
		try{
			file=new File(
					Config.RAQ_TEMPLATE_PATH + "templateFiles" + 
					Config.FILESEPARATOR + "excel" + 
					Config.FILESEPARATOR + this.excelFile
			);
    		if(file.exists()==false) return book;
    		
    		excel=new FileInputStream(file);
    		    		
    		if(reportStyle==Config.REPORT_STYLE_DD.intValue()){ //点对点式报表转Excel
				book=writeDDExcel(excel,records);
				if(book!=null) book=setReportHeaderAndFooter(book,this.reportInForm);
			}else if(this.reportStyle==Config.REPORT_STYLE_QD.intValue()){ //清单式报表转Excel
				book=writeQDExcel(excel,records,this.reportInForm);
				
			}
		}catch(Exception e){
			//result=false;
			e.printStackTrace();
		}finally{
			if(excel!=null) 
				try{
					excel.close();
				}catch(IOException ioe){
					log.printStackTrace(ioe);
				}
		}
		
		return book;
	}
	
	/**
	 * 写点对点式Excel
	 * 
	 * @param in InputStream 输入流
	 * @param cells List 报表单元格信息列表
	 * @Exception Exception 
	 * @return void 
	 */
	private HSSFWorkbook writeDDExcel(InputStream in,List cells) throws Exception{
		if(in==null) return null;
		
		POIFSFileSystem fs=new POIFSFileSystem(in);		
		HSSFWorkbook book=new HSSFWorkbook(fs);   
		
		HSSFSheet sheet=book.getSheetAt(0);
		if(cells==null) return book;
			
		HSSFCell hssfCell=null;
		HSSFRow hssfRow=null;
		ReportInInfoForm cell=null;
		int colIndex=-1;
		Number cellNumb = null;
		
		for(int i=0;i<cells.size();i++){
			cell=(ReportInInfoForm)cells.get(i);
			colIndex=this.COLS.indexOf(cell.getColId());
			if(colIndex==-1) continue;
			hssfRow=sheet.getRow(cell.getRowId().intValue()-1 );
			if(hssfRow==null){
				continue;
			}
			hssfCell=hssfRow.getCell((short)colIndex);
			if(hssfCell==null) continue;
			HSSFCellStyle cellStyle=hssfCell.getCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			hssfCell.setCellStyle(cellStyle);
			String contents = null;
			
			//if(hssfCell.getCellType() != HSSFCell.CELL_TYPE_FORMULA){
			if(hssfCell.getCellType() == HSSFCell.CELL_TYPE_FORMULA || 
					hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
				try{
					Double d = Double.valueOf(cell.getReportValue());
					hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					hssfCell.setCellValue(d.doubleValue());
				}catch(Exception ex){
					hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					//hssfCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
					hssfCell.setCellValue((cell.getReportValue()==null?"":Utils.formatVal(cell.getReportValue())));
				}
			} else if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_STRING){
				hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);
				hssfCell.setCellValue(cell.getReportValue());
			}else{
				try {
					cellNumb = parseNumber(cell.getReportValue());
					if(cellNumb == null){
						hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);
						hssfCell.setCellValue(cell.getReportValue());
					}else{
						if(hssfCell.getCellType() !=HSSFCell.CELL_TYPE_STRING )
							hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						hssfCell.setCellValue(cellNumb.doubleValue());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			 
		}

		}
		
		return book;
	}
	
	private Number parseNumber(String value){
		Number num = null;
		
		if(value == null || value.trim().equals("")) return num;
		
		java.text.NumberFormat numberFormat = java.text.NumberFormat.getInstance();
		
		try{
			num = numberFormat.parse(value);
		}catch(Exception e){
			num = null;
		}
		
		return num;
	}
	
	/**
	 * 写清单式Excel
	 * 
	 * @param in InputStream 输入流
	 * @param cells List 报表单元格信息列表
	 * @exception Exception
	 * @return HSSFWorkbook 
	 */
	private HSSFWorkbook writeQDExcel(InputStream in,List cells,AFReportForm reportInForm) throws Exception{
		if(in==null || reportInForm==null) return null;
		
		POIFSFileSystem fs=new POIFSFileSystem(in);		
		HSSFWorkbook book=new HSSFWorkbook(fs);   
		
		HSSFSheet sheet=book.getSheetAt(0);
		if(cells==null) return book;
			
		HSSFCell hssfCell=null;
		HSSFRow hssfRow=null;
		Object[] cell=null;
		int colIndex=-1;
		int rowcount = 0;
		if(rowcount>2){
		//	sheet.shiftRows(startRow, endRow, rowcount, true, false);
		
		}
		for(int i=0;i<cells.size();i++){
			cell=(Object[])cells.get(i);
			colIndex=((Long)cell[2]).intValue()-1;
			if(colIndex==-1) continue;
			int row = ((Long)cell[1]).intValue()-1 ;
	
			hssfRow=sheet.getRow(row);
			if(hssfRow==null){
				continue;
			}
			hssfCell=hssfRow.getCell((short)colIndex);
			if(hssfCell==null) continue;
//			HSSFCellStyle cellStyle=hssfCell.getCellStyle();
//			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//			hssfCell.setCellStyle(cellStyle);

			hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);
			hssfCell.setCellValue((String)cell[0]);
			
		}
		
		return book;
	
	}


	
	/**
	 * 设置报表的头和尾
	 * 
	 * @param book HSSFWorkbook Excel工作簿
	 * @param reportInForm 报表对象
	 * @return HSSFWorkbook
	 */
	private HSSFWorkbook setReportHeaderAndFooter(HSSFWorkbook book,AFReportForm reportInForm){
		HSSFWorkbook resBook=null;
		
		if(book==null || reportInForm==null) return null;
		
		File file=null;                     //文件流
		FileOutputStream fos=null;          //文件输出流
		BufferedOutputStream bos=null;      //缓存输出流
		FileInputStream fis=null;           //文件输入流
		BufferedInputStream bis=null;       //缓存输入流
		try{
			String fileName=Config.WEBROOTPATH + Config.FILESEPARATOR + 
				"tmp" + Config.FILESEPARATOR +
				System.currentTimeMillis() + ".xls";
			file=new File(fileName);
			
			fos=new FileOutputStream(file);
			bos=new BufferedOutputStream(fos);
			book.write(bos);
			bos.close();
			bos=null;
			
			fis=new FileInputStream(file);
			bis=new BufferedInputStream(fis);
			Map beans=new HashMap();
			beans.put("report",reportInForm);
			
			XLSTransformer transformer = new XLSTransformer();
			transformer.registerCellProcessor(new StyleCellProcessor());
			resBook=transformer.transformXLS(bis, beans);
		}catch(Exception e){
			resBook=null;
			e.printStackTrace();
		}finally{
			try{
				if(bos!=null) bos.close();
				if(fos!=null) fos.close();
				if(fis!=null) fis.close();
				if(bis!=null) bis.close();
				if(file!=null) file.delete();
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
		
		return resBook;
	}
}
