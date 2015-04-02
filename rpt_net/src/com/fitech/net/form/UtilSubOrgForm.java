package com.fitech.net.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;


/**
 * 
 * @author jcm
 * @serialData 2006-10-09
 */
public class UtilSubOrgForm {
	
	/**����ID*/
	private String orgId = null;
	
	/**���е��ӻ�����Ϣ*/
	private List subOrgs = null;
	
	/**���е��ӻ����ͱ�������Ϣ*/
	private List subCurrOrgs = null;
	
	/**�����¼�������Ϣ*/
	private List notLowerOrgs = null;
	
	/**�鿴Ȩ��*/
	private String childRepSearchPodedom = null;
	
	/**���Բ鿴�Ļ������û���Ȩ�����õĲ鿴Ȩ�ޣ�*/
	private List childRepSearch = null;

	/**���Բ鿴�Ļ���*/
	private List childOrgs = null;
	
	/**���Ȩ��*/
	private String childRepCheckPodedom = null;
	
	/**������˵Ļ������û���Ȩ�����õ����Ȩ�ޣ�*/
	private List childRepCheck = null;
	
	/**����Ȩ��*/
	private String childRepReportPodedom = null;
	
	/**���Դ����Ļ���*/
	private List childRepReport = null;

	/**����Ȩ��*/
	private String childRepVerifyPodedom = null;
	
	/**���Ը��˵Ļ������û���Ȩ�����õ����Ȩ�ޣ�*/
	private List childRepVerify = null;
	
	/**�����������*/
	private List rhOrgId = null;
	private String[] preorg = {"0",com.fitech.gznx.common.Config.HEAD_ORG_ID,"-99"};
	/**
	 * ͨ�����Ȩ�޻�ȡ��˻����б�
	 * 
	 * @author jcm
	 * @return List ������˵Ļ����б�
	 */
	public List getChildRepCheck() {
		if(childRepCheck == null){
			childRepCheck = new ArrayList();
			
			if(childRepCheckPodedom != null && !childRepCheckPodedom.equals("")){			
				List orgList = AFOrgDelegate.selectOrgByIds(childRepCheckPodedom.replaceAll("orgRepId","orgId"));
				if(orgList != null && orgList.size() > 0){
					AfOrg afOrg = null;
					for(Iterator iter=orgList.iterator();iter.hasNext();){
						afOrg = (AfOrg)iter.next();
						childRepCheck.add(new LabelValueBean(afOrg.getOrgName(),afOrg.getOrgId()));
					}
				}
			}
			return childRepCheck;
		}else 
			return childRepCheck;
	}

	/**
	 * ͨ���鿴Ȩ�޻�ȡ�鿴�����б�
	 * 
	 * @author jcm
	 * @return List ���Բ鿴�Ļ����б�
	 */
	public List getChildRepSearch() {
		if(childRepSearch == null){
			childRepSearch = new ArrayList();
			
			if(childRepSearchPodedom != null && !childRepSearchPodedom.equals("")){			
				List orgList = AFOrgDelegate.selectOrgByIds(childRepSearchPodedom.replaceAll("orgRepId","orgId"));
				if(orgList != null && orgList.size() > 0){
					AfOrg afOrg = null;
					for(Iterator iter=orgList.iterator();iter.hasNext();){
						afOrg = (AfOrg)iter.next();
						childRepSearch.add(new LabelValueBean(afOrg.getOrgName(),afOrg.getOrgId()));
					}
				}
			}
			return childRepSearch;
		}else 
			return childRepSearch;
	}

