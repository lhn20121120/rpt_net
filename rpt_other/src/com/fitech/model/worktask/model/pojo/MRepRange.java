package com.fitech.model.worktask.model.pojo;

/**
 * MRepRange entity. @author MyEclipse Persistence Tools
 */

public class MRepRange implements java.io.Serializable {

	// Fields

	private MRepRangeId id;
	private String orgClsId;

	// Constructors

	/** default constructor */
	public MRepRange() {
	}

	/** minimal constructor */
	public MRepRange(MRepRangeId id) {
		this.id = id;
	}

	/** full constructor */
	public MRepRange(MRepRangeId id, String orgClsId) {
		this.id = id;
		this.orgClsId = orgClsId;
	}

	// Property accessors

	public MRepRangeId getId() {
		return this.id;
	}

	public void setId(MRepRangeId id) {
		this.id = id;
	}

	public String getOrgClsId() {
		return this.orgClsId;
	}

	public void setOrgClsId(String orgClsId) {
		this.orgClsId = orgClsId;
	}

}