package com.fitech.net.Excel2Xml;

public class Name {
	//����ID
	private String repId;
	//���ݷ�Χid
	private String rangId;
	//ʱ��yyyymm
	private String time;
	//����id
	private String orgCode;
	//����xml�ļ���
	private String xmlName;
	//�汾��
	private String versionid;
	//·��
	private String path;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getVersionid() {
		return versionid;
	}
	public void setVersionid(String versionid) {
		this.versionid = versionid;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getRangId() {
		return rangId;
	}
	public void setRangId(String rangId) {
		this.rangId = rangId;
	}
	public String getRepId() {
		return repId;
	}
	public void setRepId(String repId) {
		this.repId = repId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getXmlName() {
		//return getPath()+System.getProperty("file.separator")+getOrgCode()+getRepId()+getVersionid()+getRangId()+getTime()+".xml";
		return getPath()+System.getProperty("file.separator")+xmlName+".xml";
	}
	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}

}
