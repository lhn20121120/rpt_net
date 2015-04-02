package com.fitech.model.etl.model.pojo;

/**
 * EtlFileServerInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlFileServerInfo implements java.io.Serializable {

	// Fields

	private Integer serverId;
	private String address;
	private String userName;
	private String userPwd;
	private Integer port;
	private String serverName;

	// Constructors

	/** default constructor */
	public EtlFileServerInfo() {
	}

	/** full constructor */
	public EtlFileServerInfo(String address, String userName, String userPwd, Integer port) {
		this.address = address;
		this.userName = userName;
		this.userPwd = userPwd;
		this.port = port;
	}

	// Property accessors
	
	/**服务器id 自增*/
	public Integer getServerId() {
		return this.serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	
	/**服务器地址*/
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	/**服务器登录名*/
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**密码*/
	public String getUserPwd() {
		return this.userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	
	/**端口*/
	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
	/**服务器名称*/
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	
}