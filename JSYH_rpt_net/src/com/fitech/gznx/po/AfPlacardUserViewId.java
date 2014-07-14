package com.fitech.gznx.po;



/**
 * AfPlacardUserViewId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfPlacardUserViewId implements java.io.Serializable {


    // Fields    

     private AfPlacard placard;
     private String orgId;


    // Constructors

    /** default constructor */
    public AfPlacardUserViewId() {
    }

    

   
    // Property accessors

    public AfPlacard getPlacard() {
        return this.placard;
    }
    
    public void setPlacard(AfPlacard placard) {
        this.placard = placard;
    }
  



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof AfPlacardUserViewId) ) return false;
		 AfPlacardUserViewId castOther = ( AfPlacardUserViewId ) other; 
         
		 return ( (this.getPlacard()==castOther.getPlacard()) || ( this.getPlacard()!=null && castOther.getPlacard()!=null && this.getPlacard().equals(castOther.getPlacard()) ) )
 && ( (this.getOrgId()==castOther.getOrgId()) || ( this.getOrgId()!=null && castOther.getOrgId()!=null && this.getOrgId().equals(castOther.getOrgId()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getPlacard() == null ? 0 : this.getPlacard().hashCode() );
         result = 37 * result + ( getOrgId() == null ? 0 : this.getOrgId().hashCode() );
         return result;
   }




public String getOrgId() {
	return orgId;
}




public void setOrgId(String orgId) {
	this.orgId = orgId;
}   





}