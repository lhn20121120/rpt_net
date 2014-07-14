package com.fitech.gznx.excel;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

import jxl.CellType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.entity.Cell;
import com.cbrc.smis.jdbc.FitechConnection;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.ReportExcelHandler;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.common.StringUtil;

public class NXReportExcelHandler {
	private static FitechException log = new FitechException(
			ReportExcelHandler.class);
	Locale LOCALE = Locale.CHINA;

	// add by gongming 2008-01-13
	private static String GET_TEMPLATE_INFO_SQL = "select template_Id,version_Id from af_report where rep_id=";
	// add by gongming 2008-01-13
	private static String GET_TEMPLATE_CELL_SQL = "select af_report.rep_id,af_cellinfo.cell_id,"
			+ "af_cellinfo.Cell_Name,af_cellinfo.data_type,"
			+ "af_cellinfo.row_num,af_cellinfo.col_num "
			+ "from af_cellinfo left join af_report on "
			+ "af_report.template_Id = af_cellinfo.template_Id "
			+ "and af_report.version_Id = af_cellinfo.Version_Id "
			+ "where af_report.rep_id=";
	// add by gongming 2008-01-13
	private static PropertyResourceBundle resourceBundle = (PropertyResourceBundle) PropertyResourceBundle
			.getBundle("com/cbrc/smis/excel/OffsetResources_zh_CN");
	/* ����ID */
	private Integer repInId;
	/* ����·�� */
	private String excelFilePath;
	/* �����Ӧģ��ı�� */
	private String childRepId;
	/* �������ģ��İ汾�� */
	private String versionId;
	/* �������� */
	public String title = "";
	/* ����ע���� */
	public String subTitle = "";
	/* ��Ԫ���б� */
	private List cellList = null;
	/* �������ͣ�1Ϊ���౨��2Ϊ���б���3Ϊ�������� */
	private String templateType = null;
	/*�������ݸ�ʽ���ַ���,*/
	public String formatStr = "%1$."+com.cbrc.smis.common.Config.DOUBLEPERCISION+"f";
	
	public FitechMessages messages=null;
	/**
	 * ����Excelģ���ӦPDFģ�����ʼ�е�ƫ����
	 */
	private String REPORTFILE = "com/cbrc/smis/excel/OffsetResources";

	public NXReportExcelHandler() {
	}

