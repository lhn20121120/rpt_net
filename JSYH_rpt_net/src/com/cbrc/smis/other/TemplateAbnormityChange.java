package com.cbrc.smis.other;

public class TemplateAbnormityChange {
    /**
     * �쳣�仯��Ŀ����
     */
    private String itemName = null;
    /**
     * ������������׼
     */
    private Float prevRiseStandard = null;
    /**
     * �������½���׼
     */
    private Float prevFallStandard = null;
    /**
     * ������ͬ��������׼
     */
    private Float sameRiseStandard = null;
    /**
     * ������ͬ���½���׼
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
