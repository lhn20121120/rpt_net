package com.fitech.net.collect_not1104;

import com.fitech.net.collect.util.CollectUtil;
import com.fitech.net.collect.util.Collect_Not1104;

public class P2P_add_collect implements Collect_Not1104{
	
	public boolean start(String childRepId,String versionId,String ids,Integer repInId)
	{
		boolean flag=false;
//		PreCollect pc=new PreCollect();
//		String ids="";
//		ids=pc.getNeededReportsIds(childRepId,versionId,dataRangeId,year,month,childOrgIds);
//		String orgId="";
//		if(operator!=null){
//			orgId=operator.getOrgId();
//		}
		
		flag=CollectUtil.p2p_add_collect(childRepId,versionId,ids,repInId);
		return flag;
	}
	
	/**	 
	 *���ܳ���ֹͣ���ͷ���Դ
	 */
	public void stop()
	{}
	
	/**
	 * ���ر��δ������־
	 * @return ��־����
	 */
	public Object getLogs()
	{
		return new Object();
	}

}
