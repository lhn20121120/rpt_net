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
	 * ���ܳ�������
	 */
	public Document start(ArrayList docList);
	
	
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
