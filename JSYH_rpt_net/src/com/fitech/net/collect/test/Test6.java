package com.fitech.net.collect.test;

import java.util.List;
import java.util.StringTokenizer;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.bean.Report;

public class Test6 {
	
	private static FitechException log = new FitechException(StrutsMChildReportDelegate.class);

	public  boolean test6(Report report)
	{
		boolean flag=false;
		DBConn conn=null;
		try
		{
			conn=new DBConn();
			Session session=conn.openSession();
//			List list=session.find("from ReportIn ri where ri.MChildReport.comp_id.childRepId='"+report.getChildId()+"'"
//					+" and ri.orgId='"+report.getOrgId()+"'"				
//					+" and ri.year="+Integer.parseInt(report.getYear())
//					+" and ri.term="+Integer.parseInt(report.getMonth())
//					+" and ri.MCurr.curId="+Integer.parseInt(report.getStyle())
//					+" and ri.MDataRgType.dataRangeId="+Integer.parseInt(report.getRange())	
//					+" and ri.checkFlag=1"
//			);			
			
			String hql = "from ReportIn ri where ri.checkFlag=1 and "
				+ "ri.times=(select max(r.times) from ReportIn r where "
				+ "ri.MChildReport.comp_id.childRepId=r.MChildReport.comp_id.childRepId and "
				+ "ri.MChildReport.comp_id.versionId=r.MChildReport.comp_id.versionId and "
				+ "ri.MDataRgType.dataRangeId=r.MDataRgType.dataRangeId and "
				+ "ri.orgId=r.orgId and ri.MCurr.curId=r.MCurr.curId and "
				+ "ri.year=r.year and ri.term=r.term)"
				+" and ri.MChildReport.comp_id.childRepId='"+report.getChildId()+"'"
				+" and ri.MChildReport.comp_id.versionId='"+report.getVersionId()+"'"
				+" and ri.orgId='"+report.getOrgId()+"'"		
				+" and ri.MDataRgType.dataRangeId="+Integer.parseInt(report.getRange())	
				+" and ri.MCurr.curId="+Integer.parseInt(report.getStyle())
				+" and ri.year="+Integer.parseInt(report.getYear())
				+" and ri.term="+Integer.parseInt(report.getMonth());
			
			List list=session.find(hql);
			// System.out.println(list.size());
			
			if(list!=null && list.size()!=0)
			{
				ReportIn in=(ReportIn)list.get(0);
				if(Integer.parseInt(String.valueOf(in.getForseReportAgainFlag()))==1)
				{
					//需要强制重报
					flag=true;
				}
			}			
		}		
		catch (HibernateException he) {
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.closeSession();
		}		
		return flag;
	}
	
	
	
	/**
	 * 通过文件名字获得Report对象
	 */
	public void getReportByFileName()
	{
		String name="C003H101110101001_G4100_0512_2005_12";
		StringTokenizer token=new StringTokenizer(name,"_");
		while(token.hasMoreTokens())
		{
			String namep=token.nextToken();
			// System.out.println(namep);
		}
	}
	public static void main(String args[])
	{
		Test6 test6=new Test6();
		test6.getReportByFileName();
//		Report report=new Report();
//		report.setChildId("G4100");
//		report.setMonth("12");
//		report.setYear("2005");
//		report.setOrgId("C003H101110101001   ");
//		report.setRange("1");
//		report.setStyle("1");
//		report.setVersionId("0512");
//		
//		if(test6.test6(report))
//		{
//			// System.out.println("需要重报");
//		}
//		else
//		{
//			// System.out.println("不需要重报");
//		}
		
		
	}
}
