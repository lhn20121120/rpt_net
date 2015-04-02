package com.fitech.net.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
/**
*
* <p>标题: JobLog</p>
*
* <p>描述: ETL数据提取日志记录对象Hibenate主键 </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2007-11-15
* @version 1.0
*/
public class JobLogKey implements Serializable
{

    /**
     * 报表Id
     */
    private String repId = null;
    
    /**
     * 版本号
     */
    private String versionId = null;
      
    /**
     * 报表年份
     */
    private Integer year = null;
    
    /**
     * 报表期数
     */
    private Integer term = null;
    
    /**
     * 上报机构
     */
    private OrgNet org = null;
    
    private MCurr cur;
    private MDataRgType dataRange;
    

	/**
	 * 返回 cur
	 */
	public MCurr getCur() {
		return cur;
	}

	/**
	 * 参数：cur 
	 * 设置 cur
	 */
	public void setCur(MCurr cur) {
		this.cur = cur;
	}

	/**
	 * 返回 dataRange
	 */
	public MDataRgType getDataRange() {
		return dataRange;
	}

	/**
	 * 参数：dataRange 
	 * 设置 dataRange
	 */
	public void setDataRange(MDataRgType dataRange) {
		this.dataRange = dataRange;
	}

	public JobLogKey()
    {
    }

    public OrgNet getOrg()
    {
        return org;
    }

    public void setOrg(OrgNet org)
    {
        this.org = org;
    }

    public String getRepId()
    {
        return repId;
    }

    public void setRepId(String repId)
    {
        this.repId = repId;
    }

    public Integer getTerm()
    {
        return term;
    }

    public void setTerm(Integer term)
    {
        this.term = term;
    }

    public String getVersionId()
    {
        return versionId;
    }

    public void setVersionId(String versionId)
    {
        this.versionId = versionId;
    }

    public Integer getYear()
    {
        return year;
    }

    public void setYear(Integer year)
    {
        this.year = year;
    }

    public boolean equals(Object obj)
    {
       if(!(obj instanceof JobLogKey)) return false;
       JobLogKey other = (JobLogKey) obj;
       return new EqualsBuilder()
               .append(this.getRepId(),other.getRepId())
               .append(this.getVersionId(),other.getVersionId())
               .append(this.getOrg(),other.getOrg())
               .append(this.getYear(),other.getYear())
               .append(this.getTerm(),other.getTerm())
               .isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder()
                .append(this.repId)
                .append(this.versionId)
                .append(this.org)
                .append(this.year)
                .append(this.term).toHashCode();
    }

}
