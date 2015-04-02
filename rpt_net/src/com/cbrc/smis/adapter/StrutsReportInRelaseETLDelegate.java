package com.cbrc.smis.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.hibernate.MCell;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.Boolean;
import jxl.write.biff.RowsExceededException;
import jxl.write.WritableCellFormat;

public class StrutsReportInRelaseETLDelegate {
	public String Analyse(Integer id,String fileName,String path){
		Workbook wb=null;
		FileInputStream input=null;
		FileOutputStream reportOutput=null;
	//	String fileName=null;
		boolean exception=false;
		try{
			ReportInForm report=StrutsReportInDelegate.getReportIn(id);
			
			File file=new File(path+"/Reports/templates/"+report.getChildRepId()+"_"+report.getVersionId()+".xls");
		//	File reportFile=new File("/Reports/temp/"+sessionID+"temp"+".xls");
		//	fileName=sessionID;
			File reportFile=new File(fileName);
			FileInputStream fileInput=null;
			
		//	FileOutputStream ETLOutput=null;
			
			
			try{
				fileInput=new FileInputStream(file);
				reportOutput=new FileOutputStream(reportFile);
		//		ETLOutput=new FileOutputStream(reportFile);
				byte [] b=new byte[1024];
				int temp=0;
				while((temp=fileInput.read(b,0,1024))>0){
					reportOutput.write(b,0,temp);
			//		reportOutput.flush();
		//			ETLOutput.write(b,0,temp);
				}
				fileInput.close();
				reportOutput.close();
				
		//		ETLOutput.close();
			}
			catch(Exception e){
				e.printStackTrace();
				if(fileInput!=null)
					fileInput.close();
				if(reportOutput!=null)
					reportOutput.close();
		//		if(ETLOutput!=null)
		//			ETLOutput.close();
			} 
			
	//		input=new FileInputStream(file);
			
//			wb=Workbook.getWorkbook(input);
			
			wb=Workbook.getWorkbook(reportFile);
			
			
			
			Sheet st = wb.getSheet(0);
			// System.out.println(reportFile.length()+"!!!!!!!!");
			reportOutput=new FileOutputStream(reportFile);
	//		WritableWorkbook wwb = Workbook.createWorkbook(reportOutput);
			WritableWorkbook wwb = Workbook.createWorkbook(new File(fileName),wb);
			WritableSheet ws=wwb.getSheet(0);
			
			
			
	//		ws=copySheet(st,ws);
			
			
			// System.out.println(reportFile.length()+"!!!!!!!!");
//			// System.out.println(ws+"~~~~~~~");
			List list=StrutsReportInInfoDelegate.getAllReportInInfo(id);
			
			
			
			
			
			for(int i=0;i<list.size();i++){
				ReportInInfoForm infoForm=(ReportInInfoForm)list.get(i);
				MCell cell=StrutsMCellDelegate.getMCell(infoForm.getCellId());
				int col=StringToInt(cell.getColId());
				int row=cell.getRowId().intValue();
				Cell taget=st.getCell(col,row);
				if(!infoForm.getReportValue().equals(taget.getContents())){
					Cell getCell=st.getCell(col,row);
					WritableCell wc =ws.getWritableCell(col,row);
					WritableCellFeatures feat=new WritableCellFeatures();
					feat.setComment(wc.getContents());
				//	wc.setCellFeatures(feat);
					Label label=null;
					WritableFont wf = new WritableFont(WritableFont.TIMES,18,WritableFont.BOLD,true);
					WritableCellFormat wcf = new WritableCellFormat(wf);
					if(wc.getCellFormat()!=null){
						label=new Label(col-1,row-1,infoForm.getReportValue(),wcf);
					}
					else{
						label=new Label(col-1,row-1,infoForm.getReportValue());
					}
					label.setCellFeatures(feat);
	//				// System.out.println(infoForm.getReportValue()+"~~~");
	//				// System.out.println("col=="+col+",,,row=="+row);
	//				// System.out.println(wc.getCellFormat());
					ws.addCell(label);
				}
				else{
					/*
					Cell getCell=st.getCell(col,row);
					Label label = new Label(col,row,getCell.getContents(),getCell.getCellFormat());
					
			//		WritableCellFeatures feat=new WritableCellFeatures();
					// System.out.println(label.getContents());
					ws.addCell(label);
					*/
				}
			}
	
			wwb.write();
			wwb.close();
			// System.out.println(reportFile.length()+"___________");
			wb.close();
//			input.close();
			
			// System.out.println(reportFile.getPath());
		
		}
		catch(Exception e){
			e.printStackTrace();
			fileName=null;
			exception=true;
		}	
		finally{
			if(exception==true){
				if(wb!=null)
					wb.close();
				if(input!=null)
					try {
						input.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       if(reportOutput!=null){
		    	   try {
					reportOutput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		       }
			}
		}
		return fileName;
	}
	/**
	 * 	通过string得到想对应的int数值
	 * @param col
	 * @return
	 */
	public int StringToInt(String col){
		int colInt=0;
		for(int x=0;x<col.getBytes().length;x++){
			int b=(int)col.getBytes()[x]-64;
			colInt=colInt+b;
			
		}
		return colInt;
		
	}
	public WritableSheet copySheet(Sheet sheet,WritableSheet ws) throws RowsExceededException, WriteException{
		int col=sheet.getColumns();
		for(int i=0;i<col;i++){
			Cell[] cell=sheet.getColumn(i);
			for(int j=0;j<cell.length;j++){
				Cell getCell=cell[j];
				Label label =null;
				if(getCell.getCellFormat()!=null){
					// System.out.println("has format!");
					label= new Label(i,j,getCell.getContents(),getCell.getCellFormat());
				}
				else{
					// System.out.println("no format!");
					label=new Label(i,j,getCell.getContents());
				}
		//		WritableCellFeatures feat=new WritableCellFeatures();
		//		// System.out.println(label.getContents());
				ws.addCell(label);
			}
		}
		return ws;
	}
	public static void main(String [] argc){
		StrutsReportInRelaseETLDelegate st=new StrutsReportInRelaseETLDelegate();
		st.Analyse(new Integer(70966),"test","");
	}
}
