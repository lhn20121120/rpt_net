package com.cbrc.auth.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.fitech.net.adapter.StrutsOrgTypeDelegate;
import com.fitech.net.form.OrgTypeForm;

public class ToolMenuUtil {
    /**
     * 机构的类型
     */
    private List toolLevels  =null;

	public List getToolLevels() {
		ArrayList lists =  new ArrayList();
        
        List results = null;
        try {
            results = StrutsOrgTypeDelegate.findAll();
            if (results!=null) {
                for (int i=0; i<results.size(); i++) {
                	OrgTypeForm orgForm = (OrgTypeForm)(results.get(i));
                    lists.add(new LabelValueBean(orgForm.getOrg_type_name().toString(),orgForm.getOrg_type_id().toString()));
                }
            }
        } catch (Exception e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return lists;
	}
	public void setToolLevels(List toolLevels) {
		this.toolLevels = toolLevels;
	} 


}
