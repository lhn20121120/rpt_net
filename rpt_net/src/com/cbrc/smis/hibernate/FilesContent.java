package com.cbrc.smis.hibernate;

import java.io.Serializable;
import java.sql.Blob;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 信息文件发布实体类
 * 
 * @author jcm
 * @serialData 2006-03-20
 */
public class FilesContent implements Serializable{
	
	/**
	 * 文件内容ID
	 */
	private Integer filesContentId = null;

	/**
	 * 文件内容
	 */
	private java.sql.Blob content = null;
	
	private com.cbrc.smis.hibernate.InfoFiles infoFiles;
	
	public FilesContent(){}
	
	public FilesContent(Integer filesContentId){
		this.filesContentId = filesContentId;
	}
	
	public FilesContent(Blob content, Integer id, InfoFiles files) {
		this.content = content;
		this.filesContentId = id;
		this.infoFiles = files;
	}
	
	public java.sql.Blob getContent() {
		return content;
	}

	public void setContent(java.sql.Blob content) {
		this.content = content;
	}

	public Integer getFilesContentId() {
		return filesContentId;
	}

	public void setFilesContentId(Integer filesContentId) {
		this.filesContentId = filesContentId;
	}

	public com.cbrc.smis.hibernate.InfoFiles getInfoFiles() {
		return infoFiles;
	}

	public void setInfoFiles(com.cbrc.smis.hibernate.InfoFiles infoFiles) {
		this.infoFiles = infoFiles;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("filesContentId", this.getFilesContentId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof FilesContent) ) return false;
        FilesContent castOther = (FilesContent) other;
        return new EqualsBuilder()
            .append(this.getFilesContentId(), castOther.getFilesContentId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getFilesContentId())
            .toHashCode();
    }
}
