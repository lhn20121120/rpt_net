package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class TargetDefineForm extends ActionForm {

	/** The composite primary key value. */	
	private java.lang.Integer targetDefineId=null;

	/** The value of the simple defineName property. */	
	private java.lang.String defineName=null;
	
	/** The value of the simple version property. */	
	private java.lang.String version=null;

	/** The value of the simple startDate property. */	
	private java.lang.String startDate=null;

	/** The value of the simple endDate property. */	
	private java.lang.String endDate=null;

	/** The value of the simple formula property. */	
	private java.lang.String formula=null;

	/** The value of the simple law property. */	    
	private java.lang.String law=null;

	/** The value of the simple des property. */	
	private java.lang.String des=null;

	/** The value of the simple preStandard property. */	
	private java.lang.String preStandard=null;

	/** The value of the simple yearStandard property. */	
	private java.lang.String yearStandard=null;

	/** The value of the simple seasonStandard property. */	
	private java.lang.String seasonStandard=null;

	/** The value of the simple warn property. */	
	private java.lang.String warn=null;
    
	private String curPage=null;
	
	private String normalId=null;
	
	private String  businessId=null;
	
	private String normalName=null;
	
	private String businessName=null;
	    
	/** 指标的建立机构**/	
	private String setOrgId = null;
	private String orgId;
	    	
	public String getCurPage() {	
		return curPage;	
	}
		
	public void setCurPage(String curPage) {	
		this.curPage = curPage;		
	}

	public String getBusinessName() {	
		return businessName;
	}

	public void setBusinessName(String businessName) {	
		this.businessName = businessName;		
	}

	public String getNormalName() {
		return normalName;		
	}

	public void setNormalName(String normalName) {	
		this.normalName = normalName;		
	}

	public String getBusinessId() {	
		return businessId;		
	}

	public void setBusinessId(String businessId) {	
		this.businessId = businessId;		
	}

	public String getNormalId() {	
		return normalId;		
	}

	public void setNormalId(String normalId) {	
		this.normalId = normalId;	
	}
	
	public TargetDefineForm(){}
	
	/**
	 * Constructor of AbstractTargetDefine instances given a simple primary key.
	 * @param targetDefineId
	 */	
	public TargetDefineForm(java.lang.Integer targetDefineId) {	    	
		this.setTargetDefineId(targetDefineId);	    
	}

	/**
     * Return the simple primary key value that identifies this object.
     * @return java.lang.Integer
     */	    
	public java.lang.Integer getTargetDefineId(){	
		return targetDefineId;	    
	}
  
	/**
     * Set the simple primary key value that identifies this object.
     * @param targetDefineId
     */    
	public void setTargetDefineId(java.lang.Integer targetDefineId)	{	        
		this.targetDefineId = targetDefineId;	    
	}
	    
	/**
     * Return the value of the DEFINE_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getDefineName(){
    	if(this.defineName!=null) return this.defineName.trim();
    	return this.defineName;
    }

    /**
     * Set the value of the DEFINE_NAME column.
     * @param defineName
     */
    public void setDefineName(java.lang.String defineName){
        this.defineName = defineName;
    }

    /**
     * Return the value of the START_DATE column.
     * @return java.lang.String
     */
    public java.lang.String getStartDate(){
        return this.startDate;
    }

    /**
     * Set the value of the START_DATE column.
     * @param startDate
     */
    public void setStartDate(java.lang.String startDate){
        this.startDate = startDate;
    }

    /**
     * Return the value of the END_DATE column.
     * @return java.lang.String
     */
    public java.lang.String getEndDate(){
        return this.endDate;
    }

    /**
     * Set the value of the END_DATE column.
     * @param endDate
     */
    public void setEndDate(java.lang.String endDate){
        this.endDate = endDate;
    }

    /**
     * Return the value of the FORMULA column.
     * @return java.lang.String
     */
    public java.lang.String getFormula(){
        return this.formula;
    }

    /**
     * Set the value of the FORMULA column.
     * @param formula
     */
    public void setFormula(java.lang.String formula){
        this.formula = formula;
    }

    /**
     * Return the value of the LAW column.
     * @return java.lang.String
     */
    public java.lang.String getLaw(){
        return this.law;
    }

    /**
     * Set the value of the LAW column.
     * @param law
     */
    public void setLaw(java.lang.String law){
        this.law = law;
    }

    /**
     * Return the value of the DES column.
     * @return java.lang.String
     */
    public java.lang.String getDes(){
        return this.des;
    }

    /**
     * Set the value of the DES column.
     * @param des
     */
    public void setDes(java.lang.String des){
        this.des = des;
    }

    /**
     * Return the value of the PRE_STANDARD column.
     * @return java.lang.String
     */
    public java.lang.String getPreStandard(){
        return this.preStandard;
    }

    /**
     * Set the value of the PRE_STANDARD column.
     * @param preStandard
     */
    public void setPreStandard(java.lang.String preStandard){
        this.preStandard = preStandard;
    }

    /**
     * Return the value of the YEAR_STANDARD column.
     * @return java.lang.String
     */
    public java.lang.String getYearStandard(){
        return this.yearStandard;
    }

    /**
     * Set the value of the YEAR_STANDARD column.
     * @param yearStandard
     */
    public void setYearStandard(java.lang.String yearStandard){
        this.yearStandard = yearStandard;
    }

    /**
     * Return the value of the SEASON_STANDARD column.
     * @return java.lang.String
     */
    public java.lang.String getSeasonStandard(){
        return this.seasonStandard;
    }

    /**
     * Set the value of the SEASON_STANDARD column.
     * @param seasonStandard
     */
    public void setSeasonStandard(java.lang.String seasonStandard){
        this.seasonStandard = seasonStandard;
    }

    /**
     * Return the value of the WARN column.
     * @return java.lang.String
     */
    public java.lang.String getWarn(){
        return this.warn;
    }

    /**
     * Set the value of the WARN column.
     * @param warn
     */
    public void setWarn(java.lang.String warn){
        this.warn = warn;
    }

	/**
	 * @return Returns the orgId.
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @return Returns the setOrgId.
	 */
	public String getSetOrgId() {
		return setOrgId;
	}

	/**
	 * @param setOrgId The setOrgId to set.
	 */
	public void setSetOrgId(String setOrgId) {
		this.setOrgId = setOrgId;
	}

	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public java.lang.String getVersion() {
		return version;
	}

	public void setVersion(java.lang.String version) {
		this.version = version;
	}
}