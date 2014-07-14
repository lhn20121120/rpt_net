package com.fitech.net.orglayer.hibernate;

import java.io.Serializable;



public class OrgLayer implements Serializable {

	    /** identifier field */
	    private String orgLayerId;

	    /** nullable persistent field */
	    private String orgLayer;

	    /** full constructor */
	    public OrgLayer(String regionTypId, String regionTypNm) {
	        this.orgLayerId = regionTypId;
	        this.orgLayer = regionTypNm;
	    }

	    /** default constructor */
	    public OrgLayer() {
	    }

		public String getOrgLayer() {
			return orgLayer;
		}

		public void setOrgLayer(String orgLayer) {
			this.orgLayer = orgLayer;
		}

		public String getOrgLayerId() {
			return orgLayerId;
		}

		public void setOrgLayerId(String orgLayerId) {
			this.orgLayerId = orgLayerId;
		}   

	}
