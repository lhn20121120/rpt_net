package com.cbrc.smis.util;

import java.util.List;
import java.util.Locale;

import org.apache.struts.util.MessageResources;

import com.cbrc.smis.form.MExcelChildReportForm;

public class FitechEXCELReport {
	
	public static MExcelChildReportForm setMExcelChildReportFormRepId(MExcelChildReportForm mExcelChildReportForm)
	{
		if(mExcelChildReportForm==null){
			return null;
		}
		String formId=null;
		if(mExcelChildReportForm.getChildRepId().length()==5){
			formId=mExcelChildReportForm.getChildRepId();
			return mExcelChildReportForm;
		}
		
		formId=mExcelChildReportForm.getChildRepId();
		// System.out.println("oldRepId is "+formId);
		int j=5-formId.length();
		for(int i=1;i<=j;i++)
		{
			formId=formId+"0";
		}
		// System.out.println("newRepId is "+formId);
		mExcelChildReportForm.setChildRepId(formId);
		return mExcelChildReportForm;
	}
	/**
	 * Excelģ���ӦPDFģ�����ʼ�е�ƫ����
	 */
	private static String REPORTFILE="com/cbrc/smis/excel/OffsetResources";
	/**
	 * ��ȡExcelģ���ӦPDFģ�����ʼ�е�ƫ����
	 * 
	 * @param childRepId String ������
	 * @return int
	 */
	
	public static  int getOffsetRows(String childRepId,String versionId){
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
	static Locale LOCALE=Locale.CHINA;
	/**
	 * ����Դ�ļ��У�����������ȡ��ֵ
	 * 
	 * @param resourcesFile String ��Դ�ļ�
	 * @param key String ����
	 * @return String ����ֵ
	 */
	private static String getValue(String resourcesFile,String key){
		if(resourcesFile==null || key==null) return null;
		
		MessageResources resources=MessageResources.getMessageResources(resourcesFile);
		
		String value=resources.getMessage(LOCALE,key);
		
		return value==null?null:value.trim();
	}	/*
	private List loop(List cellList,int col,int row,String content){
		for(int i=0;i<cellList.size();i++){
			excelToHTML.cell.Cell cell=(excelToHTML.cell.Cell)cellList.get(i);
			int cellRow=cell.getRowNum();
			int cellCol=cell.getColNum();
			if(cellRow==row&&cellCol==col){
				cell.setCellValue(content);
				cellList.set(i,cell);
				return cellList;
			}
		}
		return cellList;
	}
	*/
}
