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
 * 对报表的异常变化方法类
 */
public class AbnormityYJZBmpl {
/**
   * 获得预警指标	主表对象
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
		 List abnormity_actu_standard_list=null;                       //预警指标列表
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
	 * 对单元格解析
	 * 
	 * @param conn
	 * @param abnormity_actu_standard 预警指标主表实体对象
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
		   
		   AbnormityResult abnormityresult=null;  //预警指标结果类
		   boolean result=true;

		   String childRepId=abnormity_actu_standard.getChild_rep_id();
		   String versionId=abnormity_actu_standard.getVersion_id();
		   
		   String cellabnormityFormu=(abnormity_actu_standard.getAs_pdf_item()).trim();
		   PreparedStatement stmt=null;
		   ResultSet rs=null;
		   int posS=0,posE=0;
		   posS=cellabnormityFormu.indexOf("[");
		   posE=cellabnormityFormu.indexOf("]");
		   String cellName=null;         //单元格名称
		   String newcellValue=null;     //本期的单元格值
		   String oldcellValue=null;    //上期的单元格值
		   
			   abnormityresult=new AbnormityResult();
			   cellName=cellabnormityFormu.substring(posS+1,posE);
			   
			   if(cellName!=null && !cellName.equals(""))
			   {
				        //得到本期的单元格值
					   newcellValue=YJZBReportImpl.getCellValue(conn,abnormity_actu_standard.getRepInId().intValue(),
					                                            childRepId,
					                                            versionId,
					                                            cellName);
					    if(newcellValue!=null) newcellValue=newcellValue.trim();
					   
					    if(abnormity_actu_standard.getExp_id().intValue()==YJZBReportImpl.EXP_SAMETERM_ID){
					    	//上年同期值
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
					    	//上期的单元格值
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
					  
					    //得到单元格上期和本期的结果值
					    String result_value=null;       //上期和本期的结果值
					    result_value=YJZBReportImpl.getResultValue(newcellValue,oldcellValue,abnormity_actu_standard.getExp_id());

					    abnormityresult.setResult_value(result_value);  
					    //得到该单元值的状态是否超过标准
					    int flag;
					    String standard=YJZBReportImpl.getstandard(conn,abnormity_actu_standard,orgid);
					    
					    // flag=YJZBReportImpl.operation(result_value,standard);
					    
					    flag=YJZBReportImpl.operation(result_value,abnormity_actu_standard.getAs_standard());
					    Integer Flag=new Integer(flag);
					    abnormityresult.setFlag(Flag);
					    
					   //得到单元格的展示到页面的相对结果值
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
	 * 格式化数据转换
	 * 
	 * @param cellValue
	 * @return res String 数据值
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
	 * 处理单表的公式
	 * @param conn
	 * @param abnormity_actu_standard  预警指标主表实体对象
	 * @param year 年
	 * @param term 期数
	 * @param orgid 机构ID
	 * @param dataRangeId 数据范围
	 * @param cur_Id 币种ID
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
	       StringBuffer resCurTerm=new StringBuffer("");  //按本期值计算的指标项
	       StringBuffer resPreTerm=new StringBuffer("");  //按上期值计算的指标项
	       int flag=0;
	       String cellValue="";
	       StringBuffer temp=new StringBuffer();
	       int start,end;
	       
	        for(int i=0;i<len;i++){
		          start=i;
		          end=i+1;
		          String currentChar=exp.substring(start,end);
		          
		          //0 代表在括号外面  ， 1 代表在括号里面 
		          if(flag==1 || exp.substring(start,end).equals("[")){
			           flag=1;
			           temp.append(currentChar);
			           if(currentChar.equals("]")){
	                       //得到单元格的值
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
	        
        //本期值
		double curTermVal=getExpressionValue(conn,resCurTerm.toString());
			
		String expResults=null;		
		AbnormityResult abnormityResult=new AbnormityResult();
		
		if(expId>YJZBReportImpl.EXP_B_ID){
			//上期值
			double preTermVal=getExpressionValue(conn,resPreTerm.toString());
			abnormityResult.setPreviou_value(YJZBReportImpl.round(preTermVal,Double.parseDouble("1.0"),YJZBReportImpl.DEFAULTSCALE));
			expResults=YJZBReportImpl.getResultValue(curTermVal,preTermVal,abnormityActuStandard.getExp_id());
		}else{
			//将运算值保留小数四位
			expResults=YJZBReportImpl.round(curTermVal,Double.parseDouble("1.0"),YJZBReportImpl.DEFAULTSCALE); 
		}
		//得到该单元值的状态是否超过标准
	    int flag1;
	    String standard=YJZBReportImpl.getstandard(conn,abnormityActuStandard,orgId);
	    // flag1=YJZBReportImpl.operation(expResult,standard);
	    flag1=YJZBReportImpl.operation(expResults,abnormityActuStandard.getAs_standard());
	    Integer Flag=new Integer(flag1);
	    abnormityResult.setFlag(Flag);
	    
	   //得到单元格的展示到页面的相对结果值
	    String face_value=null;
	    // face_value=YJZBReportImpl.getFace_value(expResult,standard);
	    face_value=YJZBReportImpl.getFace_value(expResults,abnormityActuStandard.getAs_standard());
	    abnormityResult.setFace_value(face_value);
		//将结果写入数据库		
	    abnormityResult.setAs_id(abnormityActuStandard.getAs_id());				    
	    abnormityResult.setRepInId(abnormityActuStandard.getRepInId());
	    abnormityResult.setExp_id(abnormityActuStandard.getExp_id());
	    abnormityResult.setCurrent_value(YJZBReportImpl.round(curTermVal,Double.parseDouble("1.0"),YJZBReportImpl.DEFAULTSCALE));
	    abnormityResult.setResult_value(expResults);
	  
	    if(YJZBReportImpl.setValue(conn, abnormityResult)){
	    //	// System.out.println("数值插入成功！"+expResult);
	    }	       
	}
	
     /**
      * 处理跨表的公式计算
      * @param conn
      * @param abnormity_actu_standard 预警指标主表实体对象
      * @param year 年	
      * @param term 期数
      * @param orgid 机构ID 
      * @param dataRangeId 数据范围
      * @param cur_Id 币种ID
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
		          //0 代表在括号外面  ， 1 代表在括号里面 
		          if(flag==1 || exp.substring(start,end).equals("[")){
			           flag=1;
			           temp.append(currentChar);
			           if(currentChar.equals("]")){  //得到单元格的值
			        	   if((temp.toString().split("_")).length>1){   //如果有跨表的信息
			        		   String temp2=temp.toString().substring(1,temp.length()-1);
			        		   String[] temp3=temp2.split("_");
			        		   String childRepId=temp3[0];
			        		   String cellName=temp3[1];
			        		   int repInId=YJZBReportImpl.getRepInId(conn,abnormity_actu_standard,temp3[0]);
			        		   //if(repInId==-1){//说明没有的到相应的报表，需要提示处理，待完成}
			        		   //}
			        		   cellValue=YJZBReportImpl.getCellValue(conn,
			        				   repInId,
				        			   childRepId,
				        			   abnormity_actu_standard.getVersion_id(),
				        			   cellName);
			        	   }else{    //本表单元格，不跨表
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
			             //加入静态的公式信息例如 + - * / 符号等
			              result.append(currentChar);
		            }
	            }

	    AbnormityResult abnormityresult=new AbnormityResult();
	    //得到公式结果
	    double expResult=getExpressionValue(conn,result.toString());
	    String expResults=null;
	    //将运算值保留小数四位
	    expResults=YJZBReportImpl.round(expResult,Double.parseDouble("1.0"),YJZBReportImpl.DEFAULTSCALE);
	    int flag1;
	    String standard=YJZBReportImpl.getstandard(conn,abnormity_actu_standard,orgid);
	    //  flag1=YJZBReportImpl.operation(expResult,standard);
	    flag1=YJZBReportImpl.operation(expResults,abnormity_actu_standard.getAs_standard());
	    Integer Flag=new Integer(flag1);
	    abnormityresult.setFlag(Flag);
	    
	    //得到单元格的展示到页面的相对结果值
	    String face_value=null;
	    //  face_value=YJZBReportImpl.getFace_value(expResult,standard);
	    face_value=YJZBReportImpl.getFace_value(expResults,abnormity_actu_standard.getAs_standard());
	    abnormityresult.setFace_value(face_value); 
	   
	    //将结果写入数据库
        abnormityresult.setAs_id(abnormity_actu_standard.getAs_id());				    
        abnormityresult.setRepInId(abnormity_actu_standard.getRepInId());
        abnormityresult.setExp_id(abnormity_actu_standard.getExp_id());
        abnormityresult.setCurrent_value(expResults);
        abnormityresult.setResult_value(expResults);
        if(YJZBReportImpl.setValue(conn, abnormityresult)){
 	    //    // System.out.println("数值插入成功！"+expResult);
        }	       
}
  
	/**
	 * 得到公式计算的结果值
	 * @param conn
	 * @param expression
	 * @return double 计算后的结果值
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
