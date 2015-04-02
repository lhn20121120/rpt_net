
package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MCurUnitForm;
import com.cbrc.smis.hibernate.MCurUnit;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.AAnalysisTPForm;
import com.fitech.net.hibernate.AAnalysisTP;
/**
 * @StrutsMCurUnitDelegate ���ҵ�λ��Delegate
 * @author ����
 */
public class StrutsMCurUnitDelegate {
		
	private static FitechException log=new FitechException(StrutsMCurUnitDelegate.class);
   
	/**
	 * ���ҵ�λ����	 
	 * @param mCurUnitForm MCurUnitForm
	 * @return boolean result,�����ɹ�����true,���򷵻�false
	 * @exception Exception����׽�쳣����
	 */
	public static boolean create (MCurUnitForm mCurUnitForm) throws Exception {

		boolean result=false;				//��result���	   
		MCurUnit mCurUnitPersistence = new MCurUnit ();
	   	
		if (mCurUnitForm==null || mCurUnitForm.getCurUnitName().equals("")) 
			return  result;
	   
		//���Ӷ���ĳ�ʼ��	   
		DBConn conn=null;	   
		//�Ự����ĳ�ʼ��	   
		Session session=null;	   
		try{		   
				   
			if (mCurUnitForm.getCurUnitName()==null  || mCurUnitForm.getCurUnitName().equals(""))
				return result;
		
            //��ʾ�㵽�־ò��CopyTo����(McurUnitPresistence�־ò�����ʵ��,mCurUnitForm��ʾ�����)	
			TranslatorUtil.copyVoToPersistence(mCurUnitPersistence,mCurUnitForm);		   
			//ʵ�������Ӷ���		   
			conn =new DBConn();		   
			//�Ự����Ϊ���Ӷ������������		   
			session=conn.beginTransaction();
			//�Ự���󱣴�־ò����		   
			session.save(mCurUnitPersistence);		   
			session.flush();		   
			TranslatorUtil.copyPersistenceToVo(mCurUnitPersistence,mCurUnitForm);		   
			//��־Ϊtrue		   
			result=true;	   
		}catch(HibernateException e){		   
			//�־ò���쳣����׽		   
			log.printStackTrace(e);	   
		}finally{		 
			//�������״̬��,��Ͽ�,��������,����		   
			if(conn!=null) conn.endTransaction(result);	   
		}	   
		return result;
	}

	/**
	 * ȡ�ð�������ѯ���ļ�¼����
	 * @return int ��ѯ���ļ�¼����	
     * @param   mCurUnitForm   ������ѯ��������Ϣ�����ҵ�λ���������ƣ�
	 */
	public static int getRecordCount(MCurUnitForm mCurUnitForm){
	   
		int count=0;	   	 
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn=null;					   
		Session session=null;

		//��ѯ����HQL������	   
		StringBuffer hql = new StringBuffer("select count(*) from MCurUnit mcu");	   
		StringBuffer where = new StringBuffer("");
	   	   
		if(mCurUnitForm != null) {		
			//�����������ж�,�������Ʋ���Ϊ��			
			if (mCurUnitForm.getCurUnitName() != null && !mCurUnitForm.getCurUnitName().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "mcu.curUnitName like'%" + mCurUnitForm.getCurUnitName() + "%'");	   
		}
		try{    	 
			hql.append((where.toString().equals("")?"":" where ") + where.toString());    	  
			//conn�����ʵ����	    	  
			conn=new DBConn();    	  
			//�����ӿ�ʼ�Ự    	  
			session=conn.openSession(); 
			
			List list=session.find(hql.toString());    	  
			if(list!=null && list.size()==1){    		
				count=((Integer)list.get(0)).intValue();    	  
			}
		}catch(HibernateException he){    
			log.printStackTrace(he);      
		}catch(Exception e){    	
			log.printStackTrace(e);     
		}finally{    	
			//������Ӵ��ڣ���Ͽ��������Ự������    	  
			if(conn!=null) conn.closeSession();      
		}        
		return count;   
	}

