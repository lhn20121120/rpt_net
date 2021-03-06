
package com.gather.adapter;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.gather.common.Log;
import com.gather.dao.DBConn;
import com.gather.down.reports.ReportInfoBean;
import com.gather.hibernate.ReportData;
import com.gather.struts.forms.ReportDataForm;

/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for ReportData data (i.e. 
 * com.gather.struts.forms.ReportDataForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsReportDataDelegate {
	
	 public static boolean create (com.gather.struts.forms.ReportDataForm reportDataForm) throws Exception {
	      com.gather.hibernate.ReportData reportDataPersistence = new com.gather.hibernate.ReportData ();
	      try{
	    	  TranslatorUtil.copyVoToPersistence(reportDataPersistence, reportDataForm);
	    	  DBConn conn=new DBConn();
	    	  Session session=conn.beginTransaction();
	    	  session.save(reportDataPersistence);
	    	  conn.endTransaction(true);
	    	  return true;
	      }catch(Exception e)
	      {
	    	  new Log(StrutsReportDataDelegate.class).info(":::class:StrutsReportDataDelegate --  method: create 异常："+e.getMessage());
	    	  throw e;
	      }
	 }
	   /**
	 * @author James
	 *  
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号 
	 * @return pdfFile File PDF模板文件
	 * @function 根据机构ID、子报表ID和版本号获取PDF模板文件
	 */
   public static Blob getPdf(String childRepId, String versionId) {
		Blob pdfBlob = null;
		List retVals = new ArrayList();
		ReportData reportDataPersistence = null;
		ReportDataForm reportDataForm = null;

		DBConn conn = new DBConn();
		Session session = conn.openSession();
		String hql = "from com.gather.hibernate.ReportData rd where rd.comp_id.childRepId = '"
				   + childRepId + "' and rd.comp_id.versionId = '" + versionId + "'";

		try {
			retVals.addAll(session.find(hql));
			session.close();
			// System.out.println("===== results size= " + retVals.size());
			for (Iterator it = retVals.iterator(); it.hasNext();) {
				reportDataPersistence = (ReportData) it.next();				
			}
//			TranslatorUtil.copyPersistenceToVo(reportDataPersistence, reportDataForm);
		} catch (HibernateException he) {
			new Log(StrutsReportDataDelegate.class).info(":::class:StrutsReportDataDelegate --  method: getPdf 异常："+he.getMessage());
			he.printStackTrace();
		} catch (Exception e){
			new Log(StrutsReportDataDelegate.class).info(":::class:StrutsReportDataDelegate --  method: getPdf 异常："+e.getMessage());
			e.printStackTrace();
		}
		finally{
			if (session != null)
				conn.closeSession();
		}
		
		pdfBlob = reportDataPersistence.getPdf();

		return pdfBlob;
	}
   
   /**
	 * @author linfeng
	 *  
	 * @param com.gather.down.reports.ReportInfoBean List 子报表
	 * @return ReportData list 模板文件list
	 * @function 根据机构ID、子报表ID和版本号获取PDF模板文件 list
	 */
  public static List getPdfs(List repInfoList) {
	  
	    if(repInfoList==null) return null;
	    StringBuffer hsql=new StringBuffer();
	    hsql.append("from com.gather.hibernate.ReportData rd where ");
	    for(int i=0;i<repInfoList.size();i++){
	    	ReportInfoBean myBean=(ReportInfoBean)repInfoList.get(i);
	    	hsql.append("(rd.comp_id.childRepId='"+myBean.getFormModelID()+"' and " +
	    			    "rd.comp_id.versionId='"+myBean.getVersion()+"')");
	    	hsql.append(" or ");
	    }
	    String sql=hsql.toString();
	    if(sql.substring(sql.length()-3,sql.length()).trim().equals("or")){
	    	sql=sql.substring(0,sql.length()-3);
	    }
	    // System.out.println("------hsql is: "+hsql);
	    // System.out.println("------sql is : "+sql);
	    List list=new ArrayList();
	    DBConn conn=new DBConn();
	    Session session=conn.openSession();
	    try{
	    	list.addAll(session.find(sql));
	    	if(list!=null && list.size()>0){
	    		return list;
	    	}
	    }catch(Exception e){
	    	new Log(StrutsReportDataDelegate.class).info(":::class:StrutsReportDataDelegate --  method: getPdfs 异常："+e.getMessage());
	    	e.printStackTrace();
	    }finally{
	    	try{
	    		session.close();
	    	}catch(Exception e){e.printStackTrace();}
	    }
	  
	   return null;
	}
}
