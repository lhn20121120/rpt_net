package com.cbrc.smis.proc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.proc.impl.Expression;
import com.cbrc.smis.proc.impl.LogInImpl;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.proc.impl.ReportImpl;
import com.cbrc.smis.proc.po.DataValidateInfo;
import com.cbrc.smis.proc.po.MCellFormu;
import com.cbrc.smis.proc.po.ReportIn;
import com.cbrc.smis.util.FitechLog;
/**
 * ����ı��У��洢������
 * 
 * @author rds
 * @serialData 2005-12-16 17:30
 */
public class ReportBJValidate implements Report{	
	/**
	 * ����ı���ϵУ��
	 * 
	 * @param repInId ʵ�����ݱ���ID
	 * @return void
	 */
	public static void validate(int repInId) throws SQLException,Exception{
		/**
		 * �������
		 */
		int errorCode=0;
		
		List dataValidateInfoList=null;      //У������б�
		
		Connection conn=null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		
		try{
			String sql="select child_rep_id,version_id,data_range_id,org_id,cur_id,year,term " + 
				"from report_in where rep_in_id=?";
			
//			conn=DriverManager.getConnection("jdbc:default:connection");   //��ȡ���ݿ�����
			conn=new com.cbrc.smis.proc.jdbc.FitechConnection().getConnect();
			
			stmt=conn.prepareStatement(sql.toUpperCase());
			stmt.setInt(1,repInId);
			rs=stmt.executeQuery();
			if(rs!=null){
				if(rs.next()){
					String childRepId=rs.getString("child_rep_id".toUpperCase());           //�ӱ���ID
					String versionId=rs.getString("version_id".toUpperCase());              //�汾��
					int dataRangeId=rs.getInt("data_range_id".toUpperCase());               //���ݷ�ΧID
					String orgId=rs.getString("org_id".toUpperCase());                      //����ID
					int curId=rs.getInt("cur_id".toUpperCase());                            //���ҵ�λID
					int year=rs.getInt("year".toUpperCase());                        		  //�������
					int term=rs.getInt("term".toUpperCase());                         	  //��������
					
					ReportIn reportIn=new ReportIn();
					reportIn.setRepInId(repInId);
					reportIn.setVersionId(versionId);
					reportIn.setChildRepId(childRepId);
					reportIn.setDataRangeId(dataRangeId);
					reportIn.setOrgId(orgId);
					reportIn.setCurId(curId);
					reportIn.setYear(year);
					reportIn.setTerm(term);
					
					reportIn.setChildRepId(childRepId);
					
					/*// System.out.println("reportIn.childRepId:" + reportIn.getChildRepId());
					// System.out.println("reportIn.versionId:" + reportIn.getVersionId());
					// System.out.println("reportIn.OrgId:" + reportIn.getOrgId());
					// System.out.println("reportIn.CurId:" + reportIn.getCurId());
					// System.out.println("reportIn.DataRangeId:" + reportIn.getDataRangeId());
					// System.out.println("reportIn.Term:" + reportIn.getTerm());
					// System.out.println("reportIn.Year:" + reportIn.getYear());*/
				    if( (childRepId.trim().equals("G3301") || childRepId.trim().equals("G3302") ) && versionId.trim().equals("0690"))	{
				    	System.out.println("cur_id=" + curId);
				    	
				    }
					Integer tblOuterValidateFlag=RESULT_OK;
					
					String operation="���ݱ���[���:" + childRepId + "," + 
						"�汾��:" + versionId + "," +
						"���ݷ�Χ:" + dataRangeId + "," +
						"����ID:" + orgId.trim() + "," + 
						"���ҵ�λ:" + curId + "," + 
						"���:" + year + "," +
						"����:" + term + "]";

					//if(isAllBJReportsExists(conn,childRepId,versionId,dataRangeId,orgId,curId,year,term)==true){
						/**ִ�м������Զ����洢����**/
						/*CallableStatement cstmt=conn.prepareCall("call ReportZDJS(?)");
						cstmt.setInt(1,repInId);
						cstmt.execute();*/
					//��ȡ�û�����ͬһʱ��㡢ͬһ���ݷ�Χ�Ѿ��ϱ����ӱ���ID
					String sql1="select child_rep_id from report_in"+
					" where version_id=? and data_range_id=? and org_id=? and cur_id=? and year=? and term=?";
					
					pstmt=conn.prepareStatement(sql1.toUpperCase());
					pstmt.setString(1,versionId);
					pstmt.setInt(2,dataRangeId);
					pstmt.setString(3,orgId);
					pstmt.setInt(4,curId);
					pstmt.setInt(5,year);
					pstmt.setInt(6,term);
					if(pstmt.execute()){
						rs=pstmt.getResultSet();
					}
					List existReport = new ArrayList();
					if(rs!=null){
						while(rs.next())
							existReport.add(rs.getString("child_rep_id".toUpperCase()));
					}else{
						//throw new Exception("��ѯ�޼�¼");
					}
						int reportStyle=ReportImpl.getReportStyle(conn,childRepId,versionId);
						reportIn.setReportStyle(new Integer(reportStyle));
						
						List cellFormuList=ReportImpl.getCellFormus(conn,childRepId,versionId,FORMU_TYPE_BJ);
						
						if(cellFormuList!=null && cellFormuList.size()>0){
							dataValidateInfoList=new ArrayList();
							MCellFormu mCellFormu=null;
							
							for(int i=0;i<cellFormuList.size();i++){
								mCellFormu=(MCellFormu)cellFormuList.get(i);
								
								if(isCurCellformuReportsExists(childRepId,mCellFormu,existReport)==true){
									DataValidateInfo dvi=new DataValidateInfo();
									dvi.setRepInId(new Integer(repInId));
									dvi.setCellFormuId(mCellFormu.getCellFormuId());
									dvi.setValidateTypeId(Report.VALIDATE_TYPE_BJ);
									if(reportStyle==Report.REPORT_STYLE_DD){
										if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2")){
											dvi.setSeqNo("  Seq_Data_Validate_Info.nextval");
					            		}
					            		if(Config.DB_SERVER_TYPE.equals("db2")){
					            			dvi.setSeqNo("nextval for Seq_Data_Validate_Info");
					            		}    
					            		
										
										if(Expression.bjValidate(conn,reportIn,mCellFormu.getCellFormu(),mCellFormu.getPointNumber())==true){
											dvi.setResult(RESULT_OK);
										}else{
											dvi.setResult(RESULT_NO);
											tblOuterValidateFlag=RESULT_NO;
										}
									}else{
										if(Expression.bjValidate(conn,reportIn,mCellFormu.getCellFormu(),mCellFormu.getPointNumber())==true){
											dvi.setResult(RESULT_OK);
										}else{
											dvi.setResult(RESULT_NO);
											tblOuterValidateFlag=RESULT_NO;
											dvi.setCause(Expression.getCause());
											if(!Expression.getErrMsg().equals("")){  //���У�鲻ͨ������Ϊϵͳ��������д����־
												LogInImpl.writeBJLog(conn,Expression.getErrMsg());
											}
										}
									}
									dataValidateInfoList.add(dvi);
								}else{ //����ϵУ����ʽ�������ı���δ��
									DataValidateInfo dvi=new DataValidateInfo();
									dvi.setRepInId(new Integer(repInId));
									dvi.setCellFormuId(mCellFormu.getCellFormuId());
									dvi.setValidateTypeId(Report.VALIDATE_TYPE_BJ);
									dvi.setResult(RESULT_NO);
									tblOuterValidateFlag=REPORT_CANCLE;					
									if(reportStyle==Report.REPORT_STYLE_DD){
										if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2")){
											dvi.setSeqNo("  Seq_Data_Validate_Info.nextval");
					            		}
					            		if(Config.DB_SERVER_TYPE.equals("db2")){
					            			dvi.setSeqNo("nextval for Seq_Data_Validate_Info");
					            		}  
									}else{
										dvi.setCause("�����ı���δ��ȫ!");
									}
									dataValidateInfoList.add(dvi);
								}
								
							} 
						}else{
							//��У�鹫ʽҲ�����У���־��Ϊͨ��							
							tblOuterValidateFlag=RESULT_OK;
							ReportImpl.updateFlag(conn,repInId,"TBL_OUTER_VALIDATE_FLAG",tblOuterValidateFlag.intValue());
						}
						/**���У�������Ϣ��Ϊ�գ�����У������������**/
						if(dataValidateInfoList!=null && dataValidateInfoList.size()>0){
							if(ReportImpl.writeBJDataValidateInfo(conn,dataValidateInfoList,reportStyle)==true){
								ReportImpl.updateFlag(conn,repInId,"TBL_OUTER_VALIDATE_FLAG",tblOuterValidateFlag.intValue());
								LogInImpl.writeBJLog(conn,operation + "У��ͨ��!");
							}else{
								LogInImpl.writeBJLog(conn,operation + "У��δͨ��!");
							}
						}
						 
					//}
				}
			}else{    //ϵͳ����
				throw new Exception("ϵͳ����!");
			}
		}catch(SQLException sqle){
			sqle.printStackTrace();
			//throw new Exception(sqle.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			//throw new Exception(e.getMessage());
		}finally{
			conn.commit();
			if(stmt!=null) stmt.cancel();
			if(conn!=null) conn.close();
		}
	}
	
	/**
	 * �жϱ���ϵУ���������ı����Ƿ�����ȫ�б�
	 * 
	 * @param conn Connection ���ݿ�����
	 * @param childRepId String �ӱ���ID
	 * @param versionId String �汾��
	 * @param dataRangeId int ���ݷ�ΧID
	 * @param orgId String ����ID
	 * @param curId int ���ҵ�λID
	 * @param year int �������
	 * @param term int ��������
	 * @return boolean
	 */
	private static boolean isAllBJReportsExists(
			Connection conn,
			String childRepId,
			String versionId,
			int dataRangeId,
			String orgId,
			int curId,
			int year,
			int term) throws Exception{
		boolean isAllExists=false;
		
		if(conn==null || childRepId==null || versionId==null || year<=0 || term<=0){
			return false;
		}
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String _sql="",sql="";
		int tReports=0;		//����ϵУ�鹫ʽ�����ı�������
		int _tReports=0;    //����ϵУ����Ҫ�ϱ��ı�����ʵ�����ϱ��ı�������
		
		try{
			_sql="select distinct mctf.child_rep_id,mctf.version_id from m_cell_to_formu mctf " + 
				" where mctf.cell_formu_id in (" + 
				" select distinct ctf.cell_formu_id from m_cell_to_formu ctf " +
				" inner join m_cell_formu mcf on ctf.cell_formu_id=mcf.cell_formu_id " +
				" where ctf.child_rep_id=? and ctf.version_id=? and mcf.formu_type=?)";
			
			sql="select count(*) as total from (" + _sql + ") as tmp";
			/*// System.out.println(sql);*/
			pstmt=conn.prepareStatement(sql.toUpperCase());
			pstmt.setString(1,childRepId);
			pstmt.setString(2,versionId);
			pstmt.setInt(3,FORMU_TYPE_BJ);
			if(pstmt.execute()){
				rs=pstmt.getResultSet();
				if(rs!=null && rs.next()){
					tReports=rs.getInt("total".toUpperCase());
				}else{
					//throw new Exception("��ѯ�޼�¼");
				}
			}else{  //ִ�в�ѯʧ��
				//throw new Exception(sql);
			}
			
			if(tReports>0){
				sql="select count(*) as total from (" + 
					"select distinct ri.child_rep_id,ri.version_id from report_in ri," + 
					"(" + _sql + ") as tmp " +
					"where ri.child_rep_id=tmp.child_rep_id and ri.version_id=tmp.version_id and " +
					"ri.data_range_id=? and ri.org_id=? and ri.cur_id=? and ri.year=? and ri.term=?) as temp";
				// System.out.println(sql);
				pstmt=conn.prepareStatement(sql.toUpperCase());
				pstmt.setString(1,childRepId);
				pstmt.setString(2,versionId);
				pstmt.setInt(3,FORMU_TYPE_BJ);
				pstmt.setInt(4,dataRangeId);
				pstmt.setString(5,orgId);
				pstmt.setInt(6,curId);
				pstmt.setInt(7,year);
				pstmt.setInt(8,term);
				
				if(pstmt.execute()){
					rs=pstmt.getResultSet();
					if(rs!=null && rs.next()){
						_tReports=rs.getInt("total".toUpperCase());
					}else{
						throw new Exception("��ѯ�޼�¼");
					}
				}else{  //ִ�в�ѯʧ��
					throw new Exception(sql);
				}
				
				if(tReports==_tReports){
					isAllExists=true;
				}
			}			
		}catch(SQLException sqle){
			isAllExists=false;
			throw new Exception(sqle.getMessage());
		}finally{
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
		}
		
		return isAllExists;
	}
	
	/**
	 * �жϱ���ϵУ���������ı����Ƿ�����ȫ�б� 
	 * 
	 * @param childRepId String �ӱ���ID
	 * @param mCellFormu MCellFormu ��乫ʽ
	 * @param existReport �Ѿ����ڵı�
	 * 
	 * @return boolean
	 */
	private static boolean isCurCellformuReportsExists(
			String childRepId,
			MCellFormu mCellFormu,
			List existReport
			) throws Exception{
		boolean isAllExists=true;		
		int posS=0,posE=0;
		List linkReports=new ArrayList();
		List formuReport = new ArrayList();
		
		try{
			String cellFormu=null;			
			cellFormu=(String)mCellFormu.getCellFormu().trim();
			
			posS=cellFormu.indexOf("[");
			posE=cellFormu.indexOf("]");
			String cellName="";

			while(posS>=0 && posE>=0 && posE>posS){
				cellName=cellFormu.substring(posS+1,posE);
				if(!cellName.equals("")) linkReports.add(cellName);
				if(posE<cellFormu.length()) cellFormu=cellFormu.substring(posE+1);
				posS=cellFormu.indexOf("[");
				posE=cellFormu.indexOf("]");
			}
			
			//ȡmCellFormu��ʽ�а������ӱ�ID
			formuReport.add(childRepId);			
			String arr[]=null;
			for(int i=0;i<linkReports.size();i++){
				arr=linkReports.get(i)==null?null:((String)linkReports.get(i)).split("_");
				if(arr!=null && arr.length==3){
					if(!formuReport.contains(arr[0])){
						formuReport.add(arr[0]);
					}
				}
			}				
		}catch(Exception e){
			e.printStackTrace();
		}
		
		isAllExists=ifContainValue(existReport,formuReport);

		return isAllExists;		
	}
	
	public static boolean ifContainValue(List bigList,List smallList){
		boolean flag=true;
		if(bigList==null) return false;		
		if(smallList==null) return false;
		
		if(bigList.size()<smallList.size()) return false;
		flag=bigList.containsAll(smallList);
		
		/*for(int i=0;i<smallList.size();i++){
			boolean bool=false;
			for(int j=0;j<bigList.size();j++){
				if(bigList.get(j).equals(smallList.get(i))==true){
					bool=true;break;}
			}
			if(bool==false)
				flag=false;
		}*/
		return flag;
	}
}
