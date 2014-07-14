package com.cbrc.smis.db2xml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Map;
import java.util.ResourceBundle;

import com.cbrc.smis.adapter.StrutsListingTableDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.jdbc.FitechConnection;
import com.cbrc.smis.util.FitechException;

/**
 * 为Db2Xml类提供服务
 * 
 * @author 姚捷
 * 
 */
public class Db2XmlUtil {
	private static FitechException log = new FitechException(Db2XmlUtil.class);

	private FitechConnection connectionFactory = new FitechConnection();

	/**
	 * 点对点报表
	 */
	public static int P2P_PEPORT = 1;

	/**
	 * 清单报表
	 */
	public static int BILL_REPORT = 2;

	/**
	 * 取得需要转xml的报表列表
	 * 
	 * @return List 报表列表集合
	 */
	public List get_NeedToXmlReps() {
		return StrutsReportInDelegate.getNeedToXmlReps();
	}

	/**
	 * 获得该报表的类型（清单式 or 点对点式）
	 * 
	 * @param childReportId
	 *            String 子报表id
	 * @param versionId
	 *            String 版本号id
	 * @return 1 点对点式 2 清单式
	 */
	public int get_PeportStyle(String childReportId, String versionId) {
		return StrutsMChildReportDelegate.getReportStyle(childReportId,
				versionId);
	}

	/**
	 * 根据点对点实际子报表id，查询出它所对应的记录
	 * 
	 * @param reportInId
	 *            String 实际子报表id
	 * @return List 该实际子报表的内容
	 */
	public List get_P2PReport_Data(Integer reportInId) {
		return StrutsReportInInfoDelegate.getReportData(reportInId);
	}

	/**
	 * 根据实际子报表Id获取报表的列信息
	 * 
	 * @author rds
	 * @date 2005-17:36
	 * 
	 * @param childRepId
	 *            实际子报表ID
	 * @param versionId
	 *            String 版本号
	 * @param exceptCells
	 *            String 除外的列
	 * @return List
	 */
	public List getP2PReportCols(String childRepId, String versionId,
			String exceptCells) {
		if (childRepId == null || versionId == null)
			return null;

		return StrutsReportInInfoDelegate.getCols(childRepId, versionId,
				exceptCells);
	}

	/**
	 * 根据清单式实际子报表id，查询出它所对应的记录
	 * 
	 * @param reportInId
	 * @return
	 */
	public ResultSet get_BillReport_Data(Integer reportInId,String childReportId, String versionId) {
		String existTypeTable = "S3600,S3209,S3402";
		ResultSet rs = null;
		// FitechConnection connectionFactory = new FitechConnection();
		Connection conn = null;
		String cols = null;

		try {
			conn = connectionFactory.getConn();
			String tableName = this.get_BillReport_TbName(childReportId,versionId);

			ResourceBundle resource = ResourceBundle.getBundle("com.cbrc.smis.db2xml.QDColsResources");

			try {
				cols = resource.getString(childReportId + versionId);
			} catch (Exception e) {
				cols = null;
			}

			if ((tableName != null && !tableName.equals(""))
					&& (cols != null && !cols.equals(""))) {
				String sql = "select "
						+ cols
						+ " from (select "
						+ cols
						+ ",(case replace(col1,' ','') when '' then 999998 when '合计' then 999999 else cast(col1 as integer) end) as seq "
						+ " from "
						+ tableName
						+ " where Rep_In_ID="
						+ reportInId
						+ " order by "
						+ (existTypeTable.indexOf(childReportId) > -1 ? " type,"
								: "") + " seq) as t";
				// System.out.println("[Db2XmlUtil]sql:" + sql);
				PreparedStatement pstmt = conn.prepareStatement(sql);
				// pstmt.setString(1,tableName);
				// pstmt.setInt(1,reportInId.intValue());
				rs = pstmt.executeQuery();
			}
		} catch (SQLException e) {
			rs=null;
			log.printStackTrace(e);
			connectionFactory.close();
		}

		return rs;
	}

	/**
	 * 根据清单式报表的id和版本号，取得该清单式报表所对应的数据库表名
	 * 
	 * @param childReportId
	 *            String 子报表id(清单式的)
	 * @param versionId
	 *            String 版本号id
	 * @return 返回该清单式报表所对应的数据库表名
	 */
	public String get_BillReport_TbName(String childReportId, String versionId) {
		try {
			return StrutsListingTableDelegate.getTalbeName(childReportId,
					versionId);
		} catch (Exception e) {
			log.printStackTrace(e);
		}
		return "";
	}

	/**
	 * 生成xml文件后，改变数据库中的报送数据仓库标志
	 * 
	 * @param reportId
	 *            Integer 实际报表id
	 * @return 是否设置成功
	 */
	public boolean setReportDataWarehouseFlag(Integer reportId) {
		return StrutsReportInDelegate.setReportDataWarehouseFlag(reportId);
	}

	/**
	 * 取得该特殊报表中的各行的名字(无重复)
	 * 
	 * @param reportData
	 *            List 包含查询出的该报表的所有数据
	 */
	public List get_specialReportRowList(List reportData) {
		/** 筛选出不重复的行号 */
		HashSet rowNames = new HashSet();
		String rowId = "";
		for (int i = 0; i < reportData.size(); i++) {
			rowId = ((P2P_Report_Data) reportData.get(i)).getCellRow();
			rowNames.add(Integer.valueOf(rowId));
		}

		Iterator it = rowNames.iterator();
		List rowNameSort = new ArrayList();

		while (it.hasNext()) {
			rowNameSort.add((Integer) it.next());

		}
		/** 行号排序 */
		Collections.sort(rowNameSort);

		/*
		 * for(int i=0;i<rowNameSort.size();i++) { // System.out.println("Row "+
		 * rowNameSort.get(i)); }
		 */

		return rowNameSort;
	}

