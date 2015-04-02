package com.fitech.institution.form;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.cbrc.smis.hibernate.SysParameter;



public class SysParameterForm extends ActionForm
{
	public Integer parValue;

	public String description;
	
	public String parType;

	public String parName;

	public List<SysParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<SysParameter> parameters) {
		this.parameters = parameters;
	}

	public List <SysParameter> parameters;
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getParName()
	{
		return parName;
	}

	public void setParName(String parName)
	{
		this.parName = parName;
	}

	public String getParType()
	{
		return parType;
	}

	public void setParType(String parType)
	{
		this.parType = parType;
	}

	public Integer getParValue()
	{
		return parValue;
	}

	public void setParValue(Integer parValue)
	{
		this.parValue = parValue;
	}
	
}


