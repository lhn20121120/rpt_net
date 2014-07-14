package com.cbrc.smis.other;

import com.cbrc.smis.common.Config;
/**
 * 表内表间校验关系表达式类
 * 
 * @author rds
 * @serialData 2005-12-09
 */
public class Expression {
	/**
	 * 表内校验标识
	 */
	public static Integer FLAG_BL=Config.CELL_CHECK_INNER;
	/**
	 * 表间校验标识
	 */
	public static Integer FLAG_BJ=Config.CELL_CHECK_BETWEEN;
	/**
	 * 跨频度校验标识
	 */
	public static Integer FLAG_KP=Config.CELL_CHECK_FREQ;
	/**
	 * 计算公式标识
	 */
	public static Integer FLAG_JS=new Integer(3);
	
	/**
	 * 表达式
	 */
	private String content=null;
	/**
	 * 表达式类型
	 */
	private Integer type=null;
	
	public Expression(){}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
