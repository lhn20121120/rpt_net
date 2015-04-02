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
	 * Excelģ���ӦPDFģ�����ʼ�е�ƫ����
	 */
	private String REPORTFILE="com/cbrc/smis/excel/OffsetResources";
	/**
	 * ��Ԫ���б�
	 */
	public List cells=null;
	/**
	 * ����
	 */
	private String[] ARRCOLS={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
	/**
	 * ����Ϣ�б�
	 */
	private List COLS=null;
	/**
	 * �����ID
	 */
	private Integer repInId=null;	
	/**
	 * ��������
	 */
	private int reportStyle=0;	
	/**
	 * ����ģ��ı��
	 */
	private String childRepId=null;
	/**
	 * ����ģ��İ汾��
	 */
	private String versionId=null;
	/**
	 * ʵ�ʱ������
	 */
	private ReportInForm reportInForm=null;
	/**
	 * Excel��ӦPDF�������ƫ����
	 */
	private int offset=0;
	/**
	 * �����Ӧ��Excelģ���ļ� 
	 */
	private String excelFile=null;
	/**
	 * �ٷ�����
	 */
	private Map percentCells=null;
	
	/**
	 * ���캯��
	 * 
	 * @param repInId ����ID
	 */
	public DB2Excel(Integer repInId){
		this.repInId=repInId;
		
		COLS=new ArrayList();
		for(int i=0;i<this.ARRCOLS.length;i++){
			COLS.add(ARRCOLS[i]);
		}
	}
	
	/**
	 * ��ʼ��
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
	 * ��ȡ�����ʵ������
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
	 * ����Excel�ĵ�
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
	 * ����Excel�ĵ�
	 * 
	 * @param cells records ��Ԫ����Ϣ�б�
	 * @param out OutputStream
	 * @return boolean �����ɹ�������true;���򣬷���false
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
    		
    		if(reportStyle==Config.REPORT_STYLE_DD.intValue()){ //��Ե�ʽ����תExcel
    			
				book=writeDDExcel(excel,records);
				if(book!=null) book=setReportHeaderAndFooter(book,this.reportInForm);
			}else if(this.reportStyle==Config.REPORT_STYLE_QD.intValue()){ //�嵥ʽ����תExcel
				book=writeQDExcel(excel,records,this.reportInForm);
			}
    		    		
    		if(book!=null){
	    		bout=new BufferedOutputStream(out);
	    		book.write(bout);
	    		out.flush();
	    		bout.close();
	    		out.close();                 // 07��4��18�Ÿ�
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
	 * д��Ե�ʽExcel
	 * 
	 * @param in InputStream ������
	 * @param cells List ����Ԫ����Ϣ�б�
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
	 * д�嵥ʽExcel
	 * 
	 * @param in InputStream ������
	 * @param cells List ����Ԫ����Ϣ�б�
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
	 * ��ȡExcelģ���ӦPDFģ�����ʼ�е�ƫ����
	 * 
	 * @param childRepId String ������
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
	 * ����Դ�ļ��У�����������ȡ��ֵ
	 * 
	 * @param resourcesFile String ��Դ�ļ�
	 * @param key String ����
	 * @return String ����ֵ
	 */
	private String getValue(String resourcesFile,String key){
		if(resourcesFile==null || key==null) return null;
		
		MessageResources resources=MessageResources.getMessageResources(resourcesFile);
		
		String value=resources.getMessage(this.LOCALE,key);
		
		return value==null?null:value.trim();
	}	
	
	/**
	 * ���ñ����ͷ��β
	 * 
	 * @param book HSSFWorkbook Excel������
	 * @param reportInForm �������
	 * @return HSSFWorkbook
	 */
	private HSSFWorkbook setReportHeaderAndFooter(HSSFWorkbook book,ReportInForm reportInForm){
		HSSFWorkbook resBook=null;
		
		if(book==null || reportInForm==null) return null;
		
		File file=null;                     //�ļ���
		FileOutputStream fos=null;          //�ļ������
		BufferedOutputStream bos=null;      //���������
		FileInputStream fis=null;           //�ļ�������
		BufferedInputStream bis=null;       //����������
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
	 * ����Excel�ĵ� ��ָ��·��
	 * @param String filePath ����ļ�·��
	 * @return boolean �ɹ���true ʧ�ܷ���false
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
