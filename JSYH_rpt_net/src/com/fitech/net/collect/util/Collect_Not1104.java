package com.fitech.net.collect.util;


public interface Collect_Not1104 {
	/**
	 * ���ܳ�������
	 */
	public boolean start(String childRepId,String versionId,String ids,Integer curOrgId);
	
	/**	 
	 *���ܳ���ֹͣ���ͷ���Դ
	 */
	public void stop();
	
	/**
	 * ���ر��δ������־
	 * @return ��־����
	 */
	public Object getLogs();

}
