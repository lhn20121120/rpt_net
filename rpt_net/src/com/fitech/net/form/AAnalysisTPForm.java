package com.fitech.net.form;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * ͳ�Ʊ���ģ����Ϣ��
 *  @author wh 
 *  
 */
public class AAnalysisTPForm  extends ActionForm {

    /** ģ��ID */
    private String ATId;

    /** ģ������ */
    private String ATName;
    
    /** ģ������ID */
	 private Integer analyTypeID;

    /**
     * ����ģ���ϴ��ļ�
     */
    private FormFile templateFile=null;
    
    /**  ����     */
    private Integer curId ;
    
    /** ����ھ� */
    private Integer dataRangeId;
    
    /** �� */
    private Integer year;
    private Integer term;
    
    /** ������� */
    private String orgId;
    
    private String  analyTypeName;
    
	public String getATId()
	{
		return ATId;
	}

	public String getATName()
	{
		return ATName;
	}

	public void setATName(String name)
	{
		ATName = name;
	}

	public void setATId(String id)
	{
		ATId = id;
	}

	public FormFile getTemplateFile()
	{
		return templateFile;
	}

	public void setTemplateFile(FormFile templateFile)
	{
		this.templateFile = templateFile;
	}

	public Integer getCurId()
	{
		return curId;
	}

	public void setCurrId(Integer currId)
	{
		this.curId = currId;
	}

	public Integer getDataRangeId()
	{
		return dataRangeId;
	}

	public void setDataRangeId(Integer dataRangeId)
	{
		this.dataRangeId = dataRangeId;
	}

	public String getOrgId()
	{
		return orgId;
	}

	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}

	public Integer getTerm()
	{
		return term;
	}

	public void setTerm(Integer term)
	{
		this.term = term;
	}

	public Integer getYear()
	{
		return year;
	}

	public void setYear(Integer year)
	{
		this.year = year;
	}


	public void setCurId(Integer curId)
	{
		this.curId = curId;
	}

	public Integer getAnalyTypeID()
	{
		return analyTypeID;
	}

	public void setAnalyTypeID(Integer analyTypeID)
	{
		this.analyTypeID = analyTypeID;
	}

	public String getAnalyTypeName()
	{
		return analyTypeName;
	}

	public void setAnalyTypeName(String analyTypeName)
	{
		this.analyTypeName = analyTypeName;
	}


}
