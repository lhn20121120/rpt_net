package com.fitech.model.worktask.model.pojo;

import java.math.BigDecimal;


/**
 * AfTemplateFreqRelationId entity. @author MyEclipse Persistence Tools
 */

public class AfTemplateFreqRelationId  implements java.io.Serializable {


    // Fields    

     private String templateId;
     private String versionId;
     private BigDecimal repFreqId;


    // Constructors

    /** default constructor */
    public AfTemplateFreqRelationId() {
    }

    
    /** full constructor */
    public AfTemplateFreqRelationId(String templateId, String versionId, BigDecimal repFreqId) {
        this.templateId = templateId;
        this.versionId = versionId;
        this.repFreqId = repFreqId;
    }

   
    // Property accessors

    public String getTemplateId() {
        return this.templateId;
    }
    
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getVersionId() {
        return this.versionId;
    }
    
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public BigDecimal getRepFreqId() {
        return this.repFreqId;
    }
    
    public void setRepFreqId(BigDecimal repFreqId) {
        this.repFreqId = repFreqId;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof AfTemplateFreqRelationId) ) return false;
		 AfTemplateFreqRelationId castOther = ( AfTemplateFreqRelationId ) other; 
         
		 return ( (this.getTemplateId()==castOther.getTemplateId()) || ( this.getTemplateId()!=null && castOther.getTemplateId()!=null && this.getTemplateId().equals(castOther.getTemplateId()) ) )
 && ( (this.getVersionId()==castOther.getVersionId()) || ( this.getVersionId()!=null && castOther.getVersionId()!=null && this.getVersionId().equals(castOther.getVersionId()) ) )
 && ( (this.getRepFreqId()==castOther.getRepFreqId()) || ( this.getRepFreqId()!=null && castOther.getRepFreqId()!=null && this.getRepFreqId().equals(castOther.getRepFreqId()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getTemplateId() == null ? 0 : this.getTemplateId().hashCode() );
         result = 37 * result + ( getVersionId() == null ? 0 : this.getVersionId().hashCode() );
         result = 37 * result + ( getRepFreqId() == null ? 0 : this.getRepFreqId().hashCode() );
         return result;
   }   





}