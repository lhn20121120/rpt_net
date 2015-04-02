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
 * ΪDb2Xml���ṩ����
 * 
 * @author Ҧ��
 * 
 */
public class Db2XmlUtil {
	private static FitechException log = new FitechException(Db2XmlUtil.class);

	private FitechConnection connectionFactory = new FitechConnection();

	/**
	 * ��Ե㱨��
	 */
	public static int P2P_PEPORT = 1;

	/**
	 * �嵥����
	 */
	public static int BILL_REPORT = 2;

	/**
	 * ȡ����Ҫתxml�ı����б�
	 * 
	 * @return List �����б���
	 */
	public List get_NeedToXmlReps() {
		return StrutsReportInDelegate.getNeedToXmlReps();
	}

	/**
	 * ��øñ�������ͣ��嵥ʽ or ��Ե�ʽ��
	 * 
	 * @param childReportId
	 *            String �ӱ���id
	 * @param versionId
	 *            String �汾��id
	 * @return 1 ��Ե�ʽ 2 �嵥ʽ
	 */
	public int get_PeportStyle(String childReportId, String versionId) {
		return StrutsMChildReportDelegate.getReportStyle(childReportId,
				versionId);
	}

	/**
	 * ���ݵ�Ե�ʵ���ӱ���id����ѯ��������Ӧ�ļ�¼
	 * 
	 * @param reportInId
	 *            String ʵ���ӱ���id
	 * @return List ��ʵ���ӱ��������
	 */
	public List get_P2PReport_Data(Integer reportInId) {
		return StrutsReportInInfoDelegate.getReportData(reportInId);
	}

	/**
	 * ����ʵ���ӱ���Id��ȡ���������Ϣ
	 * 
	 * @author rds
	 * @date 2005-17:36
	 * 
	 * @param childRepId
	 *            ʵ���ӱ���ID
	 * @param versionId
	 *            String �汾��
	 * @param exceptCells
	 *            String �������
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
	 * �����嵥ʽʵ���ӱ���id����ѯ��������Ӧ�ļ�¼
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
						+ ",(case replace(col1,' ','') when '' then 999998 when '�ϼ�' then 999999 else cast(col1 as integer) end) as seq "
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
	 * �����嵥ʽ�����id�Ͱ汾�ţ�ȡ�ø��嵥ʽ��������Ӧ�����ݿ����
	 * 
	 * @param childReportId
	 *            String �ӱ���id(�嵥ʽ��)
	 * @param versionId
	 *            String �汾��id
	 * @return ���ظ��嵥ʽ��������Ӧ�����ݿ����
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
	 * ����xml�ļ��󣬸ı����ݿ��еı������ݲֿ��־
	 * 
	 * @param reportId
	 *            Integer ʵ�ʱ���id
	 * @return �Ƿ����óɹ�
	 */
	public boolean setReportDataWarehouseFlag(Integer reportId) {
		return StrutsReportInDelegate.setReportDataWarehouseFlag(reportId);
	}

	/**
	 * ȡ�ø����ⱨ���еĸ��е�����(���ظ�)
	 * 
	 * @param reportData
	 *            List ������ѯ���ĸñ������������
	 */
	public List get_specialReportRowList(List reportData) {
		/** ɸѡ�����ظ����к� */
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
		/** �к����� */
		Collections.sort(rowNameSort);

		/*
		 * for(int i=0;i<rowNameSort.size();i++) { // System.out.println("Row "+
		 * rowNameSort.get(i)); }
		 */

		return rowNameSort;
	}

	/**
	 * �жϱ����ĳ��Ԫ���ǲ��ǰٷֱȵ�Ԫ��
	 * 
	 * @param childRepId  �ӱ���id
	 * @param versionId  �汾��id
	 * @param cellName  ��Ԫ������
	 * @return boolean �ǰٷֱȵ�Ԫ���򷵻�true
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
	 * ת���ɰٷ���
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
	 * �жϱ����ĳ��Ԫ���ǲ��ǰٷֱȵ�Ԫ��
	 * 
	 * @param childRepId  �ӱ���id
	 * @param versionId  �汾��id
	 * @param cellName  ��Ԫ������
	 * @return boolean �ǰٷֱȵ�Ԫ���򷵻�true
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
	 * ���Է���
	 * 
	 * @param args
	 * @return void
	 */
	public static void main(String[] args) {
		// System.out.println(new Db2XmlUtil().toPercent("48.2100"));
	}

	/**
	 * �ر����ݿ�����
	 * 
	 */
	public void closeConnection() throws Exception {
		connectionFactory.close();
	}

	/**
	 * ��ý�����ı�������T2���͵Ľӿ��ļ�ʱ���ںϼƱ��������ų�������Ϣ
	 * 
	 * @author rds
	 * @date 2006-01-09
	 * 
	 * @param childRepId String �ӱ���ID
	 * @param versionId String �汾��
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
	 * ��ȡ�嵥�������ϸ��¼
	 * 
	 * @auther rds
	 * @date 2006-04-22
	 * 
	 * @param repInId Integer ���ݱ���ID
	 * @param childRepId String ����ģ����
	 * @param versionId String ����ģ��汾��
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
