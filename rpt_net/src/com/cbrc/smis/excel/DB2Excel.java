package com.cbrc.smis.excel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.util.MessageResources;
import org.jxls.XLSTransformer;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.db2xml.Db2XmlUtil;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.util.FitechException;

public class DB2Excel {
	FitechException log=new FitechException(DB2Excel.class);
	
	Locale LOCALE=Locale.CHINA;
	
	/**
	 * Excel模板对应PDF模板的起始行的偏移量
	 */
	private String REPORTFILE="com/cbrc/smis/excel/OffsetResources";
	/**
	 * 单元格列表
	 */
	public List cells=null;
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
	private String childRepId=null;
	/**
	 * 报表模板的版本号
	 */
	private String versionId=null;
	/**
	 * 实际报表对象
	 */
	private ReportInForm reportInForm=null;
	/**
	 * Excel对应PDF报表的行偏移量
	 */
	private int offset=0;
	/**
	 * 报表对应的Excel模板文件 
	 */
	private String excelFile=null;
	/**
	 * 百分数列
	 */
	private Map percentCells=null;
	
	/**
	 * 构造函数
	 * 
	 * @param repInId 报表ID
	 */
	public DB2Excel(Integer repInId){
		this.repInId=repInId;
		
		COLS=new ArrayList();
		for(int i=0;i<this.ARRCOLS.length;i++){
			COLS.add(ARRCOLS[i]);
		}
	}
	
	/**
	 * 初始化
	 *
	 * @exception Exception
	 * @return void
	 */
	private void init() throws Exception{
		this.reportInForm=StrutsReportInDelegate.getReportIn(this.repInId);
		if(this.reportInForm==null) return;
		this.reportStyle=StrutsMChildReportDelegate.getReportStyle(reportInForm.getChildRepId(),reportInForm.getVersionId());
		this.childRepId=reportInForm.getChildRepId();
		this.versionId=reportInForm.getVersionId();
		this.offset=getOffsetRows(childRepId,versionId);
	//	this.excelFile=getExcelFile(childRepId,versionId);
		this.excelFile = childRepId.trim()+versionId.trim()+".xls";
		this.percentCells=new Db2XmlUtil().getPercentCells(this.childRepId,this.versionId);
	}
	
	/**
	 * 获取报表的实际数据
	 * 
	 * @param repInId Integer
	 * @return List (ReportInInfoForm)
	 */
	private List reportData(){
		if(this.reportStyle==Config.REPORT_STYLE_DD.intValue()){
			return StrutsReportInInfoDelegate.getAllReportInInfo(this.repInId);
		}else if(this.reportStyle==Config.REPORT_STYLE_QD.intValue()){
			return new Db2XmlUtil().findQDReportLst(this.repInId,this.childRepId,this.versionId);
		}else{
			return null;
		}
	}
	
	/**
	 * 创建Excel文档
	 * 
	 * @param out OutputStream
	 * @return void 
	 */
	public void createExcel(HttpServletResponse response){
		boolean res=true;
		
		try{
			init();
			if(this.reportStyle!=Config.REPORT_STYLE_DD.intValue() && 
					this.reportStyle!=Config.REPORT_STYLE_QD.intValue())
				res=false;
			
			List cells=reportData();
			
			if(cells==null) res=true;
			
			if(res==true){
				//String orgName=MOrgUtil.getOrgName(this.reportInForm.getOrgId());
				String reportName=this.childRepId + "_" + 
					this.reportInForm.getYear()  + this.reportInForm.getTerm()  + "_" +
					this.reportInForm.getDataRangeId() + "_" + 
					this.reportInForm.getOrgId().trim() + ".xls";  
						
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition","attachment;filename=" + Utils.toUtf8String(reportName));
	
				OutputStream out=response.getOutputStream();	
				writeExcel(cells,out);
			}
		}catch(Exception e){
			res=false;
			e.printStackTrace();
		}finally{
			if(res==false){
				try{
					response.sendRedirect("../error.jsp");
				}catch(IOException ioe){}
			}
		}
	}
	
