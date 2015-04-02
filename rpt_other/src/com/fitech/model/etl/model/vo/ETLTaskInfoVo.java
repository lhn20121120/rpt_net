package com.fitech.model.etl.model.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fitech.model.etl.model.pojo.EtlLoadFileInfo;

public class ETLTaskInfoVo extends ETLBaseVo implements Serializable{

	// Fields

	private Integer taskId;
	private String taskName;
	private Integer loadFlag;
	private Integer etlCallTypeId;
	private String loadTypeCode;
	private String loadWay;
	private Integer transFlag;
	private String customTypeCode;
	private String transWay;
	private Integer orderId;
	private Integer usingFlag;
	private Integer warngingExecTime;
	private Set<EtlLoadFileInfo> etlLoadSets=new HashSet<EtlLoadFileInfo>();
	private Boolean isChange_1;
	private Boolean isChange_2;
	private Boolean isChange_3;

	// Constructors

	/** default constructor */
	public ETLTaskInfoVo() {
	}


	// Property accessors

	public Integer getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getLoadFlag() {
		return this.loadFlag;
	}

	public void setLoadFlag(Integer loadFlag) {
		this.loadFlag = loadFlag;
	}

	

	public String getLoadWay() {
		return this.loadWay;
	}

	public void setLoadWay(String loadWay) {
		this.loadWay = loadWay;
	}

	public Integer getTransFlag() {
		return this.transFlag;
	}

	public void setTransFlag(Integer transFlag) {
		this.transFlag = transFlag;
	}

	public String getTransWay() {
		return this.transWay;
	}

	public void setTransWay(String transWay) {
		this.transWay = transWay;
	}

	

	public Set<EtlLoadFileInfo> getEtlLoadSets() {
		return etlLoadSets;
	}

	public void setEtlLoadSets(Set<EtlLoadFileInfo> etlLoadSets) {
		this.etlLoadSets = etlLoadSets;
	}


	public Integer getEtlCallTypeId() {
		return etlCallTypeId;
	}


	public void setEtlCallTypeId(Integer etlCallTypeId) {
		this.etlCallTypeId = etlCallTypeId;
	}


	public String getLoadTypeCode() {
		return loadTypeCode;
	}


	public void setLoadTypeCode(String loadTypeCode) {
		this.loadTypeCode = loadTypeCode;
	}


	public String getCustomTypeCode() {
		return customTypeCode;
	}


	public void setCustomTypeCode(String customTypeCode) {
		this.customTypeCode = customTypeCode;
	}


	public Integer getOrderId() {
		return orderId;
	}


	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}


	public Integer getUsingFlag() {
		return usingFlag;
	}


	public void setUsingFlag(Integer usingFlag) {
		this.usingFlag = usingFlag;
	}


	public Integer getWarngingExecTime() {
		return warngingExecTime;
	}


	public void setWarngingExecTime(Integer warngingExecTime) {
		this.warngingExecTime = warngingExecTime;
	}


	public Boolean getIsChange_1() {
		return isChange_1;
	}


	public void setIsChange_1(Boolean isChange_1) {
		this.isChange_1 = isChange_1;
	}


	public Boolean getIsChange_2() {
		return isChange_2;
	}


	public void setIsChange_2(Boolean isChange_2) {
		this.isChange_2 = isChange_2;
	}


	public Boolean getIsChange_3() {
		return isChange_3;
	}


	public void setIsChange_3(Boolean isChange_3) {
		this.isChange_3 = isChange_3;
	}
	
	


	
	
	
	
}
