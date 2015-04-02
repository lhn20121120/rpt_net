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
 * 解析公式成Excel工具类
 * @author Yao
 *
 */
public class DataFormulaToExcel
{
	/**
	 * 报表Id
	 */
	private String childRepId = null;

	/**
	 * 报表版本号
	 */
	private String versionId = null;

	/**
	 * 所需参数Map
	 */
	private Map paramaMap = null;

	/**
	 * 机构ID
	 */
	private String orgId = null;

	/**
	 * 模板路径
	 */
	private String excelPath = null;

	/**
	 * 另存为的路径
	 */
	private String saveAsPath = null;

	/**
	 * 总行ID
	 */
	private final String HEAD_ORG_ID = "331010000";

	/**
	 * 构造函数初始化需要的参数
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
			throw new IllegalArgumentException("参数错误!无法生成该报表!");
		}

		this.paramaMap = new HashMap();

		this.childRepId = childRepId;
		this.versionId = versionId;

		/*不分机构生成*/
		if (orgId.equals("0"))
		{
			this.orgId = this.HEAD_ORG_ID;
			/* 机构参数为所有的机构的总和 */
			this.paramaMap.put("机构ID", "select ORG_ID from V_ORG");
		}
		/*分机构生成*/
		else
		{
			this.orgId = orgId.trim();
			/* 机构参数 为单个机构的Id*/
			this.paramaMap.put("机构ID", "'" + this.orgId + "'");
		}

		/* 报告日参数 */
		this.paramaMap.put("报告日", this.getDateId(reportDate));
		/*Excel模板路径*/
		this.excelPath = Config.RAQ_TEMPLATE_PATH + "Reports" + Config.FILESEPARATOR + "templates"
				+ Config.FILESEPARATOR + this.childRepId + "_" + this.versionId + ".xls";

	//	 this.saveAsPath="D:";
	//	 this.excelPath ="D:\\template"+ Config.FILESEPARATOR + this.childRepId + "_" + this.versionId + ".xls";
		
		String year = reportDate.split("-")[0];
		String term = reportDate.split("-")[1];
		if(term != null && term.length() == 2 && term.indexOf("0") == 0){
			term = term.substring(1);
		}
		
		/*另存为的路径*/
		this.saveAsPath = Config.WEBROOTPATH + "Reports" + Config.FILESEPARATOR + "releaseTemplates"
				+ Config.FILESEPARATOR + year + "_" + term;
		