	/**
	 * ���һ��ҵ�λ    
	 * @param mCurUnitForm MCurUnitForm ��ѯ������
	 * @return List ������ҵ���¼������List��¼�������򣬷���null
	 */
	public static List select(MCurUnitForm mCurUnitForm,int offset,int limit){

		//List���ϵĶ��� 	   
		List refVals=null;		
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn=null;	   
		Session session=null;
	   	   
		//��ѯ����HQL������	
		StringBuffer hql = new StringBuffer("from MCurUnit mcu ");							   
		StringBuffer where = new StringBuffer("");	   
	   
		if (mCurUnitForm != null) {		
			// �����������ж�,�������Ʋ���Ϊ��			
			if (mCurUnitForm.getCurUnitName()!= null && !mCurUnitForm.getCurUnitName().equals(""))			
				where.append("where mcu.curUnitName like '%"+mCurUnitForm.getCurUnitName()+"%'");
		}
		try{    	 
			//��ʼ��    	  
			hql.append(where.toString());    	  
			//conn�����ʵ����    	  
			conn=new DBConn();    	  
			//�����ӿ�ʼ�Ự    	  
			session=conn.openSession();
    	  			
			Query query=session.createQuery(hql.toString());		  
			query.setFirstResult(offset).setMaxResults(limit);		  
			List list=query.list();
		      	  
			if (list!=null){    		
				refVals = new ArrayList();    		  
				//ѭ����ȡ���ݿ����������¼		      
				for (Iterator it = list.iterator(); it.hasNext(); ) {		        
					MCurUnitForm mCurUnitFormTemp = new MCurUnitForm();		         
					MCurUnit mCurUnitPersistence = (MCurUnit)it.next();		         
					TranslatorUtil.copyPersistenceToVo(mCurUnitPersistence, mCurUnitFormTemp);		         
					refVals.add(mCurUnitFormTemp);		      
				}    	   
			}      
		}catch(HibernateException he){    	
			refVals=null;    	  
			log.printStackTrace(he);      
		}catch(Exception e){    	
			refVals=null;      	  
			log.printStackTrace(e);      
		}finally{    	
			//������Ӵ��ڣ���Ͽ��������Ự������    	  
			if(conn!=null) conn.closeSession();      
		}
		return refVals;   
	}

