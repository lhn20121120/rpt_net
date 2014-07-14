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
	 * 数据汇总函数
	 *@author gen
	 *@return 0 报表为空；1 年份、月份不正确 2 建立输入文件夹错误 3正确
	 * @throws FileNotFoundException 
	 */
	public static boolean collecttoExcel(List repList,String year,String mon) throws FileNotFoundException{

		boolean re=false;
		int state=0;
		//验证参数
		if(repList==null&&repList.size()<1){
			log ="写入Excel的参数错误！";
			FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,ConfigOncb.HANDLER,log);
			return re;
		}

		if(year==null||mon==null||Integer.parseInt(mon)>12){
			log ="写入Excel的参数错误！";
			FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,ConfigOncb.HANDLER,log);
			return re;
		}
		
		
        //创建输出文件夹
		
		String outpath=CollectConfig.Excel_OutPath+Config.FILESEPARATOR+year+mon;
		if(!Util.mkdir(outpath)){
			log ="生成输入文件夹出错！";
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
				log =report.getChildRepName()+"_"+report.getVersionId()+"对应的模板不存在！";
				FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,ConfigOncb.HANDLER,log);
				return re;
			}			
		}
		
		re = true;
		
		return re;		
	}
	
	/**
	 * 单个报表汇总
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
	 * 汇总
	 * @author GEN
	 * @param collect 报表
	 * @param model 模板
	 * @param out文件输出流
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
		    		    
		    /**点式*/
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
		    		    	
		    		    	log =collect.getChildRepName()+"_"+collect.getVersionId()+"对应的单元格 " +
		    		    			"第"+report.getRowId().intValue()+"行 第"+report.getColId()+"列有问题！";
		    				FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,ConfigOncb.HANDLER,log);
		    				//// System.out.println("Cell Wrong==========================");
		    				return;
		    		    }
		    		    /**不向已经设置了公式的单元格填数据，以便他可以自己进行合计项操作*/
		    		    //if(!(cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA))
		    		    cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						String temp=Util.getUnicode(cellValue,"gb2312");
						cell.setCellValue(temp);
		    		   // // System.out.println("Row==="+report.getRowId().intValue()+"==Col==="+report.getColId()+"===Value=="+cellValue);
					}
		    	}
		    
		    }
		    /**清单式*/
		  
		    if (collect.getFlag() == 2) 
		    {
		    	List billList=collect.getBill_dataList();
		    	if(billList!=null)
		    	{
			    	//设置起始行
			    	collect.setStartRow(3);
			    	int startRow=collect.getStartRow();
					
			    	//// System.out.println("行数==== "+billList.size());
			    	int rowNum = billList.size();
			    	/**excel最大行数*/
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
						
						//// System.out.println("列数==== "+content.length);
						
						for(int j=0;j<content.length;j++)
						{
							//// System.out.println("dsfsdfsdfsdf＝＝＝"+content[j]);
							//// System.out.println("COl"+j);
							cell=row.createCell((short)j);
							
							
							if(cell==null)
							{
								//// System.out.println("Error********************");
								log =collect.getChildRepName()+"_"+collect.getVersionId()+"对应的单元格 " +
	    		    			"第"+report.getRowId()+"行 列有问题！";
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
