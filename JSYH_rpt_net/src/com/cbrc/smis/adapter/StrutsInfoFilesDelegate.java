package com.cbrc.smis.adapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import com.cbrc.auth.hibernate.Operator;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.InfoFilesForm;
import com.cbrc.smis.hibernate.FilesContent;
import com.cbrc.smis.hibernate.InfoFiles;

/**
 * 信息文件发布实体操作类
 * 
 * @author rds
 * @serialData 2005-12-17 18:35
 */
public class StrutsInfoFilesDelegate {
	/**
	 * 新建信息发布上传文件
	 * @author 曹发根
	 * @throws HibernateException 
	 */
	public static boolean newInfoFIle(InfoFilesForm infoFilesForm) throws HibernateException{
		boolean result=false;
		DBConn conn=null;
		Transaction tx=null;
		Session session=null;
		try{			
		    conn=new DBConn();
		    session=conn.beginTransaction();
		    InfoFiles infoFile=new InfoFiles();
		    
            TranslatorUtil.copyVoToPersistence(infoFile,infoFilesForm);	
		    
            Operator operator = new Operator();
            operator.setUserId(infoFilesForm.getUserId());
            infoFile.setOperator(operator);
		    
            session.save(infoFile);
            session.flush();
		    result=true;
		    		    
		}
        catch(Exception e)
        {
            result = false;
			e.printStackTrace();
		}finally{
            if(conn!=null)
                conn.endTransaction(result);
		}
		return result;
	}
	public static boolean newInfoFIle2(InfoFilesForm infoFilesForm,InputStream inStream) throws HibernateException{
		boolean result=false;
		DBConn conn=null;
		Transaction tx=null;
		Session session=null;
				
		try{			
		    conn=new DBConn();
		    session=conn.beginTransaction();
		    tx=session.beginTransaction();
		    InfoFiles infoFile=new InfoFiles();
		    
            TranslatorUtil.copyVoToPersistence(infoFile,infoFilesForm);	
		                
            Operator operator = new Operator();
            operator.setUserId(infoFilesForm.getUserId());
            infoFile.setOperator(operator);
		    
            FilesContent filesContent = new FilesContent();
            filesContent.setContent(Hibernate.createBlob(inStream));
            filesContent.setInfoFiles(infoFile);
            
            Set filesContentSet = new HashSet();
            filesContentSet.add(filesContent);
            infoFile.setFilesContents(filesContentSet);

            session.save(infoFile);
            session.flush();
            tx.commit();
		    result=true;
		    		    
		}
        catch(Exception e)
        {
            result = false;
            tx.rollback();
			e.printStackTrace();
		}finally{
            if(conn!=null)
                conn.endTransaction(result);
		}
		return result;
	}
	
