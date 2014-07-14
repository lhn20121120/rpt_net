package com.fitech.net.collect.util;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.ReportIn;
import com.fitech.dataCollect.DB2ExcelHandler;
import com.fitech.net.collect.common.DocAndBlob;

public class PreCollect {
	
	/**获得需要的报表id号*/
	public String getNeededReportsIds(String childRepId,String versionId,Integer dataRangeId,int year,int term,String childOrgIds,Integer curId)
	{
		DB2ExcelHandler db2ExcelHandler=new DB2ExcelHandler();
		List reportList=db2ExcelHandler.needCollectReports(childRepId,versionId,dataRangeId,year,term,childOrgIds,curId);
				
		String repInIds="";
		if(reportList!=null && reportList.size()!=0)
		{
			for(int i=0;i<reportList.size();i++)
			{
				repInIds += (repInIds.equals("") ? "" : ",") + ((ReportIn)reportList.get(i)).getRepInId();
			}
			
		}		
		return repInIds;
	}
	
	
	/**由id号获得相应的Document集合*/
	public ArrayList getNeededReportsDoc(String childRepId,String versionId,Integer dataRangeId,int year,int term,String childOrgIds,Integer curId)
	{
		SAXReader reader=new SAXReader();
		List blobList=null;
		DBConn conn=null;
		Session session=null;
		ArrayList alDoc=new ArrayList();
		String repInIds=this.getNeededReportsIds(childRepId,versionId,dataRangeId,year,term,childOrgIds,curId);
		if(repInIds!=null && !repInIds.equals("")){
			conn=new DBConn();
			session=conn.openSession();
			try {
				blobList=session.find("select rid.xml from ReportInData rid where rid.repInId in("+repInIds+")");
			}catch (HibernateException e) {
				e.printStackTrace();
			}finally{
				if(conn!=null)				
					conn.closeSession();				
			}

			if(blobList!=null && blobList.size()!=0)
			{
				
				Iterator it=blobList.iterator();
				while(it.hasNext())
				{
					Document doc=null;
					Blob xml=(Blob)it.next();
					try {
						doc=reader.read(xml.getBinaryStream());
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					alDoc.add(doc);
				}
				
			}
			DocAndBlob.docsToFiles(alDoc);
		}
		return alDoc;
	}
	
}
