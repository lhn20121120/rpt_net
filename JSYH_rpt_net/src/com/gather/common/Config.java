/*
 * Created on 2005-5-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gather.common;

/**
 * @author rds
 * 
 * ������Ϣ������
 */
public class Config {
	/**
	 * ϵͳ��file.separator
	 */
	public static String FILESEPARATOR = System.getProperty("file.separator");
	
	/**
	 * zip���е�xml�ļ���
	 */
	public static final String REFER_FILE_XML_NAME = "listing.xml";
	/**
	 * �ļ������Ŀ¼
	 */
	public static final String FILE_ROOT = "data";
	/**
	 * �ļ����ձ����Ŀ¼
	 */
	public static final String FILE_RESULT = FILE_ROOT+FILESEPARATOR+"result";
	/**
	 * �����õ�Ŀ¼
	 */
	public static final String DOWNLOAD = FILE_ROOT+FILESEPARATOR+"down";
	/**
	 * ����ģ���õ�Ŀ¼
	 */
	public static final String 	DOWNLOAD_REPORT = DOWNLOAD+FILESEPARATOR+"report";
	/**
	 * ��Դ�����õ�Ŀ¼
	 */
	public static final String 	DOWNLOAD_RESOURCE = DOWNLOAD+FILESEPARATOR+"resource";
	/**
	 * �ϴ��õ�Ŀ¼
	 */
	public static final String REFER = FILE_ROOT+FILESEPARATOR+"refer";
    /**
     * �ϴ������õ�Ŀ¼
     */
	public static final String REFER_REPORT = REFER+FILESEPARATOR+"report";
    /**
     * �ϴ��ļ��õ�Ŀ¼
     */
	public static final String REFER_FILE = REFER+FILESEPARATOR+"file";

	
	/**
	 * �����ɼ�ϵͳ����
	 */
	public static String OUTER_SYSTEM_NAME = "gather";
	
	/**
	 * ģ������zip�ļ���ʱ�ļ���
	 */
	public static String DOWNLOAD_DIR = "data";

	/**
	 * ϵͳ����Global Forward
	 */
	public static final String SYSTEM_ERROR_FORWARD = "systemError";

	/**
	 * ����Ա��¼��,������Ա���󱣴浽Session�е�����
	 */
	public static final String OPERATOR_SESSION_ATTRIBUTE_NAME = "Operator";

	/**
	 * ϵͳ�û���¼Global Forward
	 */
	public static final String LOGON_FORWARD = "logon";

	/**
	 * ���ظ��ͻ��˵ļ�¼��Request��Session�е�Attribute������
	 */
	public static final String RECORDS_ATTRIBUTE_NAME = "Records";

	/**
	 * MESSAGE��Request��Session��Attribute������
	 */
	public static final String MESSAGE_ATTRIBUTE_NAME = "Messages";

	/**
	 * ApartGage��Request��Session��Attribute������
	 */
	public static final String APARTPAGE_ATTRIBUTE_NAME = "ApartPage";



	/**
	 * ÿҳ��ʾ�ļ�¼��
	 */
	public static final int PER_PAGE_ROWS = 8;

	/**
	 * ��־����(���)
	 */
	public static final int LOG_TYPE_ADD = 1;

	/**
	 * ��־����(�޸�)
	 */
	public static final int LOG_TYPE_EDIT = 2;

	/**
	 * ��־����(ɾ��)
	 */
	public static final int LOG_TYPE_DEL = 3;

	/**
	 * ��־����(��¼)
	 */
	public static final int LOG_TYPE_LOGIN = 4;

	/**
	 * ��־����(�˳�)
	 */
	public static final int LOG_TYPE_EXIT = 5;

	/**
	 * @Ƶ�� ����
	 */
	public static final int FREQUENCY_MONTH = 1;

	/**
	 * @Ƶ�� ����
	 */
	public static final int FREQUENCY_SEASON = 2;

	/**
	 * @Ƶ�� ������
	 */
	public static final int FREQUENCY_HALF_YEAR = 3;

	/**
	 * @Ƶ�� ����
	 */
	public static final int FREQUENCY_YEAR = 4;
	
	/**
	 * @���ݷ�Χ :����
	 */
	public static final int CORPORATION = 1;
	/**
	 * @���ݷ�Χ������
	 */
	public static final int REALM_IN = 2;
	/**
	 * @���ݷ�Χ������
	 */
	public static final int REALM_OUT = 3;
	/**
	 * ��������id
	 */
	public static final int WORK_TYPE_ID = 1;
	
	/**
	 * ��־��𣺵�¼
	 */
	
	public static final int LOG_LOGIN = 1;
	/**
	 * ��־����˳�
	 */
	public static final int LOG_LOGOUT = 2;
	/**
	 * ��־���ģ������
	 */
	public static final int LOG_DOWN_REPORT = 3;
	/**
	 * ��־��������ϱ�
	 */
	public static final int LOG_REFER_REPORT = 4;
	/**
	 * ��־����ϴ��ļ�
	 */
	public static final int LOG_REFER_FILE = 5;
}
