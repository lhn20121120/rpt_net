package com.fitech.gznx.security;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * �û���
 * 
 * @author jack
 * @serialData 2009-12-14
 */
public class OperatorLead 
{
	private String sessionId = null;

	/**
	 * ��������ID
	 */
	private String orgID = null;

	/**
	 * ������������
	 */
	private String orgName = null;

	/**
	 * ���ɵ���ʱ�ļ��б�
	 */
	private List tempFileList = new ArrayList();
	
	/**
	 * ����Ӧ�õľ���·��
	 */
	private String contextP = null ;


	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getOrgID()
	{
		return orgID;
	}

	public void setOrgID(String orgID)
	{
		this.orgID = orgID;
	}

	public String getOrgName()
	{
		return orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}


	public List getTempFileList()
	{
		return tempFileList;
	}

	/**
	 * ��Ӹ��û���Ӧ��ʹ�ù����в�������ʱ�ļ���Ϣ
	 * 
	 * @author jack
	 * @date 2007-5-9
	 * @param filePath
	 * 
	 */
	public void addTempFile(String filePath)
	{
		if (filePath != null && !tempFileList.contains(filePath))
			tempFileList.add(filePath);
	}

	/**
	 * ����û�Ӧ��ʹ�ù����в�������ʱ�ļ�
	 * 
	 * @author jack
	 * @date 2007-5-9
	 * @return
	 * 
	 */
	public boolean clearsTempFile()
	{
		boolean result = false;

		if (tempFileList != null && tempFileList.size() != 0)
		{
			for (int i = 0; i < tempFileList.size(); i++)
			{
				String filePath = (String) tempFileList.get(i);
				if (filePath != null && !filePath.equals(""))
				{
					try
					{
						File file = new File(filePath);
						if (file.exists())
							file.delete();
					}
					catch (Exception e)
					{
						result = false;
						e.printStackTrace();
						continue;
					}
				}
				result = true;
			}
		}
		return result;
	}

	public void setTempFileList(List tempFileList)
	{
		this.tempFileList = tempFileList;
	}

	public String getContextP() {
		return contextP;
	}

	public void setContextP(String contextP) {
		this.contextP = contextP;
	}


}
