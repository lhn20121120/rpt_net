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
	//��ͷ
	public static final String[] checkHeader = {"��ʽ","��ʽ����","У�����"};
	public static final String[] gathHeader = {"��Ԫ��ID","��Ԫ������","��ʽ"};
	
	public static final String[] allcheckHeader = {"����ID","�汾��","��������","��ʽ","��ʽ����","У�����"};
	public static final String[] allgathHeader = {"����ID","�汾��","��������","��Ԫ��ID","��Ԫ������","��ʽ"};
	
	 //����������   
    public static HSSFWorkbook checkWorkBook = new HSSFWorkbook();  
  //������   
    public static HSSFSheet excelsheet = checkWorkBook.createSheet("Sheet1"); 
    

    
  //���ݿ�������   
    public static final int columNumber = 7;  
    
    /**  
     * ������ͷ  
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
     * ������ͷ  
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
     * ������  
     * @param cells  
     * @param rowIndex  
     */  
    public static void createTableRow(List<String> cells,short rowIndex)   
    {   
    	//������rowIndex��   
        HSSFRow row = excelsheet.createRow((short) rowIndex);   
        for(short i = 0;i < cells.size();i++)   
        {   
            //������i����Ԫ��   
            HSSFCell cell = row.createCell((short) i);   
            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
            cell.setCellValue(cells.get(i));   
        }   

    }
    /**  
     * ��ʹ��hibernate ���Ը� 2011-12-27
     * Ӱ�����AfTemplate AfGatherformula AfCellinfo MCellFormu ValidateType
     * ��������Excel��  
     * @throws SQLException   
     *  
     */  
    public static void createExcelSheeet(String templateId,String versionId ,String forflg,String reportFlg) 
    {   
    	if(checkWorkBook==null){
    		checkWorkBook = new HSSFWorkbook();
    		excelsheet = checkWorkBook.createSheet("Sheet1");
    	//	checkWorkBook.setSheetName(0, "У���ϵ��ʽ��",(short)1);
    	}
    	/**��ʹ��hibernate ���Ը� 2011-12-22
    	 *  Ӱ�����AfTemplate**/
    	AfTemplate template = AFTemplateDelegate.getTemplate(templateId,versionId);
    	HSSFHeader header = excelsheet.getHeader();  
    	header.setCenter(template.getTemplateName()); 
    	int rownum=1;
    	if(forflg.equals("1")){
    		createTableHeader(gathHeader);
    		/**��ʹ��hibernate ���Ը� 2011-12-22
    		 * Ӱ�����AfGatherformula AfCellinfo**/
    		List<Object[]> gatherFormulaList = AFReportmergerView.getGatherFormula(templateId, versionId);
			for(Object[] merger:gatherFormulaList){
				int j=0;		    			
    			//������rowIndex��   
    		    HSSFRow row = excelsheet.createRow((short) (rownum)); 
    		    rownum++;
    		    
		    	//������i����Ԫ��   
	            HSSFCell cell = row.createCell((short)(j++));   
	            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
	            cell.setCellValue((String)merger[3]);
	            //������i����Ԫ��   
	            cell = row.createCell((short)(j++));   
	            cell.setEncoding(HSSFCell.ENCODING_UTF_16);
	            if(merger[6] != null && !StringUtil.isEmpty((String)merger[6])){
	            	 cell.setCellValue((String)merger[6]+":"+(String)merger[4]+"_"+(String)merger[5]);
	            } else {
	            	 cell.setCellValue((String)merger[4]+"_"+(String)merger[5]);
	            }
	            //������i����Ԫ��   
	            cell = row.createCell((short)(j++));   
	            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
	            cell.setCellValue((String)merger[1]);
    		    
    		 }
    		
    	} else{
    		createTableHeader(checkHeader);
    		List<Object[]> validateFormulaList = new ArrayList();
			/** ����ѡ�б�־ **/
			if(reportFlg.equals("1")){
				/** ʹ��hibernate ���Ը� 2011-12-22
				 * Ӱ�����MCellFormu ValidateType**/
			 validateFormulaList = AFReportmergerView.getAllBNJValidateList(templateId, versionId);
			}else{
				/** ʹ��hibernate ���Ը� 2011-12-22
				 * Ӱ�����AfValidateformula ValidateType**/
			 validateFormulaList = AFReportmergerView.getValidateFormula(templateId, versionId);
			}
			//for(Object[] validate:validateFormulaList){
			for(Object[] validate:validateFormulaList){
				int j=0;		    			
    			//������rowIndex��   
				HSSFRow row = excelsheet.createRow((short) (rownum)); 
    		    rownum++;
		    	 //������i����Ԫ��    {"����ID","��������","��ʽID","��ʽ","��ʽ����","У�����"};
	            HSSFCell cell = row.createCell((short)(j++));   
	            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
	            cell.setCellValue((String)validate[1]);
	            //������i����Ԫ��   
	            cell = row.createCell((short)(j++));   
	            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
	            cell.setCellValue((String)validate[2]);
	            //������i����Ԫ��   
	            cell = row.createCell((short)(j++));   
	            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
	            cell.setCellValue(String.valueOf(validate[3]));
			}
    	}
    }
    
    /**  
     * �������  
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
     * ��ʹ��hibernate ���Ը� 2011-12-22
     * Ӱ�����AfTemplate AfGatherformula AfCellinfo ValidateType
     * ��������Excel��  
     * @throws SQLException   
     *  
     */ 
	public static void createAllExcelSheeet(String templateType, String templateName,
			String	reportFlg,String forflg) {
    	if(checkWorkBook==null){
    		checkWorkBook = new HSSFWorkbook();
    		excelsheet = checkWorkBook.createSheet("Sheet1");
    		//checkWorkBook.setSheetName(0, "У���ϵ��ʽ��",(short)1);
    	}
		HSSFHeader header = excelsheet.getHeader();
		int rownum =1;
		if(forflg.equals("1")){
			createTableallHeader(allgathHeader); 
			/**��ʹ��hibernate ���Ը� 2011-12-22
			 * Ӱ�����AfTemplate**/
			List<AfTemplate> templateList = AFTemplateDelegate.selectAllTemplate(templateName,templateType,reportFlg);
			if(templateList !=null && templateList.size()>0){
				for(AfTemplate template:templateList){
					String templateId = template.getId().getTemplateId();
					String versionId = template.getId().getVersionId();
					/**��ʹ��hibernate ���Ը� 2011-12-22
					 * Ӱ�����AfGatherformula AfCellinfo**/
					List<Object[]> gatherFormulaList = AFReportmergerView.getGatherFormula(templateId, versionId);
					for(Object[] merger:gatherFormulaList){
						int j=0;		    			
		    			//������rowIndex��   
		    		    HSSFRow row = excelsheet.createRow((short) (rownum)); 
		    		    rownum++;
		    		    
				    	//������i����Ԫ��   
			            HSSFCell cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(templateId);
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(versionId);
			            //������i����Ԫ��   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(template.getTemplateName());
			            //������i����Ԫ��   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue((String)merger[3]);
			            //������i����Ԫ��   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			            if(merger[6] != null && !StringUtil.isEmpty((String)merger[6])){
			            	 cell.setCellValue((String)merger[6]+":"+(String)merger[4]+"_"+(String)merger[5]);
			            } else {
			            	 cell.setCellValue((String)merger[4]+"_"+(String)merger[5]);
			            }
			            //������i����Ԫ��   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue((String)merger[1]);
			           
			            
					}
				}
			}
			
		} else{
			createTableallHeader(allcheckHeader); 
			/**��ʹ��hibernate ���Ը� 2011-12-22
			 * Ӱ�����AfTemplate**/
			List<AfTemplate> templateList = AFTemplateDelegate.selectAllTemplate(templateName,templateType,reportFlg);
			if(templateList !=null && templateList.size()>0){
				for(AfTemplate template:templateList){
					String templateId = template.getId().getTemplateId();
					String versionId = template.getId().getVersionId();
					//List<Object[]> validateFormulaList = AFReportmergerView.getValidateFormula(templateId, versionId); yqlע�͵���
					List<Object[]> validateFormulaList = new ArrayList();
					/** ����ѡ�б�־ **/
					if(reportFlg.equals("1")){
						/**ʹ��hibernate ���Ը� 2011-12-22
						 * Ӱ�����MCellFormu ValidateType**/
					 validateFormulaList = AFReportmergerView.getAllBNJValidateList(templateId, versionId);
					}else{
						/**��ʹ��hibernate ���Ը� 2011-12-22
						 * Ӱ�����AfValidateformula ValidateType**/
					 validateFormulaList = AFReportmergerView.getValidateFormula(templateId, versionId);
					}
					//for(Object[] validate:validateFormulaList){
					for(Object[] validate:validateFormulaList){
						int j=0;		    			
		    			//������rowIndex��   
						HSSFRow row = excelsheet.createRow((short) (rownum)); 
		    		    rownum++;
				    	 //������i����Ԫ��    {"����ID","��������","��ʽID","��ʽ","��ʽ����","У�����"};
			            HSSFCell cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(templateId);
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(versionId);
			            //������i����Ԫ��   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(template.getTemplateName());

			            //������i����Ԫ��   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue((String)validate[1]);
			            //������i����Ԫ��   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue((String)validate[2]);
			            //������i����Ԫ��   
			            cell = row.createCell((short)(j++));   
			            cell.setEncoding(HSSFCell.ENCODING_UTF_16);   
			            cell.setCellValue(String.valueOf(validate[3]));
					}
				}
			}
			
		}
		
	}
	
       
  }   
    