	/**
	 * 判断报表的某单元格是不是百分比单元格
	 * 
	 * @param childRepId  子报表id
	 * @param versionId  版本号id
	 * @param cellName  单元格名称
	 * @return boolean 是百分比单元格则返回true
	 */
	public boolean isPercentCell(String childRepId, String versionId,
			String cellName) {
		boolean result = false;
		ResultSet rs = null;
		// FitechConnection connectionFactory = new FitechConnection();
		Connection conn = null;

		try {
			conn = connectionFactory.getConn();
			if (childRepId != null && !childRepId.equals("")
					&& versionId != null && !versionId.equals("")
					&& cellName != null && !cellName.equals("")) {
				String sql = "select * from cell_percent where child_rep_id=? and version_id=? and cell_name=?";
				PreparedStatement pstmt = conn.prepareStatement(sql.toUpperCase());
				pstmt.setString(1, childRepId);
				pstmt.setString(2, versionId);
				pstmt.setString(3, cellName);
				rs = pstmt.executeQuery();
				if (rs.next())
					result = true;

			}
		} catch (SQLException e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				connectionFactory.close();
		}
		return result;
	}
	
	/**
	 * 转换成百分数
	 * 
	 * @param value
	 * @return
	 */
	public String toPercent(String value) {
		String result = "";
		if (value != null && !value.equals("")) {
			try {
				double oldNum = Double.parseDouble(value);
				double newNum = oldNum / 100;
				result = String.valueOf(new Double(newNum).floatValue());
			} catch (NumberFormatException e) {
				result = "";
			}
		}

		return result.equals("")? Db2Xml.DEFAULTNUMBERVALUE : result;
	}

	/**
	 * 判断报表的某单元格是不是百分比单元格
	 * 
	 * @param childRepId  子报表id
	 * @param versionId  版本号id
	 * @param cellName  单元格名称
	 * @return boolean 是百分比单元格则返回true
	 */
	public Map getPercentCells(String childRepId, String versionId) {
		HashMap resHM=null;
		ResultSet rs = null;

		Connection conn = null;

		try {
			conn = connectionFactory.getConn();
			if (childRepId != null && !childRepId.equals("") && versionId != null && !versionId.equals("")) {
				String sql = "select cell_name from cell_percent where child_rep_id=? and version_id=?";
				PreparedStatement pstmt = conn.prepareStatement(sql.toUpperCase());
				
				pstmt.setString(1, childRepId);
				pstmt.setString(2, versionId);
				
				rs = pstmt.executeQuery();
				if (rs!=null){
					resHM=new HashMap();
					while(rs.next()){
						resHM.put(rs.getString(1),rs.getString(1));
					}
				}
				rs=null;
			}
		} catch (SQLException e) {
			resHM=null;
			log.printStackTrace(e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return resHM;
	}
	
	/**
	 * 测试方法
	 * 
	 * @param args
	 * @return void
	 */
	public static void main(String[] args) {
		// System.out.println(new Db2XmlUtil().toPercent("48.2100"));
	}

	/**
	 * 关闭数据库连接
	 * 
	 */
	public void closeConnection() throws Exception {
		connectionFactory.close();
	}

	/**
	 * 获得将定点的报表生成T2类型的接口文件时，在合计报表部分中排除的列信息
	 * 
	 * @author rds
	 * @date 2006-01-09
	 * 
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号
	 * @return String
	 */
	public static String getExceptCells(String childRepId, String versionId) {
		String exceptCells = "";

		try {
			ResourceBundle resourceBundle = ListResourceBundle.getBundle("com.cbrc.smis.db2xml.ExceptCellResources");
			exceptCells = resourceBundle.getString(childRepId + versionId);
		} catch (Exception e) {
			exceptCells = "";
		}

		return exceptCells;
	}
	
	/**
	 * 获取清单报表的详细记录
	 * 
	 * @auther rds
	 * @date 2006-04-22
	 * 
	 * @param repInId Integer 数据报表ID
	 * @param childRepId String 报表模板编号
	 * @param versionId String 报表模板版本号
	 * @return List
	 */
	public List findQDReportLst(Integer repInId,String childRepId, String versionId){
		List resList=null;
		
		if(repInId==null || childRepId==null || versionId==null) return resList;
		
		try{
			ResultSet rs=get_BillReport_Data(repInId,childRepId,versionId);
			if(rs!=null){
				resList=new ArrayList();
				ResultSetMetaData rsmd=rs.getMetaData();
				while(rs.next()){
					ArrayList item=new ArrayList();
					for(int i=2;i<=rsmd.getColumnCount();i++){
						item.add(rs.getString(i));
					}
					resList.add(item);
				}
				rs.close();
			}
		}catch(Exception e){
			resList=null;
			e.printStackTrace();
		}finally{
			if(connectionFactory!=null) 
				try{
					closeConnection();
				}catch(Exception ex){
					ex.printStackTrace();
				}
		}
		
		return resList;
	}
}
