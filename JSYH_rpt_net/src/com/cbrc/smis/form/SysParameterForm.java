package com.cbrc.smis.form;

import org.apache.struts.action.ActionForm;

public class SysParameterForm extends ActionForm
{
	public Integer parValue;

	public String description;
	
	public String parType;

	public String parName;

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


