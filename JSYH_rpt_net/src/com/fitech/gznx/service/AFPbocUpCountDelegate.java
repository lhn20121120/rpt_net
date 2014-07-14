package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.RhReportForm;
import com.fitech.gznx.po.AfPbocupcount;

public class AFPbocUpCountDelegate {
	
	/****
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfPbocupcount
	 * @param filename
	 * @return
	 */
	public static int saveorupdateAfPbocupcount(String filename){
		int time = 0;
		boolean result = true;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;
		int num = getTimes(filename);
		// ��ѯ����HQL������
		try {
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.beginTransaction();
			
			AfPbocupcount count = new AfPbocupcount();
			count.setFileName(filename);
			if(num > 0){
				time = num + 1;	
				if(time >9){
					time = 1;
				}
				count.setTimes(new Long(time));
				session.update(count);
			}else{
				time = 1;				
				count.setTimes(new Long(time));
				session.save(count);
			}
			
			session.flush();
			result = true;
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.endTransaction(result);
		}
		return time;
	}
	
	public static int getTimes(String filename){
		int time=0;
		DBConn conn =null;
		List<RhReportForm> pList = new ArrayList();
		
	    if(!StringUtil.isEmpty(filename))
	    {
	        try
	        {
	            conn = new DBConn();
	            
	            String hql="select t.times from AfPbocupcount t where t.fileName='"+filename+"'" ;
	            List list = conn.openSession().find(hql);
	            if (list != null && list.size() > 0) {
	            	time = ((Long)list.get(0)).intValue();
	            }
	        } catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	        finally{
	        	if(conn != null) 
	    			conn.closeSession();
	        }
	    }
	   
		
		return time;
	}
}
