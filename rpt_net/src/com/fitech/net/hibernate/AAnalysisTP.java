package com.fitech.net.hibernate;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ͳ�Ʊ���ģ����Ϣ��
 *  @author wh 
 *  
 */
public class AAnalysisTP implements Serializable {

    /** ģ��ID */
    private String ATiD;

    /** ģ������ */
    private String ATName;
    
    /** ģ������ */
    private Integer AnalyTypeID;


    /** full constructor */
    public AAnalysisTP(String curId, String ATName) {
        this.ATiD = ATiD;
        this.ATName = ATName;
       
    }

    /** default constructor */
    public AAnalysisTP() {
    }

    public AAnalysisTP(String ATiD) {
    	this.ATiD = ATiD;
    }
   
    public String toString() {
        return new ToStringBuilder(this)
            .append("ATiD", getATiD())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof AAnalysisTP) ) return false;
        AAnalysisTP castOther = (AAnalysisTP) other;
        return new EqualsBuilder()
            .append(this.getATiD(), castOther.getATiD())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getATiD())
            .toHashCode();
    }

	public String getATiD()
	{
		return ATiD;
	}

	public void setATiD(String tiD)
	{
		ATiD = tiD;
	}

	public String getATName()
	{
		return ATName;
	}

	public void setATName(String name)
	{
		ATName = name;
	}

	public Integer getAnalyTypeID()
	{
		return AnalyTypeID;
	}

	public void setAnalyTypeID(Integer analyTypeID)
	{
		AnalyTypeID = analyTypeID;
	}

}
