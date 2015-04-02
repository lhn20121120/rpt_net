package com.fitech.net.regiontyp.form;



import org.apache.struts.action.ActionForm;

public class RegionTypNetForm  extends ActionForm {

		   

		   private java.lang.String  regionTypId = null;
		   private java.lang.String  regionTypNm = null;
		   
	
		public String getRegionTypId() {
			return regionTypId;
		}
		public void setRegionTypId(String regionTypId) {
			this.regionTypId = regionTypId;
		}
		public String getRegionTypNm() {
			return regionTypNm;
		}
		public void setRegionTypNm(String regionTypNm) {
			this.regionTypNm = regionTypNm;
		}

	}


