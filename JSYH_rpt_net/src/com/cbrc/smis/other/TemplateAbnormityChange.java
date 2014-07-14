package com.cbrc.smis.other;

public class TemplateAbnormityChange {
    /**
     * 异常变化项目名称
     */
    private String itemName = null;
    /**
     * 比上期上升标准
     */
    private Float prevRiseStandard = null;
    /**
     * 比上期下降标准
     */
    private Float prevFallStandard = null;
    /**
     * 比上年同期上升标准
     */
    private Float sameRiseStandard = null;
    /**
     * 比上年同期下降标准
     */
    private Float sameFallStandard = null;
    
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public Float getPrevFallStandard() {
        return prevFallStandard;
    }
    public void setPrevFallStandard(Float prevFallStandard) {
        this.prevFallStandard = prevFallStandard;
    }
    public Float getPrevRiseStandard() {
        return prevRiseStandard;
    }
    public void setPrevRiseStandard(Float prevRiseStandard) {
        this.prevRiseStandard = prevRiseStandard;
    }
    public Float getSameFallStandard() {
        return sameFallStandard;
    }
    public void setSameFallStandard(Float sameFallStandard) {
        this.sameFallStandard = sameFallStandard;
    }
    public Float getSameRiseStandard() {
        return sameRiseStandard;
    }
    public void setSameRiseStandard(Float sameRiseStandard) {
        this.sameRiseStandard = sameRiseStandard;
    }
    
    
}
