package com.fitech.model.etl.model.vo;

import com.fitech.model.etl.model.pojo.EtlTaskOrderId;

public class EtlTaskOrderVo {
	// Fields

	private EtlTaskOrderId id=new EtlTaskOrderId();

	// Constructors

	/** default constructor */
	public EtlTaskOrderVo() {
	}

	/** full constructor */
	public EtlTaskOrderVo(EtlTaskOrderId id) {
		this.id = id;
	}

	// Property accessors

	public EtlTaskOrderId getId() {
		return this.id;
	}

	public void setId(EtlTaskOrderId id) {
		this.id = id;
	}
}
