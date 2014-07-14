package com.cbrc.smis.excel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.db2xml.Db2XmlUtil;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.util.FitechException;

public class CreateExcel {
	FitechException log=new FitechException(CreateExcel.class);
	
	Locale LOCALE=Locale.CHINA;
	/**
	 * Excelģ���ӦPDFģ�����ʼ�е�ƫ����
	 */
	private String REPORTFILE="com/cbrc/smis/excel/OffsetResources";
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
	public CreateExcel(Integer repInId){
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
		this.excelFile=getExcelFile(childRepId,versionId);
//		this.percentCells=new Db2XmlUtil().getPercentCells(this.childRepId,this.versionId);
	}
	
	/**
	 * ��ȡ�����Ӧ��Excelģ����ļ�
	 * 
	 * @param childRepId String PDFģ��ı��
	 * @param versionId String ģ��İ汾��
	 * @return String
	 */
	private String getExcelFile(String childRepId,String versionId){
		if(childRepId==null || versionId==null) return null;
		return childRepId.trim()+versionId.trim()+".xls";
		//return getValue(CONFIGFILE,childRepId.trim() + versionId.trim());
	}
	
	/**
	 * ��ȡ�����ʵ������
	 * 
	 * @param repInId Integer
	 * @return List
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
	 * @return void 
	 */
	public HSSFWorkbook createDataReport(){
		boolean res = true;
		HSSFWorkbook book = null;
		try{
			init();
			if(this.reportStyle!=Config.REPORT_STYLE_DD.intValue() && 
					this.reportStyle!=Config.REPORT_STYLE_QD.intValue())
				res=false;
			List cells = reportData();
			
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
			
			List cells=reportData();
			
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
					File file = new File(fileName+File.separator + this.childRepId + "_" + this.versionId+"_"+this.reportInForm.getDataRangeId()+"_"+this.reportInForm.getCurId() +"_"+ this.reportInForm.getOrgId()+".xls");
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
	 * ����Excel�ĵ�
	 * 
	 * @param cells records ��Ԫ����Ϣ�б�
	 * @return boolean �����ɹ�������true;���򣬷���false
	 */
	private HSSFWorkbook writeExcel(List records){
	
		HSSFWorkbook book=null;
		
		File file=null;
		FileInputStream excel=null;
		
		try{
			file=new File(
					Config.WEBROOTPATH + "report_mgr" + 
					Config.FILESEPARATOR + "excel" + 
					Config.FILESEPARATOR + this.excelFile
			);
    		if(file.exists()==false) return book;
    		
    		excel=new FileInputStream(file);
    		    		
    		if(reportStyle==Config.REPORT_STYLE_DD.intValue()){ //��Ե�ʽ����תExcel
				book=writeDDExcel(excel,records);
				if(book!=null) book=setReportHeaderAndFooter(book,this.reportInForm);
			}else if(this.reportStyle==Config.REPORT_STYLE_QD.intValue()){ //�嵥ʽ����תExcel
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
	private void filterBBName(HSSFSheet sheet)throws Exception {
		HSSFRow row = sheet.getRow(0);
		if(row!=null){
			HSSFCell cell = row.getCell((short)0);
			if(cell==null)
				return;
			try{
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				String value = cell.getStringCellValue();
				cell.setCellValue(value.replaceAll("���ϲ���", ""));
			}catch(Exception e){
				log.println("�����ͷ�޸Ĵ���");
				log.printStackTrace(e);
			}
		}
	}
	private void filterBBQDName(HSSFWorkbook book)throws Exception {
		if(book==null)
			return;
		HSSFSheet sheet = book.getSheetAt(0);
		if(sheet==null)
			return;
		try{
			this.filterBBName(sheet);
		}catch(Exception e){
			log.println("�����嵥ʽ��ͷ�޸Ĵ���");
			log.printStackTrace(e);
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
		Number cellNumb = null;
		this.filterBBName(sheet);//���˺ϲ�����
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
//			HSSFCellStyle cellStyle=hssfCell.getCellStyle();
//			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//			hssfCell.setCellStyle(cellStyle);
			
			if(hssfCell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
			//Excel�еĹ�ʽ�����趨��ֵ��������������POI�Զ����ݹ�ʽ���¼���
			}else if(//hssfCell.getCellType() == HSSFCell.CELL_TYPE_FORMULA || 
					hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
				try{
					Double d = Double.valueOf(cell.getReportValue());
					hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					hssfCell.setCellValue(d.doubleValue());
				}catch(Exception ex){
					hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					//hssfCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
					hssfCell.setCellValue(Utils.isPercentCell(this.percentCells,cell.getCellName())==true?
							Utils.round(cell.getReportValue(),2):
								(cell.getReportValue()==null?"":Utils.formatVal(cell.getReportValue())));
				}
			}else{				 
				try {
					//2013-11-08 LYF:�������ʮ�ҵ�13ϵ�к�14ϵ�У���Ҫ���⴦��--��ֹ���ֿ�ѧ���㷨�����⣬ͬʱ��Ҫ������0��ͷ���ַ���(��000855956������Ӧ��Ϊ855956)
					if(((childRepId.startsWith("G13")||childRepId.startsWith("GF13"))&&colIndex==2)//13ϵ�е�C��
							||((childRepId.startsWith("G14")||childRepId.startsWith("GF14"))&&colIndex==3)){//14ϵͳ��D��
						hssfCell.setCellValue(cell.getReportValue());//ֱ��ʹ�����ݿ��е�ֵ��������ֵת�����ֵ��쳣���
						continue;//������뼴���Բ��ô���
					}
					cellNumb = parseNumber(cell.getReportValue());
					if(cellNumb == null){
						hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);
						hssfCell.setCellValue(cell.getReportValue());
					}else{
						if(hssfCell.getCellType() !=HSSFCell.CELL_TYPE_STRING )
							hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						if(Utils.isPercentCell(this.percentCells, cell.getCellName()) == true)
							hssfCell.setCellValue(Utils.round(cell.getReportValue(), 2));
						else
							hssfCell.setCellValue(cellNumb.doubleValue());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			 
		}
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
		this.filterBBQDName(book);
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
			//�趨�������ƣ����ڵ�����ʱ����ʾ
			if(reportInForm.getOrgId()!=null && !reportInForm.getOrgId().trim().equals("")){
				com.fitech.gznx.po.AfOrg org = com.fitech.gznx.service.AFOrgDelegate.getOrgInfo(reportInForm.getOrgId());
				if(org!=null && reportInForm!=null){
					reportInForm.setOrgName(org.getOrgName());
				}
			}
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
