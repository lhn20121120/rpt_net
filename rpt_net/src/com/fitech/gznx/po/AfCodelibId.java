package com.fitech.gznx.po;


public class AfCodelibId   implements java.io.Serializable {


    // Fields    

	private Long codeId;
	private Long codeType;



    // Constructors

    /** default constructor */
    public AfCodelibId() {
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof AfCodelibId) ) return false;
		 AfCodelibId castOther = ( AfCodelibId ) other; 
         
		 return ( (this.getCodeType()==castOther.getCodeType()) || ( this.getCodeType()!=null && castOther.getCodeType()!=null && this.getCodeType().equals(castOther.getCodeType()) ) )
 && ( (this.getCodeId()==castOther.getCodeId()) || ( this.getCodeId()!=null && castOther.getCodeId()!=null && this.getCodeId().equals(castOther.getCodeId()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getCodeType() == null ? 0 : this.getCodeType().hashCode() );
         result = 37 * result + ( getCodeId() == null ? 0 : this.getCodeId().hashCode() );
         
         return result;
   }

	public Long getCodeId() {
		return codeId;
	}
	
	public void setCodeId(Long codeId) {
		this.codeId = codeId;
	}
	
	public Long getCodeType() {
		return codeType;
	}
	public void setCodeType(Long codeType) {
		this.codeType = codeType;
	}   
}
