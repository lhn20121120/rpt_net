package com.fitech.net.hibernate;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;

/**
*
* <p>����: JobLog</p>
*
* <p>����: ETL������ȡ��־��¼���� </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   ����
* @date     2007-11-15
* @version 1.0
*/
public class JobLog
{

    /**
     * JobLog��������
     */
    private JobLogKey id = null;
    
    /**
     * ��������
     */
    private String repName = null;
    
    /**
     * ETL������ȡ״̬
     */
    private String jobSts = null;
    
    /**
     * ETL������ȡʱ��
     */
    private String actStTm = null;
    
    /**
     * ETL������ȡ����ʱ��
     */
    private String actEndTm = null;
    
    /**
     * ETL������ȡ��־
     */
    private String jobLog = null;
    
    
    
    /**
     * ETL������ȡ״̬�޸ĺ�ת�����ѯ�Ĳ���URL
     */
    private String paramURL = "";
    
    public String getParamURL()
    {
        paramURL = "";
        if(id != null)
        {            
            paramURL += "&sRepId=" + id.getRepId() + "&sVersionId=" +id.getVersionId();
            paramURL += "&sYear=" + id.getYear() + "&sTerm=" + id.getTerm();
            if(null != id.getOrg())
            {
                paramURL += "&sOrgId=" + id.getOrg().getOrgId();
            }
            if(null != id.getDataRange())
            {
                paramURL += "&sDataRangId=" + id.getDataRange().getDataRangeId();
            }
            if(null != id.getCur())
            {
                paramURL += "&sCurId=" + id.getCur().getCurId();
            }
        }
        return paramURL;
    }


    public JobLog()
    {
        super();
        // TODO Auto-generated constructor stub
    }
   

    public String getActEndTm()
    {
        return actEndTm;
    }

    public void setActEndTm(String actEndTm)
    {
        this.actEndTm = actEndTm;
    }

    public String getActStTm()
    {
        return actStTm;
    }

    public void setActStTm(String actStTm)
    {
        this.actStTm = actStTm;
    }

    public JobLogKey getId()
    {
        return id;
    }

    public void setId(JobLogKey id)
    {
        this.id = id;
    }

    public String getJobLog()
    {
        return jobLog;
    }

    public void setJobLog(String jobLog)
    {
        this.jobLog = jobLog;
    }

    public String getJobSts()
    {
        return jobSts;
    }

    public void setJobSts(String jobSts)
    {
        this.jobSts = jobSts;
    }

    public String getRepName()
    {
        return repName;
    }

    public void setRepName(String repName)
    {
        this.repName = repName;
    }
    
    public boolean equals(Object obj)
    {
        if(!(obj instanceof JobLog)) return false;
        
        JobLog other = (JobLog) obj;
        
        return new EqualsBuilder()
                    .append(this.getId(),other.getId()).isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder()
                .append(this.getId()).toHashCode();
    }


}
