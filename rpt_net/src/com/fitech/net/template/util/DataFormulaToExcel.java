package com.fitech.net.template.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.FitechConnection;
import com.fitech.net.common.UtilOffset;
import com.fitech.net.form.IDataRelationForm;

/**
 * ������ʽ��Excel������
 * @author Yao
 *
 */
public class DataFormulaToExcel
{
	/**
	 * ����Id
	 */
	private String childRepId = null;

	/**
	 * ����汾��
	 */
	private String versionId = null;

	/**
	 * �������Map
	 */
	private Map paramaMap = null;

	/**
	 * ����ID
	 */
	private String orgId = null;

	/**
	 * ģ��·��
	 */
	private String excelPath = null;

	/**
	 * ���Ϊ��·��
	 */
	private String saveAsPath = null;

	/**
	 * ����ID
	 */
	private final String HEAD_ORG_ID = "331010000";

	/**
	 * ���캯����ʼ����Ҫ�Ĳ���
	 * @param childRepId 
	 * @param versionId
	 * @param orgId
	 * @param reportDate
	 * @throws Exception
	 */
	public DataFormulaToExcel(String childRepId, String versionId, String orgId, String reportDate)
			throws Exception
	{
		if (childRepId == null || childRepId.equals("") || versionId == null || versionId.equals("")
				|| orgId == null || orgId.equals("") || reportDate == null || reportDate.equals(""))
		{
			throw new IllegalArgumentException("��������!�޷����ɸñ���!");
		}

		this.paramaMap = new HashMap();

		this.childRepId = childRepId;
		this.versionId = versionId;

		/*���ֻ�������*/
		if (orgId.equals("0"))
		{
			this.orgId = this.HEAD_ORG_ID;
			/* ��������Ϊ���еĻ������ܺ� */
			this.paramaMap.put("����ID", "select ORG_ID from V_ORG");
		}
		/*�ֻ�������*/
		else
		{
			this.orgId = orgId.trim();
			/* �������� Ϊ����������Id*/
			this.paramaMap.put("����ID", "'" + this.orgId + "'");
		}

		/* �����ղ��� */
		this.paramaMap.put("������", this.getDateId(reportDate));
		/*Excelģ��·��*/
		this.excelPath = Config.RAQ_TEMPLATE_PATH + "Reports" + Config.FILESEPARATOR + "templates"
				+ Config.FILESEPARATOR + this.childRepId + "_" + this.versionId + ".xls";

	//	 this.saveAsPath="D:";
	//	 this.excelPath ="D:\\template"+ Config.FILESEPARATOR + this.childRepId + "_" + this.versionId + ".xls";
		
		String year = reportDate.split("-")[0];
		String term = reportDate.split("-")[1];
		if(term != null && term.length() == 2 && term.indexOf("0") == 0){
			term = term.substring(1);
		}
		
		/*���Ϊ��·��*/
		this.saveAsPath = Config.WEBROOTPATH + "Reports" + Config.FILESEPARATOR + "releaseTemplates"
				+ Config.FILESEPARATOR + year + "_" + term;
		
		new File(this.saveAsPath).mkdir();
		this.saveAsPath = this.saveAsPath + Config.FILESEPARATOR + this.orgId + Config.FILESEPARATOR;

	}

