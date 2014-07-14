package com.cbrc.org.form;

import java.util.Date;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class AFDataTraceForm extends ActionForm{
	// Fields    

    private Integer traceId;//����
    private String cellName;//��Ԫ������
    private String repInId;//����ID
    private String username;//�޸���
    private String dateTime;//����
    private String originalData;//ԭʼֵ
    private String changeData;//�޸�ֵ
    private String finalData;//����ֵ
    private String descTrace;//��ע
    private Integer status;//״̬
    private String oriData;//��ʼֵ
    private List<AFDataTraceForm> dataList;
    private String beginDate;//��ʼʱ��
    private String endDate;//����ʱ��
    private String reportTerm;//��������
    private String repName;//��������
    private String repInIds;//����ID
    
  
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
    * ����ID
    * @return
    */
   public String getRepInId() {
	return repInId;
   }
   
   /***
    * ����ID
    * @param repInId
    */
	public void setRepInId(String repInId) {
		this.repInId = repInId;
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
    * ��ʼֵ
    * @return
    */
   public String getOriData() {
	   return oriData;
   }
	
   /***
    * ��ʼֵ
    * @return
    */
   public void setOriData(String oriData) {
	  this.oriData = oriData;
   }
   

	public String getBeginDate() {
		return beginDate;
	}
	
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	

public String getReportTerm() {
		return reportTerm;
	}

	public void setReportTerm(String reportTerm) {
		this.reportTerm = reportTerm;
	}

/***
    * ״̬ ��ʱ���κ�����
    * @param status
    */
   public void setStatus(Integer status) {
       this.status = status;
   }

	public List<AFDataTraceForm> getDataList() {
		return dataList;
	}
	
	public void setDataList(List<AFDataTraceForm> dataList) {
		this.dataList = dataList;
	}

	public String getRepName() {
		return repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}

	public String getRepInIds() {
		return repInIds;
	}

	public void setRepInIds(String repInIds) {
		this.repInIds = repInIds;
	}
	
   
}
