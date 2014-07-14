package com.gather.struts.forms;
public class SelectForm {
     private String values[] =
	{ "Magazine", "Journal", "News Paper","Other" };

	private String labels[] =
	{ "L-Magazine", "L-Journal", "L-News Paper","L-Other"};

	private String selected = "Magazine";

	public String getSelected(){
	return selected;
	}
	public void setSelected(String selected){
	this.selected = selected;
	}


	public String[] getValues(){
	return values;
	}
	public void setValues(String[] values){
	this.values = values;
	}

	public String[] getLabels(){
	return values;
	}
	public void setLabels(String[] labels){
	this.labels = labels;
	}
}
