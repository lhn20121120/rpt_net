package com.cbrc.smis.proc.impl;

/**
 * �����У��ӿ�
 * 
 * @author rds
 * @serialData 2005-12-17 0:01
 */
public interface Report {
	/**
	 * ����У���ϵ���ʽ
	 */
	public static final int FORMU_TYPE_BN=1;
	/**
	 * ���У���ϵ���ʽ
	 */
	public static final int FORMU_TYPE_BJ=2;
	/**
	 * ���ڼ�����ʽ
	 */
	public static final int FORMU_TYPE_BN_JS=3;
	/**
	 * ��������ʽ
	 */
	public static final int FORMU_TYPE_BJ_JS=4;	
	/**
	 * ��Ե�ʽ����
	 */
	public static final int REPORT_STYLE_DD=1;
	/**
	 * �嵥ʽ����
	 */
	public static final int REPORT_STYLE_QD=2;
	/**
	 * У��ͨ��
	 */
	public static final Integer RESULT_OK=new Integer(1);
	/**
	 * У�鲻ͨ��
	 */
	public static final Integer RESULT_NO=new Integer(-1);
	/**
	 * ����δ��ȫ
	 */
	public static final Integer REPORT_CANCLE=new Integer(0);
	/**
	 * �쳣�仯�����ϱ�׼ȷ
	 */
	public static final int ABNORMITY_CHANGE_FLAG_NO=-1;
	/**
	 * �쳣�仯���ϱ�׼ȷ
	 */
	public static final int ABNORMITY_CHANGE_FLAG_OK=1;	
	/**
	 * ����У���ʶ
	 */
	public static final int VALIDATE_TYPE_BN=1;
	/**
	 * ���У���ʶ
	 */
	public static final int VALIDATE_TYPE_BJ=2;
	/**
	 * ����ID-�����
	 */
	public static final int CURR_YAN_ID=1;
	/**
	 * �������ͨ����ʾ
	 */
	public static final int CHECK_FLAG_OK=1;
	/**
	 * ������˲�ͨ����ʾ
	 */
	public static final int CHECK_FLAG_NO=-1;
	/**
	 * ����δ��˱�ʾ
	 */
	public static final int CHECK_FLAG_UN=0;
}