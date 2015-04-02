package com.fitech.gznx.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.hibernate.MCellFormu;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFGatherformulaFrom;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfValidateformula;
import com.fitech.gznx.service.AFReportmergerView;
import com.fitech.gznx.service.AFTemplateDelegate;


public class CheckExcel {
	


	FitechException log=new FitechException(CheckExcel.class);
	//表头
	public static final String[] checkHeader = {"公式","公式名称","校验类别"};
	public static final String[] gathHeader = {"单元格ID","单元格名称","公式"};
	
	public static final String[] allcheckHeader = {"报表ID","版本号","报表名称","公式","公式名称","校验类别"};
	public static final String[] allgathHeader = {"报表ID","版本号","报表名称","单元格ID","单元格名称","公式"};
	
	 //创建工作本   
    public static HSSFWorkbook checkWorkBook = new HSSFWorkbook();  
  //创建表   
    public static HSSFSheet excelsheet = checkWorkBook.createSheet("Sheet1"); 
    

    
  //数据库表的列数   
    public static final int columNumber = 7;  
    
    /**  
     * 创建表头  
     * @return  
     */  
    public static void createTableHeader(String[] tableHeader)   {
    	  
    	 HSSFRow headerRow = excelsheet.createRow((short) 0);  
    	
    	 for(int i = 0;i < tableHeader.length;i++)   
         {   
             HSSFCell headerCell = headerRow.createCell((short) i);   
             headerCell.setEncoding(HSSFCell.ENCODING_UTF_16);   
             headerCell.setCellValue(tableHeader[i]);   
         }   
    }
    
    /**  
     * 创建表头  
     * @return  
     */  
    public static void createTableallHeader(String[] tableHeader)   {
    	  
    	 HSSFRow headerRow = excelsheet.createRow((short) 0);  
    	
    	 for(int i = 0;i < tableHeader.length;i++)   
         {   
             HSSFCell headerCell = headerRow.createCell((short) i);   
             headerCell.setEncoding(HSSFCell.ENCODING_UTF_16);   
             headerCell.setCellValue(tableHeader[i]);   
         }   

    }
    
    /**  
     * 创建行  
     * @param cells  
     * @param rowIndex  
     */  
    public static void createTableRow(List<String> cells,short rowIndex)   
    {   
    	//创建第rowIndex行   
        HSSFRow row = excelsheet.createRow((short) rowIndex);   
        for(short i = 0;i < cells.size();i++)   
        {   
            //创建第i个单元格   
            HSSFCell cell = row.createCell((short) i);   
            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
            cell.setCellValue(cells.get(i));   
        }   

    }
    /**  
     * 已使用hibernate 卞以刚 2011-12-27
     * 影响对象：AfTemplate AfGatherformula AfCellinfo MCellFormu ValidateType
     * 创建整个Excel表  
     * @throws SQLException   
     *  
     */  
    public static void createExcelSheeet(String templateId,String versionId ,String forflg,String reportFlg) 
    {   
    	if(checkWorkBook==null){
    		checkWorkBook = new HSSFWorkbook();
    		excelsheet = checkWorkBook.createSheet("Sheet1");
    	//	checkWorkBook.setSheetName(0, "校验关系公式表",(short)1);
    	}
    	/**已使用hibernate 卞以刚 2011-12-22
    	 *  影响对象：AfTemplate**/
    	AfTemplate template = AFTemplateDelegate.getTemplate(templateId,versionId);
    	HSSFHeader header = excelsheet.getHeader();  
    	header.setCenter(template.getTemplateName()); 
    	int rownum=1;
    	if(forflg.equals("1")){
    		createTableHeader(gathHeader);
    		/**已使用hibernate 卞以刚 2011-12-22
    		 * 影响对象：AfGatherformula AfCellinfo**/
    		List<Object[]> gatherFormulaList = AFReportmergerView.getGatherFormula(templateId, versionId);
			for(Object[] merger:gatherFormulaList){
				int j=0;		    			
    			//创建第rowIndex行   
    		    HSSFRow row = excelsheet.createRow((short) (rownum)); 
    		    rownum++;
    		    
		    	//创建第i个单元格   
	            HSSFCell cell = row.createCell((short)(j++));   
	            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
	            cell.setCellValue((String)merger[3]);
	            //创建第i个单元格   
	            cell = row.createCell((short)(j++));   
	            cell.setEncoding(HSSFCell.ENCODING_UTF_16);
	            if(merger[6] != null && !StringUtil.isEmpty((String)merger[6])){
	            	 cell.setCellValue((String)merger[6]+":"+(String)merger[4]+"_"+(String)merger[5]);
	            } else {
	            	 cell.setCellValue((String)merger[4]+"_"+(String)merger[5]);
	            }
	            //创建第i个单元格   
	            cell = row.createCell((short)(j++));   
	            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
	            cell.setCellValue((String)merger[1]);
    		    
    		 }
    		
    	} else{
    		createTableHeader(checkHeader);
    		List<Object[]> validateFormulaList = new ArrayList();
			/** 报表选中标志 **/
			if(reportFlg.equals("1")){
				/** 使用hibernate 卞以刚 2011-12-22
				 * 影响对象：MCellFormu ValidateType**/
			 validateFormulaList = AFReportmergerView.getAllBNJValidateList(templateId, versionId);
			}else{
				/** 使用hibernate 卞以刚 2011-12-22
				 * 影响对象：AfValidateformula ValidateType**/
			 validateFormulaList = AFReportmergerView.getValidateFormula(templateId, versionId);
			}
			//for(Object[] validate:validateFormulaList){
			for(Object[] validate:validateFormulaList){
				int j=0;		    			
    			//创建第rowIndex行   
				HSSFRow row = excelsheet.createRow((short) (rownum)); 
    		    rownum++;
		    	 //创建第i个单元格    {"报表ID","报表名称","公式ID","公式","公式名称","校验类别"};
	            HSSFCell cell = row.createCell((short)(j++));   
	            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
	            cell.setCellValue((String)validate[1]);
	            //创建第i个单元格   
	            cell = row.createCell((short)(j++));   
	            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
	            cell.setCellValue((String)validate[2]);
	            //创建第i个单元格   
	            cell = row.createCell((short)(j++));   
	            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
	            cell.setCellValue(String.valueOf(validate[3]));
			}
    	}
    }
    
