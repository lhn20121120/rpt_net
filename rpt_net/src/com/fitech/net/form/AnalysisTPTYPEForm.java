package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class AnalysisTPTYPEForm extends ActionForm {

	 /** ģ������ID */
	 private Integer AnalyTypeID;

   /** ģ���������� */
   private String AnalyTypeName;

public Integer getAnalyTypeID()
{
	return AnalyTypeID;
}

public void setAnalyTypeID(Integer analyTypeID)
{
	AnalyTypeID = analyTypeID;
}

public String getAnalyTypeName()
{
	return AnalyTypeName;
}

public void setAnalyTypeName(String analyTypeName)
{
	AnalyTypeName = analyTypeName;
}
 

}
