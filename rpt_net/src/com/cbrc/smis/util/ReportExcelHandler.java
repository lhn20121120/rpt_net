package com.cbrc.smis.util;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

import jxl.CellType;

import net.sf.hibernate.Session;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.entity.Cell;
import com.cbrc.smis.jdbc.FitechConnection;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.util.DateFromExcel;
import com.fitech.gznx.util.POI2Util;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.INormalCell;
import com.runqian.report4.util.ReportUtils;

/**
 * Excel数据入库
 * 
 * @author Yao
 * 
 */
public class ReportExcelHandler{
	private static FitechException log = new FitechException(ReportExcelHandler.class);
	Locale LOCALE = Locale.CHINA;
	
    //add by gongming 2008-01-13
    private static String GET_TEMPLATE_INFO_SQL = "select child_rep_id,version_Id from report_in where rep_in_id=".toUpperCase();
    //add by gongming 2008-01-13
    private static String GET_TEMPLATE_CELL_SQL = "select report_in.rep_in_id,M_Cell.cell_id ,".toUpperCase()+
                                                   " M_Cell.Cell_Name,M_Cell.data_type,M_Cell.row_id,M_Cell.col_id ".toUpperCase()
                                                + "from M_Cell left join report_in ".toUpperCase()
                                                + "on report_in.child_rep_id = M_Cell.Child_Rep_Id ".toUpperCase()
                                                + "and report_in.version_id = M_Cell.Version_Id ".toUpperCase()
                                                + "where report_in.rep_in_id=".toUpperCase();
	//add by gongming 2008-01-13
    private static PropertyResourceBundle resourceBundle = (PropertyResourceBundle) 
    				PropertyResourceBundle.getBundle("com/cbrc/smis/excel/OffsetResources_zh_CN");
	/* 报表ID */
	private Integer repInId;
	/* 报表路径 */
	private String excelFilePath;
	/* 报表对应模版的编号 */
	private String childRepId;
	/* 报表对于模版的版本号 */
	private String versionId;
	/* 报表名称 */
	public String title="";
	/* 报表附注名称 */
	public String subTitle="";
	/* 单元格列表 */
	private List cellList=null;
	/**
	 * 法人Excel模板对应PDF模板的起始行的偏移量
	 */
	private String REPORTFILE = "com/cbrc/smis/excel/OffsetResources";

	private ReportExcelHandler(){
	}

