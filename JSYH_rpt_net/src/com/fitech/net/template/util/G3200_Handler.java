package com.fitech.net.template.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fitech.net.form.IDataRelationForm;

/**
 * G3200�����������⴦����
 * @author Yao
 *
 *	���ʽ����:[G0100_E6]+[G0100_E7]+[G0100_E8]+[G0100_E9]+[G0100_E10]
 */
public class G3200_Handler
{
	/**
	 * ��ʽ����
	 */
	private List formulaList = null;
	
	/**
	 * ����Map
	 */
	private Map paramaMap = null;
	
	/**
	 * ���ֶ�Ӧ���к�����
	 */
	private final String[] ROW_ARRAY={"6","7","8","9","10","11","12","13"};
	
	private List rowList = null;
	/**
	 * ��������
	 */
	private final String[] CURRENCY_NAME_ARRAY = {"��Ԫ","ŷԪ","�ձ�Ԫ","Ӣ��","�۱�","��ʿ����","�Ĵ�����Ԫ","���ô�Ԫ"};
	
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
	 * SQL����������
	 */
	private SQLFormulaParseUtil sqlParseUtil;
	
	
	/**
	 * �����������õĿ������(���Ըı�)
	 */
	private final String CALCULATE_TABLE = "CALCULATE_TABLE";
	/**
	 * �汾��
	 */
	private final String VERSION="0514";
	
	public G3200_Handler(List formulaList, Map paramaMap)
	{
		this.formulaList = formulaList;
		this.paramaMap = paramaMap;
		sqlParseUtil = new SQLFormulaParseUtil();

		rowList = new ArrayList();
		
		/*��ʼ���к��б�(��������ҵ��кŶ�Ӧ�Ļ�������)*/
		for(int i=0;i<this.ROW_ARRAY.length;i++)
		{
			rowList.add(ROW_ARRAY[i]);
		}
	}
	
	/**
	 * ������е�Ԫ������
	 * @return
	 */
	public Map getCellValueMap() throws Exception
	{
		if(this.formulaList==null)
			return null;
		
		Map cellMap = new HashMap();
		
		/*�������������й�ʽ*/
		for(int i=0;i<this.formulaList.size();i++)
		{
			IDataRelationForm IDataRelationForm = (IDataRelationForm) this.formulaList.get(i);
					
			String cellName = IDataRelationForm.getCellName();
			
			/*�õ�Ԫ��ı��ʽ*/
			String SQLFormula = IDataRelationForm.getIdrSql();
			try
			{
				/*�����ù�ʽ*/
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
	 * ������ʽ����������ֵ
	 * @param sqlFormula
	 * @return
	 */
	private String parseFormulaToValue(String G3200_CellName ,String formulaStr) throws Exception
	{
		if(G3200_CellName==null || G3200_CellName.equals("") 
				|| formulaStr==null || formulaStr.equals(""))
			return "";
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
			String value = this.parseCellStrToValue(G3200_CellName,cellStr);
			if(value==null)
				value="0.0";
			
			// System.out.println(cellStr+" === " + value);
			
			/* �������õĲ���׷�ӵ���������� */
			resultStr.append(formulaStr.substring(parseIndex, index_of_startSign));
			resultStr.append("double("+value+")");
			parseIndex = index_of_endSign + 1;
			
			/*ȡ����һ����Ԫ����ʽ�Ŀ�ʼ��־*/
			index_of_startSign = formulaStr.indexOf(this.CELL_START_SIGN, index_of_endSign+1);
		}
		resultStr.append(formulaStr.substring(parseIndex));
		
		// System.out.println("*******�������===" + resultStr);
		
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
	 * ����ÿ����Ԫ����ʽ,����������ֵ
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
			/*���ݲ�ͬ���к�,�滻�ɲ�ͬ�ı�������*/
			String rowId = this.splitColAndRow(G3200_CellName)[1];
			//// System.out.println("RowId=="+rowId);
			int crrencyIndex = this.rowList.indexOf(rowId);
			if(crrencyIndex!=-1)
			{
				String currName = this.CURRENCY_NAME_ARRAY[crrencyIndex];
				/*�滻��������(���ǵ�"<>" ��" '�����'"���пո��û�ո�����)*/
				String newSQL = cellSQL.replaceAll("<> '�����'","='"+currName+"'").replaceAll("<>'�����'","='"+currName+"'");
				// System.out.println("NewSQL=="+newSQL);
				
				result = this.sqlParseUtil.parseSQLFormual(newSQL,this.paramaMap);
			}
			
		}
		return result;
		
	}
	
	/**
	 * ���ݵ�Ԫ�����ƻ������SQL���ʽ
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
			cellSQL = this.sqlParseUtil.executeSQL(sql);	
		}
		
		return cellSQL;
	}
	
	/**
	 * �������������AA34��ֳ�AA��34
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
		// TODO �Զ����ɷ������

	}

}
