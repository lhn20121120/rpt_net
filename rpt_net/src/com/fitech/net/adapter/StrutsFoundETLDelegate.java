package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.Org;
import com.cbrc.smis.hibernate.ReportIn;

import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.ETLReportForm;
import com.fitech.net.hibernate.OrgNet;

public class StrutsFoundETLDelegate {
	/*
	 * 对txt类型的ETL模版状态进行查询
	 */
	 private  FitechException log=new FitechException(StrutsFoundETLDelegate.class);
	 public Integer allPage=null;
	 /*
	  *  查询条件下的模版状态
	  */
	public  List found(ETLReportForm form){
		boolean save = false;
	    DBConn conn=null;
	    Session session=null;
	    List list=new ArrayList();
	    List result=null;
	    try{
	    	
	    	
	    	String sql="from ReportIn rep,  OrgNet o ";
	    	StringBuffer where =new StringBuffer("where");  // rep.year='"+year+"' and rep.term='"+month+"' and rep.times='-2'";
	    	conn = new DBConn();
	    	session = conn.beginTransaction();
	    	
	    	if(!form.getYear().equals(""))
	    		where.append(" rep.year='"+form.getYear()+"' ");
	    	if(!form.getMonth().equals("")){
	    		where.append(where.toString().equals("where")?"":"and"+" rep.term='"+form.getMonth()+"'");
	    	}
	    	if(!form.getRepName().equals("")){
	    		where.append((where.toString().equals("where")?"":"and")+" rep.repName like '%"+form.getRepName()+"%'");
	    	}
	    	if(!form.getOrgName().equals("")){
	    //		String orgSql="form org o where o.orgId = '"+form.getOrgName();
	    //		Query orgQuert=session.createQuery(orgSql);
	    //		Org o=(Org)orgQuert.list().get(0);
	    		
	    		where.append((where.toString().equals("where")?"":"and")+" o.orgName like '%"+form.getOrgName()+"%'");
	   // 		System.out.println("Test"); 
	    	}
	    	
	    	where.append((where.toString().equals("where")?"":"and")+" rep.orgId=o.orgId and rep.times='-2'");
	    	System.out.println(where.toString());
	    	System.out.println("~~~~~~~~~  "+sql+(where.toString().equals("where")?"":where.toString()));
	    
	    	int page=1;
	    	if(form.getPage()!=null)
	    		page=Integer.parseInt(form.getPage());
	    	int size=session.createQuery(sql+(where.toString().equals("where")?"":where.toString())).list().size();
	    	this.allPage=new Integer(size);
	    	Query quert=session.createQuery(sql+(where.toString().equals("where")?"":where.toString())).setMaxResults(10).setFirstResult((page-1)*10);
	    	list=quert.list();
	    	result=new ArrayList(list.size());
	//    	System.out.println(list.size());
	    	for(int i=0;i<list.size();i++){
	    		Object[] o=(Object [])list.get(i);
	    		ReportIn rep=(ReportIn)o[0];
	    		OrgNet org=(OrgNet)o[1];
	    		rep.orgName=org.getOrgName();
	    		result.add(i,rep);
	    	}
	    		//session.find(sql+(where.toString().equals("where")?"":where.toString()));
	    }
	    catch(Exception e){
	    	if(!save)
	    		conn.closeSession();
	    	log.printStackTrace(e);
	    }
	    finally{              
            if (conn!=null) conn.endTransaction(save);         
        }
		
		
		return result;
	}
}
