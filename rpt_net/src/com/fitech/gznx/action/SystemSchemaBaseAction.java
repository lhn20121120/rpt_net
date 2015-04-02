package com.fitech.gznx.action;

import org.apache.struts.action.Action;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;

public class SystemSchemaBaseAction extends Action{
	/**
	 * 系统模式标志
	 */
	private int schemaFlag = Config.SYSTEM_SCHEMA_FLAG;
	
	public int getSchemaFlag() {
		return schemaFlag;
	}

	public void setSchemaFlag(int schemaFlag) {
		this.schemaFlag = schemaFlag;
	}

	private static FitechException log = new FitechException(
			SystemSchemaBaseAction.class);
}
