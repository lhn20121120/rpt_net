package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cbrc.auth.hibernate.Operator;
/**
 * ��Ϣ�ļ�����ʵ����
 * 
 * @author rds
 * @serialData 2005-12-17 18:10
 */
public class InfoFiles implements Serializable{
	/**
	 * ���
	 */
	private Integer infoFilesId=null;

	/**
	 * �ļ���
	 */
	private String infoFileName=null;
	/**
	 * �ļ�����
	 */
	private String infoFileType=null;
	/**
	 * �ļ���С
	 */
	private Integer infoFileSize=null;
	/**
	 * �ļ�λ��
	 */
	private String infoFileLocation=null;
	/**
	 * �ļ�����
	 */
	private String infoFileDesc=null;
	/**
	 * ��Ϣ���
	 */
	private String infoFileStyle=null;
	/**
	 * ��¼ʱ��
	 */
	private Date recordTime=null;
	
	/**
	 * �û�
	 */
	private Operator operator=null;
	
	/**
	 * �ļ���ϸ����
	 */
	private Set filesContents = null;

	
	public InfoFiles(){}
	
	public InfoFiles(Integer infoFilesId){
		this.infoFilesId=infoFilesId;
	}
	
	public InfoFiles(Integer infoFilesId,String infoFileName,String infoFileType,Integer infoFileSize,String infoFileLocation
			,String infoFileDesc,String infoFileStyle,Operator operator,Date recordTime,Set filesContents){
		this.infoFilesId=infoFilesId;
		this.infoFileName=infoFileName;
		this.infoFileType=infoFileType;
		this.infoFileSize=infoFileSize;
		this.infoFileLocation=infoFileLocation;
		this.infoFileDesc=infoFileDesc;
		this.infoFileStyle=infoFileStyle;
		this.operator=operator;
		this.recordTime=recordTime;
		this.filesContents=filesContents;
	}
	
	public String getInfoFileDesc() {
		return infoFileDesc;
	}
	public void setInfoFileDesc(String infoFileDesc) {
		this.infoFileDesc = infoFileDesc;
	}
	public String getInfoFileLocation() {
		return infoFileLocation;
	}
	public void setInfoFileLocation(String infoFileLocation) {
		this.infoFileLocation = infoFileLocation;
	}
	public String getInfoFileName() {
		return infoFileName;
	}
	public void setInfoFileName(String infoFileName) {
		this.infoFileName = infoFileName;
	}
	public Integer getInfoFilesId() {
		return infoFilesId;
	}
	public void setInfoFilesId(Integer infoFilesId) {
		this.infoFilesId = infoFilesId;
	}
	public Integer getInfoFileSize() {
		return infoFileSize;
	}
	public void setInfoFileSize(Integer infoFileSize) {
		this.infoFileSize = infoFileSize;
	}
	public String getInfoFileStyle() {
		return infoFileStyle;
	}
	public void setInfoFileStyle(String infoFileStyle) {
		this.infoFileStyle = infoFileStyle;
	}
	public String getInfoFileType() {
		return infoFileType;
	}
	public void setInfoFileType(String infoFileType) {
		this.infoFileType = infoFileType;
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
	public String toString() {
        return new ToStringBuilder(this)
            .append("infoFilesId", getInfoFilesId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof InfoFiles) ) return false;
        InfoFiles castOther = (InfoFiles) other;
        return new EqualsBuilder()
            .append(this.getInfoFilesId(), castOther.getInfoFilesId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getInfoFilesId())
            .toHashCode();
    }

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	
	public Set getFilesContents() {
		return filesContents;
	}

	public void setFilesContents(Set filesContents) {
		this.filesContents = filesContents;
	}
}
