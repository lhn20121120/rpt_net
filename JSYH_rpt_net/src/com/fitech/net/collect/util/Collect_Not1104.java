package com.fitech.net.collect.util;


public interface Collect_Not1104 {
	/**
	 * 汇总程序启动
	 */
	public boolean start(String childRepId,String versionId,String ids,Integer curOrgId);
	
	/**	 
	 *汇总程序停止，释放资源
	 */
	public void stop();
	
	/**
	 * 返回本次处理的日志
	 * @return 日志对象
	 */
	public Object getLogs();

}
