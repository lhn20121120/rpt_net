package com.cbrc.smis.entity;



/**
 * AFDataTrace entity. @author MyEclipse Persistence Tools
 */

public class AFDataTrace  implements java.io.Serializable {


    // Fields    

     private Integer traceId;//����
     private String cellName;//��Ԫ������
     private Integer repInId;//����ID
     private String username;//�޸���
     private String dateTime;//����
     private String originalData;//ԭʼֵ
     private String changeData;//�޸�ֵ
     private String finalData;//����ֵ
     private String descTrace;//��ע
     private Integer status;//״̬
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
     * ����
     */
    public Integer getTraceId() {
        return this.traceId;
    }
    
    /***
     * ����
     */
    public void setTraceId(Integer traceId) {
        this.traceId = traceId;
    }
    
    /***
     * ��Ԫ������
     * @return
     */
    public String getCellName() {
        return this.cellName;
    }
    
    /***
     * ��Ԫ������
     * @param cellName
     */
    public void setCellName(String cellName) {
        this.cellName = cellName;
    }
    
    
    /***
     * �޸���
     * @return
     */
    public String getUsername() {
        return this.username;
    }
    
    /***
     * �޸���
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /***
     * �޸�ʱ��
     * @return
     */
    public String getDateTime() {
        return this.dateTime;
    }
    
    /***
     * �޸�ʱ��
     * @param date
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    
    /***
     * ԭʼֵ
     * @return
     */
    public String getOriginalData() {
        return this.originalData;
    }
    
    /***
     * ԭʼֵ
     * @param originalData
     */
    public void setOriginalData(String originalData) {
        this.originalData = originalData;
    }
    
    /***
     * �޸�ֵ
     * @return
     */
    public String getChangeData() {
        return this.changeData;
    }
    
    /***
     * �޸�ֵ
     * @param changeData
     */
    public void setChangeData(String changeData) {
        this.changeData = changeData;
    }
    
    /***
     * ����ֵ
     * @return
     */
    public String getFinalData() {
        return this.finalData;
    }
    
    /***
     * ����ֵ
     * @param finalData
     */
    public void setFinalData(String finalData) {
        this.finalData = finalData;
    }
    
    /***
     * ��ע
     * @return
     */
    public String getDescTrace() {
        return this.descTrace;
    }
    
    /***
     * ��ע
     * @param desc
     */
    public void setDescTrace(String descTrace) {
        this.descTrace = descTrace;
    }
    
    /***
     * ״̬ ��ʱ���κ�����
     * @return
     */
    public Integer getStatus() {
        return this.status;
    }
    
    /***
     * ״̬ ��ʱ���κ�����
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /***
     * ����ID
     * @return
     */
	public Integer getRepInId() {
		return repInId;
	}

	/***
	 * ����ID
	 * @param repInId
	 */
	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}
   
    







}