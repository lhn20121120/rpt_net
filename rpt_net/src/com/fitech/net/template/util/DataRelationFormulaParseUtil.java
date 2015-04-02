package com.fitech.net.template.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.FitechConnection;
import com.fitech.net.adapter.StrutsVParameterDelegate;
import com.fitech.net.form.VParameterForm;
import com.fitech.net.hibernate.SysIbmSysrels;

/**
 * ҵ��ϵͳ���ɹ�ʽ����������
 * 
 * @author Yao
 * 
 */
public class DataRelationFormulaParseUtil
{

	/**
	 * ��ż������ֵı�����
	 */
	private Map selectPartTableNameMap = null;

	/**
	 * ��ż����������ֵı�����
	 */
	public Map wherePartTableNameMap = null;

	/**
	 * ������еı��� 
	 */
	private Map tableNameMap = null;

	/**
	 * ��ʽ��ÿһ�����ʽ�Ŀ�ͷ��־
	 */
	private final String EXPRESSION_START_SIGN = "<<";

	/**
	 * ��ʽ��ÿһ�����ʽ�Ľ�����־
	 */
	private final String EXPRESSION_END_SIGN = ">>";

	/**
	 * ���ʽ�м����ֶεĿ�ʼ��־
	 */
	private final String SELECT_START_SIGN = "{";

	/**
	 * ���ʽ�м����ֶεĽ�����־
	 */
	private final String SELECT_END_SIGN = "}";

	/**
	 * ���ʽ�������ֶεĿ�ʼ��־
	 */
	private final String WHERE_START_SIGN = "[";

	/**
	 * ���ʽ�������ֶεĽ�����־
	 */
	private final String WHERE_END_SIGN = "]";

	/**
	 * ����,�ֶηָ��
	 */
	private final String TABLE_COL_SEPARATOR_SIGN = ".";

	/**
	 * Select����
	 */
	private final int SELECT_PART = 1;

	/**
	 * Where����
	 */
	private final int WHERE_PART = 2;

	/**
	 * ��������(order by | group by | having ��)
	 */
	private final int OTHER_PART = 3;

	/**
	 * ��ֵ����
	 */
	private final int NUM_TYPE = 0;

	/**
	 * �ַ�����
	 */
	private final int STR_TYPE = 1;

	public DataRelationFormulaParseUtil()
	{

	}

	/**
	 * ������ʽ
	 * 
	 * @param formualStr
	 * @return ���ؽ�����Ĺ�ʽ
	 */
	public String parseFormualStr(String formulalStr) throws Exception
	{
		if (formulalStr == null || formulalStr.equals(""))
			return "";
		StringBuffer resultStr = new StringBuffer("");

		/* ��ȡ��һ����ĿSQL�Ŀ�ʼ��־λ�� */
		int index_of_startSign = formulalStr.indexOf(this.EXPRESSION_START_SIGN);
		if (index_of_startSign == -1)
			throw new StringIndexOutOfBoundsException("��ʽ: " + formulalStr + " ����!ȱ�ٱ��ʽ��ʼ����!");
		/* ��������λ�� */
		int parseIndex = 0;

		while (index_of_startSign >= 0)
		{
			/* ��ö�Ӧ�Ľ�����־λ�� */
			int index_of_endSign = formulalStr.indexOf(this.EXPRESSION_END_SIGN, index_of_startSign);

			if (index_of_endSign == -1)
				throw new StringIndexOutOfBoundsException("��ʽ: " + formulalStr + " ����!ȱ�ٱ��ʽ��������!");

			/* ȡ��һ��"<< >>"�еı��ʽ */
			String expressionStr = formulalStr.substring(index_of_startSign + 2, index_of_endSign);

			/* �����ñ��ʽΪSQL��ʽ */
			String SQLStrParsed = this.parseExpressionToSQL(expressionStr);

			if (SQLStrParsed == null || SQLStrParsed.equals(""))
				throw new StringIndexOutOfBoundsException("���ʽ: " + expressionStr + " ����!���ܽ�����SQL!");

			/* �������õĲ���׷�ӵ���������� */
			resultStr.append(formulalStr.substring(parseIndex, index_of_startSign + 2));
			resultStr.append(SQLStrParsed);
			resultStr.append(formulalStr.substring(index_of_endSign, index_of_endSign + 2));
			parseIndex = index_of_endSign + 2;

			/* ȡ����һ�����ʽ��ʼ��־ */
			index_of_startSign = formulalStr.indexOf(this.EXPRESSION_START_SIGN, index_of_endSign + 2);
		}
		resultStr.append(formulalStr.substring(parseIndex));
		
		
		
		return resultStr.toString();
	}