	/**
	 * �������ݿ��е����й�ʽ,���ѽ�����Ĺ�ʽ���������SQL���ֶ�
	 * 
	 * @throws Exception
	 */
	public void parseFormula() throws Exception
	{
		// System.out.println("************Start Parse <<" + this.childRepId + ">>*************");
		
		/*ȡ�øñ�������й�ʽ��¼*/
		List resultList = this.getDBInfo(this.childRepId,this.versionId,null);

		// System.out.println("���ݿ��й�ʽ����===" + resultList.size());
		List parseResultList = new ArrayList();
		DataRelationFormulaParseUtil util = new DataRelationFormulaParseUtil();
		for (int i = 0; i < resultList.size(); i++)
		{
			try
			{
				IDataRelationForm IDataRelationForm = (IDataRelationForm) resultList.get(i);
				/*������ʽ*/
				String relative = IDataRelationForm.getIdrRelative();

				/*��ʽ*/
				String formula = IDataRelationForm.getIdrFormula();
							
				String resultSQL = null;

				/*�����ҵ��ϵͳ���ɹ�ʽ,������ù�ʽ*/
				if (relative.equals(Config.RELATIIONYWXTSC))
					resultSQL = util.parseFormualStr(formula);
				/*�Ǽ������򲻽���*/
				else if (relative.equals(Config.RELATONJSX))
					resultSQL = formula;

				IDataRelationForm.setIdrSql(resultSQL);
				parseResultList.add(IDataRelationForm);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				continue;
			}
		}
		/* �������ݿ� */
		this.updateDB(resultList);

		// System.out.println("************End Parse<<" + this.childRepId + ">>*************");
	}

	/**
	 * ��� I_DATARELATION ���е�����
	 * 
	 */
	public List getDBInfo(String childRepId,String versionId,String currId) throws Exception
	{
		if(childRepId==null || childRepId.equals("") 
				||versionId==null || versionId.equals(""))
			return null;
		
		List resultList = null;

		FitechConnection fitechConn = new FitechConnection();
		Connection conn = fitechConn.getConnect();

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			String sql = "select * from I_DATARELATION idata "
					+ "where idata.IDR_REPID=? and idata.IDR_VERSIONID=?";
			
			/*�鿴�Ƿ������������*/
			if(currId!=null && !currId.equals(""))
				sql += " and idata.VC_ID='"+currId+"'";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, childRepId);
			stmt.setString(2, versionId);
			
