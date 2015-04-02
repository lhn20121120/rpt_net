package com.fitech.net.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
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

import org.apache.struts.upload.FormFile;

import com.cbrc.smis.adapter.TranslatorUtil;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.proc.impl.Expression;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.AAnalysisTPForm;
import com.fitech.net.form.AnalysisTPTYPEForm;
import com.fitech.net.form.FromCompareForm;
import com.fitech.net.hibernate.AAnalysisTP;
import com.fitech.net.hibernate.AnalysisTPTYPE;

/**
 *������
 *���ڣ�2007-12-12
 *���ߣ�wh
 */
public class StrutsAnalysisTemplateDelegate
{
//	Catch��������׳��������쳣
	private static FitechException log=new FitechException(StrutsACompareLogDelegate.class);

		public static List findAllAnalysisTPType(){
			List results=null;
			
			DBConn conn=null;
			Session session=null;
			
			try{
				conn=new DBConn();
				session=conn.openSession();
				
				String hql="from AnalysisTPTYPE at";
				Query query=session.createQuery(hql);
				
				List list=query.list();
				if(list!=null && list.size()>0){
					results=new ArrayList();
					AnalysisTPTYPE analysisTPTYPE=null;
					for(int i=0;i<list.size();i++){
						analysisTPTYPE=(AnalysisTPTYPE)list.get(i);
						AnalysisTPTYPEForm analysisTPTYPEForm=new AnalysisTPTYPEForm();
						analysisTPTYPEForm.setAnalyTypeID(analysisTPTYPE.getAnalyTypeID());
						analysisTPTYPEForm.setAnalyTypeName(analysisTPTYPE.getAnalyTypeName());
						results.add(analysisTPTYPEForm);
					}
				}
			}catch(HibernateException he){
				log.printStackTrace(he);
			}catch(Exception e){
				log.printStackTrace(e);
			}finally{
				if(conn!=null) conn.closeSession();
			}
			
			return results;
		}
	
