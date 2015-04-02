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
 * SQL���ʽ����������
 * 
 * @author Yao
 * 
 */
public class SQLFormulaParseUtil
{

	/**
	 * ÿ��SQL�Ŀ�ͷ��־
	 */
	private final String SQL_START_SIGN = "<<";

	/**
	 * ÿ��SQL�Ľ�����־
	 */
	private final String SQL_END_SIGN = ">>";

	/**
	 * �����������õĿ������(���Ըı�)
	 */
	private final String CALCULATE_TABLE = "CALCULATE_TABLE";
	
	/**
	 * ���ݾ�ȷ��,С�����λ��
	 */
	private final int PRECISION =4;
	
	/**
	 * ������ʽ
	 * 
	 * @param formualStr
	 * @return ���ؽ�����Ĺ�ʽ
	 */
	public String parseSQLFormual(String sqlFormual, Map paramMap) throws Exception
	{
		if (sqlFormual == null || sqlFormual.equals("") || paramMap==null)
			return null;
		
		ParamaParseUtil parmaUtil = new ParamaParseUtil();
		/*�ѹ�ʽ�еĲ����滻��ʵ�ʵ�ֵ*/
		sqlFormual =parmaUtil.replaceParamToValue(sqlFormual, paramMap).toString();
		
		// System.out.println("SQLMODE==="+sqlFormual);
		
		StringBuffer resultStr = new StringBuffer("");

		/* ��ȡ��һ��SQL�Ŀ�ʼ��־λ�� */
		int index_of_startSign = sqlFormual.indexOf(this.SQL_START_SIGN);
		if (index_of_startSign == -1)
			throw new StringIndexOutOfBoundsException("SQL��ʽ: " + sqlFormual + " ����!ȱ�ٽ�����־!");

		/* ��������λ�� */
		int parseIndex = 0;
		/*���н���Ƿ�������*/
		boolean isNumber = true;
		
		while (index_of_startSign >= 0)
		{
			/* ��ö�Ӧ�Ľ�����־λ�� */
			int index_of_endSign = sqlFormual.indexOf(this.SQL_END_SIGN, index_of_startSign);

			if (index_of_endSign == -1)
				throw new StringIndexOutOfBoundsException("SQL��ʽ: " + sqlFormual + " ����!ȱ�ٽ�����־!");
			
			/*�ڶ���SQL��ʼ��־��λ��*/
			int index_of_secondStartSign = sqlFormual.indexOf(this.SQL_START_SIGN, index_of_endSign);
			
			/**����<<SQL>> IN <<SQL>>�����⴦��*/
			/*������ڵڶ���SQL*/			
			if(index_of_secondStartSign!=-1)
			{			
				/*����һ��SQL������־�͵ڶ���SQL��ʼ��־���Ƿ����"IN"*/
				String str = sqlFormual.substring(index_of_endSign,index_of_secondStartSign);			
				
				/*����������滻��SQL���ʽ�����е�"<<",">>",�Ѹ�SQL��Ϊ����SQL���ִ��*/
				if(str.indexOf("IN")!=-1 || str.indexOf("in")!=-1)
				{
					/*�ڶ���SQL������־��λ��*/
					int index_of_secondEndSign = sqlFormual.indexOf(this.SQL_END_SIGN, index_of_secondStartSign);
					if (index_of_secondEndSign == -1)
						throw new StringIndexOutOfBoundsException("SQL��ʽ: " + sqlFormual + " ����!ȱ�ٽ�����־!");
					/*�ָ���ú��Ӳ�ѯ��SQL*/
					String subStr = sqlFormual.substring(index_of_startSign,index_of_secondEndSign+3);
					/*��ȥ��SQL�е�"<<",">>"*/
					String sql = subStr.replaceAll(this.SQL_START_SIGN,"").replaceAll(this.SQL_END_SIGN,"");
					String result = this.executeSQL(sql);
					if(result==null)
						result="0.0";
						
					/* �������õĲ���׷�ӵ���������� */
					resultStr.append(sqlFormual.substring(parseIndex, index_of_startSign));
					resultStr.append("double("+result+")");
					parseIndex = index_of_secondEndSign + 3;
					
					/* ȡ����һ��SQL��俪ʼ��־ */
					index_of_startSign = sqlFormual.indexOf(this.SQL_START_SIGN, index_of_secondEndSign);
					continue;
				}
			}	
			
			/**��ͨSQL����*/
			/* ȡ��һ��"<<>>"�е�SQL */
			String sqlStr = sqlFormual.substring(index_of_startSign + 2, index_of_endSign);

			/* ִ�и�SQL��䲢����������н�� */
			String sqlResult = this.executeSQL(sqlStr);
			
			/*�жϸý���ǲ�����ֵ*/
			if(sqlResult==null)
				sqlResult = "0.0";
			else
			{
				/*�ж��Ƿ�������*/
				try
				{
					new BigDecimal(sqlResult);
				}
				catch (Exception e)
				{
					isNumber = false;
				}
				
			}
		
			/* �������õĲ���׷�ӵ���������� */
			resultStr.append(sqlFormual.substring(parseIndex, index_of_startSign));
			
			/*��������������double()*/
			if(isNumber==true)
				resultStr.append("double("+sqlResult+")");
			/*���ַ��򲻼�double()*/
			else
				resultStr.append(sqlResult);
			
			parseIndex = index_of_endSign + 2;
			
			/* ȡ����һ��SQL��俪ʼ��־ */
			index_of_startSign = sqlFormual.indexOf(this.SQL_START_SIGN, index_of_endSign);
		}
		
		resultStr.append(sqlFormual.substring(parseIndex));
		
		if(resultStr!=null && !resultStr.equals(""))
		{
			/*����Ƿ�������,�����������,��ȥ��double()*/
			if(isNumber==false)
				return resultStr.toString();
			else
			{
				String sql = "select " + resultStr + " from " + this.CALCULATE_TABLE;
				// System.out.println("****����SQL===="+sql);
				String value = executeSQL(sql);
				return this.setPrecision(value);
			}
			
		}	
		else
			return null;
	}


	/**
	 * ִ��SQL��䷵������ִ�н��
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
	 * ���ݾ�ȷ�ȴ���
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

		/*ȡ��С����λ��*/
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
	}
}
