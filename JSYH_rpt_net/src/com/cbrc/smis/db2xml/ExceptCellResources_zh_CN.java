package com.cbrc.smis.db2xml;

import java.util.ListResourceBundle;

/**
 * 在生成接口文件操作中，合计报表中除外的列信息设置
 * 
 * @author rds
 * @date 2006-01-09
 */
public class ExceptCellResources_zh_CN extends ListResourceBundle {
	/**
	 * 除外的列信息,格式如下：<br>
	 * {"子报表ID+版本号","'除外的列1','除外的列2',..."}
	 */
	private String[][] contents={
		{"G23000510","'A'"},
		{"G24000510","'A'"},
		{"G23000512","'A'"},
		{"G24000512","'A'"},
		{"G23000513","'A'"},
		{"G24000513","'A'"},
		{"G23000514","'A'"},
		{"G24000514","'A'"},
		{"G23000515","'A'"},
		{"G24000515","'A'"},
		{"G23000516","'A'"},
		{"G24000516","'A'"},
		{"G23000610","'A'"},
		{"G24000610","'A'"},
		{"G23000612","'A'"},
		{"G24000612","'A'"},
		{"G23000613","'A'"},
		{"G24000613","'A'"},
		{"G23000614","'A'"},
		{"G24000614","'A'"},
		{"G23000710","'A'"},
		{"G24000710","'A'"},
		{"G23000712","'A'"},
		{"G24000712","'A'"},
		{"G23000713","'A'"},
		{"G24000713","'A'"},
		{"G23000714","'A'"},
		{"G24000714","'A'"}
	};
	
	/**
	 * getContents方法
	 * 
	 * @return Object[][]
	 */
	protected Object[][] getContents() {
		// TODO Auto-generated method stub
		return contents;
	}

}
