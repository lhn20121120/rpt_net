package com.fitech.net.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.fitech.net.adapter.StrutsVParameterDelegate;

/**
 * 
 * @author James
 * 
 */
public class UtilParameterForm {
	/**
	 * ²ÎÊý¼ÇÂ¼
	 */
	private List params = null;

	
	public List getParams() {
		if(params == null){
			List paramList = new ArrayList();
			try{
				List list = StrutsVParameterDelegate.findAll();
				if(list != null && list.size() > 0){
					for(int i=0;i<list.size();i++){
						VParameterForm vParamForm = null;
						vParamForm = (VParameterForm)list.get(i);
						paramList.add(new LabelValueBean(vParamForm.getVpTabledesc()+" - "+vParamForm.getVpColumndesc(), vParamForm.getVpId().toString()));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				paramList = new ArrayList();
			}
			return paramList;
		}
		else 
			return params;
	}

	public void setParams(List params) {
		this.params = params;
	}
}
