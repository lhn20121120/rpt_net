package com.fitech.model.commoninfo.model.pojo;

import java.util.Date;


/**
 * TMsgInfo entity. @author MyEclipse Persistence Tools
 */

public class TMsgInfo  implements java.io.Serializable {


    // Fields    

     private Integer msgId;
     private String userId;
     private String userName;
     private Integer userType;
     private String touserId;
     private String touserName;
     private Integer touserType;
     private String startTime;
     private String updateTime;
     private String msgTitle;
     private Integer msgState = 0;//默认为0
     private Integer msgType;
     private Integer revertState;//默认为0
     private String filename;
     private String content;
     private Integer fileNameFlag;//附件是否同步标记位 0为未同步 1为已同步
     private Integer synflag=0;//消息是否同步状态位 0为不同步 1为同步 已默认为0
     private String viewFileName;//展示的附件名称
     
     
     private String[] orgIds;//机构id
     private String[] orgNames;
     private String publicFlag;
     private Integer status;
    // Constructors

    /** default constructor */
    public TMsgInfo() {
    }

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/** minimal constructor */
    public TMsgInfo(Integer msgId) {
        this.msgId = msgId;
    }
    
    /** full constructor */
    public TMsgInfo(Integer msgId, String userId, String userName, Integer userType, String touserId, String touserName, Integer touserType, String startTime, String updateTime, String msgTitle, Integer msgState, Integer msgType, Integer revertState, String filename, String content) {
        this.msgId = msgId;
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
        this.touserId = touserId;
        this.touserName = touserName;
        this.touserType = touserType;
        this.startTime = startTime;
        this.updateTime = updateTime;
        this.msgTitle = msgTitle;
        this.msgState = msgState;
        this.msgType = msgType;
        this.revertState = revertState;
        this.filename = filename;
        this.content = content;
    }

   
    // Property accessors
    
    
    public Integer getMsgId() {
        return this.msgId;
    }
    
    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }
    
    /**
     * 发件人id
     */
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    /***
     * 发件人机构名称
     * @return
     */
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /***
     * 发件机构类型1：机构，2：银监局
     * @return
     */
    public Integer getUserType() {
        return this.userType;
    }
    
    /***
     * 发件机构类型1：机构，2：银监局
     * @return
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
    }
    
    /***
     * 收件人id
     * @return
     */
    public String getTouserId() {
        return this.touserId;
    }
    
    public void setTouserId(String touserId) {
        this.touserId = touserId;
    }
    
    /***
     * 收件人机构名称
     * @return
     */
    public String getTouserName() {
        return this.touserName;
    }
    
    /***
     * 收件人机构名称
     * @return
     */
    public void setTouserName(String touserName) {
        this.touserName = touserName;
    }
    
    /***
     * 收件机构类型1：机构，2：银监局
     * @return
     */
    public Integer getTouserType() {
        return this.touserType;
    }
    
    /***
     * 收件机构类型1：机构，2：银监局
     * @return
     */
    public void setTouserType(Integer touserType) {
        this.touserType = touserType;
    }
    
    /***
     * 邮件发送时间
     * @return
     */
    public String getStartTime() {
        return this.startTime;
    }
    
    /***
     * 邮件发送时间
     * @return
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    /***
     * 邮件回复时间
     * @return
     */
    public String getUpdateTime() {
        return this.updateTime;
    }
    
    /***
     * 邮件回复时间
     * @return
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
    /***
     * 邮件标题
     * @return
     */
    public String getMsgTitle() {
        return this.msgTitle;
    }
    
    /***
     * 邮件标题
     * @return
     */
    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }
    
    /***
     * 邮件是否被阅读1：已读，0：未读，2：未发送的草稿
     * 已设置默认为0
     * @return
     */
    public Integer getMsgState() {
        return this.msgState;
    }
    
    /***
     * 邮件是否被阅读1：已读，0：未读，2：未发送的草稿
     * 已设置默认为0
     * @return
     */
    public void setMsgState(Integer msgState) {
        this.msgState = msgState;
    }
    
    /***
     * 邮件类型1：公告，不需要回复，默认全部机构可以看到，2：私信,3：问题问答
     * @return
     */
    public Integer getMsgType() {
        return this.msgType;
    }
    
    /***
     * 邮件类型1：公告，不需要回复，默认全部机构可以看到，2：私信,3：问题问答
     * @return
     */
    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }
    
    /***
     * 回复标志，回复哪条邮件即为哪条邮件的msgid，若不是回复的邮件为0
     * 已设置默认为0
     * @return
     */
    public Integer getRevertState() {
        return this.revertState;
    }
    
    /***
     * 回复标志，回复哪条邮件即为哪条邮件的msgid，若不是回复的邮件为0
     * 已设置默认为0
     * @return
     */
    public void setRevertState(Integer revertState) {
        this.revertState = revertState;
    }
    
    /***
     * 附件名称，带后缀名
     * @return
     */
    public String getFilename() {
        return this.filename;
    }
    
    /***
     * 附件名称，带后缀名
     * @return
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    /***
     * 邮件正文
     * @return
     */
    public String getContent() {
        return this.content;
    }
    
    /***
     * 邮件正文
     * @return
     */
    public void setContent(String content) {
        this.content = content;
    }

	public String[] getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String[] orgIds) {
		this.orgIds = orgIds;
	}
	
	/***
	 * 附件是否同步标记位 0为未同步 1为已同步
	 * @return
	 */
	public Integer getFileNameFlag() {
		return fileNameFlag;
	}
	
	/***
	 * 附件是否同步标记位 0为未同步 1为已同步
	 * @param fileNameFlag
	 */
	public void setFileNameFlag(Integer fileNameFlag) {
		this.fileNameFlag = fileNameFlag;
	}
	
	/***
	 * 消息是否同步状态位 0为不同步 1为同步 已默认为0
	 * @return
	 */
	public Integer getSynflag() {
		return synflag;
	}
	
	/***
	 * 消息是否同步状态位 0为不同步 1为同步 已默认为0
	 * @return
	 */
	public void setSynflag(Integer synflag) {
		this.synflag = synflag;
	}
	
	/***
	 * 展示的附件名称
	 * @return
	 */
	public String getViewFileName() {
		return viewFileName;
	}
	
	/***
	 * 展示附件名称
	 * @param viewFileName
	 */
	public void setViewFileName(String viewFileName) {
		this.viewFileName = viewFileName;
	}
	
	/***
	 * 发送机构名称的集合
	 * @return
	 */
	public String[] getOrgNames() {
		return orgNames;
	}
	
	/**
	 * 发送机构名称的集合
	 * @param orgNames
	 */
	public void setOrgNames(String[] orgNames) {
		this.orgNames = orgNames;
	}

	public String getPublicFlag() {
		return publicFlag;
	}

	public void setPublicFlag(String publicFlag) {
		this.publicFlag = publicFlag;
	}
   
    







}