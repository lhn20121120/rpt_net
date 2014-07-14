package com.cbrc.smis.proc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.proc.po.MCellFormu;
import com.cbrc.smis.proc.po.ReportIn;
/**
 * 点对点式报表的方法的实现
 * 
 * @author rds
 * @date 2005-12-26 17:18
 */
public class ReportDDImpl extends ReportImpl {
	private static String termValidFlag="TERM";
	/**
	 * 需要替换为*0的字符串
	 */
	public static String needReplaceStr="/0.00,/(0.00+0.00*12.5)";
	
	/**
	 * 获得上期同单元格的值
	 * 
	 * @param conn Connection 
	 * @param reportIn ReportIn
	 * @param cellId int 单元格ID
	 * @param repFreqNm String 报表频度
	 * @return double
	 * @exception Exception 
	 */
	public static Double getPrevTermCellValue(Connection conn,ReportIn reportIn,int cellId,String repFreqNm) throws Exception{
		Double dvalue=null;
		double prevTermCellValue=0.0d;
		if(conn==null || reportIn==null || cellId<=0 || repFreqNm==null) return dvalue;
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try{
			String prevTerm=getPrevTerm(reportIn.getYear() + "-" + reportIn.getTerm(),repFreqNm);
			if(prevTerm==null) return dvalue;
			String arr[]=prevTerm.split("-");
			
			String where="child_rep_id='".toUpperCase() + reportIn.getChildRepId() + "' and " +
				"version_id='".toUpperCase() + reportIn.getVersionId() + "' and " +
				"DATA_RANGE_ID=".toUpperCase() + reportIn.getDataRangeId() + " and " +
				"org_id='".toUpperCase() + reportIn.getOrgId().trim() + "' and " +
				"cur_id=".toUpperCase() + reportIn.getCurId() + " and " +	 
				"year=".toUpperCase() + arr[0] + " and " +
				"term=" .toUpperCase()+ arr[1] + " and times=1".toUpperCase();	
			/*
			String sql="select report_value from report_in_info where rep_in_id=(select rep_in_id from report_in " +
				"where " + where + " and times=(select max(times) from report_in where " + where + ")" + 
				")";
			*/
			String sql="select report_value from report_in_info where rep_in_id=(select rep_in_id from report_in ".toUpperCase() +
				"where " + where + ")";
			sql+=" and CELL_ID="+cellId;
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs!=null && rs.next()){
				prevTermCellValue=rs.getDouble("report_value".toUpperCase());
				dvalue=new Double(prevTermCellValue);
			}
		}catch(SQLException sqle){
			throw new Exception(sqle.getMessage());
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
		}
		
