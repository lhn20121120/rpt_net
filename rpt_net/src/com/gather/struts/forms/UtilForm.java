
/** ������
	 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
	 * It has a set of methods to handle persistence for MRepRange data (i.e. 
	 * com.gather.struts.forms.MRepRangeForm objects).
	 * 
	 * �����ķ���
	 */
package com.gather.struts.forms;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.gather.adapter.StrutsLogType;
import com.gather.hibernate.LogType;
/**
 * 
 * ��LogTypes���ݿ�ļ�¼����FORM�������JSP��
 * @author FitechServer
 *
 */
public class UtilForm {
	private List logTypes = null;
	private List Orgname=null;
	private Object getOrgname;
	public List getlogTypes() {
		if (this.logTypes != null) {
			return logTypes;
		} else {
			ArrayList lists = new ArrayList();

			List results = null;
			try {
				results = StrutsLogType.findAll();
				if (results != null) {
					for (int i = 0; i < results.size(); i++) {
						LogType lt = (LogType) (results.get(i));
						lists.add(new LabelValueBean(lt.getLogType(), lt
								.getLogTypeId().toString()));
					}
				}
			} catch (Exception e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
			return lists;
		}
	}
}
