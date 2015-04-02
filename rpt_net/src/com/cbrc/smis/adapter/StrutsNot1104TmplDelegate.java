package com.cbrc.smis.adapter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.hibernate.Session;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.struts.upload.FormFile;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.MExcelChildReportForm;
import com.cbrc.smis.form.ReportDataForm;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MChildReportPK;
import com.cbrc.smis.hibernate.MCurUnit;
import com.cbrc.smis.hibernate.MMainRep;
import com.cbrc.smis.hibernate.MRepType;
import com.cbrc.smis.hibernate.ReportData;
import com.cbrc.smis.hibernate.ReportDataPK;
import com.cbrc.smis.util.DataArea;
import com.cbrc.smis.util.ExcelUtil;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.common.ExcelParser;
import com.fitech.net.common.StringTool;

/**
 * 
 * @author wdw
 * 
 */
public class StrutsNot1104TmplDelegate
{

	/** 保存模板到指定路径 */
	public static boolean uploadNot1104Template(MExcelChildReportForm mExcelChildReportForm, FormFile tmplFile)
	{
		boolean flag = false;

		if (tmplFile != null && tmplFile.getFileSize() > 0)
		{
			BufferedOutputStream bos = null;
			try
			{
				InputStream in = tmplFile.getInputStream();
				String fileName = com.fitech.net.config.Config.getTemplateFolderRealPath() + File.separator
						+ mExcelChildReportForm.getChildRepId() + "_" + mExcelChildReportForm.getVersionId() + ".xls";
				File file = new File(fileName);
				bos = new BufferedOutputStream(new FileOutputStream(file));

				byte[] line = new byte[1024];
				while (in.read(line) != -1)
				{
					bos.write(line);
				}
				bos.flush();
				flag = true;
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				if (bos != null)
					try
					{
						bos.close();
					}
					catch (IOException ioe)
					{
						ioe.printStackTrace();
					}
			}
		}

		return flag;

	}

	/**
	 * 模板入库 要insert四张表，按先后顺序为MMainRep,MChildReport,ReportData,MCell
	 * 为保持数据完整性，如果insert任何一张表失败，即回滚并退出程序
	 */
	public static boolean insertTemplate(MExcelChildReportForm mExcelChildReportForm, File tmptFile, DataArea dataArea)
	{
		boolean flag = false;

		DBConn conn = new DBConn();
		Session session = conn.beginTransaction();

		try
		{
			/** insertMMainRep表 */
			MRepType mRepType = (MRepType) session.load(MRepType.class, mExcelChildReportForm.getRepTypeId());
			MCurUnit mCurUnit = StrutsMCurUnitDelegate.getMCurUnit(mExcelChildReportForm.getReportCurUnit());
			MMainRep mMainRep = new MMainRep();
			mMainRep.setRepCnName(mExcelChildReportForm.getReportName());
			mMainRep.setMCurUnit(mCurUnit);
			mMainRep.setMRepType(mRepType);
			session.save(mMainRep);

			/** insertMMainRep表如果成功则继续insertMChildReport表，否则返回false */
			if (mMainRep.getRepId() == null)
			{
				return flag;
			}
			MChildReport mChildReport = new MChildReport();
			mChildReport.setComp_id(new MChildReportPK(mExcelChildReportForm.getChildRepId(), mExcelChildReportForm
					.getVersionId()));
			mChildReport.setIsPublic(new Integer(0));
			mChildReport.setReportName(mExcelChildReportForm.getReportName());
			mChildReport.setMMainRep(mMainRep);
			mChildReport.setMCurUnit(mMainRep.getMCurUnit());
			mChildReport.setTemplateType("excel");
			mChildReport.setReportStyle(com.cbrc.smis.common.Config.REPORT_STYLE_DD);
			session.save(mChildReport);

			/** insertMCell表如果成功则提交所有操作，并把flag置为true */
			/** 目前只支持列号在A-Z之间 */
			FileInputStream inStream = new FileInputStream(tmptFile);
			if (dataArea.getStartCol().length() == 1 && dataArea.getEndCol().length() == 1)
			{
				MCell mCell = null;
				HSSFRow row = null;
				HSSFCell cell = null;
				/** 获取HSSFSheet */
				HSSFSheet sheet = ExcelParser.getNeedSheet(mExcelChildReportForm, inStream, 0);
				if (sheet != null)
				{
					for (int r = Integer.parseInt(dataArea.getStartRow()); r <= Integer.parseInt(dataArea.getEndRow()); r++)
					{
						/** 获取HSSFRow对象 */
						row = sheet.getRow(r - 1);
						for (char c = dataArea.getStartCol().charAt(0); c <= dataArea.getEndCol().charAt(0); c++)
						{
							/** 获取HSSFCell对象 */
							if (row == null)
								continue;
							cell = row
									.getCell(Short.parseShort(String.valueOf(StringTool.colIndex(String.valueOf(c)))));

							/** 如果单元格非空，则入库 */
							if (cell == null)
								continue;
							mCell = new MCell();
							mCell.setCellName(String.valueOf(c) + String.valueOf(r));
							mCell.setDataType(new Integer(cell.getCellType()));
							mCell.setRowId(new Integer(r));
							mCell.setColId(String.valueOf(c));
							mCell.setMChildReport(mChildReport);
							session.save(mCell);
						}
					}
				}
			}
			session.flush();
			inStream.close();

			/** insertReportData表 */
			inStream = new FileInputStream(tmptFile);
			ReportDataForm reportDataForm = new ReportDataForm();
			reportDataForm.setChildRepId(mExcelChildReportForm.getChildRepId());
			reportDataForm.setVersionId(mExcelChildReportForm.getVersionId());
			reportDataForm.setPdfIN(inStream);
			if (StrutsReportDataDelegate.insert(reportDataForm) == false)
				return false;

			inStream.close();
			flag = true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if (conn != null)
				conn.endTransaction(flag);
		}
		return flag;

	}

