package com.fitech.gznx.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.proc.util.EngineException;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.CellInfo;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.common.TemplateArea;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IByteMap;
import com.runqian.report4.usermodel.INormalCell;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;
public class AFReportDealDelegate {
	private static FitechException log = new FitechException(AFReportDealDelegate.class);
	
	/***
	 * 226行 两个oracle序列的问题未解决 卞以刚 2011-12-27
	 * 
	 * 195行 将oracle的序列去除，修改为sqlserver的默认自增
	 * 209行 将oracle的序列去除，修改为sqlserver的默认自增
	 * 320行 修改数据类型为sqlserver数据类型
	 * 324行 修改数据类型为sqlserver数据类型
	 * 350行 将oracle的序列去除，修改为sqlserver的默认自增
	 * 影响表：AF_Template AF_CellInfo  m_cell  AF_GatherFormula qd_cellinfo
	 * @throws IOException 
	 */
	public static String resolveReport(InputStream filePath,AFTemplateForm templateForm,String reportFlg) throws IOException,EngineException{		log.println("====================开始进入resolveReport方法！！！");
		StringBuffer addsql= null;
		
		if(filePath==null || filePath.available()==0){
			log.println("====================filePath==null或filePath.available()==0");
			return null;
		}else{
			DBConn conn=null;
	    	Session session=null;
	    	Connection connection=null;
	    	Statement stmt = null;
	    	boolean result=false;
	    	
			try {
				conn = new DBConn();
				
				Long currCellInfoVal=new AFReportDealDelegate().currVal("AF_CELLINFO", conn, "CELL_ID");
				Long currMCellVal=new AFReportDealDelegate().currVal("M_CELL", conn, "CELL_ID");
				Long currGatherFormulaVal=new AFReportDealDelegate().currVal("AF_GATHERFORMULA", conn, "FORMULA_ID");
				
		    	session=conn.beginTransaction();
		    	connection=session.connection();
		    	
		    	
		    	
		    	stmt = connection.createStatement();
		    	addsql = new StringBuffer();
		    	// 定义模板路径
		    	String raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "Raq"+ 
				File.separator+templateForm.getTemplateId() + "_" + templateForm.getVersionId() + ".raq";
		    	//String initRqlFileName=Config.RAQ_INIT_TEMP_PATH+templateForm.getTemplateId()+"_"+templateForm.getVersionId()+".raq";
		    	
		    	File testFile = new File(raqFileName);
		    	log.println("==============" + Config.RAQ_TEMPLATE_PATH);
		    	log.println(raqFileName);
		    	if(!testFile.exists())
		    		log.println("=======================file not exists！");
		    	else
		    		log.println("=======================file exists！");
		    	templateForm.setFilePath(raqFileName);
		    	if(templateForm.getStartDate() == null){
		    		templateForm.setStartDate("");
		    	}
		    	if(templateForm.getEndDate() == null){
		    		templateForm.setEndDate("");
		    	}
		    	if(templateForm.getSupplementFlag() == null){
		    		templateForm.setSupplementFlag("");
		    	}
		    	// 没有选中即是点对点报表
		    	if(StringUtil.isEmpty(templateForm.getReportStyle())){
		    		templateForm.setReportStyle(com.fitech.gznx.common.Config.REPORT_DD);
		    	}

		    	// 定义报表信息
		    	addsql.append("insert into AF_Template ( Template_ID ,Version_ID  ," +
		    			"Template_Name, File_Path,Using_Flag,Template_Type ,is_report,bak1,is_leader,report_style,priority_flag,supplement_flag,start_date,end_date,join_template_id) values('").
		    			append(templateForm.getTemplateId()).append("','").append(templateForm.getVersionId())
		    			.append("','").append(templateForm.getTemplateName())
		    			.append("','").append(templateForm.getFilePath()).append("',0,'").
		    			append(reportFlg).append("',").append(templateForm.getIsReport()).append(",'").
		    			append(templateForm.getBak1()).append("',").append(templateForm.getIsLeader())
		    			.append(",").append(templateForm.getReportStyle()).append(",").append(templateForm.getPriorityFlag()).append(",").append(templateForm.getSupplementFlag()).append(",'").append(templateForm.getStartDate())
		    			.append("','").append(templateForm.getEndDate()).append("','").append(templateForm.getJoinTemplateId()).append("')");
		    	stmt.addBatch(addsql.toString());
				ReportDefine rd = (ReportDefine)ReportUtils.read(filePath);
				System.out.println("================read success============");
				// 定义点对点报表单元格信息
				if(templateForm.getReportStyle() == null || !templateForm.getReportStyle().equals(com.fitech.gznx.common.Config.REPORT_QD)){
					
					// 需要解析的单元格颜色
					final int[] PARSE_CELL_COLOR =
					{ Config.PITEM_CELL_BGCOLOR, Config.PCOL_CELL_BGCOLOR,Config.PID_CELL_BGCOLOR};
					// 解析单元格
					Map parseCellMap  = parseRaqColorCell(rd, PARSE_CELL_COLOR);
					System.out.println("==============read cell success=============");
					
					List pItemList = (List) parseCellMap.get(String.valueOf(Config.PITEM_CELL_BGCOLOR));
					List pColList = (List) parseCellMap.get(String.valueOf(Config.PCOL_CELL_BGCOLOR));
					List pIDlList = (List) parseCellMap.get(String.valueOf(Config.PID_CELL_BGCOLOR));
					if(pIDlList == null){
						pIDlList = new ArrayList();
					}
					if(pItemList == null){
						pItemList = new ArrayList();
					}
					if(pColList == null){
						pColList = new ArrayList();
					}
					List templateAreaList = createTemplateArea(pItemList,pColList,pIDlList);
					for(int num=0;num<templateAreaList.size();num++){
					TemplateArea area = (TemplateArea)templateAreaList.get(num);
					List aItemList = area.getItemList();
					List aColList = area.getColList();
					List aPidList = area.getPidList();
					
					List itemList = makeRowList(aItemList);
					
					List colList = makeColList(aColList);
					//aPidList = makeRowList(aPidList);
					Map cellmap = makeListtoMap(aPidList);
					Map itenMap = makeListtoMap(aItemList);
	
					// 取得行信息
					int nSize =0;
					if(itemList.size() >= aPidList.size()){
						nSize = itemList.size();
					} else{
						nSize = aPidList.size();
					}
					for(int i=0;i<nSize;i++){
						CellInfo cell=null;
						String templateName =null;
						if(itemList.size() >= aPidList.size()){
							cell= (CellInfo)itemList.get(i);
							templateName = cell.getValue();						
						}else{
							cell= (CellInfo)aPidList.get(i);
							if(itenMap.get(String.valueOf(cell.getRow()))!= null){
								templateName = (String) itenMap.get(String.valueOf(cell.getRow()));
							}else{
								templateName = "";
							}						
						}
						if(!StringUtil.isEmpty(templateName)){
							templateName = templateName.trim();
						}
						int row = cell.getRow();
						//int nextCellInfoVal=new AFReportDealDelegate().nextVal("AF_CellInfo", connection, "cell_id");
						
						
						for(int j=0;j<colList.size();j++){
							CellInfo cellcol = (CellInfo)colList.get(j);
							if(cell.getRow()<=cellcol.getRow()){
								continue;
							}
							if(cell.getCol()>=cellcol.getCol()){
								continue;
							}
							String colTemplateNmae = "";
							if(!StringUtil.isEmpty(cellcol.getValue())){
								colTemplateNmae = cellcol.getValue().trim();
							}
							int col = cellcol.getCol();
							String colname= convertNumToCol(col-1);
							
							INormalCell cs = rd.getCell(row, (short) col);
							IByteMap map = cs.getExpMap(false);
							//2014-01-21:LuYueFei:灰色单元格不需要入库
							int backColor = cs.getBackColor();
							if(backColor == Config.DGRAY_BGCOLOR || backColor == Config.GRAY_BGCOLOR || backColor == Config.LGRAY_BGCOLOR){
								continue;
							}
							
							String cellPid = null;
							if( cellmap.get(String.valueOf(row))!= null){
								cellPid = (String) cellmap.get(String.valueOf(row));
							}else{
								cellPid="";
							}
							if(cellPid!=null)
								cellPid = cellPid.trim();
							// 保存单元格信息
							addsql = new StringBuffer();
							/**此处将oracle的序列去除，修改为sqlserver的默认自增
							 * 已增加数据库判断**/

							if(Config.DB_SERVER_TYPE.equals("oracle"))
							{
								addsql.append("insert into AF_CellInfo (cell_id ,Template_ID,Version_ID ,row_name," +
										"col_name ,row_num,col_num,cell_name ,cell_pid,collect_type,data_type ) values (SEQ_AF_CELLINFO.NEXTVAL,'").
										append(templateForm.getTemplateId()).append("','").append(templateForm.getVersionId())
						    			.append("','").append(templateName).append("','").append(colTemplateNmae).append("','").append(row).append("','")
						    			.append(colname).append("','").append(colname+row).append("','").append(cellPid).append("',null,null)");
							}
							else if(Config.DB_SERVER_TYPE.equals("sqlserver"))
							{
								addsql.append("insert into AF_CellInfo (cell_id,Template_ID,Version_ID ,row_name," +
									"col_name ,row_num,col_num,cell_name ,cell_pid,collect_type,data_type ) values ("+(++currCellInfoVal)+",'").
					    			append(templateForm.getTemplateId()).append("','").append(templateForm.getVersionId())
					    			.append("','").append(templateName).append("','").append(colTemplateNmae).append("','").append(row).append("','")
					    			.append(colname).append("','").append(colname+row).append("','").append(cellPid).append("',null,null)");
							}
							
							stmt.addBatch(addsql.toString());
							// 保存1104报表单元格信息
							if(reportFlg.equals("1")){
								addsql = new StringBuffer();
								/**此处将oracle的序列去除，修改为sqlserver的默认自增
								 * 已增加数据库判断**/
								if(Config.DB_SERVER_TYPE.equals("oracle"))
								{
									addsql.append("insert into m_cell (cell_id ,CHILD_REP_ID,VERSION_ID ,ROW_ID,COL_ID,CELL_NAME ) values (SEQ_M_CELL.NEXTVAL,'").
									append(templateForm.getTemplateId()).append("','").append(templateForm.getVersionId())
					    			.append("','").append(row).append("','")
					    			.append(colname).append("','").append(colname+row).append("')");
								}
								else if(Config.DB_SERVER_TYPE.equals("sqlserver"))
								{
									addsql.append("insert into m_cell (cell_id,CHILD_REP_ID,VERSION_ID ,ROW_ID,COL_ID,CELL_NAME ) values ("+(++currMCellVal)+",'").
					    			append(templateForm.getTemplateId()).append("','").append(templateForm.getVersionId())
					    			.append("','").append(row).append("','")
					    			.append(colname).append("','").append(colname+row).append("')");
								}
								
								stmt.addBatch(addsql.toString());
							}
							// 保存取数公式
							try{
								if(map != null && !map.isEmpty()){
								if(map.getValue(0) != null){
									addsql = new StringBuffer();
									String formulaValue = (String) map.getValue(0);
									formulaValue = formulaValue.replaceAll("'", "''");
									/**此处将oracle的序列去除，修改为sqlserver的默认自增**/
									if(Config.DB_SERVER_TYPE.equals("oracle"))
									{
										addsql.append("insert into AF_GatherFormula (formula_id ,Template_ID,Version_ID,cell_id,formula_value," +
												"formula_name) values (SEQ_AF_GATHERFORMULA.NEXTVAL,'").append(templateForm.getTemplateId()).
												append("','").append(templateForm.getVersionId()).
												append("',SEQ_AF_CELLINFO.Currval,'").append(formulaValue).append("',null)");
									}
									else if(Config.DB_SERVER_TYPE.equals("sqlserver"))
									{
										addsql.append("insert into AF_GatherFormula (formula_id ,Template_ID,Version_ID,cell_id,formula_value," +
										"formula_name) values ("+(++currGatherFormulaVal)+",'").append(templateForm.getTemplateId()).
										append("','").append(templateForm.getVersionId()).
										append("',"+(currCellInfoVal)+",'").append(formulaValue).append("',null)");
									}
									stmt.addBatch(addsql.toString());
								}
							}
							}catch(Exception e){
								e.printStackTrace();
								AFReportDealDelegate.clearJYHData(templateForm);
							}						
						}
					}
					}
					System.out.println("============exec success===============");
					//ReportUtils.write(initRqlFileName,rd);  //将原始模板保存到文件
					resetbackcolor(rd,pItemList);
					resetbackcolor(rd,pColList);
					resetbackcolor(rd,pIDlList);
				} else {
					// 定义清单式报表单元格信息
					if(templateForm.getQdreportFile() != null && templateForm.getQdreportFile().getFileSize()>0 ){
						ReportDefine qdrd = (ReportDefine)ReportUtils.read(templateForm.getQdreportFile().getInputStream());
						// 定义模板路径
				    	String raqqdFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "printRaq"+ 
						File.separator+"qdfile"+ 
						File.separator+templateForm.getTemplateId() + "_" + templateForm.getVersionId() + ".raq";
				    	ReportUtils.write(raqqdFileName,qdrd);
				    	// 更新共享文件夹
					}
					
					// 需要解析的单元格颜色
					final int[] PARSE_CELL_COLOR =
					{ Config.PITEM_CELL_BGCOLOR, Config.PCOL_CELL_BGCOLOR};
					List<CellInfo> pItemList =null ;
					List<CellInfo> pColList =null;
					CellInfo endFlgCell = null;
					try {
						// 解析单元格
						Map parseCellMap = parseRaqColorCell(rd, PARSE_CELL_COLOR);
						pItemList = (List) parseCellMap.get(String.valueOf(Config.PITEM_CELL_BGCOLOR));
						pColList = (List) parseCellMap.get(String.valueOf(Config.PCOL_CELL_BGCOLOR));
						
						
						if(pItemList == null){
							pItemList = new ArrayList();
						}
						if(pColList == null){
							pColList = new ArrayList();
						}
						endFlgCell = pColList.get(0);
					} catch (Exception e1) {
						e1.printStackTrace();
						AFReportDealDelegate.clearJYHData(templateForm);
						throw new EngineException("单元格解析异常，请检查报表数据或涂色");
					}
					// 结束标志名称
					String endFlgName = endFlgCell.getValue();
					int endFlgcol = endFlgCell.getCol();
					// 列数
					int coln = 0;
					// 起始列
					int startCol = 0;
					// 结束列
					int endCol = 0;
					// 开始行
					int startRow = 0;
					for(CellInfo qdCell:pItemList){
						if(startRow == 0)
							startRow = qdCell.getRow();
						if(startCol>qdCell.getCol() || startCol==0){
							startCol = qdCell.getCol();
						}
						if(endCol<qdCell.getCol() || endCol==0){
							endCol = qdCell.getCol();
						}
					}
					coln = endCol- startCol;
					// 保存清单式报表单元格信息
					addsql = new StringBuffer();
					addsql.append("insert into qd_cellinfo (Template_ID,Version_ID ,COL_COUNT," +
							"START_COL ,END_COL,FLAG_COL,END_FLAG ,START_ROW) values ('").
	    			append(templateForm.getTemplateId()).append("','").append(templateForm.getVersionId()).
	    			append("',").append(coln).append(",'").append(convertNumToCol(startCol-1)).
	    			append("','").append(convertNumToCol(endCol-1)).append("','").append(convertNumToCol(endFlgcol-1)).
	    			append("','").append(endFlgName).append("',").append(startRow).append(")");
					
					stmt.addBatch(addsql.toString());
					/**此处将创建表结构的字段类型修改为sqlserver数据类型*/
					if(!reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
						// 创建表结构
						String tablename = "AF_QD_"+templateForm.getTemplateId().toUpperCase().trim();
						addsql = new StringBuffer();					
						addsql.append("CREATE TABLE ").append(tablename).append(" (Rep_ID INTEGER NOT NULL," +
								"ROW_ID INTEGER NOT NULL,");
						for(int i=startCol;i<endCol+1;i++){
							/**此处将创建表结构的字段类型修改为sqlserver数据类型
							 * 已增加数据库判断*/
							if(Config.DB_SERVER_TYPE.equals("oracle"))
								addsql.append("COL").append(i).append(" VARCHAR2(200) NULL,");
							if(Config.DB_SERVER_TYPE.equals("sqlserver"))
								addsql.append("COL").append(i).append(" VARCHAR(200) NULL,");
						}
						/**此处将创建表结构的字段类型修改为sqlserver数据类型*/
						if(Config.DB_SERVER_TYPE.equals("oracle"))
							addsql.append(" bak1 VARCHAR2(10) NULL,bak2 VARCHAR2(200) NULL)");
						if(Config.DB_SERVER_TYPE.equals("sqlserver"))
							addsql.append(" bak1 VARCHAR(10) NULL,bak2 VARCHAR(200) NULL)");
						stmt.addBatch(addsql.toString().toUpperCase());
						addsql = new StringBuffer();
						addsql.append("ALTER TABLE ").append(tablename).append(" ADD PRIMARY KEY (Rep_ID, ROW_ID)");					
						stmt.addBatch(addsql.toString());
					}
					

					// 取得取数公式
					int rownum = rd.getRowCount();
					int colnum = rd.getColCount();
					for(int i=1;i<=rownum;i++){
						for(int j=1;j<=colnum;j++){
							INormalCell cs = rd.getCell(i, (short) j);
							IByteMap map = cs.getExpMap(false);

							try{
								if(map != null && !map.isEmpty()){
								if(map.getValue(0) != null){
									addsql = new StringBuffer();
									String formulaValue = (String) map.getValue(0);
									formulaValue = formulaValue.replaceAll("'", "''");
									/**此处将oracle的序列去除，修改为sqlserver的默认自增
									 * 已增加数据库判断*/
									if(Config.DB_SERVER_TYPE.equals("oracle"))
									{
										addsql.append("insert into AF_GatherFormula (formula_id ,Template_ID,Version_ID,cell_id,formula_value," +
												"formula_name) values (SEQ_AF_GATHERFORMULA.NEXTVAL,'").append(templateForm.getTemplateId()).
												append("','").append(templateForm.getVersionId()).
												append("',SEQ_AF_CELLINFO.NEXTVAL,'").append(formulaValue).append("',null)");
									}
									if(Config.DB_SERVER_TYPE.equals("sqlserver"))
									{
										addsql.append("insert into AF_GatherFormula (formula_id ,Template_ID,Version_ID,cell_id,formula_value," +
										"formula_name) values ("+(++currGatherFormulaVal)+",'").append(templateForm.getTemplateId()).
										append("','").append(templateForm.getVersionId()).
										append("',"+(++currCellInfoVal)+",'").append(formulaValue).append("',null)");
									}
									stmt.addBatch(addsql.toString());
								}
							}
							}catch(Exception e){
								e.printStackTrace();
								AFReportDealDelegate.clearJYHData(templateForm);
							
						}
					}
					//ReportUtils.write(initRqlFileName,rd);  //将原始模板保存到文件
					resetbackcolor(rd,pItemList);
					resetbackcolor(rd,pColList);				
				}
				}
				String[] currList = templateForm.getCurrList();
				if(currList != null && currList.length>0){
					for(String curr:currList){
						addsql = new StringBuffer();
						addsql.append("insert into af_template_curr_relation (Template_ID,Version_ID,cur_id) values ('")
						.append(templateForm.getTemplateId()).append("','").append(templateForm.getVersionId())
						.append("',").append(curr).append(")");
						stmt.addBatch(addsql.toString());
					}
				} else {
					addsql = new StringBuffer();
					addsql.append("insert into af_template_curr_relation (Template_ID,Version_ID,cur_id) values ('")
					.append(templateForm.getTemplateId()).append("','").append(templateForm.getVersionId())
					.append("',").append(1).append(")");
					stmt.addBatch(addsql.toString());
				}
				stmt.executeBatch();
				System.out.println("=============raqFileName:"+raqFileName+"============");
				
				ReportUtils.write(raqFileName,rd);  //将ReportDefine保存到文件
				System.out.println("===============finally success=============");
				// 共享文件夹
				result = true;
			}catch(EngineException e){
				e.printStackTrace();
				throw e;
			}catch(Exception e){
				e.printStackTrace();
				AFReportDealDelegate.clearJYHData(templateForm);
			}
			catch (Throwable e1) {
				result = false;
				e1.printStackTrace();
				AFReportDealDelegate.clearJYHData(templateForm);
				return null;
			}
			finally{
	    		try {
					if(conn != null) 
						conn.endTransaction(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
		}
		return templateForm.getFilePath();
	}
	/**
	 * 创建分区
	 * @param itemList 行区域
	 * @param colList 列区域
	 * @param pidList 
	 * @return 区
	 */
	private static List createTemplateArea(List itemList, List colList,
			List pidList) {
		if(pidList == null){
			pidList = new ArrayList();
		}
		if(itemList == null){
			itemList = new ArrayList();
		}
		if(colList == null){
			colList = new ArrayList();
		}
		List templateAreaList = new ArrayList();
		ArrayList rowCount = new ArrayList();
		ArrayList colCount = new ArrayList();
		ArrayList pidCount = new ArrayList();
		int itemnum = 0;
		int colnum=0;
		int pidnum=0;
		for(int i=0;i<itemList.size();i++){
			CellInfo cell = (CellInfo)itemList.get(i);
			int col = cell.getCol();
			if(i==0){
				itemnum = col;
				int[] intcell = {cell.getRow(),cell.getCol()};
				rowCount.add(intcell);
			}else{
				if(col>itemnum){
					if ( col-itemnum > 1){
						int[] intcell = {cell.getRow(),cell.getCol()};
						rowCount.add(intcell);
					}
					itemnum = col;
				}
			}			
		}
		for(int i=0;i<colList.size();i++){
			CellInfo cell = (CellInfo)colList.get(i);
			int row = cell.getRow();
			if(i==0){
				colnum = row;
				int[] intcell = {cell.getRow(),cell.getCol()};
				colCount.add(intcell);
			}else{
				if(row>colnum){					
					if ( row-colnum > 1){
						int[] intcell = {cell.getRow(),cell.getCol()};
						colCount.add(intcell);
					}
					colnum = row;
				}
			}			
		}
		for(int i=0;i<pidList.size();i++){
			CellInfo cell = (CellInfo)pidList.get(i);
			int col = cell.getCol();
			if(i==0){
				pidnum = col;
				int[] intcell = {cell.getRow(),cell.getCol()};
				pidCount.add(intcell);
			}else{
				if(col>pidnum){
					if ( col-pidnum > 1){
						int[] intcell = {cell.getRow(),cell.getCol()};
						pidCount.add(intcell);
					}
					pidnum = col;
				}				
			}
		}
		for(int i=0;i<rowCount.size();i++){
			int[] rowcell = (int[]) rowCount.get(i);
			int[] rowcell1 ={0,0};
			if(i+1<rowCount.size()){
				rowcell1 = (int[]) rowCount.get(i+1);
			}
			for(int j=0;j<colCount.size();j++){
				TemplateArea area = new TemplateArea();
				int[] colcell = (int[]) colCount.get(j);
				int[] colcell1 ={0,0};
				if(j+1<colCount.size()){
					 colcell1 = (int[]) colCount.get(j+1);
				}
				area.setStartCol(rowcell[1]);
				if(rowcell1[1] != 0){
					area.setEndCol(rowcell1[1]);
				}else{
					area.setEndCol(9999);
				}
				area.setStartRow(colcell[0]);
				if(colcell1[0] != 0){
					area.setEndRow(colcell1[0]);
				}else{
					area.setEndRow(9999);
				}
				for(int s=0;s<itemList.size();s++){
					CellInfo cell = (CellInfo)itemList.get(s);
					if(cell.getRow()>area.getEndRow() || cell.getRow()<area.getStartRow() 
							|| cell.getCol()>=area.getEndCol() || cell.getCol()<area.getStartCol()){
						continue;
					}
					area.addItemList(cell);
				}
				for(int s=0;s<colList.size();s++){
					CellInfo cell = (CellInfo)colList.get(s);
					if(cell.getRow()>=area.getEndRow() || cell.getRow()<area.getStartRow() 
							|| cell.getCol()>area.getEndCol() || cell.getCol()<area.getStartCol()){
						continue;
					}
					area.addColList(cell);
				}
				for(int s=0;s<pidList.size();s++){
					CellInfo cell = (CellInfo)pidList.get(s);
					if(cell.getRow()>area.getEndRow() || cell.getRow()<area.getStartRow() 
							|| cell.getCol()>area.getStartCol()|| area.getStartCol()-cell.getCol()>3){
						continue;
					}
					area.addPidList(cell);
				}
				templateAreaList.add(area);
			}
		}
		
		
		return templateAreaList;
	}

	private static List makeRowList(List itemList) {
		List ogrList  = new ArrayList();
		HashMap map = new HashMap();
		for(int i=0;i<itemList.size();i++){
			CellInfo cellA = (CellInfo) itemList.get(i);
			if(map.get(String.valueOf(cellA.getRow())) != null){
				continue;
			}

			String cellValue = getLastName(itemList,i);
			
			int col = cellA.getCol();
			for(int j=i+1;j<itemList.size();j++){
				CellInfo cellB = (CellInfo) itemList.get(j);				
				if(cellA.getRow()==cellB.getRow()){
					if(!cellA.getValue().equals(cellB.getValue())){
						cellValue = cellValue+"_"+getLastName(itemList,j);
					}
					col = cellB.getCol();
				}
			}
			CellInfo mapCell = new CellInfo();
			mapCell.setCol(col);
			mapCell.setRow(cellA.getRow());
			mapCell.setValue(cellValue);
			map.put(String.valueOf(cellA.getRow()), mapCell);
			ogrList.add(mapCell);
		}
		return ogrList;
	}

	public static void resetbackcolor(ReportDefine rd,List cellList){
		for(int i=0;i<cellList.size();i++){
			CellInfo cellC = (CellInfo) cellList.get(i);
			INormalCell cs = rd.getCell(cellC.getRow(), (short) cellC.getCol());
			cs.setBackColor(-1);
			rd.setCell(cellC.getRow(), (short) cellC.getCol(), cs);
		}
	}
	/**
	 * 生成excel
	 * @param filePath
	 * @return 文件路径
	 */
	public static String toExcel(String filePath) throws EngineException{
		String excelFileName = null;
		if(filePath==null || filePath.equals("")){
			return excelFileName;
		}else{
			try {

				ReportDefine rd = (ReportDefine)ReportUtils.read(filePath);	
				Context cxt = new Context();  //构建报表引擎计算环境				
				Engine engine = new Engine(rd, cxt);  //构造报表引擎
				try {
					engine.calc();  //运算报表
				} catch (Exception e) {
					log.printStackTrace(e);
					throw new EngineException("报表引擎运算报表出错，请检查报表数据");
				}
				int rownum = rd.getRowCount();
				int colnum = rd.getColCount();
				for(int i=1;i<=rownum;i++){
					for(int j=1;j<=colnum;j++){					
						INormalCell cs = rd.getCell(i, (short) j);				
						IByteMap map = cs.getExpMap(false);
						if(map != null && !map.isEmpty() && map.getValue(0) != null){
							cs.setExpMap(null);
							//Nick:2013-11-15--如果为报表日期单元格，则设定此单元格的特殊格式值
							String val = map.getValue(0).toString();
							if(val.indexOf("报表日期")>-1){
								cs.setValue("报表日期： ${report.year} 年${report.term} 月");
							}else if(val.indexOf("填报机构")>-1){
								cs.setValue("填报机构： ${report.orgName}");
							}
						}
					}
				}
				
				
				IReport iReport = rd;  //运算报表
				excelFileName = Config.WEBROOTPATH +"temp" + File.separator + "excel"+System.currentTimeMillis() + ".xls";
					
				ReportUtils.exportToExcel(excelFileName, iReport, false);
				

			} catch (EngineException e1) {
				e1.printStackTrace();
				log.printStackTrace(e1);
				throw e1;
			}catch (Throwable e2) {
				e2.printStackTrace();
				e2.getMessage();
			}
		
		}
		return excelFileName;
	}
	/**
	 * 无数据库交互
	 * 转换模板公式到excel文件
	 * @param excelpath
	 * @param rqpath
	 * @param templateId
	 * @return 时候成功
	 */
	public static boolean resoveExcelFormaule(String excelpath,String rqpath,String templateId)throws Exception{
		
		if(rqpath==null || rqpath.equals("")){
			return false;
		}else{
			HSSFWorkbook book=null;
			
			File file=null;
			FileInputStream excel=null;
			HSSFCell hssfCell=null;
			HSSFRow hssfRow=null;
			try {
				file=new File(excelpath);
				if(file.exists()==false) return false;
				excel=new FileInputStream(file);				
				book=new HSSFWorkbook(excel);
				book.setSheetName(0, templateId);
				HSSFSheet sheet=book.getSheetAt(0);
				ReportDefine rd = null;
				try {
					rd = (ReportDefine)ReportUtils.read(rqpath);
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new EngineException("报表引擎转换模板公式至excel文件时出错，请检查模板公式");
				}
				int rownum = rd.getRowCount();
				int colnum = rd.getColCount();
				for(int i=1;i<=rownum;i++){
					for(int j=1;j<=colnum;j++){					
						INormalCell cs = rd.getCell(i, (short) j);//润乾获取单元格			
						IByteMap map = cs.getExpMap(false);//
						if(map != null && !map.isEmpty()){
							//判断是否具有公式 和背景色是否为指定色
							if(map.getValue(0) != null && cs.getBackColor() == Config.FORMULA_BGCOLOR){
								
								String formulaValue = (String) map.getValue(0);//获取取数规则
								if(StringUtil.isEmpty(formulaValue)){
									continue;
								}
								hssfRow=sheet.getRow(i-1 );
								if(hssfRow==null){
									continue;
								}
								try{
									hssfCell=hssfRow.getCell((short)(j-1));
									if(hssfCell==null) continue;
									hssfCell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
									String valformul = checkFormul(formulaValue.trim());
									//处理表达式中IF 函数的 “==”excel中不能解析的问题 。
									valformul = valformul.replaceAll("==", "=");
									hssfCell.setCellFormula(valformul);//放入取数规则
								}catch(Exception e){
									System.out.println(formulaValue);
									throw e;
								}
							}
						}
						//Nick:2013-07-15--设定Excel中的格式，目前设定为数值保留两位小数"0.00"，百分比设定为四位小数"0.0000%"
						String format = cs.getFormat();//取润乾模板中的格式字符串
						if(format!=null && format.length()>0){
							hssfRow=sheet.getRow(i-1);
							if(hssfRow==null) continue;
							hssfCell=hssfRow.getCell((short)(j-1));
							if(hssfCell==null) continue;

							//Nick:2013-08-15--设定常量的数值的正常显示
							if(hssfCell.getCellType()==HSSFCell.CELL_TYPE_STRING 
									&& hssfCell.getStringCellValue()!=null && !hssfCell.getStringCellValue().equals("")){
								try {
									if(hssfCell.getStringCellValue().equals("0%")){//润乾报表导出存在值为0格式化为0%时导出为字符型0%，因此需要特殊处理
										hssfCell.setCellValue(0);
									}else if(com.fitech.gznx.util.POI2Util.isNumber(hssfCell.getStringCellValue())){
										Double d=Double.parseDouble(hssfCell.getStringCellValue());
										hssfCell.setCellValue(d);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							
							org.apache.poi.hssf.usermodel.HSSFCellStyle cellStyle = hssfCell.getCellStyle();
							org.apache.poi.hssf.usermodel.HSSFCellStyle myCellStyle = book.createCellStyle();
							org.apache.commons.beanutils.BeanUtils.copyProperties(myCellStyle, cellStyle);
							//Nick:2013-08-15--再次调整格式设定，百分比格式分为：保留4位小数(自动计算区域)/保留2位小数(常量)/保留整数的情况(常量：G4B01的权重列);数字格式分为：保留2位小数/保留整数(银行卡张数)
							if(format.indexOf("%")>-1){//设定百分比格式
								if(format.indexOf(".") == -1){//保留整数的情况(常量：G4B01的权重列)!!!目前强烈不建议使用整数格式，因为在线填报操作时，整数格式会解析有误
									myCellStyle.setDataFormat(org.apache.poi.hssf.usermodel.HSSFDataFormat.getBuiltinFormat("0%"));
								}else if(format.substring(format.indexOf(".")).length()==4){//".00%"--保留2位小数(常量)
									myCellStyle.setDataFormat(org.apache.poi.hssf.usermodel.HSSFDataFormat.getBuiltinFormat("0.00%"));
								}else{//保留4位小数(自动计算区域)--保留4位小数(自动计算区域)
									org.apache.poi.hssf.usermodel.HSSFDataFormat dataFormat= book.createDataFormat();
									myCellStyle.setDataFormat(dataFormat.getFormat("0.0000%"));
								}
							}else{//设定为数字格式
								if(format.indexOf(".") == -1){//保留整数(银行卡张数)
									myCellStyle.setDataFormat(org.apache.poi.hssf.usermodel.HSSFDataFormat.getBuiltinFormat("0"));
								}else{//保留2位小数(默认设定)
									myCellStyle.setDataFormat(org.apache.poi.hssf.usermodel.HSSFDataFormat.getBuiltinFormat("0.00"));
								}
							}
							hssfCell.setCellStyle(myCellStyle);
						}
					}
				}
				
				FileOutputStream fos = new FileOutputStream(file);
    			book.write(fos);  
    			fos.close();
				return true ;
			}catch(EngineException e1){
				log.printStackTrace(e1);
				throw e1;
			}catch(Exception e){
				e.printStackTrace();
				throw e;
			}finally{
				try {
					if(excel != null)
					excel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	/**
	 * 转化计算函数
	 * @param formulaValue
	 * @return
	 */
	private static String checkFormul(String formulaValue) {
		if(formulaValue.indexOf("{}")>=0 && (formulaValue.indexOf("SUM")>=0 || formulaValue.indexOf("sum")>=0)){
			
			String checkFormul = formulaValue.substring(4);
			int idex = checkFormul.indexOf("{");
			String cellrow = checkFormul.substring(0, idex);
			String[] formulA = subformul(cellrow);
			
			return "SUM(INDIRECT(\""+""+cellrow+":"+formulA[0]+"\"&ROW()-1))";
		}else{
			return formulaValue;
		}
	}
	
	private static String[] subformul(String formul) {
		String[] formarr = new String[2];
		int len = formul.length();
		for(int i=0;i<len;i++){
			if(Character.isDigit(formul.charAt(i))){
				formarr[0] =  formul.substring(0, i);
				formarr[1] =  formul.substring(i);
				
				return formarr;
			}
		}
		return null;
	}

	/**
	 * 解析制定颜色所对应的单元格列表	
	 * @param excelFilePath
	 * @param parseColor
	 * @return Map 颜色－单元格List
	 *
	 */
	public static Map parseRaqColorCell(ReportDefine rd, int[] parseColor) throws Exception
	{
		try {
			Map colorCellMap = new HashMap();
			for(int i=0;i<parseColor.length;i++)
			{
				List cellList = parseRaq(rd,parseColor[i]);
				if(cellList!=null && cellList.size()>0)
				{
					colorCellMap.put(String.valueOf(parseColor[i]), cellList);
				}
			}
			return colorCellMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("解析单元格出错，请检查单元格信息");
		}
	}
	/**
	 * 解析该Raq中的所有填报单元格
	 * 
	 * @param RaqFilePath
	 * @return
	 */
	private static List parseRaq(ReportDefine rd, int colorn) {
		List cellList = new ArrayList();
		try {
			
			int rownum = rd.getRowCount();
			int colnum = rd.getColCount();
			for(int i=1;i<=rownum;i++){
				for(int j=1;j<=colnum;j++){
					try{
					INormalCell cs = rd.getCell(i, (short) j);
					if(cs.getBackColor() == colorn){
						CellInfo cell = new CellInfo();
						cell.setRow(i);
						cell.setCol(j);

						IByteMap map = cs.getExpMap(false);
						if(!map.isEmpty()){
							cell.setFormual((String)map.getValue(0));
						}
						cell.setColor(colorn);
						cell.setValue((String)cs.getValue());
						cellList.add(cell);
					}
					}catch (Exception e){
						e.printStackTrace();
					}					
				}				
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cellList;
	}
	
	private static List makeColList(List colList) {
		List ogrList  = new ArrayList();
		HashMap map = new HashMap();
		for(int i=0;i<colList.size();i++){
			CellInfo cellA = (CellInfo) colList.get(i);
			if(map.get(String.valueOf(cellA.getCol())) != null){
				continue;
			}

			String cellValue = cellA.getValue();
			
			int row = cellA.getRow();
			for(int j=i+1;j<colList.size();j++){
				CellInfo cellB = (CellInfo) colList.get(j);				
				if(cellA.getCol()==cellB.getCol() ){
					if(StringUtil.isEmpty(cellValue)){
						cellValue = cellB.getValue();
					}else{
						if(!cellValue.equals(cellB.getValue())){
							cellValue = cellValue+"_"+cellB.getValue();
						}
					}					
					row = cellB.getRow();
				}
			}
			CellInfo mapCell = new CellInfo();
			mapCell.setCol(cellA.getCol());
			mapCell.setRow(row);
			mapCell.setValue(cellValue);
			map.put(String.valueOf(cellA.getCol()), mapCell);
			ogrList.add(mapCell);
		}
		return ogrList;
	}
	/**
	 * 取得并联的名字
	 * @param cellList
	 * @param i
	 * @return
	 */
	private static String getLastName(List cellList,int i){
		int colnum = i-1;
		CellInfo cellA = (CellInfo) cellList.get(i);
		String cellValue = cellA.getValue();
		while(StringUtil.isEmpty(cellValue)){
			CellInfo cellC = (CellInfo) cellList.get(colnum);
			cellValue = cellC.getValue();
			colnum--;
		}
		return cellValue;
	}

	private static Map makeListtoMap(List cellList){
		Map cellmap = new HashMap();
		for(int i=0;i<cellList.size();i++){
			CellInfo cellA = (CellInfo) cellList.get(i);
			cellmap.put(String.valueOf(cellA.getRow()),cellA.getValue());
		}
		return cellmap;
	}
	/**
     * 将列号转换为数字
     * @param ref
     * @return
     */
    public static String convertNumToCol(int num) {
    	StringBuffer col= new StringBuffer() ;
    	if(num<26){
    		int acs = num + 'A';
    		col.append((char) acs);
    	}else{
    		int acs = num/26-1 + 'A';
    		int acs1 = num%26 + 'A';
    		col.append((char) acs).append((char) acs1);
    	}
    	return col.toString();
    }
    // 删除上传的ZIP文件及解压文件夹
    public static void deleteUploadFile(String filePath) {
		// 删除ZIP文件
		File zipFile = new File(filePath);
		zipFile.delete();		
    }
	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 取得报表信息
	 * @param repInId
	 * @return 报表信息
	 */
	public static AFReportForm getReportIn(Integer repInId) {
		AFReportForm reportFrom = new AFReportForm();
		DBConn conn = null;

		try {
			StringBuffer hql = new StringBuffer("from AfReport a where a.repId=").append(repInId);
			conn = new DBConn();
			Session session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();
			if(list == null || list.size()==0){
				return null;
			}
			AfReport report = (AfReport) list.get(0);
			reportFrom.setRepId(String.valueOf(report.getRepId()));
			reportFrom.setRepName(report.getRepName());
			reportFrom.setBak1(report.getBak1());
			reportFrom.setRepFreqId(String.valueOf(report.getRepFreqId()));
			//reportFrom.setCheckDate(DateUtil.toSimpleDateFormat(report.getCheckDate(), "yyyymmdd"));
			reportFrom.setCheckDate(report.getCheckDate());
			reportFrom.setTemplateId(report.getTemplateId());
			reportFrom.setVersionId(report.getVersionId());
			reportFrom.setChecker(report.getChecker());
			reportFrom.setCheckFlag(String.valueOf(report.getCheckFlag()));
			reportFrom.setCurId(String.valueOf(report.getCurId()));
			//reportFrom.setDataRangeId(String.valueOf(report.getDataRangeId()));
			reportFrom.setDay(String.valueOf(report.getDay()));
			reportFrom.setForseReportAgainFlag(String.valueOf(report.getForseReportAgainFlag()));
			reportFrom.setLaterReportDay(String.valueOf(report.getLaterReportDay()));
			reportFrom.setMonth(String.valueOf(report.getTerm()));
			reportFrom.setNotReportFlag(String.valueOf(report.getNotReportFlag()));
			reportFrom.setOrgId(report.getOrgId());
			reportFrom.setPackage_(String.valueOf(report.getPackage_()));
			reportFrom.setPreStandard(report.getPreStandard());
			reportFrom.setPrincipal(report.getPrincipal());
			reportFrom.setRecheckFlag(String.valueOf(report.getRecheckFlag()));
			reportFrom.setReportDataWarehouseFlag(String.valueOf(report.getReportDataWarehouseFlag()));
			//reportFrom.setReportDate(DateUtil.toSimpleDateFormat(report.getReportDate(), "yyyymmdd"));
			reportFrom.setReportDate(report.getReportDate());
			reportFrom.setRepRangeFlag(String.valueOf(report.getRepRangeFlag()));
			reportFrom.setReReportTimes(String.valueOf(report.getReReportTimes()));
			reportFrom.setTblInnerValidateFlag(String.valueOf(report.getTblInnerValidateFlag()));
			reportFrom.setTblOuterValidateFlag(String.valueOf(report.getTblOuterValidateFlag()));
			reportFrom.setTimes(String.valueOf(report.getTimes()));
			//reportFrom.setVerifyDate(DateUtil.toSimpleDateFormat(report.getVerifyDate(), "yyyymmdd"));
			reportFrom.setVerifyDate(report.getVerifyDate());
			reportFrom.setWriter(report.getWriter());
			reportFrom.setYear(String.valueOf(report.getYear()));
			
			
		} catch (HibernateException he) {
			he.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return reportFrom;
	}
	/**
	 * jdbc技术 可能需要修改  卞以刚  2011-12-21
	 * 此次修改为Oracle向SQLServer做数据迁移
	 * 将jdbc技术修改为使用hibernate 
	 * 作者：卞以刚 日期：2011-12-26
	 * 取得报表数据
	 * @param repInId
	 * @param reportFlg
	 * @return 报表数据
	 */
	public static List getReportData(Integer repInId,String reportFlg) {
		List retVals = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String hhql="";
		if (repInId != null)
		{
			try
			{
				// 1104报表
				if(reportFlg.equals("1")){
					hhql = "select r.cell_id, r.report_value ,m.col_id,m.row_id  from report_in_info r"
						+ " left join  M_cell m on  r.cell_id=m.cell_id where r.rep_in_id=? and r.report_value is not null";
//					hhql="select r.MCell.cellId,r.reportValue,r.MCell.colId,r.MCLL.rowId from ReportInInfo r"+
//						"where r.reportIn.repInId=:repId and r.reportValue is not null";
						
				
				} else if(reportFlg.equals("2")){
					// 人行报表
					
					hhql = "select r.cell_id, r.cell_data ,m.col_num,m.row_num  from af_pbocreportdata r"
						+ " left join  af_cellinfo m on  r.cell_id=m.cell_id where r.rep_id=? and r.cell_data is not null";
//					hhql="select r.cellId,r.cellData,m.colNum,m.rowNum from AfPbocreportdata r"+
//						"left join AfCellinfo m on r.cellId=m.cellId where r.repId=:repId and r.cellData is not null";
				
				} else if(reportFlg.equals("3")){
					// 其他报表
					hhql = "select r.cell_id, r.cell_data ,m.col_num,m.row_num  from af_otherreportdata r"
						+ " left join  af_cellinfo m on  r.cell_id=m.cell_id where r.rep_id=? and r.cell_data is not null";
//					hhql="select r.cellId,r.cellData,m.colNum,m.rowNum from AfOtherreportdata r"+
//						"left join AfCellinfo m on r.cellId=m.cellId where r.repId=:repId and r.cellData is not null";
				}
				con = new com.cbrc.smis.proc.jdbc.FitechConnection().getConnect();
				stmt = con.prepareStatement(hhql.toUpperCase());
				stmt.setInt(1, repInId.intValue());
				rs = stmt.executeQuery();
				if (rs != null)
				{
					retVals = new ArrayList();
					while (rs.next())
					{
						int cellId=rs.getInt(1);
						String reportValue = rs.getString(2);
						String colId = rs.getString(3);
						int rowId = rs.getInt(4);

						ReportInInfoForm reportInInfoForm = new ReportInInfoForm();
						reportInInfoForm.setCellId(new Integer(cellId));
						reportInInfoForm.setReportValue(reportValue);
						reportInInfoForm.setColId(colId);
						reportInInfoForm.setRowId(new Integer(rowId));
						retVals.add(reportInInfoForm);

					}
				}
				/***
				 *  此次修改为Oracle向SQLServer做数据迁移
				 * 将jdbc技术修改为使用hibernate 
				 * 作者：卞以刚 日期：2011-12-26
				 */
//				conn=new DBConn();
//				session=conn.openSession();
//				Query query=session.createQuery(hhql);
//				query.setInteger("repId", repInId.intValue());
//				Iterator list=query.iterate();
//				if(list!=null)
//				{
//					while (list.hasNext()) {
//						retVals = new ArrayList();
//						Object[] row=(Object[])list.next();
//						int cellId=Integer.valueOf(row[0].toString());
//						String reportValue = row[1].toString();
//						String colId = row[2].toString();
//						int rowId = Integer.valueOf(row[3].toString());
//
//						ReportInInfoForm reportInInfoForm = new ReportInInfoForm();
//						reportInInfoForm.setCellId(new Integer(cellId));
//						reportInInfoForm.setReportValue(reportValue);
//						reportInInfoForm.setColId(colId);
//						reportInInfoForm.setRowId(new Integer(rowId));
//						retVals.add(reportInInfoForm);
//					}
//				}
			}
			catch (Exception e)
			{
				
				try
				{
					con.close();
				}
				catch (Exception e1)
				{
					// TODO 自动生成 catch 块
					e1.printStackTrace();
				}
			}
			finally
			{
				if (con != null)
					try
					{
						con.close();
					}
					catch (Exception e)
					{
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
			}
		}
		return retVals;
	}
	/**
	 * 判断是否存在数据
	 * @param repInId
	 * @param reportFlg
	 * @return 判断结果
	 */
	public static boolean isReportData(int repInId,String reportFlg) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String hhql = null;

		try
		{
			if(reportFlg.equals("1")){
				hhql = "select count(*)  from report_in_info r"
					+ " where r.rep_in_id=? and r.report_value is not null";
			
			} else if(reportFlg.equals("2")){
				hhql = "select count(*)  from af_pbocreportdata r"
					+ "  where r.rep_id=? and r.cell_data is not null";
			
			} else if(reportFlg.equals("3")){
				hhql = "select count(*)  from af_otherreportdata r"
					+ " where r.rep_id=? and r.cell_data is not null";
			
			}
			con = new com.cbrc.smis.proc.jdbc.FitechConnection().getConnect();
			stmt = con.prepareStatement(hhql.toUpperCase());
			stmt.setInt(1, repInId);

			rs = stmt.executeQuery();
			while (rs.next())
			{
				int cellId=rs.getInt(1);
				if(cellId > 0){
					return true;
				} else {
					return false;
				}

			}
		}
		catch (Exception e)
		{
			
			try
			{
				con.close();
			}
			catch (SQLException e1)
			{
				// TODO 自动生成 catch 块
				e1.printStackTrace();
			}
			return false;
		}
		finally
		{
			if (con != null)
				try
				{
					con.close();
				}
				catch (SQLException e)
				{
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
		}
		return false;
		
	}
	/**
	 * jdbc技术 需要修改 卞以刚 2011-12-22
	 * 修改报表信息
	 * @param templateForm
	 * @param reportFlg
	 * @return 修改结果
	 */
	public static boolean updateTemplate(AFTemplateForm templateForm, String reportFlg) {
		
		StringBuffer addsql= null;
		
		DBConn conn=null;
    	Session session=null;
    	Connection connection=null;
    	Statement stmt = null;
    	boolean result=false;

		try {
			conn = new DBConn();
	    	session=conn.beginTransaction();
	    	connection=session.connection();
	    	connection.setAutoCommit(false);
	    	
	    	stmt = connection.createStatement();
	    	
	    	
	    	if(templateForm.getStartDate() == null){
	    		templateForm.setStartDate("");
	    	}
	    	if(templateForm.getEndDate() == null){
	    		templateForm.setEndDate("");
	    	}
	    	if(templateForm.getSupplementFlag() == null){
	    		templateForm.setSupplementFlag("");
	    	}
	    	if(templateForm.getBak1() == null){
	    		templateForm.setBak1("");
	    	}
	    	if(templateForm.getJoinTemplateId() == null){
	    		templateForm.setJoinTemplateId("");
	    	}
	    	// 没有选中即是点对点报表
	    	if(StringUtil.isEmpty(templateForm.getReportStyle())){
	    		templateForm.setReportStyle(com.fitech.gznx.common.Config.REPORT_DD);
	    	}
	    	
	    	//银监会报表需多同步的表
	    	if(reportFlg.trim().equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
	    		
	    		addsql = new StringBuffer();
	    		
		    	addsql.append("update M_MAIN_REP set REP_CN_NAME='").append(templateForm.getTemplateName())
		    	.append("', CUR_UNIT=").append(templateForm.getCurUnit())
	    		.append(",REP_TYPE_ID=").append(templateForm.getRepTypeId())
    			.append(" where REP_ID=(select REP_ID from M_CHILD_REPORT")
    			.append(" WHERE CHILD_REP_ID='").append(templateForm.getTemplateId())
	    		.append("' and VERSION_ID='").append(templateForm.getVersionId()).append("')");
	    		
		    	stmt.addBatch(addsql.toString());
		    	
	    		addsql = new StringBuffer();
	    		
		    	addsql.append("update M_CHILD_REPORT set REPORT_NAME='").append(templateForm.getTemplateName())
		    	.append("', CUR_UNIT=").append(templateForm.getCurUnit())
	    		.append(",REPORT_STYLE=").append(templateForm.getReportStyle())
	    		.append(",START_DATE='").append(templateForm.getStartDate())
	    		.append("',END_DATE='").append(templateForm.getEndDate())
    			.append("' where CHILD_REP_ID='").append(templateForm.getTemplateId())
	    		.append("' and VERSION_ID='").append(templateForm.getVersionId()).append("'");
	    		
		    	stmt.addBatch(addsql.toString());
		    	
		    	addsql = new StringBuffer();
		    	
		    	addsql.append("update AF_Template set Template_Name='").append(templateForm.getTemplateName())
		    		.append("',bak1='").append(templateForm.getRepTypeId())
		    		.append("',report_style=").append(templateForm.getReportStyle())
		    		.append(",priority_flag=").append(templateForm.getPriorityFlag())
		    		.append(",start_date='").append(templateForm.getStartDate())
		    		.append("',end_date='").append(templateForm.getEndDate())
		    		.append("',JOIN_TEMPLATE_ID='").append(templateForm.getJoinTemplateId())
	    			.append("' where Template_ID='").append(templateForm.getTemplateId())
		    		.append("' and Version_ID='").append(templateForm.getVersionId()).append("'");
		    	
		    	stmt.addBatch(addsql.toString());
	    		
	    	} else {
	    		
		    	addsql = new StringBuffer();
		    	// 修改报表信息
		    	addsql.append("update AF_Template set Template_Name='").append(templateForm.getTemplateName()).
		    		append("', is_report=").append(templateForm.getIsReport())
		    		.append(",bak1='").append(templateForm.getBak1())
		    		.append("',is_leader=").append(templateForm.getIsLeader())
		    		.append(",report_style=").append(templateForm.getReportStyle())
		    		.append(",priority_flag=").append(templateForm.getPriorityFlag())
		    		.append(",start_date='").append(templateForm.getStartDate())
		    		.append("',end_date='").append(templateForm.getEndDate()).append("' ")
		    		.append(",JOIN_TEMPLATE_ID='").append(templateForm.getJoinTemplateId()).append("'");
	    			
		    	if(templateForm.getSupplementFlag()!=null && !templateForm.getSupplementFlag().equals("")  
		    			&& !templateForm.getSupplementFlag().equals("null"))
	    			addsql.append(",supplement_flag=").append(templateForm.getSupplementFlag());
		    	addsql.append(" where Template_ID='").append(templateForm.getTemplateId())
	    			  .append("' and Version_ID='").append(templateForm.getVersionId()).append("'");
		    	stmt.addBatch(addsql.toString());
	    		
		    	// 修改币种信息
		    	String[] currList = templateForm.getCurrList();
				if(currList != null && currList.length>0){
					// 删除关联关系
					addsql = new StringBuffer();
					addsql.append("delete from af_template_curr_relation where Template_ID='")
					.append(templateForm.getTemplateId())
					.append("' and Version_ID='")
					.append(templateForm.getVersionId()).append("'");
					
					stmt.addBatch(addsql.toString());
					
					for(String curr:currList){					
						// 添加关联关系
						addsql = new StringBuffer();
						addsql.append("insert into af_template_curr_relation ")
						.append("(Template_ID,Version_ID,cur_id) values ('")
						.append(templateForm.getTemplateId()).append("','")
						.append(templateForm.getVersionId())
						.append("',").append(curr).append(")");
						stmt.addBatch(addsql.toString());
					}
				}
	    	}
			
			result = true;
			stmt.executeBatch();
			
		}  catch (Throwable e1) {
			e1.printStackTrace();
			result = false;
		}finally{
    		try {
    			if(conn!=null)
    				conn.endTransaction(result);
				//if(result == true) {
				///	connection.commit();
				//}else{
				//	connection.rollback();
				//}
				//connection.setAutoCommit(result);
				//if(stmt != null) 
				//	stmt.close();
				//if(connection != null)
				//	connection.close();
				//if(session != null) 
				//	session.close();
			//	if(conn != null) 
				//	conn.closeSession();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
    	}
		return result;
	}
	/**
	 * 解析模板单元格信息
	 * @param filePath
	 * @param templateId
	 * @param versionId
	 * @param reportFlg
	 * @return 模板保存路径
	 */
	public static String resolveRAQ(InputStream filePath,
			String templateId,String versionId, String reportFlg) {
		StringBuffer addsql= null;
		AfTemplate aftemplate = AFTemplateDelegate.getTemplate(templateId,versionId);
		if(aftemplate.getReportStyle() != null && com.fitech.gznx.common.Config.REPORT_QD.equals(String.valueOf(aftemplate.getReportStyle()))){
			return resolveQDRAQ(filePath,templateId, versionId,  reportFlg);
		}
		DBConn conn=null;
    	Session session=null;
    	Connection connection=null;
    	Statement stmt = null;
    	boolean result=false;
    	String raqFileName = null;
		try {
			conn = new DBConn();
			Long currCellInfoVal=new AFReportDealDelegate().currVal("AF_CELLINFO", conn, "cell_Id");
			Long currMCellVal=new AFReportDealDelegate().currVal("M_CELL", conn, "cell_Id");
			Long currGatherFormulaVal=new AFReportDealDelegate().currVal("AF_GATHERFORMULA", conn, "FORMULA_ID");
			
	    	session=conn.beginTransaction();
	    	connection=session.connection();
	    	connection.setAutoCommit(false);	    	
	    	stmt = connection.createStatement();
	    	
	    	addsql = new StringBuffer();
	    	
    		raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "Raq"+ 
			File.separator+templateId + "_" + versionId + ".raq";		    	
			ReportDefine rd = (ReportDefine)ReportUtils.read(filePath);
			// 需要解析的单元格颜色
			final int[] PARSE_CELL_COLOR =
			{ Config.PITEM_CELL_BGCOLOR, Config.PCOL_CELL_BGCOLOR,Config.PID_CELL_BGCOLOR};
			// 解析单元格
			Map parseCellMap = parseRaqColorCell(rd, PARSE_CELL_COLOR);
			List pItemList = (List) parseCellMap.get(String.valueOf(Config.PITEM_CELL_BGCOLOR));
			List pColList = (List) parseCellMap.get(String.valueOf(Config.PCOL_CELL_BGCOLOR));
			List pIDlList = (List) parseCellMap.get(String.valueOf(Config.PID_CELL_BGCOLOR));
			if(pIDlList == null){
				pIDlList = new ArrayList();
			}
			if(pItemList == null){
				pItemList = new ArrayList();
			}
			if(pColList == null){
				pColList = new ArrayList();
			}
			// 删除单元格公式
			addsql = new StringBuffer();
			addsql.append("delete from AF_GatherFormula where Template_ID='").append(templateId)
			.append("' and Version_ID=").append(versionId);
			stmt.addBatch(addsql.toString());
			Map afcellMap = StrutsAFCellDelegate.getAFCellIdMap(templateId, versionId);
			Map mcellMap = null;
			if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
				mcellMap = StrutsAFCellDelegate.getMCellIdMap(templateId, versionId);
			}
			List templateAreaList = createTemplateArea(pItemList,pColList,pIDlList);
			for(int num=0;num<templateAreaList.size();num++){
			TemplateArea area = (TemplateArea)templateAreaList.get(num);
			List aItemList = area.getItemList();
			List aColList = area.getColList();
			List aPidList = area.getPidList();
			
			List itemList = makeRowList(aItemList);
			
			List colList = makeColList(aColList);
			//aPidList = makeRowList(aPidList);
			Map cellmap = makeListtoMap(aPidList);
			Map itenMap = makeListtoMap(aItemList);

			// 取得行信息
			int nSize =0;
			if(itemList.size() >= aPidList.size()){
				nSize = itemList.size();
			} else{
				nSize = aPidList.size();
			}
			
			for(int i=0;i<nSize;i++){
				CellInfo cell=null;
				String templateName =null;
				if(itemList.size() >= aPidList.size()){
					cell= (CellInfo)itemList.get(i);
					templateName = cell.getValue();					
				}else{
					cell= (CellInfo)aPidList.get(i);
					if(itenMap.get(String.valueOf(cell.getRow()))!= null){
						templateName = (String) itenMap.get(String.valueOf(cell.getRow()));
					}else{
						templateName = "";
					}
					
				}
				if(!StringUtil.isEmpty(templateName)){
					templateName = templateName.trim();
				}
				int row = cell.getRow();
				
				if(colList != null && colList.size()>0){
				
				for(int j=0;j<colList.size();j++){
					CellInfo cellcol = (CellInfo)colList.get(j);
					if(cell.getRow()<=cellcol.getRow()){
						continue;
					}
					if(cell.getCol()>=cellcol.getCol()){
						continue;
					}
					String colTemplateNmae = cellcol.getValue();
					if(!StringUtil.isEmpty(colTemplateNmae)){
						colTemplateNmae = colTemplateNmae.trim();
					} else {
						colTemplateNmae = "";
					}
					int col = cellcol.getCol();
					String colname= convertNumToCol(col-1);
					
					INormalCell cs = rd.getCell(row, (short) col);
					IByteMap map = cs.getExpMap(false);
					//2014-01-21:LuYueFei:灰色单元格不需要入库
					int backColor = cs.getBackColor();
					if(backColor == Config.DGRAY_BGCOLOR || backColor == Config.GRAY_BGCOLOR || backColor == Config.LGRAY_BGCOLOR){
						continue;
					}
					
					String cellPid = null;
					if( cellmap.get(String.valueOf(row))!= null){
						cellPid = (String) cellmap.get(String.valueOf(row));
					}else{
						cellPid="";
					}
					if(cellPid!=null)
						cellPid = cellPid.trim();
					//int cellId = StrutsAFCellDelegate.getCellId(templateId,versionId,colname+row);
					Integer cellId = (Integer) afcellMap.get(colname+row);
					addsql = new StringBuffer();
					// 存在则修改，否则插入新的单元格信息
					if(cellId != null){
						//afcellMap.remove(colname+row);
						addsql.append("update AF_CellInfo set row_name='"+templateName+"'," +
						"col_name='"+colTemplateNmae+"' ,cell_pid='"+cellPid+"' where cell_id="+cellId);
						
					}else{
						
						if(Config.DB_SERVER_TYPE.equals("oracle")){
							addsql.append("insert into AF_CellInfo (cell_id ,Template_ID,Version_ID ,row_name," +
									"col_name ,row_num,col_num,cell_name ,cell_pid,collect_type,data_type ) values (SEQ_AF_CELLINFO.NEXTVAL,'").
			    			append(templateId).append("','").append(versionId)
			    			.append("','").append(templateName).append("','").append(colTemplateNmae).append("','").append(row).append("','")
			    			.append(colname).append("','").append(colname+row).append("','").append(cellPid).append("',null,null)");
						}
						if(Config.DB_SERVER_TYPE.equals("sqlserver")){
							addsql.append("insert into AF_CellInfo (cell_id ,Template_ID,Version_ID ,row_name," +
									"col_name ,row_num,col_num,cell_name ,cell_pid,collect_type,data_type ) values ("+(++currCellInfoVal)+",'").
			    			append(templateId).append("','").append(versionId)
			    			.append("','").append(templateName).append("','").append(colTemplateNmae).append("','").append(row).append("','")
			    			.append(colname).append("','").append(colname+row).append("','").append(cellPid).append("',null,null)");
						}
					}
					
					stmt.addBatch(addsql.toString());
					// 更新1104单元格信息
					if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
					//	Integer mcellId = StrutsMCellDelegate.getCellId(templateId,versionId,colname+row);
						Integer mcellId = (Integer) mcellMap.get(colname+row);
						if(mcellId == null){
							addsql = new StringBuffer();
							if(Config.DB_SERVER_TYPE.equals("oracle")){
								addsql.append("insert into m_cell (cell_id ,CHILD_REP_ID,VERSION_ID ,ROW_ID,COL_ID,CELL_NAME ) values (SEQ_M_CELL.NEXTVAL,'").
				    			append(templateId).append("','").append(versionId)
				    			.append("','").append(row).append("','")
				    			.append(colname).append("','").append(colname+row).append("')");
								stmt.addBatch(addsql.toString());
							}
							if(Config.DB_SERVER_TYPE.equals("sqlserver")){
								addsql.append("insert into m_cell (cell_id ,CHILD_REP_ID,VERSION_ID ,ROW_ID,COL_ID,CELL_NAME ) values ("+(++currMCellVal)+",'").
				    			append(templateId).append("','").append(versionId)
				    			.append("','").append(row).append("','")
				    			.append(colname).append("','").append(colname+row).append("')");
								stmt.addBatch(addsql.toString());
							}
						}
					}
					// 更新归并关系公式
					try{
						if(map != null && !map.isEmpty()){
						if(map.getValue(0) != null){
							addsql = new StringBuffer();
							String formulaValue = (String) map.getValue(0);
							formulaValue = formulaValue.replaceAll("'", "''");
							formulaValue = formulaValue.length() > 3500 ? formulaValue.substring(0, 3500) : formulaValue;
							
							if(cellId!=null){
								if(Config.DB_SERVER_TYPE.equals("oracle")){
									addsql.append("insert into AF_GatherFormula (formula_id ,Template_ID,Version_ID,cell_id,formula_value," +
									"formula_name) values (SEQ_AF_GATHERFORMULA.NEXTVAL,'").append(templateId).
									append("','").append(versionId).
									append("',"+cellId+",'").append(formulaValue).append("',null)");
								}
								if(Config.DB_SERVER_TYPE.equals("sqlserver")){
									addsql.append("insert into AF_GatherFormula (formula_id ,Template_ID,Version_ID,cell_id,formula_value," +
									"formula_name) values ("+(++currGatherFormulaVal)+",'").append(templateId).
									append("','").append(versionId).
									append("',"+cellId+",'").append(formulaValue).append("',null)");
								}
							}else{
								if(Config.DB_SERVER_TYPE.equals("oracle")){
									addsql.append("insert into AF_GatherFormula (formula_id ,Template_ID,Version_ID,cell_id,formula_value," +
									"formula_name) values (SEQ_AF_GATHERFORMULA.NEXTVAL,'").append(templateId).
									append("','").append(versionId).
									append("',SEQ_AF_CELLINFO.Currval,'").append(formulaValue).append("',null)");
								}
								if(Config.DB_SERVER_TYPE.equals("sqlserver")){
									addsql.append("insert into AF_GatherFormula (formula_id ,Template_ID,Version_ID,cell_id,formula_value," +
									"formula_name) values ("+(++currGatherFormulaVal)+",'").append(templateId).
									append("','").append(versionId).
									append("',"+currCellInfoVal+",'").append(formulaValue).append("',null)");
								}
							}
							
							stmt.addBatch(addsql.toString());
						}
					}
					}catch(Exception e){
						e.printStackTrace();
					}						
				}
	
				}
			}
			}
			resetbackcolor(rd,pItemList);
			resetbackcolor(rd,pColList);
			resetbackcolor(rd,pIDlList);			
			stmt.executeBatch();
			ReportUtils.write(raqFileName,rd);  
			//将ReportDefine保存到文件
//			if(!Config.SHARE_DATA_PATH.equals(Config.WEBROOTPATH)){ 
//				raqFileName =Config.SHARE_DATA_PATH +"templateFiles" + File.separator + "Raq"+ 
//				File.separator+templateId + "_" + versionId + ".raq";
//				ReportUtils.write(raqFileName,rd);
//			}
			result = true;

			return raqFileName;
		}  catch (Throwable e1) {
			e1.printStackTrace();
			result = false;
			return null;
		}finally{
    		try {
				//if(result == true) {
				//	connection.commit();
			//	}else{
				//	connection.rollback();
				//}
			//	connection.setAutoCommit(result);
    			if(conn!=null)
    				conn.endTransaction(result);
				
			//	if(stmt != null) 
				//	stmt.close();
				//if(connection != null)
			//		connection.close();
			//	if(session != null) 
			//		session.close();
			//	if(conn != null) 
			//		conn.closeSession();
			//} catch (SQLException e1) {
			//	e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}		
	}
	/**
	 * 解析清单式模板单元格信息
	 * @param filePath
	 * @param templateId
	 * @param versionId
	 * @param reportFlg
	 * @return 模板保存路径
	 */
	private static String resolveQDRAQ(InputStream filePath,String templateId,
			String versionId, String reportFlg) {

		// 定义清单式报表单元格信息
		StringBuffer addsql= null;
		
		DBConn conn=null;
    	Session session=null;
    	Connection connection=null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	boolean result=false;
    	String raqFileName = null;
		try {

			conn = new DBConn();
			Long currCellInfoVal=new AFReportDealDelegate().currVal("AF_CELLINFO", conn, "CELL_ID");
			Long currMCellVal=new AFReportDealDelegate().currVal("m_cell", conn, "CELL_ID");
			Long currGatherFormulaVal=new AFReportDealDelegate().currVal("AF_GatherFormula", conn, "formula_id");
	    	session=conn.beginTransaction();
	    	connection=session.connection();
	    	connection.setAutoCommit(false);	    	
	    	stmt = connection.createStatement();
	    	boolean temexit = true;
	    	// 检测是否存在报表对应的表信息
	    	addsql = new StringBuffer();
	    	String tablename = "AF_QD_"+templateId.toUpperCase().trim();
	    	addsql.append("select COLUMN_NAME from  user_tab_columns  where   TABLE_NAME='").append(tablename).append("'");
	    	rs  = stmt.executeQuery(addsql.toString().toUpperCase());	
	    	if(!rs.next()){
	    		temexit = false;
	    	}
	    	
	    	// 保存清单式报表单元格信息
			addsql = new StringBuffer();
			addsql.append("select count(*) from qd_cellinfo  where Template_ID='").append(templateId)
			.append("' and Version_ID=").append(versionId);
			rs = stmt.executeQuery(addsql.toString().toUpperCase());	
			rs.next();
			int templatenum = rs.getInt(1);
			
			
			stmt.clearBatch();
	    	addsql = new StringBuffer();
	    	// 删除单元格公式
			addsql.append("delete from AF_GatherFormula where Template_ID='").append(templateId)
			.append("' and Version_ID=").append(versionId);
			stmt.addBatch(addsql.toString());
    		raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "Raq"+ 
			File.separator+templateId + "_" + versionId + ".raq";		    	
			ReportDefine rd = (ReportDefine)ReportUtils.read(filePath);
		
			// 需要解析的单元格颜色
			final int[] PARSE_CELL_COLOR =
			{ Config.PITEM_CELL_BGCOLOR, Config.PCOL_CELL_BGCOLOR};
			// 解析单元格
			Map parseCellMap = parseRaqColorCell(rd, PARSE_CELL_COLOR);
			List<CellInfo> pItemList = (List) parseCellMap.get(String.valueOf(Config.PITEM_CELL_BGCOLOR));
			List<CellInfo> pColList = (List) parseCellMap.get(String.valueOf(Config.PCOL_CELL_BGCOLOR));
			
			
			if(pItemList == null){
				pItemList = new ArrayList();
			}
			if(pColList == null){
				pColList = new ArrayList();
			}
			CellInfo endFlgCell =  pColList.get(0);
			// 结束标志名称
			String endFlgName = endFlgCell.getValue();
			int endFlgcol = endFlgCell.getCol();
			// 列数
			int coln = 0;
			// 起始列
			int startCol = 0;
			// 结束列
			int endCol = 0;
			// 开始行
			int startRow = 0;
			for(CellInfo qdCell:pItemList){
				if(startRow == 0)
					startRow = qdCell.getRow();
				if(startCol>qdCell.getCol() || startCol==0){
					startCol = qdCell.getCol();
				}
				if(endCol<qdCell.getCol() || endCol==0){
					endCol = qdCell.getCol();
				}
			}
			coln = endCol- startCol;
			if(templatenum==0){
				// 保存清单式报表单元格信息
				addsql = new StringBuffer();
				addsql.append("insert into qd_cellinfo (Template_ID,Version_ID ,COL_COUNT," +
				"START_COL ,END_COL,FLAG_COL,END_FLAG ,START_ROW) values ('").
				append(templateId).append("','").append(versionId).
				append("',").append(coln).append(",'").append(convertNumToCol(startCol-1)).
				append("','").append(convertNumToCol(endCol-1)).append("','").append(convertNumToCol(endFlgcol-1)).
				append("','").append(endFlgName).append("',").append(startRow).append(")");
				stmt.addBatch(addsql.toString());
			} else {
				// 保存清单式报表单元格信息
				addsql = new StringBuffer();
				addsql.append("update qd_cellinfo set COL_COUNT=").append(coln).append(",START_COL='").
					append(convertNumToCol(startCol-1)).append("' ,END_COL='").append(convertNumToCol(endCol-1)).
					append("',FLAG_COL='").append(convertNumToCol(endFlgcol-1)).append("',END_FLAG='").
					append(endFlgName).append("' ,START_ROW=").append(startRow).append(" where Template_ID='").append(templateId)
				.append("' and Version_ID=").append(versionId);
				stmt.addBatch(addsql.toString());
			}
			
			if(!reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
		    	if (!temexit) {
					// 创建表结构				
					addsql = new StringBuffer();					
					addsql.append("CREATE TABLE ").append(tablename).append(" (Rep_ID INTEGER NOT NULL," +
							"ROW_ID INTEGER NOT NULL,");
					for(int i=startCol;i<endCol+1;i++){
						if(Config.DB_SERVER_TYPE.equals("oracle"))
							addsql.append("COL").append(i).append(" VARCHAR2(200) NULL,");
						if(Config.DB_SERVER_TYPE.equals("sqlserver"))
							addsql.append("COL").append(i).append(" VARCHAR(200) NULL,");
					}
					if(Config.DB_SERVER_TYPE.equals("oracle"))
						addsql.append(" bak1 VARCHAR2(10) NULL,bak2 VARCHAR2(200) NULL)");
					if(Config.DB_SERVER_TYPE.equals("oracle"))
						addsql.append(" bak1 VARCHAR(10) NULL,bak2 VARCHAR(200) NULL)");
					stmt.addBatch(addsql.toString().toUpperCase());
					addsql = new StringBuffer();
					addsql.append("ALTER TABLE ").append(tablename).append(" ADD PRIMARY KEY (Rep_ID, ROW_ID)");					
					stmt.addBatch(addsql.toString());				
				} else {
					// 修改表结构
					Map colmap = new HashMap();
					while(rs.next()){
						String colname = rs.getString("COLUMN_NAME");
						if(!StringUtil.isEmpty(colname) && colname.length()>3 
								&& colname.substring(0, 3).toUpperCase().equals("COL")){
							colmap.put(colname, colname);
						}
					}
					for(int i=startCol;i<endCol+1;i++){
						String colname = "COL"+i;
						if(colmap.containsKey(colname)){
							colmap.remove(colname);
						} else {
							addsql = new StringBuffer();
							if(Config.DB_SERVER_TYPE.equals("oracle"))
								addsql.append("ALTER TABLE ").append(tablename).append(" ADD ").append(colname).append(" VARCHAR2(200) NULL");
							if(Config.DB_SERVER_TYPE.equals("sqlserver"))
								addsql.append("ALTER TABLE ").append(tablename).append(" ADD ").append(colname).append(" VARCHAR(200) NULL");					
							stmt.addBatch(addsql.toString());
						}
					}
					if(!colmap.isEmpty()){
						Iterator it=colmap.keySet().iterator();
						if(it!=null){
							while(it.hasNext()){
								String colname = (String) it.next();
								if(!StringUtil.isEmpty(colname)){
									addsql = new StringBuffer();
									addsql.append("ALTER TABLE ").append(tablename).append(" drop column  ").append(colname);					
									stmt.addBatch(addsql.toString());
								}
							}
						}
					}				
				}
			}
			// 取得取数公式
			int rownum = rd.getRowCount();
			int colnum = rd.getColCount();
			for(int i=1;i<=rownum;i++){
				for(int j=1;j<=colnum;j++){
					INormalCell cs = rd.getCell(i, (short) j);
					IByteMap map = cs.getExpMap(false);
	
					try{
						if(map != null && !map.isEmpty()){
						if(map.getValue(0) != null){
							addsql = new StringBuffer();
							String formulaValue = (String) map.getValue(0);
							formulaValue = formulaValue.replaceAll("'", "''");
							if(Config.DB_SERVER_TYPE.equals("oracle")){
								addsql.append("insert into AF_GatherFormula (formula_id ,Template_ID,Version_ID,cell_id,formula_value," +
								"formula_name) values (SEQ_AF_GATHERFORMULA.NEXTVAL,'").append(templateId).
								append("','").append(versionId).
								append("',SEQ_AF_CELLINFO.NEXTVAL,'").append(formulaValue).append("',null)");
							}
							if(Config.DB_SERVER_TYPE.equals("sqlserver")){
								addsql.append("insert into AF_GatherFormula (formula_id ,Template_ID,Version_ID,cell_id,formula_value," +
								"formula_name) values ("+(++currGatherFormulaVal)+",'").append(templateId).
								append("','").append(versionId).
								append("',"+currCellInfoVal+",'").append(formulaValue).append("',null)");
							}
							stmt.addBatch(addsql.toString());
						}
					}
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}
			}
	
			resetbackcolor(rd,pItemList);
			resetbackcolor(rd,pColList);
			stmt.executeBatch();
			ReportUtils.write(raqFileName,rd);  
			//将ReportDefine保存到文件
//			if(!Config.SHARE_DATA_PATH.equals(Config.WEBROOTPATH)){ 
//				raqFileName =Config.SHARE_DATA_PATH +"templateFiles" + File.separator + "Raq"+ 
//				File.separator+templateId + "_" + versionId + ".raq";
//				ReportUtils.write(raqFileName,rd);
//			}
			result = true;
			return raqFileName;
		}  catch (Throwable e1) {
			e1.printStackTrace();
			result = false;
			return null;
		}finally{
    		try {
				if(result == true) {
					connection.commit();
				}else{
					connection.rollback();
				}
				connection.setAutoCommit(result);
				if (rs != null)
					rs.close();
				if(stmt != null) 
					stmt.close();
				if(connection != null)
					connection.close();
				if(session != null) 
					session.close();
				if(conn != null) 
					conn.closeSession();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
    	}		
	}
	/**
	 * 无数据交互
	 * 创建1104单元格信息
	 * @param fileName
	 * @param templateId
	 * @param versionId
	 * @return 创建结果
	 */ 
	public static boolean resoveYJH(String fileName,String templateId,String versionId) {
		boolean result = false;
		
		try{
			// 写模板到reports/templates目录下，用于机构下载模板
			String templateFileName = Config.WEBROOTPATH + com.fitech.net.config.Config.REPORT_NAME
					+ File.separator + com.fitech.net.config.Config.TEMPLATE_NAME + File.separator
					+ templateId + "_" + versionId + ".xls";
			result = FitechUtil.copyFile(fileName, templateFileName);
		
			// 写模板到report_mgr/excel目录下，但文件需要做适当的修改
			String excelFileName = Config.WEBROOTPATH + "report_mgr" + File.separator + "excel" + File.separator
					+ templateId + versionId + ".xls";
			result = FitechUtil.copyFile(fileName, excelFileName);
			
//			if(!Config.SHARE_DATA_PATH.equals(Config.WEBROOTPATH)){
//				// 更新共享文件夹
//				templateFileName = Config.SHARE_DATA_PATH + com.fitech.net.config.Config.REPORT_NAME
//				+ File.separator + com.fitech.net.config.Config.TEMPLATE_NAME + File.separator
//				+ templateId + "_" + versionId + ".xls";
//				result = FitechUtil.copyFile(fileName, templateFileName);
//			
//				// 写模板到report_mgr/excel目录下，但文件需要做适当的修改
//				excelFileName = Config.SHARE_DATA_PATH + "report_mgr" + File.separator + "excel" + File.separator
//						+ templateId + versionId + ".xls";
//				result = FitechUtil.copyFile(fileName, excelFileName);
//			}
			

			return result;
		}catch (Exception ex)
		{
			ex.printStackTrace();
			result = false;
		}

		return false;
	}
	/***
	 * jdbc技术 需要修改 卞以刚 2011-12-22
	 * @param childRepId
	 * @param versionId
	 * @param intValue
	 * @return
	 */
	public static boolean updateTemplateStyle(String childRepId,
			String versionId, int intValue) {
		// 定义清单式报表单元格信息
		StringBuffer addsql= null;
		
		DBConn conn=null;
    	Session session=null;
    	Connection connection=null;
    	Statement stmt = null;
 
    	boolean result=false;
    	String raqFileName = null;
		try {

			conn = new DBConn();
	    	session=conn.beginTransaction();
	    	connection=session.connection();
	    	connection.setAutoCommit(false);	    	
	    	stmt = connection.createStatement();
	    	addsql = new StringBuffer();
	    	addsql.append("update AF_Template set report_style=").append(intValue).
			append(" where Template_ID='").append(childRepId)
    			.append("' and Version_ID=").append(versionId);
	    	stmt.addBatch(addsql.toString());
	    	addsql = new StringBuffer();
	    	addsql.append("update m_child_report set report_style=").append(intValue).
			append(" where child_rep_id='").append(childRepId)
    			.append("' and Version_ID=").append(versionId);
	    	stmt.addBatch(addsql.toString());
	    	stmt.executeBatch();
	    	result = true;
	    	return result;
		}  catch (Throwable e1) {
			e1.printStackTrace();
			result = false;
			return result;
		}finally{
    		try {
				if(result == true) {
					connection.commit();
				}else{
					connection.rollback();
				}
				connection.setAutoCommit(result);

				if(stmt != null) 
					stmt.close();
				
				if(conn != null) 
					conn.closeSession();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}		
	}
	
	public Long currVal(String tableName,DBConn conn,String id){
		Connection  con =null;
		Statement stat=null;
		ResultSet rs=null;
		try {
			String sql = "select max("+id+") as rs from "+tableName;
			con =conn.openSession().connection();
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			int result = 0;
			if(rs.next())
				result = rs.getInt("rs");
			return Long.valueOf(result); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0L;
		}
	}
	
	public static AfReport getAFReportByRepId(Long repid){
		// 连接和会话对象的初始化
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session=conn.openSession();
			AfReport report = (AfReport) session.load(AfReport.class,
					repid);
			return report;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		finally {
			if (conn != null)
				conn.closeSession();
		}
	}
	
	public static boolean updateAFReport(AfReport afReport)
	{
		// 置标志result
		boolean result = false;
		// 连接和会话对象的初始化
		DBConn conn = null;
		Session session = null;
		if(afReport!=null)
		{
			try {
				conn = new DBConn();
				session = conn.beginTransaction();
				session.update(afReport);
				
				session.flush();
				result=true;
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				// 如果由连接则断开连接，结束会话，返回
				if (conn != null)
					conn.endTransaction(result);
			}
		}
		return result;
	}
	
	/***
	 * 解决由于模板问题导致载入模板失败之后的数据冗余问题
	 * 2012-06-05 卞以刚
	 * 添加报表失败 清除数据
	 * @return isRight
	 * @param rptID
	 * @return
	 */
	public static boolean cleanSQLData(String rptID,String versionId){
		boolean isRight = true;
		
		DBConn conn=null;
    	Session session=null;
    	Connection connection=null;
    	Statement stmt = null;
    	
    	try {
			conn = new DBConn();
			session=conn.beginTransaction();
			connection=session.connection();
			connection.setAutoCommit(false);	    	
			stmt = connection.createStatement();
			
			System.out.println("-----创建清库脚本。。。。。-----");
			/***清除数据sql*/
			String sql_1 = "delete from m_main_rep where REP_ID in (select REP_ID from m_child_report  where CHILD_REP_ID='"+rptID+"')";
			String sql_2 = "delete from m_child_report  where CHILD_REP_ID='"+rptID+"' and VERSION_ID='"+versionId+"'";
			String sql_3 = "delete from m_actu_rep where CHILD_REP_ID='"+rptID+"' and VERSION_ID='"+versionId+"'";
			String sql_4 = "delete from m_rep_range where CHILD_REP_ID='"+rptID+"' and VERSION_ID='"+versionId+"' ";
			String sql_5 = "delete from  AF_TEMPLATE where TEMPLATE_ID='"+rptID+"' and VERSION_ID='"+versionId+"'";
			String sql_6 = "delete from  AF_CELLINFO where TEMPLATE_ID='"+rptID+"' and VERSION_ID='"+versionId+"'";
			String sql_7 = "delete from AF_GATHERFORMULA where TEMPLATE_ID='"+rptID+"' and VERSION_ID='"+versionId+"'";
			String sql_8 = "delete from AF_TEMPLATE_CURR_RELATION where TEMPLATE_ID='"+rptID+"' and VERSION_ID='"+versionId+"'";
			String sql_9 = "delete from m_cell  where CHILD_REP_ID='"+rptID+"' and VERSION_ID='"+versionId+"'";
			String sql_10 = "delete from qd_cellinfo where TEMPLATE_ID='"+rptID+"' and VERSION_ID='"+versionId+"'";
			
			System.out.println("-----清库脚本创建成功。。。。。-----");
			stmt.addBatch(sql_1);
			stmt.addBatch(sql_2);
			stmt.addBatch(sql_3);
			stmt.addBatch(sql_4);
			stmt.addBatch(sql_5);
			stmt.addBatch(sql_6);
			stmt.addBatch(sql_7);
			stmt.addBatch(sql_8);
			stmt.addBatch(sql_9);
			stmt.addBatch(sql_10);
			
			System.out.println("-----执行清库脚本。。。。。-----");
			stmt.executeBatch();//执行
			System.out.println("-----执行成功。。。。。-----");
		} catch (Exception e) {
			isRight = false;
			System.out.println("-----执行失败。。。。。-----");
			e.printStackTrace();
		}finally{
    		try {
				if(isRight == true) {
					connection.commit();
				}else{
					connection.rollback();
				}
				connection.setAutoCommit(isRight);

				if(stmt != null) 
					stmt.close();
				
				if(conn != null) 
					conn.closeSession();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}		
		return isRight;
	}
	
	/***
	 * 银监会模板保存失败时，删除已存数据，防止数据冗余
	 * @param templateId
	 * @param versionId
	 * @return
	 */
	public static boolean clearJYHData(AFTemplateForm templateForm){
		if(templateForm==null || templateForm.getTemplateId()==null || templateForm.getVersionId()==null)
			return true;
		
		String templateId = templateForm.getTemplateId();//模板ID
		String versionId = templateForm.getVersionId();//版本
		String reportFlg = templateForm.getReportStyle();
		
		boolean isRight = true;
		
		DBConn conn=null;
    	Session session=null;
    	Connection connection=null;
    	Statement stmt = null;
    	
    	try {
			conn = new DBConn();
			session=conn.beginTransaction();
			connection=session.connection();
			connection.setAutoCommit(false);	    	
			stmt = connection.createStatement();
			
			System.out.println("-----创建清库脚本。。。。。-----");
			
			String sql_1 = "delete from AF_Template a where a.Template_ID ='"+templateId+"' and  a.Version_ID ='"+versionId+"'";
			String sql_2 = "";
			String sql_3 = "";
			String sql_4 = "";
			String sql_5 = "";
			String sql_6 = "";
			String sql_7 = "";
			String sql_8 = "";
			String sql_9 = "";
			String sql_10 = "";
			String sql_11 = "";
			String sql_12 = "";
			
			// 定义点对点报表单元格信息
			if(templateForm.getReportStyle() == null || !templateForm.getReportStyle().equals(com.fitech.gznx.common.Config.REPORT_QD)){
				sql_2 = "delete from AF_CellInfo a where a.Template_ID='"+templateId+"' and a.Version_ID='"+versionId+"'";
				stmt.addBatch(sql_2);
				// 删除1104报表单元格信息
				if(reportFlg.equals("1")){
					sql_3 = "delete from m_cell m where m.CHILD_REP_ID='"+templateId+"' and m.VERSION_ID='"+versionId+"'" ;
					stmt.addBatch(sql_3);
				}
			}else{//清单式报表
				sql_5 = "delete from qd_cellinfo q where q.Template_ID='"+templateId+"' and q.Version_ID='"+versionId+"' ";
				// 删除原有数据
				String tablename = "AF_QD_"+templateId.toUpperCase().trim();
				sql_6 = "drop table "+tablename;
				stmt.addBatch(sql_5);
				stmt.addBatch(sql_6);
			}
			/***清除数据sql*/
			//删除取数公式
			sql_4 = "delete from AF_GatherFormula a where a.Template_ID='"+templateId+"' and a.Version_ID='"+versionId+"'";
			sql_7 = "delete from af_template_curr_relation a where a.Template_ID='"+templateId+"' and a.Version_ID='"+versionId+"'";
			sql_8 = "delete from m_main_rep where REP_ID in (select REP_ID from m_child_report  where CHILD_REP_ID='"+templateId+"' and version_id='"+versionId+"')";
			sql_9 = "delete from m_child_report  where CHILD_REP_ID='"+templateId+"' and version_id='"+versionId+"'";
			sql_10 = "delete from m_actu_rep where CHILD_REP_ID='"+templateId+"' and version_id='"+versionId+"'";
			sql_11 = "delete from m_rep_range where CHILD_REP_ID='"+templateId+"' and version_id='"+versionId+"'";
			sql_12 = "delete from  AF_CELLINFO where TEMPLATE_ID='"+templateId+"' and version_id='"+versionId+"'";
			
			System.out.println("-----清库脚本创建成功。。。。。-----");
			stmt.addBatch(sql_1);		
			stmt.addBatch(sql_4);			
			stmt.addBatch(sql_7);

			
			System.out.println("-----执行清库脚本。。。。。-----");
			stmt.executeBatch();//执行
			System.out.println("-----执行成功。。。。。-----");
		} catch (Exception e) {
			isRight = false;
			System.out.println("-----执行失败。。。。。-----");
			e.printStackTrace();
		}finally{
    		try {
				if(isRight == true) {
					connection.commit();
				}else{
					connection.rollback();
				}
				connection.setAutoCommit(isRight);

				if(stmt != null) 
					stmt.close();
				
				if(conn != null) 
					conn.closeSession();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}		
		return isRight;
	}
	
	/***
	 * 数据查询
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static List<Object[]> searchData(String sql) throws Exception{
		List<Object[]> objList = null;
		DBConn conn=null;
    	Session session=null;
    	Statement stmt = null;
    	Connection connection=null;
    	
    	conn = new DBConn();
    	session = conn.openSession();
    	connection = session.connection();
    	stmt = connection.createStatement();
    
    	ResultSet rs = stmt.executeQuery(sql);
    	objList = new ArrayList<Object[]>();
    	while (rs.next()) {
    		Object[] obj = new Object[7];
			obj[0] = rs.getObject("ORG_NAME");
    		obj[1] = rs.getObject("CELL_NAME");
    		String rowName = (String)rs.getObject("ROW_NAME");
    		String colName = (String)rs.getObject("COL_NAME");
    		if(rowName==null || rowName.equals(""))
    			rowName = "--";
    		if(colName==null || colName.equals(""))
    			colName = "--";
    		obj[2] = rowName+"|"+colName;
    		obj[3] = rs.getObject("REPORT_VALUE");
    		obj[4] = rs.getObject("REP_NAME");
    		obj[5] = rs.getObject("VERSION_ID");
    		obj[6] = rs.getObject("YEAR")+"年-"+rs.getObject("TERM")+"月";
		
    		objList.add(obj);
		}
		return objList;
	}
	/***
	 * PBOC数据查询
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static List<Object[]> searchPBOCData(String sql) throws Exception{
		List<Object[]> objList = null;
		DBConn conn=null;
		Session session=null;
		Statement stmt = null;
		Connection connection=null;
		
		conn = new DBConn();
		session = conn.openSession();
		connection = session.connection();
		stmt = connection.createStatement();
		
		ResultSet rs = stmt.executeQuery(sql);
		objList = new ArrayList<Object[]>();
		while (rs.next()) {
			Object[] obj = new Object[7];
			obj[0] = rs.getObject("ORG_NAME");
			obj[1] = rs.getObject("CELL_NAME");
			String rowName = (String)rs.getObject("ROW_NAME");
			String colName = (String)rs.getObject("COL_NAME");
			if(rowName==null || rowName.equals(""))
				rowName = "--";
			if(colName==null || colName.equals(""))
				colName = "--";
			obj[2] = rowName+"|"+colName;
			obj[3] = rs.getObject("CELL_DATA");
			obj[4] = rs.getObject("REP_NAME");
			obj[5] = rs.getObject("VERSION_ID");
			obj[6] = rs.getObject("YEAR").toString()+"年-"+rs.getObject("TERM").toString()+"月";
			
			objList.add(obj);
		}
		return objList;
	}
	public static List<MCell> getMCellData(String childRepId,String versionId){
		DBConn conn = null;
		try {
			conn = new DBConn();
			Session session = conn.openSession();
			String hql = "from MCell m where m.MChildReport.comp_id.childRepId =:childRepId and m.MChildReport.comp_id.versionId =:versionId";
			Query query = session.createQuery(hql).setParameter("childRepId", childRepId).setParameter("versionId", versionId);
			return query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(conn!=null)
				conn.closeSession();
		}
		return null;
	}
}
