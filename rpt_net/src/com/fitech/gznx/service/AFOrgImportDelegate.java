package com.fitech.gznx.service;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.po.AfOrg;

public class AFOrgImportDelegate {

	private static FitechException log = new FitechException(
			AFOrgImportDelegate.class);
	
	public boolean orgImport(File excel){
		boolean result = false;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;
		HSSFWorkbook book=null;
		FileInputStream input=null;
		HSSFCell cell=null;
		HSSFRow row=null;
		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.beginTransaction();
			book=new HSSFWorkbook(new FileInputStream(excel));
			HSSFSheet sheet=book.getSheetAt(0);
			for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum(); i ++) {
				row = sheet.getRow(i);
				cell = row.getCell((short)0);
				String orgId = cell.getStringCellValue();
				cell = row.getCell((short)1);
				String regionId = cell.getStringCellValue();
				cell = row.getCell((short)2);
				String orgName = cell.getStringCellValue();
				cell = row.getCell((short)3);
				String orgIdOuter = cell.getStringCellValue();
				cell = row.getCell((short)4);
				String preOrgId = cell.getStringCellValue();
				com.fitech.net.hibernate.MRegion region = new com.fitech.net.hibernate.MRegion();
				region.setRegionId(new Integer(regionId));
				region.setRegionName(orgName);
				region.setPreRegionId(-1);
				com.fitech.net.hibernate.OrgType orgType = new com.fitech.net.hibernate.OrgType();
				orgType.setOrgTypeId(1);
				region.setOrgType(orgType);
				session.saveOrUpdateCopy(region);
			}
			session.flush();
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}
}
