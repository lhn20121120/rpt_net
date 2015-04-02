package com.fitech.net.collect.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.dom4j.io.XMLWriter;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.ReportIn;

public class Test3 {
	public static void main(String[] args)
	{
		ArrayList alDoc=getRepDocs(getRepIds());
		for(int i=0;i<alDoc.size();i++)
		{
			try {
				File file=new File("D:\\wdw\\collectTest\\get\\test"+(i+1)+".xml");
				FileOutputStream fos=new FileOutputStream(file);
				XMLWriter writer=new XMLWriter(fos);
				writer.write((Document)alDoc.get(i));
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static String getRepIds()
	{
	ReportIn ri=new ReportIn();
	DBConn conn=null;
	Session session=null;
	ReportIn repIn=new ReportIn();
	conn=new DBConn();
	session=conn.openSession();
	String hql="from ReportIn ri where "
				+"ri.MChildReport.comp_id.childRepId='G2300' and "
				+"ri.MChildReport.comp_id.versionId='0512' and "
				+"ri.MDataRgType.dataRangeId=1 and "
				+"ri.year=2005 and "
				+"ri.term=12";
	List reportList=null;
	try {
		reportList = session.find(hql);
		for(int j=0;j<reportList.size();j++)
			 System.out.println(((ReportIn)reportList.get(j)).getRepInId());
	} catch (HibernateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally{
		if(conn!=null)conn.closeSession();
	}
	
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
	
	public static ArrayList getRepDocs(String s)
	{
		SAXReader reader=new SAXReader();
		List blobList=null;
		DBConn conn=null;
		Session session=null;
		ArrayList alDoc=new ArrayList();
		
		
		conn=new DBConn();
		session=conn.openSession();
		try {
			blobList=session.find("select rid.xml from ReportInData rid where rid.repInId in("+s+")");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return alDoc;
	}
}
