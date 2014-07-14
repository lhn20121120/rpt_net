package com.cbrc.smis.adapter;

import java.sql.SQLException;

import net.sf.hibernate.Session;

import com.cbrc.smis.proc.ReportValid;
import com.cbrc.smis.proc.ReportYCBH;
import com.cbrc.smis.proc.ReportZDJS;
import com.cbrc.smis.proc.UpdateMChildReport;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.util.FitechException;

;
/**
 * �洢���̲�����
 * 
 * @author rds
 * @date 2006-01-02 11:30
 */
public class Procedure {
	private static FitechException log=new FitechException(Procedure.class);
	
	/**
	 * ִ�б����Զ�������ļ���洢����
	 * 
	 * @param repInId
	 *            Integer ʵ�����ݱ���ID
	 * @return boolean ִ�гɹ�������true;���򣬷���false
	 */
	public static boolean runZDJS(Integer repInId){
		boolean result=false;
		
		if(repInId==null) return result;
		
		try {
			ReportZDJS.calculateBN(repInId.intValue());
			ReportZDJS.calculateBJ(repInId.intValue());
			result=true;
		}catch(SQLException sqle){
			result=false;
			log.printStackTrace(sqle);
		}catch(Exception e){
			result=false;
			log.printStackTrace(e);
		}
		/*
		 * DBConn conn=null; Session session=null; Connection connection=null;
		 * CallableStatement cstmt=null;
		 * 
		 * try{ String hql="call ReportZDJS(?)";
		 * 
		 * conn=new DBConn(); session=conn.beginTransaction();
		 * connection=session.connection(); cstmt=connection.prepareCall(hql);
		 * cstmt.setInt(1,repInId.intValue()); cstmt.execute();
		 * conn.endTransaction(true); result=true; }catch(SQLException sqle){
		 * result=false; log.printStackTrace(sqle); }catch(Exception e){
		 * result=false; log.printStackTrace(e); }finally{ if(conn!=null)
		 * conn.closeSession(); }
		 */
		return result;
	}
	
	/**ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21**/
	/**
	 * 
	 * ִ�б���/��?��У��洢����
	 * 
	 * @param repInId
	 *            Integer ʵ�����ݱ���ID
	 * @return boolean ִ�гɹ�������true;���򣬷���false
	 */
	public static boolean runBNJY(Integer repInId,String userName){
		boolean result=false;
		// System.out.println("repInId:" + repInId);
		if(repInId==null) return result;
	// ReportBNValidate bn=new ReportBNValidate();
		try{
			//ReportBNValidate.validate(repInId.intValue());
            //����2007-09-25�޸�
            //return ReportBNValidate.valid(repInId.intValue());
            //����÷��������⣬��ָ�ԭ�޸ĺ󷽷�2007-09-30����
			/**ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21**/
            return ReportValid.processValid(repInId,Report.VALIDATE_TYPE_BN,userName);
		}catch(SQLException sqle){
			result=false;
			log.printStackTrace(sqle);
		}catch(Exception e){
			result=false;
			log.printStackTrace(e);
		}
		
		/*
		 * DBConn conn=null; Session session=null; Connection connection=null;
		 * CallableStatement cstmt=null; ReportBNValidate bn = new
		 * ReportBNValidate(); try{ // bn.validate(repInId.intValue()); //
		 * String hql="call ReportBNValidate(?)";
		 * bn.validate(repInId.intValue()); // conn=new DBConn(); //
		 * session=conn.beginTransaction(); // connection=session.connection(); //
		 * cstmt=connection.prepareCall(hql); //
		 * cstmt.setInt(1,repInId.intValue()); // cstmt.execute(); //
		 * conn.endTransaction(true); result=true; // System.out.println("result:" +
		 * result); }catch(SQLException sqle){ result=false;
		 * log.printStackTrace(sqle); }catch(Exception e){ result=false;
		 * log.printStackTrace(e); }finally{ if(conn!=null) conn.closeSession(); }
		 */
		return result;
	}
	
	/**ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21**/
	/**
	 * ִ�б��У��洢����
	 * 
	 * @param repInId
	 *            Integer ʵ�����ݱ���ID
	 * @return boolean ִ�гɹ�������true;���򣬷���false
	 */
	public static boolean runBJJY(Integer repInId,String userName){
		boolean result=false;
		
		if(repInId==null) return result;
		try{
		//	ReportBJValidate.validate(repInId.intValue());
			//result=true;
            //����÷��������⣬��ָ�ԭ��������2007-09-30����
			/**ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21**/
            return ReportValid.processValid(repInId,Report.VALIDATE_TYPE_BJ,userName);
		}catch(SQLException sqle){
			result=false;
			log.printStackTrace(sqle);
		}catch(Exception e){
			result=false;
			log.printStackTrace(e);
		}
		
		/*
		 * DBConn conn=null; Session session=null; Connection connection=null;
		 * CallableStatement cstmt=null;
		 * 
		 * try{ String hql="call ReportBJValidate(?)"; conn=new DBConn();
		 * session=conn.beginTransaction(); connection=session.connection();
		 * cstmt=connection.prepareCall(hql);
		 * cstmt.setInt(1,repInId.intValue()); cstmt.execute();
		 * conn.endTransaction(true); result=true; // System.out.println("result:" +
		 * result); }catch(SQLException sqle){ result=false;
		 * log.printStackTrace(sqle); }catch(Exception e){ result=false;
		 * log.printStackTrace(e); }finally{ if(conn!=null) conn.closeSession(); }
		 */
		return result;
	}
	
