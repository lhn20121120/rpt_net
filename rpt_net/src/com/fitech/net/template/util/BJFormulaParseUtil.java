package com.fitech.net.template.util;


import java.util.Map;

/**
 * 表间公式处理解析工具类
 * @author Yao
 *
 *	表达式例如:[G0100_E6]+[G0200_E7]+[G0100_E8]+[G0100_E9]
 */
public class BJFormulaParseUtil
{
	/**
	 * 参数Map
	 */
	private Map paramaMap = null;
		
	/**
	 * 表达式中单元格开始标志
	 */
	private final String CELL_START_SIGN="[";
	
	/**
	 * 表达式中单元格结束标志
	 */
	private final String CELL_END_SIGN="]";
	 
	/**
	 * 单元格表达式中 报表名称和单元格的分割标志 例如:G0100_A3
	 */
	private final String CELL_STR_SPLIT="_";

	/**
	 * 解析工具类
	 */
	private SQLFormulaParseUtil parseUtil;
	
	/**
	 * 四则运算所用的库表名称(可以改变)
	 */
	private final String CALCULATE_TABLE = "CALCULATE_TABLE";
	/**
	 * 版本号
	 */
	private final String VERSION="0514";
	
	
	public BJFormulaParseUtil()
	{
		parseUtil = new SQLFormulaParseUtil();
		
	}
	
	/**
	 * 解析表间公式
	 * @param formulaStr 公式
	 * @param paramaMap 参数
	 * @return
	 * @throws Exception
	 */
	public String parseBJFormula(String formulaStr,Map paramaMap) throws Exception
	{
		if(formulaStr==null || formulaStr.equals("") 
				|| paramaMap==null)
			return null;
		
		this.paramaMap = paramaMap;

		StringBuffer resultStr = new StringBuffer("");

		/* 获取表达式中第一个单元格的开始标志位置*/
		int index_of_startSign = formulaStr.indexOf(this.CELL_START_SIGN);
		
		if (index_of_startSign == -1)
			throw new StringIndexOutOfBoundsException("公式: "+formulaStr+" 错误!缺少表达式开始括号!");
		
		/* 解析到的位置 */
		int parseIndex = 0;

		while (index_of_startSign >= 0)
		{
			/* 获得对应的结束标志位置 */
			int index_of_endSign = formulaStr.indexOf(this.CELL_END_SIGN, index_of_startSign);

			if (index_of_endSign == -1)
				throw new StringIndexOutOfBoundsException("公式: "+formulaStr+" 错误!缺少表达式结束括号!");
			
			/* 取出一对"[ ]"中单元格表达式 */
			String cellStr = formulaStr.substring(index_of_startSign + 1, index_of_endSign);

			/* 解析出该单元格的值*/
			String value = this.parseCellStrToValue(cellStr);
			
			// System.out.println(cellStr+" === " + value);
			if(value==null)
				value="0.0";
			/* 将解析好的部分追加到解析结果中 */
			resultStr.append(formulaStr.substring(parseIndex, index_of_startSign));
			resultStr.append(value);
			parseIndex = index_of_endSign + 1;
			
			/*取得下一个单元格表达式的开始标志*/
			index_of_startSign = formulaStr.indexOf(this.CELL_START_SIGN, index_of_endSign+1);
		}
		resultStr.append(formulaStr.substring(parseIndex));
		
		// System.out.println("*******解析结果===" + resultStr);
		
		if(resultStr!=null && !resultStr.equals(""))
		{		
			String sql = "select " + resultStr + " from " + this.CALCULATE_TABLE;
			// System.out.println("****计算SQL===="+sql);
			String value = parseUtil.executeSQL(sql);
			//// System.out.println("Value===="+value);
			return parseUtil.setPrecision(value);
		}	
		else
			return null;
	}
	
	
	/**
	 * 解析每个单元格表达式,并返回它的值
	 * @param cellStr 单元格表达式 例如 G0100_A3
	 * @return
	 */
	private String parseCellStrToValue(String cellStr) throws Exception
	{
		if(cellStr==null || cellStr.equals(""))
			return "0.0";
		String result = "0.0";
		String cellSQL = this.getCellSQLStrByCellStr(cellStr);
		if(cellSQL!=null)
		{
			result = this.parseUtil.parseSQLFormual(cellSQL,this.paramaMap);
		}
		return result;
		
	}
	
	/**
	 * 根据单元格名称获得它的SQL表达式
	 * @param cellStr 单元格表达式 例如 G0100_A3
	 * @return
	 * @throws Exception
	 */
	private String getCellSQLStrByCellStr(String cellStr) throws Exception
	{
		if(cellStr == null || cellStr.equals(""))
			return null;
		String cellSQL = null;
		
		String childRepId = null;
		String cellName =null;
		/*分割出报表Id 和 单元格名称 */
		String[] cellStrSplit = cellStr.split(this.CELL_STR_SPLIT);
		if(cellStrSplit!=null && cellStrSplit.length==2)
		{
			childRepId = cellStrSplit[0];
			cellName = cellStrSplit[1];
		}
		
		if(childRepId!=null && !childRepId.equals("") 
				&& cellName!=null && !cellName.equals("") )
		{
			String sql = "select idata.IDR_SQL from I_DATARELATION idata "
				+ "where idata.IDR_REPID='"+childRepId+"'and idata.IDR_VERSIONID='"+this.VERSION+"' " +
					"and idata.IDR_CELLNAME='"+cellName+"'";
			//// System.out.println("SQL=="+sql);
			cellSQL = this.parseUtil.executeSQL(sql);	
		}
		
		return cellSQL;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO 自动生成方法存根

	}

}
