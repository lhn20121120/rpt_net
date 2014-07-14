package com.fitech.net.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
/**
*
* <p>����: JobLog</p>
*
* <p>����: ��ѯETL������ȡ��־ActionForm </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   ����
* @date     2007-11-15
* @version 1.0
*/
public class JobForm extends ActionForm 
{    
    /**
     * ����Id
     */
    private String  orgId = null;
    
    /**
     * �������
     */
    private String year = null;
    
    /**
     * ��������
     */
    private String term = null;
    
    /**
     * ��������
     */
    private String repName = null;
        
    /**
     * ETL������ȡ״̬
     */
    private String jobSts = null;
    
    /**
     * ��ҳ�������б��ʹ�õ��·�
     */
    private String[] month = null;
    
    public String getRepName()
    {
        return repName;
    }

    public void setRepName(String repName)
    {
        this.repName = repName;
    }

    public String getOrgId()
    {
        return orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }

 
    public String[] getMonth()
    {
        if(month == null){
            month = new String[12];
            for(int i=0; i < 12; i ++){
                month[i] = String.valueOf(i + 1);
            }
        }
        return month;
    }

    public void setMonth(String[] month)
    {
        this.month = month;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        this.year = null;
        this.term = null;
    }

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1)
    {
        return super.validate(arg0, arg1);
    }

    public String getJobSts()
    {
        return jobSts;
    }

    public void setJobSts(String jobSts)
    {
        this.jobSts = jobSts;
    }

    public String getTerm()
    {
        return term;
    }

    public void setTerm(String term)
    {
        this.term = term;
    }

}