	/**
	 * ���»��ҵ�λ MCurUnitForm����
	 * @param   mCurUnitForm   MCurUnitForm ������ݵĶ���
	 * @exception   Exception  ���MCurUnitForm����ʧ��,��׽�׳��쳣
	 */
	public static boolean update (MCurUnitForm mCurUnitForm) throws Exception {	   
		boolean result = false;
		DBConn conn = null;
		Session session = null;

		MCurUnit mCurUnitPersistence = new MCurUnit();
		if (mCurUnitForm == null) {
			return result;
		}
		try {
			if (mCurUnitForm.getCurUnitName() == null
					|| mCurUnitForm.getCurUnitName().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(mCurUnitPersistence,
					mCurUnitForm);
			session.update(mCurUnitPersistence);

			result = true;
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * �༭����	 
	 * @param mCurUnitForm
	 *            The MCurUnitForm object containing the data used to retrieve
	 *            the object (i.e. the primary key info).
	 * @exception Exception
	 *                If the MCurUnitForm object cannot be retrieved.
	 */
	public static boolean edit(MCurUnitForm mCurUnitForm) throws Exception {	   
		boolean result=false;	   
		DBConn conn=null;	   
		Session session=null;
	  	   
		MCurUnit mCurUnitPersistence = new MCurUnit ();	  
		mCurUnitForm=new MCurUnitForm();	   
		mCurUnitForm.getCurUnitName();
	   	   
		if (mCurUnitForm==null ) return  result;

		try{	   
			if (mCurUnitForm.getCurUnitName()==null && mCurUnitForm.getCurUnitName().equals(""))
				return result;
				   
			conn=new DBConn();	   
			session=conn.beginTransaction();
	   
			mCurUnitPersistence = (MCurUnit)session.load(MCurUnit.class, mCurUnitForm.getCurUnitName());	   
			TranslatorUtil.copyVoToPersistence(mCurUnitPersistence, mCurUnitForm);         	  
		}catch(HibernateException he){		
			log.printStackTrace(he);	   
		}finally {		
			if(conn!=null) conn.endTransaction(result);	   
		}	
		return result;   
	}
   
	/**
	 * ɾ������
	 * @param   mCurUnitForm   MCurUnitForm ��ѯ���Ķ���
	 * @return   boolean  ���ɾ���ɹ��򷵻�true,����false
	 */  
	public static boolean remove (MCurUnitForm mCurUnitForm) throws Exception {    
		//�ñ�־result	   
		boolean result=false;	   
		//���ӺͻỰ����ĳ�ʼ��	   
		DBConn conn=null;	   
		Session session=null;
	 	   
		//mCurUnit�Ƿ�Ϊ��,����result	  
		if (mCurUnitForm==null || mCurUnitForm.getCurUnit()==null) return result;
	  	  
		try{	   
			//	���Ӷ���ͻỰ�����ʼ��		   
			conn=new DBConn();		   
			session=conn.beginTransaction();
		   
			//��mCurUnitForm�Ļ��ҵ�λ�Ļ�����������HQL�в�ѯ		   
			MCurUnit mCurUnit=(MCurUnit)session.load(MCurUnit.class,mCurUnitForm.getCurUnit());		 
			TranslatorUtil.copyPersistenceToVo(mCurUnit,mCurUnitForm);
			if(mCurUnit!=null){		       
				//�Ự����ɾ���־ò����               
				session.delete(mCurUnit);               
				session.flush();                              
				//ɾ���ɹ�����Ϊtrue              
				result=true;
			}
		}catch(Exception he){		 
			//��׽������쳣,�׳�           
			result = false;		   
			log.printStackTrace(he);	   
		}finally{		
			//�����������Ͽ����ӣ������Ự������		   
			if (conn!=null) conn.endTransaction(result);	   
		}
		return result;   
	}
	/**
	 * ɾ������
	 * @param   mCurUnitForm   MCurUnitForm ��ѯ���Ķ���
	 * @return   boolean  ���ɾ���ɹ��򷵻�true,����false
	 */  
	public static boolean remove (AAnalysisTPForm aAnalysisTPForm ) throws Exception {    
		//�ñ�־result	   
		boolean result=false;	   
		//���ӺͻỰ����ĳ�ʼ��	   
		DBConn conn=null;	   
		Session session=null;
	 	   
		//mCurUnit�Ƿ�Ϊ��,����result	  
		if (aAnalysisTPForm==null || aAnalysisTPForm.getATId()==null) return result;
	  	  
		try{	   
			//	���Ӷ���ͻỰ�����ʼ��		   
			conn=new DBConn();		   
			session=conn.beginTransaction();
		   
			//��mCurUnitForm�Ļ��ҵ�λ�Ļ�����������HQL�в�ѯ		   
			AAnalysisTP at=(AAnalysisTP)session.load(AAnalysisTP.class,aAnalysisTPForm.getATId());		 
		 
			if(at!=null){		       
				//�Ự����ɾ���־ò����               
				session.delete(at);               
				session.flush();                              
				//ɾ���ɹ�����Ϊtrue              
				result=true;
			}
		}catch(Exception he){		 
			//��׽������쳣,�׳�           
			result = false;		   
			log.printStackTrace(he);	   
		}finally{		
			//�����������Ͽ����ӣ������Ự������		   
			if (conn!=null) conn.endTransaction(result);	   
		}
		return result;   
	}

	/**
	  * ��ѯһ����¼,���ص�EditAction��
	  * @param mCurUnitForm MCurUnitForm
	  * @return MCurUnitForm ����һ����¼
	  */
	public static MCurUnitForm selectOne(MCurUnitForm mCurUnitForm){
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn=null;	  
		Session session=null;

		try{    	  
			if (mCurUnitForm.getCurUnit() != null && !mCurUnitForm.getCurUnit().equals("")){
				//conn�����ʵ����		      	  
				conn=new DBConn();    	  
				//�����ӿ�ʼ�Ự	    	  
				session=conn.openSession();
	    		    	 
				MCurUnit mCurUnitPersistence = (MCurUnit)session.load(MCurUnit.class, mCurUnitForm.getCurUnit());	    	  
				TranslatorUtil.copyPersistenceToVo(mCurUnitPersistence, mCurUnitForm);
			}
		}catch(HibernateException he){    	
			log.printStackTrace(he);     
		}catch(Exception e){    	
			log.printStackTrace(e);      
		}finally{    	
			//������Ӵ��ڣ���Ͽ��������Ự������    	  
			if(conn!=null) conn.closeSession();      
		}	    
		return mCurUnitForm;   
	}
	
	/**
	 * ���ݻ��ҵ�λ�����ƻ�ȡ���ҵ�λ��IDֵ
	 * 
	 * @param curUnitName String ���ҵ�λ����
	 * @return Integer ���û���ҵ�ָ���Ļ��ҵ�λ������null
	 */
	public static Integer getCurUnitID(String curUnitName){		
		if(curUnitName==null) return null;		
		Integer curUnit=null;		
		DBConn conn=null;
		
		try{
			String hql="from MCurUnit mcu where mcu.curUnitName='" + curUnitName + "'";
			
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				MCurUnit mCurUnit=(MCurUnit)list.get(0);
				curUnit=mCurUnit.getCurUnit();
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}finally{
			if(conn!=null) conn.closeSession();
		}		
		return curUnit;
	}
		
    
	/**
	 * ���ݻ���ID��ȡ���Ҷ���
	 * 
	 * @param curUnitName String ���ҵ�λ����
	 * @return Integer ���û���ҵ�ָ���Ļ��ҵ�λ������null
	 */	
	public static MCurUnit getMCurUnit(String curUnitName){
			
		if(curUnitName==null) return null;
		MCurUnit mCurUnit=null;				
		DBConn conn=null;

		try{	
			String hql="from MCurUnit mcu where mcu.curUnitName='" + curUnitName + "'";
			conn=new DBConn();		
			
			List list=conn.openSession().find(hql);			
			if(list!=null && list.size()>0){			
				mCurUnit=(MCurUnit)list.get(0);				
			}			
		}catch(HibernateException he){		
			log.printStackTrace(he);			
		}finally{		
			if(conn!=null) conn.closeSession();			
		}
		return mCurUnit;		
	}  
	
	/**
	 * Retrieve all existing MCurUnitForm objects.
	 *
	 * @exception   Exception   If the MCurUnitForm objects cannot be retrieved.
	 */
	public static List findAll () throws Exception {      
		List retVals =null;
		DBConn conn=null;
		
		try{    	
			String hql="from MCurUnit mcu order by mcu.curUnitName asc";    	  
			conn=new DBConn();

			List list=conn.openSession().find(hql);    	  
			if(list!=null && list.size()>0){    		
				retVals=new ArrayList();				 
    		  
				for(int i=0;i<list.size();i++){    			
					MCurUnit mCurUnit=(MCurUnit)list.get(i);    			  
					MCurUnitForm mCurUnitForm=new MCurUnitForm();    			  
					TranslatorUtil.copyPersistenceToVo(mCurUnit,mCurUnitForm);
					retVals.add(mCurUnitForm);    		  
				}    	  
			}      
		}catch(HibernateException he){    	
			log.printStackTrace(he);      
		}catch(Exception e){    	
			log.printStackTrace(e);     
		}finally{    	
			if(conn!=null) conn.closeSession();      
		}           
		return retVals;
	}
}
