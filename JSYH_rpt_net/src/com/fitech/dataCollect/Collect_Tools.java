package com.fitech.dataCollect;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.util.FitechLog;

public class Collect_Tools {
	private static String log = "";

	/**
	 * ���ݻ��ܺ���
	 *@author gen
	 *@return 0 ����Ϊ�գ�1 ��ݡ��·ݲ���ȷ 2 ���������ļ��д��� 3��ȷ
	 * @throws FileNotFoundException 
	 */
	public static boolean collecttoExcel(List repList,String year,String mon) throws FileNotFoundException{

		boolean re=false;
		int state=0;
		//��֤����
		if(repList==null&&repList.size()<1){
			log ="д��Excel�Ĳ�������";
			FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,ConfigOncb.HANDLER,log);
			return re;
		}

		if(year==null||mon==null||Integer.parseInt(mon)>12){
			log ="д��Excel�Ĳ�������";
			FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,ConfigOncb.HANDLER,log);
			return re;
		}
		
		
        //��������ļ���
		
		String outpath=CollectConfig.Excel_OutPath+Config.FILESEPARATOR+year+mon;
		if(!Util.mkdir(outpath)){
			log ="���������ļ��г���";
			FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,ConfigOncb.HANDLER,log);
			return re;
			
		}
				
		for(int i=0;i<repList.size();i++){
			Collect_Report report = (Collect_Report)repList.get(i);
			String modelname = report.getChildRepId()+"_"+report.getVersionId();
			File model = new File(CollectConfig.Excel_ModelPath+CollectConfig.FILESEPARATOR+modelname+".xls");
			if(model.exists()&&model.isFile()){
				OutputStream out=new FileOutputStream(outpath+CollectConfig.FILESEPARATOR+report.getDataRangeId()+modelname+".xls");
				collecttoExcel(report,model,out);
			}else{
				log =report.getChildRepName()+"_"+report.getVersionId()+"��Ӧ��ģ�岻���ڣ�";
				FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,ConfigOncb.HANDLER,log);
				return re;
			}			
		}
		
		re = true;
		
		return re;		
	}
	
	/**
	 * �����������
	 * @param report
	 * @param year
	 * @param mon
	 * @return
	 * @throws FileNotFoundException
	 */
	public static boolean collecttoExcel(Collect_Report report,String year,String mon) throws FileNotFoundException{
		//report.setFlag(1);
		List repList=new ArrayList();
		repList.add(report);
		return collecttoExcel(repList,year,mon);
		
	}
	
	/**
	 * ����
	 * @author GEN
	 * @param collect ����
	 * @param model ģ��
	 * @param out�ļ������
	 */
	private static void collecttoExcel(Collect_Report collect,File model,OutputStream out){
			
		if(collect==null)
			return;
		FileInputStream in =null;
		
		try{
			in=new FileInputStream(model);
			BufferedOutputStream bout=null;
								
			POIFSFileSystem fs=new POIFSFileSystem(in);
			HSSFWorkbook book=new HSSFWorkbook(fs);
    		
		    HSSFSheet sheet=book.getSheetAt(0);
		    in=new FileInputStream(model);
		    		    
		    /**��ʽ*/
		    if(collect.getFlag()==1)
		    {	
		    	//// System.out.println("ToExcel====ID=="+collect.getChildRepId()+"==Size==="+collect.getP2p_dataList().size());
		    	List p2pList = collect.getP2p_dataList();
		    	if(p2pList!=null)
		    	{
			    	for(int i=0;i<p2pList.size();i++)
			    	{
						P2PRep_Data report=(P2PRep_Data)p2pList.get(i);
		    		  		       		
		    		    HSSFRow row=null;
		    		    HSSFCell cell=null;
		    		    String cellValue=report.getValue();
		    		    
		    		    row=sheet.getRow(report.getRowId().intValue()-1);
		    		
		    		    cell=row.getCell((short)Util.convertColStringToNum(report.getColId()));
		    		    if(cell==null)
		    		    {
		    		    	
		    		    	log =collect.getChildRepName()+"_"+collect.getVersionId()+"��Ӧ�ĵ�Ԫ�� " +
		    		    			"��"+report.getRowId().intValue()+"�� ��"+report.getColId()+"�������⣡";
		    				FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,ConfigOncb.HANDLER,log);
		    				//// System.out.println("Cell Wrong==========================");
		    				return;
		    		    }
		    		    /**�����Ѿ������˹�ʽ�ĵ�Ԫ�������ݣ��Ա��������Լ����кϼ������*/
		    		    //if(!(cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA))
		    		    cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						String temp=Util.getUnicode(cellValue,"gb2312");
						cell.setCellValue(temp);
		    		   // // System.out.println("Row==="+report.getRowId().intValue()+"==Col==="+report.getColId()+"===Value=="+cellValue);
					}
		    	}
		    
		    }
		    /**�嵥ʽ*/
		  
		    if (collect.getFlag() == 2) 
		    {
		    	List billList=collect.getBill_dataList();
		    	if(billList!=null)
		    	{
			    	//������ʼ��
			    	collect.setStartRow(3);
			    	int startRow=collect.getStartRow();
					
			    	//// System.out.println("����==== "+billList.size());
			    	int rowNum = billList.size();
			    	/**excel�������*/
			    	int MAX = 6000;
			    	
			    	rowNum = (rowNum+startRow < MAX ) ? rowNum : MAX; 
			    	
			    	for (int i = 0; i < rowNum; i++) 
					{
						
						BillRep_Data report = (BillRep_Data) billList.get(i);
						if(report==null)
							return;
						String data = report.getData();
						if(data==null)
							return;
						String[] content = data.split(",");
						
						HSSFRow row =sheet.createRow(startRow++);
						HSSFCell cell = null;
						
						//// System.out.println("����==== "+content.length);
						
						for(int j=0;j<content.length;j++)
						{
							//// System.out.println("dsfsdfsdfsdf������"+content[j]);
							//// System.out.println("COl"+j);
							cell=row.createCell((short)j);
							
							
							if(cell==null)
							{
								//// System.out.println("Error********************");
								log =collect.getChildRepName()+"_"+collect.getVersionId()+"��Ӧ�ĵ�Ԫ�� " +
	    		    			"��"+report.getRowId()+"�� �������⣡";
								FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,ConfigOncb.HANDLER,log);
								continue;
							}if(j == 0){
								cell.setCellValue(i);
							}else{
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setEncoding(HSSFCell.ENCODING_UTF_16);
								String temp=Util.getUnicode(content[j],"gb2312");
								cell.setCellValue(temp);
							}													
						}	
					}
		    	}
			}
	
    		bout=new BufferedOutputStream(out);
    		book.write(bout);
    		out.flush();
    		out.close();
    	}catch(IOException ioe){
    		ioe.printStackTrace();
    		new Exception(ioe);
    	}catch(Exception e){
    		e.printStackTrace();
    		new Exception(e);
    	}finally{
    		if(in!=null){
    			try{
    				in.close();
    			}catch(IOException ioe){
    				new Exception(ioe);
    			}
    		}
    	}    	
	}

}
