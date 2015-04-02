package com.cbrc.smis.hibernate;

import java.io.Serializable;

public class EEXChangeRate  implements Serializable{
	EEXChangeRatePK comp_id;
	Double eeramt;
	public EEXChangeRate(){
		
	}
    public EEXChangeRate(EEXChangeRatePK pk){
		this.comp_id=comp_id;
	}
	public Double getEeramt() {
		return eeramt;
	}
	public void setEeramt(Double eeramt) {
		this.eeramt = eeramt;
	}
	public EEXChangeRatePK getComp_id() {
		return comp_id;
	}
	public void setComp_id(EEXChangeRatePK comp_id) {
		this.comp_id = comp_id;
	}

}
