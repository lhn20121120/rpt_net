package com.fitech.institution.po;

import java.io.Serializable;

public class AfTemplateColDefineId implements Serializable {
	
	public AfTemplateColDefineId() {
		super();
	}
	public AfTemplateColDefineId(String templateId, String col) {
		super();
		this.templateId = templateId;
		this.col = col;
	}
	private String templateId;
	private String col;
	

	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}
	@Override
	public String toString() {
		return "AfTemplateColDefineId [templateId=" + templateId + ", col="
				+ col + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((col == null) ? 0 : col.hashCode());
		result = prime * result
				+ ((templateId == null) ? 0 : templateId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AfTemplateColDefineId other = (AfTemplateColDefineId) obj;
		if (col == null) {
			if (other.col != null)
				return false;
		} else if (!col.equals(other.col))
			return false;
		if (templateId == null) {
			if (other.templateId != null)
				return false;
		} else if (!templateId.equals(other.templateId))
			return false;
		return true;
	}

	
}
