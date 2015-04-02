
/** ¼§»³±¦
	 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
	 * It has a set of methods to handle persistence for MRepRange data (i.e. 
	 * com.gather.struts.forms.MRepRangeForm objects).
	 * 
	 */

package com.gather.adapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.gather.common.StringUtil;
import com.gather.dao.DBConn;
import com.gather.struts.forms.MChildReportForm;
public class DBfenye {

  public static List findWithPage(int pageSize, int startRow, String[] SubReportIds) throws
      HibernateException {
    List vehicleList = null;
   // Transaction tx = null;
    List list=null;
    
    DBConn conn=null;
    Session session=null;
    
    try {
    	conn=new DBConn();
 	    session=conn.openSession();
        //  tx = session.beginTransaction();
 	    for(int i=0;i<SubReportIds.length;i++)
 	    {
 	    	// System.out.println(SubReportIds[1]);
 	    }
 	    String sql="from com.gather.hibernate.MChildReport as obj1 where obj1.comp_id.childRepId in (" + 
		StringUtil.getStrForSqlIN(SubReportIds) + ")";
        Query q = session.createQuery(sql);
        q.setFirstResult(startRow);
        q.setMaxResults(pageSize);
        list = q.list();
        if(list!=null && list.size()>0) 
        	{
        	vehicleList=new ArrayList();
        	// System.out.println(list.size());
        	}
      
        for (Iterator it = list.iterator(); it.hasNext(); ) {
    	  MChildReportForm mchildreportform=new  MChildReportForm();
    	  com.gather.hibernate.MChildReport mchildreport =(com.gather.hibernate.MChildReport)it.next();
   
    	  TranslatorUtil.copyPersistenceToVo(mchildreport, mchildreportform);
    	  vehicleList.add(mchildreportform);
        }
    } catch (HibernateException he) {
       	he.printStackTrace();
    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		if(conn!=null) conn.closeSession();
	}
    return vehicleList;
  }
 // wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
  /*public static List findWithPage(int pageSize, int startRow) throws
  HibernateException {
List vehicleList = null;
// Transaction tx = null;
List list=null;

DBConn conn=null;
Session session=null;

try {
	conn=new DBConn();
	    session=conn.openSession();
    Query q = session.createQuery(sql);
    q.setFirstResult(startRow);
    q.setMaxResults(pageSize);
    list = q.list();
    if(list!=null && list.size()>0) 
    	{
    	vehicleList=new ArrayList();
    	// System.out.println(list.size());
    	}
  
    for (Iterator it = list.iterator(); it.hasNext(); ) {
	  MChildReportForm mchildreportform=new  MChildReportForm();
	  com.gather.hibernate.MChildReport mchildreport =(com.gather.hibernate.MChildReport)it.next();

	  TranslatorUtil.copyPersistenceToVo(mchildreport, mchildreportform);
	  vehicleList.add(mchildreportform);
    }
} catch (HibernateException he) {
   	he.printStackTrace();
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}finally{
	if(conn!=null) conn.closeSession();
}
return vehicleList;
}
  
  public static int  getRows(List list1) throws
      HibernateException {
    int totalRows = 0;
    
    	if(list1==null)
    	{
    	//	// System.out.print("fthfgh");
    		return 0;
    		}
    	else
    		totalRows=list1.size();
       //    // System.out.println("shuju"+totalRows);
    return totalRows;
  }
  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  public int getRows(String query) throws
  Exception {
int totalRows = 0;
DBConn conn=null;
Session session=null;
try {
	conn=new DBConn();
    session=conn.openSession();
  totalRows = ((Integer) session.iterate(query).next()).
              intValue();
} catch (Exception e) {
	e.printStackTrace();
} finally {
	if(conn!=null) conn.closeSession();
}

return totalRows;
}*/

}
