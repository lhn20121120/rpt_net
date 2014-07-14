package com.cbrc.smis.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.EExChangeRateForm;
import com.cbrc.smis.hibernate.EEXChangeRate;
import com.cbrc.smis.hibernate.EEXChangeRatePK;

public class StrutsEXChangeRateDelegate {
	/**
	 * 得到汇率列表
	 * @param exform
	 * @return
	 */
	public static List findExChangeRate(EExChangeRateForm exform){
		List re=new ArrayList();
		DBConn conn=null;
    	
    	try{
    		conn=new DBConn();
    		
    		//EEXChangeRate ex=new EEXChangeRate();
    		Session session=conn.beginTransaction();
    		StringBuffer hql=new StringBuffer("select vc.vcid,vc.vccnname,vc.vcenname,vd.vdname,ex.eeramt,vd.vdid from EEXChangeRate ex ,VDate vd,VCurrency vc ");
    		StringBuffer where=new StringBuffer(" where ex.comp_id.vcid=vc.vcid and ex.comp_id.vdid=vd.vdid ");
    		if(exform!=null){
    			if(exform.getVcid()!=null&&!exform.getVcid().equals("")){
    				where.append(" and ex.comp_id.vcid='"+exform.getVcid()+"' ");
    			}
    			if(exform.getVdid()!=null&&!exform.getVdid().equals("")){
    				where.append(" and  ex.comp_id.vdid="+exform.getVdid()+" ");
    			}
    			
    		}
    		if(!where.toString().trim().equals("where")){
    			hql.append(where);
    		}
    		List list=session.find(hql.toString());
    		for(int i=0;i<list.size();i++){
    			Object[] items=(Object[])list.get(i);
    			EExChangeRateForm exform1=new EExChangeRateForm();
    			exform1.setVcid((String)items[0]);
    			exform1.setVccnname((String)items[1]);
    			exform1.setVcenname((String)items[2]);
    			exform1.setVdname((String)items[3]);
    			exform1.setEeramt(items[4]==null?new Double(0.00):(Double)items[4]);
    			exform1.setVdid((Integer)items[5]);
    			re.add(exform1);	
    		}
    		
    	}catch(Exception he){
    		he.printStackTrace();
    	}finally{
    		if(conn!=null) conn.closeSession();
    	}
		return re;
	}
	/**
	 * 得到币种列表
	 * @param exform
	 * @return
	 */
	public static List findAllCurrency(){
		List re=new ArrayList();
		DBConn conn=null;
    	
    	try{
    		conn=new DBConn();
    		
    		EEXChangeRate ex=new EEXChangeRate();
    		Session session=conn.beginTransaction();
    		StringBuffer hql=new StringBuffer("from VCurrency");
    		
    		re=session.find(hql.toString());
    		
    	}catch(Exception he){
    		he.printStackTrace();
    	}finally{
    		if(conn!=null) conn.closeSession();
    	}
		return re;
	}
	/**
	 * 得到日期名称对应的日期ID
	 * @param 
	 * @return
	 */
	public static Integer getVdidbyname(String vdname){
		Integer re=new Integer(0);
		DBConn conn=null;
    	
    	try{
    		conn=new DBConn();
    		
    		EEXChangeRate ex=new EEXChangeRate();
    		Session session=conn.beginTransaction();
    		Connection connect=session.connection();
    		//StringBuffer hql=new StringBuffer("from VDate vd where vd.vdname='"+vdname+"'");
    		String sql="select days('"+vdname+"') from CALCULATE_TABLE";
    		Statement smt=connect.createStatement();
    		ResultSet rs=smt.executeQuery(sql);
    		if(rs.next()){
    			re=new Integer(rs.getInt(1));
    		}
//    	//	List list=session.(sql);
//    		if(list.size()==1){
//    			VDate vd=(VDate)list.get(0);
//    			re=vd.getVdid();
//    		}
    		
    	}catch(Exception he){
    		he.printStackTrace();
    	}finally{
    		if(conn!=null) conn.closeSession();
    	}
		return re;
	}
	/**
	 * 更新汇率
	 */
	public static boolean updateExChangeRate(EExChangeRateForm exform){
		boolean re=false;
		DBConn conn=null;
    	
    	try{
    		conn=new DBConn();
    		
    		//EEXChangeRate ex=new EEXChangeRate();
    		Session session=conn.beginTransaction();
    		EEXChangeRatePK comp=new EEXChangeRatePK(exform.getVcid(),exform.getVdid());
    		EEXChangeRate ex=(EEXChangeRate)session.load(EEXChangeRate.class,comp);
    		ex.setEeramt(exform.getEeramt());
    		conn.endTransaction(true);
    		re=true;
    	}catch(Exception he){
    		he.printStackTrace();
    	}finally{
    		if(conn!=null) conn.closeSession();
    	}
		return re;
		
	}

}