	/**
	 * �������ʽ��SQL
	 * 
	 * @param expressionStr
	 * @return
	 */
	private String parseExpressionToSQL(String expressionStr) throws Exception
	{
		StringBuffer resultSQL = new StringBuffer("");

		/* ��ʼ����ż������ֱ�����,�����������ֱ�����,ȫ�������Ƶ�Map */
		this.selectPartTableNameMap = new HashMap();
		this.wherePartTableNameMap = new HashMap();
		this.tableNameMap = new HashMap();

		/* ȡ�õ�һ��������ʼ���ŵ�λ�� */
		int index_of_whereSign = expressionStr.indexOf(this.WHERE_START_SIGN);

		/* ���û��where�ı�־,��ֱ�Ӳ������ñ��ʽֱ�ӷ���(��ʱ��Ҫֱ���ֹ�дSQL���) */
		if (index_of_whereSign == -1)
		{
			return expressionStr;
			// throw new StringIndexOutOfBoundsException("���ʽ: "+expressionStr+"
			// ����!ȱ��������ʼ����!");
		}

		/* �ָ�������ֶβ��ֺ��������� */
		String selectStr = expressionStr.substring(0, index_of_whereSign);
		String whereStr = expressionStr.substring(index_of_whereSign);

		/* ����select���� */
		String selectSQL = this.parseSelectPartToSQL(selectStr);
		/* ����where���� */
		String whereSQL = this.parseWherePartToSQL(whereStr);

		/** ��ӻ������� */
		String orgSQL = this.addOrgParama();

		/* ����������ϵ���� */
		String relationSQL = this.getAllFKRelationSQL();
		/* ���From���� */
		String fromSQL = this.getFromTableSQL();

		if (selectSQL == null || selectSQL.equals("") || whereSQL == null || whereSQL.equals(""))
			throw new StringIndexOutOfBoundsException("��ʽ����!" + expressionStr + "���ܽ�����SQL!");

		/* ���SQL��� */
		resultSQL.append("select " + selectSQL + " from " + fromSQL + " where 1=1");
		
		/*��������*/
		if (orgSQL != null && !orgSQL.equals(""))
			resultSQL.append(" and " + orgSQL);
		
		/*���������*/
		if (relationSQL != null && !relationSQL.equals(""))
			resultSQL.append(" and " + relationSQL);
		
		/*��ѯ����*/
		if (whereSQL != null && !whereSQL.equals(""))
			resultSQL.append(" and " + whereSQL);

		return resultSQL.toString();
	}

	/**
	 * ����Select���ֳ�SQL
	 * 
	 * @param selectStr
	 * @return
	 */
	private String parseSelectPartToSQL(String selectStr) throws Exception
	{
		if (selectStr == null || selectStr.equals(""))
			return null;
		return replaceCNToEN(selectStr, this.SELECT_PART);
	}

	/**
	 * ����where���ֳ�SQL
	 * 
	 * @param selectStr
	 * @return
	 */
	private String parseWherePartToSQL(String whereStr) throws Exception
	{
		if (whereStr == null || whereStr.equals(""))
			return null;
		return this.replaceCNToEN(this.replaceCNToEN(whereStr, this.WHERE_PART), this.OTHER_PART);
	}

