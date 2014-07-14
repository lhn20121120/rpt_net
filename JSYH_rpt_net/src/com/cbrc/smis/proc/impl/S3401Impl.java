package com.cbrc.smis.proc.impl;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cbrc.smis.proc.po.ReportIn;

public class S3401Impl {
	/**
	 * S3402的D列的值 
	 */
	private static String S3401_Col5=null;

	/**
	 * 校验
	 * 
	 * @param colFormu String 校验关系
	 * @param arr String[] 
	 * @param reportIn ReportIn
	 * @param table String 数据表名称
	 * @param conn Connection 数据库连接
	 * @param operator String 表达式操作符
	 * @return boolean
	 */
	public static boolean validate(String colFormu,String[] arr,ReportIn reportIn,String table,Connection conn,String operator){
		boolean resVal=true;
		
		String sql="";
		
		try{
			List resList=null;
			
			boolean isSameRow=getIsSameRow(arr);
			/*// System.out.println("colFormu:" + colFormu);
			// System.out.println("isSameRow:" + isSameRow);*/
			if(isSameRow==true){
				String tmp=formatFormu(arr[0],reportIn.getChildRepId());
				
				if(tmp.equals("")){
					return false;
				}
				for(int i=1;i<arr.length;i++){
					if(Expression.getCellName(arr[i]).equals("")){return false;}
					sql+=(sql.equals("")?"":",") + 
						"(" + tmp + 
						")-(" + 
						formatFormu(arr[i],reportIn.getChildRepId()) +
						")";
				}
				
				sql=sql.trim();
				
				//将表内校验关系表达式生成的SQL为空，操作终止!
				if(sql.equals("")) {return false;}
	
				sql="select " + sql + " from " + table + " where rep_in_id=" + reportIn.getRepInId().intValue() +
					(S3401_Col5.trim().equals("")?"":" and Col5='" + S3401_Col5 + "'");
				
				resList=ReportImpl.getCalculatorResults(conn,sql,arr.length-1);
				
				//获得表内校验关系表达式运算值失败，操作终止!
				if(resList==null){return true;}
				
				double res[]=null;
				
				for(int i=0;i<resList.size();i++){
					res=(double[])resList.get(i);					
					for(int j=0;j<res.length;j++){
						if(operator.equals(">=")){
							if(res[j]<0){resVal=false;break;}
						}else if(operator.equals("<=")){
							if(res[j]>0){resVal=false;break;}
						}else if(operator.equals(">")){
							if(res[j]<=0){resVal=false;break;}
						}else if(operator.equals("<")){
							if(res[j]>=0){resVal=false;break;}
						}else{
							if(res[j]!=0){resVal=false;break;}
						}
					}
				}
			}else{
				int posS=0,posE=0;
				
				colFormu=colFormu.trim();
				
				List col2Lst=getCol2Lst(conn,reportIn.getRepInId(),table);
				if(col2Lst==null) return false;
				
				String cellName=null;
				String cellValue="0",_colFormu="";
				String[] _arr=null;
				String col=null,col2=null,col5=null;
				for(int i=0;i<col2Lst.size();i++){
					_arr=null;
					cellName="";
					_colFormu=colFormu;
					posS=_colFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
					posE=_colFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
					
					while(posS>=0 && posE>=0){
						cellName=_colFormu.substring(posS+1,posE);
						col2=null;
						col5=null;
						if(cellName!=null && cellName.length()>2){
							_arr=cellName.split("-");
							if(_arr!=null){
								col=_arr[0];
								col5=_arr.length<2?"1":_arr[1];
								col2=(String)col2Lst.get(i);
							}
							/*// System.out.println("col:" + col);
							// System.out.println("col5:" + col5);
							// System.out.println("col2:" + col2);*/
							cellValue=getCellValue(conn,reportIn.getRepInId(),col,col2,col5,table);
						
							cellValue=Expression.format(cellValue);
							
							_colFormu=_colFormu.substring(0,posS) + 
								(cellValue.indexOf("-")==0?"(" + String.valueOf(cellValue) + ")":String.valueOf(cellValue)) + 
								_colFormu.substring(posE+1);
	
							posS=_colFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
							posE=_colFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
						}
					}
					resVal=Expression.isEquals(conn,_colFormu,new Integer(6));

					if(resVal==false) break;
				}
			}			
		}catch(Exception e){
			resVal=false;
			e.printStackTrace();
		}
		
		return resVal;
	}
	
