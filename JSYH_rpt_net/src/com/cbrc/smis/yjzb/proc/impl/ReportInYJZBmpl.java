package com.cbrc.smis.yjzb.proc.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.cbrc.smis.proc.po.ReportIn;
/**
 * �����ʵ�ַ�����
 * @author jhb
 *
 */

public class ReportInYJZBmpl {

	/**
	 * ��ñ����ʵ���ӱ���ɣģ��汾�����ݷ�Χ����
	 * @param conn Connection ���ݿ�����
	 * @param repInId int ʵ�����ݱ���ID
	 * @return ReportIn
	 */
	public static ReportIn getReportIn(Connection conn, int repInId) throws Exception{
		if(conn==null)return null;
		
		ReportIn reportIn=null;
	    
		 PreparedStatement stmt=null;
		 ResultSet rs=null;
		 try{
			 String sql="select child_rep_id,version_id,data_range_id,org_id,year,term,cur_id from report_in"+
			 " where rep_in_id=?";
			 
			// // System.out.println("sql is :"+sql);
			 
			 stmt=conn.prepareStatement(sql.toUpperCase());
			 stmt.setInt(1,repInId);
			 
			 if(stmt.execute()){
				 reportIn=new ReportIn();
				 rs=stmt.getResultSet();
				 if(rs!=null && rs.next()){
					    String childRepId=rs.getString("child_rep_id".toUpperCase());           //�ӱ���ID
						String versionId=rs.getString("version_id".toUpperCase());              //�汾��
						int dataRangeId=rs.getInt("data_range_id".toUpperCase());               //���ݷ�ΧID
						String orgId=rs.getString("org_id".toUpperCase());                      //����id
						int year=rs.getInt("year".toUpperCase());
						int term=rs.getInt("term".toUpperCase());
						reportIn.setRepInId(repInId);
						reportIn.setVersionId(versionId);
						reportIn.setChildRepId(childRepId);
						reportIn.setDataRangeId(dataRangeId);
						reportIn.setOrgId(orgId);
						reportIn.setYear(new Integer(year));
						reportIn.setTerm(new Integer(term));
						reportIn.setCurId(rs.getInt("cur_id".toUpperCase()));
						
					/*	// System.out.println("reportIn.setRepInId(repInId) is"+reportIn.getRepInId());
						// System.out.println("reportIn.setVersionId(versionId) is"+reportIn.getVersionId());
						// System.out.println("reportIn.setChildRepId(childRepId) is"+reportIn.getChildRepId());
						// System.out.println("reportIn.setDataRangeId(dataRangeId) is"+reportIn.getDataRangeId());
						// System.out.println("reportIn.setOrgId(orgId) is"+reportIn.getOrgId());
						// System.out.println("reportIn.getYear() is"+reportIn.getYear());
						// System.out.println("reportIn.getTerm() is"+reportIn.getTerm());*/
						
				 }
				 
			 }
		 }catch(Exception e)
		 {
			 e.printStackTrace();
		 }finally{
			 if(rs!=null)rs.close();
			 if(stmt!=null)stmt.close();
			 }
		return reportIn;
	}
	
	/**
	 * ��û�����Ƶ��ID OATID
	 * @param conn ���ݿ�����
	 * @param String orgid �����ɣ�
	 * @return Integer Oatid  ����Ƶ��ID
	 * @throws Exception
	 */
	public static Integer getOatId(Connection conn, String orgid) throws Exception{
		if(conn==null || orgid==null ||orgid=="")return null;
		
		PreparedStatement stmt=null;
		ResultSet rs=null;
		Integer OatId=null;
		try{
			String sql="select oat_id from org where org_id=?";
			stmt=conn.prepareStatement(sql.toUpperCase());
			stmt.setString(1,orgid);
			if(stmt.execute())
			{
				rs=stmt.getResultSet();
				if(rs!=null && rs.next())
				{
					int oatid=rs.getInt("oat_id".toUpperCase());
					OatId=new Integer(oatid);
					
				//	// System.out.println("OatId is :"+OatId);
				}
			}
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}finally{
			if(stmt!=null)stmt.close();
			if(rs!=null)rs.close();
		}
		return OatId;
	}
	
	/**
	 * ��ñ����Ƶ��ID rep_freq_id
	 * @param conn
	 * @param child_rep_id 
	 * @param version_id
	 * @param oat_id
	 * @param data_range_id
	 * @return rep_freq_id
	 * @throws Exception
	 */
	
	public static Integer getRep_Freq_Id(Connection conn,String child_rep_id,String version_id,Integer Oat_Id,Integer Data_Range_Id) throws Exception{
		if(conn==null|| child_rep_id==null || version_id==null|| Oat_Id==null || Data_Range_Id==null)return null;
		Integer rep_freq_id=null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try{
			 int data_range_id=Data_Range_Id.intValue();
			 int oat_id=Oat_Id.intValue();
			String sql="select rep_freq_id from m_actu_rep where"+
			" data_range_id=? and child_rep_id=? and version_id=? and oat_id=?";
		
		 stmt=conn.prepareStatement(sql.toUpperCase());
		 stmt.setInt(1,data_range_id);
		 stmt.setString(2,child_rep_id);
		 stmt.setString(3,version_id);
		 stmt.setInt(4,oat_id);
		 rs=stmt.executeQuery();
		 if(rs!=null && rs.next()){
			 
			 int Rep_Freq_Id=rs.getInt("rep_freq_id".toUpperCase());
			 rep_freq_id=new Integer(Rep_Freq_Id);
			// // System.out.println("rep_freq_id is "+rep_freq_id.toString());
		 }
		}catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}finally{
			if(stmt!=null)stmt.close();
			if(rs!=null)rs.close();
		}
		
		return rep_freq_id;
	}
	
}
