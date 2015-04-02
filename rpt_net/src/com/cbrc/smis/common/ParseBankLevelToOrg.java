package com.cbrc.smis.common;

import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;

public class ParseBankLevelToOrg {

	/**
	 * 解析行级定义
	 * @param bankLevelName 行级名称
	 * @param orgId 当前机构ID
	 * @return
	 */
	public static String parseBankLevel(String bankLevelName,String orgId){
		String orgIds = "";
		
		if(bankLevelName.equals("本行")){			
			orgIds = "'" + orgId + "'";
		}else if(bankLevelName.equals("下属一级子行")){
			orgIds = StrutsOrgNetDelegate.selectSubOrgIds(orgId);
		}else if(bankLevelName.equals("下属所有子行")){
			orgIds = StrutsOrgNetDelegate.selectAllLowerOrgIds(orgId);
		}
		return orgIds;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-28
	 * 影响对象：AfOrg
	 * 解析行级定义（取aforg，支持添加虚拟机构）
	 * @param bankLevelName 行级名称
	 * @param orgId 当前机构ID
	 * @return
	 */
	public static String parseBankLevelNew(String bankLevelName,String orgId){
		String orgIds = "";
		
		if(bankLevelName.equals("本行")){			
			orgIds = "'" + orgId + "'";
		}else if(bankLevelName.equals("下属一级子行")){
			/** 已使用hibernate 卞以刚 2011-12-21
			 * 影响对象：AfOrg**/
			orgIds = AFOrgDelegate.selectSubOrgIds(orgId);
		}else if(bankLevelName.equals("下属所有子行")){
			/**已使用hibernate 卞以刚 2011-12-21
			 * 影响对象：AfOrg*/
			orgIds = AFOrgDelegate.selectAllLowerOrgIds(orgId);
		}
		return orgIds;
	}
}