	/**
	 * 浏览上传文件
	 * @return
	 * @throws HibernateException
	 */
	public static List viewupfile(int offset,int limit) throws HibernateException{
		ArrayList result=new ArrayList();
		DBConn conn = null;
		Session session = null;
		try{
			conn = new DBConn();
			session = conn.openSession();
			Query query=session.createQuery("from InfoFiles a where a.infoFileStyle='"+Config.INFO_FILES_STYLE_UP+"' order by a.recordTime desc");
			query.setFirstResult(offset).setMaxResults(limit);
            List list=query.list();
            for(int i=0;list!=null&&i<list.size();i++){
            	InfoFiles infoFiles=(InfoFiles)list.get(i);
            	InfoFilesForm infoFilesForm=new InfoFilesForm();
            	TranslatorUtil.copyPersistenceToVo(infoFiles,infoFilesForm);
                infoFilesForm.setUserName(infoFiles.getOperator().getUserName());
            	result.add(infoFilesForm);
            }
            session.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn != null) conn.closeSession();
			if(session != null) session.close();
		}
		return result;
	}
	/**
	 * 上传文件数量
	 * @throws HibernateException 
	 */
	public static int getTotalUpFile() throws HibernateException{
		int result=0;
		DBConn conn = null;
		Session session = null;
		try{
			conn = new DBConn();
			session = conn.openSession();
			Query query=session.createQuery("select count(*) from InfoFiles a where a.infoFileStyle='"+Config.INFO_FILES_STYLE_UP+"'");
			query.setFirstResult(0).setMaxResults(Config.PER_PAGE_ROWS);
            List list=query.list();
			if (list != null) {
				result = ((Integer) list.get(0)).intValue();
			}
            session.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn != null) conn.closeSession();
			if(session != null) session.close();
		}
		return result;
	}
	/**
	 * 发布文件数量
	 * @throws HibernateException 
	 */
	public static int getTotalOutFile() throws HibernateException{
		int result=0;
		DBConn conn = null;
		Session session = null;
		try{
			conn = new DBConn();
			session = conn.openSession();
			Query query=session.createQuery("select count(*) from InfoFiles a where a.infoFileStyle='"+Config.INFO_FILES_STYLE_OUT+"'");
			query.setFirstResult(0).setMaxResults(Config.PER_PAGE_ROWS);
            List list=query.list();
			if (list != null) {
				result = ((Integer) list.get(0)).intValue();
			}
            session.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn != null) conn.closeSession();
			if(session != null) session.close();
		}
		return result;
	}
	/**
	 * 浏览发布文件
	 * @return
	 * @throws HibernateException
	 */
	public static List viewoutfile(int offset,int limit) throws HibernateException{
		ArrayList result=new ArrayList();
		DBConn conn = null;
		Session session = null;
		try{
			conn = new DBConn();
			session = conn.openSession();
			Query query=session.createQuery("from InfoFiles a where a.infoFileStyle='"+Config.INFO_FILES_STYLE_OUT+"' order by a.recordTime desc");
			query.setFirstResult(offset).setMaxResults(limit);
            List list=query.list();
            for(int i=0;list!=null&&i<list.size();i++){
            	InfoFiles infoFiles=(InfoFiles)list.get(i);
            	InfoFilesForm infoFilesForm=new InfoFilesForm();
            	TranslatorUtil.copyPersistenceToVo(infoFiles,infoFilesForm);
                
                infoFilesForm.setUserName(infoFiles.getOperator().getUserName());
                
                result.add(infoFilesForm);
            }
            session.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn != null) conn.closeSession();
			if(session != null) session.close();
		}
		return result;
	}
	/**
	 * 上传文件数量
	 * @throws HibernateException 
	 */
	public static int getTotaloutFile() throws HibernateException{
		int result=0;
		DBConn conn = null;
		Session session = null;
		try{
			conn = new DBConn();
			session = conn.openSession();
			Query query=session.createQuery("select count(*) from InfoFiles where infoFileStyle='"+Config.INFO_FILES_STYLE_UP+"'");
			query.setFirstResult(0).setMaxResults(Config.PER_PAGE_ROWS);
            List list=query.list();
			if (list != null) {
				result = ((Integer) list.get(0)).intValue();
			}
            session.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn != null) conn.closeSession();
			if(session != null) session.close();
		}
		return result;
	}
	/**
	 * 测试期间用的操作员
	 * @author 曹发根
	 * @throws HibernateException 
	 */
	public static Operator getTestOPerator() throws HibernateException{
		Operator operator=null;
		DBConn conn=null;
		Transaction tx=null;
		Session session=null;
		try{			
		    conn=new DBConn();
		    session=conn.openSession();
		   // tx=session.beginTransaction();
		  operator=(Operator)session.load(Operator.class,new Long(2));
		  // System.out.println(operator.getUserName());
	
		    session.close();
		  		    		    
		}catch(Exception e){
		//	tx.rollback();
			e.printStackTrace();
		}finally{
			if(conn != null) conn.closeSession();
			if(session != null) session.close();
		}
		return operator;
	}
/**
 * 得到一条记录
 * @author 曹发根
 * @return
 * @throws HibernateException
 */
	public static InfoFilesForm find(Integer infoFilesId)
			throws HibernateException {
		InfoFilesForm result = new InfoFilesForm();
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			InfoFiles infoFiles=null;
			String hql="from InfoFiles a where a.infoFilesId="+infoFilesId;
//			InfoFiles infoFiles=(InfoFiles)session.load(InfoFiles.class,infoFilesId);
			Query query=session.createQuery(hql);
//			query.setFirstResult(0).setMaxResults(Config.PER_PAGE_ROWS);
            List list=query.list();
            if(list!=null)infoFiles=(InfoFiles)list.get(0);
			if(infoFiles!=null)
                  TranslatorUtil.copyPersistenceToVo(infoFiles,result);
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn!=null)
                conn.closeSession();
		}
		return result;
	}
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-21
	 * @param infoFilesId
	 * @return
	 * @throws HibernateException
	 */
	public static InfoFilesForm find2(Integer infoFilesId)
	throws HibernateException {
		InfoFilesForm result = new InfoFilesForm();
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			InfoFiles infoFiles=null;
			String hql="from InfoFiles a where a.infoFilesId="+infoFilesId;
			Query query=session.createQuery(hql);
		    List list=query.list();
		    FilesContent filesContent = null;
		    if(list!=null)infoFiles=(InfoFiles)list.get(0);
			if(infoFiles!=null){
				Set filesContentSet = infoFiles.getFilesContents();
				if(filesContentSet == null) filesContentSet = new HashSet();
				for(Iterator iter = filesContentSet.iterator();iter.hasNext();){
					filesContent = (FilesContent)iter.next();
					result.setBlob(filesContent.getContent());
					break;
				}
				TranslatorUtil.copyPersistenceToVo(infoFiles,result);
			}
		          
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn!=null)
		        conn.closeSession();
		}
		return result;
	}
	
	/**
	 * 删除一条记录
	 * @author 曹发根
	 * @return
	 * @throws HibernateException
	 */
		public static boolean delete(Integer infoFilesId)
				throws HibernateException {
			boolean result =false;
			DBConn conn = null;
			Session session = null;
			try {
				conn = new DBConn();
				session = conn.openSession();
				InfoFiles infoFiles=(InfoFiles)session.load(InfoFiles.class,infoFilesId);
				session.delete(infoFiles);
				session.flush();
				session.close();
				result=true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(conn != null) conn.closeSession();
				if(session != null) session.close();
			}
			return result;
		}
}