package com.cbrc.smis.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.struts.util.MessageResources;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ReportInInfoForm;

import excelToHTML.usermodel.ReportDefine;
/**
 * 	查询对比文件的生成
 * @author zyl_xh
 *
 */
public class StrutsContrastReportDelegate {
	
	public List getCheckList(Integer repInId,Integer compRepInId){
		List reportInInfoList = null;
		Connection con = null;
		ResultSet rs = null;		
		
		//获得比对结果reportInInfoList 
		try {
			con = new DBConn().openSession().connection();
			String hhql = "SELECT A.CELL_ID,A.CELL_NAME,A.COL_ID,A.ROW_ID,B.REPORT_VALUE VALUE1,C.REPORT_VALUE VALUE2 " +
					"FROM M_CELL A LEFT JOIN REPORT_IN_INFO B ON A.CELL_ID=B.CELL_ID LEFT JOIN REPORT_IN_INFO C ON A.CELL_ID=C.CELL_ID"
					+ " WHERE B.REP_IN_ID=" + repInId + " and c.rep_in_id=" + compRepInId;
			rs = con.createStatement().executeQuery(hhql);
		
			if (rs != null) {
				reportInInfoList = new ArrayList();
				boolean isNumber=true;
				String value1=null;
				String dValue1=null;
				String value2=null;
				String dValue2=null;
				ReportInInfoForm reportInInfoForm=null;
				while (rs.next()) {
					isNumber=true;
					reportInInfoForm = new ReportInInfoForm();
					reportInInfoForm.setCellId(new Integer(rs.getInt("CELL_ID")));
					reportInInfoForm.setCellName(rs.getString("CELL_NAME"));
					reportInInfoForm.setColId(rs.getString("COL_ID"));
					reportInInfoForm.setRowId(new Integer(rs.getInt("ROW_ID")));
					value1=rs.getString("VALUE1");
					value2=rs.getString("VALUE2");
					
					//如果单元格的值不为空，判断值是否相等。
					//如果单元格的值都为double型，直接进行数值的比较;如果单元格的值有一个不为数值，则进行字面意义的比较
					if(value1!=null&&!value1.trim().equals("")
							&&value2!=null&&!value2.trim().equals("")){
						try {
							dValue1=new java.text.DecimalFormat("#0.00").format(Double.parseDouble(value1));
							dValue2=new java.text.DecimalFormat("#0.00").format(Double.parseDouble(value2));
						} catch (NumberFormatException e) {
							isNumber=false;
						}
						if(isNumber==true){
							if(dValue1.equals(dValue2)){
								reportInInfoForm.setReportValue(value1);
							}else{
								reportInInfoForm.setReportValue("<font color=red>"+value1+"("+value2+")"+"</font>");
							}
						}else{
							if(value1.equals(value2)){
								reportInInfoForm.setReportValue(value1);
							}else{
								reportInInfoForm.setReportValue("<font color=red>"+value1+"("+value2+")"+"</font>");
							}
						}
					}else{
						if(value1==null||value1.trim().equals("")){
							value1="空值";
						}
						if(value2==null||value2.trim().equals("")){
							value2="空值";
						}
						reportInInfoForm.setReportValue("<font color=red>"+value1+"("+value2+")"+"</font>");
					}
					
					reportInInfoList.add(reportInInfoForm);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return reportInInfoList;
	}
	
	/**
	 * 	对比方法
	 *  id 对比文件id
	 *  checkId 汇总文件id
	 *  fileName 保存文件名
	 *  path 路径
	 */
	public ReportDefine  check  (Integer id,Integer checkID,String fileName,String path){
		
		
		//不烦了，自己写List 曹发根
	
		List reportInInfoList = new ArrayList();
		Connection con = null;
		ResultSet rs = null;		
		
		//获得比对结果reportInInfoList 
		try {
			ReportInForm reportInForm = StrutsReportInInfoDelegate.getReportToCollect(id);
			con = new DBConn().openSession().connection();
			String hhql = "SELECT A.CELL_ID,A.CELL_NAME,A.COL_ID,A.ROW_ID,B.REPORT_VALUE VALUE1,C.REPORT_VALUE VALUE2 " +
					"FROM M_CELL A LEFT JOIN REPORT_IN_INFO B ON A.CELL_ID=B.CELL_ID LEFT JOIN REPORT_IN_INFO C ON A.CELL_ID=C.CELL_ID"
					+ " WHERE B.REP_IN_ID="+id+" and c.rep_in_id="+reportInForm.getRepInId();
			
			rs = con.createStatement().executeQuery(hhql.toUpperCase());
		
			if (rs != null) {
				while (rs.next()) {
					ReportInInfoForm reportInInfoForm = new ReportInInfoForm();
					reportInInfoForm.setCellId(new Integer(rs.getInt("CELL_ID")));
					reportInInfoForm.setCellName(rs.getString("CELL_NAME"));
					reportInInfoForm.setColId(rs.getString("COL_ID"));
					reportInInfoForm.setRowId(new Integer(rs.getInt("ROW_ID")));
					String value1=rs.getString("VALUE1");
					String value2=rs.getString("VALUE2");
					if(value1!=null&&value2!=null){
						if(value1.endsWith(".0")){
							value1=value1+0;
						}
						if(value2.endsWith(".0")){
							value2=value1+0;
						}
						
						
						if(value1.equals(value2)){
							reportInInfoForm.setReportValue(value1);
						}else{
							reportInInfoForm.setReportValue("<font color=red>"+value1+"("+value2+")"+"</font>");
						}
					}else if(value2!=null){
						if(value2.equals("0.00")){
							reportInInfoForm.setReportValue("0.00");
						}else{
							reportInInfoForm.setReportValue("<font color=red>"+"0.00("+value2+")"+"</font>");
						}
					}else if(value1!=null){
						if(value1.equals("0.00")){
							reportInInfoForm.setReportValue("0.00");
						}else{
							reportInInfoForm.setReportValue("<font color=red>"+value1+"(0.00)"+"</font>");
						}
						
					}
					reportInInfoList.add(reportInInfoForm);

				}
			}
		} catch (Exception e) {

		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
		}
		
		
		//将比对结果写入EXCEL
		ReportInForm report=StrutsReportInDelegate.getReportIn(id);
		File excelFile=new File(path+"/Reports/templates/"+report.getChildRepId()+"_"+report.getVersionId()+".xls");
		File reportFile=new File(path+fileName);
		FileInputStream fileInput=null;
		FileOutputStream reportOutput=null;
		try{
				fileInput=new FileInputStream(excelFile);
				reportOutput=new FileOutputStream(reportFile);
		
				byte [] b=new byte[1024];
				int temp=0;
				while((temp=fileInput.read(b,0,1024))>0){
					reportOutput.write(b,0,temp);
			
				}
				fileInput.close();
				reportOutput.close();
		
		}catch(Exception e){
			
		}
		try{
			ReportDefine rd=new ReportDefine(path+fileName);
			List resultList=rd.getCellSet();
			int moveDate=this.getOffsetRows(report.getChildRepId(),report.getVersionId());			
			for(int i=0;i<reportInInfoList.size();i++){
				ReportInInfoForm infoForm=(ReportInInfoForm)reportInInfoList.get(i);
				int row=infoForm.getRowId().intValue()+moveDate;
				int col=StringToInt(infoForm.getColId());
				resultList=loop(resultList,col,row,infoForm.getReportValue());
			}
			
			return rd;
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
		// 添加结束
		
		/**
		
		
		
		ReportInForm report=StrutsReportInDelegate.getReportIn(id);
		
		File file=new File(path+"/Reports/templates/"+report.getChildRepId()+"_"+report.getVersionId()+".xls");
		// System.out.println(file.getName());
		File reportFile=new File(path+fileName);
		FileInputStream fileInput=null;
		FileOutputStream reportOutput=null;
		try{
				fileInput=new FileInputStream(file);
				reportOutput=new FileOutputStream(reportFile);
		
				byte [] b=new byte[1024];
				int temp=0;
				while((temp=fileInput.read(b,0,1024))>0){
					reportOutput.write(b,0,temp);
			
				}
				fileInput.close();
				reportOutput.close();
				
			// 得到reportInInfo信息，对比后写如List
			 	
			List list=StrutsReportInInfoDelegate.getAllReportInInfo(id);
			if(list == null ){
				return null;
			}
			// 得到对应汇总的报表
			ReportInForm reportInForm = StrutsReportInInfoDelegate.getReportToCollect(id);
			if(reportInForm == null ){
				return null;
			}
			List reportList=StrutsReportInInfoDelegate.getAllReportInInfo(reportInForm.getRepInId());
			//	StrutsReportInInfoDelegate.getAllReportInInfo(checkID);
//			List mCell=StrutsMCellDelegate.getAllCell(reportID.getId().getChildRepId(),reportID.getId().getVersionId());
			
			if(reportList == null ){
				return null;
			}
			int moveDate=this.getOffsetRows(reportInForm.getChildRepId(),reportInForm.getVersionId());
			
			
			ReportDefine rd=new ReportDefine(path+fileName);
			List resultList=rd.getCellSet();

			// 循环对比各个单元格内容
			
			for(int i=0;i<list.size();i++){
				ReportInInfoForm infoForm=(ReportInInfoForm)list.get(i);

				String taget="";
				
				for(int j=0;j<reportList.size();j++){
					ReportInInfoForm checkForm=(ReportInInfoForm)reportList.get(j);
		
					if(infoForm.getCellId().equals(checkForm.getCellId())){
	
						taget=checkForm.getReportValue();					
						
						MCell cell=StrutsMCellDelegate.getMCell(infoForm.getCellId());
						int col=StringToInt(cell.getColId())-moveDate;
						int row=cell.getRowId().intValue()-moveDate;
						
						
						if(infoForm.getReportValue()!=null&&(!infoForm.getReportValue().equals(taget))){
							String content="<font color=red>"+infoForm.getReportValue()+"("+taget+")</font>";
				//			// System.out.println(infoForm.getReportValue()+"???"+taget);
							resultList=loop(resultList,col,row,content);
				
						}
						else{
							if(infoForm.getReportValue()==null&&taget!=null){
								String content="<font color=red>"+"("+taget+")</font>";
								resultList=loop(resultList,col,row,content);
							}
							else
								resultList=loop(resultList,col,row,infoForm.getReportValue());
						}
						break;
					}
				}
			}
	
			return rd;
				
			
		}
		catch(Exception e){
			e.printStackTrace();
		}	
		finally{
				if(fileInput!=null)
					try {
						fileInput.close();
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
		
		**/
		return null;
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
	/**
	 * Excel模板对应PDF模板的起始行的偏移量
	 */
	private String REPORTFILE="com/cbrc/smis/excel/OffsetResources";
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
	Locale LOCALE=Locale.CHINA;
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
	
	
	
}
