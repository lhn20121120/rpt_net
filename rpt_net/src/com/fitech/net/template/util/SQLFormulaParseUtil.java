package com.fitech.net.template.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.cbrc.smis.dao.FitechConnection;

/**
 * SQL表达式解析工具类
 * 
 * @author Yao
 * 
 */
public class SQLFormulaParseUtil
{

	/**
	 * 每段SQL的开头标志
	 */
	private final String SQL_START_SIGN = "<<";

	/**
	 * 每段SQL的结束标志
	 */
	private final String SQL_END_SIGN = ">>";

	/**
	 * 四则运算所用的库表名称(可以改变)
	 */
	private final String CALCULATE_TABLE = "CALCULATE_TABLE";
	
	/**
	 * 数据精确度,小数点后位数
	 */
	private final int PRECISION =4;
	
	/**
	 * 解析公式
	 * 
	 * @param formualStr
	 * @return 返回解析后的公式
	 */
	public String parseSQLFormual(String sqlFormual, Map paramMap) throws Exception
	{
		if (sqlFormual == null || sqlFormual.equals("") || paramMap==null)
			return null;
		
		ParamaParseUtil parmaUtil = new ParamaParseUtil();
		/*把公式中的参数替换成实际的值*/
		sqlFormual =parmaUtil.replaceParamToValue(sqlFormual, paramMap).toString();
		
		// System.out.println("SQLMODE==="+sqlFormual);
		
		StringBuffer resultStr = new StringBuffer("");

		/* 获取第一个SQL的开始标志位置 */
		int index_of_startSign = sqlFormual.indexOf(this.SQL_START_SIGN);
		if (index_of_startSign == -1)
			throw new StringIndexOutOfBoundsException("SQL公式: " + sqlFormual + " 错误!缺少结束标志!");

		/* 解析到的位置 */
		int parseIndex = 0;
		/*运行结果是否是数字*/
		boolean isNumber = true;
		
		while (index_of_startSign >= 0)
		{
			/* 获得对应的结束标志位置 */
			int index_of_endSign = sqlFormual.indexOf(this.SQL_END_SIGN, index_of_startSign);

			if (index_of_endSign == -1)
				throw new StringIndexOutOfBoundsException("SQL公式: " + sqlFormual + " 错误!缺少结束标志!");
			
			/*第二个SQL开始标志的位置*/
			int index_of_secondStartSign = sqlFormual.indexOf(this.SQL_START_SIGN, index_of_endSign);
			
			/**对于<<SQL>> IN <<SQL>>的特殊处理*/
			/*如果存在第二个SQL*/			
			if(index_of_secondStartSign!=-1)
			{			
				/*检查第一个SQL结束标志和第二个SQL开始标志间是否存在"IN"*/
				String str = sqlFormual.substring(index_of_endSign,index_of_secondStartSign);			
				
				/*如果存在则替换该SQL表达式中所有的"<<",">>",把该SQL作为单个SQL语句执行*/
				if(str.indexOf("IN")!=-1 || str.indexOf("in")!=-1)
				{
					/*第二个SQL结束标志的位置*/
					int index_of_secondEndSign = sqlFormual.indexOf(this.SQL_END_SIGN, index_of_secondStartSign);
					if (index_of_secondEndSign == -1)
						throw new StringIndexOutOfBoundsException("SQL公式: " + sqlFormual + " 错误!缺少结束标志!");
					/*分割出该含子查询的SQL*/
					String subStr = sqlFormual.substring(index_of_startSign,index_of_secondEndSign+3);
					/*除去该SQL中的"<<",">>"*/
					String sql = subStr.replaceAll(this.SQL_START_SIGN,"").replaceAll(this.SQL_END_SIGN,"");
					String result = this.executeSQL(sql);
					if(result==null)
						result="0.0";
						
					/* 将解析好的部分追加到解析结果中 */
					resultStr.append(sqlFormual.substring(parseIndex, index_of_startSign));
					resultStr.append("double("+result+")");
					parseIndex = index_of_secondEndSign + 3;
					
					/* 取得下一个SQL语句开始标志 */
					index_of_startSign = sqlFormual.indexOf(this.SQL_START_SIGN, index_of_secondEndSign);
					continue;
				}
			}	
			
			/**普通SQL处理*/
			/* 取出一对"<<>>"中的SQL */
			String sqlStr = sqlFormual.substring(index_of_startSign + 2, index_of_endSign);

			/* 执行该SQL语句并获得它的运行结果 */
			String sqlResult = this.executeSQL(sqlStr);
			
			/*判断该结果是不是数值*/
			if(sqlResult==null)
				sqlResult = "0.0";
			else
			{
				/*判断是否是数字*/
				try
				{
					new BigDecimal(sqlResult);
				}
				catch (Exception e)
				{
					isNumber = false;
				}
				
			}
		
			/* 将解析好的部分追加到解析结果中 */
			resultStr.append(sqlFormual.substring(parseIndex, index_of_startSign));
			
			/*如果是数字则加上double()*/
			if(isNumber==true)
				resultStr.append("double("+sqlResult+")");
			/*是字符则不加double()*/
			else
				resultStr.append(sqlResult);
			
			parseIndex = index_of_endSign + 2;
			
			/* 取得下一个SQL语句开始标志 */
			index_of_startSign = sqlFormual.indexOf(this.SQL_START_SIGN, index_of_endSign);
		}
		
		resultStr.append(sqlFormual.substring(parseIndex));
		
		if(resultStr!=null && !resultStr.equals(""))
		{
			/*结果是否是数字,如果不是数字,则去掉double()*/
			if(isNumber==false)
				return resultStr.toString();
			else
			{
				String sql = "select " + resultStr + " from " + this.CALCULATE_TABLE;
				// System.out.println("****计算SQL===="+sql);
				String value = executeSQL(sql);
				return this.setPrecision(value);
			}
			
		}	
		else
			return null;
	}