	/**
	 * ִ���쳣�仯�洢����
	 * 
	 * @param repInId
	 *            Integer ʵ�����ݱ���ID
	 * @return boolean ִ�гɹ�������true;���򣬷���false
	 */
	public static boolean runYCBH(Integer repInId){
		boolean result=false;
		
		if(repInId==null) return result;
		try{
			ReportYCBH.calculateYCBH(repInId.intValue()) ;
			result=true;
		}catch(SQLException sqle){
			result=false;
			log.printStackTrace(sqle);
		}catch(Exception e){
			result=false;
			log.printStackTrace(e);
		}
		/*
		 * DBConn conn=null; Session session=null; Connection connection=null;
		 * CallableStatement cstmt=null;
		 * 
		 * try{ String hql="call ReportYCBH(?)";
		 * 
		 * conn=new DBConn(); session=conn.beginTransaction();
		 * connection=session.connection(); cstmt=connection.prepareCall(hql);
		 * cstmt.setInt(1,repInId.intValue()); cstmt.execute();
		 * conn.endTransaction(true); result=true; }catch(SQLException sqle){
		 * result=false; log.printStackTrace(sqle); }catch(Exception e){
		 * result=false; log.printStackTrace(e); }finally{ if(conn!=null)
		 * conn.closeSession(); }
		 */
		return result;
	}
	
	/**
	 * ���ø���ǰ�汾�Ľ���ʱ��洢����
	 * 
	 * @param session
	 *            Session �Ự����
	 * @param childRepId
	 *            String �ӱ�Id
	 * @param versionId
	 *            String �汾��
	 * @param startDate
	 *            String �°汾�Ŀ�ʼʱ��
	 * @return boolean
	 */
	public static boolean updateEndDate(Session session,String childRepId,String versionId,String startDate){
		boolean result=false;
		
		if(session==null || childRepId==null || versionId==null || startDate==null) return result;
		try {
			UpdateMChildReport.updateEndDate(childRepId,versionId,startDate);
		// UpdateMChildReport ip =new UpdateMChildReport();
			
			result=true;
		}catch(SQLException sqle){
			result=false;
			log.printStackTrace(sqle);
		}catch(Exception e){
			result=false;
			log.printStackTrace(e);
		}
		/*
		 * DBConn conn=null; Connection connection=null; CallableStatement
		 * cstmt=null; int runResult=0; try{ // String hql="call
		 * UpdateMChildReport(?,?,?)";
		 *  // UpdateMChildReport.updateEndDate(childRepId,versionId,startDate);
		 * 
		 * String hql="update m_child_report set end_date=to_char((to_date('" +
		 * startDate + "','yyyy-mm-dd')-1),'yyyy-mm-dd') where " +
		 * "child_rep_id='" + childRepId + "' and version_id!='" + versionId + "'
		 * and to_date(end_date,'yyyy-mm-dd')>=to_date('" + startDate +
		 * "','yyyy-mm-dd')";
		 * 
		 * 
		 * conn=new DBConn(); session=conn.beginTransaction();
		 * connection=session.connection(); cstmt=connection.prepareCall(hql); //
		 * cstmt.setString(1,childRepId); // cstmt.setString(2,versionId); //
		 * cstmt.setString(3,startDate); // cstmt.execute();
		 * 
		 * result=true; }catch(SQLException sqle){ result=false;
		 * log.printStackTrace(sqle); }catch(Exception e){ result=false;
		 * log.printStackTrace(e); }
		 */
		return result;
	}
	
	/**
	 * ִ�п�Ƶ��У��洢����
	 * 
	 * @param repInId
	 *            Integer ʵ�����ݱ���ID
	 * @return boolean ִ�гɹ�������true;���򣬷���false
	 */
// public static boolean runKPDJY(Integer repInId){
// boolean result=false;
//		
// if(repInId==null) return result;
// try {
// ReportKPValidate.validate(repInId.intValue());
// result=true;
// }catch(SQLException sqle){
// result=false;
// log.printStackTrace(sqle);
// }catch(Exception e){
// result=false;
// log.printStackTrace(e);
// }
		
		/*
		 * DBConn conn=null; Session session=null; Connection connection=null;
		 * CallableStatement cstmt=null;
		 * 
		 * try{ String hql="call ReportKPValidate(?)";
		 * 
		 * conn=new DBConn(); session=conn.beginTransaction();
		 * connection=session.connection(); cstmt=connection.prepareCall(hql);
		 * cstmt.setInt(1,repInId.intValue()); cstmt.execute();
		 * conn.endTransaction(true); result=true; // System.out.println(result);
		 * }catch(SQLException sqle){ result=false; log.printStackTrace(sqle);
		 * }catch(Exception e){ result=false; log.printStackTrace(e); }finally{
		 * if(conn!=null) conn.closeSession(); }
//		 */
//		return result;
//	}
}
