package com.cbrc.smis.proc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.cbrc.smis.common.Config;

/**
 * ����PDFģ����Ϣ
 * 
 * @author rds
 * @date 2006-02-15
 */
public class UpdateMChildReport {
	/**
	 * �������µİ汾ʱ���轫��ǰģ���������Ч�İ汾�Ľ���ʱ����Ϊ��ǰ�汾��ǰһ��
	 * 
	 * @param childRepId String ģ��ı��
	 * @param versionId String �汾��
	 * @param startDate String ��ʼʱ��
	 * @return void
	 */
	public static void updateEndDate(String childRepId,String versionId,String startDate) throws SQLException,Exception{
		Connection conn=null;
		PreparedStatement pstmt=null;
		
		if(childRepId==null || versionId==null || startDate==null){
			throw new Exception("����Ĳ�������!");
		}
		
		try{
//			conn=DriverManager.getConnection("jdbc:default:connection");   //��ȡ���ݿ�����
			conn=new com.cbrc.smis.proc.jdbc.FitechConnection().getConnect();
//			
//			String sql="update m_child_report set end_date=char(date('" + startDate + "')-1 days) where " + 
//				"child_rep_id='" + childRepId + 
//				"' and version_id!='" + versionId + 
//				"' and date(end_date)>=date('" + startDate + "')";
			
			 String sql="update m_child_report set end_date=to_char((to_date('" +
			  startDate + "','yyyy-mm-dd')-1),'yyyy-mm-dd') where " +
			  "child_rep_id='" + childRepId + "' and version_id!='" + versionId + 
			  "'and to_date(end_date,'yyyy-mm-dd')>=to_date('" + startDate +
			  "','yyyy-mm-dd')";
			
			 String hql="update m_child_report set end_date=char(date('" + startDate + "')-1 days) where " + 
				"child_rep_id='" + childRepId + 
				"' and version_id!='" + versionId + 
				"' and date(end_date)>=date('" + startDate + "')";

			 String sybasehql="update M_CHILD_REPORT set END_DATE=DATEADD(day, 1, '" + startDate + "')  where " + 
				"CHILD_REP_ID='" + childRepId + 
				"' and VERSION_ID!='" + versionId + 
				"' and END_DATE>='" + startDate + "'";
			 
			 	if(Config.DB_SERVER_TYPE.equals("oracle")){
			 		pstmt=conn.prepareStatement(sql);
	    		}
	    		if(Config.DB_SERVER_TYPE.equals("db2")){
	    			pstmt=conn.prepareStatement(hql);
	    		} 
	    		if(Config.DB_SERVER_TYPE.equals("sybase")){
	    			pstmt=conn.prepareStatement(sybasehql);
	    		} 
			/*pstmt.setString(1,startDate);
			pstmt.setString(2,childRepId);
			pstmt.setString(3,versionId);
			pstmt.setString(4,startDate);*/
			pstmt.executeUpdate();
		}catch(SQLException sqle){
			throw new Exception(sqle.getMessage());
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
}
