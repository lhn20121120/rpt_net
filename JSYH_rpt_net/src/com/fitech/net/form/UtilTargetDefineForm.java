package com.fitech.net.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.fitech.net.adapter.StrutsTargetDefineDelegate;
public class UtilTargetDefineForm
{
	List normalList=null;
	List businessList=null;
	public List getBusinessList() {
		
		if(businessList==null)
		{
			List tempList=new ArrayList();
			try
			{
				List list=StrutsTargetDefineDelegate.findAllBusiness();
				if(list!=null && list.size()>0)
				{
					for(int i=0;i<list.size();i++)
					{
						TargetBusinessForm form=null;
						form=(TargetBusinessForm)list.get(i);
						tempList.add(new LabelValueBean(form.getBusinessName(),form.getBusinessId().toString()));
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				tempList=new ArrayList();
			}
			return tempList;
		}else return businessList;
		
		
	}
	public void setBusinessList(List businessList) {
		this.businessList = businessList;
	}
	public List getNormalList() {
		
		if(normalList==null)
		{
			List tempList=new ArrayList();
			try
			{
				List list=StrutsTargetDefineDelegate.findAllNormal();
				if(list!=null && list.size()>0)
				{
					for(int i=0;i<list.size();i++)
					{
						TargetNormalForm form=null;
						form=(TargetNormalForm)list.get(i);
						tempList.add(new LabelValueBean(form.getNormalName(),form.getNormalId().toString()));
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				tempList=new ArrayList();
			}
			return tempList;
		}else return normalList;
		
		
		
	}
	public void setNormalList(List normalList) {
		this.normalList = normalList;
	}
	
	
	
}