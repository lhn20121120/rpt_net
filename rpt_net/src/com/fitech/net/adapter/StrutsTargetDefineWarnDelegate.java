package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.TargetDefineWarnForm;
import com.fitech.net.hibernate.TargetDefineWarn;

public class StrutsTargetDefineWarnDelegate{
	
//	Catch到本类的抛出的所有异常
	private static FitechException log=new FitechException(StrutsTargetDefineWarnDelegate.class);
	
	public static boolean insert(List list)
	{
		 boolean  result = false;				//置result标记
		   boolean bool = false;
		   	   
		   if (list == null || list.size()==0 ) 
			   return result;
		   
		   //连接对象的初始化
		   DBConn conn=null;
		   //会话对象的初始化
		   Session session=null;
		   try{
			   
//			   实例化连接对象
			   conn =new DBConn();
			   //会话对象为连接对象的事务属性
			   session=conn.beginTransaction();
			   // System.out.println(list.size()+"==");
			   for(int i=0;i<list.size();i++)
			   {
				  
				   TargetDefineWarnForm targetDefineForm=(TargetDefineWarnForm)list.get(i);
				   TargetDefineWarn targetDefineWarnPersistence = new TargetDefineWarn();			   
				  
			   TranslatorUtil.copyVoToPersistence(targetDefineWarnPersistence,targetDefineForm);
			   
			   
		
			   //会话对象保存持久层对象
			   session.save(targetDefineWarnPersistence);
			   
			   }
			   session.flush();
			   result=true;       //增加成功
			   bool = true;
			   
		   }
		   catch(HibernateException e){
			   //持久层的异常被捕捉
			   result = false;
			   bool = false;
			   log.printStackTrace(e);
		   }
		   finally{
			   //如果连接状态有,则断开,结束事务,返回
			   if(conn!=null) conn.endTransaction(bool);
		   }
		   return result;
	}
	/**
	 * 
	 * @param targetDefineForm
	 * @param type
	 * @return
	 */
	public static boolean delete(TargetDefineWarnForm targetDefineForm,String type)
	{
		boolean result=false;
		DBConn conn=null;
		try
		{
		String sql="from TargetDefineWarn tt where tt.targetDefine.targetDefineId="+targetDefineForm.getTargetDefineId()
		            +"  and tt.type='"+type+"'";	
		conn=new DBConn();
		Session session=conn.beginTransaction();
		session.delete(sql);
		session.flush();
		result=true;
		}
		catch(Exception e)
		{
			log.printStackTrace(e);
			result=false;
		}
		finally
		{
			if(conn!=null)conn.endTransaction(result);
		}
		return result;
	}
	public static List findAll(TargetDefineWarnForm targetDefineForm,String type)
	{
        List resList=null;
		
		if(targetDefineForm==null || targetDefineForm.getTargetDefineId()==null) return null;
		
		DBConn conn=null;
		
		try{
			String hql="from TargetDefineWarn tt  where tt.targetDefine.targetDefineId="+targetDefineForm.getTargetDefineId()
			              +"  and tt.type='"+type+"'";
			
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				for(int i=0;i<list.size();i++){
					TargetDefineWarn objs=(TargetDefineWarn)list.get(i);
					TargetDefineWarnForm  form=new TargetDefineWarnForm();
					TranslatorUtil.copyPersistenceToVo(objs,form);
					resList.add(form);
				}				
			}
		}catch(HibernateException he){
			resList=null;
			log.printStackTrace(he);
		}catch(Exception e){
			resList=null;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}
	
}