	/**
	 * �滻���������ֶγ�Ӣ���ֶ�
	 * 
	 * @param str
	 * @param flag
	 *            �滻���� 1-"{}"���� 2-"[]"����
	 */
	public String replaceCNToEN(String str, int flag) throws Exception
	{
		StringBuffer resultSQL = new StringBuffer("");
		/* ��ʼ��־ */
		String startSign = "";
		/* ������־ */
		String endSign = "";

		if (flag == this.SELECT_PART || flag == this.OTHER_PART)
		{
			startSign = this.SELECT_START_SIGN;
			endSign = this.SELECT_END_SIGN;
		}
		else if (flag == this.WHERE_PART)
		{
			startSign = this.WHERE_START_SIGN;
			endSign = this.WHERE_END_SIGN;
		}

		/* ��õ�һ����ʼ��־ */
		int index_of_startSign = str.indexOf(startSign);
		/* ����λ�� */
		int parseIndex = 0;
		int index_of_endSign = 0;
		while (index_of_startSign >= 0)
		{
			/* ������־ */
			index_of_endSign = str.indexOf(endSign, index_of_startSign);
			if (index_of_endSign == -1)
				throw new StringIndexOutOfBoundsException("���ʽ����!" + str + "û�н�����־!");

			/* �ֶ��������� */
			String field_CN = str.substring(index_of_startSign + 1, index_of_endSign);
			/* �ֶ�Ӣ������ */
			String field_EN = this.parseFieldCNToEN(field_CN, flag);

			/* �������õĲ���׷�ӵ���������� */
			resultSQL.append(str.substring(parseIndex, index_of_startSign));

			if (field_EN != null && !field_EN.equals(""))
				resultSQL.append(field_EN);
			else
				resultSQL.append(field_CN);
			
			/*�����һ����ʼ��־*/
			index_of_startSign = str.indexOf(startSign, index_of_endSign);
			
			parseIndex = index_of_endSign + 1;
		}
		resultSQL.append(str.substring(parseIndex));

		return resultSQL.toString();
	}

	/**
	 * ���������ֶγ�Ӣ��
	 * 
	 * @param field
	 * @param flag
	 */
	private String parseFieldCNToEN(String field, int flag) throws Exception
	{
		String result = null;
		if (field == null || field.equals(""))
			return null;
		Map saveToMap = null;
		if (flag == this.SELECT_PART)
			saveToMap = this.selectPartTableNameMap;
		else if (flag == this.WHERE_PART || flag == this.OTHER_PART)
			saveToMap = this.wherePartTableNameMap;

		/* �ָ�����ı������ֶ����� */
		int index_of_separator = field.indexOf(this.TABLE_COL_SEPARATOR_SIGN);
		String tableName_CN = null;
		String colName_CN = null;
		if (index_of_separator != -1)
		{
			tableName_CN = field.substring(0, index_of_separator);
			colName_CN = field.substring(index_of_separator + 1, field.length());
		}
		else
			throw new StringIndexOutOfBoundsException("ȱ�ٱ���ֶεķָ��־!" + field);

		// ���ݱ������ֶ�������������,��������ֶ���Ϣ
		VParameterForm fieldInfo = StrutsVParameterDelegate.getENNameByCNName(tableName_CN, colName_CN);

		if (fieldInfo != null)
		{
			/* ���� */
			String tableName_EN = fieldInfo.getVpTableid();
			/* �ֶ��� */
			String colName_EN = fieldInfo.getVpColumnid();
			/* �ֶ����� */
			Integer colType = fieldInfo.getVpColtype();

			if (tableName_EN == null || colName_EN == null || colType == null)
				throw new IllegalArgumentException("û�и����ı������ֶ����Ķ�Ӧ��Ϣ!" + tableName_CN + "." + colName_CN);
			else
			{
				tableName_EN = tableName_EN.toUpperCase();
				colName_EN = colName_EN.toUpperCase();
			}
			/* ���������ı�����Ž���Ӧ��Map�� */
			if (!this.tableNameMap.containsKey(tableName_EN))
				this.tableNameMap.put(tableName_EN, "");
			if (!saveToMap.containsKey(tableName_EN))
				saveToMap.put(tableName_EN, "");

			/* ����Ǽ����ֶβ�������ֵ����,�����SUM() */
			/*
			 * if(flag==this.SELECT_PART && colType.intValue()==this.NUM_TYPE)
			 * result = "SUM("+tableName_EN + "." + colName_EN+")"; else result =
			 * tableName_EN + "." + colName_EN;
			 */

			result = tableName_EN + "." + colName_EN;
		}
		else
			throw new IllegalArgumentException("û�и����ı������ֶ����Ķ�Ӧ��Ϣ!" + tableName_CN + "." + colName_CN);
		return result;

	}

