/*
 * Created on 2006-5-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.obtain.excel.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.struts.upload.FormFile;
import org.dom4j.Document;
import org.dom4j.Element;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MMainRep;
import com.cbrc.smis.hibernate.MRepRange;
import com.fitech.net.common.XmlParser;
import com.fitech.net.config.Config;
import com.fitech.net.obtain.excel.TemplateObj;
import com.fitech.net.obtain.excel.TemplateObtainConfigure;
import com.fitech.net.obtain.excel.persistent.ExcelTemplate;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ObtainManager {
	
	/**
	 * 上传文件
	 * @param file 文件对象
	 * @param saveUri 保存地址
	 * @throws IOException IO异常
	 */
	public boolean uploadFile(FormFile file,
						      String savedFileName)throws IOException{
		boolean resultFlag = false;
		String fName = file.getFileName();
		boolean validFlag = false;
		int len = fName.length();
		if (fName != null && len > 4) {
			String suffixStr = fName.substring(len-4);
			
			if (suffixStr.equalsIgnoreCase(".xls")) {
				validFlag = true;
			}
		}
		
		if (validFlag) {			
			InputStream in = file.getInputStream();
			OutputStream out = new FileOutputStream(Config.getTemplateTempFolderRealPath() + File.separator + savedFileName);
			int byteRead = 0;
			byte[] buffer = new byte[1024];
			while((byteRead = in.read(buffer, 0, 1024)) != -1){
				out.write(buffer, 0, byteRead);
			}
			out.flush();
			out.close();
			in.close();	
			resultFlag = true;
		}else{
			throw new IOException("上传的文件不合法，请仔细检查");
		}
		
		return resultFlag;
	}
	
	
	public static ArrayList getOrgId(String repId, String versionId)
	{
		ArrayList al=new ArrayList();
		List mrrList=null;
		DBConn conn=null;
		Session session=null;
		conn=new DBConn();
		session=conn.openSession();
		
		try {
			mrrList=session.find("from MRepRange mrr where mrr.comp_id.childRepId='"
								+repId
								+"' and mrr.comp_id.versionId='"
								+versionId
								+"'");			
			if(mrrList!=null && mrrList.size()>0){
				
				MRepRange mrr=null;
				for(int i=0;i<mrrList.size();i++){
					mrr=(MRepRange)mrrList.get(i);
					// System.out.println(mrr.getComp_id().getOrgId());
					al.add(mrr.getComp_id().getOrgId());
				}
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally{
			if(conn != null) conn.closeSession();
		}
		return al;
	}
	
	/**
	 * 取得Excel文件名
	 * @param repID 模板ID
	 * @param versionID 模板版本号
	 * @return Excel文件名
	 */
	public static String getExcelName(String repId,
							          String versionId){
		
		String fileName="";
		DBConn conn=null;
		Session session=null;
		conn=new DBConn();
		session=conn.openSession();
		List mcrList=null;
		try {
			mcrList=session.find("from MChildReport mcr where mcr.comp_id.childRepId='"
						+repId
						+"' and mcr.comp_id.versionId='"
						+versionId+"'");
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		MChildReport mChildReport=null;
		MMainRep mMainRep=null;
		if(mcrList!=null && mcrList.size()==1){
			mChildReport=(MChildReport)mcrList.get(0);
		
		
			List mmrList=null;
			try {
				mmrList=session.find("from MMainRep mmr where mmr.repId=" + mChildReport.getMMainRep().getRepId());
				
				if(mmrList!=null && mmrList.size()==1){
					mMainRep=(MMainRep)mmrList.get(0);
				}				
				if(mMainRep!=null) fileName = mMainRep.getRepEnName();
				
			} catch (HibernateException e) {				
				e.printStackTrace();
			} finally{
				if(conn != null) conn.closeSession();
			}
		}
		return fileName;
	}
	
	/**
	 * 返回模板节点及其所有包含的数据源属性
	 * @param repID
	 * @param versionID
	 * @return
	 */
	public TemplateObj findTemplate(String repID, String versionID){
		TemplateObtainConfigure toc = new TemplateObtainConfigure(repID, versionID);
		TemplateObj to = toc.getTemplateObj();
		
		return to;
	}
	
	/**
	 * 查找合法的取数模板
	 * @return ArrayList object=com.fitech.net.obtain.excel.TemplateObj
	 */
	public ArrayList findValidObtainTemplates(){
		ArrayList al = new ArrayList();
		
		Document doc = XmlParser.parseXml(Config.getSystemFolderPath() + File.separator + Config.CONFIGURATION_OBTAIN_FILE);
		Element rootEle = doc.getRootElement();
		List templateEleList = rootEle.selectNodes("templates/template");
		if (templateEleList != null) {
			for(int i=0,n=templateEleList.size(); i<n; i++){
				Element templateEle = (Element)templateEleList.get(i);
				/*
				 * 校验合法性,过滤掉不合法的报表
				 */
				List datasourceEleList = templateEle.selectNodes("datasource[@state='"+ Config.DATA_SOURCE_STATE_TEMP +"']");
				if (datasourceEleList != null && datasourceEleList.size() > 0) {
					continue;
				}
				
				TemplateObj to = new TemplateObj();
				String repID = templateEle.valueOf("@repID");
				String versionID = templateEle.valueOf("@version");
				String xlsName = getExcelName(repID, versionID);
				
				to.setGuid(templateEle.valueOf("@guid"));
				to.setRepID(repID);
				to.setVersionID(versionID);
				to.setMode(templateEle.valueOf("@mode"));
				to.setExcelName(xlsName);
				al.add(to);
			}
		}
		
		return al;		
	}
	
	/**
	 * 新增一个excel模板文件 
	 *
	 */
	public void insertExcelTemplate(String repID, 
									String versionID, 
									String excleName){
		Session session = null;
		DBConn dbConn = null;
		boolean resultFalg = false;
		
		try {
			
			dbConn = new DBConn();
			
			session = dbConn.beginTransaction();
			
			ExcelTemplate excel = new ExcelTemplate();
			excel.setChildRepId(repID);
			excel.setVersionId(versionID);
			excel.setExcelName(excleName);
			session.save(excel);
			resultFalg = true;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(dbConn != null) dbConn.endTransaction(resultFalg);
		}
	}
	
	
	

	public static void main(String args[]){
		ObtainManager  om = new ObtainManager();
		om.insertExcelTemplate("0002", "0513", "G12.xls");
		//// System.out.println(om.getExcelName("0004", "0512"));
		// System.out.println("this is ok!");
	}
}
