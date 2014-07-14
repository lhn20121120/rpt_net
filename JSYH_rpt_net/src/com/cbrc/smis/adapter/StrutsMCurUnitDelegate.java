
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
 * @StrutsMCurUnitDelegate 货币单位表单Delegate
 * @author 唐磊
 */
public class StrutsMCurUnitDelegate {
		
	private static FitechException log=new FitechException(StrutsMCurUnitDelegate.class);
   
	/**
	 * 货币单位新增	 
	 * @param mCurUnitForm MCurUnitForm
	 * @return boolean result,新增成功返回true,否则返回false
	 * @exception Exception，捕捉异常处理
	 */
	public static boolean create (MCurUnitForm mCurUnitForm) throws Exception {

		boolean result=false;				//置result标记	   
		MCurUnit mCurUnitPersistence = new MCurUnit ();
	   	
		if (mCurUnitForm==null || mCurUnitForm.getCurUnitName().equals("")) 
			return  result;
	   
		//连接对象的初始化	   
		DBConn conn=null;	   
		//会话对象的初始化	   
		Session session=null;	   
		try{		   
				   
			if (mCurUnitForm.getCurUnitName()==null  || mCurUnitForm.getCurUnitName().equals(""))
				return result;
		
            //表示层到持久层的CopyTo方法(McurUnitPresistence持久层对象的实例,mCurUnitForm表示层对象)	
			TranslatorUtil.copyVoToPersistence(mCurUnitPersistence,mCurUnitForm);		   
			//实例化连接对象		   
			conn =new DBConn();		   
			//会话对象为连接对象的事务属性		   
			session=conn.beginTransaction();
			//会话对象保存持久层对象		   
			session.save(mCurUnitPersistence);		   
			session.flush();		   
			TranslatorUtil.copyPersistenceToVo(mCurUnitPersistence,mCurUnitForm);		   
			//标志为true		   
			result=true;	   
		}catch(HibernateException e){		   
			//持久层的异常被捕捉		   
			log.printStackTrace(e);	   
		}finally{		 
			//如果连接状态有,则断开,结束事务,返回		   
			if(conn!=null) conn.endTransaction(result);	   
		}	   
		return result;
	}

	/**
	 * 取得按条件查询到的记录条数
	 * @return int 查询到的记录条数	
     * @param   mCurUnitForm   包含查询的条件信息（货币单位，货币名称）
	 */
	public static int getRecordCount(MCurUnitForm mCurUnitForm){
	   
		int count=0;	   	 
		//连接对象和会话对象初始化	   
		DBConn conn=null;					   
		Session session=null;

		//查询条件HQL的生成	   
		StringBuffer hql = new StringBuffer("select count(*) from MCurUnit mcu");	   
		StringBuffer where = new StringBuffer("");
	   	   
		if(mCurUnitForm != null) {		
			//查找条件的判断,查找名称不可为空			
			if (mCurUnitForm.getCurUnitName() != null && !mCurUnitForm.getCurUnitName().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "mcu.curUnitName like'%" + mCurUnitForm.getCurUnitName() + "%'");	   
		}
		try{    	 
			hql.append((where.toString().equals("")?"":" where ") + where.toString());    	  
			//conn对象的实例化	    	  
			conn=new DBConn();    	  
			//打开连接开始会话    	  
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
			//如果连接存在，则断开，结束会话，返回    	  
			if(conn!=null) conn.closeSession();      
		}        
		return count;   
	}