	/**
	 * ���SQL From���Table����
	 * 
	 * @return
	 */
	private String getFromTableSQL() throws Exception
	{
		StringBuffer result = new StringBuffer("");

		/* ȡ�����б��� */
		Iterator tableMap_it = this.tableNameMap.keySet().iterator();
		while (tableMap_it.hasNext())
		{
			result.append(tableMap_it.next() + Config.SPLIT_SYMBOL_COMMA);
		}

		/* ɾ�����Ķ��� */
		if (!result.equals("") && result.lastIndexOf(Config.SPLIT_SYMBOL_COMMA) != -1)
		{
			result.delete(result.lastIndexOf(Config.SPLIT_SYMBOL_COMMA), result.length());
		}
		return result.toString();
	}

	/**
	 * �������������������еı���໥����SQL
	 * 
	 * @return
	 */
	private String getAllFKRelationSQL() throws Exception
	{
		StringBuffer result = new StringBuffer("");
		/* ȡ�������еı��� */
		Iterator selectTableMap_it = this.selectPartTableNameMap.keySet().iterator();
		/* ȡ�������еı��� */
		Iterator whereTableMap_it = this.wherePartTableNameMap.keySet().iterator();

		while (selectTableMap_it.hasNext())
		{
			String factTableName = (String) selectTableMap_it.next();
			
			while (whereTableMap_it.hasNext())
			{
				String dimTableName = (String) whereTableMap_it.next();

				/* ��������ϵSQL */
				String relationSQL = this.getFKRelationSQLByTableName(factTableName, dimTableName);

				if (relationSQL != null && !relationSQL.equals(""))
					result.append(relationSQL);
			}
			whereTableMap_it = this.wherePartTableNameMap.keySet().iterator();
		}
		/* ɾ������"and" */
		if (result != null && !result.equals("") && result.lastIndexOf(" and") != -1)
			result.delete(result.lastIndexOf(" and"), result.length());
		return result.toString();
	}

	/**
	 * ���ݱ����ƻ���������������ϵ��SQL
	 * 
	 * @param factTable
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	private String getFKRelationSQLByTableName(String factTable, String dimTable) throws Exception
	{
		/* ȡ�������ʵ�����������γ�ȱ����Ϣ */
		List FactTable_TbList = this.getFKInfoByTableName(factTable, this.SELECT_PART);

		/* ȡ�����γ�ȱ������������ʵ�����Ϣ */
		List DimTable_TbList = this.getFKInfoByTableName(dimTable, this.WHERE_PART);