	/**
	 * ͨ������Ȩ�޻�ȡ���������б�
	 * 
	 * @author jcm
	 * @return List ���Դ����Ļ����б�
	 */
	public List getReportOrgs() {
		
		if(childRepReport == null){
			childRepReport = new ArrayList();
			
			if(childRepReportPodedom != null && !childRepReportPodedom.equals("")){
				List orgList = AFOrgDelegate.selectOrgByIds(childRepReportPodedom.replaceAll("orgRepId","orgId"));
				if(orgList != null && orgList.size() > 0){
					AfOrg afOrg = null;
					for(Iterator iter=orgList.iterator();iter.hasNext();){
						afOrg = (AfOrg)iter.next();
						childRepReport.add(new LabelValueBean(afOrg.getOrgName(),afOrg.getOrgId()));
					}
				}
			}
			return childRepReport;
		}else 
			return childRepReport;
	}
	
	/**
	 * �õ��鿴��֧�����Ļ���Ȩ���б�
	 * @return
	 */
	public List getChildOrgs() {		
		if(childOrgs == null){
			childOrgs = new ArrayList();
						
			try{
				//�ܷ����޸�
				List list1=StrutsOrgNetDelegate.selectLowerOrgList(orgId);
				if(list1 != null && list1.size() > 0){
					for(int i=0;i<list1.size();i++){
						OrgNet orgNet = (OrgNet)list1.get(i);
						childOrgs.add(new LabelValueBean(orgNet.getOrgName(),orgNet.getOrgId()));
					}						
				}
			}catch(Exception e){
				e.printStackTrace();
				childOrgs = new ArrayList();
			}
			return childOrgs;
		}else
			return childOrgs;
	}

	public void setChildOrgs(List childOrgs) {
		this.childOrgs = childOrgs;
	}

	/**
	 * @return Returns the childRepCheckPodedom.
	 */
	public String getChildRepCheckPodedom() {
		return childRepCheckPodedom;
	}

	/**
	 * @param childRepCheckPodedom The childRepCheckPodedom to set.
	 */
	public void setChildRepCheckPodedom(String childRepCheckPodedom) {
		this.childRepCheckPodedom = childRepCheckPodedom;
	}

	/**
	 * @return Returns the childRepSearchPodedom.
	 */
	public String getChildRepSearchPodedom() {
		return childRepSearchPodedom;
	}

	/**
	 * @param childRepSearchPodedom The childRepSearchPodedom to set.
	 */
	public void setChildRepSearchPodedom(String childRepSearchPodedom) {
		this.childRepSearchPodedom = childRepSearchPodedom;
	}

	/**
	 * @return Returns the childRepReportPodedom.
	 */
	public String getChildRepReportPodedom() {
		return childRepReportPodedom;
	}

	/**
	 * @param childRepReportPodedom The childRepReportPodedom to set.
	 */
	public void setChildRepReportPodedom(String childRepReportPodedom) {
		this.childRepReportPodedom = childRepReportPodedom;
	}

	public List getSubOrgs() {
		if(orgId == null) return subOrgs;
		
		if(subOrgs == null){
			subOrgs = new ArrayList();
			try{
				List list = StrutsOrgNetDelegate.selectLowerOrgList(orgId);
				if(list != null && list.size() > 0){
					this.getOneLayer(subOrgs,list);
				}
			}catch(Exception e){
				e.printStackTrace();
				subOrgs = new ArrayList();
			}
			return subOrgs;
		}else return subOrgs;
	}

