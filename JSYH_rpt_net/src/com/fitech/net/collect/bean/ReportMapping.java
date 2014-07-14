package com.fitech.net.collect.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ReportMapping implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private Long repMapId = null;
    
    private String srcRepId = null;
    
    private String srcVersionId = null;
    
    private String tarRepId = null;
    
    private String tarVersionId = null;
    
    private Set cellMappings = new HashSet();
    
    public ReportMapping(){}

    public Set getCellMappings() {
        return cellMappings;
    }

    public void setCellMappings(Set cellMappings) {
        this.cellMappings = cellMappings;
    }

    public Long getRepMapId() {
        return repMapId;
    }

    public void setRepMapId(Long repMapId) {
        this.repMapId = repMapId;
    }

    public String getSrcRepId() {
        return srcRepId;
    }

    public void setSrcRepId(String srcRepId) {
        this.srcRepId = srcRepId;
    }

    public String getSrcVersionId() {
        return srcVersionId;
    }

    public void setSrcVersionId(String srcVersionId) {
        this.srcVersionId = srcVersionId;
    }

    public String getTarRepId() {
        return tarRepId;
    }

    public void setTarRepId(String tarRepId) {
        this.tarRepId = tarRepId;
    }

    public String getTarVersionId() {
        return tarVersionId;
    }

    public void setTarVersionId(String tarVersionId) {
        this.tarVersionId = tarVersionId;
    }

  
   }