	/**
	 * 已使用hibernate  卞以刚 2011-12-22
	 * 分支模版入库 （Excel）
	 * 
	 * @param mChildReportForm
	 * @return
	 * 
	 */
	public static boolean insertFZTemplate(MChildReportForm mChildReportForm,String fileName) throws Exception
	{
		boolean result = false;
		DBConn conn = new DBConn();
		Session session = conn.beginTransaction();

		try
		{
			/** 生成临时文件，方便后面解析该临时文件 */
			FormFile tmplFile = mChildReportForm.getTemplateFile();

//			boolean tempFileResult = FitechUtil.copyFile(inStream, fileName);
//			
//			if (tempFileResult == true)
//			{

				/** 插入insertMMainRep表 */
				MMainRep mMainRep = new MMainRep();
				mMainRep.setRepCnName(mChildReportForm.getReportName());
				mMainRep.setMCurUnit(new MCurUnit(Integer.valueOf(mChildReportForm.getReportCurUnit())));
				mMainRep.setMRepType(new MRepType(mChildReportForm.getRepTypeId()));
				session.save(mMainRep);

				/** 插入insertMChildReport表 */
				MChildReport mChildReport = new MChildReport();
				mChildReport.setComp_id(new MChildReportPK(mChildReportForm.getChildRepId(), mChildReportForm
						.getVersionId()));
				mChildReport.setIsPublic(new Integer(0));
				mChildReport.setReportName(mChildReportForm.getReportName());
				mChildReport.setMMainRep(mMainRep);
				mChildReport.setMCurUnit(mMainRep.getMCurUnit());
				mChildReport.setTemplateType("excel");
				if(mChildReportForm.getReportStyle() !=null && mChildReportForm.getReportStyle().intValue()==2){
					mChildReport.setReportStyle(Config.REPORT_STYLE_QD);
				} else{
					mChildReport.setReportStyle(Config.REPORT_STYLE_DD);
				}
				//有GF，SF为分支
				if(mChildReportForm.getChildRepId().indexOf("GF")==-1
						&& mChildReportForm.getChildRepId().indexOf("SF")==-1){
					mChildReport.setFrOrFzType("1");
				}else
					mChildReport.setFrOrFzType("2");
				
				session.save(mChildReport);

				/** 插入insertReportData表 */
				session.flush();

				// 写模板到reports/templates目录下，用于机构下载模板
				String templateFileName = Config.WEBROOTPATH + com.fitech.net.config.Config.REPORT_NAME
						+ File.separator + com.fitech.net.config.Config.TEMPLATE_NAME + File.separator
						+ mChildReportForm.getChildRepId() + "_" + mChildReportForm.getVersionId() + ".xls";
				System.out.println("=========templateFileName:"+templateFileName+"=============");
				result = FitechUtil.copyFile(fileName, templateFileName);
				System.out.println("==============copyFile templateFile success===============");
			
				// 写模板到report_mgr/excel目录下，但文件需要做适当的修改
				String excelFileName = Config.WEBROOTPATH + "report_mgr" + File.separator + "excel" + File.separator
						+ mChildReportForm.getChildRepId() + mChildReportForm.getVersionId() + ".xls";
				System.out.println("================excelFileName:"+excelFileName+"==============");
				result = FitechUtil.copyFile(fileName, excelFileName);
				System.out.println("=========copyFile excelFileName success============");

				if(result==true)
				{
					ExcelUtil.cleanExcelCellBg(new File(templateFileName));
					ExcelUtil.cleanExcelCellBg(new File(excelFileName));
					System.out.println("==============cleanExcelCell success============");
				}
//			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			result = false;
			throw ex;
		}
		finally
		{
			try
			{
				if (conn != null)
					conn.endTransaction(result);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				result = false;
				throw e;
			}
		}
		return result;
	}

