package com.fitech.net.collect.bean;

import java.io.Serializable;

/**
*
* <p>Title: CollectReportPK  bean</p>
*
* <p>Description �������bean������</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   ����
* @date     2009-09-11
* @version 1.0
*/

public class CollectReportPK implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**����Id*/
    private  String orgId = null;
     
    /**�ӱ���Id*/
    private String childRepId = null;
    
    /**�汾��*/
    private String versionId = null;
   
    /**���*/
    private Integer year = null;
    
    /**����*/
    private Integer term = null;
    public String getVersionId() {
        return versionId;
    }



    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    
   
    public String getChildRepId() {
        return childRepId;
    }



    public void setChildRepId(String childRepId) {
        this.childRepId = childRepId;
    }



    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }



    public String getOrgId() {
        return orgId;   
    }


    
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public CollectReportPK() {
        super();
        // TODO Auto-generated constructor stub
    }



    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }



    public Integer getTerm() {
        return term;
    }



    public void setTerm(Integer term) {
        this.term = term;
    }



    public Integer getYear() {
        return year;
    }



    public void setYear(Integer year) {
        this.year = year;
    }

}
