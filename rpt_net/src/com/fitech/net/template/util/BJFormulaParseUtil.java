package com.fitech.net.template.util;


import java.util.Map;

/**
 * ��乫ʽ�������������
 * @author Yao
 *
 *	���ʽ����:[G0100_E6]+[G0200_E7]+[G0100_E8]+[G0100_E9]
 */
public class BJFormulaParseUtil
{
	/**
	 * ����Map
	 */
	private Map paramaMap = null;
		
	/**
	 * ���ʽ�е�Ԫ��ʼ��־
	 */
	private final String CELL_START_SIGN="[";
	
	/**
	 * ���ʽ�е�Ԫ�������־
	 */
	private final String CELL_END_SIGN="]";
	 
	/**
	 * ��Ԫ����ʽ�� �������ƺ͵�Ԫ��ķָ��־ ����:G0100_A3
	 */
	private final String CELL_STR_SPLIT="_";

	/**
	 * ����������
	 */
	private SQLFormulaParseUtil parseUtil;
	
	/**
	 * �����������õĿ������(���Ըı�)
	 */
	private final String CALCULATE_TABLE = "CALCULATE_TABLE";
	/**
	 * �汾��
	 */
	private final String VERSION="0514";
	
	
	public BJFormulaParseUtil()
	{
		parseUtil = new SQLFormulaParseUtil();
		
	}
	
	/**
	 * ������乫ʽ
	 * @param formulaStr ��ʽ
	 * @param paramaMap ����
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

		/* ��ȡ���ʽ�е�һ����Ԫ��Ŀ�ʼ��־λ��*/
		int index_of_startSign = formulaStr.indexOf(this.CELL_START_SIGN);
		
		if (index_of_startSign == -1)
			throw new StringIndexOutOfBoundsException("��ʽ: "+formulaStr+" ����!ȱ�ٱ��ʽ��ʼ����!");
		
		/* ��������λ�� */
		int parseIndex = 0;

		while (index_of_startSign >= 0)
		{
			/* ��ö�Ӧ�Ľ�����־λ�� */
			int index_of_endSign = formulaStr.indexOf(this.CELL_END_SIGN, index_of_startSign);

			if (index_of_endSign == -1)
				throw new StringIndexOutOfBoundsException("��ʽ: "+formulaStr+" ����!ȱ�ٱ��ʽ��������!");
			
			/* ȡ��һ��"[ ]"�е�Ԫ����ʽ */
			String cellStr = formulaStr.substring(index_of_startSign + 1, index_of_endSign);

			/* �������õ�Ԫ���ֵ*/
			String value = this.parseCellStrToValue(cellStr);
			
			// System.out.println(cellStr+" === " + value);
			if(value==null)
				value="0.0";
			/* �������õĲ���׷�ӵ���������� */
			resultStr.append(formulaStr.substring(parseIndex, index_of_startSign));
			resultStr.append(value);
			parseIndex = index_of_endSign + 1;
			
			/*ȡ����һ����Ԫ����ʽ�Ŀ�ʼ��־*/
			index_of_startSign = formulaStr.indexOf(this.CELL_START_SIGN, index_of_endSign+1);
		}
		resultStr.append(formulaStr.substring(parseIndex));
		
		// System.out.println("*******�������===" + resultStr);
		
		if(resultStr!=null && !resultStr.equals(""))
		{		
			String sql = "select " + resultStr + " from " + this.CALCULATE_TABLE;
			// System.out.println("****����SQL===="+sql);
			String value = parseUtil.executeSQL(sql);
			//// System.out.println("Value===="+value);
			return parseUtil.setPrecision(value);
		}	
		else
			return null;
	}
	
	
	/**
	 * ����ÿ����Ԫ����ʽ,����������ֵ
	 * @param cellStr ��Ԫ����ʽ ���� G0100_A3
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
	 * ���ݵ�Ԫ�����ƻ������SQL���ʽ
	 * @param cellStr ��Ԫ����ʽ ���� G0100_A3
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
		/*�ָ������Id �� ��Ԫ������ */
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
		// TODO �Զ����ɷ������

	}

}
