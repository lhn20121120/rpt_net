package com.fitech.gznx.po;

/**
 * AfTemplateCollRule entity. @author MyEclipse Persistence Tools
 */

public class AfTemplateCollRule implements java.io.Serializable {

	// Fields

	private AfTemplateCollRuleId id;
	private Integer collSchema;
	private String collFormula;

	// Constructors

	/** default constructor */
	public AfTemplateCollRule() {
	}

	/** minimal constructor */
	public AfTemplateCollRule(AfTemplateCollRuleId id) {
		this.id = id;
	}


	// Property accessors

	public AfTemplateCollRuleId getId() {
		return this.id;
	}

	public void setId(AfTemplateCollRuleId id) {
		this.id = id;
	}


	public AfTemplateCollRule(AfTemplateCollRuleId id, Integer collSchema,
			String collFormula) {
		super();
		this.id = id;
		this.collSchema = collSchema;
		this.collFormula = collFormula;
	}

	public Integer getCollSchema() {
		return collSchema;
	}

	public void setCollSchema(Integer collSchema) {
		this.collSchema = collSchema;
	}

	public String getCollFormula() {
		return this.collFormula;
	}

	public void setCollFormula(String collFormula) {
		this.collFormula = collFormula;
	}

}