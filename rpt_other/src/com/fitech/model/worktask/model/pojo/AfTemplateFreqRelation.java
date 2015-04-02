package com.fitech.model.worktask.model.pojo;

import java.math.BigDecimal;


/**
 * AfTemplateFreqRelation entity. @author MyEclipse Persistence Tools
 */

public class AfTemplateFreqRelation  implements java.io.Serializable {


    // Fields    

     private AfTemplateFreqRelationId id;
     private BigDecimal normalTime;
     private BigDecimal laterTime;
     private String freqName;


    // Constructors

    /** default constructor */
    public AfTemplateFreqRelation() {
    }

	/** minimal constructor */
    public AfTemplateFreqRelation(AfTemplateFreqRelationId id) {
        this.id = id;
    }
    
    /** full constructor */
    public AfTemplateFreqRelation(AfTemplateFreqRelationId id, BigDecimal normalTime, BigDecimal laterTime, String freqName) {
        this.id = id;
        this.normalTime = normalTime;
        this.laterTime = laterTime;
        this.freqName = freqName;
    }

   
    // Property accessors

    public AfTemplateFreqRelationId getId() {
        return this.id;
    }
    
    public void setId(AfTemplateFreqRelationId id) {
        this.id = id;
    }

    public BigDecimal getNormalTime() {
        return this.normalTime;
    }
    
    public void setNormalTime(BigDecimal normalTime) {
        this.normalTime = normalTime;
    }

    public BigDecimal getLaterTime() {
        return this.laterTime;
    }
    
    public void setLaterTime(BigDecimal laterTime) {
        this.laterTime = laterTime;
    }

    public String getFreqName() {
        return this.freqName;
    }
    
    public void setFreqName(String freqName) {
        this.freqName = freqName;
    }
   








}