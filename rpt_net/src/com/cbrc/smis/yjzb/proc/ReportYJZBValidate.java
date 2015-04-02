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
 * �����Ԥ��ָ��洢������
 * 
 * @author jhb
 */
public class ReportYJZBValidate {	
	/**
	 * ��ñ����ʵ���ӱ���ɣģ��汾�����ݷ�Χ
	 * @param �ӱ����id
	 * @return void
	 */
	public static void Validate(int _repInId) throws SQLException,Exception{
		Integer repInId=new Integer(_repInId);
	    List reportlist=null;             //report�����б�
	    String childRepId=null;           //�ӱ���ID
		String versionId=null;            //�汾��
		Integer dataRangeId=null;         // ���ݷ�ΧID
		Integer OatId=null;               //����Ƶ��ID
		Integer rep_freq_id=null;         //����Ƶ��ID
		Integer cur_Id=null;              //����ID
		List  abnormity_actu_standard_list=null; //Ԥ��ָ����б�
		Abnormity_actu_standard abnormity_actu_standard=null; //Ԥ��ָ�����
		Connection conn=null;
		
		  try{
			 conn=DriverManager.getConnection("jdbc:default:connection");   //��ȡ���ݿ�����
			 //conn=new FitechConnection().getConnect();
			  
			 ReportIn reportIn=ReportInYJZBmpl.getReportIn(conn,repInId.intValue()); //���reportIn����
			 childRepId=reportIn.getChildRepId();
			 versionId=reportIn.getVersionId();
			 dataRangeId=reportIn.getDataRangeId();
			 cur_Id=reportIn.getCurId();
			 String orgid=reportIn.getOrgId();       
			 Integer year=reportIn.getYear();
			 Integer term=reportIn.getTerm();
			 OatId=ReportInYJZBmpl.getOatId(conn,orgid);          //���OAT_id 
			 
			 //// System.out.println("OatId IS"+OatId.toString());
			  
			 rep_freq_id=ReportInYJZBmpl.getRep_Freq_Id(conn,childRepId,versionId,OatId, dataRangeId); //��ñ���Ƶ��
			 
			 //// System.out.println("rep_freq_id is :"+rep_freq_id.toString());
			 
			 //���Ԥ��ָ����������б���
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
					  case 1: //::::��Ԫ��ָ��
						   AbnormityYJZBmpl.CellParse(conn,abnormity_actu_standard,year,term, orgid,dataRangeId,cur_Id);
						   break;
					  case 2: //::::����Ԫ������ָ��
						  AbnormityYJZBmpl.parseExp(conn,abnormity_actu_standard,year,term, orgid,dataRangeId,cur_Id);
						  break;
					  case 3: //:::::���Ԫ������ָ��
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
	 * �жϹ�ʽ���
	 * @param ��ʽ String
	 * @return ��ʽ��� int
	 * 1 ��Ԫ��
	 * 2 ����Ԫ������
	 * 3 ���Ԫ������
	 */
    private static int getExpType(String exp){
    	byte[] exps=exp.getBytes();
    	if(isMultiTableExp(exps)) return 3;
    	if(isExp(exps)) return 2;
    	return 1;
    }
	
    /**
     * �ж��Ƿ��ǹ�ʽ(������+ - * / ���ŵľ��ǹ�ʽ)
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
     * �ж��Ƿ��ж��֮��ĵ�Ԫ��ʽ(������ _ �ľ��Ƕ��ʽ)
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
	 * �ر����ݿ�����
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