    /**  
     * 导出表格  
     * @param sheet  
     * @param os  
     * @throws IOException  
     */  
    public void exportExcel(OutputStream os) throws IOException   
    {   
    	excelsheet.setGridsPrinted(true);   
        HSSFFooter footer = excelsheet.getFooter();   
        footer.setRight("Page " + HSSFFooter.page() + " of " +   
        HSSFFooter.numPages());   
        checkWorkBook.write(os);
        checkWorkBook=null;
    }
    /**  
     * 已使用hibernate 卞以刚 2011-12-22
     * 影响对象：AfTemplate AfGatherformula AfCellinfo ValidateType
     * 创建整个Excel表  
     * @throws SQLException   
     *  
     */ 
	public static void createAllExcelSheeet(String templateType, String templateName,
			String	reportFlg,String forflg) {
    	if(checkWorkBook==null){
    		checkWorkBook = new HSSFWorkbook();
    		excelsheet = checkWorkBook.createSheet("Sheet1");
    		//checkWorkBook.setSheetName(0, "校验关系公式表",(short)1);
    	}
		HSSFHeader header = excelsheet.getHeader();
		int rownum =1;
		if(forflg.equals("1")){
			createTableallHeader(allgathHeader); 
			/**已使用hibernate 卞以刚 2011-12-22
			 * 影响对象：AfTemplate**/
			List<AfTemplate> templateList = AFTemplateDelegate.selectAllTemplate(templateName,templateType,reportFlg);
			if(templateList !=null && templateList.size()>0){
				for(AfTemplate template:templateList){
					String templateId = template.getId().getTemplateId();
					String versionId = template.getId().getVersionId();
					/**已使用hibernate 卞以刚 2011-12-22
					 * 影响对象：AfGatherformula AfCellinfo**/
					List<Object[]> gatherFormulaList = AFReportmergerView.getGatherFormula(templateId, versionId);
					for(Object[] merger:gatherFormulaList){
						int j=0;		    			
		    			//创建第rowIndex行   
		    		    HSSFRow row = excelsheet.createRow((short) (rownum)); 
		    		    rownum++;
		    		    
				    	//创建第i个单元格   
			            HSSFCell cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(templateId);
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(versionId);
			            //创建第i个单元格   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(template.getTemplateName());
			            //创建第i个单元格   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue((String)merger[3]);
			            //创建第i个单元格   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			            if(merger[6] != null && !StringUtil.isEmpty((String)merger[6])){
			            	 cell.setCellValue((String)merger[6]+":"+(String)merger[4]+"_"+(String)merger[5]);
			            } else {
			            	 cell.setCellValue((String)merger[4]+"_"+(String)merger[5]);
			            }
			            //创建第i个单元格   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue((String)merger[1]);
			           
			            
					}
				}
			}
			
		} else{
			createTableallHeader(allcheckHeader); 
			/**已使用hibernate 卞以刚 2011-12-22
			 * 影响对象：AfTemplate**/
			List<AfTemplate> templateList = AFTemplateDelegate.selectAllTemplate(templateName,templateType,reportFlg);
			if(templateList !=null && templateList.size()>0){
				for(AfTemplate template:templateList){
					String templateId = template.getId().getTemplateId();
					String versionId = template.getId().getVersionId();
					//List<Object[]> validateFormulaList = AFReportmergerView.getValidateFormula(templateId, versionId); yql注释掉的
					List<Object[]> validateFormulaList = new ArrayList();
					/** 报表选中标志 **/
					if(reportFlg.equals("1")){
						/**使用hibernate 卞以刚 2011-12-22
						 * 影响对象：MCellFormu ValidateType**/
					 validateFormulaList = AFReportmergerView.getAllBNJValidateList(templateId, versionId);
					}else{
						/**已使用hibernate 卞以刚 2011-12-22
						 * 影响对象：AfValidateformula ValidateType**/
					 validateFormulaList = AFReportmergerView.getValidateFormula(templateId, versionId);
					}
					//for(Object[] validate:validateFormulaList){
					for(Object[] validate:validateFormulaList){
						int j=0;		    			
		    			//创建第rowIndex行   
						HSSFRow row = excelsheet.createRow((short) (rownum)); 
		    		    rownum++;
				    	 //创建第i个单元格    {"报表ID","报表名称","公式ID","公式","公式名称","校验类别"};
			            HSSFCell cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(templateId);
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(versionId);
			            //创建第i个单元格   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(template.getTemplateName());

			            //创建第i个单元格   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue((String)validate[1]);
			            //创建第i个单元格   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue((String)validate[2]);
			            //创建第i个单元格   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(String.valueOf(validate[3]));
					}
				}
			}
			
		}
		
	}
	
       
  }   
    
