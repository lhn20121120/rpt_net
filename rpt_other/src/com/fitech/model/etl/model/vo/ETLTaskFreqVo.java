package com.fitech.model.etl.model.vo;

import com.fitech.model.etl.model.pojo.EtlTaskFreqId;

public class ETLTaskFreqVo {
	// Fields

	private EtlTaskFreqId id=new EtlTaskFreqId();

	// Constructors

	/** default constructor */
	public ETLTaskFreqVo() {
	}

	/** full constructor */
	public ETLTaskFreqVo(EtlTaskFreqId id) {
		this.id = id;
	}

	// Property accessors

	public EtlTaskFreqId getId() {
		return this.id;
	}

	public void setId(EtlTaskFreqId id) {
		this.id = id;
	}
}
