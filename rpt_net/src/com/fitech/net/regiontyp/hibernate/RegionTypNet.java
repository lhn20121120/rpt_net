package com.fitech.net.regiontyp.hibernate;

import java.io.Serializable;



public class RegionTypNet implements Serializable {

	    /** identifier field */
	    private String regionTypId;

	    /** nullable persistent field */
	    private String regionTypNm;

	    /** full constructor */
	    public RegionTypNet(String regionTypId, String regionTypNm) {
	        this.regionTypId = regionTypId;
	        this.regionTypNm = regionTypNm;
	    }

	    /** default constructor */
	    public RegionTypNet() {
	    }

	    /** minimal constructor */
	    public RegionTypNet(String regionTypId) {
	        this.regionTypId = regionTypId;
	    }

	    public String getRegionTypId() {
	        return this.regionTypId;
	    }

	    public void setRegionTypId(String regionTypId) {
	        this.regionTypId = regionTypId;
	    }

	    public String getRegionTypNm() {
	        return this.regionTypNm;
	    }

	    public void setRegionTypNm(String regionTypNm) {
	        this.regionTypNm = regionTypNm;
	    }

	 

	   

	}

