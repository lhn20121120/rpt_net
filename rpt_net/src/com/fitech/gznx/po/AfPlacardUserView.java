package com.fitech.gznx.po;

/**
 * AfPlacardUserView entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfPlacardUserView implements java.io.Serializable {

	// Fields

	private AfPlacardUserViewId id;
	private Long viewState;

	// Constructors

	/** default constructor */
	public AfPlacardUserView() {
	}

	/** minimal constructor */
	public AfPlacardUserView(AfPlacardUserViewId id) {
		this.id = id;
	}

	/** full constructor */
	public AfPlacardUserView(AfPlacardUserViewId id, Long viewState) {
		this.id = id;
		this.viewState = viewState;
	}

	// Property accessors

	public AfPlacardUserViewId getId() {
		return this.id;
	}

	public void setId(AfPlacardUserViewId id) {
		this.id = id;
	}

	public Long getViewState() {
		return this.viewState;
	}

	public void setViewState(Long viewState) {
		this.viewState = viewState;
	}

}