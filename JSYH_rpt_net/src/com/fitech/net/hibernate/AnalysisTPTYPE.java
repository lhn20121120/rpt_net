package com.fitech.net.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 统计报表模板信息表
 *  @author wh 
 *  
 */
public class AnalysisTPTYPE implements Serializable {

    /** 模板类型ID */
	 private Integer AnalyTypeID;

    /** 模板类型名称 */
    private String AnalyTypeName;
  


    /** full constructor */
    public AnalysisTPTYPE(String AnalyTypeName, Integer AnalyTypeID) {
        this.AnalyTypeName = AnalyTypeName;
        this.AnalyTypeID = AnalyTypeID;
       
    }

    /** default constructor */
    public AnalysisTPTYPE() {
    }

    public AnalysisTPTYPE(Integer AnalyTypeID) {
    	this.AnalyTypeID = AnalyTypeID;
    }
   
    public String toString() {
        return new ToStringBuilder(this)
            .append("ATiD", getAnalyTypeID())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof AnalysisTPTYPE) ) return false;
        AnalysisTPTYPE castOther = (AnalysisTPTYPE) other;
        return new EqualsBuilder()
            .append(this.getAnalyTypeID(), castOther.getAnalyTypeID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getAnalyTypeID())
            .toHashCode();
    }


	public Integer getAnalyTypeID()
	{
		return AnalyTypeID;
	}

	public void setAnalyTypeID(Integer analyTypeID)
	{
		AnalyTypeID = analyTypeID;
	}

	public String getAnalyTypeName()
	{
		return AnalyTypeName;
	}

	public void setAnalyTypeName(String analyTypeName)
	{
		AnalyTypeName = analyTypeName;
	}

}
