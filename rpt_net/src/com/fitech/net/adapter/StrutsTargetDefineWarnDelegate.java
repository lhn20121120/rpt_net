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
	
//	Catch��������׳��������쳣
	private static FitechException log=new FitechException(StrutsTargetDefineWarnDelegate.class);
	
	public static boolean insert(List list)
	{
		 boolean  result = false;				//��result���
		   boolean bool = false;
		   	   
		   if (list == null || list.size()==0 ) 
			   return result;
		   
		   //���Ӷ���ĳ�ʼ��
		   DBConn conn=null;
		   //�Ự����ĳ�ʼ��
		   Session session=null;
		   try{
			   
//			   ʵ�������Ӷ���
			   conn =new DBConn();
			   //�Ự����Ϊ���Ӷ������������
			   session=conn.beginTransaction();
			   // System.out.println(list.size()+"==");
			   for(int i=0;i<list.size();i++)
			   {
				  
				   TargetDefineWarnForm targetDefineForm=(TargetDefineWarnForm)list.get(i);
				   TargetDefineWarn targetDefineWarnPersistence = new TargetDefineWarn();			   
				  
			   TranslatorUtil.copyVoToPersistence(targetDefineWarnPersistence,targetDefineForm);
			   
			   
		
			   //�Ự���󱣴�־ò����
			   session.save(targetDefineWarnPersistence);
			   
			   }
			   session.flush();
			   result=true;       //���ӳɹ�
			   bool = true;
			   
		   }
		   catch(HibernateException e){
			   //�־ò���쳣����׽
			   result = false;
			   bool = false;
			   log.printStackTrace(e);
		   }
		   finally{
			   //�������״̬��,��Ͽ�,��������,����
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