	/**
	 * 查找货币单位    
	 * @param mCurUnitForm MCurUnitForm 查询表单对象
	 * @return List 如果查找到记录，返回List记录集；否则，返回null
	 */
	public static List select(MCurUnitForm mCurUnitForm,int offset,int limit){

		//List集合的定义 	   
		List refVals=null;		
		//连接对象和会话对象初始化	   
		DBConn conn=null;	   
		Session session=null;
	   	   
		//查询条件HQL的生成	
		StringBuffer hql = new StringBuffer("from MCurUnit mcu ");							   
		StringBuffer where = new StringBuffer("");	   
	   
		if (mCurUnitForm != null) {		
			// 查找条件的判断,查找名称不可为空			
			if (mCurUnitForm.getCurUnitName()!= null && !mCurUnitForm.getCurUnitName().equals(""))			
				where.append("where mcu.curUnitName like '%"+mCurUnitForm.getCurUnitName()+"%'");
		}
		try{    	 
			//初始化    	  
			hql.append(where.toString());    	  
			//conn对象的实例化    	  
			conn=new DBConn();    	  
			//打开连接开始会话    	  
			session=conn.openSession();
    	  			
			Query query=session.createQuery(hql.toString());		  
			query.setFirstResult(offset).setMaxResults(limit);		  
			List list=query.list();
		      	  
			if (list!=null){    		
				refVals = new ArrayList();    		  
				//循环读取数据库符合条件记录		      
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
			//如果连接存在，则断开，结束会话，返回    	  
			if(conn!=null) conn.closeSession();      
		}
		return refVals;   
	}

	/**
	 * 更新货币单位 MCurUnitForm对象
	 * @param   mCurUnitForm   MCurUnitForm 存放数据的对象
	 * @exception   Exception  如果MCurUnitForm更新失败,则捕捉抛出异常
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
	 * 编辑操作	 
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
	 * 删除操作
	 * @param   mCurUnitForm   MCurUnitForm 查询表单的对象
	 * @return   boolean  如果删除成功则返回true,否则false
	 */  
	public static boolean remove (MCurUnitForm mCurUnitForm) throws Exception {    
		//置标志result	   
		boolean result=false;	   
		//连接和会话对象的初始化	   
		DBConn conn=null;	   
		Session session=null;
	 	   
		//mCurUnit是否为空,返回result	  
		if (mCurUnitForm==null || mCurUnitForm.getCurUnit()==null) return result;
	  	  
		try{	   
			//	连接对象和会话对象初始化		   
			conn=new DBConn();		   
			session=conn.beginTransaction();
		   
			//将mCurUnitForm的货币单位的货币主键传入HQL中查询		   
			MCurUnit mCurUnit=(MCurUnit)session.load(MCurUnit.class,mCurUnitForm.getCurUnit());		 
			TranslatorUtil.copyPersistenceToVo(mCurUnit,mCurUnitForm);
			if(mCurUnit!=null){		       
				//会话对象删除持久层对象               
				session.delete(mCurUnit);               
				session.flush();                              
				//删除成功，置为true              
				result=true;
			}
		}catch(Exception he){		 
			//捕捉本类的异常,抛出           
			result = false;		   
			log.printStackTrace(he);	   
		}finally{		
			//如果由连接则断开连接，结束会话，返回		   
			if (conn!=null) conn.endTransaction(result);	   
		}
		return result;   
	}
	/**
	 * 删除操作
	 * @param   mCurUnitForm   MCurUnitForm 查询表单的对象
	 * @return   boolean  如果删除成功则返回true,否则false
	 */  
	public static boolean remove (AAnalysisTPForm aAnalysisTPForm ) throws Exception {    
		//置标志result	   
		boolean result=false;	   
		//连接和会话对象的初始化	   
		DBConn conn=null;	   
		Session session=null;
	 	   
		//mCurUnit是否为空,返回result	  
		if (aAnalysisTPForm==null || aAnalysisTPForm.getATId()==null) return result;
	  	  
		try{	   
			//	连接对象和会话对象初始化		   
			conn=new DBConn();		   
			session=conn.beginTransaction();
		   
			//将mCurUnitForm的货币单位的货币主键传入HQL中查询		   
			AAnalysisTP at=(AAnalysisTP)session.load(AAnalysisTP.class,aAnalysisTPForm.getATId());		 
		 
			if(at!=null){		       
				//会话对象删除持久层对象               
				session.delete(at);               
				session.flush();                              
				//删除成功，置为true              
				result=true;
			}
		}catch(Exception he){		 
			//捕捉本类的异常,抛出           
			result = false;		   
			log.printStackTrace(he);	   
		}finally{		
			//如果由连接则断开连接，结束会话，返回		   
			if (conn!=null) conn.endTransaction(result);	   
		}
		return result;   
	}

	/**
	  * 查询一条记录,返回到EditAction中
	  * @param mCurUnitForm MCurUnitForm
	  * @return MCurUnitForm 包含一条记录
	  */
	public static MCurUnitForm selectOne(MCurUnitForm mCurUnitForm){
		//连接对象和会话对象初始化	   
		DBConn conn=null;	  
		Session session=null;

		try{    	  
			if (mCurUnitForm.getCurUnit() != null && !mCurUnitForm.getCurUnit().equals("")){
				//conn对象的实例化		      	  
				conn=new DBConn();    	  
				//打开连接开始会话	    	  
				session=conn.openSession();
	    		    	 
				MCurUnit mCurUnitPersistence = (MCurUnit)session.load(MCurUnit.class, mCurUnitForm.getCurUnit());	    	  
				TranslatorUtil.copyPersistenceToVo(mCurUnitPersistence, mCurUnitForm);
			}
		}catch(HibernateException he){    	
			log.printStackTrace(he);     
		}catch(Exception e){    	
			log.printStackTrace(e);      
		}finally{    	
			//如果连接存在，则断开，结束会话，返回    	  
			if(conn!=null) conn.closeSession();      
		}	    
		return mCurUnitForm;   
	}
	
	/**
	 * 根据货币单位的名称获取货币单位的ID值
	 * 
	 * @param curUnitName String 货币单位名称
	 * @return Integer 如果没有找到指定的货币单位，返回null
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
	 * 根据货币ID获取货币对象
	 * 
	 * @param curUnitName String 货币单位名称
	 * @return Integer 如果没有找到指定的货币单位，返回null
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
