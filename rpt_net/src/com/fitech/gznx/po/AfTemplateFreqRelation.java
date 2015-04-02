package com.fitech.gznx.po;

/**
 * AfTemplateFreqRelation entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfTemplateFreqRelation implements java.io.Serializable {

	// Fields

	private AfTemplateFreqRelationId id;
	private Long normalTime;
	private Long laterTime;
	private String freqName;

	// Constructors

	/** default constructor */
	public AfTemplateFreqRelation() {
	}

	/** minimal constructor */
	public AfTemplateFreqRelation(AfTemplateFreqRelationId id) {
		this.id = id;
	}

	/** full constructor */
	public AfTemplateFreqRelation(AfTemplateFreqRelationId id, Long normalTime,
			Long laterTime, String freqName) {
		this.id = id;
		this.normalTime = normalTime;
		this.laterTime = laterTime;
		this.freqName = freqName;
	}

	// Property accessors

	public AfTemplateFreqRelationId getId() {
		return this.id;
	}

	public void setId(AfTemplateFreqRelationId id) {
		this.id = id;
	}

	public Long getNormalTime() {
		return this.normalTime;
	}

	public void setNormalTime(Long normalTime) {
		this.normalTime = normalTime;
	}

	public Long getLaterTime() {
		return this.laterTime;
	}

	public void setLaterTime(Long laterTime) {
		this.laterTime = laterTime;
	}

	public String getFreqName() {
		return this.freqName;
	}

	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}

}