	/**
	 * 构造函数
	 * 
	 * @param reptId
	 */
	public ReportExcelHandler(Integer repInId, String excelFilePath) throws Exception{
		if (repInId == null || repInId.equals(new Long(0)))
			throw new IllegalArgumentException("构造参数错误！");
		else{
			this.repInId = repInId;
			this.excelFilePath = excelFilePath;
			FitechConnection connFactory = null;
			Connection conn = null;
			Statement stmt  = null;
			ResultSet rs = null;
			try{
				connFactory = new FitechConnection();
				conn = connFactory.getConn();
				conn.setAutoCommit(false);
				stmt = conn.createStatement();
				rs = stmt.executeQuery(GET_TEMPLATE_INFO_SQL + repInId);
				if (rs.next()){
					this.childRepId = rs.getString(1);
					this.versionId = rs.getString(2);
				}
				else
					throw new IllegalArgumentException("没有该模板信息存在！");
			}catch (Exception e){
				log.printStackTrace(e);
			}finally{
				if(rs!=null)
					rs.close();
				if(stmt!=null)
					stmt.close();
				if (conn != null)
					conn.close();
				if(connFactory!=null)  connFactory.close();
			}
		}
		try {
			if(excelFilePath!=null){
                cellList=this.getDataFromExcels(repInId.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 构造函数
	 * 
	 * @param reptId
	 */
	public ReportExcelHandler(Integer repInId, String excelFilePath,String produceExcelPath) throws Exception{
		if (repInId == null || repInId.equals(new Long(0)))
			throw new IllegalArgumentException("构造参数错误！");
		else{
			this.repInId = repInId;
			this.excelFilePath = excelFilePath;
			FitechConnection connFactory = null;
			Connection conn = null;
			Statement stmt  = null;
			ResultSet rs = null;
			try{
				connFactory = new FitechConnection();
				conn = connFactory.getConn();
				conn.setAutoCommit(false);
				stmt = conn.createStatement();
				rs = stmt.executeQuery(GET_TEMPLATE_INFO_SQL + repInId);
				if (rs.next()){
					this.childRepId = rs.getString(1);
					this.versionId = rs.getString(2);
				}
				else
					throw new IllegalArgumentException("没有该模板信息存在！");
			}catch (Exception e){
				log.printStackTrace(e);
			}finally{
				if(rs!=null)
					rs.close();
				if(stmt!=null)
					stmt.close();
				if (conn != null)
					conn.close();
				if(connFactory!=null)  connFactory.close();
			}
		}
		try {
			if(excelFilePath!=null){
				POI2Util.excelFormulaEval(new File(this.excelFilePath));
                cellList=this.getDataFromExcels(repInId.toString());
                
                UtilForExcelAndRaq.copyDataToExcel(produceExcelPath, cellList
                		,this.childRepId,this.versionId );
                
                this.excelFilePath = produceExcelPath;
                
                //打开导出的Excel，重新计算所有的公式区域并保存
      			POI2Util.excelFormulaEval(new File(this.excelFilePath));
      			
                cellList=this.getDataFromExcels(repInId.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 拷贝Excel数据进数据库
	 * 
	 * @return
	 * 
	 */
	public boolean copyExcelToDB(boolean isAddTrace) throws Exception{
		boolean result = false;
		Connection conn = null;
		FitechConnection connFactory = null;
		Statement stmt = null;
		Statement stmt1= null;
		try
		{
			connFactory = new FitechConnection();
			//List celllist = this.getDataFromExcel();
			if (cellList != null)
			{
				conn = connFactory.getConn();
				conn.setAutoCommit(false);
				stmt = conn.createStatement();
				stmt1 = conn.createStatement();
				
				// 删除原有数据
				stmt.addBatch("delete from report_in_info where rep_in_id=" + this.repInId);
				stmt.executeBatch();
				conn.commit();
				// 插入新数据
				for (int i = 0; i < cellList.size(); i++)
				{
					double finalData = 0;
					int sumData =0;
					Cell cell = (Cell) cellList.get(i);
					
					//根据repInId查询数据痕迹表 查询出template_ID ,version_ID 和cell_name
					//根据template_ID,version_ID 和cell_name查询af_cellinfo表 得出cell_id
					//根据repInId和cell_name查询出单元格修改数据总和
					//将当前值和修改数据总和的值相加
					//查询数据痕迹
					if(isAddTrace){
						String sumFormu = "";
						if(Config.DB_SERVER_TYPE.equals("oracle")){
							sumFormu = " sum(t.change_data) ";
						}
						if(Config.DB_SERVER_TYPE.equals("db2")){
							sumFormu = " sum(decimal(t.change_data)) ";
						}
						if(Config.DB_SERVER_TYPE.equals("sqlserver")){
							sumFormu = " sum(convert(float,t.change_data)) ";
						}
						String henjiSQL = "select "+sumFormu+" from af_data_trace t " +
						  "left join report_in r on t.rep_in_id=r.rep_in_id " +
						  "left join m_cell a " +
						  "on r.child_rep_id=a.child_rep_id and r.version_id=a.version_id and a.cell_name=t.cell_name " +
						  "where t.cell_name='"+cell.getCellName()+"' and t.status=0 and t.rep_in_id="+this.repInId+"";
						//取出调整值
						ResultSet rs = stmt1.executeQuery(henjiSQL);
						
						if(rs.next()){
							sumData= rs.getObject(1)==null?0:rs.getInt(1);
							System.out.println(sumData);
						}
					}
					String insertSQL = "";
					try{
						if(Config.ISADDTRACE){
							finalData = Double.valueOf(cell.getCellValue())+sumData;
							insertSQL = "insert into report_in_info (Rep_In_ID,Cell_ID,Report_Value) values("
								+ cell.getRepInId() + "," + cell.getCellId() + ",'" + String.format("%1$.2f",finalData) + "')";
						}else{
							insertSQL = "insert into report_in_info (Rep_In_ID,Cell_ID,Report_Value) values("
								+ cell.getRepInId() + "," + cell.getCellId() + ",'" + cell.getCellValue() + "')";
						}
					}catch (java.lang.NumberFormatException ne){
						insertSQL = "insert into report_in_info (Rep_In_ID,Cell_ID,Report_Value) values("
							+ cell.getRepInId() + "," + cell.getCellId() + ",'" + cell.getCellValue() + "')";
					}
					
		
				 	stmt.addBatch(insertSQL);
				}


				stmt.executeBatch();
				conn.commit();
				
				
				result = true;
			}

		}catch (Exception e){
			result = false;
			conn.rollback();
			log.printStackTrace(e);
		}finally{
			if(stmt!=null)
				stmt.close();
			if(stmt1!=null)
				stmt1.close();
			if(conn!=null){
				conn.close();
				conn = null;
			}
/*			if(connFactory!=null){
				connFactory.close();
			}*/
		}
		return result;
	}

	 /**
	  * 获得模版单元格信息	
	  * @return
	  * @throws Exception
	  *
	  */
	public List getCellList() throws Exception{
		FitechConnection connFactory = null;
		Connection conn = null;

		List celllist = new ArrayList();
		Statement stmt = null;
		ResultSet rst = null;
		
		try{
			connFactory = new FitechConnection();
			conn = connFactory.getConn();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			rst = stmt.executeQuery(GET_TEMPLATE_CELL_SQL+repInId);
			while (rst.next()){
				Cell cell = new Cell();
				cell.setCellId(rst.getInt("cell_id".toUpperCase()));
				cell.setCellName(rst.getString("cell_name".toUpperCase()));
				cell.setDataType(rst.getInt("data_type".toUpperCase()));
				cell.setRepInId(rst.getInt("rep_in_id".toUpperCase()));
				cell.setColId(rst.getString("col_id".toUpperCase()));
				cell.setRowId(rst.getInt("row_id".toUpperCase()));
				celllist.add(cell);
			}
		}catch (Exception e){
			log.println("报表文件路径有问题!");
			log.printStackTrace(e);
			return null;
		}finally{
			if(rst!=null)
				rst.close();
			if(stmt!=null)
				stmt.close();
			if (conn != null)
				conn.close();
			if(connFactory!=null) connFactory.close(); 
		}
		return celllist;
	}

	/**
	 * 获得单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public String getCellasStr(jxl.Cell cell){
		if(cell==null)
			return "";
		String result = "";
		if (cell == null)
			return result;
		String contents = cell.getContents();
		CellType cellType = cell.getType();
		
		if(cellType.equals(CellType.NUMBER)||cellType.equals(CellType.NUMBER_FORMULA))
		{
			boolean isPresentNumber = false;
			/*千位分割符数据处理*/
			if (contents.indexOf(",") > -1)
				contents = contents.replaceAll(",", "");
			/*百分数数据处理*/
			if (contents.indexOf("%") > -1 && contents.indexOf("%") == contents.length() - 1){
				isPresentNumber = true;
				contents = parsePresentToDouble(contents);
				//contents = contents.substring(0, contents.indexOf("%"));
				/*Double value = new Double(contents.substring(0, contents.indexOf("%")));
				contents = String.valueOf(FitechUtil.doubleDivide(value,new Double(100.0)));*/
			}
			/*科学计数法数据处理*/
			if(contents.indexOf('E')>-1||contents.indexOf('e')>-1){
				try
				{
					//如果是数字格式将其格式化为小数点后两位
					if(!isPresentNumber)
						contents = new java.text.DecimalFormat("##0.00").format(Double.parseDouble(contents)).toString();
	    		}
				catch(Exception e)
				{
	    			e.printStackTrace();
	    		}
			}
		}
		return contents;
	}
    /**
     * 获得模版单元格信息
     * @author gongming
     * @date   2008-01-13
     * @return     List
     * @throws Exception
     *
     */
   public List getCellList(String repInId) throws Exception{
       FitechConnection connFactory = null;
       Connection conn = null;

       List celllist = new ArrayList();
       Statement stmt = null;
       ResultSet rst = null;
       
       try{
           connFactory = new FitechConnection();
           conn = connFactory.getConn();
           conn.setAutoCommit(false);
           
           stmt = conn.createStatement();           
           rst = stmt.executeQuery(GET_TEMPLATE_CELL_SQL + repInId);
           while (rst.next()){
               Cell cell = new Cell();
               cell.setCellId(rst.getInt("cell_id"));
               cell.setCellName(rst.getString("cell_name"));
               cell.setDataType(rst.getInt("data_type"));
               cell.setRepInId(rst.getInt("rep_in_id"));
               cell.setColId(rst.getString("col_id"));
               cell.setRowId(rst.getInt("row_id"));
               celllist.add(cell);
           }
              
       }catch (Exception e){
           log.println("报表文件路径有问题!");
           log.printStackTrace(e);
           return null;
       }finally{
           if(rst!=null)
               rst.close();
           if(stmt!=null)
               stmt.close();
           if (conn != null)
               conn.close();
           if(connFactory!=null) connFactory.close();
       }
       return celllist;
   }
   public static Map getColName(String repInId)throws Exception{
	    DBConn conn = null;
		Session session = null;
		Map result = new HashMap();
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "select ri.MChildReport.comp_id.childRepId , ri.MChildReport.comp_id.versionId from ReportIn ri where ri.repInId=" + repInId;
			Object[] tv = (Object[])session.find(hql).get(0);
			String templateId = (String)tv[0];
			String versionid = (String)tv[1];
			hql = "select c.colName,c.cellName from AfCellinfo c where c.templateId='" + tv[0] + "' and c.versionId='" + versionid + "'";
			List<Object[]> list = session.createQuery(hql).list();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Object[] os = list.get(i);
					result.put(os[1], os[0]);
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			result = new HashMap();
		} finally {
			if (session != null)
				conn.closeSession();
		}
		return result;
   }
   /**
    * 解析Excel并获得所需数据
    * @author gongming
    * @date   2008-01-13
    * @return
    * 
    */
   private List getDataFromExcels(String repInId) throws Exception{      
       List result = new ArrayList();
       //List cellList = this.getCellList();
       List cellList = getCellList(repInId);
       Map<String,String> colMap = getColName(repInId);
       FileInputStream inStream = null;
       try{
           File inputWorkbook  = new File(excelFilePath);
           if(!inputWorkbook.exists()){
               return null;
           } 
           inStream = new FileInputStream(this.excelFilePath);
           HSSFWorkbook workbook = new HSSFWorkbook(inStream);        
           HSSFSheet sheet = workbook.getSheetAt(0);
           HSSFRow rowTitle = sheet.getRow(0);
          
           if (rowTitle!=null&&rowTitle.cellIterator().hasNext()&& null != rowTitle.getCell((short)0).getStringCellValue()) {
               title=rowTitle.getCell((short)0).getStringCellValue().replaceAll(" ", "");
               if(title.indexOf("GF04利润表")>-1){
                   rowTitle=sheet.getRow(2);
                   if(rowTitle!=null&&rowTitle.cellIterator().hasNext()&& null != rowTitle.getCell((short)0).getStringCellValue()){
                       if(rowTitle.getCell((short)0).getStringCellValue().indexOf("附注项目")>-1){
                           title="GF04利润表附注";
                       }
                   }
               }
               if (title==null||title.equals("")) {
            	   rowTitle=sheet.getRow(1);
                   title=rowTitle.getCell((short)1).getStringCellValue().replaceAll(" ", "");
               }
           }else {
               rowTitle=sheet.getRow(1);
               if (rowTitle!=null&&rowTitle.cellIterator().hasNext()&& null != rowTitle.getCell((short)1).getStringCellValue()) {
                   title=rowTitle.getCell((short)1).getStringCellValue().replaceAll(" ", "");
               }               
           }
           
           
           int subTitleCol= 0;
           int subTitleRow= 2;
           HSSFRow row1 = sheet.getRow(subTitleRow);
          
	       if (null != row1.getCell((short)subTitleCol)&&row1.getCell((short)subTitleCol).getCellType()==HSSFCell.CELL_TYPE_STRING) {
	    	   subTitle=row1.getCell((short)subTitleCol).getStringCellValue().trim();
	       }
           
           if (sheet == null)
               return null;
           int mergeRegions = sheet.getNumMergedRegions();
          
           int size = cellList.size();
           for (int i = 0; i < size; i++){
               Cell cell = (Cell) cellList.get(i);
               if (cell.getCellName() != null){
                   // 处理法人报表的Excel偏移量问题
                   int row = cell.getRowId() - 1 + getOffsetRow(this.childRepId, this.versionId);
                   short col = (short) this.convertColStringToNum(cell.getColId());
                   HSSFCell excelcell = null;
                   try{
                	   excelcell = sheet.getRow(row).getCell(col);
                	   boolean isMerge = false;
                	   for (int j = 0; j < mergeRegions; j++) {
                    	   Region rg = sheet.getMergedRegionAt(j);
                    	   boolean res = rg.contains(row, col);
                    	   if(res){
                    		   isMerge = true;
                    		   break;
                    	   }
            		}
                	   if(isMerge)continue;
                       //System.out.println(cell.getCellName()+":value:"+excelcell.getNumericCellValue());
                	   String cellValue = this.getCellasString(excelcell);
                       if(!StringUtil.isEmpty(cellValue)){
                           cell.setCellValue(cellValue.trim());
                       }else{
                    	   if(cellValue!=null && isSetZero(excelcell,colMap,cell)){//判断是否需要set为0.00,
                    		   cell.setCellValue("0.00");
                    	   }else{
                    		   continue; 
                    	   }
                       }
                       
                       result.add(cell);
                   }catch(Exception ex){}
               }
           }
       }catch (Exception e){
           log.println("读取报表文件出现错误!");
           log.printStackTrace(e);
           result = null;
       }finally{
           if(inStream!=null)
               inStream.close();
       }
       return result;
   }
   /**
    * 判断单元格是否要设置为0.00，判断的条件是除了灰显单元格和最大十家报表的客户名称，客户代码以外都可以设为0.00
     * @Title: isSetZero 
     * @author: CHENBING
     * @date: Nov 13, 2014  4:42:27 PM
     * @param excelcell
     * @param colMap
     * @param cell
     * @return boolean
    */
   public boolean isSetZero(HSSFCell excelcell,Map<String,String> colMap,Cell cell){
	   boolean result = true;
//	   String[] templateIds = {"G13","G14","G15","G23","G24","GF13","GF14","GF15","GF23","GF24"};
	   String colName = colMap.get(cell.getCellName());
	   if(excelcell.getCellStyle().getFillForegroundColor()==16 || excelcell.getCellStyle().getFillForegroundColor()==17
			   || colName.trim().endsWith("名称")||
			   colName.trim().endsWith("代码")){
		   result = false;
	   }
	   return result;
   }
   
   /**
    * 获得单元格的值
    * @author gongming
    * @date   2008-01-13
    * @param cell
    * @return
    */
   private String getCellasString(HSSFCell cell){
       if(cell==null)
           return "";
       String result = "";
       if (cell == null)
           return result;
       //String contents = cell.getContents();
       //CellType cellType = cell.getType();
       String contents = null;
       if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
    	   if(!StringUtil.isEmpty(cell.getStringCellValue())
    			   && cell.getStringCellValue().indexOf("%")>-1){
    		   cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    	   }
       }
       if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA ||  cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
           try{
               contents = String.valueOf(cell.getNumericCellValue());
               if("0.0".equals(contents))contents = "0.00";
           }catch(Exception e){
               contents = "0.00";
           }
           boolean isPresentNumber = false;
           /*千位分割符数据处理*/
           if (contents.indexOf(",") > -1)
               contents = contents.replaceAll(",", "");
           /*百分数数据处理*/
           if (contents.indexOf("%") > -1 && contents.indexOf("%") == contents.length() - 1)
           {
               isPresentNumber = true;
               contents = parsePresentToDouble(contents);
               //contents = contents.substring(0, contents.indexOf("%"));
               /*Double value = new Double(contents.substring(0, contents.indexOf("%")));
               contents = String.valueOf(FitechUtil.doubleDivide(value,new Double(100.0)));*/
           }
           /*科学计数法数据处理*/
           if(contents.indexOf('E')>-1||contents.indexOf('e')>-1){
               try{
                   //如果是数字格式将其格式化为小数点后两位
                   if(!isPresentNumber)
                       contents = new java.text.DecimalFormat("##0.000000").format(Double.parseDouble(contents)).toString();
               }catch(Exception e){
                   e.printStackTrace();
               }
           }
           if(!isPresentNumber)
        	   contents = com.fitech.gznx.util.ExcelUtil.subZeroAndDot(contents);//删除多余0
           if(contents.equals("NaN")){
        	   contents = "0.00";
           }
       }else if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
           contents = cell.getStringCellValue();
       }else if(cell.getCellType() ==  HSSFCell.CELL_TYPE_BLANK){
           contents = null;
       }
     
       return contents;
   }
	/**
	 * 将百分数转换位小数	
	 * @param presentNumber
	 * @return
	 *
	 */
	private String parsePresentToDouble(String presentNumber){
		String contents = presentNumber.substring(0, presentNumber.indexOf("%"));
		String sign = "";
		if(contents.substring(0,1).equals("+")||contents.substring(0,1).equals("-")){
			sign =String.valueOf(contents.charAt(0));
			contents = contents.substring(1);
		}
		int dotIndex =  contents.indexOf(".");
		
		if(dotIndex==-1)
			contents = "0."+contents;
		else if(dotIndex==0)
			contents = "0.00"+contents.substring(1);
		else if(dotIndex==1)
			contents = "0.0"+contents.substring(0,1)+contents.substring(2);
		else if(dotIndex==2)
			contents = "0."+contents.substring(0,2)+contents.substring(3);
		else {
			StringBuffer contentsStr = new StringBuffer(contents);
			contentsStr.insert(dotIndex-2, ".");
			contentsStr.deleteCharAt(dotIndex+1);
			contents = contentsStr.toString();
		}
		
		return sign+contents;
	}
	/**
	 * 将列号转换为数字
	 * 
	 * @param ref
	 * @return
	 * 
	 */
	public int convertColStringToNum(String ref){
		int retval = 0;
		int pos = 0;
		for (int k = ref.length() - 1; k > -1; k--){
			char thechar = ref.charAt(k);
			if (pos == 0)
				retval += Character.getNumericValue(thechar) - 9;
			else
				retval += (Character.getNumericValue(thechar) - 9) * (pos * 26);
			pos++;
		}

		return retval - 1;
	}
	
	/**
	 * 获取Excel模板对应PDF模板的起始行的偏移量
	 * 
	 * @author jcm
	 * @return int 偏移量
	 */
	public int getOffset(){
		int offset = 0;
		
		if(this.childRepId == null || this.versionId == null)
			return offset;
		
		String _offset = getValue(REPORTFILE,this.childRepId.trim() + this.versionId.trim());
		if(_offset != null)
			try{
				offset = Integer.parseInt(_offset.trim());
			}catch(Exception ex){
				offset = 0;
			}
		return offset;
	}
	/**
	 * 从资源文件中，根据主键获取其值
	 * 
	 * @param resourcesFile
	 *            String 资源文件
	 * @param key
	 *            String 主键
	 * @return String 健的值
	 */
	private String getValue(String resourcesFile, String key){
		if (resourcesFile == null || key == null)
			return null;

		MessageResources resources = MessageResources.getMessageResources(resourcesFile);
		String value = resources.getMessage(this.LOCALE, key);

		return value == null ? null : value.trim();
	}
		
	   /**
     * 获取Excel模板对应PDF模板的起始行的偏移量 
     * @param   childRepId      String 报表编号
     * @return  int
     */
    private int getOffsetRow(String childRepId, String versionId){
        int offset = 0;

        if (childRepId == null || versionId == null)
            return offset;

        String _offset = getValue(childRepId.trim() + versionId.trim());

        if (_offset != null){
            try{
                offset = Integer.parseInt(_offset.trim());
            }catch (Exception e){
                offset = 0;
            }
        }

        return offset;
    }
    
    /**
     * 在资源文件中，根据主键获取其值
     * @author gongming
     * @date   2008-01-13
     * @param    key        String 主键
     * @return  String 健的值
     */
    private String getValue(String key){
        if (key == null)  return null;
            
        String value = null;
        try{
            value = resourceBundle.getString(key);
        }catch (MissingResourceException e){
            value = null;
        }
        return value == null ? null : value;
    }
}
