package com.fitech.institution.po;

import java.io.Serializable;

public class AfTemplateColDefine implements Serializable {
	
	private AfTemplateColDefineId id;
	private String colName;
	
	public AfTemplateColDefineId getId() {
		return id;
	}
	public void setId(AfTemplateColDefineId id) {
		this.id = id;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	@Override
	public String toString() {
		return "AfTemplateColDefine [id=" +id + ", colName="
				+ colName + "]";
	}
	
	
}
