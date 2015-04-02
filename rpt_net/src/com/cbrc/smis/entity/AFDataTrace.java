package com.cbrc.smis.entity;



/**
 * AFDataTrace entity. @author MyEclipse Persistence Tools
 */

public class AFDataTrace  implements java.io.Serializable {


    // Fields    

     private Integer traceId;//主键
     private String cellName;//单元格名称
     private Integer repInId;//报表ID
     private String username;//修改人
     private String dateTime;//日期
     private String originalData;//原始值
     private String changeData;//修改值
     private String finalData;//最终值
     private String descTrace;//备注
     private Integer status;//状态
     private String reportFlag;

    // Constructors

    /** default constructor */
    public AFDataTrace() {
    }

    
    public String getReportFlag() {
		return reportFlag;
	}


	public void setReportFlag(String reportFlag) {
		this.reportFlag = reportFlag;
	}


	/** full constructor */
    public AFDataTrace(String cellName, String templateId, Integer repInId,String username, String date, String originalData, String changeData, String finalData, String desc, Integer status) {
        this.cellName = cellName;
        this.repInId = repInId;
        this.username = username;
        this.dateTime = date;
        this.originalData = originalData;
        this.changeData = changeData;
        this.finalData = finalData;
        this.descTrace = desc;
        this.status = status;
    }

   
    // Property accessors
    
    /***
     * 主键
     */
    public Integer getTraceId() {
        return this.traceId;
    }
    
    /***
     * 主键
     */
    public void setTraceId(Integer traceId) {
        this.traceId = traceId;
    }
    
    /***
     * 单元格名称
     * @return
     */
    public String getCellName() {
        return this.cellName;
    }
    
    /***
     * 单元格名称
     * @param cellName
     */
    public void setCellName(String cellName) {
        this.cellName = cellName;
    }
    
    
    /***
     * 修改人
     * @return
     */
    public String getUsername() {
        return this.username;
    }
    
    /***
     * 修改人
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /***
     * 修改时间
     * @return
     */
    public String getDateTime() {
        return this.dateTime;
    }
    
    /***
     * 修改时间
     * @param date
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    
    /***
     * 原始值
     * @return
     */
    public String getOriginalData() {
        return this.originalData;
    }
    
    /***
     * 原始值
     * @param originalData
     */
    public void setOriginalData(String originalData) {
        this.originalData = originalData;
    }
    
    /***
     * 修改值
     * @return
     */
    public String getChangeData() {
        return this.changeData;
    }
    
    /***
     * 修改值
     * @param changeData
     */
    public void setChangeData(String changeData) {
        this.changeData = changeData;
    }
    
    /***
     * 最终值
     * @return
     */
    public String getFinalData() {
        return this.finalData;
    }
    
    /***
     * 最终值
     * @param finalData
     */
    public void setFinalData(String finalData) {
        this.finalData = finalData;
    }
    
    /***
     * 备注
     * @return
     */
    public String getDescTrace() {
        return this.descTrace;
    }
    
    /***
     * 备注
     * @param desc
     */
    public void setDescTrace(String descTrace) {
        this.descTrace = descTrace;
    }
    
    /***
     * 状态 暂时无任何作用
     * @return
     */
    public Integer getStatus() {
        return this.status;
    }
    
    /***
     * 状态 暂时无任何作用
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /***
     * 报表ID
     * @return
     */
	public Integer getRepInId() {
		return repInId;
	}

	/***
	 * 报表ID
	 * @param repInId
	 */
	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}
   
    







}