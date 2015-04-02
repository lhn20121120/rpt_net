package com.fitech.gznx.po;

/**
 * AfTemplatetype entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfTemplatetype implements java.io.Serializable {

	// Fields

	private AfTemplatetypeId id;
	private String typeName;
	private Long pretypeId;
	private String bak1;
	private String bak2;

	// Constructors

	/** default constructor */
	public AfTemplatetype() {
	}

	/** minimal constructor */
	public AfTemplatetype(AfTemplatetypeId id, String typeName) {
		this.id = id;
		this.typeName = typeName;
	}

	/** full constructor */
	public AfTemplatetype(AfTemplatetypeId id, String typeName, Long pretypeId,
			String bak1, String bak2) {
		this.id = id;
		this.typeName = typeName;
		this.pretypeId = pretypeId;
		this.bak1 = bak1;
		this.bak2 = bak2;
	}

	// Property accessors

	public AfTemplatetypeId getId() {
		return this.id;
	}

	public void setId(AfTemplatetypeId id) {
		this.id = id;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Long getPretypeId() {
		return this.pretypeId;
	}

	public void setPretypeId(Long pretypeId) {
		this.pretypeId = pretypeId;
	}

	public String getBak1() {
		return this.bak1;
	}

	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}

	public String getBak2() {
		return this.bak2;
	}

	public void setBak2(String bak2) {
		this.bak2 = bak2;
	}

}