	/**
	 * ���캯��
	 * 
	 * @param reptId
	 */
	public NXReportExcelHandler(Integer repInId, String excelFilePath,FitechMessages messager, String templateType)
			throws Exception {
		messages = messager;
		if (repInId == null || repInId.equals(new Long(0))){
			messages.add("�����������");
			throw new IllegalArgumentException("�����������");
		}else {

			this.repInId = repInId;
			this.excelFilePath = excelFilePath;
			this.templateType = templateType;

			FitechConnection connFactory = null;
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;

			try {
				connFactory = new FitechConnection();
				conn = connFactory.getConn();
				conn.setAutoCommit(false);
				stmt = conn.createStatement();

				rs = stmt.executeQuery(GET_TEMPLATE_INFO_SQL + repInId);

				if (rs.next()) {
					this.childRepId = rs.getString(1);
					this.versionId = rs.getString(2);
				} else {
					messages.add("û�и�ģ����Ϣ���ڣ�");
					throw new IllegalArgumentException("û�и�ģ����Ϣ���ڣ�");
				}

			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			}
		}

		try {
			if (excelFilePath != null) {
				cellList = this.getDataFromExcels(repInId.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ����excel���ݽ����ݿ�
	 * 
	 * @param repType
	 *            ��������
	 * @return
	 * @throws Exception
	 */
	public boolean copyExcelToDB(String repType) throws Exception {
		boolean result = false;

		if (repType == null)
			return result;

		Connection conn = null;
		FitechConnection connFactory = null;
		Statement stmt = null;

		try {
			connFactory = new FitechConnection();

			if (cellList != null) {
				conn = connFactory.getConn();
				conn.setAutoCommit(false);
				stmt = conn.createStatement();

				// ɾ��ԭ������
				if (repType.equals(Config.PBOC_REPORT)) {
					stmt.addBatch("delete from af_pbocreportdata where rep_id=" + this.repInId);
				} else if (repType.equals(Config.OTHER_REPORT)) {
					stmt.addBatch("delete from af_otherreportdata where rep_id=" + this.repInId);
				}

				stmt.executeBatch();
				conn.commit();
				
				
				
				// ����������
				if (repType.equals(Config.PBOC_REPORT)){
					for (int i = 0; i < cellList.size(); i++) {
						Cell cell = (Cell) cellList.get(i);
						
						//����repInId��ѯ���ݺۼ��� ��ѯ��template_ID ,version_ID ��cell_name
						//����template_ID,version_ID ��cell_name��ѯaf_cellinfo�� �ó�cell_id
						//����repInId��cell_name��ѯ����Ԫ���޸������ܺ�
						//����ǰֵ���޸������ܺ͵�ֵ���
						//��ѯ���ݺۼ�
						String sumFormu = "";
						if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("oracle")){
							sumFormu = " sum(t.change_data) ";
						}
						if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("sqlserver")){
							sumFormu = " sum(convert(float,t.change_data)) ";
						}
						String insertSQL = "";
						double finalData = 0;
						if(false&&com.cbrc.smis.common.Config.ISADDTRACE){
							/***
							 * �������ݺۼ��������� -------------------------------------------------------------------��ʼ
							 * ���ܿ��������Ը�
							 */
							String henjiSQL = "select "+sumFormu+" from af_data_trace t " +
							  "left join af_report r on t.rep_in_id=r.rep_id " +
							  "left join af_cellinfo a " +
							  "on r.template_id=a.template_id and r.version_id=a.version_id and a.cell_name=t.cell_name " +
							  "where t.cell_name='"+cell.getCellName()+"' and t.status=0";
							//ȡ������ֵ
							ResultSet rs = stmt.executeQuery(henjiSQL);
							int sumData =0;
							if(rs.next()){
								sumData= rs.getObject(1)==null?0:rs.getInt(1);
								System.out.println(sumData);
							}
							finalData = Double.valueOf(cell.getCellValue())+sumData;
							insertSQL = "insert into af_pbocreportdata (Rep_ID,Cell_ID,Cell_Data) values("
								+ cell.getRepInId()
								+ ","
								+ cell.getCellId()
								+ ",'"
								+ String.format("%1$.2f",finalData) + "')";
						}else{
						   insertSQL = "insert into af_pbocreportdata (Rep_ID,Cell_ID,Cell_Data) values("
								+ cell.getRepInId()
								+ ","
								+ cell.getCellId()
								+ ",'"
								+ cell.getCellValue() + "')";
						}
//						log.println(insertSQL + ";");
						stmt.addBatch(insertSQL);
					}
				}else if(repType.equals(Config.OTHER_REPORT)){
					for (int i = 0; i < cellList.size(); i++) {
						
						Cell cell = (Cell) cellList.get(i);
						
						//����repInId��ѯ���ݺۼ��� ��ѯ��template_ID ,version_ID ��cell_name
						//����template_ID,version_ID ��cell_name��ѯaf_cellinfo�� �ó�cell_id
						//����repInId��cell_name��ѯ����Ԫ���޸������ܺ�
						//����ǰֵ���޸������ܺ͵�ֵ���
						//��ѯ���ݺۼ�
						String sumFormu = "";
						if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("oracle")){
							sumFormu = " sum(t.change_data) ";
						}
						if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("sqlserver")){
							sumFormu = " sum(convert(float,t.change_data)) ";
						}
						double finalData = 0;
						
						/***
						 * �����������ݺۼ��������� -------------------------------------------------------------------��ʼ
						 * ���ܿ��������Ը�
						 */
//						String henjiSQL = "select "+sumFormu+" from af_data_trace t " +
//						  "left join af_report r on t.rep_in_id=r.rep_id " +
//						  "left join af_cellinfo a " +
//						  "on r.template_id=a.template_id and r.version_id=a.version_id and a.cell_name=t.cell_name " +
//						  "where t.cell_name='"+cell.getCellName()+"' and t.status=0";
//						//ȡ������ֵ
//						ResultSet rs = stmt.executeQuery(henjiSQL);
//						int sumData =0;
//						if(rs.next()){
//							sumData= rs.getObject(1)==null?0:rs.getInt(1);
//							System.out.println(sumData);
//						}
//						
//						if(com.cbrc.smis.common.Config.ISADDTRACE)
//							finalData = Double.valueOf(cell.getCellValue())+sumData;
//						else
						/***
						 * �����������ݺۼ��������� -------------------------------------------------------------------����
						 * ���ܿ��������Ը�
						 */
							finalData = Double.valueOf(cell.getCellValue());
						
						String insertSQL = "insert into af_otherreportdata (Rep_ID,Cell_ID,Cell_Data) values("
								+ cell.getRepInId()
								+ ","
								+ cell.getCellId()
								+ ",'"
								+ finalData + "')";

						stmt.addBatch(insertSQL);
					}
				}
				
				stmt.executeBatch();
				conn.commit();
				result = true;
			}

		} catch (Exception e) {
			result = false;
			conn.rollback();
			log.printStackTrace(e);
		} finally {
			if (stmt != null)
				stmt.close();
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	/**
	 * ���ģ�浥Ԫ����Ϣ
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public List getCellList() throws Exception {
		FitechConnection connFactory = null;
		Connection conn = null;

		List celllist = new ArrayList();
		Statement stmt = null;
		ResultSet rst = null;

		try {
			connFactory = new FitechConnection();
			conn = connFactory.getConn();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			rst = stmt.executeQuery(GET_TEMPLATE_CELL_SQL + repInId);
			while (rst.next()) {
				Cell cell = new Cell();
				cell.setCellId(rst.getInt("cell_id".toUpperCase()));
				cell.setCellName(rst.getString("cell_name".toUpperCase()));
				cell.setDataType(rst.getInt("data_type".toUpperCase()));
				cell.setRepInId(rst.getInt("rep_id".toUpperCase()));
				cell.setColId(rst.getString("col_name".toUpperCase()));
				cell.setRowId(rst.getInt("row_name".toUpperCase()));
				celllist.add(cell);
			}
		} catch (Exception e) {
			messages.add("û�и�ģ����Ϣ���ڣ�");
			log.println("�����ļ�·��������!");
			log.printStackTrace(e);
			return null;
		} finally {
			if (rst != null)
				rst.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}
		return celllist;
	}

	/**
	 * ��õ�Ԫ���ֵ
	 * 
	 * @param cell
	 * @return
	 */
	public String getCellasStr(jxl.Cell cell) {
		if (cell == null)
			return "";
		String result = "";
		if (cell == null)
			return result;
		String contents = cell.getContents();
		CellType cellType = cell.getType();

		if (cellType.equals(CellType.NUMBER)
				|| cellType.equals(CellType.NUMBER_FORMULA)) {
			boolean isPresentNumber = false;
			/* ǧλ�ָ�����ݴ��� */
			if (contents.indexOf(",") > -1)
				contents = contents.replaceAll(",", "");
			/* �ٷ������ݴ��� */
			if (contents.indexOf("%") > -1
					&& contents.indexOf("%") == contents.length() - 1) {
				isPresentNumber = true;
				contents = parsePresentToDouble(contents);
				// contents = contents.substring(0, contents.indexOf("%"));
				/*
				 * Double value = new Double(contents.substring(0,
				 * contents.indexOf("%"))); contents =
				 * String.valueOf(FitechUtil.doubleDivide(value,new
				 * Double(100.0)));
				 */
			}
			/* ��ѧ���������ݴ��� */
			if (contents.indexOf('E') > -1 || contents.indexOf('e') > -1) {
				try {
					// ��������ָ�ʽ�����ʽ��ΪС�������λ
					if (!isPresentNumber)
						contents = new java.text.DecimalFormat("##0.00")
								.format(Double.parseDouble(contents))
								.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return contents;
	}

	/**
	 * ���ģ�浥Ԫ����Ϣ
	 * 
	 * @author gongming
	 * @date 2008-01-13
	 * @return List
	 * @throws Exception
	 * 
	 */
	public List getCellList(String repInId) throws Exception {
		FitechConnection connFactory = null;
		Connection conn = null;

		List celllist = new ArrayList();
		Statement stmt = null;
		ResultSet rst = null;

		try {
			connFactory = new FitechConnection();
			conn = connFactory.getConn();
			conn.setAutoCommit(false);

			stmt = conn.createStatement();
			rst = stmt.executeQuery(GET_TEMPLATE_CELL_SQL + repInId);
			while (rst.next()) {
				Cell cell = new Cell();
				cell.setCellId(rst.getInt("cell_id"));
				cell.setCellName(rst.getString("cell_name"));
				cell.setDataType(rst.getInt("data_type"));
				cell.setRepInId(rst.getInt("rep_id"));
				cell.setColId(rst.getString("col_num"));
				cell.setRowId(rst.getInt("row_num"));
				celllist.add(cell);
			}

		} catch (Exception e) {
			log.println("�����ļ�·��������!");
			log.printStackTrace(e);
			return null;
		} finally {
			if (rst != null)
				rst.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}
		return celllist;
	}

	/**
	 * ����Excel�������������
	 * 
	 * @author gongming
	 * @date 2008-01-13
	 * @return
	 * 
	 */
	private List getDataFromExcels(String repInId) throws Exception {

		List result = new ArrayList();

		// ��ȡ��Ԫ��list
		List cellList = getCellList(repInId);

		FileInputStream inStream = null;

		try {
			inStream = new FileInputStream(this.excelFilePath);
			File inputWorkbook = new File(excelFilePath);
			if (!inputWorkbook.exists()) {
				return null;
			}
			HSSFWorkbook workbook = new HSSFWorkbook(inStream);
			HSSFSheet sheet = workbook.getSheetAt(0); // ֻȡ��һ��sheet
			// ��ȡ������
			HSSFRow rowTitle = sheet.getRow(0); // Ĭ��ȡ��һ�У����⣩

			if (rowTitle != null && rowTitle.cellIterator().hasNext()
					&& null != rowTitle.getCell((short) 0).getStringCellValue()) {

				title = rowTitle.getCell((short) 0).getStringCellValue()
						.replaceAll(" ", "");

				if (title == null || title.equals("")) {
					rowTitle = sheet.getRow(1); // �����һ��û�У�ȡ�ڶ���
					title = rowTitle.getCell((short) 1).getStringCellValue()
							.replaceAll(" ", "");
				}

			} else {

				rowTitle = sheet.getRow(1); // �����һ��û�У�ȡ�ڶ���

				if (rowTitle != null
						&& rowTitle.cellIterator().hasNext()
						&& null != rowTitle.getCell((short) 1)
								.getStringCellValue()) {
					title = rowTitle.getCell((short) 1).getStringCellValue()
							.replaceAll(" ", "");
				}

			}

			// ��ȡ������
			int subTitleCol = 0;
			int subTitleRow = 2;
			HSSFRow row1 = sheet.getRow(subTitleRow);

			if (null != row1.getCell((short) subTitleCol)
					&& row1.getCell((short) subTitleCol).getCellType() == HSSFCell.CELL_TYPE_STRING) {
				subTitle = row1.getCell((short) subTitleCol)
						.getStringCellValue().trim();
			}

			if (sheet == null)
				return null;

			int size = cellList.size();

			for (int i = 0; i < size; i++) {

				Cell cell = (Cell) cellList.get(i);
				if (cell.getCellName() != null) {

					int row = cell.getRowId() - 1; // poi��0��ʼ��
					short col = (short) this.convertColStringToNum(cell.getColId());

					HSSFCell excelcell = null;

					try {
						
						excelcell = sheet.getRow(row).getCell(col);
						
						String cellValue = this.getCellasString(excelcell);
						
						if (!StringUtil.isEmpty(cellValue)) {
							if(templateType!=null&&templateType.equals("2")){//20140311:��������ݽ��и�ʽ��
								cellValue = String.format(formatStr, Double.valueOf(cellValue.trim()));
							}
							cell.setCellValue(cellValue.trim());
						} else {
							continue;
						}

						result.add(cell);

					} catch (Exception ex) {
					}
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add("��ȡ�����ļ����ִ���!");
			result = null;
		} finally {
			if (inStream != null)
				inStream.close();
		}
		return result;
	}

	/**
	 * ��õ�Ԫ���ֵ
	 * 
	 * @author gongming
	 * @date 2008-01-13
	 * @param cell
	 * @return
	 */
	private String getCellasString(HSSFCell cell) {
		if (cell == null)
			return "";
		String result = "";
		if (cell == null)
			return result;
		// String contents = cell.getContents();
		// CellType cellType = cell.getType();
		
		String contents = null;
				
		if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA
				|| cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			try {
				contents = String.valueOf(cell.getNumericCellValue());
				
				if(contents.trim().equals("NaN"))
					contents = "0.00";
				
				if ("0.0".equals(contents))
					contents = "0.00";
				
			} catch (Exception e) {
				contents = "0.00";
			}
			boolean isPresentNumber = false;
			/* ǧλ�ָ�����ݴ��� */
			if (contents.indexOf(",") > -1)
				contents = contents.replaceAll(",", "");
			/* �ٷ������ݴ��� */
			if (contents.indexOf("%") > -1
					&& contents.indexOf("%") == contents.length() - 1) {
				isPresentNumber = true;
				contents = parsePresentToDouble(contents);
				// contents = contents.substring(0, contents.indexOf("%"));
				/*
				 * Double value = new Double(contents.substring(0,
				 * contents.indexOf("%"))); contents =
				 * String.valueOf(FitechUtil.doubleDivide(value,new
				 * Double(100.0)));
				 */
			}
			/* ��ѧ���������ݴ��� */
			if (contents.indexOf('E') > -1 || contents.indexOf('e') > -1) {
				try {
					// ��������ָ�ʽ�����ʽ��ΪС�������λ
					if (!isPresentNumber)
						contents = new java.text.DecimalFormat("##0.00")
								.format(Double.parseDouble(contents))
								.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				contents = cell.getStringCellValue();
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			contents = "";
		}

		return contents;
	}

	/**
	 * ���ٷ���ת��λС��
	 * 
	 * @param presentNumber
	 * @return
	 * 
	 */
	private String parsePresentToDouble(String presentNumber) {
		String contents = presentNumber
				.substring(0, presentNumber.indexOf("%"));
		String sign = "";
		if (contents.substring(0, 1).equals("+")
				|| contents.substring(0, 1).equals("-")) {
			sign = String.valueOf(contents.charAt(0));
			contents = contents.substring(1);
		}
		int dotIndex = contents.indexOf(".");

		if (dotIndex == -1)
			contents = "0." + contents;
		else if (dotIndex == 0)
			contents = "0.00" + contents.substring(1);
		else if (dotIndex == 1)
			contents = "0.0" + contents.substring(0, 1) + contents.substring(2);
		else if (dotIndex == 2)
			contents = "0." + contents.substring(0, 2) + contents.substring(3);
		else {
			StringBuffer contentsStr = new StringBuffer(contents);
			contentsStr.insert(dotIndex - 2, ".");
			contentsStr.deleteCharAt(dotIndex + 1);
			contents = contentsStr.toString();
		}

		return sign + contents;
	}

	/**
	 * ���к�ת��Ϊ����
	 * 
	 * @param ref
	 * @return
	 * 
	 */
	public int convertColStringToNum(String ref) {
		int retval = 0;
		int pos = 0;
		for (int k = ref.length() - 1; k > -1; k--) {
			char thechar = ref.charAt(k);
			if (pos == 0)
				retval += Character.getNumericValue(thechar) - 9;
			else
				retval += (Character.getNumericValue(thechar) - 9) * (pos * 26);
			pos++;
		}

		return retval - 1;
	}

	/**
	 * ��ȡExcelģ���ӦPDFģ�����ʼ�е�ƫ����
	 * 
	 * @author jcm
	 * @return int ƫ����
	 */
	public int getOffset() {
		int offset = 0;

		if (this.childRepId == null || this.versionId == null)
			return offset;

		String _offset = getValue(REPORTFILE, this.childRepId.trim()
				+ this.versionId.trim());
		if (_offset != null)
			try {
				offset = Integer.parseInt(_offset.trim());
			} catch (Exception ex) {
				offset = 0;
			}
		return offset;
	}

	/**
	 * ����Դ�ļ��У�����������ȡ��ֵ
	 * 
	 * @param resourcesFile
	 *            String ��Դ�ļ�
	 * @param key
	 *            String ����
	 * @return String ����ֵ
	 */
	private String getValue(String resourcesFile, String key) {
		if (resourcesFile == null || key == null)
			return null;

		MessageResources resources = MessageResources
				.getMessageResources(resourcesFile);
		String value = resources.getMessage(this.LOCALE, key);

		return value == null ? null : value.trim();
	}

	/**
	 * ��ȡExcelģ���ӦPDFģ�����ʼ�е�ƫ����
	 * 
	 * @param childRepId
	 *            String ������
	 * @return int
	 */
	private int getOffsetRow(String childRepId, String versionId) {
		int offset = 0;

		if (childRepId == null || versionId == null)
			return offset;

		String _offset = getValue(childRepId.trim() + versionId.trim());

		if (_offset != null) {
			try {
				offset = Integer.parseInt(_offset.trim());
			} catch (Exception e) {
				offset = 0;
			}
		}

		return offset;
	}

	/**
	 * ����Դ�ļ��У�����������ȡ��ֵ
	 * 
	 * @author gongming
	 * @date 2008-01-13
	 * @param key
	 *            String ����
	 * @return String ����ֵ
	 */
	private String getValue(String key) {
		if (key == null)
			return null;

		String value = null;
		try {
			value = resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			value = null;
		}
		return value == null ? null : value;
	}

	public FitechMessages getMessages() {
		return messages;
	}

	public void setMessages(FitechMessages messages) {
		this.messages = messages;
	}
}