	/**
	 * ģ���ŵļ��
	 * @param atId
	 * @return
	 */
	public static boolean isATIdExists(String atId){
		boolean result = false;
		if(atId==null || "".equals(atId))return result;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		StringBuffer hql =new StringBuffer( "select count(*) from AAnalysisTP at ");
		hql.append("where  at.ATiD='"+atId.trim()+"'");
		try
		{ 
			// List���ϵĲ���
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();
			if(list!=null && list.size()>0)
			{
				int count = list.get(0) == null ? 0 : ((Integer) list.get(0))
						.intValue();
				if (count > 0)
					result = true;
			}
		}
		catch (HibernateException he)
		{
			log.printStackTrace(he);
		}
		catch (Exception e)
		{
			log.printStackTrace(e);
		}
		finally
		{
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return result;
		
	}
	/**
	 * ģ�����Ƶļ��
	 * @param atName
	 * @return
	 */
	public static boolean isATNameExists(String atName){
		boolean result = false;
		if(atName==null || "".equals(atName))return result;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		StringBuffer hql =new StringBuffer( "select count(*) from AAnalysisTP at ");
		hql.append("where  at.ATName='"+atName.trim()+"'");
		try
		{ 
			// List���ϵĲ���
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();
			if(list!=null && list.size()>0)
			{
				int count = list.get(0) == null ? 0 : ((Integer) list.get(0))
						.intValue();
				if (count > 0)
					result = true;
			}
		}
		catch (HibernateException he)
		{
			log.printStackTrace(he);
		}
		catch (Exception e)
		{
			log.printStackTrace(e);
		}
		finally
		{
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return result;
		
	}
	
	/**
	 * ͳ�Ʒ���ģ����� ��cpt��
	 * 
	 * @param aAnalysisTPForm
	 * @return
	 * 
	 */
	public static boolean insertAnalysisTemplate(AAnalysisTPForm aAnalysisTPForm) throws Exception
	{
		boolean result = false;
		DBConn conn = new DBConn();
		Session session = conn.beginTransaction();

		try
		{
//			 дģ�嵽web-inf/reportletsĿ¼�£�����finereportչʾ
			String templateFileName = Config.WEBROOTPATH + "WEB-INF" + File.separator +  com.fitech.net.config.Config.ANALYSIS_REPORT_NAME
					+ File.separator + aAnalysisTPForm.getATId() + ".cpt";
			FormFile tmplFile = aAnalysisTPForm.getTemplateFile();
			InputStream inStream = tmplFile.getInputStream();
	    	boolean tempFileResult = false;   //FitechUtil.copyFile(inStream, templateFileName);
	    	
	    	if (tmplFile != null)
			{
				InputStream ips = tmplFile.getInputStream();
				FileOutputStream fops = new FileOutputStream(templateFileName);
				byte[] bytes = new byte[1024];
				int index = 0;
				while ((index = ips.read(bytes)) != -1)
				{
					fops.write(bytes, 0, index);
				}
				fops.close();
				ips.close();
				tempFileResult=true;
			}
	    	
	    	if(tempFileResult){
				/** ����AAnalysisTP�� */
				AAnalysisTP aAnalysisTP = new AAnalysisTP();
				aAnalysisTP.setATiD(aAnalysisTPForm.getATId());
				aAnalysisTP.setATName(aAnalysisTPForm.getATName());
				aAnalysisTP.setAnalyTypeID(aAnalysisTPForm.getAnalyTypeID());
				session.save(aAnalysisTP);
				session.flush();
				inStream.close();
				
				result=true;
	    	}	
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			result = false;
		}
		finally
		{
			try
			{
				if (conn != null)
					conn.endTransaction(result);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}
	 /**
     * ����ͳ�Ʒ���ģ���¼
     * @author wh
     * @param aAnalysisTPForm
     * @return replist
     */
	 public static int getAnalysisCount(AAnalysisTPForm aAnalysisTPForm)
	    {
	    	int count = 0;
	    	if(aAnalysisTPForm==null)return count;
	    	DBConn conn=null;

	    	try{
	    		String hql="select count(*) from AAnalysisTP aa where 1=1";
	    		if(aAnalysisTPForm.getATName()!=null && !"".equals(aAnalysisTPForm.getATName())){
	    			hql+="  and aa.ATName like '%"+aAnalysisTPForm.getATName()+"%'";
	    		}
	    		if(aAnalysisTPForm.getAnalyTypeID()!=null && !aAnalysisTPForm.getAnalyTypeID().equals(new Integer("0"))){
	    			hql+="  and aa.AnalyTypeID ="+aAnalysisTPForm.getAnalyTypeID();
	    		}
	    		conn=new DBConn();
	    		List list=conn.openSession().find(hql);
	    		if(list != null && list.size() > 0){
	    			count = ((Integer)list.get(0)).intValue();
	    		}
	    	}catch(Exception e){   
	    		count = 0;
	    		e.printStackTrace();
	    		log.printStackTrace(e);
	    	}finally{
	    		if(conn!=null) conn.closeSession();
	    	}
	    	return count;    	
	    }
	 /**
     * ����ͳ�Ʒ���ģ���¼
     * @author wh
     * @param aAnalysisTPForm
     * @return replist
     */
    public static List selectAnalysisReport(AAnalysisTPForm aAnalysisTPForm,int offset,int limit){
    	List reslist=new ArrayList();
    	if(aAnalysisTPForm == null) return null;
    	DBConn conn=null;
    	Session session = null;

    	AAnalysisTP aAnalysisTP=null;
    	try{
    		String hql="from AAnalysisTP aa where 1=1";
    		if(aAnalysisTPForm.getATName()!=null && !"".equals(aAnalysisTPForm.getATName())){
    			hql+="  and aa.ATName like '%"+aAnalysisTPForm.getATName()+"%'";
    		}
    		if(aAnalysisTPForm.getAnalyTypeID()!=null && !aAnalysisTPForm.getAnalyTypeID().equals(new Integer("0"))){
    			hql+="  and aa.AnalyTypeID ="+aAnalysisTPForm.getAnalyTypeID();
    		}
    		hql+="  order by aa.AnalyTypeID";
    		conn = new DBConn();
    		session = conn.openSession();
    		
    		Query query = session.createQuery(hql);
    		query.setFirstResult(offset);
			query.setMaxResults(limit);			
    		List list = query.list();
    		
    		for(int i=0;i<list.size();i++){
    			aAnalysisTP=(AAnalysisTP)list.get(i);
    			AAnalysisTPForm aTPForm=new AAnalysisTPForm();	
    			aTPForm.setATId(aAnalysisTP.getATiD());
    			aTPForm.setATName(aAnalysisTP.getATName());
    			aTPForm.setAnalyTypeID(aAnalysisTP.getAnalyTypeID());
    			aTPForm.setAnalyTypeName(selectAnalysisTtpe(aAnalysisTP.getAnalyTypeID(),session));
    			reslist.add(aTPForm);
    		}
    	}catch(Exception e)
    	{   reslist=null;
    		log.printStackTrace(e);
    	}finally{
    		if(conn!=null)
    		conn.closeSession();
    	}
    	return reslist;
    	
    }
    /**
     * ����ͳ�Ʒ���ģ���¼
     * @author wh
     * @param aAnalysisTPForm
     * @return replist
     */
    public static List selectAnalysisReport(AAnalysisTPForm aAnalysisTPForm){
    	List reslist=new ArrayList();
    	if(aAnalysisTPForm == null) return null;
    	DBConn conn=null;
    	Session session = null;

    	AAnalysisTP aAnalysisTP=null;
    	try{
    		String hql="from AAnalysisTP aa where 1=1";
    		if(aAnalysisTPForm.getATName()!=null && !"".equals(aAnalysisTPForm.getATName())){
    			hql+="  and aa.ATName like '%"+aAnalysisTPForm.getATName()+"%'";
    		}
    		if(aAnalysisTPForm.getAnalyTypeID()!=null && !aAnalysisTPForm.getAnalyTypeID().equals(new Integer("0"))){
    			hql+="  and aa.AnalyTypeID ="+aAnalysisTPForm.getAnalyTypeID();
    		}
    		hql+="  order by aa.AnalyTypeID";
    		conn = new DBConn();
    		session = conn.openSession();
    		
    		Query query = session.createQuery(hql);		
    		List list = query.list();
    		
    		for(int i=0;i<list.size();i++){
    			aAnalysisTP=(AAnalysisTP)list.get(i);
    			AAnalysisTPForm aTPForm=new AAnalysisTPForm();	
    			aTPForm.setATId(aAnalysisTP.getATiD());
    			aTPForm.setATName(aAnalysisTP.getATName());
    			aTPForm.setAnalyTypeID(aAnalysisTP.getAnalyTypeID());
    			aTPForm.setAnalyTypeName(selectAnalysisTtpe(aAnalysisTP.getAnalyTypeID(),session));
    			reslist.add(aTPForm);
    		}
    	}catch(Exception e)
    	{   reslist=null;
    		log.printStackTrace(e);
    	}finally{
    		if(conn!=null)
    		conn.closeSession();
    	}
    	return reslist;
    	
    }
    /**
     * ����һ���������ж�������¼
     * @author wh
     * @param aAnalysisTPForm
     * @return map
     */
    public static HashMap selectOneAnalysisCount(AAnalysisTPForm aAnalysisTPForm){
    	HashMap map=new HashMap();
    	if(aAnalysisTPForm == null) return null;
    	
      	 DBConn conn=null;
         Session session=null;
         Connection connection=null;
    	try{
    		conn = new DBConn();
            session = conn.beginTransaction();
            connection=session.connection();
            //�жϻ��ܼ�¼�Ƿ����
            int count=0;
            int typeId=0;
            
            String whereSql="Select ANALY_TYPE_ID,COUNT( AT_ID)  from A_ANALYSIS_TP  GROUP BY ANALY_TYPE_ID";
            Statement st=connection.createStatement();
           
            ResultSet rs=st.executeQuery(whereSql);
            while(rs.next()){
            	
            	typeId=rs.getInt(1);
            	count=rs.getInt(2);
            	map.put(new Integer(typeId), new Integer(count));
            }
    	}catch(Exception e)
    	{   map=null;
    		log.printStackTrace(e);
    	}finally{
    		if(conn!=null)
    		conn.closeSession();
    	}
    	return map;
    	
    }
    public static String selectAnalysisTtpe(Integer analyTypeID,Session session){
    	String  result=null;
    	if(analyTypeID == null) return null;
    	try{
    		String hql="select aa.AnalyTypeName from AnalysisTPTYPE aa where aa.AnalyTypeID="+analyTypeID;

    		Query query = session.createQuery(hql);
    		List list = query.list();
    		if(list!=null && list.size()>0){
    			result=(String)list.get(0);
    		}

    	}catch(Exception e)
    	{   result=null;
    		log.printStackTrace(e);
    	}
    	return result;
    	
    }
    public static FromCompareForm getCompareResult(String compareValue,FromCompareForm fromCompareForm,String org){
    	FromCompareForm resultForm = new FromCompareForm();
    	if(compareValue == null || fromCompareForm==null  ){
    		return resultForm;
    	}

    	 DBConn conn=null;
         Session session=null;
         Connection connection=null;
    	try{
    		conn = new DBConn();
            session = conn.beginTransaction();
            connection=session.connection();
            Double dvalue=null;
    		double currValue=0.0d;
            double pre = 0.0d;
            double	preTow=0.0d;
            double  preYear=0.0d;
            
            String versionID=fromCompareForm.getVersionId();
            
            // �õ�Ƶ��
            String freqName = null;
            String sql1 = "select REP_FREQ_NAME from M_REP_FREQ where REP_FREQ_ID in (Select REP_FREQ_ID from M_ACTU_REP where CHILD_REP_ID='"+fromCompareForm.getReportName()+"' and VERSION_ID='"+versionID+"')";
            

            java.sql.Statement ps=connection.createStatement();
            ResultSet rs=ps.executeQuery(sql1);

            while(rs.next()){
            	freqName=rs.getString(1);
            }
            
            //  �Ƿ��б�����
            if((compareValue.indexOf('1'))>-1){
            	String prevTerm=com.cbrc.smis.proc.impl.ReportDDImpl.getPrevTerm(fromCompareForm.getYear() + "-" + fromCompareForm.getTerm(),freqName);
    			if(prevTerm==null){
    				System.out.println("û�л����������");
    				return null;
    			}
    			String arr[]=prevTerm.split("-");
    			
    			String where="child_rep_id='".toUpperCase() + fromCompareForm.getReportName() + "' and " +
    				"version_id='".toUpperCase()+versionID+"' and " +
    				"DATA_RANGE_ID=1  and " +
    				"org_id='".toUpperCase() + org + "' and " +
    				"cur_id=1   and ".toUpperCase() +	 
    				"year=".toUpperCase() + arr[0] + " and " +
    				"term=".toUpperCase() + arr[1] + " and times=1".toUpperCase();	
    
    			String sql="select report_value from report_in_info where rep_in_id=(select rep_in_id from report_in ".toUpperCase() +
    				"where " + where + ")";
    			sql+=" and CELL_ID="+fromCompareForm.getCellId();
    			 java.sql.Statement pstmt=connection.createStatement();
    			rs=pstmt.executeQuery(sql);
    			if(rs!=null && rs.next()){
    				pre=rs.getDouble("report_value".toUpperCase());    				
    			}
            }
             // �Ƿ��б�������
            if((compareValue.indexOf('2'))>-1){
            	String prevTwoTerm=com.cbrc.smis.proc.impl.ReportDDImpl.getPrevTwoTerm(fromCompareForm.getYear() + "-" + fromCompareForm.getTerm(),freqName);
    			if(prevTwoTerm==null){
    				System.out.println("û�л����������");
    				return null;
    			}
    			String arr[]=prevTwoTerm.split("-");
    			
    			String where="child_rep_id='".toUpperCase() + fromCompareForm.getReportName() + "' and " +
    				"version_id='".toUpperCase()+versionID+"' and " +
    				"DATA_RANGE_ID=1  and " +
    				"org_id='".toUpperCase() + org + "' and " +
    				"cur_id=1   and ".toUpperCase() +	 
    				"year=".toUpperCase()+ arr[0] + " and " +
    				"term=".toUpperCase() + arr[1] + " and times=1".toUpperCase();	
    
    			String sql="select report_value from report_in_info where rep_in_id=(select rep_in_id from report_in ".toUpperCase() +
    				"where " + where + ")";
    			sql+=" and CELL_ID="+fromCompareForm.getCellId();
    			 java.sql.Statement pstmtwo=connection.createStatement();
    			 rs=pstmtwo.executeQuery(sql);
    			if(rs!=null && rs.next()){
    				preTow=rs.getDouble("report_value".toUpperCase());    				
    			}
            }
            // �Ƿ��б�ȥ��ͬ��
            if((compareValue.indexOf('3'))>-1){
            	String prevyearTerm=com.cbrc.smis.proc.impl.ReportDDImpl.getPrevTwoTerm(fromCompareForm.getYear() + "-" + fromCompareForm.getTerm(),freqName);
    			if(prevyearTerm==null){
    				System.out.println("û�л����������");
    				return null;
    			}
    			String arr[]=prevyearTerm.split("-");
    			
    			String where="child_rep_id='".toUpperCase() + fromCompareForm.getReportName() + "' and " +
    				"version_id='".toUpperCase()+versionID+"' and " +
    				"DATA_RANGE_ID=1  and " +
    				"org_id='".toUpperCase() + org + "' and " +
    				"cur_id=1   and ".toUpperCase() +	 
    				"year=".toUpperCase() + arr[0] + " and " +
    				"term=".toUpperCase() + arr[1] + " and times=1".toUpperCase();	
    
    			String sql="select report_value from report_in_info where rep_in_id=(select rep_in_id from report_in ".toUpperCase() +
    				"where " + where + ")";
    			sql+=" and CELL_ID="+fromCompareForm.getCellId();
    			 java.sql.Statement pstmtwo=connection.createStatement();
    			 rs=pstmtwo.executeQuery(sql);
    			if(rs!=null && rs.next()){
    				preYear=rs.getDouble("report_value".toUpperCase());    				
    			}
            }// ��ǰֵ
            String currStrValue="";
             String where="child_rep_id='".toUpperCase() + fromCompareForm.getReportName() + "' and " +
    				"version_id='".toUpperCase()+versionID+"' and " +
    				"DATA_RANGE_ID=1  and " +
    				"org_id='".toUpperCase()+ org + "' and " +
    				"cur_id=1   and ".toUpperCase() +	 
    				"year=".toUpperCase() + fromCompareForm.getYear() + " and " +
    				"term=".toUpperCase() + fromCompareForm.getTerm() + " and times=1".toUpperCase();	
    
    			String sql="select report_value from report_in_info where rep_in_id=(select rep_in_id from report_in ".toUpperCase() +
    				"where " + where + ")";
    			sql+=" and CELL_ID="+fromCompareForm.getCellId();
    			 java.sql.Statement pstmtwo=connection.createStatement();
    			 rs=pstmtwo.executeQuery(sql);
    			if(rs!=null && rs.next()){
    				currStrValue=(rs.getBigDecimal("report_value".toUpperCase())).toString();
    				currValue=rs.getDouble("report_value".toUpperCase());    				
    			}
    			
    			
    		String sqlCellId="Select CELL_NAME from M_CELL where CHILD_REP_ID='"+fromCompareForm.getReportName()+"' and CELL_ID="+fromCompareForm.getCellId()+" ";

			 java.sql.Statement pscellName=connection.createStatement();
			 rs=pscellName.executeQuery(sqlCellId);
			if(rs!=null && rs.next()){
				resultForm.setCellName(rs.getString("CELL_NAME"));    				
			}
			// �õ�����
			String repName = findReportName(fromCompareForm.getReportName());
			
			resultForm.setReportStr(repName);
    		resultForm.setReportName(fromCompareForm.getReportName());
    		resultForm.setCellId(fromCompareForm.getCellId());
    		resultForm.setYear(fromCompareForm.getYear());
    		resultForm.setTerm(fromCompareForm.getTerm());
    	
    			String contents=currStrValue;
    			if(contents.indexOf('E')>-1||contents.indexOf('e')>-1)
    			{
    				try
    				{
    					//��������ָ�ʽ�����ʽ��ΪС�������λ
    						contents = new java.text.DecimalFormat("##0.00").format(Double.parseDouble(contents)).toString();
    	    		}
    				catch(Exception e)
    				{
    	    			e.printStackTrace();
    	    		}
    			}
    	
    	
    		resultForm.setReportValue(contents);
    		if(pre != 0.0){
    			resultForm.setPreValue((Expression.round(currValue,pre,4)*100)+"");
    		}else{
    			resultForm.setPreValue(null);
    		}
    		
    		if(preTow != 0.0){
    			
    			resultForm.setPreTowValue((Expression.round(currValue,preTow,4)*100)+"");
    		}else{
    			resultForm.setPreTowValue(null);
    		}
    		if(preYear != 0){
    			resultForm.setPreYearValue((Expression.round(currValue,preYear,4)*100)+"");
    		}else{
    			resultForm.setPreYearValue(null);
    		}
    		
    		
            connection.commit();
           
    	}catch(Exception e){
    		
    		try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
    		e.printStackTrace();
    	}finally{
    		if(connection!=null){
    			try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		}
    	}
    
    	
    	
    	return resultForm;
    }
    /**
	 * ��ѯ�����ӱ�����Ϣ
	 * 
	 * @author rds
	 * @serialData 2005-12-07
	 * 
	 * @return List
	 * @exception Exception
	 *                If the com.cbrc.smis.form.MChildReportForm objects cannot
	 *                be retrieved.
	 */
	public static String findReportName(String repId) throws Exception {
		String retVals = null;
		Map map = new HashMap();
		DBConn conn = null;
		Session session = null;
		
		try {
			String hql = "from MChildReport mcr where mcr.comp_id.childRepId='"+repId+"'";

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql);
			List list = query.list();
			
			if (list != null && list.size() > 0) {
				
				MChildReport mChildReport = new MChildReport();
					mChildReport = (MChildReport) list.get(0);
					
					 if (mChildReport.getMMainRep() != null && mChildReport.getMMainRep().getRepCnName() != null){
				        	if(mChildReport.getReportName().equals(mChildReport.getMMainRep().getRepCnName()))
				        		retVals=mChildReport.getReportName();
				        	else
				        		retVals=mChildReport.getMMainRep().getRepCnName()+ "-" + mChildReport.getReportName();
				     }else
				        		retVals = mChildReport.getReportName();
					
									
			
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return retVals;
	}
}