	public void setSubOrgs(List subOrgs) {
		this.subOrgs = subOrgs;
	}
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	private void getOneLayer(List subOrgs,List list){
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				OrgNet orgNet = (OrgNet)list.get(i);
				subOrgs.add(new LabelValueBean(orgNet.getOrgName(),orgNet.getOrgId()));
				List tempList = StrutsOrgNetDelegate.selectLowerOrgList(orgNet.getOrgId());
				if(tempList != null && tempList.size() > 0)
					this.getOneLayer(subOrgs,tempList);
			}
		}
	}
	public List getNotLowerOrgs() {
		if(notLowerOrgs == null){
			notLowerOrgs = new ArrayList();
			try{
				List list = StrutsOrgNetDelegate.getNotLowerOrgs();
				if(list != null && list.size() > 0){
					for(int i=0;i<list.size();i++){
						OrgNet orgNet = (OrgNet)list.get(i);
						notLowerOrgs.add(new LabelValueBean(orgNet.getOrgName(),orgNet.getOrgId()));
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
				notLowerOrgs = new ArrayList();
			}
			return notLowerOrgs;
		}else
			return notLowerOrgs;
	}

	public void setNotLowerOrgs(List notLowerOrgs) {
		this.notLowerOrgs = notLowerOrgs;
	}

	public List getSubCurrOrgs()
	{
		if(orgId == null) return subCurrOrgs;
		
		if(subCurrOrgs == null){
			subCurrOrgs = new ArrayList();
			try{
				List list = StrutsOrgNetDelegate.selectLowerOrgCurrOrgList(orgId);
				if(list != null && list.size() > 0){
					this.getOneLayer(subCurrOrgs,list);
				}
			}catch(Exception e){
				e.printStackTrace();
				subCurrOrgs = new ArrayList();
			}
			return subCurrOrgs;
		}else return subCurrOrgs;
		
	
	}

	public void setSubCurrOrgs(List subCurrOrgs)
	{
		this.subCurrOrgs = subCurrOrgs;
	}

	public List getChildRepReport() {
		
		if(childRepReport == null){
			childRepReport = new ArrayList();
			
			if(childRepReportPodedom != null && !childRepReportPodedom.equals("")){			
				List orgList = AFOrgDelegate.selectOrgByIds(childRepReportPodedom.replaceAll("orgRepId","orgId"));
				if(orgList != null && orgList.size() > 0){
					AfOrg afOrg = null;
					for(Iterator iter=orgList.iterator();iter.hasNext();){
						afOrg = (AfOrg)iter.next();
						childRepReport.add(new LabelValueBean(afOrg.getOrgName(),afOrg.getOrgId()));
					}
				}
			}
			return childRepReport;
		}else 
			return childRepReport;
	}

	public void setChildRepReport(List childRepReport) {
		this.childRepReport = childRepReport;
	}

	public String getChildRepVerifyPodedom() {
		return childRepVerifyPodedom;
	}

	public void setChildRepVerifyPodedom(String childRepVerifyPodedom) {
		this.childRepVerifyPodedom = childRepVerifyPodedom;
	}

	public List getChildRepVerify() {
		if(childRepVerify == null){
			childRepVerify = new ArrayList();
			
			if(childRepVerifyPodedom != null && !childRepVerifyPodedom.equals("")){			
				List orgList = AFOrgDelegate.selectOrgByIds(childRepVerifyPodedom.replaceAll("orgRepId","orgId"));
				if(orgList != null && orgList.size() > 0){
					AfOrg afOrg = null;
					for(Iterator iter=orgList.iterator();iter.hasNext();){
						afOrg = (AfOrg)iter.next();
						childRepVerify.add(new LabelValueBean(afOrg.getOrgName(),afOrg.getOrgId()));
					}
				}
			}
			return childRepVerify;
		}else 
			return childRepVerify;
	}

	public void setChildRepVerify(List childRepVerify) {
		this.childRepVerify = childRepVerify;
	}

	public void setRhOrgId(List rhOrgId) {
		this.rhOrgId = rhOrgId;
	}

	public List getRhOrgId() {
				
		if(rhOrgId == null){
			rhOrgId = new ArrayList();
						
			try{
				//�ܷ����޸�
				for(int j=0;j<preorg.length;j++){
					String preOrgId=preorg[j];
				List list1=StrutsOrgNetDelegate.RhOrgId(preOrgId);
				if(list1 != null && list1.size() > 0){
					for(int i=0;i<list1.size();i++){
						OrgNet orgNet = (OrgNet)list1.get(i);
						rhOrgId.add(new LabelValueBean(orgNet.getOrgName(),orgNet.getOrgId()));
					}						
				}
				}
			}catch(Exception e){
				e.printStackTrace();
				rhOrgId = new ArrayList();
			}
			return rhOrgId;
		}else{						
			return rhOrgId;
		}

	}
}