	/**
	 * 执行SQL语句返回它的执行结果
	 * 
	 * @param sql
	 * @return
	 */
	public String executeSQL(String sql) throws Exception
	{
		if(sql==null ||sql.equals(""))
			return null;
		String result = null;
		FitechConnection fitechConn =new FitechConnection();
		Connection conn = fitechConn.getConnect();
		if (conn == null)
		{
			return null;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (!rs.next())
				result = null;
			else
			{
				result = rs.getString(1);
				if(result!=null&& result.length()<20){
					try{
						double d=Double.parseDouble(result);
						result=new java.text.DecimalFormat("#0.00").format(d).toString();
					}catch(Exception ex){
						result = rs.getString(1);
					}					
				}
			}		
		}
		catch (Exception e)
		{
			result = null;
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
		finally
		{
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if(conn!=null)
				conn.close();
		}

		return result;
	}
//	public String executeSQL(String sql) throws Exception
//	{
//		if(sql==null ||sql.equals(""))
//			return null;
//		String result = null;
//		FitechConnection fitechConn =new FitechConnection();
//		Connection conn = fitechConn.getConnect();
//		if (conn == null)
//		{
//			return null;
//		}
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//
//		try
//		{
//			stmt = conn.prepareStatement(sql);
//			rs = stmt.executeQuery();
//			if (!rs.next())
//				result = null;
//			else
//			{
//				result = rs.getString(1);
//				if(result!=null&& result.length()<20){
//					double d=Double.parseDouble(result);
//					result=new java.text.DecimalFormat("0.000000").format(d).toString();
//				}
//			}
//			
//		}
//		catch (Exception e)
//		{
//			result = null;
//			e.printStackTrace();
//			throw new SQLException(e.getMessage());
//		}
//		finally
//		{
//			if (rs != null)
//				rs.close();
//			if (stmt != null)
//				stmt.close();
//			if(conn!=null)
//				conn.close();
//		}
//
//		return result;
//	}
	

	/**
	 * 数据精确度处理
	 * @param value
	 * @return
	 */
	public String setPrecision(String value) throws Exception
	{
		if(value==null)
			return "0.0";
		String result =null;		
		try
		{
			new BigDecimal(value);		
		}
		catch (NumberFormatException e)
		{
			return "'"+value+"'";
		}

		/*取得小数点位置*/
		int index_of_radixPoint = value.indexOf("."); 
		
		if(index_of_radixPoint!=-1 && index_of_radixPoint+this.PRECISION+1 <= value.length())
			result = value.substring(0,index_of_radixPoint+this.PRECISION+1);
		else
			result = value;
		
		return result;
	}
	
	public static void main(String arg[])
	{
		try
		{
			
			SQLFormulaParseUtil util = new SQLFormulaParseUtil();
			String sql = "(<<select idata.IDR_ID from I_DATARELATION idata where idata.IDR_ID>> in(<<select mcell.Cell_Id from M_Cell mcell where mcell.cell_id=28967>>)+<<select idata.IDR_ID from I_DATARELATION idata where idata.IDR_ID=28967>>)+<<select idata.IDR_ID from I_DATARELATION idata where idata.IDR_ID=28967>>";
			Map paramMap = new HashMap();
			paramMap.put("DATE","'2006-12-31'");
			util.parseSQLFormual(sql,paramMap);

		}
		catch (Exception e)
		{
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
}