		return dvalue;
	}
	
	/**
	 * 获得上年同期同单元格的值
	 * 
	 * @param conn Connection 
	 * @param reportIn ReportIn
	 * @param cellId int 单元格ID
	 * @param repFreqNm String 报表频度
	 * @return double
	 * @exception Exception 
	 */
	public static Double getLastYearSameTermCellValue(Connection conn,ReportIn reportIn,int cellId,String repFreqNm) throws Exception{
		double prevTermCellValue=0.0d;
		Double dvalue=null;
		if(conn==null || reportIn==null || cellId<=0 || repFreqNm==null) return dvalue;
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try{
			int lastYear=reportIn.getYear().intValue()-1;
			
			String where="child_rep_id='".toUpperCase()+ reportIn.getChildRepId() + "' and " +
				"version_id='".toUpperCase() + reportIn.getVersionId() + "' and " +
				"DATA_RANGE_ID=".toUpperCase() + reportIn.getDataRangeId() + " and " +
				"org_id='".toUpperCase() + reportIn.getOrgId() + "' and " +
				"cur_id=".toUpperCase() + reportIn.getCurId() + " and " +	 
				"year=".toUpperCase() + lastYear + " and " +
				"term=".toUpperCase() + reportIn.getTerm()+ " and " +	
				"times=1".toUpperCase();
			/*
			String sql="select report_value from report_in_info where rep_in_id=(select rep_in_id from report_in " +
				"where " + where + " and times=(select max(times) from report_in where " + where + ")" + 
				")";
			*/
			String sql="select report_value from report_in_info where rep_in_id=(select rep_in_id from report_in ".toUpperCase() +
				"where " + where + ")";
			
			sql+=" and CELL_ID="+cellId;
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs!=null && rs.next()){
				prevTermCellValue=rs.getDouble("report_value".toUpperCase());
				dvalue=new Double(prevTermCellValue);
			}
		}catch(SQLException sqle){
			sqle.printStackTrace();
			throw new Exception(sqle.getMessage());
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
		}
		
		return dvalue;
	}
	
	/**
	 * 更新单元格的异常标准变化值
	 * 
	 * @param conn Connection
	 * @param repInId int
	 * @param cellId int
	 * @param float prevRiseStandard
	 * @param float prevFallStandard
	 * @param float sameRiseStandard
	 * @param float sameFallStandard
	 * @return boolean 更新成功，返回true;否则，返回false
	 * @exception Exception
	 */
	public static boolean update(Connection conn,int repInId,int cellId,float prevRiseStandard,
			float prevFallStandard,float sameRiseStandard,float sameFallStandard) throws Exception{
		boolean result=false;
		
		if(conn==null) return false;
		
		Statement pstmt=null;
		ResultSet rs=null;

		try{
			String sql="update REPORT_IN_INFO set THAN_PREV_RISE="+prevRiseStandard+"," +
				"THAN_PREV_FALL="+prevFallStandard+",THAN_SAME_RISE="+sameRiseStandard+",THAN_SAME_FALL="+sameFallStandard+" " +
				"where CELL_ID="+cellId+" and REP_IN_ID="+repInId;
			
			pstmt=conn.createStatement();
			if(pstmt.executeUpdate(sql)>0) result=true;
			conn.commit();
		}catch(SQLException sqle){
			throw new Exception(sqle.getMessage());
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
		}
		
		return result;
	}

	/**
	 * 更新单元格的异常标准变化值
	 * 
	 * @param conn Connection
	 * @param repInId int
	 * @param cellId int
	 * @param float prevRiseStandard
	 * @param float prevFallStandard
	 * @param float sameRiseStandard
	 * @param float sameFallStandard
	 * @return boolean 更新成功，返回true;否则，返回false
	 * @exception Exception
	 */
	public static boolean updateValue(Connection conn,int repInId,int cellId,Float prevRiseStandard,
			Float prevFallStandard,Float sameRiseStandard,Float sameFallStandard) throws Exception{
		boolean result=false;
		
		if(conn==null) return false;
		
		Statement pstmt=null;
		ResultSet rs=null;

		try{
			String sql="update REPORT_IN_INFO ";
			if(prevRiseStandard==null){
				sql+="set THAN_PREV_RISE="+null;
			}else{
				sql+="set THAN_PREV_RISE="+prevRiseStandard.floatValue();
			}
			if(prevFallStandard==null){
				sql+=",THAN_PREV_FALL="+null;
			}else{
				sql+=",THAN_PREV_FALL="+prevFallStandard.floatValue();
			}
			if(sameRiseStandard==null){
				sql+=",THAN_SAME_RISE="+null;
			}else{
				sql+=",THAN_SAME_RISE="+sameRiseStandard.floatValue();
			}
			if(sameFallStandard==null){
				sql+=",THAN_SAME_FALL="+null;
			}else{
				sql+=",THAN_SAME_FALL="+sameFallStandard.floatValue();
			}
			sql+=" where CELL_ID="+cellId+" and REP_IN_ID="+repInId;
			
			pstmt=conn.createStatement();
			if(pstmt.executeUpdate(sql)>0) result=true;
			conn.commit();
		}catch(SQLException sqle){
			throw new Exception(sqle.getMessage());
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
		}
		
		return result;
	}
	
	/**
	 * 获得单元格的值
	 * 
	 * @param conn Connection
	 * @param repInId int
	 * @param cellId int
	 * @return float 
	 */
	public static double getCellValue(Connection conn,int repInId,int cellId) throws Exception{
		double cellValue=0.0d;
		
		if(conn==null) return cellValue;
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try{
			String sql="select report_value from report_in_info where rep_in_id=? and cell_id=?".toUpperCase();
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1,repInId);
			pstmt.setInt(2,cellId);
			rs=pstmt.executeQuery();
			if(rs!=null && rs.next()){
				cellValue=rs.getDouble("report_value".toUpperCase());
			}
		}catch(SQLException sqle){
			throw new Exception(sqle.getMessage());
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
		}
		
		return cellValue;
	}
	
	public static boolean isRegexRight(Connection conn, int repInId,
			String cellFormu, String childRepId, String versionId) throws Exception{
		int colLeft = cellFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
		int colRight = cellFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
		int regLeft = cellFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU,colRight + 1);
		int regRight = cellFormu.lastIndexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
		
		String cellName = cellFormu.substring(colLeft + 1, colRight);
		String reg = cellFormu.substring(regLeft + 1, regRight);
		String cellValue = getCellValueString(conn,repInId,childRepId,versionId,cellName);
		if(cellValue==null)
			return true;
		return Pattern.matches(reg, cellValue);
	}
	
    /**
     * 验证填报数据是否通过正则表达式验证
     * @author  gongming
     * @date    2007-09-27
     * @cellFormu   String              正则表达式
     * @cellMap     Map                 填报数据集合(key-单元格名称value单元格值)
     * @return  boolean 验证通过返回true  不通过返回false
     */
    public static boolean isRegexRight(MCellFormu mCellFormu,String cellFormu,Map cellMap) throws Exception{
        int colLeft = cellFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
        int colRight = cellFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
        int regLeft = cellFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU,colRight + 1);
        int regRight = cellFormu.lastIndexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
        
        String cellName = cellFormu.substring(colLeft + 1, colRight);
        String reg = cellFormu.substring(regLeft + 1, regRight);
        if (null == cellMap.get(cellName)){
        	mCellFormu.setSource("");
        	mCellFormu.setTarget("");
        	return false;
        }
       
        String cellValue = (cellMap.get(cellName)).toString(); 