		if (FactTable_TbList == null || DimTable_TbList == null)
			return null;
		for (int i = 0; i < FactTable_TbList.size(); i++)
		{
			for (int j = 0; j < DimTable_TbList.size(); j++)
			{
				/** ��ʵ�������Ϣ */
				SysIbmSysrels FactTb_FKInfo = (SysIbmSysrels) FactTable_TbList.get(i);
				/*��ʵ������*/
				String factTBName = FactTb_FKInfo.getTbName().trim();
				/* ����ʵ����γ�ȱ�������ֶ����� */
				String factTBColName = FactTb_FKInfo.getFkColNames().trim();
				
				/* ȡ�ø���ʵ�������γ�ȱ����� */
				String fact_dimTBName = FactTb_FKInfo.getRefTBName().trim();
				/* ����ʵ�������γ�ȱ�������ֶ����� */
				String fact_dimTBColName = FactTb_FKInfo.getPkColNames().trim();

				/** γ�ȱ������Ϣ */
				SysIbmSysrels DimTb_FKInfo = (SysIbmSysrels) DimTable_TbList.get(j);
				/*γ�ȱ�����*/
				String dimTBName = DimTb_FKInfo.getRefTBName().trim();
				/*��γ�ȱ�������ֶ����� */
				String dimTBColName = DimTb_FKInfo.getPkColNames().trim();

				/* ȡ�ø�γ�ȱ��������ʵ������ */
				String dim_factTBName = DimTb_FKInfo.getTbName().trim();
				/* �����γ�ȱ��������ʵ�������ֶ����� */
				String dim_factTBColName = DimTb_FKInfo.getFkColNames().trim();
				

				/* ��ʵ��ֱ�Ӻ�γ�ȱ��й��� */
				if (fact_dimTBName.equalsIgnoreCase(dimTBName))
					return factTBName + "." + factTBColName + "=" + dimTBName + "." + dimTBColName + " and ";
				/* ��ʵ���Ӻ�γ�ȱ����(����γ��) */
				else if (fact_dimTBName.equalsIgnoreCase(dim_factTBName))
				{
					/* �Ѹñ�����Ƽӵ�Map��,�Ա���From�м���ñ��� */
					if (!this.tableNameMap.containsKey(fact_dimTBName))
					{
						this.tableNameMap.put(fact_dimTBName, "");
					}
					return factTable + "." + factTBColName + "=" + fact_dimTBName + "." + fact_dimTBColName
							+ " and " + dim_factTBName + "." + dim_factTBColName + "=" + dimTBName + "."
							+ dimTBColName + " and ";
				}

			}
		}
		return "";
	}

	/**
	 * ���ݱ����ƻ���������������Ϣ(�������ʵ��,�������ж���һ��γ�ȱ�,�����γ�ȱ�,�������������ʵ�����)
	 * 
	 * @param tableName
	 * @param int
	 *            flag 1-��ʵ�� 2-γ�ȱ�
	 * @return
	 * @throws Exception
	 */
	private List getFKInfoByTableName(String tableName, int flag) throws Exception
	{
		List resultList = null;
		FitechConnection fitechConn = new FitechConnection();
		Connection conn = fitechConn.getConnect();
		if (conn == null)
		{
			return null;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			String sql = "select * from RELS sys ";
			if (flag == this.SELECT_PART)
				sql += "where sys.tbname = ?";
			else if (flag == this.WHERE_PART)
				sql += "where sys.reftbname = ?";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tableName.toUpperCase());
			rs = stmt.executeQuery();
			resultList = new ArrayList();
			while (rs.next())
			{
				SysIbmSysrels sysIbmSysrels = new SysIbmSysrels();
				sysIbmSysrels.setTbName(rs.getString("tbname"));
				sysIbmSysrels.setRefTBName(rs.getString("reftbname"));
				sysIbmSysrels.setFkColNames(rs.getString("FKCOLNAMES"));
				sysIbmSysrels.setPkColNames(rs.getString("PKCOLNAMES"));
				resultList.add(sysIbmSysrels);
			}
		}
		catch (Exception e)
		{
			resultList = null;
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
		finally
		{
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}

		return resultList;
	}

	/**
	 * ��ӻ�������
	 * 
	 * @return
	 */
	private String addOrgParama() throws Exception
	{
		StringBuffer result = new StringBuffer("");
		/* ȡ�������еı��� */
		Iterator selectTableMap_it = this.selectPartTableNameMap.keySet().iterator();
		while (selectTableMap_it.hasNext())
		{
			String factTableName = (String) selectTableMap_it.next();
			/* ��������ϵSQL */
			String relationSQL = this.getOrgSQLByTableName(factTableName);
			if (relationSQL != null && !relationSQL.equals(""))
				result.append(relationSQL);
		}
		/* ɾ������"and" */
		if (result != null && !result.equals("") && result.lastIndexOf(" and") != -1)
			result.delete(result.lastIndexOf(" and"), result.length());
		return result.toString();
	}

	/**
	 * ���ݱ�����DB2��ϵͳ��,����ñ��л����ֶ��򷵻ػ�������
	 * 
	 * @param tableName
	 * @return
	 */
	private String getOrgSQLByTableName(String tableName) throws Exception
	{
		String orgSQL = "";
		FitechConnection fitechConn = new FitechConnection();
		Connection conn = fitechConn.getConnect();
		if (conn == null)
		{
			return null;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			String sql = "select NAME from SYSIBM.SYSCOLUMNS sys where sys.TBNAME = ? and sys.NAME = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tableName.toUpperCase());
			stmt.setString(2, "ORG_ID");
			rs = stmt.executeQuery();

			if (rs.next())
			{
				orgSQL = tableName + ".ORG_ID in ((@����ID@)) and";
			}
		}
		catch (Exception e)
		{
			orgSQL = "";
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
		finally
		{
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}

		return orgSQL;
	}

	public static void main(String arg[])
	{

		DataRelationFormulaParseUtil util = new DataRelationFormulaParseUtil();
		try
		{
			String formulaStr = "(<<SUM({ͬҵ����.���})[�ͻ������Ϣ��.�������] = '��������' and [ͬҵҵ��Ʒ��.����] = '���ͬҵ' and [����.��������] = '�����' and [ͬҵ����.���ݲɼ�����]=(@������@)>>)/10000";
			util.parseFormualStr(formulaStr);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
