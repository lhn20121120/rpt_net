
package com.gather.adapter;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Session;

import com.gather.common.Log;
import com.gather.dao.DBConn;
import com.gather.hibernate.OtherFile;
import com.gather.refer.files.UploadShowForm;
/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for OtherFile data (i.e. 
 * com.gather.struts.forms.OtherFileForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsOtherFileDelegate {
    /**
     * @author linfeng
     * @function �����ļ��ϴ���¼
     * @param otherFileList
     * @return boolean �ɹ���ʧ��
     */
	public static boolean insert(List otherFileList){
		if(otherFileList==null || otherFileList.size()<1) return false;
		DBConn conn=new DBConn();
		Session session=conn.beginTransaction();
		int maxId=getMaxId();
		for(int i=0;i<otherFileList.size();i++){
			UploadShowForm myBean=(UploadShowForm)otherFileList.get(i);
			OtherFile myFile=new OtherFile();
			myFile.setOtherFileId(new Integer(++maxId));
			myFile.setFileName(myBean.getFileName());
			myFile.setOrgId(myBean.getOrgId());
			myFile.setOperator(myBean.getOperator());
			myFile.setUpReportDate(new java.util.Date());
			try{
			    session.save(myFile);
			}catch(Exception e){
				new Log(StrutsOtherFileDelegate.class).info(":::class:StrutsOtherFileDelegate --  method: insert(List otherFileList) �쳣��"+e.getMessage());
				e.printStackTrace();
				conn.endTransaction(false);
				//throw e;
				return false;
			}	
		}
		conn.endTransaction(true);
		return true;
	}
	
    /**
     * @author linfeng
     * @function �����ļ��ϴ���¼
     * @param fileName String
     * @param orgId String
     * @param operator String
     * @return boolean �ɹ���ʧ��
     */
	public static boolean insert(String fileName,String orgId,String operator){
		if(fileName==null || orgId==null) return false;
		DBConn conn=new DBConn();
		Session session=conn.beginTransaction();
		int maxId=getMaxId();
			OtherFile myFile=new OtherFile();
			myFile.setOtherFileId(new Integer(++maxId));
			myFile.setFileName(fileName);
			myFile.setOrgId(orgId);
			myFile.setOperator(operator);
			myFile.setUpReportDate(new java.util.Date());
			try{
			    session.save(myFile);
			}catch(Exception e){
				new Log(StrutsOtherFileDelegate.class).info(":::class:StrutsOtherFileDelegate --  method: insert(String fileName,String orgId,String operator) �쳣��"+e.getMessage());
				e.printStackTrace();
				conn.endTransaction(false);
				//throw e;
				return false;
			}	
		conn.endTransaction(true);
		return true;
	}
	
	/**
	 * @author linfeng
	 * @function �õ�id�����ֵ
	 * @return int 
	 */
	public static int getMaxId(){
		DBConn conn=new DBConn();
		Session session=conn.openSession();
		String hsql="select max(obj.otherFileId) from com.gather.hibernate.OtherFile as obj";
		// System.out.println("otherFile getMaxId() is: "+hsql);
		List list=new ArrayList();
		try{
		  list.addAll(session.find(hsql));
		}catch(Exception e){
			new Log(StrutsOtherFileDelegate.class).info(":::class:StrutsOtherFileDelegate --  method:  getMaxId() �쳣��"+e.getMessage());
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(session!=null) session.close();
			}catch(Exception e){e.printStackTrace();}
		}
		if(list.get(0)==null) return 0;
		return ((Integer)list.get(0)).intValue();
	}
}