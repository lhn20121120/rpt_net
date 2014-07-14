package com.cbrc.smis.yjzb.proc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.cbrc.smis.proc.impl.ReportDDImpl;
import com.cbrc.smis.proc.impl.ReportImpl;
import com.cbrc.smis.yjzb.bean.AbnormityResult;
import com.cbrc.smis.yjzb.bean.Abnormity_actu_standard;
/**
 * �Ա�����쳣�仯������
 */
public class AbnormityYJZBmpl {
/**
   * ���Ԥ��ָ��	�������
   * @param conn
   * @param childRepId
   * @param versionId
   * @return Abnormity_Main
   * @throws Exception
   */
	public static List getAbnormity_actu_standard(
			Connection conn, 
			String childRepId, 
			String versionId,
			Integer rep_freq_id,
	        Integer repInId,String orgid) throws Exception{
		if(conn==null || childRepId==null || versionId==null ||  
		   rep_freq_id==null || repInId==null || orgid==null)return null;
		 List abnormity_actu_standard_list=null;                       //Ԥ��ָ���б�
		 Abnormity_actu_standard abnormity_actu_standard=null;
		 PreparedStatement stmt=null;
		 ResultSet rs=null;
		 try{
			 
			String sql="select * from abnormity_actu_standard where " +
				"child_rep_id=? and version_id=? and rep_freq_id=? and org_id=?";
		
			stmt=conn.prepareStatement(sql.toUpperCase());
			stmt.setString(1,childRepId);
			stmt.setString(2,versionId);
			stmt.setInt(3,rep_freq_id.intValue());
			stmt.setString(4,orgid);
			rs=stmt.executeQuery(); 
			abnormity_actu_standard_list=new ArrayList();
			while(rs.next())
			{
				abnormity_actu_standard=new Abnormity_actu_standard();
				abnormity_actu_standard.setRepInId(repInId);
			    int as_id=rs.getInt("as_id".toUpperCase());
				abnormity_actu_standard.setAs_id(new Integer(as_id));
				abnormity_actu_standard.setAs_pdf_item(rs.getString("as_pdf_item".toUpperCase()));
				abnormity_actu_standard.setAs_standard(rs.getString("as_standard".toUpperCase()));
				abnormity_actu_standard.setAs_excel_item(rs.getString("as_excel_item".toUpperCase()));
				int exp_id=rs.getInt("exp_id".toUpperCase());
				abnormity_actu_standard.setExp_id(new Integer(exp_id));
				int title_id=rs.getInt("title_id".toUpperCase());
				abnormity_actu_standard.setTitle_id(new Integer(title_id));
				int repfreqid=rs.getInt("rep_freq_id".toUpperCase());
				abnormity_actu_standard.setRep_freq_id(new Integer(repfreqid));
				abnormity_actu_standard.setChild_rep_id(rs.getString("child_rep_id".toUpperCase()));
				abnormity_actu_standard.setVersion_id(rs.getString("version_id".toUpperCase()));
				abnormity_actu_standard_list.add(abnormity_actu_standard);
			}
		 }catch(Exception e)
		 {
			 throw new Exception(e.getMessage());
		 }finally{
			 if(stmt!=null)stmt.close();
			 if(rs!=null)rs.close();
		 }
		 
		 return abnormity_actu_standard_list;
		 
	}
	
	
	
