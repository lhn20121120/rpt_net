package com.fitech.gznx.po;



/**
 * AfReportForceRepId entity. @author MyEclipse Persistence Tools
 */

public class AfReportForceRepId  implements java.io.Serializable {


    // Fields    

     private Long repId;
     private Long forceTypeId;


    // Constructors

    /** default constructor */
    public AfReportForceRepId() {
    }

    
    /** full constructor */
    public AfReportForceRepId(Long repId, Long forceTypeId) {
        this.repId = repId;
        this.forceTypeId = forceTypeId;
    }

   
    // Property accessors

    public Long getRepId() {
        return this.repId;
    }
    
    public void setRepId(Long repId) {
        this.repId = repId;
    }

    public Long getForceTypeId() {
        return this.forceTypeId;
    }
    
    public void setForceTypeId(Long forceTypeId) {
        this.forceTypeId = forceTypeId;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof AfReportForceRepId) ) return false;
		 AfReportForceRepId castOther = ( AfReportForceRepId ) other; 
         
		 return ( (this.getRepId()==castOther.getRepId()) || ( this.getRepId()!=null && castOther.getRepId()!=null && this.getRepId().equals(castOther.getRepId()) ) )
 && ( (this.getForceTypeId()==castOther.getForceTypeId()) || ( this.getForceTypeId()!=null && castOther.getForceTypeId()!=null && this.getForceTypeId().equals(castOther.getForceTypeId()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getRepId() == null ? 0 : this.getRepId().hashCode() );
         result = 37 * result + ( getForceTypeId() == null ? 0 : this.getForceTypeId().hashCode() );
         return result;
   }   





}