		new File(this.saveAsPath).mkdir();
		this.saveAsPath = this.saveAsPath + Config.FILESEPARATOR + this.orgId + Config.FILESEPARATOR;

	}

	/**
	 * 解析数据库中的所有公式,并把解析后的公式更新至存放SQL的字段
	 * 
	 * @throws Exception
	 */
	public void parseFormula() throws Exception
	{
		// System.out.println("************Start Parse <<" + this.childRepId + ">>*************");
		
		/*取得该报表的所有公式记录*/
		List resultList = this.getDBInfo(this.childRepId,this.versionId,null);

		// System.out.println("数据库中公式数量===" + resultList.size());
		List parseResultList = new ArrayList();
		DataRelationFormulaParseUtil util = new DataRelationFormulaParseUtil();
		for (int i = 0; i < resultList.size(); i++)
		{
			try
			{
				IDataRelationForm IDataRelationForm = (IDataRelationForm) resultList.get(i);
				/*关联方式*/
				String relative = IDataRelationForm.getIdrRelative();

				/*公式*/
				String formula = IDataRelationForm.getIdrFormula();
							
				String resultSQL = null;

				/*如果是业务系统生成公式,则解析该公式*/
				if (relative.equals(Config.RELATIIONYWXTSC))
					resultSQL = util.parseFormualStr(formula);
				/*是计算项则不解析*/
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
		/* 更新数据库 */
		this.updateDB(resultList);

		// System.out.println("************End Parse<<" + this.childRepId + ">>*************");
	}

	/**
	 * 获得 I_DATARELATION 表中的数据
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
			
			/*查看是否包含币种条件*/
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
				
				/*新增币种*/
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
	 * 根据报表获得它包含多少种币种
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
	 * 更新数据库SQL字段
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
					/*币种*/
					+ "and idata.VC_ID=?";

			stmt = conn.prepareStatement(sql);

			for (int i = 0; i < list.size(); i++)
			{
				IDataRelationForm IDataRelationForm = (IDataRelationForm) list.get(i);
				stmt.setString(1, IDataRelationForm.getIdrSql());
				stmt.setString(2, this.childRepId);
				stmt.setString(3, this.versionId);
				stmt.setString(4, IDataRelationForm.getCellName());
				/*币种*/
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
	 * 解析SQL并写入Excel
	 * 
	 * @throws Exception
	 */
	public boolean createExcel() throws Exception
	{
		// System.out.println("**************Start Create Excel*********************");
		/* 错误公式数量 */
		int failedNum = 0;
		
		/*取得该报表有多少币种*/
		List currList = this.getCurrListByID(this.childRepId,this.versionId);
		if(currList==null || currList.size()==0)
			return false;
		
		/*循环生成各个币种的报表*/
		for(int j=0;j<currList.size();j++)
		{
			/*取得币种ID*/
			String currId = (String)currList.get(j);
			
			/*获得该报表一种币种下的所有记录*/
			List list = this.getDBInfo(this.childRepId,this.versionId,currId);

			Map cellMap = null;

			/*G3200特殊处理*/
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
						/*获得该单元格的关联方式*/
						String relative = IDataRelationForm.getIdrRelative();

						/*单元格名称*/
						String cellName = IDataRelationForm.getCellName();

						/*单元格公式*/
						String formula = IDataRelationForm.getIdrSql();
						
						String value = null;

						/*如果业务系统生成,则解析该单元格的SQL表达式*/
						if (relative != null && relative.equals(Config.RELATIIONYWXTSC))
						{
							if (formula == null || formula.equals(""))
								throw new IllegalArgumentException(cellName + "缺少解析后的表达式!可能该表达式还没有解析!");
							value = sqlFormulaUtil.parseSQLFormual(formula, this.paramaMap);
						}
						
						/*如果是计算项则解析该单元格的表间表达式*/
						else if (relative != null && relative.equals(Config.RELATONJSX))
						{
							if (formula == null || formula.equals(""))
								throw new IllegalArgumentException(cellName + "缺少表间关系表达式!");
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

		/* 如果有公式错误,则生成错误 */
		return failedNum == 0 ? true : false;

	}

	/**
	 * 写Excel
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
		
		/*根据报表取得它相对于主报表的行偏移量*/
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
				/* 检查是否是非公式单元格 */
				if (cell.getCellType() != HSSFCell.CELL_TYPE_FORMULA)
				{
					if (cell.getCellNum() >= ARRCOLS.length)
						continue;
					String cellName = ARRCOLS[cell.getCellNum()]
							+ (row.getRowNum() + 1 + Integer.parseInt(value));
					/*如果该单元格在Map中有值则把值写入该单元格*/
					if (cellMap.get(cellName) != null)
					{
						String cellValue = (String) cellMap.get(cellName);
						try
						{
							/*数值型单元格*/
							Double d = Double.valueOf(cellValue);
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
							cell.setCellValue(d.doubleValue());
						}
						catch (Exception ex)
						{
							/*字符型单元格*/
							cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(cellValue);
						}
					}
				}
			}
		}
		/*创建目录*/
		new File(this.saveAsPath).mkdir();
		/*写文件*/
		String saveAsFileName = null;
		if(!currId.equals("")) saveAsFileName = this.saveAsPath+this.childRepId + "_" + this.versionId +"_"+currId+ ".xls";
		else saveAsFileName = this.saveAsPath+this.childRepId + "_" + this.versionId + ".xls";
		FileOutputStream stream = new FileOutputStream(saveAsFileName);
		
		sourceWb.write(stream);
		stream.close();

	}

	/**
	 * 获得报表的单元格Id
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
	 * 根据日期获得它存放在日期纬度表中的Id
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public String getDateId(String date) throws Exception
	{
		if (date == null || date.equals(""))
			return null;
		/* 把yyyy-mm-dd的形式改变成yyyymmdd的形式 */
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	/**
	 * 解析数据库中的所有公式,并把解析后的公式更新至存放SQL的字段
	 * 
	 * @throws Exception
	 */
	public void parseFormula_(String cellname) throws Exception
	{
		// System.out.println("************Start Parse <<" + this.childRepId + ">>*************");
		
		/*取得该报表的所有公式记录*/
		List resultList = this.getDBInfo_(this.childRepId,this.versionId,null,cellname);

		// System.out.println("数据库中公式数量===" + resultList.size());
		List parseResultList = new ArrayList();
		DataRelationFormulaParseUtil util = new DataRelationFormulaParseUtil();
		for (int i = 0; i < resultList.size(); i++)
		{
			try
			{
				IDataRelationForm IDataRelationForm = (IDataRelationForm) resultList.get(i);
				/*关联方式*/
				String relative = IDataRelationForm.getIdrRelative();

				/*公式*/
				String formula = IDataRelationForm.getIdrFormula();
					// System.out.println(IDataRelationForm.getCellName()+"=="+formula);		
				String resultSQL = null;

				/*如果是业务系统生成公式,则解析该公式*/
				if (relative.equals(Config.RELATIIONYWXTSC))
					resultSQL = util.parseFormualStr(formula);
				/*是计算项则不解析*/
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
		/* 更新数据库 */
		this.updateDB(resultList);

		// System.out.println("************End Parse<<" + this.childRepId + ">>*************");
	}
	/**
	 * 获得 I_DATARELATION 表中的数据
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
			
			/*查看是否包含币种条件*/
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
				
				/*新增币种*/
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
		/* 错误公式数量 */
		int failedNum = 0;
		
		/*取得该报表有多少币种*/
		List currList = this.getCurrListByID(this.childRepId,this.versionId);
		if(currList==null || currList.size()==0)
			return false;
		
		/*循环生成各个币种的报表*/
		for(int j=0;j<currList.size();j++)
		{
			/*取得币种ID*/
			String currId = (String)currList.get(j);
			
			/*获得该报表一种币种下的所有记录*/
			List list = this.getDBInfo_(this.childRepId,this.versionId,currId,cellname);

			Map cellMap = null;

			/*G3200特殊处理*/
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
						/*获得该单元格的关联方式*/
						String relative = IDataRelationForm.getIdrRelative();

						/*单元格名称*/
						String cellName = IDataRelationForm.getCellName();

						/*单元格公式*/
						String formula = IDataRelationForm.getIdrSql();
						
						String value = null;

						/*如果业务系统生成,则解析该单元格的SQL表达式*/
						if (relative != null && relative.equals(Config.RELATIIONYWXTSC))
						{
							if (formula == null || formula.equals(""))
								throw new IllegalArgumentException(cellName + "缺少解析后的表达式!可能该表达式还没有解析!");
							value = sqlFormulaUtil.parseSQLFormual(formula, this.paramaMap);
						}
						
						/*如果是计算项则解析该单元格的表间表达式*/
						else if (relative != null && relative.equals(Config.RELATONJSX))
						{
							if (formula == null || formula.equals(""))
								throw new IllegalArgumentException(cellName + "缺少表间关系表达式!");
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
		/* 如果有公式错误,则生成错误 */
		return failedNum == 0 ? true : false;

}}