	/**
	 * �Ե�Ԫ�����
	 * 
	 * @param conn
	 * @param abnormity_actu_standard Ԥ��ָ������ʵ�����
	 * @throws Exception
	 */
	public static boolean CellParse(Connection conn,
			Abnormity_actu_standard abnormity_actu_standard,
			Integer year,
			Integer term,
			String orgid,
			Integer dataRangeId,
			Integer curId) throws Exception{
		   if(conn==null || (abnormity_actu_standard==null || abnormity_actu_standard.getExp_id()==null) ||
			  year==null || term==null ||
		      orgid==null || dataRangeId==null || curId==null) return false;
		   
		   AbnormityResult abnormityresult=null;  //Ԥ��ָ������
		   boolean result=true;

		   String childRepId=abnormity_actu_standard.getChild_rep_id();
		   String versionId=abnormity_actu_standard.getVersion_id();
		   
		   String cellabnormityFormu=(abnormity_actu_standard.getAs_pdf_item()).trim();
		   PreparedStatement stmt=null;
		   ResultSet rs=null;
		   int posS=0,posE=0;
		   posS=cellabnormityFormu.indexOf("[");
		   posE=cellabnormityFormu.indexOf("]");
		   String cellName=null;         //��Ԫ������
		   String newcellValue=null;     //���ڵĵ�Ԫ��ֵ
		   String oldcellValue=null;    //���ڵĵ�Ԫ��ֵ
		   
			   abnormityresult=new AbnormityResult();
			   cellName=cellabnormityFormu.substring(posS+1,posE);
			   
			   if(cellName!=null && !cellName.equals(""))
			   {
				        //�õ����ڵĵ�Ԫ��ֵ
					   newcellValue=YJZBReportImpl.getCellValue(conn,abnormity_actu_standard.getRepInId().intValue(),
					                                            childRepId,
					                                            versionId,
					                                            cellName);
					    if(newcellValue!=null) newcellValue=newcellValue.trim();
					   
					    if(abnormity_actu_standard.getExp_id().intValue()==YJZBReportImpl.EXP_SAMETERM_ID){
					    	//����ͬ��ֵ
					    	oldcellValue=YJZBReportImpl.getPreYearSameTermCellValue(conn,
									childRepId,
									versionId,
									orgid,
									dataRangeId,
                                    abnormity_actu_standard.getRep_freq_id(),                                    
                                    cellName,
                                    year,
                                    term,
                                    curId);
					    }else{
					    	//���ڵĵ�Ԫ��ֵ
					    	if(abnormity_actu_standard.getExp_id().intValue()!=YJZBReportImpl.EXP_B_ID){
					    		oldcellValue=YJZBReportImpl.getOldCellValue(conn,
					    											childRepId,
					    		                                    abnormity_actu_standard.getRep_freq_id(),
                                                                    versionId,
                                                                    cellName,
                                                                    year,
                                                                    term,
                                                                    orgid,
                                                                    dataRangeId,
                                                                    curId);
					    	}
					    }
					    if(oldcellValue!=null) oldcellValue=oldcellValue.trim();
					    
					    if(ReportImpl.isPercentCell(conn,childRepId,versionId,cellName)==true){
					    	if(newcellValue!=null && !newcellValue.equals("")){
					    		abnormityresult.setCurrent_value(YJZBReportImpl.format(Double.parseDouble(newcellValue)*100) + "%");
					    	}else{
					    		abnormityresult.setCurrent_value(newcellValue);
					    	}	
					    	if(oldcellValue!=null && !oldcellValue.equals("")){
					    		abnormityresult.setPreviou_value(YJZBReportImpl.format(Double.parseDouble(oldcellValue)*100) + "%");
					    	}else{
					    		abnormityresult.setPreviou_value(oldcellValue);
					    	}
			   			}else{
					    	abnormityresult.setCurrent_value(newcellValue);	
					    	abnormityresult.setPreviou_value(oldcellValue);
			   			}
					  
					    //�õ���Ԫ�����ںͱ��ڵĽ��ֵ
					    String result_value=null;       //���ںͱ��ڵĽ��ֵ
					    result_value=YJZBReportImpl.getResultValue(newcellValue,oldcellValue,abnormity_actu_standard.getExp_id());

					    abnormityresult.setResult_value(result_value);  
					    //�õ��õ�Ԫֵ��״̬�Ƿ񳬹���׼
					    int flag;
					    String standard=YJZBReportImpl.getstandard(conn,abnormity_actu_standard,orgid);
					    
					    // flag=YJZBReportImpl.operation(result_value,standard);
					    
					    flag=YJZBReportImpl.operation(result_value,abnormity_actu_standard.getAs_standard());
					    Integer Flag=new Integer(flag);
					    abnormityresult.setFlag(Flag);
					    
					   //�õ���Ԫ���չʾ��ҳ�����Խ��ֵ
					    String face_value=null;
					    //  face_value=YJZBReportImpl.getFace_value(result_value,standard);
					    face_value=YJZBReportImpl.getFace_value(result_value,abnormity_actu_standard.getAs_standard());
					    abnormityresult.setFace_value(face_value);
					    
					    
					    abnormityresult.setAs_id(abnormity_actu_standard.getAs_id());				    
					    abnormityresult.setRepInId(abnormity_actu_standard.getRepInId());
					    abnormityresult.setExp_id(abnormity_actu_standard.getExp_id());
					    
					    if(YJZBReportImpl.setValue(conn, abnormityresult)==true){}
		   }
		   return false;
	}
	
