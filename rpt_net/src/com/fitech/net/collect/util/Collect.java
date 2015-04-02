/*
 * Created on 2006-5-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.collect.util;

import java.util.ArrayList;

import org.dom4j.Document;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface Collect {
	/**
	 * 汇总程序启动
	 */
	public Document start(ArrayList docList);
	
	
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
