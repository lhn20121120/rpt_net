package com.cbrc.smis.other;

import com.cbrc.smis.common.Config;
/**
 * ���ڱ��У���ϵ���ʽ��
 * 
 * @author rds
 * @serialData 2005-12-09
 */
public class Expression {
	/**
	 * ����У���ʶ
	 */
	public static Integer FLAG_BL=Config.CELL_CHECK_INNER;
	/**
	 * ���У���ʶ
	 */
	public static Integer FLAG_BJ=Config.CELL_CHECK_BETWEEN;
	/**
	 * ��Ƶ��У���ʶ
	 */
	public static Integer FLAG_KP=Config.CELL_CHECK_FREQ;
	/**
	 * ���㹫ʽ��ʶ
	 */
	public static Integer FLAG_JS=new Integer(3);
	
	/**
	 * ���ʽ
	 */
	private String content=null;
	/**
	 * ���ʽ����
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