	/**
	 * ��ʽ������ת��
	 * 
	 * @param cellValue
	 * @return res String ����ֵ
	 */
	public static String format(String cellValue){
		String res="0";
		
		if(cellValue==null) return res;
		
		try{
			Double dblObj=new Double(cellValue);
			
			DecimalFormat dF=new DecimalFormat("#.####################");
			
			dF.setDecimalSeparatorAlwaysShown(false);
			res=dF.format(dblObj);
		//	// System.out.println("res is "+res);
		}catch(Exception e){
			res="0";
		}
		
		return res;
	}
	
	/**
	 * ������Ĺ�ʽ
	 * @param conn
	 * @param abnormity_actu_standard  Ԥ��ָ������ʵ�����
	 * @param year ��
	 * @param term ����
	 * @param orgid ����ID
	 * @param dataRangeId ���ݷ�Χ
	 * @param cur_Id ����ID
	 * @throws Exception
	 */
	public static void parseExp(Connection conn,
			Abnormity_actu_standard abnormityActuStandard,
			Integer year,
			Integer term,
			String orgId,
			Integer dataRangeId,
			Integer curId) throws Exception{
		   if(abnormityActuStandard==null || year==null || term==null || orgId==null || dataRangeId==null || curId==null) return;
		   
		   int expId=abnormityActuStandard.getExp_id()!=null?abnormityActuStandard.getExp_id().intValue():-1;
		   String exp=abnormityActuStandard.getAs_pdf_item();

	       int len=exp.length();
	       StringBuffer resCurTerm=new StringBuffer("");  //������ֵ�����ָ����
	       StringBuffer resPreTerm=new StringBuffer("");  //������ֵ�����ָ����
	       int flag=0;
	       String cellValue="";
	       StringBuffer temp=new StringBuffer();
	       int start,end;
	       
	        for(int i=0;i<len;i++){
		          start=i;
		          end=i+1;
		          String currentChar=exp.substring(start,end);
		          
		          //0 ��������������  �� 1 �������������� 
		          if(flag==1 || exp.substring(start,end).equals("[")){
			           flag=1;
			           temp.append(currentChar);
			           if(currentChar.equals("]")){
	                       //�õ���Ԫ���ֵ
			        	   cellValue=YJZBReportImpl.getCellValue(conn,abnormityActuStandard.getRepInId().intValue(),
			        			   abnormityActuStandard.getChild_rep_id(),
			        			   abnormityActuStandard.getVersion_id(),
                                   temp.toString().substring(1,temp.length()-1));
			        	   resCurTerm.append(cellValue);
			        	   if(expId>YJZBReportImpl.EXP_B_ID){
			        		   if(expId==YJZBReportImpl.EXP_SAMETERM_ID){
			        			  cellValue=YJZBReportImpl.getPreYearSameTermCellValue(conn,
			        				   		abnormityActuStandard.getChild_rep_id(),
			        				   		abnormityActuStandard.getVersion_id(),
			        				   		orgId,
			        				   		dataRangeId,
			        				   		abnormityActuStandard.getRep_freq_id(),	                                    	
	                                    	temp.toString().substring(1,temp.length()-1),
	                                    	year,
	                                    	term,
	                                    	curId);
			        		   }else{
				        		   cellValue=YJZBReportImpl.getOldCellValue(conn,
				        				   		abnormityActuStandard.getChild_rep_id(),
				        				   		abnormityActuStandard.getRep_freq_id(),
		                                    	abnormityActuStandard.getVersion_id(),
		                                    	temp.toString().substring(1,temp.length()-1),
		                                    	year,
		                                    	term,
		                                    	orgId,
		                                    	dataRangeId,
		                                    	curId);
			        		   }
			        		   resPreTerm.append(cellValue);
			        	   }
			        	   temp=new StringBuffer();
				           flag=0;
			           }
		          }else{
		        	  resCurTerm.append(currentChar);
		        	  if(expId>YJZBReportImpl.EXP_B_ID) resPreTerm.append(currentChar);
		          }
	        }
	        
        //����ֵ
		double curTermVal=getExpressionValue(conn,resCurTerm.toString());
			
		String expResults=null;		
		AbnormityResult abnormityResult=new AbnormityResult();
		
		if(expId>YJZBReportImpl.EXP_B_ID){
			//����ֵ
			double preTermVal=getExpressionValue(conn,resPreTerm.toString());
			abnormityResult.setPreviou_value(YJZBReportImpl.round(preTermVal,Double.parseDouble("1.0"),YJZBReportImpl.DEFAULTSCALE));
			expResults=YJZBReportImpl.getResultValue(curTermVal,preTermVal,abnormityActuStandard.getExp_id());
		}else{
			//������ֵ����С����λ
			expResults=YJZBReportImpl.round(curTermVal,Double.parseDouble("1.0"),YJZBReportImpl.DEFAULTSCALE); 
		}
		//�õ��õ�Ԫֵ��״̬�Ƿ񳬹���׼
	    int flag1;
	    String standard=YJZBReportImpl.getstandard(conn,abnormityActuStandard,orgId);
	    // flag1=YJZBReportImpl.operation(expResult,standard);
	    flag1=YJZBReportImpl.operation(expResults,abnormityActuStandard.getAs_standard());
	    Integer Flag=new Integer(flag1);
	    abnormityResult.setFlag(Flag);
	    
	   //�õ���Ԫ���չʾ��ҳ�����Խ��ֵ
	    String face_value=null;
	    // face_value=YJZBReportImpl.getFace_value(expResult,standard);
	    face_value=YJZBReportImpl.getFace_value(expResults,abnormityActuStandard.getAs_standard());
	    abnormityResult.setFace_value(face_value);
		//�����д�����ݿ�		
	    abnormityResult.setAs_id(abnormityActuStandard.getAs_id());				    
	    abnormityResult.setRepInId(abnormityActuStandard.getRepInId());
	    abnormityResult.setExp_id(abnormityActuStandard.getExp_id());
	    abnormityResult.setCurrent_value(YJZBReportImpl.round(curTermVal,Double.parseDouble("1.0"),YJZBReportImpl.DEFAULTSCALE));
	    abnormityResult.setResult_value(expResults);
	  
	    if(YJZBReportImpl.setValue(conn, abnormityResult)){
	    //	// System.out.println("��ֵ����ɹ���"+expResult);
	    }	       
	}
	
