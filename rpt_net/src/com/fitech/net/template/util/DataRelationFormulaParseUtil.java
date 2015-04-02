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
 * 业务系统生成公式解析工具类
 * 
 * @author Yao
 * 
 */
public class DataRelationFormulaParseUtil
{

	/**
	 * 存放检索部分的表名称
	 */
	private Map selectPartTableNameMap = null;

	/**
	 * 存放检索条件部分的表名称
	 */
	public Map wherePartTableNameMap = null;

	/**
	 * 存放所有的表名 
	 */
	private Map tableNameMap = null;

	/**
	 * 公式中每一个表达式的开头标志
	 */
	private final String EXPRESSION_START_SIGN = "<<";

	/**
	 * 公式中每一个表达式的结束标志
	 */
	private final String EXPRESSION_END_SIGN = ">>";

	/**
	 * 表达式中检索字段的开始标志
	 */
	private final String SELECT_START_SIGN = "{";

	/**
	 * 表达式中检索字段的结束标志
	 */
	private final String SELECT_END_SIGN = "}";

	/**
	 * 表达式中条件字段的开始标志
	 */
	private final String WHERE_START_SIGN = "[";

	/**
	 * 表达式中条件字段的结束标志
	 */
	private final String WHERE_END_SIGN = "]";

	/**
	 * 表名,字段分割符
	 */
	private final String TABLE_COL_SEPARATOR_SIGN = ".";

	/**
	 * Select部分
	 */
	private final int SELECT_PART = 1;

	/**
	 * Where部分
	 */
	private final int WHERE_PART = 2;

	/**
	 * 其他部分(order by | group by | having 等)
	 */
	private final int OTHER_PART = 3;

	/**
	 * 数值类型
	 */
	private final int NUM_TYPE = 0;

	/**
	 * 字符类型
	 */
	private final int STR_TYPE = 1;

	public DataRelationFormulaParseUtil()
	{

	}

	/**
	 * 解析公式
	 * 
	 * @param formualStr
	 * @return 返回解析后的公式
	 */
	public String parseFormualStr(String formulalStr) throws Exception
	{
		if (formulalStr == null || formulalStr.equals(""))
			return "";
		StringBuffer resultStr = new StringBuffer("");

		/* 获取第一个科目SQL的开始标志位置 */
		int index_of_startSign = formulalStr.indexOf(this.EXPRESSION_START_SIGN);
		if (index_of_startSign == -1)
			throw new StringIndexOutOfBoundsException("公式: " + formulalStr + " 错误!缺少表达式开始括号!");
		/* 解析到的位置 */
		int parseIndex = 0;

		while (index_of_startSign >= 0)
		{
			/* 获得对应的结束标志位置 */
			int index_of_endSign = formulalStr.indexOf(this.EXPRESSION_END_SIGN, index_of_startSign);

			if (index_of_endSign == -1)
				throw new StringIndexOutOfBoundsException("公式: " + formulalStr + " 错误!缺少表达式结束括号!");

			/* 取出一对"<< >>"中的表达式 */
			String expressionStr = formulalStr.substring(index_of_startSign + 2, index_of_endSign);

			/* 解析该表达式为SQL公式 */
			String SQLStrParsed = this.parseExpressionToSQL(expressionStr);

			if (SQLStrParsed == null || SQLStrParsed.equals(""))
				throw new StringIndexOutOfBoundsException("表达式: " + expressionStr + " 错误!不能解析成SQL!");

			/* 将解析好的部分追加到解析结果中 */
			resultStr.append(formulalStr.substring(parseIndex, index_of_startSign + 2));
			resultStr.append(SQLStrParsed);
			resultStr.append(formulalStr.substring(index_of_endSign, index_of_endSign + 2));
			parseIndex = index_of_endSign + 2;

			/* 取得下一个表达式开始标志 */
			index_of_startSign = formulalStr.indexOf(this.EXPRESSION_START_SIGN, index_of_endSign + 2);
		}
		resultStr.append(formulalStr.substring(parseIndex));
		
		
		
		return resultStr.toString();
	}

