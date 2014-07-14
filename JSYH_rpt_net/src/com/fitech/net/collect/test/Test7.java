package com.fitech.net.collect.test;

import java.io.File;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MChildReportPK;
public class Test7
{
	private String c="";
	
	private String r="";
	
	public void temp()
		{
			File file=new File("E:\\Tomcat\\webapps\\smis_in_net\\upload_temp\\into_xml");			
	
			if(file!=null && file.isDirectory())
			{
				File[] files=file.listFiles();
				
				for(int i=0;i<files.length;i++)
				{			
					SAXReader reader=new SAXReader();			
					try {					
						Document doc=reader.read(files[i]);				
						
						List nodes=doc.selectNodes("F/P1/*");
						
						DBConn conn=new DBConn();
						Session session=conn.openSession();
						Transaction t=session.beginTransaction();
						
						// System.out.println(nodes.size());
						for(int n=0;n<nodes.size();n++)
						{
							Node nodeone=(Node)nodes.get(n);
							
							MCell cell=new MCell();
							cell.setCellName(nodeone.getName());
							cell.setDataType(new Integer(2));
							
							MChildReport report=new MChildReport();
							MChildReportPK pk=new MChildReportPK();
							pk.setChildRepId("G4300");
							pk.setVersionId("0512");
							report.setComp_id(pk);
							cell.setMChildReport(report);
							
							if(cellInfo(nodeone.getName()))
							{							
								cell.setRowId(new Integer(getRowId()));
								cell.setColId(getColId());																											
							}
							
							session.saveOrUpdate(cell);	
							session.flush();	
							t.commit();
						}						
											
						session.close();					
						// System.out.println("@@@@@@@@@@@@@@");
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (HibernateException e) {
						// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}		
				}	
	}




		/**
		 * 分析单元格的名字
		 * true:为一般的单元格;false:为一些字段
		 */
		public boolean cellInfo(String cellname)
		{
			boolean flag=false;
			c="";
			r="";
			if(!(cellname.startsWith("fitech") || cellname.startsWith("jiaoyanwei")))
			{
				for(int i=0;i<cellname.length();i++)
				{
					if(cellname.charAt(i)>='A' && cellname.charAt(i)<='Z')
					{
						c+=cellname.charAt(i);
					}
					else
					r+=cellname.charAt(i);
				}			
				flag=true;
			}				
			return flag;
		}
		
		/**
		 * 获得列的id	
		 */
		public String getColId()
		{
			return c;
		}
		
		
		/**
		 * 获得行的id
		 * @return
		 */
		public int getRowId()
		{
			return Integer.parseInt(r);
		}

	
	public static void main(String args[])
	{
		new Test7().temp();		
	}
}