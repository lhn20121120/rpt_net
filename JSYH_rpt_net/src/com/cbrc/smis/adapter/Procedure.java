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
 * 存储过程操作类
 * 
 * @author rds
 * @date 2006-01-02 11:30
 */
public class Procedure {
	private static FitechException log=new FitechException(Procedure.class);
	
	/**
	 * 执行报表自动计算域的计算存储过程
	 * 
	 * @param repInId
	 *            Integer 实际数据报表ID
	 * @return boolean 执行成功，返回true;否则，返回false
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
	
	/**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
	/**
	 * 
	 * 执行表（间/内?）校验存储过程
	 * 
	 * @param repInId
	 *            Integer 实际数据报表ID
	 * @return boolean 执行成功，返回true;否则，返回false
	 */
	public static boolean runBNJY(Integer repInId,String userName){
		boolean result=false;
		// System.out.println("repInId:" + repInId);
		if(repInId==null) return result;
	// ReportBNValidate bn=new ReportBNValidate();
		try{
			//ReportBNValidate.validate(repInId.intValue());
            //龚明2007-09-25修改
            //return ReportBNValidate.valid(repInId.intValue());
            //如果该方法有问题，请恢复原修改后方法2007-09-30龚明
			/**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
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
	
	/**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
	/**
	 * 执行表间校验存储过程
	 * 
	 * @param repInId
	 *            Integer 实际数据报表ID
	 * @return boolean 执行成功，返回true;否则，返回false
	 */
	public static boolean runBJJY(Integer repInId,String userName){
		boolean result=false;
		
		if(repInId==null) return result;
		try{
		//	ReportBJValidate.validate(repInId.intValue());
			//result=true;
            //如果该方法有问题，请恢复原方法调用2007-09-30龚明
			/**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
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
	 * 执行异常变化存储过程
	 * 
	 * @param repInId
	 *            Integer 实际数据报表ID
	 * @return boolean 执行成功，返回true;否则，返回false
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
	 * 调用更新前版本的结束时间存储过程
	 * 
	 * @param session
	 *            Session 会话连接
	 * @param childRepId
	 *            String 子报Id
	 * @param versionId
	 *            String 版本号
	 * @param startDate
	 *            String 新版本的开始时间
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
	 * 执行跨频度校验存储过程
	 * 
	 * @param repInId
	 *            Integer 实际数据报表ID
	 * @return boolean 执行成功，返回true;否则，返回false
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
