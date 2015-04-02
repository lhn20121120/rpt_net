package com.cbrc.smis.proc.po;

import java.io.Serializable;

import java.math.BigDecimal;

/** @author Hibernate CodeGenerator */
public class AbnormityChange implements Serializable {
	/**实际数据表ID**/
	private int repInId;
	/**单元格ID*/
    private int cellId;
    /**列名**/
    private String colName;
    /** nullable persistent field */
    private float prevRiseStandard;

    /** nullable persistent field */
    private float prevFallStandard;

    /** nullable persistent field */
    private float sameRiseStandard;

    /** nullable persistent field */
    private float sameFallStandard;
     
    public AbnormityChange(){}
    
    public float getPrevRiseStandard() {
        return this.prevRiseStandard;
    }

    public void setPrevRiseStandard(float prevRiseStandard) {
        this.prevRiseStandard = prevRiseStandard;
    }

    public float getPrevFallStandard() {
        return this.prevFallStandard;
    }

    public void setPrevFallStandard(float prevFallStandard) {
        this.prevFallStandard = prevFallStandard;
    }

    public float getSameRiseStandard() {
        return this.sameRiseStandard;
    }

    public void setSameRiseStandard(float sameRiseStandard) {
        this.sameRiseStandard = sameRiseStandard;
    }

    public float getSameFallStandard() {
        return this.sameFallStandard;
    }

    public void setSameFallStandard(float sameFallStandard) {
        this.sameFallStandard = sameFallStandard;
    }

	public int getCellId() {
		return cellId;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public int getRepInId() {
		return repInId;
	}

	public void setRepInId(int repInId) {
		this.repInId = repInId;
	}

}