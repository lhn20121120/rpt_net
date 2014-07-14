package com.fitech.net.region.form;

import java.text.SimpleDateFormat;

import org.apache.struts.action.ActionForm;



public class RegionForm extends ActionForm {
    private final static SimpleDateFormat FORMAT = new SimpleDateFormat(
    "dd.MM.yyyy hh:mm:ss");

	  

	   private String  regionId = null;
	   private String  regionTypId = null;
	   private String  regionName = null;

	   /**
	    * Standard constructor.
	    */
	   public RegionForm() {
	   }

	public String getRegionId() {
		return regionId;
	}



	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}



	public String getRegionName() {
		return regionName;
	}



	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}



	public String getRegionTypId() {
		return regionTypId;
	}



	public void setRegionTypId(String regionTypId) {
		this.regionTypId = regionTypId;
	}

	}