//        //判断小数点是否超过两位
//        int point = cellValue.lastIndexOf(".");
//        if(cellValue.length() - point > 3)
//        {     
//            Double temp = null;
//            try
//            {
//                temp = Double.valueOf(cellValue);  
//            }
//            catch (NumberFormatException e)
//            {
//                return false;
//            }
//            cellValue = String.valueOf(temp.doubleValue() * 100);
//            //判断是否是小数点后只有一位小数
//            point = cellValue.lastIndexOf(".");
//            if(cellValue.length() - point == 2)
//                cellValue = cellValue + "0";
//            //判断小数位数是否超长
//            if(cellValue.length() - point > 3)
//                cellValue = cellValue.substring(0,point + 3);
//        }
        
        try{
        	Double.valueOf(cellValue);
        }catch(Exception e){
        	mCellFormu.setSource(cellValue);
        	mCellFormu.setTarget("");
        	return false;
        }
    	   
        return Pattern.matches(reg, cellValue);
    }
    
	public static String getCellValueString(Connection conn, int repInId,
			String childRepId, String versionId, String cellName) throws Exception{
		String value = null;

		if (conn == null || repInId <= 0 || childRepId == null
				|| versionId == null || cellName == null)
			return value;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "select rii.report_value from report_in_info rii where rii.rep_in_id=?".toUpperCase()
					+ " and rii.cell_id=(select mc.cell_id from m_cell mc where mc.child_rep_id=? and ".toUpperCase()
					+ " mc.version_id=? and mc.cell_name=?)".toUpperCase();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, repInId);
			pstmt.setString(2, childRepId);
			pstmt.setString(3, versionId);
			pstmt.setString(4, cellName);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				value = rs.getString("report_value".toUpperCase());
				if(value==null)
					value="";
			}
		} catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return value;
	}
    
   
    /**
     * 获取含有上报值和单元格名称数组的数组
     * @author  gongming
     * @date    2007-09-24
     * @param orclCon       Connection      数据库联接
     * @param cellNameLst   List            单元格名称集合
     * @param cellMap       Map             填报数据Map集合(key-单元格名称,value-填报值)
     * @param reportIn      ReportIn        内网表单表
     * @return  String[][]  
     * @throws Exception
     */
     public static String[][] getFormuValue(Connection orclCon,
			List cellNameLst, ReportIn reportIn, Map cellMap) throws Exception {

		if (cellNameLst == null || cellNameLst.isEmpty())
			return null;
		// 单元格表达式数组
		String formu[][] = null;

		Statement stmt = null;
		ResultSet orclRS = null;
		boolean flag = false;
		Integer dataRangeId = null;
		dataRangeId = reportIn.getDataRangeId();
		try {
			StringBuffer orclSql = null;

			int size = cellNameLst.size();
			// 初始化数组
			formu = new String[size][];
			// 查询表达式中单元格的上报报值和单元格名称
			for (int i = 0; i < size; i++) {
				stmt = orclCon.createStatement();
				String cellName = cellNameLst.get(i).toString();
				// 判断是否属于表间校验
				if (cellName.indexOf("_") != -1) {
					orclSql = new StringBuffer();
					String report[] = cellName.split("_");
					if (report.length >= 3) {
						// 判断是否是连续性校验,先获得需要比较报表的ID，然后根据报表ID获得对应单元格的数据
						if (report[0].equals(termValidFlag)) {
							int repInId=0;
							//获得报表ID的sql
							String sqlRepInId="";
							if(report[1].equals("上期")){
								if(Config.DB_SERVER_TYPE.equals("oracle")){
									sqlRepInId="SELECT REP_IN_ID FROM(" +
										"select A.REP_IN_ID from REPORT_IN A where A.ORG_ID='"+reportIn.getOrgId()+"' and A.CHILD_REP_ID='"+report[2]+"' AND A.VERSION_ID='"+report[3]+"' AND A.DATA_RANGE_ID="+dataRangeId +
										" and A.CUR_ID="+reportIn.getCurId()+"  and A.TIMES=1  AND A.CHECK_FLAG=1 AND A.YEAR+A.TERM<"+(reportIn.getYear().intValue()+reportIn.getTerm().intValue())+" order by A.YEAR desc,A.TERM desc)   " +
										"where ROWNUM=1";
								}

								if(Config.DB_SERVER_TYPE.equals("db2")){
									sqlRepInId="select A.REP_IN_ID from report_in A where A.ORG_ID='"+reportIn.getOrgId()+"' and A.CHILD_REP_ID='"+report[2]+"' AND A.VERSION_ID='"+report[3]+"' AND A.DATA_RANGE_ID="+ dataRangeId +
									"   and A.CUR_ID="+reportIn.getCurId()+" and A.TIMES=1  AND A.CHECK_FLAG=1 AND A.YEAR+A.TERM<"+(reportIn.getYear().intValue()+reportIn.getTerm().intValue())+" order by A.YEAR desc,A.TERM desc fetch first 1 rows only";
								}

								if(Config.DB_SERVER_TYPE.equals("sybase")){
									sqlRepInId="select TOP 1 A.REP_IN_ID from REPORT_IN A where A.ORG_ID='"+reportIn.getOrgId()+"' and A.CHILD_REP_ID='"+report[2]+"' AND A.VERSION_ID='"+report[3]+"' AND A.DATA_RANGE_ID="+ dataRangeId +
									"   and A.CUR_ID="+reportIn.getCurId()+" and A.TIMES=1  AND A.CHECK_FLAG=1 AND A.YEAR+A.TERM<"+(reportIn.getYear().intValue()+reportIn.getTerm().intValue())+" order by A.YEAR desc,A.TERM desc ";

								}
							}
							if(report[1].equals("年初")){
								sqlRepInId="select A.REP_IN_ID from REPORT_IN A where A.ORG_ID='"+reportIn.getOrgId()+"' and A.CHILD_REP_ID='"+report[2]+"' AND A.VERSION_ID='"+report[3]+"' AND A.DATA_RANGE_ID="+ dataRangeId +
								"   and A.CUR_ID="+reportIn.getCurId()+" and A.TIMES=1  AND A.CHECK_FLAG=1 AND A.YEAR="+(reportIn.getYear().intValue()-1)+" and A.TERM=12 order by A.REP_IN_ID";
							}
							if(report[1].equals("上年同期")){
								sqlRepInId="select A.REP_IN_ID from REPORT_IN A where A.ORG_ID='"+reportIn.getOrgId()+"' and A.CHILD_REP_ID='"+report[2]+"' AND A.VERSION_ID='"+report[3]+"' AND A.DATA_RANGE_ID="+ dataRangeId +
								"   and A.CUR_ID="+reportIn.getCurId()+" and A.TIMES=1  AND A.CHECK_FLAG=1 AND A.YEAR="+(reportIn.getYear().intValue()-1)+" and A.TERM="+reportIn.getTerm()+" order by A.REP_IN_ID";
							}
							orclRS=stmt.executeQuery(sqlRepInId);
							if(orclRS.next()){
								repInId=orclRS.getInt(1);
							}
							orclSql.append(" select a.report_value from report_in_info a ".toUpperCase());
							orclSql.append(" left join m_cell b on a.cell_id = b.cell_id ".toUpperCase());
							orclSql.append(" where b.cell_name='".toUpperCase()).append(report[4]).append("' and a.REP_IN_ID=".toUpperCase()).append(repInId);

						} else {
							if (report.length > 3) {
								try {
									if (report.length == 4&& !report[3].startsWith("(")) dataRangeId = new Integer(report[3]);
									if (report.length == 5)
										dataRangeId = new Integer(report[4]);
								} catch (Exception e) {
									dataRangeId = new Integer(0);
								}
							}

							orclSql.append(" select a.report_value from report_in_info a ".toUpperCase());
							orclSql.append(" left join m_cell b on a.cell_id = b.cell_id ".toUpperCase());
							orclSql.append(" left join report_in c on a.rep_in_id = c.rep_in_id ".toUpperCase());
							orclSql.append(" where b.cell_name='".toUpperCase()).append(report[2]).append("' and ");
							orclSql.append(" c.times = (select max(d.times) from report_in d where ".toUpperCase());
							orclSql.append(" d.child_rep_id = '".toUpperCase()).append(report[0]).append("' and ");
							//orclSql.append(" d.version_id = '".toUpperCase()).append(report[1]).append("' and ");
							orclSql.append(" d.org_id ='".toUpperCase()).append(reportIn.getOrgId()).append("' and ");
							orclSql.append(" d.year = ".toUpperCase()).append(reportIn.getYear()).append(" and ");
							orclSql.append(" d.term=".toUpperCase()).append(reportIn.getTerm()).append(" and ");
							orclSql.append(" d.data_range_id =".toUpperCase()).append(dataRangeId).append(")");
							orclSql.append(" and c.child_rep_id = '".toUpperCase()).append(report[0]).append("' and ");
							orclSql.append(" c.version_id = '".toUpperCase()).append(report[1]).append("' and ");
							orclSql.append(" c.org_id ='".toUpperCase()).append(reportIn.getOrgId()).append("' and ");
							orclSql.append(" c.year = ".toUpperCase()).append(reportIn.getYear()).append(" and ");
							orclSql.append(" c.term=".toUpperCase()).append(reportIn.getTerm()).append(" and ");
							orclSql.append(" c.data_range_id =".toUpperCase()).append(dataRangeId).append(" and ");
							orclSql.append(" c.CUR_ID = 1".toUpperCase());

						}
						String reportValue = "0.00";
						
						orclRS = stmt.executeQuery(orclSql.toString());
							if (orclRS.next()&&orclRS.getString("report_value".toUpperCase())!=null) {
							// 单元格对应的上报值
							reportValue = orclRS.getString("report_value".toUpperCase());
						}
						orclRS.close();
						stmt.close();
						flag = false;
						formu[i] = new String[] { reportValue.trim(), cellName };
					}
				} else {
					// 默认上报值防止填报数据为空
					String reportValue = "0.00";
					if (null != cellMap.get(cellName))
						reportValue = cellMap.get(cellName).toString();
					// formu[i] = new
					// String[]{(cellMap.get(cellName)).toString(),cellName};
					formu[i] = new String[] { reportValue.trim(), cellName };
				}
			}
		} catch (SQLException e) {
			flag = true;
			throw new Exception(e.getMessage());
		} finally {
			if (flag) {
				if (orclRS != null)
					orclRS.close();
				if (stmt != null)
					stmt.close();
			}
		}
		return formu;
	}
     
     /**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
     /**
      * 
		 * 获取一个上报报表所有单元格的名称和上报值键值对
		 * 
		 * @author gongming
		 * @date 2007-09-25
		 * @param orclCon
		 *            Connection 数据库连接类
		 * @param reportIn
		 *            ReportIn 内网表单表
		 * @return Map key-单元格名称 value-填报值
		 * @throws Exception
		 */
     public static Map parseCell(Connection orclCon,ReportIn reportIn) throws Exception
     {
        if(orclCon == null || reportIn == null) return null;
        Map cellMap = null;
        PreparedStatement pStmt = null;
        ResultSet plRS = null;
         try {
             String orclSql = "select b.cell_name,a.report_value from report_in_info a ".toUpperCase();
                     orclSql += " left join m_cell b on a.cell_id = b.cell_id where ".toUpperCase();
                     orclSql += " a.rep_in_id = ?".toUpperCase();
             pStmt = orclCon.prepareStatement(orclSql);
             pStmt.setInt(1,reportIn.getRepInId().intValue());
             plRS = pStmt.executeQuery();
             cellMap = new HashMap();
             while(plRS.next()){
                 String cellName = plRS.getString("cell_name".toUpperCase());// 单元格名称
                 String reportValue = plRS.getString("report_value".toUpperCase());//填报值
                 if(cellName != null && reportValue != null)
                     cellMap.put(cellName,reportValue);
             }             
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }finally{
            if(plRS != null) plRS.close();
            if(pStmt != null) pStmt.close();
        }
        return cellMap;
     }
     
     /**
     * 验证表达式是否成立
     * @author gongming
     * @date   2007-09-25
     * @param orclCon       Connection      数据库连接类
     * @param formu[]       String          拆分成左右两边的表达式数组
     * @param compareSign   String          比较的符号
     * @return  表达式成立返回true否则返回false
     * @throws Exception
     */
     public static boolean hasResult(Connection orclCon,String formu[],String compareSign) throws Exception{
         if(orclCon == null || formu == null || compareSign == null) return false;
         boolean hasResult =  false;
         Statement plStmt = null;
         ResultSet plRS = null;
         
          try {
              //表达式是否成立的统计查询语句
              
              String orclSql = "select count(1) as count1 from REPORT_IN where ";
                     orclSql += "round(("+formu[0]+"),2)"+compareSign+"round(("+formu[1]+"),2)";
              plStmt = orclCon.createStatement();             
              plRS = plStmt.executeQuery(orclSql);
             
              if(plRS.next()){
                  //判断是否有符合表达式的记录存在
                  int rsCount = Integer.parseInt(plRS.getString("count1"));
                  if(rsCount > 0) hasResult = true;
              }        
         } catch (SQLException e) {
             throw new Exception(e.getMessage());
         }finally{
             if(plRS != null) plRS.close();
             if(plStmt != null) plStmt.close();
         }
         return hasResult;
     }
     
     /**
      * 验证一个填报报表的所有单元格表达式是否成立
      * @author gongming
      * @date   2007-09-26
      * @param orclCon       Connection      数据库连接类
      * @param formuMap      Map             单元格表达集合
      *                                      -value:去除"[","]"符号后
      *                                      符合数据库比较形式的字符串
      * @return Map          (key-单元格表达式,value是否成立的boolean值)
      * @throws Exception
      */
     public static Map hasResult(Connection orclCon,Map formuMap) throws Exception{
       
        Map resultMap = null;
        Statement plStmt = null;
        ResultSet plRS = null;
        MCellFormu cellFormu = null;
        try {
            
            if (formuMap != null && !formuMap.isEmpty()) {
                resultMap = new HashMap();
                Iterator itr = formuMap.keySet().iterator();
                while (itr.hasNext()) {
                    cellFormu = (MCellFormu) itr.next();
                    
                    if (formuMap.get(cellFormu) != null) {
                    	String formu = (formuMap.get(cellFormu)).toString();
                    	//将分母为0 的数改为0；
                    	if(needReplaceStr!=null){
	                    	String[] needReplaceStrs=needReplaceStr.split(",");
	                    	for(int i=0;i<needReplaceStrs.length;i++){
	                    		while(formu.indexOf(needReplaceStrs[i])>-1){
	                        		formu=formu.substring(0,formu.indexOf(needReplaceStrs[i])) + "*0.00 " 
	                        		+ formu.substring(formu.indexOf(needReplaceStrs[i])+needReplaceStrs[i].length()); 
	    		
	                        	}
	                    	}
                    	}
                        	
                        String orclSql = "select count(1) as count1 from CALCULATE_TABLE where ";
                                orclSql += formu;
                        try {
                            plStmt = orclCon.createStatement();
                            plRS = plStmt.executeQuery(orclSql);
                            if (plRS.next()) {
                                // 判断是否有符合表达式的记录存在
                                int rsCount = Integer.parseInt(plRS.getString("count1"));
                                if (rsCount > 0) {
                                    resultMap.put(cellFormu, new Boolean(true));
                                } else {
                                    resultMap.put(cellFormu, new Boolean(false));
                                }
                            }
                            plRS.close();
                            
                            plStmt.close();
                            plRS = null;
                            plStmt= null;
                        } catch (SQLException e) {
                            resultMap.put(cellFormu,new Boolean(false));
                            continue;
                        }                        
                    }
                }
                formuMap.clear();
            }
        } catch ( Exception e) {            
        	e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            if (plRS != null)
                plRS.close();
            if (plStmt != null)
                plStmt.close();
        }
        return resultMap;
    }
     

}
