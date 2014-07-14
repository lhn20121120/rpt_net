package com.cbrc.org.form;

import java.util.Date;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class AFDataTraceForm extends ActionForm{
	// Fields    

    private Integer traceId;//主键
    private String cellName;//单元格名称
    private String repInId;//报表ID
    private String username;//修改人
    private String dateTime;//日期
    private String originalData;//原始值
    private String changeData;//修改值
    private String finalData;//最终值
    private String descTrace;//备注
    private Integer status;//状态
    private String oriData;//初始值
    private List<AFDataTraceForm> dataList;
    private String beginDate;//开始时间
    private String endDate;//结束时间
    private String reportTerm;//报表期数
    private String repName;//报表名称
    private String repInIds;//报表ID
    
  
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
    * 报表ID
    * @return
    */
   public String getRepInId() {
	return repInId;
   }
   
   /***
    * 报表ID
    * @param repInId
    */
	public void setRepInId(String repInId) {
		this.repInId = repInId;
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
    * 初始值
    * @return
    */
   public String getOriData() {
	   return oriData;
   }
	
   /***
    * 初始值
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
    * 状态 暂时无任何作用
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
