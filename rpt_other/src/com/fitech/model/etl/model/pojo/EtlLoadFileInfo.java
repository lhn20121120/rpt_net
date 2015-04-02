package com.fitech.model.etl.model.pojo;

/**
 * EtlLoadFileInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlLoadFileInfo implements java.io.Serializable {

	// Fields

	private Integer fileId;
	private Integer taskId;
	private String fileName;
	private String filePath;
	private String tableCode;
	private Integer startRow;
	private String fileTypeCode;
	private Integer recordsNum;
	private String fileSeper;
	private Integer callTypeId;
	private String customTypeCode;
	private String customWay;
	private Integer serverId;

	// Constructors

	/** default constructor */
	public EtlLoadFileInfo() {
	}

	/** full constructor */
	public EtlLoadFileInfo(Integer taskId, String fileName, String filePath, String tableCode, Integer startRow, String fileTypeCode,
			Integer recordsNum, String fileSeper,Integer callTypeId, String customTypeCode, String customWay, Integer serverId) {
		this.taskId = taskId;
		this.fileName = fileName;
		this.filePath = filePath;
		this.tableCode = tableCode;
		this.startRow = startRow;
		this.fileTypeCode = fileTypeCode;
		this.recordsNum = recordsNum;
		this.fileSeper = fileSeper;
		this.customTypeCode = customTypeCode;
		this.customWay = customWay;
		this.serverId = serverId;
		this.callTypeId=callTypeId;
	}

	// Property accessors
	/**
	 * 文件编号
	 */
	public Integer getFileId() {
		return this.fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	
	
	/***
	 * 关联到任务ID
	 * @return
	 */
	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	/***
	 * 文件名称
	 * @return
	 */
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/***
	 * 文件路径
	 * @return
	 */
	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	/***
	 * 载入数据表名
	 * @return
	 */
	public String getTableCode() {
		return this.tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	
	/***
	 * 起始读取行
	 * @return
	 */
	public Integer getStartRow() {
		return this.startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}
	
	/***
	 * 关联文件类型
	 * @return
	 */
	public String getFileTypeCode() {
		return fileTypeCode;
	}

	public void setFileTypeCode(String fileTypeCode) {
		this.fileTypeCode = fileTypeCode;
	}
	
	/**
	 * 提交记录数 ，不得小于1
	 * @return
	 */
	public Integer getRecordsNum() {
		if(this.recordsNum==null)
			return null;
		else if(this.recordsNum<1)
			return 1;
		else 
			return this.recordsNum;
	}

	public void setRecordsNum(Integer recordsNum) {
		this.recordsNum = recordsNum;
	}
	
	/***
	 * 文件分隔符
	 * @return
	 */
	public String getFileSeper() {
		return this.fileSeper;
	}

	public void setFileSeper(String fileSeper) {
		this.fileSeper = fileSeper;
	}

	
	/***
	 * 关联到数据装载模式
	 * @return
	 */
	public void setCustomTypeCode(String customTypeCode) {
		this.customTypeCode = customTypeCode;
	}

	public void setCustomWay(String customWay) {
		this.customWay = customWay;
	}
	
	/**
	 * 文件自定义装载程序或脚本位置
	 * @return
	 */
	public String getCustomWay() {
		return this.customWay;
	}

	public String getCustomTypeCode() {
		return customTypeCode;
	}

	
	
	/**
	 * 源数据服务器
	 * @return
	 */
	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	
	/***
	 * 关联到EtlCallTypeInfo 装载模式
	 * @return
	 */
	public Integer getCallTypeId() {
		return callTypeId;
	}
	public void setCallTypeId(Integer callTypeId) {
		this.callTypeId = callTypeId;
	}


	
}