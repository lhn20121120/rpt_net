package com.fitech.gznx.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts.upload.FormFile;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.smis.adapter.TranslatorUtil;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.fitech.gznx.form.PlacardForm;
import com.fitech.gznx.po.AFFileInfo;
import com.fitech.gznx.po.AfPlacard;
import com.fitech.gznx.po.AfPlacardUserView;
import com.fitech.gznx.po.AfPlacardUserViewId;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;




public class StrutsPlacardDelegate {


	public static List select(PlacardForm placardForm) throws Exception {
		if (placardForm == null || placardForm.getPublicUserId() == null
				|| placardForm.getPublicUserId().equals("")) {
			return null;
		}
		List result = null;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			StringBuffer hql = new StringBuffer(
					"from AfPlacard pd where 1=1 and pd.publicUserId='"
							+ placardForm.getPublicUserId() + "' ");
			String title = placardForm.getTitle();
			String startDate = placardForm.getStartDate();
			String endDate = placardForm.getEndDate();

			if (title != null && !title.equals("")) {
				hql.append(" and pd.title like '%" + title.trim() + "%'");
			}
			if (startDate != null && !startDate.equals("")) {
				hql.append(" and to_char(pd.publicDate,'yyyy-mm-dd')>='" + startDate.trim() + "'");
			}
			if (endDate != null && !endDate.equals("")) {
				hql.append(" and to_char(pd.publicDate,'yyyy-mm-dd')<='" + endDate.trim() + "'");
			}
			hql.append(" order by pd.placardId desc");

			Query query = session.createQuery(hql.toString());
			List list = query.list();
			if (list != null && list.size() != 0) {
				result = new ArrayList();
				for (Iterator it = list.iterator(); it.hasNext();) {
					PlacardForm placardFormTemp = new PlacardForm();
					AfPlacard placardPersistence = (AfPlacard) it.next();
					TranslatorUtil.copyPersistenceToVo(placardPersistence,
							placardFormTemp);
					result.add(placardFormTemp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * 
	 * @param placardForm
	 *            PlacardForm 包含插入公告的信息
	 * @return
	 * @throws Exception
	 */
	public static boolean create(PlacardForm placardForm) throws Exception {
		if (placardForm == null)
			return false;
		boolean result = false;
		DBConn conn = null;
		Session session = null;
		if (placardForm != null) {
			try {
				conn = new DBConn();
				session = conn.beginTransaction();

				AfPlacard placard = new AfPlacard();
				if(placardForm.getPlacardFile().getFileSize() > 0){
				/* 附件信息插入 */
				AFFileInfo fileInfo = StrutsFileInfoDelegate.create(placardForm
						.getPlacardFile(), Config.FILE_TYPE_PLACARD_FILE);
					if (fileInfo != null)
						placardForm.setFileId(fileInfo.getFileId());
					else
						return false;
				}
				TranslatorUtil.copyPersistenceToVo1(placard, placardForm);
				session.save(placard);

				/* 插入针对用户信息 */
				String[] userIdStr = placardForm.getUserIdStr().split(",");
				if (userIdStr != null && userIdStr.length>0) {
					
					for (int i = 0; i < userIdStr.length; i++) {
						String userId = userIdStr[i];
						OperatorForm operatorForm = StrutsOperatorDelegate.getUserDetail(Long.valueOf(userId));
						if(operatorForm != null && operatorForm.getUserName() != null){
							AfPlacardUserView placardUserView = new AfPlacardUserView();

							AfPlacardUserViewId placardUserViewId = new AfPlacardUserViewId();
							placardUserViewId.setPlacard(placard);
							placardUserViewId.setOrgId(userId);
							placardUserView.setId(placardUserViewId);
							placardUserView.setViewState(new Long(0));
							session.save(placardUserView);
						}
					}
				}

				session.flush();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			} finally {
				if (conn != null)
					conn.endTransaction(result);
			}
		}
		return result;
	}

	/**
	 * 根据公告ID删除公告
	 */
	public static boolean delete(Long placardId) throws Exception {
		if (placardId == null)
			return false;
		boolean result = false;
		DBConn conn = null;
		Session session = null;
		AfPlacard placard = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			String hql = "from AfPlacardUserView po where po.id.placard.placardId="
					+ placardId;
			List list = session.createQuery(hql).list();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					session.delete((AfPlacardUserView) list.get(i));
				}
			}

			placard = (AfPlacard) session.load(AfPlacard.class, placardId);
			session.delete(placard);
			session.flush();

			result = true;
			if (placard.getFileId()!=null)
				// 删除附件信息
			{					
				result = StrutsFileInfoDelegate.delete(placard.getFileId().intValue());
			}
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		} finally {

			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 根据公告ID获得该公告的详细信息
	 * 
	 * @author db2admin
	 * @date 2007-4-4
	 * @param placardId
	 * @return
	 * @throws Exception
	 * 
	 */
	public static PlacardForm getPlacardInfo(Long placardId) throws Exception {
		if (placardId == null) {
			return null;
		}
		PlacardForm result = null;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			StringBuffer hql = new StringBuffer(
					"from AfPlacard pd where pd.placardId=" + placardId);

			Query query = session.createQuery(hql.toString());
			List list = query.list();
			if (list != null && list.size() != 0) {
				result = new PlacardForm();
				AfPlacard placardPersistence = (AfPlacard) list.get(0);
				TranslatorUtil.copyPersistenceToVo(placardPersistence, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * 
	 * @param placardForm
	 *            PlacardForm 包含插入公告的信息
	 * @return
	 * @throws Exception
	 */
	public static boolean update(PlacardForm placardForm) throws Exception {
		if (placardForm == null || placardForm.getPlacardId() == null)
			return false;
		boolean result = false;
		DBConn conn = null;
		Session session = null;
		if (placardForm != null) {
			try {
				conn = new DBConn();
				session = conn.beginTransaction();
				Long placardId = placardForm.getPlacardId();

				AfPlacard placard = (AfPlacard) session.load(AfPlacard.class,
						placardId);
				/* 判断附件信息重新载入为空*/
				
				if(!placardForm.getPlacardFile().toString().equals(""))
				{
					if(placard.getFileId()!= null){
						placardForm.setFileId(Integer.valueOf(placard.getFileId().intValue()));
						result = StrutsFileInfoDelegate.update(Integer.valueOf(placard.getFileId().intValue()), placardForm
								.getPlacardFile());
						
					}else{						
						/* 附件信息插入 */
						AFFileInfo fileInfo = StrutsFileInfoDelegate.create(placardForm
								.getPlacardFile(), Config.FILE_TYPE_PLACARD_FILE);
						if (fileInfo != null)
							placardForm.setFileId(fileInfo.getFileId());
						else
							return false;
					}
				}
				
				TranslatorUtil.copyPersistenceToVo1(placard, placardForm);
				session.update(placard);

				String hql = "from AfPlacardUserView puv where puv.id.placard.placardId="
						+ placardId;
				List list = session.createQuery(hql).list();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						session.delete((AfPlacardUserView) list.get(i));
					}
				}
				String[] userIdStr = placardForm.getUserIdStr().split(",");
				if (userIdStr != null && userIdStr.length>0) {
					
					for (int i = 0; i < userIdStr.length; i++) {
						String userId = userIdStr[i];
						OperatorForm operatorForm = StrutsOperatorDelegate.getUserDetail(Long.valueOf(userId));
						if(operatorForm != null && operatorForm.getUserName() != null){
							AfPlacardUserView placardUserView = new AfPlacardUserView();
	
							AfPlacardUserViewId placardUserViewId = new AfPlacardUserViewId();
							placardUserViewId.setPlacard(placard);
							placardUserViewId.setOrgId(userId);
							placardUserView.setId(placardUserViewId);
							placardUserView.setViewState(new Long(0));
							session.save(placardUserView);
						}
					}
				}

				session.flush();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			} finally {
				if (conn != null)
					conn.endTransaction(result);
			}
		}
		return result;
	}

	public static void createUserTree(String filePath, Map userMap) {
		org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
		document.setXMLEncoding("GB2312");
	
		Element rootElement = document.addElement("tree");
		rootElement.addAttribute("id", "0");
	
		Element oneElement = rootElement.addElement("item");
		oneElement.addAttribute("text", "全部用户");
		oneElement.addAttribute("id", "-999");
		oneElement.addAttribute("open", "1");
		oneElement.addAttribute("checked", "1");
		
		getNode(userMap, oneElement);
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GB2312");
			XMLWriter output = new XMLWriter();
			output = new XMLWriter(new FileOutputStream(filePath), format);
			output.write(document);
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void getNode(Map orgMap, Element rootElement) {
		List orgList = null;
		
		orgList = XmlTreeUtil.selectUserTree();
		if (orgList != null && orgList.size()>0) {
			for (int i = 0; i < orgList.size(); i++) {
				OrgNet o = (OrgNet) orgList.get(i);
				try {
					Element element = rootElement.addElement("item");
					element.addAttribute("text", o.getOrgName());
					element.addAttribute("id", o.getOrgId());
					addChild(element, o, orgMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}


	private static void addChild(Element e, OrgNet orgNet, Map orgMap) {
		if (orgNet != null) {
			String id = orgNet.getOrgId();
			List childList = XmlTreeUtil.selectUserTreebyID(orgNet.getOrgId());
			if (childList != null && childList.size()>0) {
				for (int i = 0; i < childList.size(); i++) {
					com.cbrc.auth.hibernate.Operator o = (com.cbrc.auth.hibernate.Operator) childList.get(i);
					
					Element child =  e.addElement("item");
					child.addAttribute("text", o.getUserName());
					child.addAttribute("id", String.valueOf(o.getUserId()));
					if (orgMap.containsKey(String.valueOf(o.getUserId()))) {
						child.addAttribute("checked", "1");
						child.addAttribute("open", "1");
					}
				
				}
				
			}
		}
	}

}
