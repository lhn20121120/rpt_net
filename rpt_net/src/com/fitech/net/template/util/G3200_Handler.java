package com.fitech.net.template.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fitech.net.form.IDataRelationForm;

/**
 * G3200报表生成特殊处理类
 * @author Yao
 *
 *	表达式例如:[G0100_E6]+[G0100_E7]+[G0100_E8]+[G0100_E9]+[G0100_E10]
 */
public class G3200_Handler
{
	/**
	 * 公式集合
	 */
	private List formulaList = null;
	
	/**
	 * 参数Map
	 */
	private Map paramaMap = null;
	
	/**
	 * 币种对应的行号数组
	 */
	private final String[] ROW_ARRAY={"6","7","8","9","10","11","12","13"};
	
	private List rowList = null;
	/**
	 * 币种名称
	 */
	private final String[] CURRENCY_NAME_ARRAY = {"美元","欧元","日本元","英镑","港币","瑞士法郎","澳大利亚元","加拿大元"};
	
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
	 * SQL解析工具类
	 */
	private SQLFormulaParseUtil sqlParseUtil;
	
	
	/**
	 * 四则运算所用的库表名称(可以改变)
	 */
	private final String CALCULATE_TABLE = "CALCULATE_TABLE";
	/**
	 * 版本号
	 */
	private final String VERSION="0514";
	
	public G3200_Handler(List formulaList, Map paramaMap)
	{
		this.formulaList = formulaList;
		this.paramaMap = paramaMap;
		sqlParseUtil = new SQLFormulaParseUtil();

		rowList = new ArrayList();
		
		/*初始化行号列表(方便后面找到行号对应的货币名称)*/
		for(int i=0;i<this.ROW_ARRAY.length;i++)
		{
			rowList.add(ROW_ARRAY[i]);
		}
	}
	
	/**
	 * 获得所有单元格数据
	 * @return
	 */
	public Map getCellValueMap() throws Exception
	{
		if(this.formulaList==null)
			return null;
		
		Map cellMap = new HashMap();
		
		/*遍历解析出所有公式*/
		for(int i=0;i<this.formulaList.size();i++)
		{
			IDataRelationForm IDataRelationForm = (IDataRelationForm) this.formulaList.get(i);
					
			String cellName = IDataRelationForm.getCellName();
			
			/*该单元格的表达式*/
			String SQLFormula = IDataRelationForm.getIdrSql();
			try
			{
				/*解析该公式*/
				String value = this.parseFormulaToValue(cellName,SQLFormula);
				
				cellMap.put(cellName, value);
				
				// System.out.println("CellName===" + cellName + " Value===" + value);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				continue;
			}
		}
		return cellMap;
	}
	
	/**
	 * 解析公式并返回它的值
	 * @param sqlFormula
	 * @return
	 */
	private String parseFormulaToValue(String G3200_CellName ,String formulaStr) throws Exception
	{
		if(G3200_CellName==null || G3200_CellName.equals("") 
				|| formulaStr==null || formulaStr.equals(""))
			return "";
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
			String value = this.parseCellStrToValue(G3200_CellName,cellStr);
			if(value==null)
				value="0.0";
			
			// System.out.println(cellStr+" === " + value);
			
			/* 将解析好的部分追加到解析结果中 */
			resultStr.append(formulaStr.substring(parseIndex, index_of_startSign));
			resultStr.append("double("+value+")");
			parseIndex = index_of_endSign + 1;
			
			/*取得下一个单元格表达式的开始标志*/
			index_of_startSign = formulaStr.indexOf(this.CELL_START_SIGN, index_of_endSign+1);
		}
		resultStr.append(formulaStr.substring(parseIndex));
		
		// System.out.println("*******解析结果===" + resultStr);
		
		if(resultStr!=null && !resultStr.equals(""))
		{		
			String sql = "select " + resultStr + " from " + this.CALCULATE_TABLE;
			String value = sqlParseUtil.executeSQL(sql);
			return sqlParseUtil.setPrecision(value);
		}	
		else
			return null;
	}
	
	/**
	 * 解析每个单元格表达式,并返回它的值
	 * @return
	 */
	private String parseCellStrToValue(String G3200_CellName,String cellStr) throws Exception
	{
		if(G3200_CellName==null || G3200_CellName.equals("") 
				||cellStr==null || cellStr.equals(""))
			return "0.0";
		String result = "0.0";
		String cellSQL = this.getCellSQLStrByCellStr(cellStr);
		if(cellSQL!=null && !cellSQL.equals(""))
		{
			/*根据不同的行号,替换成不同的币种条件*/
			String rowId = this.splitColAndRow(G3200_CellName)[1];
			//// System.out.println("RowId=="+rowId);
			int crrencyIndex = this.rowList.indexOf(rowId);
			if(crrencyIndex!=-1)
			{
				String currName = this.CURRENCY_NAME_ARRAY[crrencyIndex];
				/*替换币种条件(考虑到"<>" 和" '人民币'"中有空格和没空格的情况)*/
				String newSQL = cellSQL.replaceAll("<> '人民币'","='"+currName+"'").replaceAll("<>'人民币'","='"+currName+"'");
				// System.out.println("NewSQL=="+newSQL);
				
				result = this.sqlParseUtil.parseSQLFormual(newSQL,this.paramaMap);
			}
			
		}
		return result;
		
	}
	
	/**
	 * 根据单元格名称获得它的SQL表达式
	 * @param cellStr
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
			cellSQL = this.sqlParseUtil.executeSQL(sql);	
		}
		
		return cellSQL;
	}
	
	/**
	 * 拆分行列名，如AA34拆分成AA，34
	 */
	private String[] splitColAndRow(String cellName)
	{

		String[] rowAndCol = new String[2];
		if (cellName == null || cellName.equals(""))
		{
			return null;
		}
		else
		{
			String[] temp = cellName.split("\\d");
			rowAndCol[0] = temp[0];
			rowAndCol[1] = cellName.substring(rowAndCol[0].length(), cellName.length());
			return rowAndCol;
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO 自动生成方法存根

	}

}