     /**
      * ������Ĺ�ʽ����
      * @param conn
      * @param abnormity_actu_standard Ԥ��ָ������ʵ�����
      * @param year ��	
      * @param term ����
      * @param orgid ����ID 
      * @param dataRangeId ���ݷ�Χ
      * @param cur_Id ����ID
      * @throws Exception
      */
	public static void parseMultiTableExp(Connection conn,Abnormity_actu_standard abnormity_actu_standard,Integer year,Integer term,String orgid,Integer dataRangeId,Integer cur_Id) throws Exception{
		   String exp=abnormity_actu_standard.getAs_pdf_item();
		   
	       int len=exp.length();
	       StringBuffer result=new StringBuffer();
	       int flag=0;
	       String cellValue="";
	       StringBuffer temp=new StringBuffer();
	        for(int i=0;i<len;i++){
		          int start=i;
		          int end=i+1;
		          String currentChar=exp.substring(start,end);
		          //0 ��������������  �� 1 �������������� 
		          if(flag==1 || exp.substring(start,end).equals("[")){
			           flag=1;
			           temp.append(currentChar);
			           if(currentChar.equals("]")){  //�õ���Ԫ���ֵ
			        	   if((temp.toString().split("_")).length>1){   //����п�����Ϣ
			        		   String temp2=temp.toString().substring(1,temp.length()-1);
			        		   String[] temp3=temp2.split("_");
			        		   String childRepId=temp3[0];
			        		   String cellName=temp3[1];
			        		   int repInId=YJZBReportImpl.getRepInId(conn,abnormity_actu_standard,temp3[0]);
			        		   //if(repInId==-1){//˵��û�еĵ���Ӧ�ı�����Ҫ��ʾ���������}
			        		   //}
			        		   cellValue=YJZBReportImpl.getCellValue(conn,
			        				   repInId,
				        			   childRepId,
				        			   abnormity_actu_standard.getVersion_id(),
				        			   cellName);
			        	   }else{    //����Ԫ�񣬲����
			        		   cellValue=YJZBReportImpl.getCellValue(conn,abnormity_actu_standard.getRepInId().intValue(),
	                                   abnormity_actu_standard.getChild_rep_id(),
	                                   abnormity_actu_standard.getVersion_id(),
	                                   temp.toString().substring(1,temp.length()-1));
			        	   }
			        	   
			        	   result.append(cellValue);
			        	   temp=new StringBuffer();
				           flag=0;
			              }
		           }else{
			             //���뾲̬�Ĺ�ʽ��Ϣ���� + - * / ���ŵ�
			              result.append(currentChar);
		            }
	            }

	    AbnormityResult abnormityresult=new AbnormityResult();
	    //�õ���ʽ���
	    double expResult=getExpressionValue(conn,result.toString());
	    String expResults=null;
	    //������ֵ����С����λ
	    expResults=YJZBReportImpl.round(expResult,Double.parseDouble("1.0"),YJZBReportImpl.DEFAULTSCALE);
	    int flag1;
	    String standard=YJZBReportImpl.getstandard(conn,abnormity_actu_standard,orgid);
	    //  flag1=YJZBReportImpl.operation(expResult,standard);
	    flag1=YJZBReportImpl.operation(expResults,abnormity_actu_standard.getAs_standard());
	    Integer Flag=new Integer(flag1);
	    abnormityresult.setFlag(Flag);
	    
	    //�õ���Ԫ���չʾ��ҳ�����Խ��ֵ
	    String face_value=null;
	    //  face_value=YJZBReportImpl.getFace_value(expResult,standard);
	    face_value=YJZBReportImpl.getFace_value(expResults,abnormity_actu_standard.getAs_standard());
	    abnormityresult.setFace_value(face_value); 
	   
	    //�����д�����ݿ�
        abnormityresult.setAs_id(abnormity_actu_standard.getAs_id());				    
        abnormityresult.setRepInId(abnormity_actu_standard.getRepInId());
        abnormityresult.setExp_id(abnormity_actu_standard.getExp_id());
        abnormityresult.setCurrent_value(expResults);
        abnormityresult.setResult_value(expResults);
        if(YJZBReportImpl.setValue(conn, abnormityresult)){
 	    //    // System.out.println("��ֵ����ɹ���"+expResult);
        }	       
}
  
	/**
	 * �õ���ʽ����Ľ��ֵ
	 * @param conn
	 * @param expression
	 * @return double �����Ľ��ֵ
	 */
	public static double getExpressionValue(Connection conn,String expression){
		double a=0.0;
		try{
		    a=new Double(ReportDDImpl.getExpressionResult(conn,expression)).doubleValue();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return a;
	}  
}
