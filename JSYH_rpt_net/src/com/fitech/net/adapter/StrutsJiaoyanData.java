package com.fitech.net.adapter;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateShape;

public class StrutsJiaoyanData {
	private static  FitechException log=new FitechException(StrutsJiaoyanData.class);
	public  static void save(AfTemplateShape form){
		boolean save = false;
	    DBConn conn=null;
	    Session session=null;
	    try{
	    	conn=new DBConn();
	    	session=conn.beginTransaction();
	    	session.save(form);
	    	save=true;
	    }
	    catch(Exception e){
	    	log.printStackTrace(e);
	    }
	    finally{              
            if (conn!=null) conn.endTransaction(save);         
        }
	}
	
	public static boolean isexists(AfTemplateShape form){
		boolean save = false;
	    DBConn conn=null;
	    Session session=null;
	    try{
	    	conn=new DBConn();
	    	session=conn.openSession();
	    	String hql="select count(*) from AfTemplateShape a where a.id.templateId='"+form.getId().getTemplateId()+
	    				"' and a.id.versionId='"+form.getId().getVersionId()+"' and a.id.cellName='"+form.getId().getCellName()+"'";
	    	Integer num = (Integer)session.createQuery(hql).uniqueResult();
	    	if(num!=null && num>0)
	    		return true;
	    }
	    catch(Exception e){
	    	log.printStackTrace(e);
	    }
	    finally{              
            if (conn!=null) conn.closeSession();    
        }
	    return save;
	}
}
