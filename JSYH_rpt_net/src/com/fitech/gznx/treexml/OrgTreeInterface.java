package com.fitech.gznx.treexml;

import java.util.List;

import com.cbrc.smis.security.Operator;


/**
 * 
 * title: 该接口用于递归生成所有层次的机构树
 * 
 * @author chenbing 2008-2-19
 */
public interface OrgTreeInterface {
	/**
	 * 
	 * title:该方法用于递归生成所有的机构树生成机构树
	 * author:chenbing
	 * date:2008-2-19
	 * @return boolean
	 */
	public boolean createTreeForTagXml() ;
	/**
	 * 
	 * title:该方法用于在指定的父机构ID下增加指定子机构ID
	 * author:chenbing
	 * date:2008-2-19
	 * @param parentOrgId 父机构ID
	 * @param orgId 子机构ID
	 * @return boolean
	 */
	public boolean addTreeNodeByParentOrg(String parentOrgId,String orgId);
	
	public boolean createTreeForTagXml(Operator operator, List checkList);
	
	/**构建生成关系树*/
	public boolean createTreeForVorgRelXml();
}
