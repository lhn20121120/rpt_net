package com.fitech.institution.po;

import java.io.Serializable;


public class AfValidateFormulaStandard implements Serializable {
	private Long formulaId;
	private String formulaValue;
	private String formulaName;
	private Long validateTypeId;
	private String templateId;
	private String versionId;
	private Long cellId;
	public AfValidateFormulaStandard() {
		super();
	}
	public Long getFormulaId() {
		return formulaId;
	}
	public void setFormulaId(Long formulaId) {
		this.formulaId = formulaId;
	}
	public String getFormulaValue() {
		return formulaValue;
	}
	public void setFormulaValue(String formulaValue) {
		this.formulaValue = formulaValue;
	}
	public String getFormulaName() {
		return formulaName;
	}
	public void setFormulaName(String formulaName) {
		this.formulaName = formulaName;
	}
	public Long getValidateTypeId() {
		return validateTypeId;
	}
	public void setValidateTypeId(Long validateTypeId) {
		this.validateTypeId = validateTypeId;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public Long getCellId() {
		return cellId;
	}
	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}
	public AfValidateFormulaStandard(Long formulaId, String formulaValue,
			String formulaName, Long validateTypeId, String templateId,
			String versionId, Long cellId) {
		super();
		this.formulaId = formulaId;
		this.formulaValue = formulaValue;
		this.formulaName = formulaName;
		this.validateTypeId = validateTypeId;
		this.templateId = templateId;
		this.versionId = versionId;
		this.cellId = cellId;
	}
	@Override
	public String toString() {
		return "AfValidateFormulaStandard [formulaId=" + formulaId
				+ ", formulaValue=" + formulaValue + ", formulaName="
				+ formulaName + ", validateTypeId=" + validateTypeId
				+ ", templateId=" + templateId + ", versionId=" + versionId
				+ ", cellId=" + cellId + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cellId == null) ? 0 : cellId.hashCode());
		result = prime * result
				+ ((formulaId == null) ? 0 : formulaId.hashCode());
		result = prime * result
				+ ((formulaName == null) ? 0 : formulaName.hashCode());
		result = prime * result
				+ ((formulaValue == null) ? 0 : formulaValue.hashCode());
		result = prime * result
				+ ((templateId == null) ? 0 : templateId.hashCode());
		result = prime * result
				+ ((validateTypeId == null) ? 0 : validateTypeId.hashCode());
		result = prime * result
				+ ((versionId == null) ? 0 : versionId.hashCode());
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
		AfValidateFormulaStandard other = (AfValidateFormulaStandard) obj;
		if (cellId == null) {
			if (other.cellId != null)
				return false;
		} else if (!cellId.equals(other.cellId))
			return false;
		if (formulaId == null) {
			if (other.formulaId != null)
				return false;
		} else if (!formulaId.equals(other.formulaId))
			return false;
		if (formulaName == null) {
			if (other.formulaName != null)
				return false;
		} else if (!formulaName.equals(other.formulaName))
			return false;
		if (formulaValue == null) {
			if (other.formulaValue != null)
				return false;
		} else if (!formulaValue.equals(other.formulaValue))
			return false;
		if (templateId == null) {
			if (other.templateId != null)
				return false;
		} else if (!templateId.equals(other.templateId))
			return false;
		if (validateTypeId == null) {
			if (other.validateTypeId != null)
				return false;
		} else if (!validateTypeId.equals(other.validateTypeId))
			return false;
		if (versionId == null) {
			if (other.versionId != null)
				return false;
		} else if (!versionId.equals(other.versionId))
			return false;
		return true;
	}
	
}