	/**
	 * 创建Excel文档
	 * 
	 * @param cells records 单元格信息列表
	 * @param out OutputStream
	 * @return boolean 创建成功，返回true;否则，返回false
	 */
	private void writeExcel(List records,OutputStream out){
		BufferedOutputStream bout=null;
		
		File file=null;
		FileInputStream excel=null;
		
		try{
			file=new File(
					Config.WEBROOTPATH + "report_mgr" + 
					Config.FILESEPARATOR + "excel" + 
					Config.FILESEPARATOR + this.excelFile
			);
    		if(file.exists()==false) return;
    		
    		excel=new FileInputStream(file);
    		
    		HSSFWorkbook book=null;
    		
    		if(reportStyle==Config.REPORT_STYLE_DD.intValue()){ //点对点式报表转Excel
    			
				book=writeDDExcel(excel,records);
				if(book!=null) book=setReportHeaderAndFooter(book,this.reportInForm);
			}else if(this.reportStyle==Config.REPORT_STYLE_QD.intValue()){ //清单式报表转Excel
				book=writeQDExcel(excel,records,this.reportInForm);
			}
    		    		
    		if(book!=null){
	    		bout=new BufferedOutputStream(out);
	    		book.write(bout);
	    		out.flush();
	    		bout.close();
	    		out.close();                 // 07年4月18号改
    		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(excel!=null) 
				try{
					excel.close();
				}catch(IOException ioe){
					log.printStackTrace(ioe);
				}
		}
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
		for(int i=0;i<cells.size();i++){
			cell=(ReportInInfoForm)cells.get(i);
			colIndex=this.COLS.indexOf(cell.getColId());
			if(colIndex==-1) continue;
			hssfRow=sheet.getRow(cell.getRowId().intValue()-1 + offset);
			if(hssfRow==null){
				continue;
			}
			hssfCell=hssfRow.getCell((short)colIndex);
			if(hssfCell==null) continue;
			
			HSSFCellStyle cellStyle=hssfCell.getCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			hssfCell.setCellStyle(cellStyle);
			
		
			if(hssfCell.getCellType() != HSSFCell.CELL_TYPE_FORMULA){
				try{
					Double d = Double.valueOf(cell.getReportValue());
					hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					hssfCell.setCellValue(d.doubleValue());
				}catch(Exception ex){
					hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					hssfCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
					hssfCell.setCellValue(Utils.isPercentCell(this.percentCells,cell.getCellName())==true?
							Utils.round(cell.getReportValue(),2):
								(cell.getReportValue()==null?"":Utils.formatVal(cell.getReportValue())));
					
				}
//			}else{
//				if (removeFormual) {
//					try {
//
//						hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);
//						hssfCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
//						hssfCell
//								.setCellValue(Utils.isPercentCell(
//										this.percentCells, cell.getCellName()) == true ? Utils
//										.round(cell.getReportValue(), 2)
//										: (cell.getReportValue() == null ? ""
//												: Utils.formatVal(cell
//														.getReportValue())));
//
//					} catch (Exception ex) {
//						ex.printStackTrace();
//					}
//				}
//			}
			
//			hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);
//			hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
//			HSSFCellStyle cellStyle=hssfCell.getCellStyle();
//			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//			hssfCell.setCellStyle(cellStyle);
//			//hssfCell.setCellValue(cell.getReportValue()==null?"":cell.getReportValue());
//			hssfCell.setCellValue(Utils.isPercentCell(this.percentCells,cell.getCellName())==true?
//					Utils.round(cell.getReportValue(),2):
//						(cell.getReportValue()==null?"":Utils.formatVal(cell.getReportValue())));
		}
		}
		return book;
	}
	
	/**
	 * 写清单式Excel
	 * 
	 * @param in InputStream 输入流
	 * @param cells List 报表单元格信息列表
	 * @exception Exception
	 * @return HSSFWorkbook 
	 */
	private HSSFWorkbook writeQDExcel(InputStream in,List records,ReportInForm reportInForm) throws Exception{
		if(in==null || reportInForm==null) return null;
		
		HSSFWorkbook book=null;
		
		Map beans=new HashMap();
				
		beans=Utils.formatRecords(records,this.childRepId);
				
		beans.put("report",reportInForm);
						
		XLSTransformer transformer = new XLSTransformer();
		transformer.registerCellProcessor(new StyleCellProcessor());
			
		book=transformer.transformXLS(in, beans);		
		
		return book;
	}

	/**
	 * 获取Excel模板对应PDF模板的起始行的偏移量
	 * 
	 * @param childRepId String 报表编号
	 * @return int
	 */
	private int getOffsetRows(String childRepId,String versionId){
		int offset=0;
		
		if(childRepId==null || versionId==null) return offset;
			
		String _offset=getValue(REPORTFILE,childRepId.trim()+versionId.trim());
		
		if(_offset!=null){
			try{
				offset=Integer.parseInt(_offset.trim());
			}catch(Exception e){
				offset=0;
			}
		}
		
		return offset;
	}
	
	/**
	 * 从资源文件中，根据主键获取其值
	 * 
	 * @param resourcesFile String 资源文件
	 * @param key String 主键
	 * @return String 健的值
	 */
	private String getValue(String resourcesFile,String key){
		if(resourcesFile==null || key==null) return null;
		
		MessageResources resources=MessageResources.getMessageResources(resourcesFile);
		
		String value=resources.getMessage(this.LOCALE,key);
		
		return value==null?null:value.trim();
	}	
	
	/**
	 * 设置报表的头和尾
	 * 
	 * @param book HSSFWorkbook Excel工作簿
	 * @param reportInForm 报表对象
	 * @return HSSFWorkbook
	 */
	private HSSFWorkbook setReportHeaderAndFooter(HSSFWorkbook book,ReportInForm reportInForm){
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
	/**
	 * 创建Excel文档 到指定路径
	 * @param String filePath 输出文件路径
	 * @return boolean 成功返true 失败返回false
	 */
	public boolean createExcel(String filePath){
		boolean res=true;		
		try{
			init();
			if(this.reportStyle!=Config.REPORT_STYLE_DD.intValue() && 
					this.reportStyle!=Config.REPORT_STYLE_QD.intValue())
				res=false;		
			
			if(cells == null)
				cells=reportData();
			
			if(cells==null) res=true;
			
			if(res==true){ 
				
				OutputStream out=new FileOutputStream(filePath);
				
				writeExcel(cells,out);
			}
		}catch(Exception e){
			res=false;
			e.printStackTrace();
		}
		return res;
	}
	public String getFileName(){
		return this.childRepId + "_" + 
		this.reportInForm.getYear() + "_" + this.reportInForm.getTerm() + "_" +
		this.reportInForm.getDataRangeId() + "_" +this.reportInForm.getCurId()+ "_"+
		this.reportInForm.getOrgId().trim() + ".xls";
	}
}
