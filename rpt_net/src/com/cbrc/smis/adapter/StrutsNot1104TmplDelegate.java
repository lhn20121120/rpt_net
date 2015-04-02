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

	/** ����ģ�嵽ָ��·�� */
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
	 * ģ����� Ҫinsert���ű����Ⱥ�˳��ΪMMainRep,MChildReport,ReportData,MCell
	 * Ϊ�������������ԣ����insert�κ�һ�ű�ʧ�ܣ����ع����˳�����
	 */
	public static boolean insertTemplate(MExcelChildReportForm mExcelChildReportForm, File tmptFile, DataArea dataArea)
	{
		boolean flag = false;

		DBConn conn = new DBConn();
		Session session = conn.beginTransaction();

		try
		{
			/** insertMMainRep�� */
			MRepType mRepType = (MRepType) session.load(MRepType.class, mExcelChildReportForm.getRepTypeId());
			MCurUnit mCurUnit = StrutsMCurUnitDelegate.getMCurUnit(mExcelChildReportForm.getReportCurUnit());
			MMainRep mMainRep = new MMainRep();
			mMainRep.setRepCnName(mExcelChildReportForm.getReportName());
			mMainRep.setMCurUnit(mCurUnit);
			mMainRep.setMRepType(mRepType);
			session.save(mMainRep);

			/** insertMMainRep������ɹ������insertMChildReport�����򷵻�false */
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

			/** insertMCell������ɹ����ύ���в���������flag��Ϊtrue */
			/** Ŀǰֻ֧���к���A-Z֮�� */
			FileInputStream inStream = new FileInputStream(tmptFile);
			if (dataArea.getStartCol().length() == 1 && dataArea.getEndCol().length() == 1)
			{
				MCell mCell = null;
				HSSFRow row = null;
				HSSFCell cell = null;
				/** ��ȡHSSFSheet */
				HSSFSheet sheet = ExcelParser.getNeedSheet(mExcelChildReportForm, inStream, 0);
				if (sheet != null)
				{
					for (int r = Integer.parseInt(dataArea.getStartRow()); r <= Integer.parseInt(dataArea.getEndRow()); r++)
					{
						/** ��ȡHSSFRow���� */
						row = sheet.getRow(r - 1);
						for (char c = dataArea.getStartCol().charAt(0); c <= dataArea.getEndCol().charAt(0); c++)
						{
							/** ��ȡHSSFCell���� */
							if (row == null)
								continue;
							cell = row
									.getCell(Short.parseShort(String.valueOf(StringTool.colIndex(String.valueOf(c)))));

							/** �����Ԫ��ǿգ������ */
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

			/** insertReportData�� */
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
	 * ��ʹ��hibernate  ���Ը� 2011-12-22
	 * ��֧ģ����� ��Excel��
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
			/** ������ʱ�ļ�����������������ʱ�ļ� */
			FormFile tmplFile = mChildReportForm.getTemplateFile();

//			boolean tempFileResult = FitechUtil.copyFile(inStream, fileName);
//			
//			if (tempFileResult == true)
//			{

				/** ����insertMMainRep�� */
				MMainRep mMainRep = new MMainRep();
				mMainRep.setRepCnName(mChildReportForm.getReportName());
				mMainRep.setMCurUnit(new MCurUnit(Integer.valueOf(mChildReportForm.getReportCurUnit())));
				mMainRep.setMRepType(new MRepType(mChildReportForm.getRepTypeId()));
				session.save(mMainRep);

				/** ����insertMChildReport�� */
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
				//��GF��SFΪ��֧
				if(mChildReportForm.getChildRepId().indexOf("GF")==-1
						&& mChildReportForm.getChildRepId().indexOf("SF")==-1){
					mChildReport.setFrOrFzType("1");
				}else
					mChildReport.setFrOrFzType("2");
				
				session.save(mChildReport);

				/** ����insertReportData�� */
				session.flush();

				// дģ�嵽reports/templatesĿ¼�£����ڻ�������ģ��
				String templateFileName = Config.WEBROOTPATH + com.fitech.net.config.Config.REPORT_NAME
						+ File.separator + com.fitech.net.config.Config.TEMPLATE_NAME + File.separator
						+ mChildReportForm.getChildRepId() + "_" + mChildReportForm.getVersionId() + ".xls";
				System.out.println("=========templateFileName:"+templateFileName+"=============");
				result = FitechUtil.copyFile(fileName, templateFileName);
				System.out.println("==============copyFile templateFile success===============");
			
				// дģ�嵽report_mgr/excelĿ¼�£����ļ���Ҫ���ʵ����޸�
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
	// /**����EXCEL������*/
	// DataArea dataArea=new DataArea();
	// dataArea.setStartRow(mExcelChildReportForm.getStartRow());
	// dataArea.setStartCol(mExcelChildReportForm.getStartCol().toUpperCase());
	// dataArea.setEndRow(mExcelChildReportForm.getEndRow());
	// dataArea.setEndCol(mExcelChildReportForm.getEndCol().toUpperCase());
	//	    
	// /**��֤EXCEL���������Ч��*/
	// if(!DataArea.isValid(dataArea)){
	// return false;
	// }
	// /**Ŀǰֻ֧���к���A-Z֮��*/
	// if(dataArea.getStartCol().length()==1 &&
	// dataArea.getEndCol().length()==1){
	// MCell mCell=null;
	// HSSFRow row=null;
	// HSSFCell cell=null;
	// /**��ȡHSSFSheet*/
	// HSSFSheet
	// sheet=ExcelParser.getNeedSheet(mExcelChildReportForm,inStream,0);
	// if(sheet!=null){
	// DBConn conn=new DBConn();
	// Session session=conn.beginTransaction();
	// for(int
	// r=Integer.parseInt(dataArea.getStartRow());r<=Integer.parseInt(dataArea.getEndRow());r++)
	// {
	// /**��ȡHSSFRow����*/
	// row=sheet.getRow(r-1);
	// for(char
	// c=dataArea.getStartCol().charAt(0);c<=dataArea.getEndCol().charAt(0);c++)
	// {
	// /**��ȡHSSFCell����*/
	// if(row!=null){
	// cell=row.getCell(Short.parseShort(String.valueOf(StringTool.colIndex(String.valueOf(c)))));
	// }
	// /**�����Ԫ��ǿգ������*/
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