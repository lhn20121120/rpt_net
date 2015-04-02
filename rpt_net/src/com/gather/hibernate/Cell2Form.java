package com.gather.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Cell2Form implements Serializable {

    /** identifier field */
    private Integer cell2FormId;
    private Integer cellFormId;
    private String childRepId;
    private String versionId;
    private Integer state;

    /**
	 * @return Returns the cell2FormId.
	 */
	public Integer getCell2FormId() {
		return cell2FormId;
	}

	/**
	 * @param cell2FormId The cell2FormId to set.
	 */
	public void setCell2FormId(Integer cell2FormId) {
		this.cell2FormId = cell2FormId;
	}

	/**
	 * @return Returns the cellFormId.
	 */
	public Integer getCellFormId() {
		return cellFormId;
	}

	/**
	 * @param cellFormId The cellFormId to set.
	 */
	public void setCellFormId(Integer cellFormId) {
		this.cellFormId = cellFormId;
	}

	/**
	 * @return Returns the childRepId.
	 */
	public String getChildRepId() {
		return childRepId;
	}

	/**
	 * @param childRepId The childRepId to set.
	 */
	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}

	/**
	 * @return Returns the state.
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * @param state The state to set.
	 */
	public void setState(Integer state) {
		this.state = state;
	}

	/**
	 * @return Returns the versionId.
	 */
	public String getVersionId() {
		return versionId;
	}

	/**
	 * @param versionId The versionId to set.
	 */
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	/** full constructor */
    public Cell2Form(Integer cell2FormId,Integer cellFormId, String childRepId, String versionId,Integer state) {
        this.cell2FormId = cell2FormId;
        this.cellFormId = cellFormId;
        this.childRepId = childRepId;
        this.versionId = versionId;
        this.state = state;
    }

    /** default constructor */
    public Cell2Form() {
    }

  
    public String toString() {
        return new ToStringBuilder(this)
            .append("cell2FormId", getCell2FormId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Cell2Form) ) return false;
        Cell2Form castOther = (Cell2Form) other;
        return new EqualsBuilder()
            .append(this.getCell2FormId(), castOther.getCell2FormId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCell2FormId())
            .toHashCode();
    }

}
