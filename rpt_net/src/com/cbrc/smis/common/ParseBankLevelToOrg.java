package com.cbrc.smis.common;

import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;

public class ParseBankLevelToOrg {

	/**
	 * �����м�����
	 * @param bankLevelName �м�����
	 * @param orgId ��ǰ����ID
	 * @return
	 */
	public static String parseBankLevel(String bankLevelName,String orgId){
		String orgIds = "";
		
		if(bankLevelName.equals("����")){			
			orgIds = "'" + orgId + "'";
		}else if(bankLevelName.equals("����һ������")){
			orgIds = StrutsOrgNetDelegate.selectSubOrgIds(orgId);
		}else if(bankLevelName.equals("������������")){
			orgIds = StrutsOrgNetDelegate.selectAllLowerOrgIds(orgId);
		}
		return orgIds;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����AfOrg
	 * �����м����壨ȡaforg��֧��������������
	 * @param bankLevelName �м�����
	 * @param orgId ��ǰ����ID
	 * @return
	 */
	public static String parseBankLevelNew(String bankLevelName,String orgId){
		String orgIds = "";
		
		if(bankLevelName.equals("����")){			
			orgIds = "'" + orgId + "'";
		}else if(bankLevelName.equals("����һ������")){
			/** ��ʹ��hibernate ���Ը� 2011-12-21
			 * Ӱ�����AfOrg**/
			orgIds = AFOrgDelegate.selectSubOrgIds(orgId);
		}else if(bankLevelName.equals("������������")){
			/**��ʹ��hibernate ���Ը� 2011-12-21
			 * Ӱ�����AfOrg*/
			orgIds = AFOrgDelegate.selectAllLowerOrgIds(orgId);
		}
		return orgIds;
	}
}
