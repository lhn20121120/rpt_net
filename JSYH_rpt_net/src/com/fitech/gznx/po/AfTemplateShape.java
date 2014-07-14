package com.fitech.gznx.po;

/**
 * AfTemplateShape entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfTemplateShape implements java.io.Serializable {

	// Fields

	private AfTemplateShapeId id=new AfTemplateShapeId();
	private String cellContext;

	// Constructors

	/** default constructor */
	public AfTemplateShape() {
	}

	/** minimal constructor */
	public AfTemplateShape(AfTemplateShapeId id) {
		this.id = id;
	}

	/** full constructor */
	public AfTemplateShape(AfTemplateShapeId id, String cellContext) {
		this.id = id;
		this.cellContext = cellContext;
	}

	// Property accessors

	public AfTemplateShapeId getId() {
		return this.id;
	}

	public void setId(AfTemplateShapeId id) {
		this.id = id;
	}

	public String getCellContext() {
		return this.cellContext;
	}

	public void setCellContext(String cellContext) {
		this.cellContext = cellContext;
	}

}