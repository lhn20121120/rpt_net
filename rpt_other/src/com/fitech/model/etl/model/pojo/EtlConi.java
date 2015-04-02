package com.fitech.model.etl.model.pojo;

import java.util.Date;

/**
 * EtlConi entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlConi implements java.io.Serializable {

	// Fields

	private Integer rowNumber;
	private Integer eventClass;
	private String applicationName;
	private Integer cpu;
	private Integer clientProcessId;
	private Integer databaseId;
	private String databaseName;
	private Long duration;
	private Date endTime;
	private Integer error;
	private Long eventSequence;
	private Integer groupId;
	private String hostName;
	private Integer isSystem;
	private String loginName;
	private String loginSid;
	private String ntdomainName;
	private String ntuserName;
	private Long reads;
	private Integer requestId;
	private Long rowCounts;
	private Integer spid;
	private String serverName;
	private String sessionLoginName;
	private Date startTime;
	private String textData;
	private Long transactionId;
	private Long writes;
	private Long xactSequence;
	private Integer severity;
	private Integer state;
	private String binaryData;

	// Constructors

	/** default constructor */
	public EtlConi() {
	}

	/** full constructor */
	public EtlConi(Integer eventClass, String applicationName, Integer cpu, Integer clientProcessId, Integer databaseId, String databaseName,
			Long duration, Date endTime, Integer error, Long eventSequence, Integer groupId, String hostName, Integer isSystem, String loginName,
			String loginSid, String ntdomainName, String ntuserName, Long reads, Integer requestId, Long rowCounts, Integer spid,
			String serverName, String sessionLoginName, Date startTime, String textData, Long transactionId, Long writes, Long xactSequence,
			Integer severity, Integer state, String binaryData) {
		this.eventClass = eventClass;
		this.applicationName = applicationName;
		this.cpu = cpu;
		this.clientProcessId = clientProcessId;
		this.databaseId = databaseId;
		this.databaseName = databaseName;
		this.duration = duration;
		this.endTime = endTime;
		this.error = error;
		this.eventSequence = eventSequence;
		this.groupId = groupId;
		this.hostName = hostName;
		this.isSystem = isSystem;
		this.loginName = loginName;
		this.loginSid = loginSid;
		this.ntdomainName = ntdomainName;
		this.ntuserName = ntuserName;
		this.reads = reads;
		this.requestId = requestId;
		this.rowCounts = rowCounts;
		this.spid = spid;
		this.serverName = serverName;
		this.sessionLoginName = sessionLoginName;
		this.startTime = startTime;
		this.textData = textData;
		this.transactionId = transactionId;
		this.writes = writes;
		this.xactSequence = xactSequence;
		this.severity = severity;
		this.state = state;
		this.binaryData = binaryData;
	}

	// Property accessors

	public Integer getRowNumber() {
		return this.rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	public Integer getEventClass() {
		return this.eventClass;
	}

	public void setEventClass(Integer eventClass) {
		this.eventClass = eventClass;
	}

	public String getApplicationName() {
		return this.applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public Integer getCpu() {
		return this.cpu;
	}

	public void setCpu(Integer cpu) {
		this.cpu = cpu;
	}

	public Integer getClientProcessId() {
		return this.clientProcessId;
	}

	public void setClientProcessId(Integer clientProcessId) {
		this.clientProcessId = clientProcessId;
	}

	public Integer getDatabaseId() {
		return this.databaseId;
	}

	public void setDatabaseId(Integer databaseId) {
		this.databaseId = databaseId;
	}

	public String getDatabaseName() {
		return this.databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public Long getDuration() {
		return this.duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getError() {
		return this.error;
	}

	public void setError(Integer error) {
		this.error = error;
	}

	public Long getEventSequence() {
		return this.eventSequence;
	}

	public void setEventSequence(Long eventSequence) {
		this.eventSequence = eventSequence;
	}

	public Integer getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Integer getIsSystem() {
		return this.isSystem;
	}

	public void setIsSystem(Integer isSystem) {
		this.isSystem = isSystem;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginSid() {
		return this.loginSid;
	}

	public void setLoginSid(String loginSid) {
		this.loginSid = loginSid;
	}

	public String getNtdomainName() {
		return this.ntdomainName;
	}

	public void setNtdomainName(String ntdomainName) {
		this.ntdomainName = ntdomainName;
	}

	public String getNtuserName() {
		return this.ntuserName;
	}

	public void setNtuserName(String ntuserName) {
		this.ntuserName = ntuserName;
	}

	public Long getReads() {
		return this.reads;
	}

	public void setReads(Long reads) {
		this.reads = reads;
	}

	public Integer getRequestId() {
		return this.requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public Long getRowCounts() {
		return this.rowCounts;
	}

	public void setRowCounts(Long rowCounts) {
		this.rowCounts = rowCounts;
	}

	public Integer getSpid() {
		return this.spid;
	}

	public void setSpid(Integer spid) {
		this.spid = spid;
	}

	public String getServerName() {
		return this.serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getSessionLoginName() {
		return this.sessionLoginName;
	}

	public void setSessionLoginName(String sessionLoginName) {
		this.sessionLoginName = sessionLoginName;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getTextData() {
		return this.textData;
	}

	public void setTextData(String textData) {
		this.textData = textData;
	}

	public Long getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getWrites() {
		return this.writes;
	}

	public void setWrites(Long writes) {
		this.writes = writes;
	}

	public Long getXactSequence() {
		return this.xactSequence;
	}

	public void setXactSequence(Long xactSequence) {
		this.xactSequence = xactSequence;
	}

	public Integer getSeverity() {
		return this.severity;
	}

	public void setSeverity(Integer severity) {
		this.severity = severity;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getBinaryData() {
		return this.binaryData;
	}

	public void setBinaryData(String binaryData) {
		this.binaryData = binaryData;
	}

}