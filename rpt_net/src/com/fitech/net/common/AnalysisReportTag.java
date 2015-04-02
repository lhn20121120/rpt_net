package com.fitech.net.common;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.fitech.net.form.AAnalysisTPForm;

/**
 * 报表分析的表单
 * 
 * @author wh
 * 
 */
public class AnalysisReportTag extends TagSupport
{
	/**
	 * 要分析报表的列表
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
										"<input type='button' class='input-button' onClick=\"_viewReportData('<bean:write name="+aTPForm.getAnalyTypeName()+" property="+aTPForm.getATId()+" />')\" value='查 看'/>" +
										"</td></TR>" );
								flag=false;
							}else if(typeId.intValue()==key.intValue()  && !flag){
								sb.append("<TR bgcolor='#FFFFFF'>" +
										"<td align='center'></td>" +
										"<td align='center'>"+aTPForm.getATId()+"</td>" +
										"<td align='center'>"+aTPForm.getATName()+"</td>" +
										"<td align='center'>" +
										"<input type='button' class='input-button' onClick=\"_viewReportData('<bean:write name="+aTPForm.getAnalyTypeName()+" property="+aTPForm.getATId()+" />')\" value='查 看'/>" +
										"</td></TR>" );								
							}
							
	
						}		
					}

				}else{
				sb.append("	<tr align='left'><td bgcolor='#ffffff'><span style='font-size:9.0pt;font-family:宋体;mso-bidi-font-family:宋体;" +
									"mso-font-kerning:0pt'>暂无分析报表</span></td></tr>");
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
