package com.fitech.gznx.treexml;

import java.util.List;

import com.cbrc.smis.security.Operator;


/**
 * 
 * title: �ýӿ����ڵݹ��������в�εĻ�����
 * 
 * @author chenbing 2008-2-19
 */
public interface OrgTreeInterface {
	/**
	 * 
	 * title:�÷������ڵݹ��������еĻ��������ɻ�����
	 * author:chenbing
	 * date:2008-2-19
	 * @return boolean
	 */
	public boolean createTreeForTagXml() ;
	/**
	 * 
	 * title:�÷���������ָ���ĸ�����ID������ָ���ӻ���ID
	 * author:chenbing
	 * date:2008-2-19
	 * @param parentOrgId ������ID
	 * @param orgId �ӻ���ID
	 * @return boolean
	 */
	public boolean addTreeNodeByParentOrg(String parentOrgId,String orgId);
	
	public boolean createTreeForTagXml(Operator operator, List checkList);
	
	/**�������ɹ�ϵ��*/
	public boolean createTreeForVorgRelXml();
}
