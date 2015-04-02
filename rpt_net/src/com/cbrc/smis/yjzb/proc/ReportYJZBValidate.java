package com.cbrc.smis.yjzb.proc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.cbrc.smis.proc.po.ReportIn;
import com.cbrc.smis.yjzb.bean.Abnormity_actu_standard;
import com.cbrc.smis.yjzb.proc.impl.AbnormityYJZBmpl;
import com.cbrc.smis.yjzb.proc.impl.ReportInYJZBmpl;
/**
 * 报表的预警指标存储过程类
 * 
 * @author jhb
 */
public class ReportYJZBValidate {	
	/**
	 * 获得报表的实际子报表ＩＤ，版本，数据范围
	 * @param 子报表的id
	 * @return void
	 */
	public static void Validate(int _repInId) throws SQLException,Exception{
		Integer repInId=new Integer(_repInId);
	    List reportlist=null;             //report对象列表
	    String childRepId=null;           //子报表ID
		String versionId=null;            //版本号
		Integer dataRangeId=null;         // 数据范围ID
		Integer OatId=null;               //机构频度ID
		Integer rep_freq_id=null;         //报表频度ID
		Integer cur_Id=null;              //币种ID
		List  abnormity_actu_standard_list=null; //预警指标表列表
		Abnormity_actu_standard abnormity_actu_standard=null; //预警指标对象
		Connection conn=null;
		
		  try{
			 conn=DriverManager.getConnection("jdbc:default:connection");   //获取数据库连接
			 //conn=new FitechConnection().getConnect();
			  
			 ReportIn reportIn=ReportInYJZBmpl.getReportIn(conn,repInId.intValue()); //获得reportIn对象
			 childRepId=reportIn.getChildRepId();
			 versionId=reportIn.getVersionId();
			 dataRangeId=reportIn.getDataRangeId();
			 cur_Id=reportIn.getCurId();
			 String orgid=reportIn.getOrgId();       
			 Integer year=reportIn.getYear();
			 Integer term=reportIn.getTerm();
			 OatId=ReportInYJZBmpl.getOatId(conn,orgid);          //获得OAT_id 
			 
			 //// System.out.println("OatId IS"+OatId.toString());
			  
			 rep_freq_id=ReportInYJZBmpl.getRep_Freq_Id(conn,childRepId,versionId,OatId, dataRangeId); //获得报表频度
			 
			 //// System.out.println("rep_freq_id is :"+rep_freq_id.toString());
			 
			 //获得预警指标主表对象列表方法
			 abnormity_actu_standard_list=AbnormityYJZBmpl.getAbnormity_actu_standard(conn,childRepId, 
					 versionId,rep_freq_id,repInId,orgid);
			 			 
			 //// System.out.println("abnormity_actu_standard_list.size() is :"+abnormity_actu_standard_list.size());
			 
			  if(abnormity_actu_standard_list!=null && abnormity_actu_standard_list.size()>0){
				  for(int i=0;i<abnormity_actu_standard_list.size();i++)
				  {
					  abnormity_actu_standard=(Abnormity_actu_standard)abnormity_actu_standard_list.get(i);					  
					  
					  /*// System.out.println("abnormity_actu_standard.getAs_excel_item()"+abnormity_actu_standard.getAs_excel_item());
					  // System.out.println("abnormity_actu_standard.getAs_pdf_item()"+abnormity_actu_standard.getAs_pdf_item());
					  // System.out.println("abnormity_actu_standard.getVersion_id()"+abnormity_actu_standard.getVersion_id());
					  // System.out.println("abnormity_actu_standard.getChild_rep_id()"+abnormity_actu_standard.getChild_rep_id());
					  // System.out.println("abnormity_actu_standard.getAs_id()"+abnormity_actu_standard.getAs_id());
					  // System.out.println("abnormity_actu_standard.getAs_standard()"+abnormity_actu_standard.getAs_standard());*/
					  
					  String exp=abnormity_actu_standard.getAs_pdf_item();
					  switch(getExpType(exp)){
					  case 1: //::::单元格指标
						   AbnormityYJZBmpl.CellParse(conn,abnormity_actu_standard,year,term, orgid,dataRangeId,cur_Id);
						   break;
					  case 2: //::::单表单元格运算指标
						  AbnormityYJZBmpl.parseExp(conn,abnormity_actu_standard,year,term, orgid,dataRangeId,cur_Id);
						  break;
					  case 3: //:::::多表单元格运算指标
						  AbnormityYJZBmpl.parseMultiTableExp(conn,abnormity_actu_standard,year,term, orgid,dataRangeId,cur_Id);
						  break;						  						  
					  }
				  }
			   }
		     }catch(Exception e){
				e.printStackTrace();
				throw new Exception(e.getMessage());
			 }finally{
				close(conn);
			}			
	}
	
	/**
	 * 判断公式类别
	 * @param 公式 String
	 * @return 公式类别 int
	 * 1 单元格
	 * 2 单表单元格运算
	 * 3 多表单元格运算
	 */
    private static int getExpType(String exp){
    	byte[] exps=exp.getBytes();
    	if(isMultiTableExp(exps)) return 3;
    	if(isExp(exps)) return 2;
    	return 1;
    }
	
    /**
     * 判断是否是公式(规则：有+ - * / 符号的就是公式)
     *
     */
    private static boolean isExp(byte[] exps){
    	boolean b=false;
    	  for(int i=0;i<exps.length;i++){
  			if(exps[i]==42){ b=true; break;}
			if(exps[i]==43){ b=true; break;}
			if(exps[i]==45){ b=true; break;}
			if(exps[i]==47){ b=true; break;}
    	  }
    	  return b;
    }
    
    /**
     * 判断是否有多表之间的单元格公式(规则：有 _ 的就是多表共式)
     *
     */
    private static boolean isMultiTableExp(byte[] exps){
    	boolean bb=false;
    	  for(int i=0;i<exps.length;i++){
  			if(exps[i]==95){ bb=true; break;}
    	  }
    	  return bb;
    }
    
	/**
	 * 关闭数据库连接
	 * @param conn
	 * @throws Exception
	 */
	private static void close(Connection conn) throws Exception{
		if(conn !=null)
			try{
				conn.close();
			}catch(SQLException sqle)
			{
				throw new Exception(sqle.getMessage());
			}
	    }
}