	/**
	 * 解析表达式成SQL
	 * 
	 * @param expressionStr
	 * @return
	 */
	private String parseExpressionToSQL(String expressionStr) throws Exception
	{
		StringBuffer resultSQL = new StringBuffer("");

		/* 初始化存放检索部分表名称,检索条件部分表名称,全部表名称的Map */
		this.selectPartTableNameMap = new HashMap();
		this.wherePartTableNameMap = new HashMap();
		this.tableNameMap = new HashMap();

		/* 取得第一个条件开始括号的位置 */
		int index_of_whereSign = expressionStr.indexOf(this.WHERE_START_SIGN);

		/* 如果没有where的标志,则直接不解析该表达式直接返回(有时需要直接手工写SQL语句) */
		if (index_of_whereSign == -1)
		{
			return expressionStr;
			// throw new StringIndexOutOfBoundsException("表达式: "+expressionStr+"
			// 错误!缺少条件开始括号!");
		}

		/* 分割出检索字段部分和条件部分 */
		String selectStr = expressionStr.substring(0, index_of_whereSign);
		String whereStr = expressionStr.substring(index_of_whereSign);

		/* 解析select部分 */
		String selectSQL = this.parseSelectPartToSQL(selectStr);
		/* 解析where部分 */
		String whereSQL = this.parseWherePartToSQL(whereStr);

		/** 添加机构参数 */
		String orgSQL = this.addOrgParama();

		/* 获得主外键关系部分 */
		String relationSQL = this.getAllFKRelationSQL();
		/* 获得From部分 */
		String fromSQL = this.getFromTableSQL();

		if (selectSQL == null || selectSQL.equals("") || whereSQL == null || whereSQL.equals(""))
			throw new StringIndexOutOfBoundsException("公式错误!" + expressionStr + "不能解析成SQL!");

		/* 组成SQL语句 */
		resultSQL.append("select " + selectSQL + " from " + fromSQL + " where 1=1");
		
		/*机构条件*/
		if (orgSQL != null && !orgSQL.equals(""))
			resultSQL.append(" and " + orgSQL);
		
		/*主外键条件*/
		if (relationSQL != null && !relationSQL.equals(""))
			resultSQL.append(" and " + relationSQL);
		
		/*查询条件*/
		if (whereSQL != null && !whereSQL.equals(""))
			resultSQL.append(" and " + whereSQL);

		return resultSQL.toString();
	}

	/**
	 * 解析Select部分成SQL
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
	 * 解析where部分成SQL
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
	 * 替换所有中文字段成英文字段
	 * 
	 * @param str
	 * @param flag
	 *            替换类型 1-"{}"部分 2-"[]"部分
	 */
	public String replaceCNToEN(String str, int flag) throws Exception
	{
		StringBuffer resultSQL = new StringBuffer("");
		/* 开始标志 */
		String startSign = "";
		/* 结束标志 */
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

		/* 获得第一个开始标志 */
		int index_of_startSign = str.indexOf(startSign);
		/* 解析位置 */
		int parseIndex = 0;
		int index_of_endSign = 0;
		while (index_of_startSign >= 0)
		{
			/* 结束标志 */
			index_of_endSign = str.indexOf(endSign, index_of_startSign);
			if (index_of_endSign == -1)
				throw new StringIndexOutOfBoundsException("表达式错误!" + str + "没有结束标志!");

			/* 字段中文名称 */
			String field_CN = str.substring(index_of_startSign + 1, index_of_endSign);
			/* 字段英文名称 */
			String field_EN = this.parseFieldCNToEN(field_CN, flag);

			/* 将解析好的部分追加到解析结果中 */
			resultSQL.append(str.substring(parseIndex, index_of_startSign));

			if (field_EN != null && !field_EN.equals(""))
				resultSQL.append(field_EN);
			else
				resultSQL.append(field_CN);
			
			/*获得下一个开始标志*/
			index_of_startSign = str.indexOf(startSign, index_of_endSign);
			
			parseIndex = index_of_endSign + 1;
		}
		resultSQL.append(str.substring(parseIndex));

		return resultSQL.toString();
	}

