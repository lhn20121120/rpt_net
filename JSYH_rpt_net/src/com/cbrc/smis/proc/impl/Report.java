package com.cbrc.smis.proc.impl;

/**
 * 报表的校验接口
 * 
 * @author rds
 * @serialData 2005-12-17 0:01
 */
public interface Report {
	/**
	 * 表内校验关系表达式
	 */
	public static final int FORMU_TYPE_BN=1;
	/**
	 * 表间校验关系表达式
	 */
	public static final int FORMU_TYPE_BJ=2;
	/**
	 * 表内计算表达式
	 */
	public static final int FORMU_TYPE_BN_JS=3;
	/**
	 * 表间计算表达式
	 */
	public static final int FORMU_TYPE_BJ_JS=4;	
	/**
	 * 点对点式报表
	 */
	public static final int REPORT_STYLE_DD=1;
	/**
	 * 清单式报表
	 */
	public static final int REPORT_STYLE_QD=2;
	/**
	 * 校验通过
	 */
	public static final Integer RESULT_OK=new Integer(1);
	/**
	 * 校验不通过
	 */
	public static final Integer RESULT_NO=new Integer(-1);
	/**
	 * 报表未齐全
	 */
	public static final Integer REPORT_CANCLE=new Integer(0);
	/**
	 * 异常变化不符合标准确
	 */
	public static final int ABNORMITY_CHANGE_FLAG_NO=-1;
	/**
	 * 异常变化符合标准确
	 */
	public static final int ABNORMITY_CHANGE_FLAG_OK=1;	
	/**
	 * 表内校验标识
	 */
	public static final int VALIDATE_TYPE_BN=1;
	/**
	 * 表间校验标识
	 */
	public static final int VALIDATE_TYPE_BJ=2;
	/**
	 * 币种ID-人民币
	 */
	public static final int CURR_YAN_ID=1;
	/**
	 * 报表审核通过标示
	 */
	public static final int CHECK_FLAG_OK=1;
	/**
	 * 报表审核不通过标示
	 */
	public static final int CHECK_FLAG_NO=-1;
	/**
	 * 报表未审核标示
	 */
	public static final int CHECK_FLAG_UN=0;
}