package com.fitech.gznx.security;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 用户类
 * 
 * @author jack
 * @serialData 2009-12-14
 */
public class OperatorLead 
{
	private String sessionId = null;

	/**
	 * 所属机构ID
	 */
	private String orgID = null;

	/**
	 * 所属机构名称
	 */
	private String orgName = null;

	/**
	 * 生成的临时文件列表
	 */
	private List tempFileList = new ArrayList();
	
	/**
	 * 生成应用的绝对路径
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
	 * 添加该用户在应用使用过程中产生的临时文件信息
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
	 * 清除用户应用使用过程中产生的临时文件
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