	// boolean flag=false;
	// /**设置EXCEL数据域*/
	// DataArea dataArea=new DataArea();
	// dataArea.setStartRow(mExcelChildReportForm.getStartRow());
	// dataArea.setStartCol(mExcelChildReportForm.getStartCol().toUpperCase());
	// dataArea.setEndRow(mExcelChildReportForm.getEndRow());
	// dataArea.setEndCol(mExcelChildReportForm.getEndCol().toUpperCase());
	//	    
	// /**验证EXCEL数据域的有效性*/
	// if(!DataArea.isValid(dataArea)){
	// return false;
	// }
	// /**目前只支持列号在A-Z之间*/
	// if(dataArea.getStartCol().length()==1 &&
	// dataArea.getEndCol().length()==1){
	// MCell mCell=null;
	// HSSFRow row=null;
	// HSSFCell cell=null;
	// /**获取HSSFSheet*/
	// HSSFSheet
	// sheet=ExcelParser.getNeedSheet(mExcelChildReportForm,inStream,0);
	// if(sheet!=null){
	// DBConn conn=new DBConn();
	// Session session=conn.beginTransaction();
	// for(int
	// r=Integer.parseInt(dataArea.getStartRow());r<=Integer.parseInt(dataArea.getEndRow());r++)
	// {
	// /**获取HSSFRow对象*/
	// row=sheet.getRow(r-1);
	// for(char
	// c=dataArea.getStartCol().charAt(0);c<=dataArea.getEndCol().charAt(0);c++)
	// {
	// /**获取HSSFCell对象*/
	// if(row!=null){
	// cell=row.getCell(Short.parseShort(String.valueOf(StringTool.colIndex(String.valueOf(c)))));
	// }
	// /**如果单元格非空，则入库*/
	// if(cell!=null){
	// mCell=new MCell();
	// mCell.setCellName(String.valueOf(c)+String.valueOf(r));
	// mCell.setDataType(new Integer(cell.getCellType()));
	// mCell.setRowId(new Integer(r));
	// mCell.setColId(String.valueOf(c));
	// MChildReportPK comp_id=new MChildReportPK();
	// comp_id.setChildRepId(mChildReport.getComp_id().getChildRepId());
	// comp_id.setVersionId(mChildReport.getComp_id().getVersionId());
	// MChildReport mcr=new MChildReport();
	// mcr.setComp_id(comp_id);
	// mCell.setMChildReport(mcr);
	// try {
	// session.save(mCell);
	// } catch (HibernateException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
	// flag=true;
	// conn.endTransaction(true);
	// }
	// }
	//		
	//	
	// return flag;

}