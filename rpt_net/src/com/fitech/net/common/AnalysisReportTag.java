package com.fitech.net.common;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.fitech.net.form.AAnalysisTPForm;

/**
 * ��������ı�
 * 
 * @author wh
 * 
 */
public class AnalysisReportTag extends TagSupport
{
	/**
	 * Ҫ����������б�
	 * wh
	 */
	protected List reportList=null;
	public List getReportList() {
		return reportList;
	}
	public void setReportList(List reportList) {
		this.reportList = reportList;
	}
	public int doStartTag() throws JspTagException
	{
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspTagException
	{
		try
		{
			StringBuffer sb = new StringBuffer("");
			HashSet hashSet = new HashSet();
				
				
	
				if (reportList!= null && reportList.size()>0 ){
					for(Iterator iterator=reportList.iterator();iterator.hasNext();){
						AAnalysisTPForm aTPForm =(AAnalysisTPForm)iterator.next();
						hashSet.add(aTPForm.getAnalyTypeID());
					}	
					for (Iterator it = hashSet.iterator(); it.hasNext();){
						Integer key=(Integer)it.next();
						boolean flag = true;
						for(Iterator iter=reportList.iterator();iter.hasNext();){
							AAnalysisTPForm aTPForm =(AAnalysisTPForm)iter.next();
							Integer typeId=aTPForm.getAnalyTypeID();
							if(typeId.intValue()==key.intValue() && flag){
								sb.append("<TR bgcolor='#FFFFFF'>" +
										"<td align='center' rowspan=2 >"+aTPForm.getAnalyTypeName()+"</td>" +
										"<td align='center'>"+aTPForm.getATId()+"</td>" +
										"<td align='center'>"+aTPForm.getATName()+"</td>" +
										"<td align='center'>" +
										"<input type='button' class='input-button' onClick=\"_viewReportData('<bean:write name="+aTPForm.getAnalyTypeName()+" property="+aTPForm.getATId()+" />')\" value='�� ��'/>" +
										"</td></TR>" );
								flag=false;
							}else if(typeId.intValue()==key.intValue()  && !flag){
								sb.append("<TR bgcolor='#FFFFFF'>" +
										"<td align='center'></td>" +
										"<td align='center'>"+aTPForm.getATId()+"</td>" +
										"<td align='center'>"+aTPForm.getATName()+"</td>" +
										"<td align='center'>" +
										"<input type='button' class='input-button' onClick=\"_viewReportData('<bean:write name="+aTPForm.getAnalyTypeName()+" property="+aTPForm.getATId()+" />')\" value='�� ��'/>" +
										"</td></TR>" );								
							}
							
	
						}		
					}

				}else{
				sb.append("	<tr align='left'><td bgcolor='#ffffff'><span style='font-size:9.0pt;font-family:����;mso-bidi-font-family:����;" +
									"mso-font-kerning:0pt'>���޷�������</span></td></tr>");
				}
			
			pageContext.getOut().print(sb.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	
}