	/**
	 * 解析中文字段成英文
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

		/* 分割出中文表名和字段名称 */
		int index_of_separator = field.indexOf(this.TABLE_COL_SEPARATOR_SIGN);
		String tableName_CN = null;
		String colName_CN = null;
		if (index_of_separator != -1)
		{
			tableName_CN = field.substring(0, index_of_separator);
			colName_CN = field.substring(index_of_separator + 1, field.length());
		}
		else
			throw new StringIndexOutOfBoundsException("缺少表和字段的分割标志!" + field);

		// 根据表名和字段名的中文名称,获得他的字段信息
		VParameterForm fieldInfo = StrutsVParameterDelegate.getENNameByCNName(tableName_CN, colName_CN);

		if (fieldInfo != null)
		{
			/* 表名 */
			String tableName_EN = fieldInfo.getVpTableid();
			/* 字段名 */
			String colName_EN = fieldInfo.getVpColumnid();
			/* 字段类型 */
			Integer colType = fieldInfo.getVpColtype();

			if (tableName_EN == null || colName_EN == null || colType == null)
				throw new IllegalArgumentException("没有该中文表名和字段名的对应信息!" + tableName_CN + "." + colName_CN);
			else
			{
				tableName_EN = tableName_EN.toUpperCase();
				colName_EN = colName_EN.toUpperCase();
			}
			/* 将解析出的表名存放近对应的Map中 */
			if (!this.tableNameMap.containsKey(tableName_EN))
				this.tableNameMap.put(tableName_EN, "");
			if (!saveToMap.containsKey(tableName_EN))
				saveToMap.put(tableName_EN, "");

			/* 如果是检索字段并且是数值类型,则加上SUM() */
			/*
			 * if(flag==this.SELECT_PART && colType.intValue()==this.NUM_TYPE)
			 * result = "SUM("+tableName_EN + "." + colName_EN+")"; else result =
			 * tableName_EN + "." + colName_EN;
			 */

			result = tableName_EN + "." + colName_EN;
		}
		else
			throw new IllegalArgumentException("没有该中文表名和字段名的对应信息!" + tableName_CN + "." + colName_CN);
		return result;

	}

	/**
	 * 获得SQL From后的Table名称
	 * 
	 * @return
	 */
	private String getFromTableSQL() throws Exception
	{
		StringBuffer result = new StringBuffer("");

		/* 取出所有表名 */
		Iterator tableMap_it = this.tableNameMap.keySet().iterator();
		while (tableMap_it.hasNext())
		{
			result.append(tableMap_it.next() + Config.SPLIT_SYMBOL_COMMA);
		}

		/* 删除最后的逗号 */
		if (!result.equals("") && result.lastIndexOf(Config.SPLIT_SYMBOL_COMMA) != -1)
		{
			result.delete(result.lastIndexOf(Config.SPLIT_SYMBOL_COMMA), result.length());
		}
		return result.toString();
	}

	/**
	 * 获得请求项和请求条件中的表的相互关联SQL
	 * 
	 * @return
	 */
	private String getAllFKRelationSQL() throws Exception
	{
		StringBuffer result = new StringBuffer("");
		/* 取出检索中的表名 */
		Iterator selectTableMap_it = this.selectPartTableNameMap.keySet().iterator();
		/* 取出检索中的表名 */
		Iterator whereTableMap_it = this.wherePartTableNameMap.keySet().iterator();

		while (selectTableMap_it.hasNext())
		{
			String factTableName = (String) selectTableMap_it.next();
			
			while (whereTableMap_it.hasNext())
			{
				String dimTableName = (String) whereTableMap_it.next();

				/* 获得外键关系SQL */
				String relationSQL = this.getFKRelationSQLByTableName(factTableName, dimTableName);

				if (relationSQL != null && !relationSQL.equals(""))
					result.append(relationSQL);
			}
			whereTableMap_it = this.wherePartTableNameMap.keySet().iterator();
		}
		/* 删除最后的"and" */
		if (result != null && !result.equals("") && result.lastIndexOf(" and") != -1)
			result.delete(result.lastIndexOf(" and"), result.length());
		return result.toString();
	}

	/**
	 * 根据表名称获得他的所有外键关系的SQL
	 * 
	 * @param factTable
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	private String getFKRelationSQLByTableName(String factTable, String dimTable) throws Exception
	{
		/* 取得与该事实表关联的所有纬度表的信息 */
		List FactTable_TbList = this.getFKInfoByTableName(factTable, this.SELECT_PART);

		/* 取得与该纬度表关联的所有事实表的信息 */
		List DimTable_TbList = this.getFKInfoByTableName(dimTable, this.WHERE_PART);

		if (FactTable_TbList == null || DimTable_TbList == null)
			return null;
		for (int i = 0; i < FactTable_TbList.size(); i++)
		{
			for (int j = 0; j < DimTable_TbList.size(); j++)
			{
				/** 事实表关联信息 */
				SysIbmSysrels FactTb_FKInfo = (SysIbmSysrels) FactTable_TbList.get(i);
				/*事实表名称*/
				String factTBName = FactTb_FKInfo.getTbName().trim();
				/* 该事实表与纬度表关联的字段名称 */
				String factTBColName = FactTb_FKInfo.getFkColNames().trim();
				
				/* 取得该事实表关联的纬度表名称 */
				String fact_dimTBName = FactTb_FKInfo.getRefTBName().trim();
				/* 该事实表关联的纬度表的主键字段名称 */
				String fact_dimTBColName = FactTb_FKInfo.getPkColNames().trim();

				/** 纬度表关联信息 */
				SysIbmSysrels DimTb_FKInfo = (SysIbmSysrels) DimTable_TbList.get(j);
				/*纬度表名称*/
				String dimTBName = DimTb_FKInfo.getRefTBName().trim();
				/*该纬度表的主键字段名称 */
				String dimTBColName = DimTb_FKInfo.getPkColNames().trim();

				/* 取得该纬度表关联的事实表名称 */
				String dim_factTBName = DimTb_FKInfo.getTbName().trim();
				/* 该与该纬度表关联的事实表的外键字段名称 */
				String dim_factTBColName = DimTb_FKInfo.getFkColNames().trim();
				

				/* 事实表直接和纬度表有关联 */
				if (fact_dimTBName.equalsIgnoreCase(dimTBName))
					return factTBName + "." + factTBColName + "=" + dimTBName + "." + dimTBColName + " and ";
				/* 事实表间接和纬度表关联(二级纬度) */
				else if (fact_dimTBName.equalsIgnoreCase(dim_factTBName))
				{
					/* 把该表的名称加到Map中,以便在From中加入该表名 */
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
	 * 根据表名称获得他的所有外键信息(如果是事实表,则获得它有多少一级纬度表,如果是纬度表,则获得它与多少事实表关联)
	 * 
	 * @param tableName
	 * @param int
	 *            flag 1-事实表 2-纬度表
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
	 * 添加机构参数
	 * 
	 * @return
	 */
	private String addOrgParama() throws Exception
	{
		StringBuffer result = new StringBuffer("");
		/* 取出检索中的表名 */
		Iterator selectTableMap_it = this.selectPartTableNameMap.keySet().iterator();
		while (selectTableMap_it.hasNext())
		{
			String factTableName = (String) selectTableMap_it.next();
			/* 获得外键关系SQL */
			String relationSQL = this.getOrgSQLByTableName(factTableName);
			if (relationSQL != null && !relationSQL.equals(""))
				result.append(relationSQL);
		}
		/* 删除最后的"and" */
		if (result != null && !result.equals("") && result.lastIndexOf(" and") != -1)
			result.delete(result.lastIndexOf(" and"), result.length());
		return result.toString();
	}

	/**
	 * 根据表名查DB2的系统表,如果该表有机构字段则返回机构条件
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
				orgSQL = tableName + ".ORG_ID in ((@机构ID@)) and";
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
			String formulaStr = "(<<SUM({同业往来.余额})[客户类别信息表.类别名称] = '境内银行' and [同业业务品种.描述] = '拆放同业' and [币种.中文名称] = '人民币' and [同业往来.数据采集日期]=(@报告日@)>>)/10000";
			util.parseFormualStr(formulaStr);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