	/**
	 * 获取单元格的值
	 * 
	 * @param conn Connection 数据库连接
	 * @param repInId Integer 报表ID
	 * @param col String
	 * @param col2 String 
	 * @param col5 String
	 * @param table String
	 * @return String 单元格的值
	 */
	private static String getCellValue(Connection conn,Integer repInId,String col,String col2,String col5,String table) throws Exception{
		String res="0";
		
		if(conn==null || col2==null || col5==null) return res;
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try{
			String sql="select " + col.toUpperCase() + " from " + table.toUpperCase() + " where rep_in_id=".toUpperCase() +
				repInId + " and col2='".toUpperCase() + col2 + "' and col5='".toUpperCase() + col5 + "'";
			
			pstmt=conn.prepareStatement(sql);
		
			rs=pstmt.executeQuery();
			if(rs!=null && rs.next()){
				res=rs.getString(1);
			}
		}catch(SQLException sqle){
			throw new Exception(sqle.getMessage());
		}finally{
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
		}
		
		if(res.trim().equals("")) res="0";
			
		return res;
	}
	
	/**
	 * 
	 */
	private static List getCol2Lst(Connection conn,Integer repInId,String table)throws Exception{
		List resLst=null;
		
		if(conn==null || repInId==null) return resLst;
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try{
			String sql="select distinct col2 from ".toUpperCase() + table + " where rep_in_id=".toUpperCase() + repInId + " and replace(col2,' ','')!=''".toUpperCase();
			
			pstmt=conn.prepareStatement(sql);
		
			rs=pstmt.executeQuery();
			if(rs!=null){
				resLst=new ArrayList();
				while(rs.next()){
					resLst.add(rs.getString("col2"));
				}
			}
		}catch(SQLException sqle){
			resLst=null;
			throw new Exception(sqle.getMessage());
		}finally{
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
		}
			
		return resLst;
	}
	
	/** 
	 * 格式单元格表达式
	 * 
	 * @param cellFormu String 表达式中的单元格名称,如:[A10]-[A11]
	 * @return String 格式化过的单元格名称,如:Decimal(A10,10,2)-Decimal(A11,10,2)
	 */
	private static String formatFormu(String cellFormu,String childRepId){
		String G51="G5100";
		String S3401="S3401";
		String cellName="",_cellFormu="";
		
		if(cellFormu==null) return cellName;
		
		try{
			int posS=cellFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
			int posE=cellFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
			
			while(posS>=0 && posE>=0){
				//cellName+= cellFormu.substring(posS+1,posE);
				if(childRepId!=null && childRepId.toUpperCase().equals(G51)){
					cellName=Expression.getColName(cellFormu.substring(posS+1,posE));
				}else if(childRepId!=null && childRepId.toUpperCase().equals(S3401)){
					cellName=cellFormu.substring(posS+1,posE);
					
					String[] arr=cellName.split("-");
					if(arr!=null && arr.length==2){
						cellName=arr[0];
						S3401_Col5=arr[1];
					}
				}else{
					cellName=cellFormu.substring(posS+1,posE);
				}
				if(cellName.equals("")) return "";
				cellFormu=cellFormu.substring(0,posS) + 
					"case when " + cellName + " is null then 0 else double(" +  cellName + ") end" + 
					cellFormu.substring(posE+1);
				posS=cellFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU,posS+1);
				posE=cellFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU,posE+1);
			}
			_cellFormu=cellFormu;
		}catch(Exception e){
			_cellFormu="";
		}
		
		return _cellFormu;
	}
	
	/**
	 * 判断当前校验是否是同一行记录的数值校验
	 */
	private static boolean getIsSameRow(String[] cellFormus){
		boolean res=true;
		
		if(cellFormus==null) return res;
		
		String cellFormu="",cellName="",col5="";
		
		int posS=-1,posE=-1;
		String[] arr=null;
		
		try{
			for(int i=0;i<cellFormus.length;i++){
				cellFormu=cellFormus[i];
				posS=cellFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
				posE=cellFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
								
				while(posS>=0 && posE>=0){					
					cellName=cellFormu.substring(posS+1,posE);
						
					arr=cellName.split("-");
					if(arr!=null && arr.length==2){
						cellName=arr[0];
						if(!col5.equals("")){
							if(!col5.equals(arr[1])){
								return false;
							}
						}
						col5=arr[1];
					}
				
					posS=cellFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU,posS+1);
					posE=cellFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU,posE+1);
				}
			}
		}catch(Exception e){
			res=false;
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static void main(String[] args){
		//String[] cellFormus={"[COL13-1]+[COL13-2]+[COL13-3]+2","[COL14]","[COL13-1]+[COL13-2]+[COL13-3]-2"};
		String[] cellFormus={"[COL13-1]+[COL14-1]+[COL15-1]+2","[COL14]","[COL13-1]+[COL14-1]+[COL15-1]-2"};
		// System.out.println(S3401Impl.getIsSameRow(cellFormus));
	}
}