			rs = stmt.executeQuery();
			resultList = new ArrayList();
			while (rs.next())
			{
				IDataRelationForm IDataRelationForm = new IDataRelationForm();

				IDataRelationForm.setCellName(rs.getString("IDR_CELLNAME"));

				IDataRelationForm.setIdrFormula(rs.getString("IDR_FORMULA"));
				IDataRelationForm.setIdrSql(rs.getString("IDR_SQL"));
				IDataRelationForm.setIdrRelative(rs.getString("IDR_RELATIVE"));
				
				/*��������*/
				IDataRelationForm.setCurrId(rs.getString("VC_ID"));
				
				resultList.add(IDataRelationForm);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resultList = null;
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
	 * ���ݱ����������������ֱ���
	 * @return
	 */
	private List getCurrListByID(String childRepId,String versionId) throws Exception
	{
		if(childRepId==null || childRepId.equals("") 
				||versionId==null || versionId.equals(""))
			return null;
		
		List resultList = null;

		FitechConnection fitechConn = new FitechConnection();
		Connection conn = fitechConn.getConnect();

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			String sql = "select distinct idata.VC_ID from I_DATARELATION idata "
					+ "where idata.IDR_REPID=? and idata.IDR_VERSIONID=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, this.childRepId);
			stmt.setString(2, this.versionId);
			
			rs = stmt.executeQuery();
			resultList = new ArrayList();
			while (rs.next())
			{
				String currId = rs.getString("VC_ID");
				
				resultList.add(currId);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resultList = null;
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
	 * �������ݿ�SQL�ֶ�
	 * 
	 * @param list
	 */
	public void updateDB(List list) throws Exception
	{

		FitechConnection fitechConn = new FitechConnection();
		Connection conn = fitechConn.getConnect();

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			conn.setAutoCommit(false);

			String sql = "update I_DATARELATION idata set idata.IDR_SQL=? "
					+ "where idata.IDR_REPID=? and idata.IDR_VERSIONID=? and idata.IDR_CELLNAME=? "
					/*����*/
					+ "and idata.VC_ID=?";

			stmt = conn.prepareStatement(sql);

			for (int i = 0; i < list.size(); i++)
			{
				IDataRelationForm IDataRelationForm = (IDataRelationForm) list.get(i);
				stmt.setString(1, IDataRelationForm.getIdrSql());
				stmt.setString(2, this.childRepId);
				stmt.setString(3, this.versionId);
				stmt.setString(4, IDataRelationForm.getCellName());
				/*����*/
				stmt.setString(5,IDataRelationForm.getCurrId());
				
				stmt.addBatch();
			}

			stmt.executeBatch();
			conn.commit();

		}
		catch (Exception e)
		{
			e.printStackTrace();
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
	}

	/**
	 * ����SQL��д��Excel
	 * 
	 * @throws Exception
	 */
	public boolean createExcel() throws Exception
	{
		// System.out.println("**************Start Create Excel*********************");
		/* ����ʽ���� */
		int failedNum = 0;
		
		/*ȡ�øñ����ж��ٱ���*/
		List currList = this.getCurrListByID(this.childRepId,this.versionId);
		if(currList==null || currList.size()==0)
			return false;
		
		/*ѭ�����ɸ������ֵı���*/
		for(int j=0;j<currList.size();j++)
		{
			/*ȡ�ñ���ID*/
			String currId = (String)currList.get(j);
			
			/*��øñ���һ�ֱ����µ����м�¼*/
			List list = this.getDBInfo(this.childRepId,this.versionId,currId);

			Map cellMap = null;

			/*G3200���⴦��*/
			if (this.childRepId.equals("G3200"))
			{
				G3200_Handler G3200Handler = new G3200_Handler(list, this.paramaMap);
				cellMap = G3200Handler.getCellValueMap();
			}
			else
			{
				cellMap = new HashMap();
				SQLFormulaParseUtil sqlFormulaUtil = new SQLFormulaParseUtil();
				BJFormulaParseUtil bjFormulaUtil = new BJFormulaParseUtil();
				for (int i = 0; i < list.size(); i++)
				{
					IDataRelationForm IDataRelationForm = (IDataRelationForm) list.get(i);
					try
					{
						/*��øõ�Ԫ��Ĺ�����ʽ*/
						String relative = IDataRelationForm.getIdrRelative();

						/*��Ԫ������*/
						String cellName = IDataRelationForm.getCellName();

						/*��Ԫ��ʽ*/
						String formula = IDataRelationForm.getIdrSql();
						
						String value = null;

						/*���ҵ��ϵͳ����,������õ�Ԫ���SQL���ʽ*/
						if (relative != null && relative.equals(Config.RELATIIONYWXTSC))
						{
							if (formula == null || formula.equals(""))
								throw new IllegalArgumentException(cellName + "ȱ�ٽ�����ı��ʽ!���ܸñ��ʽ��û�н���!");
							value = sqlFormulaUtil.parseSQLFormual(formula, this.paramaMap);
						}
						
						/*����Ǽ�����������õ�Ԫ��ı����ʽ*/
						else if (relative != null && relative.equals(Config.RELATONJSX))
						{
							if (formula == null || formula.equals(""))
								throw new IllegalArgumentException(cellName + "ȱ�ٱ���ϵ���ʽ!");
							value = bjFormulaUtil.parseBJFormula(formula, this.paramaMap);
						}

						cellMap.put(cellName, value);

						// System.out.println("CellName===" + cellName + " Value===" + value);
					}
					catch (Exception e)
					{
						failedNum++;
						e.printStackTrace();
						continue;
					}
				}
			}
			if(currList.size() == 1 && currId.equals("01")) currId = "";
			
			this.writeExcel(cellMap, this.childRepId, this.versionId,currId);
			// System.out.println("**************End Create Excel*********************");
		}

		/* ����й�ʽ����,�����ɴ��� */
		return failedNum == 0 ? true : false;

	}

	/**
	 * дExcel
	 * 
	 * @param cellMap
	 * @param childRepId
	 * @param version
	 * @throws Exception
	 */
	public void writeExcel(Map cellMap, String childRepId, String version,String currId) throws Exception
	{

		String[] ARRCOLS =
		{ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
				"U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ",
				"AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY",
				"AZ" };

		HSSFWorkbook sourceWb = null;
		HSSFSheet sheet = null;
		String path = this.excelPath;
		FileInputStream inStream = new FileInputStream(path);
		POIFSFileSystem srcPOIFile = new POIFSFileSystem(inStream);
		sourceWb = new HSSFWorkbook(srcPOIFile);
		if (sourceWb.getNumberOfSheets() > 0)
		{
			sheet = sourceWb.getSheetAt(0);
		}
		inStream.close();

		HSSFRow row = null;
		HSSFCell cell = null;
		
		/*���ݱ���ȡ������������������ƫ����*/
		String key = childRepId + version;
		String value = new UtilOffset().getValueByKey(key);
		if (value == null || value.equals(""))
			value = "0";

		for (Iterator iter = sheet.rowIterator(); iter.hasNext();)
		{
			row = (HSSFRow) iter.next();
			for (short i = row.getFirstCellNum(), n = row.getLastCellNum(); i < n; i++)
			{
				cell = (HSSFCell) row.getCell(i);
				if (cell == null)
					continue;
				/* ����Ƿ��Ƿǹ�ʽ��Ԫ�� */
				if (cell.getCellType() != HSSFCell.CELL_TYPE_FORMULA)
				{
					if (cell.getCellNum() >= ARRCOLS.length)
						continue;
					String cellName = ARRCOLS[cell.getCellNum()]
							+ (row.getRowNum() + 1 + Integer.parseInt(value));
					/*����õ�Ԫ����Map����ֵ���ֵд��õ�Ԫ��*/
					if (cellMap.get(cellName) != null)
					{
						String cellValue = (String) cellMap.get(cellName);
						try
						{
							/*��ֵ�͵�Ԫ��*/
							Double d = Double.valueOf(cellValue);
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
							cell.setCellValue(d.doubleValue());
						}
						catch (Exception ex)
						{
							/*�ַ��͵�Ԫ��*/
							cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(cellValue);
						}
					}
				}
			}
		}
		/*����Ŀ¼*/
		new File(this.saveAsPath).mkdir();
		/*д�ļ�*/
		String saveAsFileName = null;
		if(!currId.equals("")) saveAsFileName = this.saveAsPath+this.childRepId + "_" + this.versionId +"_"+currId+ ".xls";
		else saveAsFileName = this.saveAsPath+this.childRepId + "_" + this.versionId + ".xls";
		FileOutputStream stream = new FileOutputStream(saveAsFileName);
		
		sourceWb.write(stream);
		stream.close();

	}

	/**
	 * ��ñ���ĵ�Ԫ��Id
	 * 
	 * @param childRepId
	 * @param version
	 * @return
	 */
	/*
	 public List getCellList(String childRepId, String version) throws Exception
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
	 String sql = "select * from M_CELL where CHILD_REP_ID = ? and VERSION_ID=?";

	 stmt = conn.prepareStatement(sql);
	 stmt.setString(1, childRepId);
	 stmt.setString(2, version);

	 rs = stmt.executeQuery();
	 resultList = new ArrayList();
	 while (rs.next())
	 {
	 MCellForm cellForm = new MCellForm();
	 cellForm.setCellName(rs.getString("CELL_NAME"));
	 cellForm.setCellId(new Integer(rs.getInt("CELL_ID")));
	 resultList.add(cellForm);
	 }
	 }
	 catch (Exception e)
	 {
	 resultList = null;
	 e.printStackTrace();
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
	 }*/

	/**
	 * �������ڻ�������������γ�ȱ��е�Id
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public String getDateId(String date) throws Exception
	{
		if (date == null || date.equals(""))
			return null;
		/* ��yyyy-mm-dd����ʽ�ı��yyyymmdd����ʽ */
		date = date.replaceAll("-", "");
		String result = null;
		Connection conn = (new FitechConnection()).getConnect();
		if (conn == null)
		{
			return null;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			stmt = conn.prepareStatement("select VD_ID from V_DATE where VD_NAME=?");
			stmt.setString(1, date);
			rs = stmt.executeQuery();
			if (!rs.next())
				result = null;
			else
			{
				result = rs.getString(1);
			}
		}
		catch (Exception e)
		{
			result = null;
			e.printStackTrace();
		}
		finally
		{
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}

		return result;
	}

	public static void main(String[] args)
	{
		try
		{

			String versionId = "0690";
			String[] reportId =
			//{ "G0100","G0101","G0102","G0104","G0105","G0106" };
			//{"G0300","G0400","G1101","G1102","G1200"};
			//{"G2100","G3100","G1402"};
			
			
			{"G0400"};
			
			String orgId = "0";

			for (int i = 0; i < reportId.length; i++)
			{
				DataFormulaToExcel test = new DataFormulaToExcel(reportId[i], versionId, orgId, "2006-06-30");
//				test.parseFormula_("");
//				test.createExcel_("C26");
//				test.parseFormula();
				test.createExcel();
			}

		}
		catch (Exception e)
		{
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
	}
	/**
	 * �������ݿ��е����й�ʽ,���ѽ�����Ĺ�ʽ���������SQL���ֶ�
	 * 
	 * @throws Exception
	 */
	public void parseFormula_(String cellname) throws Exception
	{
		// System.out.println("************Start Parse <<" + this.childRepId + ">>*************");
		
		/*ȡ�øñ�������й�ʽ��¼*/
		List resultList = this.getDBInfo_(this.childRepId,this.versionId,null,cellname);

		// System.out.println("���ݿ��й�ʽ����===" + resultList.size());
		List parseResultList = new ArrayList();
		DataRelationFormulaParseUtil util = new DataRelationFormulaParseUtil();
		for (int i = 0; i < resultList.size(); i++)
		{
			try
			{
				IDataRelationForm IDataRelationForm = (IDataRelationForm) resultList.get(i);
				/*������ʽ*/
				String relative = IDataRelationForm.getIdrRelative();

				/*��ʽ*/
				String formula = IDataRelationForm.getIdrFormula();
					// System.out.println(IDataRelationForm.getCellName()+"=="+formula);		
				String resultSQL = null;

				/*�����ҵ��ϵͳ���ɹ�ʽ,������ù�ʽ*/
				if (relative.equals(Config.RELATIIONYWXTSC))
					resultSQL = util.parseFormualStr(formula);
				/*�Ǽ������򲻽���*/
				else if (relative.equals(Config.RELATONJSX))
					resultSQL = formula;

				IDataRelationForm.setIdrSql(resultSQL);
				parseResultList.add(IDataRelationForm);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				continue;
			}
		}
		/* �������ݿ� */
		this.updateDB(resultList);

		// System.out.println("************End Parse<<" + this.childRepId + ">>*************");
	}
	/**
	 * ��� I_DATARELATION ���е�����
	 * 
	 */
	public List getDBInfo_(String childRepId,String versionId,String currId,String celname) throws Exception
	{
		if(childRepId==null || childRepId.equals("") 
				||versionId==null || versionId.equals(""))
			return null;
		
		List resultList = null;

		FitechConnection fitechConn = new FitechConnection();
		Connection conn = fitechConn.getConnect();

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			String sql = "select * from I_DATARELATION idata "
					+ "where idata.IDR_REPID=? and idata.IDR_VERSIONID=?";
			
			/*�鿴�Ƿ������������*/
			if(currId!=null && !currId.equals(""))
				sql += " and idata.VC_ID='"+currId+"'";
			if(celname!=null && !celname.equals(""))
				sql += " and idata.IDR_CELLNAME='"+celname+"'";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, childRepId);
			stmt.setString(2, versionId);
			
			rs = stmt.executeQuery();
			resultList = new ArrayList();
			while (rs.next())
			{
				IDataRelationForm IDataRelationForm = new IDataRelationForm();

				IDataRelationForm.setCellName(rs.getString("IDR_CELLNAME"));

				IDataRelationForm.setIdrFormula(rs.getString("IDR_FORMULA"));
				IDataRelationForm.setIdrSql(rs.getString("IDR_SQL"));
				IDataRelationForm.setIdrRelative(rs.getString("IDR_RELATIVE"));
				
				/*��������*/
				IDataRelationForm.setCurrId(rs.getString("VC_ID"));
				
				resultList.add(IDataRelationForm);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resultList = null;
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
	public boolean createExcel_(String cellname) throws Exception
	{
		// System.out.println("**************Start Create Excel*********************");
		/* ����ʽ���� */
		int failedNum = 0;
		
		/*ȡ�øñ����ж��ٱ���*/
		List currList = this.getCurrListByID(this.childRepId,this.versionId);
		if(currList==null || currList.size()==0)
			return false;
		
		/*ѭ�����ɸ������ֵı���*/
		for(int j=0;j<currList.size();j++)
		{
			/*ȡ�ñ���ID*/
			String currId = (String)currList.get(j);
			
			/*��øñ���һ�ֱ����µ����м�¼*/
			List list = this.getDBInfo_(this.childRepId,this.versionId,currId,cellname);

			Map cellMap = null;

			/*G3200���⴦��*/
			if (this.childRepId.equals("G3200"))
			{
				G3200_Handler G3200Handler = new G3200_Handler(list, this.paramaMap);
				cellMap = G3200Handler.getCellValueMap();
			}
			else
			{
				cellMap = new HashMap();
				SQLFormulaParseUtil sqlFormulaUtil = new SQLFormulaParseUtil();
				BJFormulaParseUtil bjFormulaUtil = new BJFormulaParseUtil();
				for (int i = 0; i < list.size(); i++)
				{
					IDataRelationForm IDataRelationForm = (IDataRelationForm) list.get(i);
					try
					{
						/*��øõ�Ԫ��Ĺ�����ʽ*/
						String relative = IDataRelationForm.getIdrRelative();

						/*��Ԫ������*/
						String cellName = IDataRelationForm.getCellName();

						/*��Ԫ��ʽ*/
						String formula = IDataRelationForm.getIdrSql();
						
						String value = null;

						/*���ҵ��ϵͳ����,������õ�Ԫ���SQL���ʽ*/
						if (relative != null && relative.equals(Config.RELATIIONYWXTSC))
						{
							if (formula == null || formula.equals(""))
								throw new IllegalArgumentException(cellName + "ȱ�ٽ�����ı��ʽ!���ܸñ��ʽ��û�н���!");
							value = sqlFormulaUtil.parseSQLFormual(formula, this.paramaMap);
						}
						
						/*����Ǽ�����������õ�Ԫ��ı����ʽ*/
						else if (relative != null && relative.equals(Config.RELATONJSX))
						{
							if (formula == null || formula.equals(""))
								throw new IllegalArgumentException(cellName + "ȱ�ٱ���ϵ���ʽ!");
							value = bjFormulaUtil.parseBJFormula(formula, this.paramaMap);
						}

						cellMap.put(cellName, value);

						// System.out.println("CellName===" + cellName + " Value===" + value);
					}
					catch (Exception e)
					{
						failedNum++;
						e.printStackTrace();
						continue;
					}
				}
			}
			//this.writeExcel(cellMap, this.childRepId, this.versionId,currId);
			// System.out.println("**************End Create Excel*********************");
		}
		/* ����й�ʽ����,�����ɴ��� */
		return failedNum == 0 ? true : false;

}}

