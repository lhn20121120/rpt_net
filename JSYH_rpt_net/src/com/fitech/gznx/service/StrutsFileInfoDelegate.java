package com.fitech.gznx.service;

import java.sql.Blob;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.struts.upload.FormFile;

import com.cbrc.smis.dao.DBConn;

import com.fitech.gznx.po.AFFileInfo;


public class StrutsFileInfoDelegate {
	

	/**
	 * 插入附件信息
	 * 
	 * @author db2admin
	 * @date 2007-3-17
	 * @param file
	 *            附件
	 * @param fileType
	 *            附件所属类别
	 * @return
	 * 
	 */
	public static AFFileInfo create(FormFile file, Integer fileType) {
		AFFileInfo fileInfo = null;
		boolean result = false;
		if (file == null || fileType == null)
			return null;

		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			fileInfo = new AFFileInfo();
			Blob blob = Hibernate.createBlob(file.getInputStream());
			fileInfo.setFileContent(blob);
			fileInfo.setFileName(file.getFileName());
			fileInfo.setFileSize(new Integer(file.getFileSize()));
			fileInfo.setFileContentType(file.getContentType());
			fileInfo.setFileType(fileType);
			session.save(fileInfo);
			session.flush();
			result = true;
		} catch (Exception he) {
			he.printStackTrace();
			fileInfo = null;
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}

		return fileInfo;
	}

	/**
	 * <p>
	 * 标题：获得fileinfo的实体类
	 * <p>
	 * <p>
	 * 描述：
	 * <p>
	 * <p>
	 * 参数：Integer fileId
	 * <p>
	 * <p>
	 * 返回：FileInfo
	 * <p>
	 * <p>
	 * 日期：2007-03-17
	 * <p>
	 * <p>
	 * 作者：曹发根
	 * <p>
	 */
	public static AFFileInfo load(Integer fileId) {
		AFFileInfo result = null;

		if (fileId == null)
			return null;

		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();
			AFFileInfo fileInfo = (AFFileInfo) session.load(AFFileInfo.class, fileId);
			if (fileInfo != null) {
				result = new AFFileInfo();
				result.setFileContent(fileInfo.getFileContent());
				result.setFileContentType(fileInfo.getFileContentType());
				result.setFileId(fileInfo.getFileId());
				result.setFileName(fileInfo.getFileName());
				result.setFileSize(fileInfo.getFileSize());
				result.setFileType(fileInfo.getFileType());
				//result.setTemplateTranses(fileInfo.getTemplateTranses());
			}

		} catch (Exception he) {
			he.printStackTrace();
			result = null;
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return result;
	}

	/**
	 * 获得该文件的基本信息
	 * 
	 * @author db2admin
	 * @date 2007-4-4
	 * @param fileId
	 * @return
	 * 
	 */
	public static AFFileInfo getFileInfo(Integer fileId) {
		AFFileInfo result = null;

		if (fileId == null)
			return null;

		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();
			StringBuffer hql = new StringBuffer(
					"select file.fileName,file.fileSize,file.fileType "
							+ "from AFFileInfo file where file.fileId=" + fileId);

			Query query = session.createQuery(hql.toString());
			List recordList = query.list();
			if (recordList != null && recordList.size() > 0) {
				result = new AFFileInfo();
				Object[] itemArray = (Object[]) recordList.get(0);
				result.setFileId(fileId);
				result.setFileName((String) itemArray[0]);
				result.setFileSize((Integer) itemArray[1]);
				result.setFileType((Integer) itemArray[2]);
			}
		} catch (Exception he) {
			he.printStackTrace();
			result = null;
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return result;
	}

	/**
	 * 更新附件信息
	 * 
	 * @author db2admin
	 * @date 2007-3-17
	 * @param file
	 *            附件
	 * @param fileType
	 *            附件所属类别
	 * @return
	 * 
	 */
	public static boolean update(Integer fileId, FormFile file) {
		boolean result = false;
		if (file == null || file == null)
			return false;

		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			if (fileId == null) { // 如果为空则直接插入
				AFFileInfo fileInfo = new AFFileInfo();
				fileInfo.setFileContent(Hibernate.createBlob(file
						.getInputStream()));
				fileInfo.setFileName(file.getFileName());
				fileInfo.setFileSize(new Integer(file.getFileSize()));
				fileInfo.setFileContentType(file.getContentType());
				session.save(fileInfo);
				result=true;
				return true;
			}

			AFFileInfo fileInfo = (AFFileInfo) session.get(AFFileInfo.class, fileId);
			if (fileInfo == null) {
				AFFileInfo fi = new AFFileInfo();
				fi.setFileContent(Hibernate.createBlob(file.getInputStream()));
				fi.setFileName(file.getFileName());
				fi.setFileSize(new Integer(file.getFileSize()));
				fi.setFileContentType(file.getContentType());
				session.saveOrUpdate(fi);
				result=true;
				return true;

			}
			fileInfo
					.setFileContent(Hibernate.createBlob(file.getInputStream()));
			fileInfo.setFileName(file.getFileName());
			fileInfo.setFileSize(new Integer(file.getFileSize()));
			fileInfo.setFileContentType(file.getContentType());
			session.update(fileInfo);
			result = true;
		} catch (Exception he) {
			he.printStackTrace();
			result = false;
		} finally {
			if (conn != null)
				result = conn.endTransaction(result);
		}

		return result;
	}

	/**
	 * 删除附件信息
	 * 
	 * @author db2admin
	 * @date 2007-3-17
	 * @param file
	 *            附件
	 * @param fileType
	 *            附件所属类别
	 * @return
	 * 
	 */
	public static boolean delete(Integer fileId) {
		boolean result = false;
		if (fileId == null)
			return false;

		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			AFFileInfo fileInfo = (AFFileInfo) session.get(AFFileInfo.class, fileId);
			if (fileInfo != null)
				session.delete(fileInfo);
			result = true;
		} catch (Exception he) {
			he.printStackTrace();
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}

		return result;
	}

	/**
	 * 
	 * 方法说明:该方法用于插入文件大对象
	 * 
	 * @author chenbing
	 * @date 2007-3-30
	 * @param blob
	 * @param fileType
	 */
	public static AFFileInfo insertFile(Blob blob, String fileName,
			Integer fileType, Session session) throws Exception {

		AFFileInfo fileInfo = new AFFileInfo();

		fileInfo.setFileContent(blob);

		fileInfo.setFileName(fileName);

		fileInfo.setFileSize(new Integer(blob.getBinaryStream().available()));

		fileInfo.setFileContentType(getStrSuffix(fileName)[1]);

		fileInfo.setFileType(fileType);

		session.save(fileInfo);

		return fileInfo;
	}
	
	public static String[] getStrSuffix(String name) {

		if (name == null && name.equals(""))
			return null;

		String[] result = new String[2];

		int index = name.indexOf('.');

		result[0] = name.substring(0, index);

		result[1] = name.substring(index + 1);

		return result;
	}

}
