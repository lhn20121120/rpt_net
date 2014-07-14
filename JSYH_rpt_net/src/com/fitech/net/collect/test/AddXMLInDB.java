package com.fitech.net.collect.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.ReportInData;

public class AddXMLInDB {
	
	public void insertXML(ArrayList al)
	{
		ReportInData repInData=null;
		DBConn conn=null;
		Session session=null;
		File file=null;
		conn=new DBConn();
		
		for(int i=0;i<al.size();i++)
		{
			try {
				repInData=new ReportInData();
				file=(File)al.get(i);
				InputStream input=new FileInputStream(file);
				int size=input.available();
				Blob xmlBlob=Hibernate.createBlob(input);
				repInData.setRepInId(new Integer(i+1050));
				repInData.setXml(xmlBlob);
				repInData.setXmlSize(new Integer(size));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				session=conn.beginTransaction();
				session.update(repInData);
				session.flush();
				// System.out.println("update is OK!");
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				conn.endTransaction(false);
				e.printStackTrace();
			}
			finally{
	            
	            if(conn!=null)conn.endTransaction(true);
	        }
			
			
		}//for
		
	}//insertXML
	
	
	public ArrayList getXML()
	{
		SAXReader reader=new SAXReader();
		ArrayList alDoc=new ArrayList();
		ArrayList alFile=new ArrayList();
		List repInList=null;
		ReportInData repInData=null;
		DBConn conn=null;
		Session session=null;
		
		try {
			conn=new DBConn();
			session=conn.openSession();
			repInList=session.find("from ReportInData r where r.repInId in(1050,1052,1053,1054,1054)");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// System.out.println("repInList size is "+repInList.size());
		
		if(repInList!=null && repInList.size()!=0){
			Iterator it=repInList.iterator();
			while(it.hasNext()){
				repInData=new ReportInData();
				repInData=(ReportInData)it.next();
				Blob xmlBlob=repInData.getXml();
				Document doc=null;
				try {
					doc = reader.read(xmlBlob.getBinaryStream());
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				alDoc.add(doc);
				if(alDoc != null&&alDoc.size()!=0)
				{
					for(int k=0;k<alDoc.size();k++)
					{
						Document doc1=(Document)alDoc.get(k);
						File file=new File("D:\\wdw\\collectTest\\get\\test"+(k+1)+".xml");
						try {
							FileOutputStream fos=new FileOutputStream(file);
							XMLWriter writer=new XMLWriter(fos);
							writer.write(doc1);
							writer.close();
							alFile.add(file);
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
					
				
			}
		}
		// System.out.println("alXML size is "+alDoc.size());
		
		return alDoc;
	}//getXML

}
